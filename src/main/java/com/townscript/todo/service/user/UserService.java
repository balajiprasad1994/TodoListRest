package com.townscript.todo.service.user;

import java.util.List;
import java.util.TreeSet;

import com.townscript.todo.model.Category;
import com.townscript.todo.model.Tag;
import com.townscript.todo.model.Task;
import com.townscript.todo.model.User;

public interface UserService {
	int registerUser(User user);
	User authenticateUser(String username,String password);
	void changePassword(int userId, String newPassword);
	User getUserInfo(int userId);
	void deleteUser(int userId);
	List<Task> loadTasks(int userId);
	List<Task> loadMarkedTasks(int userId);
	TreeSet<Tag> loadTags(int userId);
	TreeSet<Category> loadCategories(int userId);
}
