package com.townscript.todo.service.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import com.townscript.todo.dao.JdbcTemplateFactory;
import com.townscript.todo.model.Category;
import com.townscript.todo.model.Tag;
import com.townscript.todo.model.Task;
import com.townscript.todo.model.User;
import com.townscript.todo.model.UserRowMapper;
import com.townscript.todo.service.user.UserService;

import org.junit.After;
import org.junit.Assert;
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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/com/townscript/todo/testbeans.xml")
public class UserServiceTest {
	
	@Autowired
	private UserService userService;
	
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
	public void testRegisterUser(){
		User user = new User();
		user.setUsername("jamesbond");
		user.setPassword("ionicbond");
		user.setFirstname("James");
		user.setLastname("Bond");
		int userId = userService.registerUser(user);
		String sql = "SELECT * FROM USER WHERE ID = " + userId;
		JdbcTemplate jdbcTemplate = JdbcTemplateFactory.getJdbcTemplate();
		List<User> usersList = jdbcTemplate.query(sql, new UserRowMapper());
		Assert.assertEquals(userId, usersList.get(0).getId());
		Assert.assertEquals(user.getUsername(), usersList.get(0).getUsername());
		Assert.assertEquals(user.getPassword(), usersList.get(0).getPassword());
		Assert.assertEquals(user.getFirstname(), usersList.get(0).getFirstname());
		Assert.assertEquals(user.getLastname(), usersList.get(0).getLastname());
	}
	
	@Test
	public void testGetUserInfo(){
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
		
		User user = userService.getUserInfo(userId);
		
		Assert.assertEquals(userId, user.getId());
		Assert.assertEquals("jamesbond", user.getUsername());
		Assert.assertEquals("nobond", user.getPassword());
		Assert.assertEquals("james", user.getFirstname());
		Assert.assertEquals("bond", user.getLastname());
	}
	
	@Test
	public void testChangePassword(){
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
		String newPassword = "newPassword";
		User user = userService.getUserInfo(userId);
		user.setPassword(newPassword);
		userService.changePassword(userId, newPassword);
		String sqlcheck = "SELECT PASSWORD FROM USER WHERE ID = " + userId;
		JdbcTemplate jdbcTemplateforPassword = JdbcTemplateFactory.getJdbcTemplate();
		 
		String password = jdbcTemplateforPassword.queryForObject(sqlcheck, String.class);
		Assert.assertEquals(user.getPassword(),password);
	}
	
	@Test
	public void testDeleteUser(){
		final String sql = "INSERT INTO USER (ID, USERNAME, PASSWORD, FIRSTNAME,LASTNAME) VALUES ( 0 , 'jamesbond', 'nobond', 'james', 'bond')";
		 
		JdbcTemplate jdbcTemplate = JdbcTemplateFactory.getJdbcTemplate();
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(
		  new PreparedStatementCreator() {
			    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
			      return connection.prepareStatement(sql,  Statement.RETURN_GENERATED_KEYS);
			    }
			  }, keyHolder);

		int userId  =  keyHolder.getKey().intValue();
		userService.deleteUser(userId);
		String sqlForAccess = "SELECT * FROM USER " + "WHERE ID = "+ userId;
		JdbcTemplate jdbcTemplateForAccess = JdbcTemplateFactory.getJdbcTemplate();
		List<User> usersList = jdbcTemplateForAccess.query(sqlForAccess, new UserRowMapper());
		Assert.assertTrue(usersList.isEmpty());
	}
	
	@Test
	public void testAuthenticateUser() throws Exception{
		final String sql = "INSERT INTO USER (ID, USERNAME, PASSWORD, FIRSTNAME,LASTNAME) VALUES ( 0 , 'jamesbond', 'nobond', 'james', 'bond')";
		 
		JdbcTemplate jdbcTemplate = JdbcTemplateFactory.getJdbcTemplate();
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(
		  new PreparedStatementCreator() {
			    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
			      return connection.prepareStatement(sql,  Statement.RETURN_GENERATED_KEYS);
			    }
			  }, keyHolder);
		User user = userService.authenticateUser("jamesbond", "nobond");
		Assert.assertEquals("jamesbond", user.getUsername());
		Assert.assertEquals("nobond", user.getPassword());
		Assert.assertEquals("james", user.getFirstname());
		Assert.assertEquals("bond", user.getLastname());
	}
	
	@Test
	public void testLoadTasks(){
		User user = new User();
		user.setUsername("jamesbond");
		user.setPassword("ionicbond");
		user.setFirstname("James");
		user.setLastname("Bond");
		int userId = userService.registerUser(user);
		String sql = "SELECT * FROM USER WHERE ID = " + userId;
		JdbcTemplate jdbcTemplate = JdbcTemplateFactory.getJdbcTemplate();
		User userObject = jdbcTemplate.queryForObject(sql, new UserRowMapper());
		//check whether default tasks are loaded or not
		List<Task> tasksList = userService.loadTasks(userObject.getId());
		Assert.assertEquals("Buy groceries today",tasksList.get(0).getTaskName());
		Assert.assertEquals("Write code tomorrow",tasksList.get(1).getTaskName());
		String sqlTwo = "DELETE FROM TASKS";
		jdbcTemplate.update(sqlTwo);
	}
	
	@Test
	public void testLoadTags(){
		User user = new User();
		user.setUsername("jamesbond");
		user.setPassword("ionicbond");
		user.setFirstname("James");
		user.setLastname("Bond");
		int userId = userService.registerUser(user);
		String sql = "SELECT * FROM USER WHERE ID = " + userId;
		JdbcTemplate jdbcTemplate = JdbcTemplateFactory.getJdbcTemplate();
		User userObject = jdbcTemplate.queryForObject(sql, new UserRowMapper());
		//check whether default tags are loaded or not
		TreeSet<Tag> tags = userService.loadTags(userObject.getId());
		List<Tag> tagsList = new ArrayList<Tag>(tags);
		Assert.assertEquals("home",tagsList.get(0).getTagName());
		Assert.assertEquals("office",tagsList.get(1).getTagName());
		Assert.assertEquals("project",tagsList.get(2).getTagName());
		Assert.assertEquals("vegetables",tagsList.get(3).getTagName());
		String sqlTwo = "DELETE FROM TAGS";
		jdbcTemplate.update(sqlTwo);
	}
	
	@Test
	public void testLoadCategories(){
		User user = new User();
		user.setUsername("jamesbond");
		user.setPassword("ionicbond");
		user.setFirstname("James");
		user.setLastname("Bond");
		int userId = userService.registerUser(user);
		String sql = "SELECT * FROM USER WHERE ID = " + userId;
		JdbcTemplate jdbcTemplate = JdbcTemplateFactory.getJdbcTemplate();
		User userObject = jdbcTemplate.queryForObject(sql, new UserRowMapper());
		//check whether default categories are loaded or not
		TreeSet<Category> categories = userService.loadCategories(userObject.getId());
		List<Category> categoryList = new ArrayList<Category>(categories);
		Assert.assertEquals("personal",categoryList.get(0).getCategoryName());
		Assert.assertEquals("work",categoryList.get(1).getCategoryName());
		String sqlTwo = "DELETE FROM CATEGORIES";
		jdbcTemplate.update(sqlTwo);
	}
}
