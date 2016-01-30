package com.townscript.todo.dao.tag;

import java.util.List;

import com.townscript.todo.model.Tag;

public interface TagDao {
	int addTag(Tag tag);
	void removeTag(int tagId);
	Tag loadTag(int tagId);
	void updateTag(Tag tag);
	List<Tag> getTagsofTask(int taskId);
}
