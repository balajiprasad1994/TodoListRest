package com.townscript.todo.dao.task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;

import com.townscript.todo.dao.JdbcTemplateFactory;
import com.townscript.todo.model.Task;
import com.townscript.todo.model.TaskRowMapper;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
public class TaskDaoImpl implements TaskDao {

	@Override
	public int addTask(Task task) {
		final String sql = "INSERT INTO TASKS(ID,TASK_NAME,USER_ID,MARK,SUBTASK,PARENT_ID,SEQUENCE_NUMBER) " +
		"VALUES (" + task.getId()+ ", '"+task.getTaskName()+ "', "+task.getUserid()+  ", "+task.isMark()+ ", "+task.isSubtask()+ ", "+task.getParentid()+ ", "+task.getSequenceNumber()+")";
		JdbcTemplate jdbcTemplate = JdbcTemplateFactory.getJdbcTemplate();
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(
		  new PreparedStatementCreator() {
			    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
			      return connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			    }
			  }, keyHolder);

		return keyHolder.getKey().intValue();
	}

	@Override
	public Task loadTask(int taskId) {
		String sql = "SELECT * FROM TASKS " + "WHERE ID = "+ taskId;
		JdbcTemplate jdbcTemplate = JdbcTemplateFactory.getJdbcTemplate();
		List<Task> taskList = jdbcTemplate.query(sql, new TaskRowMapper());
		if(taskList == null || taskList.isEmpty()){
			return null;
		}
	
			return taskList.get(0);
	}

	@Override
	public void updateTask(Task task) {
		String sql = "UPDATE TASKS SET TASK_NAME = ?,USER_ID = ?,MARK = ?,SUBTASK = ?,PARENT_ID = ?,SEQUENCE_NUMBER = ?  where ID = ?";
		Object[] params = {task.getTaskName(),task.getUserid(),task.isMark(),task.isSubtask(),task.getParentid(),task.getSequenceNumber(),task.getId()};
		int[] types = {Types.VARCHAR,Types.INTEGER,Types.BOOLEAN,Types.BOOLEAN,Types.INTEGER,Types.INTEGER,Types.INTEGER};
		JdbcTemplate jdbcTemplate = JdbcTemplateFactory.getJdbcTemplate();
		jdbcTemplate.update(sql,params,types);
		
	}

	@Override
	public void removeTask(int taskId) {
		String sql = "delete from TASKS where ID = "+taskId;
		JdbcTemplate jdbcTemplate = JdbcTemplateFactory.getJdbcTemplate();
		jdbcTemplate.update(sql);
	}

	@Override
	public List<Task> loadTasksofUsers(int userId) {
		String sql = "SELECT * FROM TASKS " + "WHERE USER_ID = "+ userId+" AND MARK = "+false;
		JdbcTemplate jdbcTemplate = JdbcTemplateFactory.getJdbcTemplate();
		List<Task> taskList = jdbcTemplate.query(sql, new TaskRowMapper());
		if(taskList == null || taskList.isEmpty()){
			return null;
		}
		
			return taskList;
	}

	@Override
	public List<Task> loadMarkedTasksofUsers(int userId) {
		String sql = "SELECT * FROM TASKS " + "WHERE USER_ID = "+ userId+" AND MARK = "+true;
		JdbcTemplate jdbcTemplate = JdbcTemplateFactory.getJdbcTemplate();
		List<Task> taskList = jdbcTemplate.query(sql, new TaskRowMapper());
		if(taskList == null || taskList.isEmpty()){
			return null;
		}
		
			return taskList;
	}

}
