package com.revature.app.services;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.revature.app.models.UserInformation;

public class EmployeeServiceTest {

	static EmployeeService employeeService;

	@BeforeClass
	public static void setUp() {
		employeeService = new EmployeeService();
	}

	@AfterClass
	public static void clear() {
		employeeService = null;
	}

	@Test
	public void testApproveAccount() {
		boolean success = employeeService.approveAccount(0, "test", "customer");
		assertFalse(success);
	}

	@Test
	public void testDenyAccount() {
		boolean success = employeeService.denyAccount(0, "test", "customer");
		assertFalse(success);
	}

	@Test
	public void testCheckRole() {
		boolean success = employeeService.checkRole("customer");
		assertFalse(success);
		success = employeeService.checkRole("employee");
		assertTrue(success);
	}

	@Test
	public void testFindUser() {
		UserInformation user = employeeService.findUserByUsername("test", "customer");
		assertNull(user);
	}
}
