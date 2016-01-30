package com.townscript.todo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

@Proxy(lazy=false)
@Entity
@Table(name = "CATEGORIES")

public class Category implements Comparable<Category>{
	@Id @GeneratedValue
	@Column(name = "ID")
	private int id;
	
	@Column(name = "CATEGORY_NAME")
	private String categoryName;
	
	@Column(name = "TASKIDS")
	private String taskids;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getTaskids() {
		return taskids;
	}

	public void setTaskids(String taskids) {
		this.taskids = taskids;
	}

	@Override
	public int compareTo(Category category) {
		return this.categoryName.compareTo(category.getCategoryName());
	}
}
