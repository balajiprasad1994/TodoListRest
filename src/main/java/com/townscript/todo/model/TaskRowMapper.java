package com.townscript.todo.model;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
public class TaskRowMapper implements RowMapper<Task>{

	@Override
	public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		Task alltasks = new Task();
		alltasks.setId(rs.getInt("ID"));
		alltasks.setTaskName(rs.getString("TASK_NAME"));
		alltasks.setSubtask(rs.getBoolean("SUBTASK"));
		alltasks.setMark(rs.getBoolean("MARK"));
		alltasks.setParentid(rs.getInt("PARENT_ID"));
		alltasks.setUserid(rs.getInt("USER_ID"));
		alltasks.setSequenceNumber(rs.getInt("SEQUENCE_NUMBER"));
		
		return alltasks;
	}

}
