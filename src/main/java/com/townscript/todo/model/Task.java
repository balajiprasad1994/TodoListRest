package com.townscript.todo.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

@Proxy(lazy=false)
@Entity
@Table(name = "TASKS")
public class Task {
	@Id @GeneratedValue
	@Column(name = "ID")
	private int id;
	
	@Column(name = "TASK_NAME")
	private String taskName;
	
	@Column(name = "SUBTASK")
	private boolean subtask;

	@Column(name = "MARK")
	private boolean mark;
	
	@Column(name = "PARENT_ID")
	private int parentid;

	@Column(name = "USER_ID")
	private int userid;
	
	@Column(name = "SEQUENCE_NUMBER")
	private int sequenceNumber;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public boolean isSubtask() {
		return subtask;
	}

	public void setSubtask(boolean subtask) {
		this.subtask = subtask;
	}
	
	public boolean isMark() {
		return mark;
	}

	public void setMark(boolean mark) {
		this.mark = mark;
	}

	public int getParentid() {
		return parentid;
	}

	public void setParentid(int parentid) {
		this.parentid = parentid;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public int getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
}
