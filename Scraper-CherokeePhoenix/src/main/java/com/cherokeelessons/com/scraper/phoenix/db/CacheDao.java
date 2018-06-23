package com.cherokeelessons.com.scraper.phoenix.db;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.h2.jdbcx.JdbcConnectionPool;
import org.jdbi.v3.core.ConnectionFactory;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface CacheDao {
	static String TABLE_HTML = "phoenix_html";

	@SqlQuery("select url from " + TABLE_HTML + " where" //
			+ " html is NULL")
	List<String> forRescraping();

	@SqlUpdate("update " + TABLE_HTML //
			+ " set html = NULL" //
			+ " where" //
			+ " html like" //
			+ " '%<title>Cherokee Nation Site Unavailable</title>%'" + " OR " //
			+ " html like" //
			+ " '% has been a serious error on the server." //
			+ " Please try again later%'")
	void resetMaybeBadScrapes();

	@SqlUpdate("insert into " + TABLE_HTML + " (url, html) values (:url, :html)")
	void insertUrlWithHtml(@Bind("url") String url, @Bind("html") String html);

	@SqlUpdate("delete from " + TABLE_HTML + " where url=:url")
	void deleteByUrl(@Bind("url") String url);

	default void putHtml(String url, String html) {

	}

	@SqlQuery("select html from " + TABLE_HTML + " where url=:url")
	String getHtml(@Bind("url") String url);

	@SqlQuery("select count(*)!=0 from " + TABLE_HTML + " where url=:url")
	boolean containsUrl(@Bind("url") String url);

	@SqlQuery("select max(modified) from " + TABLE_HTML)
	Date modified();

	@SqlQuery("select timestampdiff('HOUR',NOW(),max(modified))<4 from " + TABLE_HTML)
	boolean isRecent();

	@SqlQuery("select url from " + TABLE_HTML + " where modified>=:oldest")
	List<String> urlsNewerThan(@Bind("oldest") Date oldest);

	@SqlQuery("select url from " + TABLE_HTML)
	List<String> getUrls();

	default int getMaxArticleId() {
		int maxId = 0;
		List<String> urls = getUrls();
		for (String url : urls) {
			String id = StringUtils.substringAfterLast(url, "/");
			try {
				maxId = Math.max(maxId, Integer.valueOf(id));
			} catch (NumberFormatException e) {
			}
		}
		return maxId;
	}

	@SqlQuery("select max(modified) from " + TABLE_HTML)
	Date getMaxDate();

	@SqlUpdate("create CACHED table if not exists " + TABLE_HTML //
			+ "(id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY," //
			+ " status INTEGER DEFAULT 0," //
			+ " url VARCHAR(254) DEFAULT ''," //
			+ " html CLOB(64M) DEFAULT null," //
			+ " modified TIMESTAMP AS CURRENT_TIMESTAMP)")
	void _createTable1();

	@SqlUpdate("create INDEX if not exists " + TABLE_HTML + "_url on " + TABLE_HTML + "(url)")
	void _createTable1Index1();

	@SqlUpdate("create INDEX if not exists " + TABLE_HTML + "_modified on " + TABLE_HTML + "(modified)")
	void _createTable1Index2();

	default void init() {
		_createTable1();
		_createTable1Index1();
		_createTable1Index2();
	}

	static class Instance {
		private final static String dbFile = "./db" + File.separator + "htmlCache";
		private final static String options = ";AUTO_SERVER=TRUE"; // ";FILE_LOCK=SOCKET";
		private static CacheDao dao;
		private static JdbcConnectionPool cp;

		public static CacheDao getCacheDao() {
			if (dao == null) {
				cp = JdbcConnectionPool.create("jdbc:h2:" + dbFile + options, "SA", "");
				ConnectionFactory connectionFactory = new ConnectionFactory() {
					@Override
					public Connection openConnection() throws SQLException {
						return cp.getConnection();
					}
				};
				Jdbi jdbi = Jdbi.create(connectionFactory);
				jdbi.installPlugin(new SqlObjectPlugin());
				dao = jdbi.onDemand(CacheDao.class);
				dao.init();
			}
			return dao;
		}
	}
}