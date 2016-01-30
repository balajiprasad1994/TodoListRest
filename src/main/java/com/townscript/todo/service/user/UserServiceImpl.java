package com.townscript.todo.service.user;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import com.townscript.todo.dao.category.CategoryDao;
import com.townscript.todo.dao.tag.TagDao;
import com.townscript.todo.dao.task.TaskDao;
import com.townscript.todo.dao.user.UserDao;
import com.townscript.todo.model.Category;
import com.townscript.todo.model.Tag;
import com.townscript.todo.model.Task;
import com.townscript.todo.model.User;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService{
	
	private static final Logger logger = Logger.getLogger(UserServiceImpl.class);
	@Autowired
	UserDao userDao;
	@Autowired
	TaskDao taskDao;
	@Autowired
	TagDao tagDao;
	@Autowired
	CategoryDao categoryDao;
	
	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	public TaskDao getTaskDao() {
		return taskDao;
	}

	public void setTaskDao(TaskDao taskDao) {
		this.taskDao = taskDao;
	}

	public TagDao getTagDao() {
		return tagDao;
	}

	public void setTagDao(TagDao tagDao) {
		this.tagDao = tagDao;
	}

	public CategoryDao getCategoryDao() {
		return categoryDao;
	}

	public void setCategoryDao(CategoryDao categoryDao) {
		this.categoryDao = categoryDao;
	}

	@Override
	public int registerUser(User user) {
		int userId = userDao.addUser(user);
		//add default tasks
		Task defaultTask1 = new Task();
		Task defaultTask2 = new Task();
		defaultTask1.setTaskName("Buy groceries today");
		defaultTask2.setTaskName("Write code tomorrow");
		defaultTask1.setUserid(userId);
		defaultTask2.setUserid(userId);
		int taskId1 = taskDao.addTask(defaultTask1);
		int taskId2 = taskDao.addTask(defaultTask2);
		String defaulttask1Id = Integer.toString(taskId1);
		String defaulttask2Id = Integer.toString(taskId2);
		logger.info("Added default tasks");
		//add default tags
		Tag defaulttag1 = new Tag();
		Tag defaulttag2 = new Tag();
		Tag defaulttag3 = new Tag();
		Tag defaulttag4 = new Tag();
		defaulttag1.setTagName("office");
		defaulttag1.setTaskids(defaulttask2Id);
		defaulttag2.setTagName("home");
		defaulttag2.setTaskids(defaulttask1Id);
		defaulttag3.setTagName("vegetables");
		defaulttag3.setTaskids(defaulttask1Id);
		defaulttag4.setTagName("project");
		defaulttag4.setTaskids(defaulttask2Id);
		tagDao.addTag(defaulttag1);
		tagDao.addTag(defaulttag2);
		tagDao.addTag(defaulttag3);
		tagDao.addTag(defaulttag4);
		logger.info("Added default tags");
		//add default categories
		Category defaultCategory1 = new Category();
		Category defaultCategory2 = new Category();
		defaultCategory1.setCategoryName("work");
		defaultCategory1.setTaskids(defaulttask2Id);
		defaultCategory2.setCategoryName("personal");
		defaultCategory2.setTaskids(defaulttask1Id);
		categoryDao.addCategory(defaultCategory1);
		categoryDao.addCategory(defaultCategory2);
		logger.info("Added default categories");
		return userId;
	}

	@Override
	public User authenticateUser(String username, String password){
		
			User user = userDao.isAuthenticUser(username, password);
			logger.info("Authentication done");
			return user;
		
	}
	

	@Override
	public void changePassword(int userId, String newPassword){
		User user = userDao.loadUser(userId);
		user.setPassword(newPassword);
		userDao.updateUser(user);
		logger.info("Password changed for - " + user.getFirstname());
	}

	@Override
	public User getUserInfo(int userId) {
		return userDao.loadUser(userId);
	}

	@Override
	public void deleteUser(int userId) {
		userDao.deleteUser(userId);	
		logger.info("User deleted for Id - " + userId);
	}

	@Override
	public List<Task> loadTasks(int userId) {
		List<Task> tasksList = taskDao.loadTasksofUsers(userId);
		return tasksList;
	}

	//load tags of all tasks of the user
	@Override
	public TreeSet<Tag> loadTags(int userId) { 
		TreeSet<Tag> tagsList = new TreeSet<Tag>(); //use treeset to disallow duplicates and get alphabetical order of tags
		List<Task> tasksList = taskDao.loadTasksofUsers(userId);
		List<Task> markedTasksList = taskDao.loadMarkedTasksofUsers(userId);
		for (Task task : tasksList) {
			logger.debug("Processing tags of task - " + task.getTaskName());
			List<Tag> tagsListofTask = new ArrayList<Tag>();
			tagsListofTask = tagDao.getTagsofTask(task.getId());
			for(Tag tag : tagsListofTask){
				tagsList.add(tag);
			}
		}
		for (Task task : markedTasksList) {
			logger.debug("Processing tags of task - " + task.getTaskName());
			List<Tag> tagsListofTask = new ArrayList<Tag>();
			tagsListofTask = tagDao.getTagsofTask(task.getId());
			for(Tag tag : tagsListofTask){
				tagsList.add(tag);
			}
		}
		logger.info("Loaded tags");
		return tagsList;
	}

	//load categories of all tasks of the user
	@Override
	public TreeSet<Category> loadCategories(int userId) {
		TreeSet<Category> categoriesList = new TreeSet<Category>(); //use treeset to disallow duplicates and get alphabetical order of categories
		List<Task> tasksList = taskDao.loadTasksofUsers(userId);
		List<Task> markedTasksList = taskDao.loadMarkedTasksofUsers(userId);
		for (Task task : tasksList) {
			logger.debug("Processing categories of task - " + task.getTaskName());
		    Category category = categoryDao.getCategoryofTask(task.getId());
		    if(category!=null){
		    categoriesList.add(category);
		    }
		}
		for (Task task : markedTasksList) {
			logger.debug("Processing categories of task - " + task.getTaskName());
		    Category category = categoryDao.getCategoryofTask(task.getId());
		    if(category!=null){
		    categoriesList.add(category);
		    }
		}
		logger.info("Loaded categories");
		return categoriesList;
	}

	@Override
	public List<Task> loadMarkedTasks(int userId) {
		List<Task> tasksList = taskDao.loadMarkedTasksofUsers(userId);
		return tasksList;
	}
}
