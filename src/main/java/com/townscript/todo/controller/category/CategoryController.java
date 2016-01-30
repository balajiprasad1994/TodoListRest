package com.townscript.todo.controller.category;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.townscript.todo.model.Category;
import com.townscript.todo.service.category.CategoryService;

@RestController
@RequestMapping("/category")
public class CategoryController {

	private CategoryService categoryService;
	public CategoryController(){
		if (categoryService == null) {
			ApplicationContext context = new ClassPathXmlApplicationContext(
					"com/townscript/todo/mainbeans.xml");
			categoryService = (CategoryService) context
					.getBean("CategoryServiceImpl");
	}
};
	
@RequestMapping(value = "/read", method=RequestMethod.GET)
public Category readCategory(@RequestParam(value="id",required=true) int id){
	return categoryService.readCategory(id);
}

@RequestMapping(value = "/delete", method=RequestMethod.GET)
public void deleteTag(@RequestParam(value="id",required=true) int id){
	categoryService.deleteCategory(id);
}

@RequestMapping(value = "/changen", method=RequestMethod.GET)
public void changeCategoryName(@RequestParam(value="id",required=true) int id, @RequestParam(value="name",required=true) String name){
	categoryService.changeCategoryName(id, name);
}

@RequestMapping(value = "/addcatttk", method=RequestMethod.GET)
public void addExistingTagtoTask(@RequestParam(value="id",required=true) int categoryId, @RequestParam(value="tid",required=true) int taskid){
	categoryService.addExistingCategorytoTask(categoryId, taskid);
}

@RequestMapping(value = "/addcatftk", method=RequestMethod.GET)
public void deleteCategoryfromTask(@RequestParam(value="id",required=true) int categoryId, @RequestParam(value="tid",required=true) int taskid){
	categoryService.deleteCategoryfromTask(categoryId, taskid);
}

@RequestMapping(value ="/add", method=RequestMethod.POST)
public Category addCategory(@RequestParam(value="name") String catname,@RequestParam(value="tids") String taskids){
	Category category = new Category();
	category.setCategoryName(catname);
	category.setTaskids(taskids);
	categoryService.addCategory(category);
	return category;
}
}
