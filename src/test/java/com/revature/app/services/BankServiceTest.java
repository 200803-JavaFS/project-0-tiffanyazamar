package com.revature.app.services;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.revature.app.exceptions.InsufficientFundException;
import com.revature.app.exceptions.InvalidUserInput;
import com.revature.app.models.BankAccount;
import com.revature.app.models.UserInformation;

public class BankServiceTest {

	private static BankService bankService;

	@BeforeClass
	public static void setUp() {
		bankService = new BankService();
	}

	@AfterClass
	public static void clear() {
		bankService = null;
	}

	@Test
	public void testWithdraw() {
		BankAccount bank = new BankAccount(100, "pending", "checking", 0);
		String messaage = bankService.withdraw(bank, 50);
		assertEquals("Bank account ID " + bank.getAccountId() + " is not yet approved.", messaage);

		BankAccount bank2 = new BankAccount(100, "approved", "checking", 0);
		String messaage2 = bankService.withdraw(bank2, -50);
		assertEquals("Amount cannot be negative", messaage2);

		BankAccount bank3 = new BankAccount(100, "approved", "checking", 0);
		String messaage3 = bankService.withdraw(bank3, 150);
		assertEquals("You don't have enough money to withdraw $" + 150.0, messaage3);

		BankAccount bank4 = new BankAccount(100, "approved", "checking", 0);
		String messaage4 = bankService.withdraw(bank4, 100);
		assertEquals("$" + 100.0 + " is dispensing.... Don't forget to take your money", messaage4);
	}

	@Test
	public void testDeposit() {
		BankAccount bank = new BankAccount(100, "pending", "checking", 0);
		String messaage = bankService.deposit(bank, 50);
		assertEquals("Bank account ID " + bank.getAccountId() + " is not yet approved.", messaage);

		BankAccount bank2 = new BankAccount(100, "approved", "checking", 0);
		String messaage2 = bankService.deposit(bank2, -50);
		assertEquals("Amount cannot be negative", messaage2);

		BankAccount bank4 = new BankAccount(100, "approved", "checking", 0);
		String messaage4 = bankService.deposit(bank4, 100);
		assertEquals("$" + 100.0 + " is deposited into account ID " + 0, messaage4);
	}

	@Test
	public void testCreateNewAccount() {
		UserInformation user = new UserInformation(0, "test", "test", "test", "customer");
		int messaage = bankService.createNewAccount("checking", 100, user);
		assertNotEquals(0, messaage);

	}

	@Test
	public void testGetAllPendingAccounts() {
		List<BankAccount> messaage = bankService.getAllPendingAccounts("test", "customer");
		assertNotEquals(0, messaage);

	}

	@Test
	public void testGetSelectedAccount() {
		BankAccount bank2 = new BankAccount(100, "approved", "checking", 0);
		List<BankAccount> banks = new ArrayList<>();
		banks.add(bank2);
		BankAccount messaage = bankService.getSelectedAccount(banks, 0);
		assertEquals(bank2, messaage);
	}

	@Test
	public void testDeactivateAccount() {
		BankAccount bank2 = new BankAccount(100, "approved", "checking", 0);
		List<BankAccount> banks = new ArrayList<>();
		banks.add(bank2);
		boolean messaage = bankService.deactivateAccount("test", -10);
		assertTrue(messaage);
	}

	@Test
	public void testTransferFund() throws InsufficientFundException, InvalidUserInput {
		UserInformation user = new UserInformation(0, "test", "test", "test", "customer");

		BankAccount bank = new BankAccount(100, "pending", "checking", 0);
		BankAccount bank2 = new BankAccount(100, "approved", "checking", 1);
		List<BankAccount> banks = new ArrayList<>();
		banks.add(bank2);
		banks.add(bank);
		user.setBankAccounts(banks);

		boolean message = bankService.transferFunds(user, -1, 1, 100);
		assertFalse(message);

		message = bankService.transferFunds(user, 0, -1, 100);
		assertFalse(message);

		message = bankService.transferFunds(user, 0, 1, 200);
		assertFalse(message);

		// Failed because account status is pending
		message = bankService.transferFunds(user, 0, 1, 200);
		assertFalse(message);

		
		
	}
}
