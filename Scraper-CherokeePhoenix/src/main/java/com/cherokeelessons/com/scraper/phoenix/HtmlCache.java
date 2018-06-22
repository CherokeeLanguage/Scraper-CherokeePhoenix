package com.cherokeelessons.com.scraper.phoenix;

import java.io.File;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HtmlCache {
	private final static String dbFile = "./db" + File.separator + "htmlCache";
	private final static String options = ";AUTO_SERVER=TRUE"; // ";FILE_LOCK=SOCKET";

	private static Connection makeConnection() {

		Connection db = null;
		try {
			db = DriverManager.getConnection("jdbc:h2:" + dbFile + options, "SA", "");
			db.setAutoCommit(false);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return db;
	}

	static private Connection db;

	public static java.util.Date getMaxDate() {
		PreparedStatement ps;
		ResultSet rs;
		java.util.Date maxDate;

		Calendar c = Calendar.getInstance();
		c.clear();
		c.set(1970, 0, 1);
		maxDate = c.getTime();

		try {
			ps = db.prepareStatement("select max(modified) from phoenix_html");
			rs = ps.executeQuery();
			if (rs.first()) {
				maxDate = rs.getDate(1);
			}
		} catch (SQLException e) {
		}
		return maxDate;
	}

	public static int getMaxArticleId() {
		PreparedStatement ps;
		ResultSet rs;
		String url;
		int maxId = 0;
		int thisId = 0;

		try {
			ps = db.prepareStatement("select url from phoenix_html");
			rs = ps.executeQuery();
			if (rs.first())
				do {
					url = rs.getString(1).replaceAll("[^0-9]", "");
					thisId = Integer.valueOf(url);
					if (thisId > maxId)
						maxId = thisId;
				} while (rs.next());
		} catch (SQLException e) {
		}
		return maxId;
	}

	public static List<String> urlsNewerThan(java.util.Date oldest) {
		PreparedStatement ps;
		ResultSet rs;
		List<String> list = new ArrayList<String>();
		Date sqlOldest;

		sqlOldest = new Date(oldest.getTime());
		try {
			ps = db.prepareStatement("select url from phoenix_html where modified>=?");
			ps.setDate(1, sqlOldest);
			rs = ps.executeQuery();
			if (rs.first())
				do {
					list.add(rs.getString(1));
				} while (rs.next());
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return list;
	}

	public static boolean isRecent() {
		PreparedStatement ps;
		ResultSet rs;
		try {
			ps = db.prepareStatement("select timestampdiff('HOUR',NOW(),max(modified))<4 from phoenix_html");
			rs = ps.executeQuery();
			if (rs.first()) {
				return rs.getBoolean(1);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return false;
	}

	public static java.util.Date modified() {
		PreparedStatement ps;
		ResultSet rs;
		try {
			ps = db.prepareStatement("select max(modified) from phoenix_html");
			rs = ps.executeQuery();

			if (rs.first()) {
				return new java.util.Date(rs.getTimestamp(1).getTime());
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return new java.util.Date(0);
	}

	public static List<String> allUrls() {
		PreparedStatement ps;
		ResultSet rs;
		List<String> urls = new ArrayList<String>();

		try {
			ps = db.prepareStatement("select url from phoenix_html");
			rs = ps.executeQuery();
			while (rs.next()) {
				urls.add(rs.getString(1));
			}
			;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return urls;
	}

	public static void open() {
		try {
			Class.forName("org.h2.Driver");
		} catch (Exception e) {
			System.err.println("ERROR: failed to load H2 driver.");
			throw new RuntimeException(e);
		}
		db = makeConnection();
		PreparedStatement ps;
		try {
			ps = db.prepareStatement("create CACHED table if not exists phoenix_html" //
					+ "(id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY," //
					+ " status INTEGER DEFAULT 0," //
					+ " url VARCHAR(254) DEFAULT ''," //
					+ " html CLOB(64M) DEFAULT null," //
					+ " modified TIMESTAMP AS CURRENT_TIMESTAMP)");
			ps.execute();
			ps = db.prepareStatement("create INDEX if not exists URL on phoenix_html(url)");
			ps.execute();
			ps = db.prepareStatement("create INDEX if not exists modified on phoenix_html(modified)");
			ps.execute();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		System.err.println("HtmlCache DB Opened");
	}

	public static void close() {
		if (db != null) {
			try {
				Statement st = db.createStatement();
				st.execute("shutdown");
				db.close();
				db = null;
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
			System.err.println("HtmlCache DB Closed");
		}
	}

	public static boolean containsUrl(String url) {
		PreparedStatement ps;
		ResultSet rs;
		try {
			ps = db.prepareStatement("select count(*)!=0 from phoenix_html where url=?");
			ps.setString(1, url);
			rs = ps.executeQuery();
			if (rs.first()) {
				return rs.getBoolean(1);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return false;
	}

	public static String getHtml(String url) {
		String html = null;
		PreparedStatement ps;
		ResultSet rs;

		try {
			ps = db.prepareStatement("select html from phoenix_html where url=?");
			ps.setString(1, url);
			rs = ps.executeQuery();
			if (rs.first()) {
				html = rs.getString(1);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return html;
	}

	public static void putHtml(String url, String html) {
		PreparedStatement ps;

		try {
			// delete any previous copies
			ps = db.prepareStatement("delete from phoenix_html where url=?");
			ps.setString(1, url);
			ps.execute();

			ps = db.prepareStatement("insert into phoenix_html (url, html) values (?, ?)");
			ps.setString(1, url);
			ps.setString(2, html);
			ps.execute();

			db.commit();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public static void resetMaybeBadScrapes() {
		try (Statement s = db.createStatement()) {
			s.executeUpdate("update phoenix_html" //
					+ " set html = NULL" //
					+ " where" //
					+ " html like" //
					+ " '%<title>Cherokee Nation Site Unavailable</title>%'"
					+ " OR " //
					+ " html like" //
					+ " '% has been a serious error on the server." //
					+ " Please try again later%'");
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public static List<String> forRescraping() {
		//urlList.add(baseURI+queryURIA+String.valueOf(articleId));
		PreparedStatement ps;
		ResultSet rs;
		List<String> urls = new ArrayList<String>();

		try {
			ps = db.prepareStatement("select url from phoenix_html"
					+ " where" //
					+ " html is NULL");
			rs = ps.executeQuery();
			while (rs.next()) {
				urls.add(rs.getString(1));
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		if (!urls.isEmpty()) {
			System.out.println("Found "+urls.size()+" articles that need rescraping...");
		}
		return urls;
	}
}
