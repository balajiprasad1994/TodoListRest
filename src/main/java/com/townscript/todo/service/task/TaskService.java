package com.townscript.todo.service.task;

import java.util.List;

import com.townscript.todo.model.Category;
import com.townscript.todo.model.Tag;
import com.townscript.todo.model.Task;

public interface TaskService {
	int addTask(Task task);
	void toggleTask(int taskid,Boolean value);
	Task readTask(int taskid);
	void makeSubtaskTask(int taskid);
	void makeTaskSubtask(int taskid,int parentid,int sequenceNumber);
	List<Tag> getTagsList(int taskid);
	Category getCategory(int taskid);
	void changeTaskName(int taskid,String newTaskName);
	void DeleteTask(int taskid);
}
