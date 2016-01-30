package com.townscript.todo.service.task;



import java.util.ArrayList;
import java.util.List;

import com.townscript.todo.dao.category.CategoryDao;
import com.townscript.todo.dao.tag.TagDao;
import com.townscript.todo.dao.task.TaskDao;
import com.townscript.todo.model.Category;
import com.townscript.todo.model.Tag;
import com.townscript.todo.model.Task;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TaskServiceImpl implements TaskService{
	private static final Logger logger = Logger.getLogger(TaskServiceImpl.class);

	@Autowired
	TaskDao taskDao;
	@Autowired
	TagDao tagDao;
	@Autowired
	CategoryDao categoryDao;

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
	public int addTask(Task task) {
		return taskDao.addTask(task);
	}

	@Override
	public void toggleTask(int taskId, Boolean value) {
		Task task = taskDao.loadTask(taskId);
		task.setMark(value);
		taskDao.updateTask(task);
		logger.info("Toggled task");
	}

	@Override
	public Task readTask(int taskId) {
		return taskDao.loadTask(taskId);
	}

	@Override
	public void makeSubtaskTask(int taskId) {
		Task task = taskDao.loadTask(taskId);
		task.setSubtask(false);
		task.setParentid(-1);
		taskDao.updateTask(task);
	}

	@Override
	public void makeTaskSubtask(int taskId, int parentId,int sequenceNumber) {
		Task task = taskDao.loadTask(taskId);
		task.setParentid(parentId);
		task.setSubtask(true);
		task.setSequenceNumber(sequenceNumber);
		taskDao.updateTask(task);
	}

	@Override
	public List<Tag> getTagsList(int taskId) {
		List<Tag> tagsList = new ArrayList<Tag>();
		tagsList = tagDao.getTagsofTask(taskId);
		return tagsList;
	}

	@Override
	public Category getCategory(int taskId) {
		return categoryDao.getCategoryofTask(taskId);
	}

	@Override
	public void changeTaskName(int taskId, String newTaskName) {
		Task task = taskDao.loadTask(taskId);
		task.setTaskName(newTaskName);
		taskDao.updateTask(task);
		logger.info("Updated taskname");
	}

	@Override
	public void DeleteTask(int taskId) {
		taskDao.removeTask(taskId);
		//fetch tags of the task being removed
		List<Tag> tagsList = tagDao.getTagsofTask(taskId);
		String taskIdString = Integer.toString(taskId);
		if(tagsList!=null){
			for(Tag tag : tagsList){
				String taskIds = tag.getTaskids();
				//remove tag if it is associated only with the task being removed
				if(taskIdString == taskIds){
					tagDao.removeTag(tag.getId());
					logger.debug("removed tag - "+tag.getTagName());
				}
				//otherwise remove the taskid of the task being removed from taskids of tag
				else{
					String newTaskids = "";
					for (String retval: taskIds.split(", " + taskIdString)){
						newTaskids +=  retval;
					}
					tag.setTaskids(newTaskids);
					logger.debug("updated taskids attribute for - "+tag.getTagName());
					tagDao.updateTag(tag);
				}
			}
		}
		Category category = categoryDao.getCategoryofTask(taskId); //fetch category of the task being removed
		if(category!=null){
			String taskIds = category.getTaskids();
			if(taskIdString == taskIds){ //remove category if it is associated only with the task being removed
				categoryDao.removeCategory(category.getId());
				logger.debug("removed category - "+category.getCategoryName());
			}
			//otherwise remove the taskid of the task being removed from taskids of category
			else{
				String newTaskids = "";
				for (String retval: taskIds.split(", " + taskIdString)){
					newTaskids +=  retval;
				}
				category.setTaskids(newTaskids);
				logger.debug("updated taskids attribute for - "+category.getCategoryName());
				categoryDao.updateCategory(category);
			}
		}
	}
}
