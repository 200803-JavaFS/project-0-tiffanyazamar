package com.revature.app.services;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.revature.app.exceptions.InvalidUserInput;
import com.revature.app.models.UserInformation;

public class UserServiceTest {
	private static UserService userService;

	@BeforeClass
	public static void setUp() {
		userService = new UserService();
	}

	@AfterClass
	public static void clear() {
		userService = null;
	}

	@Test
	public void testLogin() {
		UserInformation user = userService.login("test", "test");
		assertNull(user);
	}

	@Test
	public void testCreateNewAccount() {
		List<Integer> user;
		try {
			user = userService.createNewAccount("test", "test", "test", "test", "test", -10);
		} catch (InvalidUserInput e) {
			String message = e.getMessage();
			assertEquals("Username must be at least 6 characters long.", message);
		}
		try {
			user = userService.createNewAccount("test1234", "test", "test", "test", "test", -10);
		} catch (InvalidUserInput e) {
			String message = e.getMessage();
			assertEquals("Password must be at least 6 characters long.", message);
		}
		try {
			user = userService.createNewAccount("test1234", "test1234", "test", "test", "test", -10);
		} catch (InvalidUserInput e) {
			String message = e.getMessage();
			assertEquals("Account type must be either Checking or Saving.", message);
		}
		try {
			user = userService.createNewAccount("test1234", "test1234", "test", "test", "checking", -10);
		} catch (InvalidUserInput e) {
			String message = e.getMessage();
			assertEquals("The initial deposit amount cannot be negative", message);
		}
	}
}
