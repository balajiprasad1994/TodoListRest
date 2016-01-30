package com.townscript.todo.service.category;

import com.townscript.todo.model.Category;

public interface CategoryService {
	int addCategory(Category category);
	void addExistingCategorytoTask(int categoryId, int taskId);
	void deleteCategory(int categoryId);
	void deleteCategoryfromTask(int categoryId, int taskId);
	Category readCategory(int categoryId);
	void changeCategoryName(int categoryId,String newCategoryName);
}
