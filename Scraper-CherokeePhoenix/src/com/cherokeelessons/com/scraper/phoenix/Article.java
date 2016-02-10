package com.cherokeelessons.com.scraper.phoenix;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Article {
	private String html="";
	private String uri="";
	private boolean isCherokee=false;
	
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
		isCherokee=this.html.matches(".*[Ꭰ-Ᏼ].*");
		Document jhtml = Jsoup.parse(html);
		date = extractDate(jhtml);
		setTitles(jhtml);
		article = extractBody(jhtml);
	}
	//html body div#container div#content div.article div.messagebubble
	private String extractDate(Document jhtml) {
		Element date;
		date = jhtml.select("div.article-single-contents div.dateTime").first();
		if (date!=null) return date.text();
		return "";
	}
	//html body div#container div#content div.article pre
	private String extractBody(Document jhtml){
		String text;
		String raw;
		Element article;
		article=jhtml.select("div.article-single-contents div.html").first();
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
	
	private void setTitles(Document jhtml){
		Element title;
		title=jhtml.select("div.article-single h2").first();
		if (title!=null) title_en = title.text();
		else title_en="";
		
		title=jhtml.select("div.translation h2").first();
		if (title!=null) title_en = title.text();
		else title_en="";
	}
	
	private String date="";
	private String title_en="";
	private String title_chr="";
	private String article="";

	public String getDate() {
		return date;
	}

	public String getTitle_en() {
		return title_en;
	}
	
	public String getTitle_chr() {
		return title_chr;
	}

	public String getArticle() {
		return article;
	}

	public boolean isCherokee(){
		return isCherokee;
//		 return article.replaceAll("[^Ꭰ-Ᏼ]", "").replace("ᏣᎳᎩ", "").length()>1;
	}	
}
