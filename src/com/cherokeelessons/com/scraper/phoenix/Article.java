package com.cherokeelessons.com.scraper.phoenix;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Article {
	private String html="";
	private String uri="";
	
	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
		Document jhtml = Jsoup.parse(html);
		date = extractDate(jhtml);
		title = extractTitle(jhtml);
		article = extractBody(jhtml);
	}
	//html body div#container div#content div.article div.messagebubble
	private String extractDate(Document jhtml) {
		Element date;
		date = jhtml.select("div#container div#content div.article div.messagebubble").first();
		if (date!=null) return date.text();
		return "";
	}
	//html body div#container div#content div.article pre
	private String extractBody(Document jhtml){
		String text;
		String raw;
		Element article;
		article=jhtml.select("div#content div.article pre").first();
		if (article!=null) {
			if (article.html().contains("<")){
				raw=article.toString();
				raw=raw.replaceAll("<h.>", "");
				raw=raw.replaceAll("</h.>", "\n\n");
				raw=raw.replaceAll("<div>", "");
				raw=raw.replaceAll("</div>", "\n\n");
				raw=raw.replaceAll("<br */?>", "\n\n");
				raw=raw.replaceAll("\t", " ");
				article=Jsoup.parse(raw);
			}
			text=article.text();
		} else {
			text="";
		}
		text=text.replaceAll("\t", " ");
		text=text.replaceAll(" +", " ");
		text=text.trim();			
		return text;
	}
	//html body div#container div#content div.article div
	private String extractTitle(Document jhtml){
		String titleText;
		Element title;
		title=jhtml.select("div#container div#content div.article div").first();
		if (title!=null) titleText = title.text();
		else titleText="";
		if (titleText.replaceAll("[^ ]", "").length()<2){
			//html body div#container div#content div.article pre h2
			title=jhtml.select("div#container div#content div.article pre h2").first();
			if (title!=null) titleText = title.text();
			else titleText="";
		}
		return titleText;
	}
	
	private String date="";
	private String title="";
	private String article="";

	public String getDate() {
		return date;
	}

	public String getTitle() {
		return title;
	}

	public String getArticle() {
		return article;
	}

	public boolean ᏣᎳᎩᎢᎩ(){
		 return article.replaceAll("[^Ꭰ-Ᏼ]", "").replace("ᏣᎳᎩ", "").length()>1;
	}	
}
