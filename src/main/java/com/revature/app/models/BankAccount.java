package com.revature.app.models;

public class BankAccount {
	private int accountId;
	private double availableAmount;
	private String accountStatus;
	private String accountType;
	private int userId;

	public BankAccount() {
		super();
	}

	public BankAccount(double availableAmount, String accountStatus, String accountType, int userId) {
		super();
		this.availableAmount = availableAmount;
		this.accountStatus = accountStatus;
		this.accountType = accountType;
		this.userId = userId;
	}

	public BankAccount(int accountId, double availableAmount, String accountStatus, String accountType, int userId) {
		super();
		this.accountId = accountId;
		this.availableAmount = availableAmount;
		this.accountStatus = accountStatus;
		this.accountType = accountType;
		this.userId = userId;
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public double getAvailableAmount() {
		return availableAmount;
	}

	public void setAvailableAmount(double availableAmount) {
		this.availableAmount = availableAmount;
	}

	public String getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

}
