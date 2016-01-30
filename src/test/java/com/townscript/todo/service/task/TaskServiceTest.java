package com.townscript.todo.service.task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

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

import com.townscript.todo.dao.JdbcTemplateFactory;
import com.townscript.todo.model.Category;
import com.townscript.todo.model.Tag;
import com.townscript.todo.model.TagRowMapper;
import com.townscript.todo.model.Task;
import com.townscript.todo.model.TaskRowMapper;
import com.townscript.todo.model.User;
import com.townscript.todo.service.category.CategoryService;
import com.townscript.todo.service.tag.TagService;
import com.townscript.todo.service.user.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/com/townscript/todo/testbeans.xml")
public class TaskServiceTest {

	@Autowired
	private TaskService taskService;
	@Autowired
	private UserService userService;
	@Autowired
	private TagService tagService;
	@Autowired
	private CategoryService categoryService;
	
	@Before
	public void createEnvironment()
	{
		String sql = "DELETE FROM TASKS";
		String sqlTwo = "DELETE FROM USER";
		 
		JdbcTemplate jdbcTemplate = JdbcTemplateFactory.getJdbcTemplate();
		jdbcTemplate.update(sql);
		jdbcTemplate.update(sqlTwo);
		
		System.out.println("Set Up Environment");
	}
	

	@After
	public void clearEnvironment()
	{
		String sql = "DELETE FROM TASKS";
		String sqlTwo = "DELETE FROM USER";
		 
		JdbcTemplate jdbcTemplate = JdbcTemplateFactory.getJdbcTemplate();
		jdbcTemplate.update(sql);
		jdbcTemplate.update(sqlTwo);
		
		System.out.println("Clear Environment");
	}
	
	@Test
	public void testAddTask(){
		User user = new User();
		user.setUsername("jamesbond");
		user.setPassword("ionicbond");
		user.setFirstname("James");
		user.setLastname("Bond");
		int userId = userService.registerUser(user);
		Task task = new Task();
		task.setMark(false);
		task.setSubtask(false);
		task.setTaskName("Test task");
		task.setSequenceNumber(0);
		task.setUserid(userId);
		
		int taskId = taskService.addTask(task);
		
		String sql = "SELECT * FROM TASKS WHERE ID = " + taskId;
		
		JdbcTemplate jdbcTemplate = JdbcTemplateFactory.getJdbcTemplate();
		 
		List<Task> tasksList = jdbcTemplate.query(sql, new TaskRowMapper());
		
		Assert.assertEquals(taskId, tasksList.get(0).getId());
		Assert.assertEquals(task.getTaskName(), tasksList.get(0).getTaskName());
		Assert.assertEquals(task.isSubtask(), tasksList.get(0).isSubtask());
		Assert.assertEquals(task.isMark(), tasksList.get(0).isMark());
		Assert.assertEquals(task.getSequenceNumber(), tasksList.get(0).getSequenceNumber());
	}
	
	@Test
	public void testReadTask(){
		User user = new User();
		user.setUsername("jamesbond");
		user.setPassword("ionicbond");
		user.setFirstname("James");
		user.setLastname("Bond");
		int userId = userService.registerUser(user);
		final String sql = "INSERT INTO TASKS(ID,TASK_NAME,USER_ID,MARK,SUBTASK,PARENT_ID,SEQUENCE_NUMBER) VALUES (0,'test-task task',"+userId+",false,false,-1,0)";
		JdbcTemplate jdbcTemplate = JdbcTemplateFactory.getJdbcTemplate();
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(
		  new PreparedStatementCreator() {
			    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
			      return connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			    }
			  }, keyHolder);
		int taskid = keyHolder.getKey().intValue();
		Task task = taskService.readTask(taskid);
		Assert.assertEquals(taskid, task.getId());
		Assert.assertEquals( "test-task task", task.getTaskName());
		Assert.assertEquals(false, task.isSubtask());
		Assert.assertEquals(false, task.isMark());
		Assert.assertEquals(0, task.getSequenceNumber());
	}
	
	@Test
	public void testDeleteTask(){
		User user = new User();
		user.setUsername("jamesbond");
		user.setPassword("ionicbond");
		user.setFirstname("James");
		user.setLastname("Bond");
		int userId = userService.registerUser(user);
		final String sql = "INSERT INTO TASKS(ID,TASK_NAME,USER_ID,MARK,SUBTASK,PARENT_ID,SEQUENCE_NUMBER) VALUES (0,'test-task task',"+userId+",false,false,-1,0)";
		JdbcTemplate jdbcTemplate = JdbcTemplateFactory.getJdbcTemplate();
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(
		  new PreparedStatementCreator() {
			    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
			      return connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			    }
			  }, keyHolder);
		int taskid = keyHolder.getKey().intValue();
		Tag tag = new Tag();
		tag.setTagName("test tag");
		tag.setTaskids("0, "+Integer.toString(taskid)); //assume tag is associated with with task id '0'
		int tagid = tagService.addTag(tag);
		Category category = new Category();
		category.setCategoryName("Test");
		category.setTaskids(Integer.toString(taskid));
		categoryService.addCategory(category);
		taskService.DeleteTask(taskid);
		String sqlTwo = "SELECT * FROM TASKS WHERE ID = " + taskid;
		List<Task> tasksList = jdbcTemplate.query(sqlTwo, new TaskRowMapper());
		Assert.assertTrue(tasksList.isEmpty());
		//check taskids attribute of tag
		String sqlThree = "SELECT * FROM TAGS WHERE ID = " + tagid;
		List<Tag> tagsList = jdbcTemplate.query(sqlThree, new TagRowMapper());
		Assert.assertEquals("0", tagsList.get(0).getTaskids());
	}
	
	@Test
	public void testChangeTaskName(){
		User user = new User();
		user.setUsername("jamesbond");
		user.setPassword("ionicbond");
		user.setFirstname("James");
		user.setLastname("Bond");
		int userId = userService.registerUser(user);
		final String sql = "INSERT INTO TASKS(ID,TASK_NAME,USER_ID,MARK,SUBTASK,PARENT_ID,SEQUENCE_NUMBER) VALUES (0,'test-task task',"+userId+",false,false,-1,0)";
		JdbcTemplate jdbcTemplate = JdbcTemplateFactory.getJdbcTemplate();
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(
		  new PreparedStatementCreator() {
			    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
			      return connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			    }
			  }, keyHolder);
		int taskid = keyHolder.getKey().intValue();
		String newTaskName = "testing bond";
		taskService.changeTaskName(taskid, newTaskName);
		String sqlTwo = "SELECT * FROM TASKS WHERE ID = " + taskid;
		List<Task> tasksList = jdbcTemplate.query(sqlTwo, new TaskRowMapper());
		Assert.assertEquals("testing bond",tasksList.get(0).getTaskName());
	}
	
	@Test
	public void testGetTagsList(){
		User user = new User();
		user.setUsername("jamesbond");
		user.setPassword("ionicbond");
		user.setFirstname("James");
		user.setLastname("Bond");
		int userId = userService.registerUser(user);
		final String sql = "INSERT INTO TASKS(ID,TASK_NAME,USER_ID,MARK,SUBTASK,PARENT_ID,SEQUENCE_NUMBER) VALUES (0,'test-task task',"+userId+",false,false,-1,0)";
		JdbcTemplate jdbcTemplate = JdbcTemplateFactory.getJdbcTemplate();
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(
		  new PreparedStatementCreator() {
			    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
			      return connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			    }
			  }, keyHolder);
		int taskid = keyHolder.getKey().intValue();
		Tag tag = new Tag();
		tag.setTagName("testjames");
		tag.setTaskids(Integer.toString(taskid));
		tagService.addTag(tag);
		Tag tagTwo = new Tag();
		tagTwo.setTagName("testbond");
		tagTwo.setTaskids(Integer.toString(taskid));
		tagService.addTag(tagTwo);
		List<Tag> tagsList = taskService.getTagsList(taskid);
		Assert.assertEquals("testjames", tagsList.get(0).getTagName());
		Assert.assertEquals("testbond", tagsList.get(1).getTagName());
	}
	
	@Test
	public void testGetCategory(){
		User user = new User();
		user.setUsername("jamesbond");
		user.setPassword("ionicbond");
		user.setFirstname("James");
		user.setLastname("Bond");
		int userId = userService.registerUser(user);
		final String sql = "INSERT INTO TASKS(ID,TASK_NAME,USER_ID,MARK,SUBTASK,PARENT_ID,SEQUENCE_NUMBER) VALUES (0,'test-task task',"+userId+",false,false,-1,0)";
		JdbcTemplate jdbcTemplate = JdbcTemplateFactory.getJdbcTemplate();
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(
		  new PreparedStatementCreator() {
			    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
			      return connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			    }
			  }, keyHolder);
		int taskid = keyHolder.getKey().intValue();
		Category category = new Category();
		category.setCategoryName("007");
		category.setTaskids(Integer.toString(taskid));
		categoryService.addCategory(category);
		Category categoryRetrieved = taskService.getCategory(taskid);
		Assert.assertEquals("007",categoryRetrieved.getCategoryName());
	}
	
	@Test
	public void testMakeSubtaskTask(){
		User user = new User();
		user.setUsername("jamesbond");
		user.setPassword("ionicbond");
		user.setFirstname("James");
		user.setLastname("Bond");
		int userId = userService.registerUser(user);
		final String sqlParent = "INSERT INTO TASKS(ID,TASK_NAME,USER_ID,MARK,SUBTASK,PARENT_ID,SEQUENCE_NUMBER) VALUES (0,'test-task task',"+userId+",false,false,-1,0)";
		JdbcTemplate jdbcTemplate = JdbcTemplateFactory.getJdbcTemplate();
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(
		  new PreparedStatementCreator() {
			    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
			      return connection.prepareStatement(sqlParent,Statement.RETURN_GENERATED_KEYS);
			    }
			  }, keyHolder);
		int taskidParent = keyHolder.getKey().intValue();
		final String sql = "INSERT INTO TASKS(ID,TASK_NAME,USER_ID,MARK,SUBTASK,PARENT_ID,SEQUENCE_NUMBER) VALUES (1,'test-task task two',"+userId+",false,true,"+taskidParent+",1)";
		JdbcTemplate jdbcTemplateTwo = JdbcTemplateFactory.getJdbcTemplate();
		KeyHolder keyHolderTwo = new GeneratedKeyHolder();
		jdbcTemplateTwo.update(
		  new PreparedStatementCreator() {
			    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
			      return connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			    }
			  }, keyHolderTwo);
		int taskid = keyHolderTwo.getKey().intValue();
		taskService.makeSubtaskTask(taskid);
		String sqlTwo = "SELECT * FROM TASKS WHERE ID = " + taskid;
		List<Task> tasksList = jdbcTemplate.query(sqlTwo, new TaskRowMapper());
		Assert.assertEquals(false, tasksList.get(0).isSubtask());
		Assert.assertEquals(-1,tasksList.get(0).getParentid());
		
	}
	
	@Test
	public void testMakeTaskSubtask(){
		User user = new User();
		user.setUsername("jamesbond");
		user.setPassword("ionicbond");
		user.setFirstname("James");
		user.setLastname("Bond");
		int userId = userService.registerUser(user);
		final String sqlParent = "INSERT INTO TASKS(ID,TASK_NAME,USER_ID,MARK,SUBTASK,PARENT_ID,SEQUENCE_NUMBER) VALUES (0,'test-task task',"+userId+",false,false,-1,0)";
		JdbcTemplate jdbcTemplate = JdbcTemplateFactory.getJdbcTemplate();
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(
		  new PreparedStatementCreator() {
			    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
			      return connection.prepareStatement(sqlParent,Statement.RETURN_GENERATED_KEYS);
			    }
			  }, keyHolder);
		int taskidParent = keyHolder.getKey().intValue();
		final String sql = "INSERT INTO TASKS(ID,TASK_NAME,USER_ID,MARK,SUBTASK,PARENT_ID,SEQUENCE_NUMBER) VALUES (1,'test-task task two',"+userId+",false,false,-1,1)";
		JdbcTemplate jdbcTemplateTwo = JdbcTemplateFactory.getJdbcTemplate();
		KeyHolder keyHolderTwo = new GeneratedKeyHolder();
		jdbcTemplateTwo.update(
		  new PreparedStatementCreator() {
			    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
			      return connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			    }
			  }, keyHolderTwo);
		int taskid = keyHolderTwo.getKey().intValue();
		taskService.makeTaskSubtask(taskid, taskidParent, 0);
		String sqlTwo = "SELECT * FROM TASKS WHERE ID = " + taskid;
		List<Task> tasksList = jdbcTemplate.query(sqlTwo, new TaskRowMapper());
		Assert.assertEquals(true,tasksList.get(0).isSubtask());
		Assert.assertEquals(taskidParent,tasksList.get(0).getParentid());
		Assert.assertEquals(0, tasksList.get(0).getSequenceNumber());
	}
	
	@Test
	public void testToggleTask(){
		User user = new User();
		user.setUsername("jamesbond");
		user.setPassword("ionicbond");
		user.setFirstname("James");
		user.setLastname("Bond");
		int userId = userService.registerUser(user);
		final String sql = "INSERT INTO TASKS(ID,TASK_NAME,USER_ID,MARK,SUBTASK,PARENT_ID,SEQUENCE_NUMBER) VALUES (0,'test-task task',"+userId+",false,false,-1,0)";
		JdbcTemplate jdbcTemplate = JdbcTemplateFactory.getJdbcTemplate();
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(
		  new PreparedStatementCreator() {
			    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
			      return connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			    }
			  }, keyHolder);
		int taskid = keyHolder.getKey().intValue();
		taskService.toggleTask(taskid, true); //mark task
		String sqlTwo = "SELECT * FROM TASKS WHERE ID = " + taskid;
		List<Task> tasksList = jdbcTemplate.query(sqlTwo, new TaskRowMapper());
		Assert.assertEquals(true, tasksList.get(0).isMark());
		taskService.toggleTask(taskid, false); //unmark task
		String sqlThree = "SELECT * FROM TASKS WHERE ID = " + taskid;
		List<Task> tasksListTwo = jdbcTemplate.query(sqlThree, new TaskRowMapper());
		Assert.assertEquals(false, tasksListTwo.get(0).isMark());
	}
}
