package com.townscript.todo.dao;

import org.springframework.jdbc.core.JdbcTemplate;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class JdbcTemplateFactory {
	private static JdbcTemplate jdbcTemplate;
	public static JdbcTemplate getJdbcTemplate() {
		if(jdbcTemplate==null){
			MysqlDataSource dataSource = new MysqlDataSource();
			dataSource.setDatabaseName("todolist");
			dataSource.setUser("root");
			dataSource.setPassword("mysql");
			dataSource.setServerName("localhost");
			jdbcTemplate = new JdbcTemplate(dataSource);
			
		}
		return jdbcTemplate;
	}
}
