package com.revature.app.services;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.app.daos.BankAccountDAO;
import com.revature.app.daos.IBankAccountDAO;
import com.revature.app.daos.IUserInformationDAO;
import com.revature.app.daos.UserInformationDAO;
import com.revature.app.models.BankAccount;
import com.revature.app.models.UserInformation;

public class EmployeeService {
	private static final Logger logger = LogManager.getLogger(IUserInformationDAO.class);

	private IUserInformationDAO userInformationDAO = new UserInformationDAO();

	private IBankAccountDAO bankAccountDAO = new BankAccountDAO();

	public UserInformation findUserByUsername(String userRole, String username) {
		if (checkRole(userRole)) {
			UserInformation user = userInformationDAO.findUserByUsername(username);
			if (user != null) {
				List<BankAccount> banks = bankAccountDAO.findAllBankByUserId(user.getId());
				user.setBankAccounts(banks);
			}
			return user;
		}
		return null;
	}

	boolean checkRole(String userRole) {
		if (userRole.toLowerCase().equals("employee")) {
			return true;
		} else {
			logger.error("Intruder Alert!!. Sending email to security team....");
			return false;
		}
	}

	public boolean approveAccount(int accountId, String username, String userRole) {
		if (userRole.toLowerCase().equals("employee")) {
			if (bankAccountDAO.updateBankAccountStatus(accountId, "approved")) {
				logger.info("User [{}] with role [{}] approved bank account ID [{}] successfully.", username, userRole,
						accountId);
				return true;
			} else {
				logger.warn("Failed to approve bank account ID [{}]", accountId);
			}
		} else {
			logger.error("User [{}] with role [{}] tried to approve accoutn ID [{}]", username, userRole, accountId);
		}
		return false;
	}

	public boolean denyAccount(int accountId, String username, String userRole) {
		if (userRole.toLowerCase().equals("employee")) {
			if (bankAccountDAO.updateBankAccountStatus(accountId, "denied")) {
				logger.info("User [{}] with role [{}] denied bank account ID [{}] successfully.", username, userRole,
						accountId);
				return true;
			} else {
				logger.warn("Failed to deny bank account ID [{}]", accountId);
			}
		} else {
			logger.error("User [{}] with role [{}] tried to denied accoutn ID [{}]", username, userRole, accountId);
		}
		return false;
	}
}
