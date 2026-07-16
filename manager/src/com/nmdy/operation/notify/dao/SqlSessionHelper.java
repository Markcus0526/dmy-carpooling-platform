package com.nmdy.operation.notify.dao;

import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class SqlSessionHelper {
	private static SqlSessionFactory sessionFactory;
	private static Reader reader;
	private static SqlSession session = null;
	
	static {
		try{
			reader = Resources.getResourceAsReader("mybatis-config.xml");
			sessionFactory = new SqlSessionFactoryBuilder().build(reader);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static SqlSessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	public static SqlSession getSession() {
		
		if (session == null) {
			session = getSessionFactory().openSession();
		}
//		SqlSessionFactory factory = getSessionFactory();
//		SqlSession session = null;
//		if (factory != null) {
//			session = factory.openSession();
//		}
		return session;
	}
	
	public static SqlSession getMapperSession() {
		SqlSessionFactory factory = getSessionFactory();
		SqlSession mapperSession = null;
		if (factory != null) 
			mapperSession = factory.openSession();
		return mapperSession;
	}
}
