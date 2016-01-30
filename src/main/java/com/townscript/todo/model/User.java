package com.townscript.todo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Proxy;
import org.hibernate.validator.constraints.NotEmpty;

@Proxy(lazy=false)
@Entity
@Table(name = "USER")

@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region="user")
public class User {
	@Id @GeneratedValue
	@Column(name = "ID")
	private int id;
	
	@Column(name = "USERNAME")
	@NotEmpty
	private String username;

	@Column(name = "PASSWORD")
	@NotEmpty
	@Size(min=8,max=14)
	private String password;
	
	@Column(name = "FIRSTNAME")
	@NotEmpty
	private String firstname;

	@Column(name = "LASTNAME")
	@NotEmpty
	private String lastname;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}


	
}
