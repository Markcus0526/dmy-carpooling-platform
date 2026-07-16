package com.webapi.common;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import com.mchange.v2.c3p0.ComboPooledDataSource;



public class DBManager
{
	private ComboPooledDataSource cpds;

	private static DBManager dbmgr;

	public DBManager()
	{
		String szUserName = "", szPassword = "", szUrl = "", szDriverClass = "";
		cpds = new ComboPooledDataSource();

		szUserName = ApiGlobal.getValueFromConnectionXML("Username");
		szPassword = ApiGlobal.getValueFromConnectionXML("Password");
		szUrl = ApiGlobal.getValueFromConnectionXML("Url");
		szDriverClass = ApiGlobal.getValueFromConnectionXML("DriverClass");

		System.out.println("szUrl=="+szUrl+"----->szUserName=="+szUserName+"------>szPassword=="+szPassword);
		cpds.setJdbcUrl(szUrl);
		cpds.setUser(szUserName);
		cpds.setPassword(szPassword);

		try {
			cpds.setDriverClass(szDriverClass);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		cpds.setInitialPoolSize(20);
		cpds.setAcquireIncrement(10);
		cpds.setMaxPoolSize(1000);
		cpds.setMinPoolSize(10);
		cpds.setMaxStatements(200);
	}

	public static DBManager getInstance() {
		if (dbmgr == null)
			dbmgr = new DBManager();

	   	return dbmgr;
	}


	static public Properties readProperties(String filename) throws IOException{
		Properties props = new Properties();
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		InputStream stream = loader.getResourceAsStream(filename);
		props.load(stream);
		return props;
	}


	public static Connection getDBConnection() throws SQLException {
		return getInstance().cpds.getConnection();
	}



	// Initialise method
//	private DBManager()
//	{
//		// Analyse connection.xml file
//		String szDriverClassName = "";
//		String szUserName = "";
//		String szPassword = "";
//		String szUrl = "";
//
//		try {
//			DocumentBuilderFactory docBF = DocumentBuilderFactory.newInstance();
//
//			DocumentBuilder db = docBF.newDocumentBuilder();
//			String szPath = "";
//			try {
//				szPath = getClass().getResource("/").getPath();
//				szPath = szPath.substring(1);
//				szPath = URLDecoder.decode(szPath,"utf-8") + "connection.xml";
//			} catch (Exception ex) {
//				ex.printStackTrace();
//			}
//
//			Document doc = db.parse(new FileInputStream(szPath));
//
//			szUserName = doc.getElementsByTagName("Username")
//					.item(0)
//					.getFirstChild().getNodeValue();
//			szPassword = doc.getElementsByTagName("Password")
//					.item(0)
//					.getFirstChild().getNodeValue();
//			szUrl = doc.getElementsByTagName("Url")
//					.item(0)
//					.getFirstChild().getNodeValue();
//
//			szDriverClassName = doc.getElementsByTagName("DriverClass")
//					.item(0)
//					.getFirstChild().getNodeValue();
//		} catch (Exception ex) {
//			ex.printStackTrace();
//
//			// For the local test
//			szDriverClassName = "com.mysql.jdbc.Driver";
//			szUserName = "bjpc_admin";
//			szPassword = "bjpc2014@)!$";
//			szUrl = "jdbc:mysql://192.168.1.79:3306/pinche";
//		}
//
//		if (!szDriverClassName.equals(""))
//			ds.setDriverClassName(szDriverClassName);
//
//		if (!szUserName.equals(""))
//			ds.setUsername(szUserName);
//
//		if (!szPassword.equals(""))
//			ds.setPassword(szPassword);
//
//		if (!szUrl.equals(""))
//			ds.setUrl(szUrl);
//
//		ds.setInitialSize(20);
//		ds.setConnectionProperties("useUnicode=yes;characterEncoding=utf8");
//		ds.setMaxTotal(1000);
//		ds.setMaxIdle(20);
//		ds.setMaxWaitMillis(100000);
//
//		dbsource = ds;
//	}
//
//
//	// Method to return db connection instance
//	public Connection getDBConnection() throws SQLException
//	{
//		if (dbsource == null)
//			return null;
//
//		Connection conn = null;
//		connLock.lock();
//
//		try {
//			conn = dbsource.getConnection();
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		} finally {
//			connLock.unlock();
//		}
//
//		return conn;
//	}
}
