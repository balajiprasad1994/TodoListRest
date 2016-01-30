package com.townscript.todo.dao.category;

import com.townscript.todo.model.Category;

public interface CategoryDao {
	int addCategory(Category category);
	void removeCategory(int categoryId);
	Category loadCategory(int categoryId);
	void updateCategory(Category category);
	Category getCategoryofTask(int taskId);
}
