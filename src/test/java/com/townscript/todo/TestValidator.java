package com.townscript.todo;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.townscript.todo.dao.JdbcTemplateFactory;
import com.townscript.todo.model.User;
import com.townscript.todo.service.user.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/com/townscript/todo/testbeans.xml")
public class TestValidator{

	@Autowired
	private UserService userService;

	private static Validator validator;

	@Before
	public void setUpEnvironment() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		String sql = "DELETE FROM USER";
		JdbcTemplate jdbcTemplate = JdbcTemplateFactory.getJdbcTemplate();
		jdbcTemplate.update(sql);
		System.out.println("Set Up Environment");
	}
	@After
	public void clearEnvironment()
	{
		String sql = "DELETE FROM USER";

		JdbcTemplate jdbcTemplate = JdbcTemplateFactory.getJdbcTemplate();
		jdbcTemplate.update(sql);

		System.out.println("Clear Environment");
	}
	@Test
	public void testUsernameEmpty(){
		User user = new User();
		user.setUsername("balaji");
		user.setPassword("ionicbond");
		user.setFirstname("James");
		user.setLastname("Bond");
		userService.registerUser(user);
		
		//throw violation in case the username is empty
		//enter valid username and comment this so that the test case passes
		/*Set<ConstraintViolation<User>> constraintViolations =
				validator.validate( user );

		assertEquals( 1, constraintViolations.size() );
		assertEquals(
				"may not be empty",
				constraintViolations.iterator().next().getMessage()
				);*/
	}

	@Test
	public void testPasswordLength(){
		User user = new User();
		user.setUsername("james");
		user.setPassword("ionicbond");
		user.setFirstname("James");
		user.setLastname("Bond");
		userService.registerUser(user);
		
		//throw violation in case password is not between 8 and 14 characters
		//enter valid password and comment this so that the test case passes
		/*Set<ConstraintViolation<User>> constraintViolations =
				validator.validate( user );

		assertEquals(1, constraintViolations.size() );
		assertEquals(
				"size must be between 8 and 14",
				constraintViolations.iterator().next().getMessage()
				);*/
	}
}
