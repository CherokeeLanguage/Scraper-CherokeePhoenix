package com.cherokeelessons.com.scraper.phoenix;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Article {
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
		Element _date;
		_date = jhtml.select("div.article-single-contents div.authors div.dateTime").first();
		if (_date != null) {
			date = _date.text();
		} else {
			date = "";
		}
	}

	public String getDate() {
		return StringUtils.strip(date);
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
}
