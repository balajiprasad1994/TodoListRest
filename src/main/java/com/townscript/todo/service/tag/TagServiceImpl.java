package com.townscript.todo.service.tag;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.townscript.todo.dao.tag.TagDao;
import com.townscript.todo.model.Tag;

@Service
@Transactional
public class TagServiceImpl implements TagService{
	private static final Logger logger = Logger.getLogger(TagServiceImpl.class);
	@Autowired
	TagDao tagDao;
	
	public TagDao getTagDao() {
		return tagDao;
	}

	public void setTagDao(TagDao tagDao) {
		this.tagDao = tagDao;
	}

	@Override
	public int addTag(Tag tag) {
		return tagDao.addTag(tag);
	}

	@Override
	public void deleteTag(int tagId) {
		tagDao.removeTag(tagId);
		logger.info("deleted tag");
	}

	@Override
	public Tag readTag(int tagId) {
		return tagDao.loadTag(tagId);
	}

	@Override
	public void changeTagName(int tagId, String newTagName) {
		Tag tag = tagDao.loadTag(tagId);
		tag.setTagName(newTagName);
		tagDao.updateTag(tag);
		logger.info("changed tag name");
		
	}

	@Override
	public void addExistingTagtoTask(int tagId, int taskId) {
		Tag tag = tagDao.loadTag(tagId);
		String taskIds = tag.getTaskids();
		String taskIdString = Integer.toString(taskId);
		tag.setTaskids(taskIds+", "+taskIdString); //updating taskids of tag
		tagDao.updateTag(tag);
		logger.debug("updated taskids attribute for tag - " + tag.getTagName());
		
	}

	@Override
	//to remove a tag from task
	public void deleteTagfromTask(int tagId, int taskId) {
		Tag tag = tagDao.loadTag(tagId);
		String taskIds = tag.getTaskids();
		String taskIdString = Integer.toString(taskId);
		//remove tag if it is associated only with this task
		if(taskIdString == taskIds){
			tagDao.removeTag(tag.getId());
			logger.debug("removed tag - "+tag.getTagName());
		}
		//otherwise remove the taskid from taskids of tag
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
