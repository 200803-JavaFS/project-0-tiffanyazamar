package com.revature.app.models;

import java.util.List;

public class UserInformation {
	private int id;
	private String username;
	private String userPassword;
	private String firstname;
	private String lastname;
	private String userRole;
	private List<BankAccount> bankAccounts;

	public UserInformation() {
		super();
	}

	public UserInformation(String username, String password, String firstname, String lastname, String userRole) {
		super();
		this.username = username.toLowerCase();
		this.userPassword = password;
		this.firstname = firstname;
		this.lastname = lastname;
		this.userRole = userRole;
	}

	public UserInformation(int id, String username, String firstname, String lastname,
			String userRole) {
		super();
		this.id = id;
		this.username = username.toLowerCase();
		this.firstname = firstname;
		this.lastname = lastname;
		this.userRole = userRole;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public List<BankAccount> getBankAccounts() {
		return bankAccounts;
	}

	public void setBankAccounts(List<BankAccount> bankAccounts) {
		this.bankAccounts = bankAccounts;
	}

}
