package com.townscript.todo.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.townscript.todo.controller.user.UserController;
import com.townscript.todo.model.Task;
import com.townscript.todo.model.User;
import com.townscript.todo.service.user.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/com/townscript/todo/testContext.xml")
@WebAppConfiguration
public class TestUserController {

	private MockMvc mockMvc;

	@Autowired
	private UserService userServiceMock;

	/*@Autowired
    private WebApplicationContext webApplicationContext;*/
	
	
	@Before
	public void setUp() {
		Mockito.reset(userServiceMock);
		//mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		//use standalone config so that there no conflicts with userService initialization in UserController 
		//and also with userService in testbeans
		mockMvc = MockMvcBuilders.standaloneSetup(new UserController(userServiceMock)).build();
	}

	@Test
	public void testLoadTasks() throws Exception {
		Task first = new Task();
		first.setTaskName("task one");
		first.setUserid(806);
		first.setSequenceNumber(0);
		first.setId(1);
		Task second = new Task();
		second.setTaskName("task two");
		second.setUserid(806);
		second.setSequenceNumber(1);
		second.setId(2);
		
		
		when(userServiceMock.loadTasks(806)).thenReturn(Arrays.asList(first, second));
		
		
		mockMvc.perform(get("/user/loadtasks?id=806").contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", hasSize(2)))
		.andExpect(jsonPath("$[0].id", is(1)))
		.andExpect(jsonPath("$[0].taskName", is("task one")))
		.andExpect(jsonPath("$[0].sequenceNumber", is(0)))
		.andExpect(jsonPath("$[0].userid", is(806)))
		.andExpect(jsonPath("$[1].id", is(2)))
		.andExpect(jsonPath("$[1].taskName", is("task two")))
		.andExpect(jsonPath("$[1].sequenceNumber", is(1)))
		.andExpect(jsonPath("$[1].userid", is(806)));

		verify(userServiceMock).loadTasks(806);
		verifyNoMoreInteractions(userServiceMock);
	}

	@Test
	public void testLoadUser() throws Exception {
		User user = new User();
		user.setId(0);
		user.setFirstname("balaji");
		user.setLastname("prasad");
		user.setUsername("coder");
		user.setPassword("password");
		
		
        when(userServiceMock.getUserInfo(0)).thenReturn(user);
        mockMvc.perform(get("/user/get?id=0")
        		.contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(0)))
                .andExpect(jsonPath("$.username", is("coder")))
                .andExpect(jsonPath("$.password", is("password")))
                .andExpect(jsonPath("$.firstname", is("balaji")))
                .andExpect(jsonPath("$.lastname", is("prasad")));

       verify(userServiceMock).getUserInfo(0);
       verifyNoMoreInteractions(userServiceMock);

	}
	
	@Test
	public void testAddUser() throws Exception {
		
		String firstname = "balaji";
		String lastname = "prasad";
		String username = "coder";
		String password = "password";
		
		User user = new User();
		user.setFirstname(firstname);
		user.setLastname(lastname);
		user.setUsername(username);
		user.setPassword(password);
		
        when(userServiceMock.registerUser(user)).thenReturn(0); //return id 0
        mockMvc.perform(post("/user/add?username=coder&password=password&firstname=balaji&lastname=prasad")
                .contentType(MediaType.APPLICATION_JSON)
                )
                // url returns user json object
        		.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(0))) //make sure id is 0
                .andExpect(jsonPath("$.username", is("coder")))
                .andExpect(jsonPath("$.password", is("password")))
                .andExpect(jsonPath("$.firstname", is("balaji")))
                .andExpect(jsonPath("$.lastname", is("prasad")));

	}

}
