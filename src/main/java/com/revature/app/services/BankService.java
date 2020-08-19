package com.revature.app.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.app.daos.BankAccountDAO;
import com.revature.app.daos.IBankAccountDAO;
import com.revature.app.daos.IUserInformationDAO;
import com.revature.app.exceptions.InsufficientFundException;
import com.revature.app.exceptions.InvalidUserInput;
import com.revature.app.models.BankAccount;
import com.revature.app.models.UserInformation;

public class BankService {
	private static final Logger logger = LogManager.getLogger(BankService.class);

	private IBankAccountDAO bankAccountDAO = new BankAccountDAO();

	public List<BankAccount> getAllAccountsForUser(int userId) {
		return bankAccountDAO.findAllBankByUserId(userId);
	}

	public String withdraw(BankAccount selectedBankAccount, double amount) {
		boolean success = false;
		if (selectedBankAccount.getAccountStatus().toLowerCase().equals("approved")) {
			if (amount <= 0) {
				logger.warn("Amount was negative");
				return "Amount cannot be negative";
			} else {
				double finalAmount = selectedBankAccount.getAvailableAmount() - amount;
				if (finalAmount < 0) {
					logger.warn("$" + amount + " was more than available amount in the account");
					return "You don't have enough money to withdraw $" + amount;
				} else {
					success = bankAccountDAO.updateAvailableAmount(selectedBankAccount.getAccountId(), finalAmount);
					if (success) {
						selectedBankAccount.setAvailableAmount(finalAmount);
						logger.info("$" + amount + " was withdraw from account " + selectedBankAccount.getAccountId());
						return "$" + amount + " is dispensing.... Don't forget to take your money";
					}
				}
			}
		} else {
			return "Bank account ID " + selectedBankAccount.getAccountId() + " is not yet approved.";
		}

		return "";
	}

	public String deposit(BankAccount selectedBankAccount, double amount) {
		boolean success = false;
		if (selectedBankAccount.getAccountStatus().toLowerCase().equals("approved")) {

			if (amount <= 0) {
				logger.warn("Amount was negative");
				return "Amount cannot be negative";
			} else {
				double finalAmount = selectedBankAccount.getAvailableAmount() + amount;

				success = bankAccountDAO.updateAvailableAmount(selectedBankAccount.getAccountId(), finalAmount);
				if (success) {
					logger.info("$" + amount + " was deposit into account " + selectedBankAccount.getAccountId());

					selectedBankAccount.setAvailableAmount(finalAmount);
					return "$" + amount + " is deposited into account ID " + selectedBankAccount.getAccountId();
				}

			}
		} else {
			return "Bank account ID " + selectedBankAccount.getAccountId() + " is not yet approved.";
		}
		return "";
	}

	public int createNewAccount(String accountType, double initialDepositAmount, UserInformation loggedInUser) {
		BankAccount bankAccount = new BankAccount(initialDepositAmount, "pending", accountType, loggedInUser.getId());
		int bankAccountId = bankAccountDAO.createNewBankAccount(bankAccount);
		bankAccount.setAccountId(bankAccountId);
		if (loggedInUser.getBankAccounts() != null) {
			loggedInUser.getBankAccounts().add(bankAccount);
		} else {
			List<BankAccount> bankAccounts = new ArrayList<>();
			bankAccounts.add(bankAccount);
			loggedInUser.setBankAccounts(bankAccounts);
		}
		logger.info("Bank account ID: " + bankAccountId + " was created for user " + loggedInUser.getUsername());
		return bankAccountId;
	}

	public List<BankAccount> getAllPendingAccounts(String username, String userRole) {
		if (userRole.toLowerCase().equals("employee")) {
			logger.info("User " + username + " accessed all pending accounts.");
			return bankAccountDAO.getAllPendingAccounts();
		} else {
			logger.warn("User " + username + " with role " + userRole + " tried to access all pending accounts !!");
		}
		return null;
	}

	public BankAccount getSelectedAccount(List<BankAccount> banks, int selectedAccountId) {
		BankAccount bankAccount = banks.stream().filter(item -> item.getAccountId() == selectedAccountId).findAny()
				.orElse(null);
		return bankAccount;
	}

	public boolean deactivateAccount(String username, int bankAccountId) {
		boolean success = bankAccountDAO.updateBankAccountStatus(bankAccountId, "deactivated");
		if (success) {
			logger.info("User " + username + " deactivated Bank Account ID " + bankAccountId);
		} else {
			logger.error("Failed to deactivate Bank Account ID " + bankAccountId);
		}
		return success;
	}

	public boolean transferFunds(UserInformation user, int fromID, int toID, double amount)
			throws InsufficientFundException, InvalidUserInput {
		BankAccount fromAccount = user.getBankAccounts().stream().filter(item -> item.getAccountId() == fromID)
				.findFirst().orElse(null);
		BankAccount toAccount = user.getBankAccounts().stream().filter(item -> item.getAccountId() == toID).findFirst()
				.orElse(null);
		if (fromAccount != null && toAccount != null) {
			double finalFromAccountAmount = fromAccount.getAvailableAmount() - amount;
			if (finalFromAccountAmount < 0) {
				logger.warn(fromAccount + " doesn't have enough ");
				throw new InsufficientFundException(
						"You don't have enough fund from the account " + fromID + " to complete the transfer");
			} else if (!fromAccount.getAccountStatus().toLowerCase().equals("approved")
					|| !toAccount.getAccountStatus().toLowerCase().equals("approved")) {
				logger.warn("One of the account is not yet approved");
				throw new InvalidUserInput("One of the account is not approved");
			} else {
				double toAccountAmount = toAccount.getAvailableAmount() + amount;
				if (bankAccountDAO.updateAvailableAmount(fromID, finalFromAccountAmount)) {
					if (bankAccountDAO.updateAvailableAmount(toID, toAccountAmount)) {
						fromAccount.setAvailableAmount(finalFromAccountAmount);
						toAccount.setAvailableAmount(toAccountAmount);
						logger.info("Transfer " + amount + " from account ID " + fromAccount.getAccountId()
								+ " to account ID " + toAccount);
						return true;
					}
				}
			}
		}
		return false;
	}

}
