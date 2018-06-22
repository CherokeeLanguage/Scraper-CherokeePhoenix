package com.cherokeelessons.com.scraper.phoenix;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ForkJoinPool;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.fit.pdfdom.PDFDomTree;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Application extends Thread {
	private static final File PDF_CACHE = new File("/var/tmp/CherokeePhoenix-pdfCache");
	private static final File HTML_OUTPUT_REPORT = new File("results/ᏣᎳᎩ ᏧᎴᎯᏌᏅᎯ --- ᏣᎳᎩ-ᏲᏁᎦ ᏗᎪᏪᎵ.html");
	final private long seconds = 1000;
	final private long minutes = 60 * seconds;
	final private long hours = 60 * minutes;

	private final long timeLimit = 4 * hours; // in ms

	private static final String BASE_URL = "http://www.cherokeephoenix.org";
	private static final String ARTICLE_PATH = "/Article/Index/";

	@Override
	public void run() {
		try {
			_run();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	private void _run() throws IOException {
		List<String> urlList;
		urlList = new ArrayList<>(loadSeedUrls());
		HtmlCache.open();
		HtmlCache.resetMaybeBadScrapes();
		urlList.addAll(HtmlCache.forRescraping());
		HtmlCache.close();
		performHarvest(urlList);
		
		HtmlCache.open();
		urlList=HtmlCache.allUrls();
		HtmlCache.close();
		
		System.err.println("========================================================");
		System.out.println("Processing "+urlList.size()+" cached articles.");
		System.err.println("========================================================");
		
		extractDataFromHtml(urlList);
		System.err.println("Processing complete at " + new Date());
		Desktop.getDesktop().open(HTML_OUTPUT_REPORT);
		Desktop.getDesktop().open(HTML_OUTPUT_REPORT.getParentFile());
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
	}

	private void extractDataFromHtml(List<String> urlList) throws IOException {
		
		HtmlCache.open();

		System.err.println("PROCESSING HTML START: " + new Date());
		int size=urlList.size();
		List<Article> listOfArticles=new ArrayList<>();
		Set<String> pdfLinks = new TreeSet<>();
		for (int ix = 0; ix<size; ix++) {
			String articleUri = urlList.get(ix);
			String html = HtmlCache.getHtml(articleUri);
			if (html == null) {
				continue;
			}
			Article newArticle = new Article();
			newArticle.setUri(articleUri);
			newArticle.setHtml(html);
			if (newArticle.isPdf()) {
				pdfLinks.addAll(newArticle.getPdfLinks());
			}
			if (!newArticle.isCherokee()){
				continue;
			}
			listOfArticles.add(newArticle);
		}
		HtmlCache.close();
		
		Collections.sort(listOfArticles);
		Collections.reverse(listOfArticles);
		
		System.out.println("Found "+listOfArticles.size()+" Cherokee language articles.");
		
		System.out.println("Found "+pdfLinks.size()+" PDF Links.");
		downloadNew(pdfLinks);
		System.out.println("Downloading done.");
		System.out.println("Looking for PDFs with Cherokee Language Articles");
		Set<String> chrLinks = getCherokeeLanguagePdfLinks(pdfLinks);
		System.out.println("Found "+chrLinks.size()+" PDFs with Cherokee Language Articles");
		
		System.err.println("PROCESSING HTML STOPPED: " + new Date());
		StringBuilder corpus1 = new StringBuilder();
		for (Article article: listOfArticles){
			corpus1.append("=========================================\n");
			if (article.getTitle_chr().length()>0) {
				corpus1.append(article.getTitle_chr());
				corpus1.append("\n");
			}
			if (article.getTitle_en().length()>0){
				corpus1.append(article.getTitle_en());
				corpus1.append("\n");
			}
			corpus1.append("=========================================");
			corpus1.append("\n");
			corpus1.append(article.getUri());
			corpus1.append("\n");
			if (article.getDate().length()>0) {
				corpus1.append("Date: "+article.getDate());
				corpus1.append("\n");
			}
			corpus1.append(article.getArticle_dual());
			corpus1.append("\n");
		}
		FileUtils.write(new File("results/allArticles.txt"), corpus1.toString(), Charset.forName("UTF-8"));
		
		StringBuilder moses_en=new StringBuilder();
		StringBuilder moses_chr=new StringBuilder();
		
		for (Article article: listOfArticles){
			moses_en.append("--- ---\n");
			moses_chr.append("--- ---\n");
			if (article.getTitle_chr().length()>0 && article.getTitle_en().length()>0) {
				moses_en.append(article.getTitle_en().replaceAll("\n+", " --- "));
				moses_chr.append(article.getTitle_chr().replaceAll("\n+", " --- "));
				moses_en.append("\n");
				moses_chr.append("\n");
			}
			if (article.getDate().length()>0) {
				moses_en.append(article.getDate().replaceAll("\n+", " --- "));
				moses_chr.append(article.getDate().replaceAll("\n+", " --- "));
				moses_en.append("\n");
				moses_chr.append("\n");
			}
			moses_en.append(article.getArticle_en().replaceAll("\n+", " --- "));
			moses_chr.append(article.getArticle_chr().replaceAll("\n+", " --- "));
			moses_en.append("\n\n");
			moses_chr.append("\n\n");
		}
		FileUtils.write(new File("results/phoenix.en"), moses_en.toString(), Charset.forName("UTF-8"));
		FileUtils.write(new File("results/phoenix.chr4"), moses_chr.toString(), Charset.forName("UTF-8"));
		
		List<String> lines=new ArrayList<String>();
		System.out.println("Saving URLS as an HTML document w/titles.");
		lines.add("<html><head>");
		lines.add("<meta charset=\"UTF-8\" lang=\"chr\" />");
		lines.add("<title>ᏣᎳᎩ ᏧᎴᎯᏌᏅᎯ | ᏣᎳᎩ-ᏲᏁᎦ ᏗᎪᏪᎵ</title>");
		lines.add("<style>");
		lines.add("@import url(http://fonts.googleapis.com/earlyaccess/notosanscherokee.css);");
		lines.add("body {font-family: 'Noto Sans Cherokee', sans-serif;}");
		lines.add("</style>");
		lines.add("</head>");
		lines.add("<body>");
		lines.add("<h2>ᏣᎳᎩ ᏧᎴᎯᏌᏅᎯ | ᏣᎳᎩ-ᏲᏁᎦ ᏗᎪᏪᎵ</h2>");
		lines.add("<h3>Cherokee Phoenix | Cherokee-English Articles</h3>");
		lines.add("<p>A total of "+NumberFormat.getInstance().format(listOfArticles.size())+" dual-language articles were found.");
		lines.add("</p>");
		lines.add("<p>");
		lines.add(NumberFormat.getInstance().format(listOfArticles.stream().mapToInt(a->a.hasAudio()?1:0).sum()));
		lines.add(" articles have links to audio. <em>Some audio files may be missing.</em>");
		lines.add("</p>");
		lines.add("<p>This list was generated by Michael Joyner on "+new Date()+" using a custom scraping program written in Java.</p>");
		lines.add("<ol>");
		String d;
		String articleId;
		for (Article article: listOfArticles) {
			String title = article.getTitle_chr();
			if (StringUtils.isBlank(title)) {
				title=article.getTitle_en();
			}
			if (article.getDate().length()>1)  {
				d=" ("+article.getDate()+")";
			} else { 
				d="";
			}
			articleId=" ["+article.getArticleId()+"]";
			
			lines.add("<li><a href=\""+article.getUri()+"\">");
			lines.add(title);
			lines.add("</a>");
			if (article.hasAudio()) {
				lines.add(" [<a href='"+article.getAudioUrl()+"'><strong>AUDIO</strong>"+"</a>]");
			}
			
			lines.add(d+articleId);
			lines.add("</li>");
		}
		lines.add("</ol>");
		lines.add("</body></html>");
		FileUtils.writeLines(HTML_OUTPUT_REPORT, "UTF-8", lines);
	}

	private Set<String> getCherokeeLanguagePdfLinks(Set<String> pdfLinks) {
		ForkJoinPool pool = new ForkJoinPool();
		Set<String> chrPdfLinks = Collections.synchronizedSet(new TreeSet<>());
		Iterator<String> iPdfs = pdfLinks.iterator();
		while (iPdfs.hasNext()) {
			String pdfLink = iPdfs.next();
			File localPdf = getLocalPdfFile(pdfLink);
			if (!localPdf.exists()) {
				continue;
			}
			pool.submit(()->{
				if (!isCherokeeLanguagePdf(localPdf)) {
					return;
				}
				chrPdfLinks.add(pdfLink);
			});
		}
		pool.shutdown();
		while (!pool.isTerminated()) {
			sleep(500);
		}
		return chrPdfLinks;
	}

	private int debugCounter = 0;
	private boolean isCherokeeLanguagePdf(File localPdf) {
		File debug = new File(PDF_CACHE, "debug");
		String debugIdx = String.valueOf(debugCounter++);
		while (debugIdx.length()<4) {
			debugIdx = "0" + debugIdx;
		}
		File debugFileHtml = new File(debug, debugIdx+"-full.html");
		File debugFileChr = new File(debug, debugIdx+"-chr.txt");
		try (PDDocument pdf = PDDocument.load(localPdf)) {
			PDFDomTree parser = new PDFDomTree();
			StringWriter output = new StringWriter();
			parser.writeText(pdf, output);
//			Document pdfHtml = Jsoup.parse(output.toString());
//			String text = pdfHtml.text();
			String text = output.toString();
			//strip tags
			text = text.replaceAll("<[^<>]*>", "");
			//text = StringEscapeUtils.unescapeHtml4(text);
			text = text.replaceAll("[^Ꭰ-Ᏼ\\s]", "");
			text = StringUtils.normalizeSpace(text);
			/*
			 * cherokee phoenix
			 */
			text = text.replace("ᏣᎳᎩ", "");
			text = text.replace("ᏧᎴᎯᏌᏅᎯ", "");
			/*
			 * sections, other
			 */
			//news
			text = text.replace("ᏗᎦᏃᏣᎸᏍᎩ", "");
			//opinion
			text = text.replace("ᏃᎵᏍᎬ", "");
			//community
			text = text.replace("ᎾᎥ ᏄᎾᏓᎸ", "");
			//health
			text = text.replace("ᎠᏰᎸ ᏄᏍᏛ", "");
			//sevices
			text = text.replace("ᎾᎾᏛᏁᎲ", "");
			//education
			text = text.replace("ᏧᎾᏕᎶᏆᏍᏗ", "");
			//money
			text = text.replace("ᎠᏕᎳ", "");
			//people
			text = text.replace("ᏴᏫ", "");
			//culture
			text = text.replace("ᎢᏳᎾᏛᏁᎵᏓᏍᏗ", "");
			
			/*
			 * months of year
			 */
			text = text.replace("ᎤᏃᎸᏔᏂ", "");
			text = text.replace("ᎧᎦᎵ", "");
			text = text.replace("ᎠᏅᏱ", "");
			text = text.replace("ᎧᏬᏂ", "");
			text = text.replace("ᎠᏂᏍᎬᏗ", "");
			text = text.replace("ᏕᎭᎷᏱ", "");
			text = text.replace("ᎫᏰᏉᏂ", "");
			text = text.replace("ᎦᎶᏂ", "");
			text = text.replace("ᏚᎵᎢᏍᏗ", "");
			text = text.replace("ᏚᏂᏂᏗ", "");
			text = text.replace("ᏅᏓᏕᏆ", "");
			text = text.replace("ᎥᏍᎩᏱ", "");
			
			if (!text.trim().isEmpty()) {
				FileUtils.copyFile(localPdf, new File(debug, localPdf.getName()));
				FileUtils.write(debugFileHtml, output.toString(), StandardCharsets.UTF_8);
				FileUtils.write(debugFileChr, text, StandardCharsets.UTF_8);
			}
			System.out.print(localPdf.getName()+" "+!text.trim().isEmpty());
			return !text.trim().isEmpty();
		} catch (IOException | ParserConfigurationException e) {
			System.out.print(localPdf.getName()+" "+false);
			return false;
		}
	}

	@SuppressWarnings("deprecation")
	private File getLocalPdfFile(String pdfLink) {
		pdfLink = StringUtils.substringAfter(pdfLink, "//");
		pdfLink = StringUtils.substringAfter(pdfLink, "/");
		while (pdfLink.startsWith("/")) {
			pdfLink = pdfLink.substring(1);
		}
		try {
			pdfLink = URLDecoder.decode(pdfLink, StandardCharsets.UTF_8.name());
		} catch (UnsupportedEncodingException e) {
			pdfLink = URLDecoder.decode(pdfLink);
		}
		File localPdf = new File(PDF_CACHE, pdfLink);
		return localPdf;
	}
	private void sleep(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
		}
	}	
	private void downloadNew(Set<String> pdfLinks) {
		Set<String> _pdfLinks = new TreeSet<>(pdfLinks);
		PDF_CACHE.mkdirs();
		Iterator<String> iPdfs = _pdfLinks.iterator();
		while (iPdfs.hasNext()) {
			String pdfLink = iPdfs.next();
			File localPdf = getLocalPdfFile(pdfLink);
			if (localPdf.exists()) {
				iPdfs.remove();
				continue;
			}
		}
		System.out.println("Have "+_pdfLinks.size()+" new PDFs to download.");
		iPdfs = _pdfLinks.iterator();
		while (iPdfs.hasNext()) {
			String pdfLink = iPdfs.next();
			URL pdfUrl;
			URLConnection conn;
			try {
				pdfUrl = new URL(pdfLink);
				conn = pdfUrl.openConnection();
				conn.setConnectTimeout(1000);
				conn.setReadTimeout(5000);
			} catch (Exception e) {
				System.err.println(e);
				sleep(250);
				continue;
			}
			try (InputStream is = conn.getInputStream()) {
				File localPdf = getLocalPdfFile(pdfLink);
				File tmpFile = new File(FileUtils.getTempDirectory(), "temp.pdf");
				FileUtils.copyInputStreamToFile(is, tmpFile);
				FileUtils.moveFile(tmpFile, localPdf);
				System.out.println("\t"+pdfLink);
			} catch (IOException e) {
				FileUtils.deleteQuietly(getLocalPdfFile(pdfLink));
				System.err.println(e);
				sleep(250);
				continue;
			}
		}
	}

	private void performHarvest(List<String> urlList) {
		HtmlCache.open();
		Document details;
		long startTime = System.currentTimeMillis();
		

		int lastPercent = -1;
		int newPercent = 0;
		String filingUri;
		String html;

		System.out.println("STARTING FETCH OF " + urlList.size() + " URLS.");
		System.err.println("HARVEST START: " + new Date());
//		boolean newData=false;
		for (int ix = 0; ix < urlList.size(); ix++) {
			if (System.currentTimeMillis() - startTime > timeLimit) {
				// out of time for harvesting, go on to next steps
				System.out.println("HARVEST Time limit reached.");
				break;
			}
			details = null;
			filingUri = urlList.get(ix);
			html = HtmlCache.getHtml(filingUri);
//			newData=false;
			if (html == null) {
				for (int retries = 0; retries < 3; retries++) {
					try {
						details = Jsoup.connect(filingUri).get();
						HtmlCache.putHtml(filingUri, details.outerHtml());
//						newData=true;
						html = details.outerHtml();
						break;
					} catch (IOException e) {
						if (e.getMessage().startsWith("404 error")){
							html="";
//							newData=false;
							break;
						} else {
							System.err.println(e.getMessage());
							System.err.println("RETRY: "+filingUri);
							randomSleep();
						}
					}
				}
			}
			if (html==null || html.length()<1) {
//				System.err.println("FAILED: "+filingUri);
				continue;
			}
			newPercent = (100 * ix / urlList.size());
			if (newPercent != lastPercent) {
				System.out.println("HARVEST " + newPercent + "% complete.");
				lastPercent = newPercent;
//				if (newData) randomSleep();
			}
		}

		// flush db to disk then reload
		System.err.println("HARVEST STOP: " + new Date());
		HtmlCache.close();
	}
	
	/**
	 * {@link http://stackoverflow.com/questions/4596447/check-if-file-exists-on-remote-server-using-its-url}
	 * @param URLName
	 * @return
	 */
	public static boolean httpExists(String URLName){
	    try {
	      HttpURLConnection.setFollowRedirects(false);
	      // note : you may also need
	      //        HttpURLConnection.setInstanceFollowRedirects(false)
	      HttpURLConnection con =
	         (HttpURLConnection) new URL(URLName).openConnection();
	      con.setRequestMethod("HEAD");
	      boolean b = con.getResponseCode() == HttpURLConnection.HTTP_OK;
//	      System.out.println("URLName: "+URLName+" ["+b+"]");
		return b;
	    }
	    catch (Exception e) {
	       e.printStackTrace();
	       return false;
	    }
	  }
	
	private Set<String> loadSeedUrls() throws MalformedURLException, IOException {
		System.err.println("Loading initial URLS: " + new Date());
		Set<String> urlList = new TreeSet<>();
		int articleId=1;
		String queryURI;
		
		HtmlCache.open();
		articleId=HtmlCache.getMaxArticleId();
		HtmlCache.close();
		int maxId=0;
		Document indexPage = Jsoup.parse(new URL("http://www.cherokeephoenix.org/"), 30000);
		Elements alist = indexPage.select("a");
		if (alist!=null) {
			for (Element a: alist) {
				if (!a.hasAttr("href")){
					continue;
				}
				String href=a.attr("href");
				if (!href.contains("Article/index/")){
					continue;
				}
				String number = StringUtils.substringAfterLast(href, "Article/index/");
				maxId=Math.max(Integer.valueOf(number), maxId);
			}
		}
		System.out.println(" - home page shows max id of: "+maxId);
		
		if (maxId==articleId) {
			System.out.println("\tUrl list seems up-to-date.");
			return urlList;
		}
		
		System.out.println(" - scanning for new valid article ids");
		do {
			articleId++;
			queryURI = BASE_URL+ARTICLE_PATH+String.valueOf(articleId);//+queryURIB;
			if (httpExists(queryURI)) {
				urlList.add(BASE_URL+ARTICLE_PATH+String.valueOf(articleId));//+queryURIB);
				System.out.println(" - articleId: "+articleId);
			}
		} while (articleId<maxId);

		System.out.println("CALCULATED URI LIST: (urls) "+urlList.size());
		return urlList;
	}
	
	

	private void randomSleep() {
		randomSleep(1000, 0);
	}

	private void randomSleep(int msecs, int minSleep) {
		Random r = new Random();
		int sleep;
		if (msecs < minSleep) {
			msecs = minSleep;
		}
		sleep = r.nextInt(msecs);
		if (sleep < minSleep) {
			sleep = minSleep;
		}
		try {
			Thread.sleep(minSleep);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
}

