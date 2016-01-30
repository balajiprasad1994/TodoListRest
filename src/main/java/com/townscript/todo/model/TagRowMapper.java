package com.townscript.todo.model;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
public class TagRowMapper implements RowMapper<Tag>{

	@Override
	public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		Tag alltags = new Tag();
		alltags.setId(rs.getInt("ID"));
		alltags.setTagName(rs.getString("TAG_NAME"));
		alltags.setTaskids(rs.getString("TASKIDS"));
		
		return alltags;
	}

}
