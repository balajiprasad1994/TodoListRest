package com.townscript.todo.model;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class UserRowMapper implements RowMapper<User>{

	@Override
	public User mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		User allusers = new User();
		allusers.setId(rs.getInt("ID"));
		allusers.setUsername(rs.getString("USERNAME"));
		allusers.setPassword(rs.getString("PASSWORD"));
		allusers.setFirstname(rs.getString("FIRSTNAME"));
		allusers.setLastname(rs.getString("LASTNAME"));
		
		return allusers;
	}

}
