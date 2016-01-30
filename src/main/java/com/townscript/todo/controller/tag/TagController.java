package com.townscript.todo.controller.tag;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.townscript.todo.model.Tag;
import com.townscript.todo.service.tag.TagService;

@RestController
@RequestMapping("/tag")
public class TagController {
	private TagService tagService;
	public TagController(){
		if (tagService == null) {
			ApplicationContext context = new ClassPathXmlApplicationContext(
					"com/townscript/todo/mainbeans.xml");
			tagService = (TagService) context
					.getBean("TagServiceImpl");
	}
};

@RequestMapping(value = "/read", method=RequestMethod.GET)
public Tag readTag(@RequestParam(value="id",required=true) int id){
	return tagService.readTag(id);
}

@RequestMapping(value = "/delete", method=RequestMethod.GET)
public void deleteTag(@RequestParam(value="id",required=true) int id){
	tagService.deleteTag(id);
}

@RequestMapping(value = "/changen", method=RequestMethod.GET)
public void changeTagName(@RequestParam(value="id",required=true) int id, @RequestParam(value="name",required=true) String name){
	tagService.changeTagName(id, name);
}

@RequestMapping(value = "/addtgttk", method=RequestMethod.GET)
public void addExistingTagtoTask(@RequestParam(value="id",required=true) int tagid, @RequestParam(value="tid",required=true) int taskid){
	tagService.addExistingTagtoTask(tagid, taskid);
}

@RequestMapping(value = "/deltgftk", method=RequestMethod.GET)
public void deleteTagfromTask(@RequestParam(value="id",required=true) int tagid, @RequestParam(value="tid",required=true) int taskid){
	tagService.deleteTagfromTask(tagid, taskid);
}

@RequestMapping(value ="/add", method=RequestMethod.POST)
public Tag addTask(@RequestParam(value="name") String tagname,@RequestParam(value="tids") String taskids){
	Tag tag = new Tag();
	tag.setTagName(tagname);
	tag.setTaskids(taskids);
	tagService.addTag(tag);
	return tag;
}
}
