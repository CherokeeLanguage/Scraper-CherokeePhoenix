package com.cherokeelessons.com.scraper.phoenix;
//http://www.cherokeephoenix.org/Article/Index/6582?StartCherokee=1
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Random;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Application implements Runnable {
	final private long seconds = 1000;
	final private long minutes = 60 * seconds;
	final private long hours = 60 * minutes;
//	final private long days = 24 * hours;

	private final long timeLimit = 4 * hours; // in ms

	private String baseURI = "http://www.cherokeephoenix.org";
	private String queryURIA = "/Article/Index/";
	private String queryURIB = "?StartCherokee=1";

	private String[] args;
	private String destDir = "results";//set by cli option
//	private String destFile = "test.dbf";//set by cli date option
	
	int hardLimit = 0;//set by cli option

	private CommandLine cmd;
	private Options cliOptions = new Options();

	public Application(String[] args) {
		super();
		this.args = args;
		
		System.out.println("Creating Application Object");

		cliOptions.addOption("help", false, "Display this help list.");
		cliOptions.addOption("cachedOnly", false, "Only use previously downloaded data.");
		cliOptions.addOption("harvestOnly", false, "Only harvest. Do not process data or generate final DBF.");
		cliOptions.addOption("autoExit", false, "Have program self-close after processing complete.");
		cliOptions.addOption("limit", true, "Option to limit how many records to process.");
	}

	private boolean cachedOnly=false;
	private boolean harvestOnly=false;
	private boolean autoExit=false;

	@Override
	public void run() {
		ArrayList<String> urlList;
		ArrayList<Article> articles=new ArrayList<Article>();
		
		if (!parseCliOptions()) {
			return;
		}
		
		if (cachedOnly){
			HtmlCache.open();
			urlList=HtmlCache.allUrls();
			HtmlCache.close();
			
		} else {
			urlList = loadSeedUrls();
			performHarvest(urlList);	
		}
		
		if (harvestOnly) {
			System.err.println("Harvest only mode. Finished.");
			if (autoExit) {
				System.exit(0);
			}
			return;
		}
		
		HtmlCache.open();
		urlList=HtmlCache.allUrls();
		HtmlCache.close();
		
		System.err.println("========================================================");
		System.out.println("Processing "+urlList.size()+" cached articles.");
		System.err.println("========================================================");
		
		extractDataFromHtml(urlList, articles);
		System.err.println("Processing complete at " + new Date());
		if (autoExit) {
			System.exit(0);
		}
	}

	private void extractDataFromHtml(ArrayList<String> urlList,
			ArrayList<Article> listOfArticles) {
		HtmlCache.open();

		System.err.println("PROCESSING HTML START: " + new Date());
		if (hardLimit==0) hardLimit=urlList.size();
		
		for (int ix = 0; ix<hardLimit; ix++) {

			String articleUri = urlList.get(ix);
			String html = HtmlCache.getHtml(articleUri);
			if (html == null) {
				// skip missing content
				continue;
			}
			Article newArticle = new Article();
			newArticle.setHtml(html);
			if (!newArticle.ᏣᎳᎩᎢᎩ()){
				continue;
			}
			newArticle.setUri(articleUri);
			listOfArticles.add(newArticle);
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
			if (b.getTitle().length()>0) {
				a.add(b.getTitle());
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
			a.add(articleId+d+u.getTitle());
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
	private ArrayList<String> loadSeedUrls() {
		System.err.println("Loading initial URLS: " + new Date());
		Document doc = null;
		ArrayList<String> urlList = new ArrayList<String>();
		int articleId=1;
		int prevArticleId=0;
		int failsInARow=0;
		int maxFailGap=0;
		String queryURI;
		Random r=new Random();
		
		HtmlCache.open();
		articleId=HtmlCache.getMaxArticleId();
		HtmlCache.close();
		
		do {
			queryURI = baseURI+queryURIA+String.valueOf(articleId)+queryURIB;
			try {
				doc = Jsoup
						.connect(queryURI)
						.referrer(queryURI)
						.userAgent("Mozilla").get();
				for (int z=0; z<r.nextInt(11)+15; z++){
					urlList.add(baseURI+queryURIA+String.valueOf(articleId)+queryURIB);
					articleId++;
				}
				failsInARow=0;
				maxFailGap=0;
				if (prevArticleId+100<articleId){
					System.out.println("Prescan at articleId: "+articleId);
					prevArticleId=articleId;
				}
			} catch (IOException e) {
				if (e.getMessage().startsWith("404 error")) {
					articleId++;
					failsInARow++;
					if (failsInARow>100) {
						break;
					}
				} else {
					System.err.println(e.getMessage());
					randomSleep();
					failsInARow++;
					if (failsInARow>100) {
						break;
					}
				}
			}
		} while (true);

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
	
	

	private boolean parseCliOptions() {
//		File dir;
		PosixParser parser = new PosixParser();
		try {
			cmd = parser.parse(cliOptions, args);
			
			if (cmd.hasOption("harvestOnly")){
				harvestOnly=true;
			}
			
			if (harvestOnly) return true;
			

			if (cmd.hasOption("autoExit")) {
				autoExit = true;
			}
			if (cmd.hasOption("cachedOnly")) {
				cachedOnly = true;
			}

			if (cmd.hasOption("limit")){
				try {
					hardLimit=Integer.valueOf(cmd.getOptionValue("limit"));
				} catch (NumberFormatException e) { }
			}
			System.out.println("Destination Directory: '" + destDir + "'");
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
		return true;
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

