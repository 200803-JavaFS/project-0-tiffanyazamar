package com.revature.app.daos;

import java.util.List;

import com.revature.app.models.BankAccount;

public interface IBankAccountDAO {

	public List<BankAccount> findAllBankByUserId(int userId);
	
	public boolean updateAvailableAmount(int accountId, double availableAmount);
	
	public boolean updateBankAccountStatus(int accountId, String accountStatus);
	
	public int createNewBankAccount(BankAccount bankAccount);

	public List<BankAccount> getAllPendingAccounts();

	public BankAccount findAccountByUserIdAndAccountId(int userID, int fromID);
	
}
