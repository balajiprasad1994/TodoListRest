package com.townscript.todo.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
public class CategoryRowMapper implements RowMapper<Category>{

	@Override
	public Category mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		Category allcategories = new Category();
		allcategories.setId(rs.getInt("ID"));
		allcategories.setCategoryName(rs.getString("CATEGORY_NAME"));
		allcategories.setTaskids(rs.getString("TASKIDS"));
		
		return allcategories;
	}

}
