package com.townscript.todo.validators;

import java.util.regex.Pattern;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.townscript.todo.model.User;

//Use this class for creating custom complex validations using spring validator
public class UserValidator implements Validator{

	private static final Pattern PASSWORD_PATTERN = 
			Pattern.compile("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,14})");
	@Override
	public boolean supports(Class type) {
		return User.class.equals(type);
	}

	@Override
	public void validate(Object target, Errors errors) {
		User user = (User) target;

	      if(user.getUsername() == "") {
	          errors.rejectValue("username", "required");
	      }
	      if(!isPassword(user.getPassword())) {
	          errors.rejectValue("password", "invalid.password");
	      }
	}
	 private boolean isPassword(String value) {
	      return PASSWORD_PATTERN.matcher(value).matches();
	 }
}
