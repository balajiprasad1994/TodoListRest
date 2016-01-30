package com.townscript.todo.controller.task;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.townscript.todo.model.Category;
import com.townscript.todo.model.Tag;
import com.townscript.todo.model.Task;
import com.townscript.todo.service.task.TaskService;

@RestController
@RequestMapping("/task")
public class TaskController {

	private TaskService taskService;
	public TaskController(){
		if (taskService == null) {
			ApplicationContext context = new ClassPathXmlApplicationContext(
					"com/townscript/todo/mainbeans.xml");
			taskService = (TaskService) context
					.getBean("TaskServiceImpl");
	}
};

@RequestMapping(value = "/read", method=RequestMethod.GET)
public Task readTask(@RequestParam(value="id",required=true) int id){
	return taskService.readTask(id);
}

@RequestMapping(value = "/changen", method=RequestMethod.GET)
public void changeTaskName(@RequestParam(value="id",required=true) int id, @RequestParam(value="name",required=true) String name){
	taskService.changeTaskName(id, name);
}

@RequestMapping(value = "/makestt", method=RequestMethod.GET)
public void makeSubtaskTask(@RequestParam(value="id",required=true) int id){
	taskService.makeSubtaskTask(id);
}

@RequestMapping(value = "/maketst", method=RequestMethod.GET)
public void makeTaskSubtask(@RequestParam(value="id",required=true) int id, @RequestParam(value="pid",required=true) int parentid, @RequestParam(value="sno",required=true) int sequenceNo){
	taskService.makeTaskSubtask(id, parentid, sequenceNo);
}

@RequestMapping(value = "/delete", method=RequestMethod.GET)
public void deleteTask(@RequestParam(value="id",required=true) int id){
	taskService.DeleteTask(id);
}

@RequestMapping(value = "/gettags", method=RequestMethod.GET)
public List<Tag> getTagsList(@RequestParam(value="id",required=true) int id){
	return taskService.getTagsList(id);
}

@RequestMapping(value = "/getc", method=RequestMethod.GET)
public Category getCategory(@RequestParam(value="id",required=true) int id){
	return taskService.getCategory(id);
}

@RequestMapping(value = "/toggle", method=RequestMethod.GET)
public void toggleTask(@RequestParam(value="id",required=true) int id, @RequestParam(value="mark",required=true) Boolean mark){
	taskService.toggleTask(id, mark);
}

@RequestMapping(value ="/showadd",method = RequestMethod.GET)
public ModelAndView showAddTask(){
	 return new ModelAndView("/static/addtask.html"); 
}

@RequestMapping(value ="/add", method=RequestMethod.POST)
public Task addTask(@RequestParam(value="task_name") String taskname,@RequestParam(value="user_id") String userid){
	Task task = new Task();
	task.setMark(false);
	task.setSubtask(false);
	task.setTaskName(taskname);
	task.setSequenceNumber(0);
	task.setUserid(Integer.parseInt(userid));
	taskService.addTask(task);
	return task;
}
}
