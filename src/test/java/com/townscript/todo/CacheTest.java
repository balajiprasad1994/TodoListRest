package com.townscript.todo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.townscript.todo.dao.JdbcTemplateFactory;
import com.townscript.todo.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/com/townscript/todo/testbeans.xml")
public class CacheTest {
	
	@Autowired 
	private SessionFactory sessionFactory;

	@Before
	public void createEnvironment()
	{
		String sql = "DELETE FROM USER";
		 
		JdbcTemplate jdbcTemplate = JdbcTemplateFactory.getJdbcTemplate();
		jdbcTemplate.update(sql);
		
		System.out.println("Set Up Environment");
	}
	
	@After
	public void clearEnvironment()
	{
		String sql = "DELETE FROM USER";
		 
		JdbcTemplate jdbcTemplate = JdbcTemplateFactory.getJdbcTemplate();
		jdbcTemplate.update(sql);
		
		System.out.println("Clear Environment");
	}
	
	@Test
	public void testCache(){
		final String sql = "INSERT INTO USER (ID,USERNAME,PASSWORD,FIRSTNAME,LASTNAME) VALUES ( 0 , 'jamesbond', 'nobond', 'james', 'bond')";
		 
		JdbcTemplate jdbcTemplate = JdbcTemplateFactory.getJdbcTemplate();
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(
		  new PreparedStatementCreator() {
			    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
			      return connection.prepareStatement(sql,  Statement.RETURN_GENERATED_KEYS);
			    }
			  }, keyHolder);
		int userId  =  keyHolder.getKey().intValue();
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		//check first level cache test; if working query should be done only once for load
		
		for(int x = 0; x < 2; x = x+1) {
			long currentTime  = System.currentTimeMillis();
			session.load(User.class, userId);
			long afterTime = System.currentTimeMillis();
			System.out.println("Time taken : "+ (afterTime - currentTime));
		}
		session.close();
		
		//second level cache test; if working hibernate should not throw query here for load
		
		System.out.println("session closed");
		Session session2 = sessionFactory.openSession();
		session2.beginTransaction();
		System.out.println("new session opened");
		
		long currentTimeWith2ndLevel  = System.currentTimeMillis();
		session2.load(User.class, userId);
		long afterTimeWith2ndLevel = System.currentTimeMillis();
		System.out.println("Time taken : "+ (afterTimeWith2ndLevel - currentTimeWith2ndLevel));
		session2.close();
		
		System.out.println("session closed");
		
		//query cache test; if working hibernate should not throw query for query2
		Session session3 = sessionFactory.openSession();
		session3.beginTransaction();
		String queryString = "FROM "+ User.class.getName() +" WHERE id = :id";
		
		Query query = session3.createQuery(queryString);
		query.setParameter("id", 1);
		query.setCacheable(true);
		long currentTimeWithoutQueryCache  = System.currentTimeMillis();
		query.list();
		long afterTimeWithoutQueryCache = System.currentTimeMillis();
		System.out.println("Time taken : "+ (afterTimeWithoutQueryCache - currentTimeWithoutQueryCache));

		Query query2 = session3.createQuery(queryString);
		query2.setParameter("id", 1);
		query2.setCacheable(true);
		long currentTimeWithQueryCache  = System.currentTimeMillis();
		query2.list();
		long afterTimeWithQueryCache = System.currentTimeMillis();
		System.out.println("Time taken : "+ (afterTimeWithQueryCache - currentTimeWithQueryCache));
	}
}
