package com.townscript.todo.dao.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;

import com.townscript.todo.dao.JdbcTemplateFactory;
import com.townscript.todo.model.User;
import com.townscript.todo.model.UserRowMapper;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;


public class UserDaoImpl implements UserDao {

	@Override
	public int addUser(final User user) {
		final String sql = "insert into USER(ID,USERNAME,PASSWORD,FIRSTNAME,LASTNAME) " +
		  "VALUES (" + user.getId()+ ", '" + user.getUsername() +"', '"+ user.getPassword()+"', '"+ user.getFirstname()+"', '"+user.getLastname()+"')";
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
	public void deleteUser(int userId) {
		String sql = "delete from USER where ID = "+userId;
		JdbcTemplate jdbcTemplate = JdbcTemplateFactory.getJdbcTemplate();
		jdbcTemplate.update(sql);
	}

	@Override
	public void updateUser(User user){
		String sql = "update USER set USERNAME = ?,PASSWORD = ?,FIRSTNAME = ?,LASTNAME = ? where ID = ?";
		Object[] params = {user.getUsername(),user.getPassword(),user.getFirstname(),user.getLastname(),user.getId()};
		int[] types = {Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.INTEGER};
		JdbcTemplate jdbcTemplate = JdbcTemplateFactory.getJdbcTemplate();
		jdbcTemplate.update(sql,params,types);
	}
	@Override
	public User isAuthenticUser(String username, String password) {
		String sql = "SELECT * FROM USER " + "WHERE USERNAME = '"+ username+"' AND PASSWORD = '"+ password + "'";
		 
		JdbcTemplate jdbcTemplate = JdbcTemplateFactory.getJdbcTemplate();

		User user = jdbcTemplate.queryForObject(sql, new UserRowMapper());
		return user;
		
	}
	@Override
	public User loadUser(int userId) {
		String sql = "SELECT * FROM USER " + "WHERE ID = "+ userId;
	 
		JdbcTemplate jdbcTemplate = JdbcTemplateFactory.getJdbcTemplate();
	 
		List<User> usersList = jdbcTemplate.query(sql, new UserRowMapper());
		
		if(usersList == null || usersList.isEmpty()){
			return null;
		}
			return usersList.get(0);
		
	}


}
