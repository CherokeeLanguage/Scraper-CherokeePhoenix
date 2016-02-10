package com.cherokeelessons.com.scraper.phoenix;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Article {
	private String article_en="";
	public String getArticle_en() {
		return article_en;
	}

	public void setArticle_en(String article_en) {
		this.article_en = article_en;
	}

	private String article_chr="";
	private String date="";
	private String html="";
	
	private boolean isCherokee=false;

	private String title_chr="";

	private String title_en="";

	private String uri="";
	
	private void setDate(Document jhtml) {
		Element _date;
		_date = jhtml.select("div.article-single-contents div.authors div.dateTime").first();
		if (_date!=null) {
			date=_date.text();
		} else {
			date="";
		}
	}
	
	public String getDate() {
		return date;
	}
	public String getHtml() {
		return html;
	}
	public String getTitle_chr() {
		return title_chr;
	}
	public String getTitle_en() {
		return title_en;
	}

	public String getUri() {
		return uri;
	}

	public boolean isCherokee(){
		return isCherokee;
	}
	
	public void setHtml(String html) {
		this.html = html;
		isCherokee=html.contains("class=\"translation\"");
		Document jhtml = Jsoup.parse(html);
		setDate(jhtml);
		setTitles(jhtml);
		setBody_en(jhtml);
		setBody_chr(jhtml);
	}

	private void setBody_chr(Document jhtml) {
		Element article;
		article=jhtml.select("div.article-single div.article-single-contents div.translation").first();
		if (article!=null) {
			setArticle_chr(article.html());
		} else {
			setArticle_chr("");
		}
	}
	private void setBody_en(Document jhtml) {
		Element article;
		//div.article-single div.article-single-contents div.translation
		article=jhtml.select("div.article-single div.article-single-contents div.html").first();
		if (article!=null) {
			setArticle_en(article.html());
		} else {
			setArticle_en("");
		}
	}
	private void setTitles(Document jhtml){
		Element title;
		title=jhtml.select("div.article-single h2").first();
		if (title!=null) title_en = title.text();
		else title_en="";
		
		title=jhtml.select("div.translation h2").first();
		if (title!=null) title_chr = title.text();
		else title_chr="";
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getArticle_chr() {
		return article_chr;
	}

	public void setArticle_chr(String article_chr) {
		this.article_chr = article_chr;
	}	
}
