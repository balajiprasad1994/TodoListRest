package com.townscript.todo.service.category;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.townscript.todo.dao.category.CategoryDao;
import com.townscript.todo.model.Category;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService{
	private static final Logger logger = Logger.getLogger(CategoryServiceImpl.class);
	@Autowired
	CategoryDao categoryDao;
	
	public CategoryDao getCategoryDao() {
		return categoryDao;
	}

	public void setCategoryDao(CategoryDao categoryDao) {
		this.categoryDao = categoryDao;
	}

	@Override
	public int addCategory(Category category) {
		return categoryDao.addCategory(category);
	}

	@Override
	public void deleteCategory(int categoryId) {
		categoryDao.removeCategory(categoryId);
		logger.info("deleted category");
	}

	@Override
	public Category readCategory(int categoryId) {
		return categoryDao.loadCategory(categoryId);
	}

	@Override
	public void changeCategoryName(int categoryId, String newCategoryName) {
		Category category = categoryDao.loadCategory(categoryId);
		category.setCategoryName(newCategoryName);
		categoryDao.updateCategory(category);
		logger.info("changed category name");
	}

	@Override
	public void addExistingCategorytoTask(int categoryId, int taskId) {
		Category category = categoryDao.loadCategory(categoryId);
		String taskIds = category.getTaskids();
		String taskIdString = Integer.toString(taskId);
		category.setTaskids(taskIds+", "+taskIdString); //updating taskids of category
		categoryDao.updateCategory(category);
		logger.debug("updated taskids for category - "+category.getCategoryName());
	}

	@Override
	public void deleteCategoryfromTask(int categoryId, int taskId) {
		Category category = categoryDao.loadCategory(categoryId);
		String taskIds = category.getTaskids();
		String taskIdString = Integer.toString(taskId);
		//remove category if it is associated only with the task 
		if(taskIdString == taskIds){ 
			categoryDao.removeCategory(category.getId());
			logger.debug("removed category - "+category.getCategoryName());
		}
		//otherwise remove the taskid from taskids of category
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
