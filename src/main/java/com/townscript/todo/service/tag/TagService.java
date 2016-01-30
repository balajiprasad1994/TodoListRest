package com.townscript.todo.service.tag;

import com.townscript.todo.model.Tag;

public interface TagService {
	int addTag(Tag tag);
	void addExistingTagtoTask(int tagId,int taskId);
	void deleteTag(int tagId);
	void deleteTagfromTask(int tagId,int taskId);
	Tag readTag(int tagId);
	void changeTagName(int tagId, String newTagName);
}
