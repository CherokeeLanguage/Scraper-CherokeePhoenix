package com.cherokeelessons.com.scraper.phoenix;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Article implements Comparable<Article>{
	private String article_en = "";
	
	public String getArticle_dual(){
		String en = StringUtils.strip(getArticle_en());
		String chr = StringUtils.strip(getArticle_chr());
		
		String unmatched = "\n"+chr+"\n\n"+en+"\n";
		if (StringUtils.countMatches(en, "\n")!=StringUtils.countMatches(chr, "\n")) {
			return unmatched;
		}
		
		if (StringUtils.countMatches(en, "\n")<2){
			return unmatched;
		}
		
		String[] a_en = StringUtils.split(en, "\n");
		String[] a_chr = StringUtils.split(chr, "\n");
		
		if (a_en.length!=a_chr.length) {
			return unmatched;
		}
		
		StringBuilder sb = new StringBuilder();
		for (int i=0; i<a_en.length; i++) {
			sb.append("\n");
			sb.append(a_chr[i]);
			sb.append("\n");
			sb.append(a_en[i]);
			sb.append("\n");
		}
		return sb.toString();
	}

	public String getArticle_en() {
		String tmp = article_en;
		tmp = tmp.replaceAll("<!-- .*?-->", "");
		
		tmp = tmp.replaceAll("<td .*?>.*?</td>", "");
		tmp = tmp.replaceAll("<tr .*?>.*?</tr>", "");
		tmp = tmp.replaceAll("<tbody .*?>.*?</tbody>", "");
		tmp = tmp.replaceAll("<thead.*?>.*?</thead.*?>", "");
		tmp = tmp.replaceAll("<table .*?>.*?</table>", "");
		
		tmp = tmp.replace("</p>", "\n");
		tmp = tmp.replace("</div>", "\n");
		tmp = tmp.replace("<br/>", "\n");
		tmp = tmp.replace("<br>", "\n");
		
		tmp = tmp.replace("&nbsp;", " ");
		tmp = tmp.replaceAll("<h5>.*</h5>", " ");
		tmp = tmp.replaceAll("<h2>.*</h2>", " ");
		tmp = tmp.replaceAll("<author .*?>.*?</author>", " ");
		tmp = tmp.replaceAll("<.*?>", " ");
		tmp = tmp.replaceAll("( *\n)+", "\n");
		tmp = tmp.replaceAll(" +", " ");
		return StringUtils.strip(tmp);
	}

	public void setArticle_en(String article_en) {
		this.article_en = article_en;
	}

	private String article_chr = "";
	private String date = "";
	private String html = "";

	private boolean isCherokee = false;

	private String title_chr = "";

	private String title_en = "";

	private String uri = "";

	private void setDate(Document jhtml) {
		Element html_date;
		html_date = jhtml.select("div.authors div.dateTime").first();
		if (html_date != null) {
			date = html_date.text();
		} else {
			date = "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy hh:mm a");
		sdf.setTimeZone(TimeZone.getTimeZone("US/Central"));
		sdf.setLenient(false);
		try {
			_date=sdf.parse(date);
		} catch (ParseException e) {
			System.err.println("date: "+String.valueOf(date)
			+", "+String.valueOf(html_date)
			+", "+getArticleId());
			throw new RuntimeException(e);
		}
	}
	
	private Date _date;
	
	public String getDate() {
		return StringUtils.strip(date);
	}
	
	public Date getJavaDate() {
		return _date;
	}

	public String getHtml() {
		return html;
	}

	public String getTitle_chr() {
		return StringUtils.strip(title_chr);
	}

	public String getTitle_en() {
		return StringUtils.strip(title_en);
	}

	public String getUri() {
		return uri;
	}

	public boolean isCherokee() {
		return isCherokee;
	}

	public void setHtml(String html) {
		this.html = html;
		isCherokee = html.contains("class=\"translation\"") || html.contains("ᏣᎳᎩ");
		Document jhtml = Jsoup.parse(html);
		setDate(jhtml);
		setTitles(jhtml);
		setBody_en(jhtml);
		setBody_chr(jhtml);
		setAudio_info(jhtml);
	}

	private void setAudio_info(Document jhtml) {
		Element audio = jhtml.select("div.article-single-contents div.media div.audioArea").first();
		if (audio==null) {
			return;
		}
		audioUrl = audio.absUrl("src");
		if (!StringUtils.isBlank(audioUrl)) {
			hasAudio=true;
		}
	}

	private void setBody_chr(Document jhtml) {
		Element article;
		article = jhtml.select("div.article-single div.article-single-contents div.translation").first();
		if (article != null) {
			setArticle_chr(article.html());
		} else {
			setArticle_chr("");
		}
	}
	
	private String audioUrl="";
	public String getAudioUrl() {
		return audioUrl;
	}

	private boolean hasAudio=false;
	public boolean hasAudio(){
		return hasAudio;
	}

	private void setBody_en(Document jhtml) {
		Element article;
		// div.article-single div.article-single-contents div.translation
		article = jhtml.select("div.article-single div.article-single-contents div.html").first();
		if (article != null) {
			setArticle_en(article.html());
		} else {
			setArticle_en("");
		}
	}

	private void setTitles(Document jhtml) {
		Element title;
		title = jhtml.select("div.article-single h2").first();
		if (title != null)
			title_en = title.text();
		else
			title_en = "";

		title = jhtml.select("div.translation h2").first();
		if (title != null)
			title_chr = title.text();
		else
			title_chr = "";
	}

	public void setUri(String uri) {
		this.uri = uri;
		setId(uri);
	}
	
	private void setId(String uri) {
		articleId=Integer.parseInt(uri.replaceAll(".*/([0-9]+)", "$1"));
	}
	private int articleId;
	public int getArticleId() {
		return articleId;
	}

	public void setArticleId(int articleId) {
		this.articleId = articleId;
	}

	public String getArticle_chr() {
		String tmp = article_chr;
		
		tmp = tmp.replaceAll("<!-- .*?-->", "");
		
		tmp = tmp.replaceAll("<td .*?>.*?</td>", "");
		tmp = tmp.replaceAll("<tr .*?>.*?</tr>", "");
		tmp = tmp.replaceAll("<tbody .*?>.*?</tbody>", "");
		tmp = tmp.replaceAll("<thead.*?>.*?</thead.*?>", "");
		tmp = tmp.replaceAll("<table .*?>.*?</table>", "");
		
		tmp = tmp.replace("</p>", "\n");
		tmp = tmp.replace("</div>", "\n");
		tmp = tmp.replace("<br/>", "\n");
		tmp = tmp.replace("<br>", "\n");
		
		tmp = tmp.replace("&nbsp;", " ");
		tmp = tmp.replaceAll("<h5>.*</h5>", " ");
		tmp = tmp.replaceAll("<h2>.*</h2>", " ");
		tmp = tmp.replaceAll("<author .*?>.*?</author>", " ");
		tmp = tmp.replaceAll("<.*?>", " ");
		tmp = tmp.replaceAll("( *\n)+", "\n");
		tmp = tmp.replaceAll(" +", " ");
		return StringUtils.strip(tmp);
	}

	public void setArticle_chr(String article_chr) {
		this.article_chr = article_chr;
	}

	@Override
	public int compareTo(Article o) {
		if (!_date.equals(o._date)) {
			return _date.compareTo(o._date);
		}
		return articleId - o.articleId;
	}
}
