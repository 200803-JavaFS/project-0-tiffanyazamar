package com.revature.app.main;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.revature.app.exceptions.InsufficientFundException;
import com.revature.app.exceptions.InvalidUserInput;
import com.revature.app.models.BankAccount;
import com.revature.app.models.UserInformation;
import com.revature.app.services.BankService;
import com.revature.app.services.EmployeeService;
import com.revature.app.services.UserService;

public class Driver {

	private static Scanner input;
	private UserInformation loggedInUser; // value of user right now is null;
	private UserService userService = new UserService();
	private EmployeeService employeeService = new EmployeeService();
	private BankService bankService = new BankService();

	private int loginTries = 3;

	// Bang!! Let there be banking
	public void start() {
		printLogo(); // printing the logo
		System.out.println("Please select one of the following options");
		System.out.println("[ 0 ] Log in");
		System.out.println("[ 1 ] Create a new account");
		System.out.println("[ quit ] Quit");
		input = new Scanner(System.in); // Create a Scanner object as global variable so we can use everywhere
		System.out.print("Selection: ");
		String userSelection = input.nextLine(); // capture user selection

		if (userSelection.equals("0")) {
			showLoginScreen();
		} else if (userSelection.equals("1")) {
			createNewAccount();
		} else if (userSelection.toLowerCase().equals("quit")) {
			System.out.println("Goodbye!");
			quit();
		} else {
			System.out.println("Invalid option");
			start();
		}
	}

	/**
	 * Prints the bank logo
	 */
	private void printLogo() {
		System.out.println(
				"  _____  _         _       _____                 _  _  _     _    _         _               ");
		System.out.println(
				" |  __ \\(_)       | |     / ____|               | |(_)| |   | |  | |       (_)              ");
		System.out.println(
				" | |__)| _  _ __  | | __ | |      _ __  ___   __| | _ | |_  | |  | | _ __   _   ___   _ __  ");
		System.out.println(
				" |  ___/| || '_ \\ | |/ / | |     | '__|/ _ \\ / _` || || __| | |  | || '_ \\ | | / _ \\ | '_ \\ ");
		System.out.println(
				" | |    | || | | ||   <  | |____ | |  |  __/| (_| || || |_  | |__| || | | || || (_) || | | |");
		System.out.println(
				" |_|    |_||_| |_||_|\\_\\  \\_____||_|   \\___| \\__,_||_| \\__|  \\____/ |_| |_||_| \\___/ |_| |_|");
		System.out.println();
		printSeparator();
		System.out.println("Welcome to PINK Tiffany's Bank");

	}

	/**
	 * Prints line separator
	 */
	private void printSeparator() {
		System.out.println(
				"-----------------------------------------------------------------------------------------------");
	}

	/**
	 * Guides user through the create new account and Creates new User account and
	 * Bank account
	 */
	private void createNewAccount() {
		printSeparator();
		System.out.print("New Username (must be 6 or more character): ");
		String username = input.next();
		System.out.print("New Password (must be 6 or more character): ");
		String password = input.next();
		System.out.print("First Name: ");
		String firstname = input.next();
		System.out.print("Last Name: ");
		String lastname = input.next();
		System.out.print("Account Type (checking/saving): ");
		String accountType = input.next();
		System.out.print("Intial Deposit Amount: ");
		double initalDepositAmount = 0;
		// Check to make sure user enter a double value for the initial deposit amount
		if (input.hasNextDouble()) {
			initalDepositAmount = input.nextDouble();
		} else {
			// punishing user for doing something BADDD!!!
			System.out.println("Invalid Amount.");
			System.out.println("Scanning for closest branch.....");
			System.out.println(
					"The closest branch is 200 miles away from your location. You must deposit initial amount in person at this branch for security purposes!!!");
		}
		// The first item in this result list is the new user id and second item is the
		// new bank account id
		List<Integer> result = null;
		try {
			result = userService.createNewAccount(username, password, firstname, lastname, accountType,
					initalDepositAmount);
		} catch (InvalidUserInput e) {
			System.out.println(e.getMessage());
			System.out.println("We're redirecting you back to the home page. Please try again!!");
			// show home screen again!!
			start();
		}
		if (result != null && result.size() > 0) {
			System.out.println("Account with username: " + username + " has been successfully created with user ID "
					+ result.get(0) + " and Bank Account ID " + result.get(1)
					+ ". Please wait for approval. You can check your Bank Account status by logging in.");
		} else {
			System.out.println("Failed to create the new account. Please try again later");
		}
		// Show the home screen again!!
		start();
	}

	/**
	 * Shows login screen
	 */
	private void showLoginScreen() {
		printSeparator();
		System.out.println("Please enter your username and password");
		System.out.print("Username: ");
		String username = input.nextLine(); // capture username
		System.out.print("Password: ");
		String password = input.nextLine(); // capture password
		// loggedInUser will be null if username and password don't match
		loggedInUser = userService.login(username, password);
		if (loggedInUser != null) {
			printSeparator();
			System.out.println("Welcome back " + loggedInUser.getFirstname() + "!!");
			System.out.println();
			// Check if user role is employee or customer
			if (loggedInUser.getUserRole().equals("employee")) {
				showEmployeeOptions();
			} else if (loggedInUser.getUserRole().equals("customer")) {
				// We're using the loggedInUser to display the accounts information
				showAccountOptions(loggedInUser);
			} else {
				// The role doesn't match any of the above
				System.out.println("Invalid user role");
				quit();
			}
		} else {
			System.out.println("Incorrect Username or Password");
			// allow 3 wrong tries
			if (--loginTries > 0) {
				System.out.println("You have " + loginTries + " left!");
				showLoginScreen();
			} else {
				System.out.println("You have reach maximum tries");
				quit();
			}
		}
	}

	/**
	 * Shows available options for our lovely customers
	 */
	private void showAccountOptions(UserInformation user) {
		printSeparator();
		System.out.println("Account ID \tAvaiable Balance \tAccount Type \t\tStatus");
		if (user.getBankAccounts().size() > 0) {
			// loop through all the accounts in getBankAccounts()
			displayAccountsOptions(user.getBankAccounts());
			// Make user select an option
			System.out.println("Please select an account ID or an option from above");
			System.out.print("Selection: ");

			// hasNextInt() checks if user input an integer
			if (input.hasNextInt()) {
				int selectedAccountId = input.nextInt();

				BankAccount bankAccount = bankService.getSelectedAccount(user.getBankAccounts(), selectedAccountId);
				// bankAccount should not be null if the selected account ID exists
				if (bankAccount != null) {
					showIndividualAccountOptions(bankAccount);
				} else {
					System.out.println("Invalid Account ID");
				}
			} else {
				String selectedOption = input.next();
				// this will only work for employee because employee has more options prior to
				// this screen
				// User doesn't have a screen prior to this.
				if (selectedOption.toLowerCase().equals("back")
						&& loggedInUser.getUserRole().toLowerCase().equals("employee")) {
					showEmployeeOptions();
				}
				if (selectedOption.toLowerCase().equals("transfer")) {
					transferFundBetweenAccounts(user);
				} else if (selectedOption.toLowerCase().equals("new")) {
					createNewBankAccount(user);
				} else if (selectedOption.toLowerCase().equals("logout")) {
					logout();
				} else {
					System.out.println("Invalid Option. Please try again!");
					showAccountOptions(user);
				}
			}
		} else {
			System.out.println("There's no account available");
		}

	}

	// Transfer between two accounts for the same user
	private void transferFundBetweenAccounts(UserInformation user) {
		System.out.print("Account to transfer from: ");
		int fromID = 0;
		int toID = 0;
		double amount = 0;
		// standard check to make sure user enter valid int selection for account id
		if (input.hasNextInt()) {
			fromID = input.nextInt();
		} else {
			System.out.println("Invalid account ID");
		}
		System.out.print("Account to transfer to: ");
		if (input.hasNextInt()) {
			toID = input.nextInt();
		} else {
			System.out.println("Invalid account ID");
		}
		System.out.print("Amount to transfer: ");
		if (input.hasNextDouble()) {
			amount = input.nextDouble();
		} else {
			System.out.println("Invalid Amount");
		}

		try {
			boolean success = bankService.transferFunds(user, fromID, toID, amount);
			if (success) {
				System.out.println("$" + amount + " was transferred from account " + fromID + " to account " + toID);
			} else {
				System.out.println("Failed to transfer $" + amount + " from account " + fromID + " to account " + toID);
			}
		} catch (InsufficientFundException e) {
			// User doesn't have enough money
			System.out.println(e.getMessage());
		} catch (InvalidUserInput e) {
			// One of the accoutn is not approved
			System.out.println(e.getMessage());
		}

		// automatically go back to the previous page after the transaction
		if (loggedInUser.getUserRole().toLowerCase().equals("employee")) {
			showAccountOptions(user);
		} else {
			showAccountOptions(loggedInUser);
		}
	}

	// Guides user through the create new bank account AND creates new bank account
	private void createNewBankAccount(UserInformation user) {
		printSeparator();
		System.out.println("Please fill out the information below: ");
		System.out.print("Account Type (checking/saving): ");
		String accountType = input.next();
		System.out.print("Initial Deposit: ");
		double amount = 0;
		if (input.hasNextDouble()) {
			amount = input.nextDouble();
		} else {
			System.out.println(
					"Invalid amount. Your account won't be approve unless you make a deposit in person. Which is 200 miles away from your location.");
		}
		int bankAccountID = bankService.createNewAccount(accountType, amount, user);

		if (bankAccountID > 0) {
			System.out.println(
					"Your bank account has been created successfully. The Bank Account ID is: " + bankAccountID);
		} else {
			System.out.println("Failed to create a new bank account. Please contact our support for help.");
		}
		if (loggedInUser.getUserRole().toLowerCase().equals("employee")) {
			showAccountOptions(user);
		} else {
			showAccountOptions(loggedInUser);
		}
	}

	// Display all the accounts for user
	private void displayAccountsOptions(List<BankAccount> bankAccounts) {
		displayAccountsInfo(bankAccounts);
		System.out.println();
		if (loggedInUser.getUserRole().toLowerCase().equals("employee")) {
			System.out.println("[ back ] Go back.");
		}
		System.out.println("[ transfer ] Transfer Fund between accounts.");
		System.out.println("[ new ] Create new Bank Account");
		System.out.println("[ logout ] Logout");
	}

	// shows bank accounts info
	private void displayAccountsInfo(List<BankAccount> bankAccounts) {
		for (int i = 0; i < bankAccounts.size(); i++) {
			System.out.println("[ " + bankAccounts.get(i).getAccountId() + " ] \t\t$"
					+ bankAccounts.get(i).getAvailableAmount() + " \t\t\t" + bankAccounts.get(i).getAccountType() + "\t\t"
					+ bankAccounts.get(i).getAccountStatus());
		}
	}

	// Display single account
	private void showIndividualAccountOptions(BankAccount selectedBankAccount) {
		printSeparator();
		System.out.println("Account Summary for Account ID " + selectedBankAccount.getAccountId());
		System.out.println("Account ID \tAvaiable Balance \tStatus");
		System.out.println(selectedBankAccount.getAccountId() + " \t\t$" + selectedBankAccount.getAvailableAmount()
				+ " \t\t" + selectedBankAccount.getAccountStatus());
		System.out.println();
		System.out.println("Please select an option below for account: ");
		System.out.println("[ 0 ] Withdraw money");
		System.out.println("[ 1 ] Deposit money");
		System.out.println("[ back ] Go back");
		System.out.println("[ logout ] Logout");

		System.out.print("Selection: ");
		String selectedOption = input.next(); // capture username
		switch (selectedOption) {
		case "0":
			withdrawMoney(selectedBankAccount);
			showIndividualAccountOptions(selectedBankAccount);
			break;
		case "1":
			depositMoney(selectedBankAccount);
			showIndividualAccountOptions(selectedBankAccount);
			break;
		case "back":
			if (loggedInUser.getUserRole().toLowerCase().equals("employee")) {
				showEmployeeOptions();
			} else {
				showAccountOptions(loggedInUser);
			}
			break;
		case "logout":
			logout();
			break;
		default:
			System.out.println("Invalid Option!! Try again");
			showIndividualAccountOptions(selectedBankAccount);
		}

	}

	private void logout() {
		System.out.println("Goodbye " + loggedInUser.getFirstname() + "!!");
		System.out.println("Logged out");
		loggedInUser = null;
		start();
	}

	// deposits money into an account
	private void depositMoney(BankAccount selectedBankAccount) {
		printSeparator();
		System.out.print("Enter the amount to deposit: ");
		if (input.hasNextDouble()) {
			double amount = input.nextDouble();
			String message = bankService.deposit(selectedBankAccount, amount);
			System.out.println(message);

		} else {
			System.out.println("Amount must be whole number or decimal.");
		}
	}

	// withdraw money from an account
	private void withdrawMoney(BankAccount selectedBankAccount) {
		printSeparator();
		System.out.print("Enter the amount to withdraw: ");
		if (input.hasNextDouble()) {
			double amount = input.nextDouble();
			String message = bankService.withdraw(selectedBankAccount, amount);
			System.out.println(message);
		} else {
			System.out.println("Amount must be whole number or decimal.");
		}
	}

	private void showEmployeeOptions() {
		printSeparator();
		System.out.println("Please select an option below");
		System.out.println("[ 0 ] Find a user by username.");
		System.out.println("[ 1 ] List all pending accounts.");
		System.out.println("[ 2 ] Deactivate an account.");
		System.out.println("[ logout ] Logout");
		System.out.print("Selection: ");
		String selectedOption = input.next(); // Read user input
		switch (selectedOption) {
		case "0":
			findAUserByUsername();
			break;
		case "1":
			listAllPendingAccounts();
			break;
		case "2":
			deactivateAccount();
			break;
		case "logout":
			logout();
		}
	}

	private void deactivateAccount() {
		System.out.print("Enter a Bank Account ID: ");
		if (input.hasNextInt()) {
			int bankAccountId = input.nextInt();
			boolean success = bankService.deactivateAccount(loggedInUser.getUsername(), bankAccountId);
			if (success) {
				System.out.println("Successfully deactivated Bank Account ID " + bankAccountId + ".");
			} else {
				System.out.println("Failed to deactivate Bank Account ID " + bankAccountId + ". Please try again!");
			}
		} else {
			System.out.println("Invalid Bank Account ID. Must be an integer.");
			deactivateAccount();
		}
		showEmployeeOptions();
	}

	private void listAllPendingAccounts() {
		printSeparator();
		List<BankAccount> pendingAccounts = bankService.getAllPendingAccounts(loggedInUser.getUsername(),
				loggedInUser.getUserRole());

		System.out.println("Account ID \tAvaiable Balance \tAccount Type \tStatus");

		if (pendingAccounts != null && pendingAccounts.size() > 0) {
			for (BankAccount account : pendingAccounts) {
				System.out.println(account.getAccountId() + "\t\t" + account.getAvailableAmount() + "\t\t"
						+ account.getAccountType() + "\t\t" + account.getAccountStatus());
			}
		} else {
			System.out.println("There's no pending account.");
		}
		System.out.println("[ back ] Go back");
		System.out.println("[ logout ] Logout");
		System.out.println("Select an account ID to approve or an option above ");
		System.out.print("Selection: ");
		if (input.hasNextInt()) {
			int accountId = input.nextInt();
			System.out.print("Enter a status (approved/denied): ");
			String status = input.next();
			if (status.toLowerCase().equals("approved")) {
				boolean success = employeeService.approveAccount(accountId, loggedInUser.getUsername(),
						loggedInUser.getUserRole());
				if (success) {
					System.out.println("Bank Account ID " + accountId + " is approved");
				} else {
					System.out.println("Failed to approve Bank Account ID " + accountId);
				}
			} else if (status.toLowerCase().equals("denied")) {
				boolean success = employeeService.denyAccount(accountId, loggedInUser.getUsername(),
						loggedInUser.getUserRole());
				if (success) {
					System.out.println("Bank Account ID " + accountId + " is denied");
				} else {
					System.out.println("Failed to denied Bank Account ID " + accountId);
				}
			}
			listAllPendingAccounts();
		} else {
			String selectedOption = input.next();
			if (selectedOption.toLowerCase().equals("back")) {
				showEmployeeOptions();
			} else if (selectedOption.toLowerCase().equals("logout")) {
				logout();
			}
		}
		
	}

	private void findAUserByUsername() {
		printSeparator();
		System.out.print("Enter a username: ");
		String username = input.next();
		UserInformation user = employeeService.findUserByUsername(loggedInUser.getUserRole(), username);
		if (user != null) {
			System.out.println("Username: " + user.getUsername());
			System.out.println("First name: " + user.getFirstname());
			System.out.println("Last name: " + user.getLastname());
			showAccountOptions(user);
		} else {
			System.out.println("Cannot find the user with username: " + username);
		}
		showEmployeeOptions();
	}

	private void quit() {
		// making sure we are cleaning out the garbage
		input.close();
		userService = null;
		employeeService = null;
		System.exit(0);
	}
}
