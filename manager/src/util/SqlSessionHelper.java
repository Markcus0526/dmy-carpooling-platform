package util;

import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class SqlSessionHelper {
	private static SqlSessionFactory sessionFactory;
	private static Reader reader;
	
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
		SqlSessionFactory factory = getSessionFactory();
		SqlSession session = null;
		if (factory != null) {
			session = factory.openSession();
		}
		return session;
	}
}
