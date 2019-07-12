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
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ForkJoinPool;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.cherokeelessons.com.scraper.phoenix.db.CacheDao;

public class Application extends Thread {
	private static final File PDF_CACHE = new File("/var/tmp/CherokeePhoenix-pdfCache");
	private static final File HTML_OUTPUT_REPORT = new File("results/ᏣᎳᎩ ᏧᎴᎯᏌᏅᎯ --- ᏣᎳᎩ-ᏲᏁᎦ ᏗᎪᏪᎵ - ARTICLES.html");
	private static final File PDF_OUTPUT_REPORT = new File("results/ᏣᎳᎩ ᏧᎴᎯᏌᏅᎯ --- ᏣᎳᎩ-ᏲᏁᎦ ᏗᎪᏪᎵ - PDFS.html");
	final private long seconds = 1000;
	final private long minutes = 60 * seconds;
	final private long hours = 60 * minutes;

	private final long timeLimit = 4 * hours; // in ms

	private static final String BASE_URL = "https://www.cherokeephoenix.org";
	private static final String ARTICLE_PATH_A = "/Article/index/";
	private static final String ARTICLE_PATH_B = "/Article/Index/";

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
		CacheDao dao = CacheDao.Instance.getCacheDao();
		List<String> urlList;
		urlList = new ArrayList<>(loadSeedUrls());
		dao.resetMaybeBadScrapes();
		urlList.addAll(dao.forRescraping());
		performHarvest(urlList);

		urlList = dao.getUrls();

		System.err.println("========================================================");
		System.out.println("Processing " + urlList.size() + " cached articles.");
		System.err.println("========================================================");

		extractDataFromHtml(urlList);
		System.err.println("Processing complete at " + new Date());
		Desktop.getDesktop().open(HTML_OUTPUT_REPORT.getParentFile());
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
	}

	private void extractDataFromHtml(List<String> urlList) throws IOException {

		CacheDao dao = CacheDao.Instance.getCacheDao();

		System.err.println("PROCESSING HTML START: " + new Date());
		List<Article> listOfAllArticles = new ArrayList<>();
		Set<String> pdfLinks = new TreeSet<>();
		for (String articleUri : urlList) {
			String html = dao.getHtml(articleUri);
			if (html == null) {
				continue;
			}
			Article newArticle = new Article();
			newArticle.setUri(articleUri);
			newArticle.setHtml(html);
			listOfAllArticles.add(newArticle);
			if (newArticle.isPdf()) {
				pdfLinks.addAll(newArticle.getPdfLinks());
			}
		}
		List<Article> listOfCherokeeArticles = new ArrayList<>(listOfAllArticles);
		listOfCherokeeArticles.removeIf(a -> !a.isCherokee());

		Collections.sort(listOfCherokeeArticles);
		Collections.reverse(listOfCherokeeArticles);

		dao.insertPdfUrl(pdfLinks);
		downloadNew(pdfLinks);

		System.out.println("Downloading done.");
		System.out.println("Looking for PDFs with Cherokee Language Articles");
		updateCherokeeLanguagePdfLinks(dao.getUnknownPdfUrls());

		System.out.println();

		saveCorpusAllDocuments(listOfCherokeeArticles);
		saveCorpusMoses(listOfCherokeeArticles);

		List<String> chrLinks = dao.getCherokeePdfUrls();
		System.out.println("Found " + listOfCherokeeArticles.size() + " Cherokee language articles.");
		saveArticleLinksDocument(listOfCherokeeArticles);

		System.out.println("Found " + chrLinks.size() + " PDFs with Cherokee Language Articles");
		savePdfLinksDocument(listOfAllArticles, chrLinks);
	}

	private void saveCorpusMoses(List<Article> listOfArticles) throws IOException {
		StringBuilder moses_en = new StringBuilder();
		StringBuilder moses_chr = new StringBuilder();

		for (Article article : listOfArticles) {
			moses_en.append("--- ---\n");
			moses_chr.append("--- ---\n");
			if (article.getTitle_chr().length() > 0 && article.getTitle_en().length() > 0) {
				moses_en.append(article.getTitle_en().replaceAll("\n+", " --- "));
				moses_chr.append(article.getTitle_chr().replaceAll("\n+", " --- "));
				moses_en.append("\n");
				moses_chr.append("\n");
			}
			if (article.getDate().length() > 0) {
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
	}

	private void saveCorpusAllDocuments(List<Article> listOfArticles) throws IOException {
		StringBuilder corpus1 = new StringBuilder();
		for (Article article : listOfArticles) {
			corpus1.append("=========================================\n");
			if (article.getTitle_chr().length() > 0) {
				corpus1.append(article.getTitle_chr());
				corpus1.append("\n");
			}
			if (article.getTitle_en().length() > 0) {
				corpus1.append(article.getTitle_en());
				corpus1.append("\n");
			}
			corpus1.append("=========================================");
			corpus1.append("\n");
			corpus1.append(article.getUri());
			corpus1.append("\n");
			if (article.getDate().length() > 0) {
				corpus1.append("Date: " + article.getDate());
				corpus1.append("\n");
			}
			corpus1.append(article.getArticle_dual());
			corpus1.append("\n");
		}
		FileUtils.write(new File("results/allArticles.txt"), corpus1.toString(), Charset.forName("UTF-8"));
	}

	private void saveArticleLinksDocument(List<Article> listOfArticles) throws IOException {
		List<String> lines = new ArrayList<>();
		System.out.println("Saving URLS as an STEEMIT READY HTML document w/titles.");
		lines.add("<html>");
		String dateStr = new java.sql.Date(System.currentTimeMillis()).toString();
		lines.add("<h2>ᏣᎳᎩ ᏧᎴᎯᏌᏅᎯ | ᏣᎳᎩ-ᏲᏁᎦ ᏗᎪᏪᎵ - Articles - " + dateStr + "</h2>");
		lines.add("<h3>Cherokee Phoenix | Cherokee-English Articles - Articles - " + dateStr + "</h3>");
		lines.add("<p>A total of " + NumberFormat.getInstance().format(listOfArticles.size())
				+ " dual-language articles were found.");
		lines.add("</p>");
		lines.add("<p>");
		lines.add(NumberFormat.getInstance().format(listOfArticles.stream().mapToInt(a -> a.hasAudio() ? 1 : 0).sum()));
		lines.add(" articles have links to audio. <em>Some audio files may be missing.</em>");
		lines.add("</p>");
		lines.add("<p>This list was generated using a custom scraping program written in Java.</p>");
		lines.add("<p>GIT HUB LINK: [" //
				+ "<a href='https://github.com/CherokeeLanguage/Scraper-CherokeePhoenix'>" //
				+ "CherokeeLanguage/Scraper-CherokeePhoenix" //
				+ "</a>]</p>");
		lines.add("<ol>");
		String d;
		String articleId;
		for (Article article : listOfArticles) {
			String title = article.getTitle_chr();
			if (StringUtils.isBlank(title)) {
				title = article.getTitle_en();
			}
			if (article.getDate().length() > 1) {
				d = " (" + article.getDate() + ")";
			} else {
				d = "";
			}
			articleId = " [" + article.getArticleId() + "]";

			lines.add("<li><a href=\"" + article.getUri() + "\">");
			lines.add(title);
			lines.add("</a>");
			if (article.hasAudio()) {
				lines.add(" [<a href='" + article.getAudioUrl() + "'><strong>AUDIO</strong>" + "</a>]");
			}

			lines.add(d + articleId);
			lines.add("</li>");
		}
		lines.add("</ol>");
		lines.add("</html>");
		FileUtils.writeLines(HTML_OUTPUT_REPORT, "UTF-8", lines);
	}

	private void savePdfLinksDocument(List<Article> listOfAllArticles, List<String> chrLinks) throws IOException {
		List<Article> listOfChrPdfArticles = new ArrayList<>(listOfAllArticles);
		listOfChrPdfArticles.removeIf((a) -> {
			Set<String> pdfLinks = new TreeSet<>(a.getPdfLinks());
			pdfLinks.retainAll(chrLinks);
			return pdfLinks.isEmpty();
		});
		Collections.sort(listOfChrPdfArticles);
		Collections.reverse(listOfChrPdfArticles);

		List<String> lines = new ArrayList<>();
		System.out.println("Saving PDF URLS as an STEEMIT READY HTML document w/titles.");
		lines.add("<html>");
		String dateStr = new java.sql.Date(System.currentTimeMillis()).toString();
		lines.add("<h2>ᏣᎳᎩ ᏧᎴᎯᏌᏅᎯ | ᏣᎳᎩ-ᏲᏁᎦ ᏗᎪᏪᎵ - PDFs - " + dateStr + "</h2>");
		lines.add("<h3>Cherokee Phoenix | Cherokee-English Articles - PDFs - " + dateStr + "</h3>");
		lines.add("<p>A total of " + NumberFormat.getInstance().format(chrLinks.size())
				+ " dual-language PDFs were found.");
		lines.add("</p>");
		lines.add("<p>This list was generated using a custom scraping program written in Java.</p>");
		lines.add("<p>GIT HUB LINK: [" //
				+ "<a href='https://github.com/CherokeeLanguage/Scraper-CherokeePhoenix'>" //
				+ "CherokeeLanguage/Scraper-CherokeePhoenix" //
				+ "</a>]</p>");
		lines.add("<ol>");
		String d;
		String articleId;
		for (Article article : listOfChrPdfArticles) {
			String title = article.getTitle_chr();
			if (StringUtils.isBlank(title)) {
				title = article.getTitle_en();
			}
			if (article.getDate().length() > 1) {
				d = " (" + article.getDate() + ")";
			} else {
				d = "";
			}
			articleId = " [" + article.getArticleId() + "]";

			lines.add("<li><a href=\"" + article.getUri() + "\">");
			lines.add(title);
			lines.add("</a>");
			lines.add(d + articleId);
			lines.add("<ul>");

			for (String pdfLink : article.getPdfLinks()) {
				String name = StringUtils.substringAfterLast(pdfLink, "/");
				if (pdfLink.contains(" ")) {
					pdfLink = pdfLink.replace(" ", "%20");
				}
				lines.add("<li>PDF: ");
				lines.add("<a target='_blank' href='" + pdfLink + "'>" + StringEscapeUtils.escapeHtml4(name) + "</a>");
				lines.add("</li>");
			}

			lines.add("</ul>");
			lines.add("</li>");
		}
		lines.add("</ol>");
		lines.add("</html>");
		FileUtils.writeLines(PDF_OUTPUT_REPORT, "UTF-8", lines);
	}

	@SuppressWarnings("unused")
	private Map<String, Set<Article>> getPdfUrlArticleMap(List<Article> listOfArticles) {
		Map<String, Set<Article>> pdfUrlsToArticles = new HashMap<>();
		for (Article article : listOfArticles) {
			if (!article.isPdf()) {
				continue;
			}
			for (String pdfUrl : article.getPdfLinks()) {
				if (!pdfUrlsToArticles.containsKey(pdfUrl)) {
					pdfUrlsToArticles.put(pdfUrl, new TreeSet<>());
				}
				pdfUrlsToArticles.get(pdfUrl).add(article);
			}
		}
		return pdfUrlsToArticles;
	}

	private void updateCherokeeLanguagePdfLinks(Collection<String> pdfLinks) {
		CacheDao dao = CacheDao.Instance.getCacheDao();
		ForkJoinPool pool = new ForkJoinPool();
		Iterator<String> iPdfs = pdfLinks.iterator();
		while (iPdfs.hasNext()) {
			String pdfLink = iPdfs.next();
			File localPdf = getLocalPdfFile(pdfLink);
			if (!localPdf.exists()) {
				continue;
			}
			pool.submit(() -> {
				dao.updatePdfUrl(pdfLink, isCherokeeLanguagePdf(localPdf));
			});
		}
		pool.shutdown();
		while (!pool.isTerminated()) {
			sleep(50);
		}
		return;
	}

	private int debugCounter = 0;

	private boolean isCherokeeLanguagePdf(File localPdf) {
		File debug = new File(PDF_CACHE, "debug");
		String debugIdx = String.valueOf(debugCounter++);
		while (debugIdx.length() < 4) {
			debugIdx = "0" + debugIdx;
		}
		File debugFileTxt = new File(debug, debugIdx + "-full.txt");
		File debugFileChr = new File(debug, debugIdx + "-chr.txt");
		try (PDDocument pdf = PDDocument.load(localPdf)) {
			PDFTextStripper parser = new PDFTextStripper();
			StringWriter output = new StringWriter();
			parser.writeText(pdf, output);
			// Document pdfHtml = Jsoup.parse(output.toString());
			// String text = pdfHtml.text();
			String text = output.toString();
			// strip tags
			text = text.replaceAll("<[^<>]*>", "");
			// text = StringEscapeUtils.unescapeHtml4(text);
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
			// news
			text = text.replace("ᏗᎦᏃᏣᎸᏍᎩ", "");
			// opinion
			text = text.replace("ᏃᎵᏍᎬ", "");
			// community
			text = text.replace("ᎾᎥ ᏄᎾᏓᎸ", "");
			// health
			text = text.replace("ᎠᏰᎸ ᏄᏍᏛ", "");
			// sevices
			text = text.replace("ᎾᎾᏛᏁᎲ", "");
			// education
			text = text.replace("ᏧᎾᏕᎶᏆᏍᏗ", "");
			// money
			text = text.replace("ᎠᏕᎳ", "");
			// people
			text = text.replace("ᏴᏫ", "");
			// culture
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
				FileUtils.write(debugFileTxt, output.toString(), StandardCharsets.UTF_8);
				FileUtils.write(debugFileChr, text, StandardCharsets.UTF_8);
			}
			int spaceCount = StringUtils.countMatches(text.trim(), " ");
			int wordCount = spaceCount > 0 ? spaceCount + 1 : 0;
			boolean hasEnoughCherokeeWords = wordCount > 5;
			System.out.println("\t" + localPdf.getName() + " " + hasEnoughCherokeeWords + " [" + wordCount + " words]");
			return hasEnoughCherokeeWords;
		} catch (IOException e) {
			System.out.println("\t" + localPdf.getName() + " " + false);
			System.err.println(e.getMessage());
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
		System.out.println("Have " + _pdfLinks.size() + " PDFs to download.");
		iPdfs = _pdfLinks.iterator();
		while (iPdfs.hasNext()) {
			String pdfLink = iPdfs.next();
			URL pdfUrl;
			URLConnection conn;
			try {
				pdfLink = pdfLink.replace(" ", "%20");
				pdfUrl = new URL(pdfLink);
				conn = pdfUrl.openConnection();
				conn.setConnectTimeout(10000);
				conn.setReadTimeout(30000);
			} catch (Exception e) {
				sleep(100);
				continue;
			}
			try (InputStream is = conn.getInputStream()) {
				File localPdf = getLocalPdfFile(pdfLink);
				File tmpFile = new File(FileUtils.getTempDirectory(), "temp.pdf");
				FileUtils.copyInputStreamToFile(is, tmpFile);
				FileUtils.moveFile(tmpFile, localPdf);
				System.out.println("\t" + pdfLink);
			} catch (IOException e) {
				FileUtils.deleteQuietly(getLocalPdfFile(pdfLink));
				sleep(100);
				continue;
			}
		}
	}

	private void performHarvest(List<String> urlList) {
		CacheDao dao = CacheDao.Instance.getCacheDao();
		Document details;
		long startTime = System.currentTimeMillis();

		int lastPercent = -1;
		int newPercent = 0;
		String filingUri;
		String html;

		System.out.println("STARTING FETCH OF " + urlList.size() + " URLS.");
		System.err.println("HARVEST START: " + new Date());
		// boolean newData=false;
		for (int ix = 0; ix < urlList.size(); ix++) {
			if (System.currentTimeMillis() - startTime > timeLimit) {
				// out of time for harvesting, go on to next steps
				System.out.println("HARVEST Time limit reached.");
				break;
			}
			details = null;
			filingUri = urlList.get(ix);
			html = dao.getHtml(filingUri);
			// newData=false;
			if (html == null) {
				for (int retries = 0; retries < 3; retries++) {
					try {
						while (httpRequestRateLimit>System.currentTimeMillis()) {
							sleep(100);
						}
						details = Jsoup.connect(filingUri).timeout(5000).get();
						httpRequestRateLimit = System.currentTimeMillis()+1000/4;
						dao.putHtml(filingUri, details.outerHtml());
						// newData=true;
						html = details.outerHtml();
						break;
					} catch (IOException e) {
						if (e.getMessage().startsWith("404 error")) {
							html = "";
							// newData=false;
							break;
						} else {
							System.err.println(e.getMessage());
							System.err.println("RETRY: " + filingUri);
							randomSleep();
						}
					}
				}
			}
			if (html == null || html.length() < 1) {
				// System.err.println("FAILED: "+filingUri);
				continue;
			}
			newPercent = 100 * ix / urlList.size();
			if (newPercent != lastPercent) {
				System.out.println("HARVEST " + newPercent + "% complete.");
				lastPercent = newPercent;
				// if (newData) randomSleep();
			}
		}

		// flush db to disk then reload
		System.err.println("HARVEST STOP: " + new Date());
	}

	/**
	 * {@link http://stackoverflow.com/questions/4596447/check-if-file-exists-on-remote-server-using-its-url}
	 * 
	 * @param URLName
	 * @return
	 */
	public static boolean httpExists(String URLName) {
		try {
			HttpURLConnection.setFollowRedirects(false);
			// note : you may also need
			// HttpURLConnection.setInstanceFollowRedirects(false)
			HttpURLConnection con = (HttpURLConnection) new URL(URLName).openConnection();
			con.setRequestMethod("HEAD");
			while (httpRequestRateLimit > System.currentTimeMillis()) {
				Thread.sleep(100);
			}
			boolean b = con.getResponseCode() == HttpURLConnection.HTTP_OK;
			httpRequestRateLimit = System.currentTimeMillis() + 1000/4;
			return b;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	private static long httpRequestRateLimit = 0;

	private Set<String> loadSeedUrls() throws MalformedURLException, IOException {
		System.err.println("Loading initial URLS: " + new Date());
		Set<String> urlList = new TreeSet<>();
		int articleId = 1;
		String queryURI;

		CacheDao dao = CacheDao.Instance.getCacheDao();
		articleId = dao.getMaxArticleId();
		int maxId = 0;
		Document indexPage = Jsoup.parse(new URL("http://www.cherokeephoenix.org/"), 30000);
		Elements alist = indexPage.select("a");
		if (alist != null) {
			for (Element a : alist) {
				if (!a.hasAttr("href")) {
					continue;
				}
				String lcHref = a.attr("href").toLowerCase();
				if (!lcHref.contains("article/index/")) {
					continue;
				}
				String number = StringUtils.substringAfterLast(lcHref, "article/index/");
				maxId = Math.max(Integer.valueOf(number), maxId);
			}
		}
		System.out.println(" - home page shows max id of: " + maxId);

		if (maxId == articleId) {
			System.out.println("\tUrl list seems up-to-date.");
			return urlList;
		}

		System.out.println(" - scanning for new valid article ids");
		do {
			articleId++;
			queryURI = BASE_URL + ARTICLE_PATH_A + String.valueOf(articleId);// +queryURIB;
			if (!httpExists(queryURI)) {
				queryURI = BASE_URL + ARTICLE_PATH_B + String.valueOf(articleId);// +queryURIB;
				if (!httpExists(queryURI)) {
					continue;
				}
			}
			urlList.add(queryURI);
			System.out.println(" - articleId: " + articleId);
		} while (articleId < maxId);

		System.out.println("CALCULATED URI LIST: (urls) " + urlList.size());
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
