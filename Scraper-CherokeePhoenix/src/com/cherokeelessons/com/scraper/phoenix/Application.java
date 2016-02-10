package com.cherokeelessons.com.scraper.phoenix;
//http://www.cherokeephoenix.org/Article/Index/6582?StartCherokee=1
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Application implements Runnable {
	final private long seconds = 1000;
	final private long minutes = 60 * seconds;
	final private long hours = 60 * minutes;

	private final long timeLimit = 4 * hours; // in ms

	private String baseURI = "http://www.cherokeephoenix.org";
	private String queryURIA = "/Article/Index/";

	private String[] args;
	private String destDir = "results";//set by cli option
	
	public Application(String[] args) {
		super();
		this.args = args;
	}

	@Override
	public void run() {
		ArrayList<String> urlList;
		ArrayList<Article> articles=new ArrayList<Article>();
		
//		urlList = loadSeedUrls();
//		performHarvest(urlList);	
		
		HtmlCache.open();
		urlList=HtmlCache.allUrls();
		HtmlCache.close();
		
		System.err.println("========================================================");
		System.out.println("Processing "+urlList.size()+" cached articles.");
		System.err.println("========================================================");
		
		extractDataFromHtml(urlList, articles);
		System.err.println("Processing complete at " + new Date());
//		System.exit(0);
	}

	private void extractDataFromHtml(ArrayList<String> urlList,
			ArrayList<Article> listOfArticles) {
		
		HtmlCache.open();

		System.err.println("PROCESSING HTML START: " + new Date());
		int size=urlList.size();
		
		for (int ix = 0; ix<size; ix++) {

			String articleUri = urlList.get(ix);
			String html = HtmlCache.getHtml(articleUri);
			if (html == null) {
				// skip missing content
				continue;
			}
			Article newArticle = new Article();
			newArticle.setHtml(html);
			if (!newArticle.isCherokee()){
				continue;
			}
			newArticle.setUri(articleUri);
			listOfArticles.add(newArticle);
			
			break;
		}
		HtmlCache.close();
		
		String saveFile="results/allArticles.txt";
		System.out.println("Found "+listOfArticles.size()+" Cherokee language articles.");
		System.err.println("PROCESSING HTML STOPPED: " + new Date());
		System.out.println("Saving to disk as '"+saveFile+"'");
		ArrayList<String> a=new ArrayList<String>();
		String[] aLines;
		for (int ix=0; ix<listOfArticles.size(); ix++){
			a.add("=========================================");
			Article b = listOfArticles.get(ix);
			if (b.getTitle_chr().length()>0) {
				a.add(b.getTitle_chr());
			} else {
				a.add("Unable to extract title.");
			}
			a.add("=========================================");
			a.add(b.getUri());
			if (b.getDate().length()>0) {
				a.add("Date: "+b.getDate());
			}
			String c=b.getArticle();
			aLines=c.split("\n\n+");
			for (int iy=0; iy<aLines.length; iy++) {
				String text=aLines[iy];
				text=text.replace("\n", " ");
				text=text.replaceAll("\t", " ");
				text=text.replaceAll(" +", " ");
				text=text.trim();
				a.add(text);
			}
			a.add("\n");
		}
		saveFile(saveFile, a);
		saveFile="results/url-list.html";
		
		a.clear();
		System.out.println("Saving URLS as an HTML document w/titles.");
		a.add("<html><head><title>ᏣᎳᎩ ᎦᏬᏂᎯᏍᏗ ᏗᎪᏪᎵ.</title></head>");
		a.add("<body><ol>");
		String d;
		String articleId;
		for (int ix=0; ix<listOfArticles.size(); ix++) {
			Article u = listOfArticles.get(ix);
			a.add("<li><a href=\""+u.getUri()+"\">");
			if (u.getDate().length()>1) d="("+u.getDate()+") ";
			else d="";
			articleId="["+u.getUri().replaceAll("[^0-9]", "")+"] ";
			a.add(articleId+d+u.getTitle_chr());
			a.add("</a></li>");
		}
		a.add("</ol></body></html>");
		saveFile(saveFile, a);
	}

	private void performHarvest(ArrayList<String> urlList) {
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
						HtmlCache.putHtml(filingUri, details.toString());
//						newData=true;
						html = details.toString();
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
	
	private ArrayList<String> loadSeedUrls() {
		System.err.println("Loading initial URLS: " + new Date());
		ArrayList<String> urlList = new ArrayList<String>();
		int articleId=1;
		int prevArticleId=0;
		int failsInARow=0;
		String queryURI;
		
		HtmlCache.open();
		articleId=HtmlCache.getMaxArticleId();
		HtmlCache.close();
		
		do {
			articleId++;
			queryURI = baseURI+queryURIA+String.valueOf(articleId);//+queryURIB;
			if (httpExists(queryURI)) {
				urlList.add(baseURI+queryURIA+String.valueOf(articleId));//+queryURIB);
				failsInARow=0;
				for (int ix=0; ix<100; ix++) {
					articleId++;
					urlList.add(baseURI+queryURIA+String.valueOf(articleId));//+queryURIB);
				}
			} else {
				failsInARow++;
			}
			if (prevArticleId+99<articleId){
				System.out.println("Prescan at articleId: "+articleId);
				prevArticleId=articleId;
			}
		} while (failsInARow<1000);

		System.out.println("CALCULATED URI LIST: (urls) "+urlList.size());
		
		System.err.println("Sorting and deduping URLS: " + new Date());
		// sort the list
		Collections.sort(urlList);
		// dedupe the sorted list
		for (int ix = urlList.size() - 1; ix > 0; ix--) {
			if (urlList.get(ix).equals(urlList.get(ix - 1))) {
				urlList.remove(ix);
			}
		}

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
	
	static public void saveFile(String outputFile, ArrayList<String> data) {
		Writer output;
		try {
			output = new BufferedWriter(new FileWriter(outputFile));
			for (int ix=0; ix<data.size(); ix++){
				output.write(data.get(ix)+"\n");
			}
			output.flush();
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}		
		
	}

}

