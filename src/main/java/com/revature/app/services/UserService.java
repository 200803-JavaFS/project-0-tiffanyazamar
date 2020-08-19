package com.revature.app.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.app.daos.IUserInformationDAO;
import com.revature.app.daos.UserInformationDAO;
import com.revature.app.exceptions.InvalidUserInput;
import com.revature.app.models.UserInformation;

public class UserService {
	private static final Logger logger = LogManager.getLogger(BankService.class);

	// this variable should always be private and is used to communicate with the
	// user_information table
	private IUserInformationDAO userInformationDAO = new UserInformationDAO();

	private BankService bankService = new BankService();

	public UserInformation login(String username, String password) {
		UserInformation user = userInformationDAO.logginIn(username, password);
		if (user != null) {
			user.setBankAccounts(bankService.getAllAccountsForUser(user.getId()));
			logger.info("User " + user.getUsername() + " is logged in.");
		}
		return user;
	}

	public List<Integer> createNewAccount(String username, String password, String firstname, String lastname,
			String accountType, double initialDepositAmount) throws InvalidUserInput {
		if (username.length() < 6) {
			throw new InvalidUserInput("Username must be at least 6 characters long.");
		} else {
			UserInformation existingUser = userInformationDAO.findUserByUsername(username);
			if (existingUser != null) {
				throw new InvalidUserInput("Username " + username + " is already existed. Please try a different one.");
			}
		}
		if (password.length() < 6) {
			throw new InvalidUserInput("Password must be at least 6 characters long.");
		}
		if (!accountType.toLowerCase().equals("checking") && !accountType.toLowerCase().equals("saving")) {
			throw new InvalidUserInput("Account type must be either Checking or Saving.");
		}
		if (initialDepositAmount < 0) {
			throw new InvalidUserInput("The initial deposit amount cannot be negative");
		}
		UserInformation userInformation = new UserInformation(username, password, firstname, lastname, "customer");
		int userID = userInformationDAO.insertNewUser(userInformation);
		userInformation.setId(userID);
		List<Integer> result = null;
		if (userID > 0) {
			result = new ArrayList<>();
			int bankAccountId = bankService.createNewAccount(accountType, initialDepositAmount, userInformation);
			result.add(userID);
			result.add(bankAccountId);
			logger.info("New account was created for user " + username);
		}
		return result;
	}

}
