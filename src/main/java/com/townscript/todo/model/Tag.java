package com.townscript.todo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

@Proxy(lazy=false)
@Entity
@Table(name = "TAGS")
public class Tag implements Comparable<Tag>{
	@Id @GeneratedValue
	@Column(name = "ID")
	private int id;
	
	@Column(name = "TAG_NAME")
	private String tagName;
	
	@Column(name = "TASKIDS")
	private String taskIds;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public String getTaskids() {
		return taskIds;
	}

	public void setTaskids(String taskids) {
		this.taskIds = taskids;
	}

	@Override
	public int compareTo(Tag tag) {
		return this.getTagName().compareTo(tag.getTagName());
	}

	
	

}
