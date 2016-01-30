package com.townscript.todo.dao.task;

import java.util.List;

import com.townscript.todo.model.Task;

public interface TaskDao {
	int addTask(Task task);
	Task loadTask(int taskId);
	List<Task> loadTasksofUsers(int userId);
	List<Task> loadMarkedTasksofUsers(int userId);
	void updateTask(Task task);
	void removeTask(int taskId);
}
