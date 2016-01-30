package com.townscript.todo.dao.tag;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.townscript.todo.dao.JdbcTemplateFactory;
import com.townscript.todo.model.Tag;
import com.townscript.todo.model.TagRowMapper;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class TagDaoImpl implements TagDao{

	@Override
	public int addTag(Tag tag) {
		final String sql = "insert into TAGS(ID,TAG_NAME,TASKIDS) VALUES (" + tag.getId()+ ", '"+tag.getTagName()+ "', '"+tag.getTaskids()+"')";
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
	public void removeTag(int tagId) {
		String sql = "delete from TAGS where ID = ?";
		Object[] params = {tagId};
		int[] types = {Types.INTEGER};
		JdbcTemplate jdbcTemplate = JdbcTemplateFactory.getJdbcTemplate();
		jdbcTemplate.update(sql,params,types);
		
	}

	@Override
	public Tag loadTag(int tagId) {
		String sql = "SELECT * FROM TAGS " + "WHERE ID = "+ tagId;
		JdbcTemplate jdbcTemplate = JdbcTemplateFactory.getJdbcTemplate();
	 
		List<Tag> tagList = jdbcTemplate.query(sql, new TagRowMapper());
		
		if(tagList == null || tagList.isEmpty()){
			return null;
		}
		
			return tagList.get(0);
	}

	@Override
	public void updateTag(Tag tag) {
		String sql = "UPDATE TAGS SET TAG_NAME = ?, TASKIDS = ? " + "WHERE ID = ?" ;
		Object[] params = { tag.getTagName(), tag.getTaskids(), tag.getId() };
		int[] types = {Types.VARCHAR, Types.VARCHAR, Types.INTEGER};
		JdbcTemplate jdbcTemplate = JdbcTemplateFactory.getJdbcTemplate();
		jdbcTemplate.update(sql, params, types);
	}

	@Override
	public List<Tag> getTagsofTask(int taskId) {
		List<Tag> tagsList = new ArrayList<Tag>();
		String taskidString = Integer.toString(taskId);
		String sql = "SELECT * FROM TAGS " + "WHERE TASKIDS LIKE '%"+taskidString+"%' ";
		JdbcTemplate jdbcTemplate = JdbcTemplateFactory.getJdbcTemplate();
	 
		tagsList = jdbcTemplate.query(sql, new TagRowMapper());
		
		if(tagsList == null || tagsList.isEmpty()){
			return null;
		}
		
			return tagsList;
	}

}
