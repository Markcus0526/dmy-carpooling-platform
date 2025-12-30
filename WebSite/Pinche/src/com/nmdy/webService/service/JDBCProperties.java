package com.nmdy.webService.service;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCProperties {
	private String classDriver;
	private String url;
	private String username;
	private String password;
	private Connection connection;
	private Statement statement;
	private ResultSet resultSet;

	/**
	 * 创建一个空参构造函数加载配置文件 取得参数连接数据库
	 * 
	 * @return Connection
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public JDBCProperties() throws IOException, ClassNotFoundException {

		// 输入文件

		// 路径为当前Classpath的路径.
		InputStream inputStream = JDBCProperties.class.getClass()
				.getResourceAsStream("/jdbc.properties");
		System.out.println(inputStream);
		java.util.Properties properties = new java.util.Properties();
		if (inputStream != null)
			properties.load(inputStream);
		// 根据Key取得配置文件中的值
		classDriver = properties.getProperty("classDriver");
		url = properties.getProperty("url");
		username = properties.getProperty("username");
		password = properties.getProperty("password");
		System.out.println(classDriver);
		System.out.println(url);
		System.out.println(username);
		System.out.println(password);
		// 加载类文件
		Class.forName(classDriver);

	}

	/**
	 * 
	 * 
	 * @return
	 * @throws SQLException
	 */
	public java.sql.Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url, username, password);
	}

	/**
	 * 关闭数据库连接信息
	 */
	public void closeConncetion() {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (statement != null) {

					try {
						statement.close();
					} catch (SQLException e) {
						e.printStackTrace();
					} finally {
						if (connection != null) {

							try {
								connection.close();
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}

		}
	}

}
