package com.revature.app.main;

import java.util.Scanner;

import com.revature.app.models.UserInformation;
import com.revature.app.services.UserService;

public class Driver {

	static Scanner input;
	private UserInformation user; // value of user right now is null;
	public static UserService userService = new UserService();

	public void start() {
		printLogo();
		System.out.println("Please select one of the following");
		System.out.println("[ 0 ] Log in");
		System.out.println("[ 1 ] Create a new account");
		input = new Scanner(System.in); // Create a Scanner object

		String userSelection = input.nextLine(); // Read user input

		if (userSelection.equals("0")) {
			System.out.println("User select login");
			userLogin();

		} else if (userSelection.equals("1")) {
			System.out.println("User creates a new account");
			createNewAccount();
		} else {
			System.out.println("Invalid option");
		}
	}

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
		System.out.println(
				"-----------------------------------------------------------------------------------------------");
		System.out.println("Welcome to PINK Tiffany's Bank");

	}
	private static void createNewAccount() {
		// TODO Auto-generated method stub

	}

	private void userLogin() {
		System.out.println("Please enter your username and password");
		System.out.print("Username: ");
		String username = input.nextLine(); // Read user input
		System.out.print("Password: ");
		String password = input.nextLine(); // Read user input
		user = userService.login(username, password);
		if(user!=null) {
			System.out.println("Hello " + user.getFirstname());
			if(user.getUserRole().equals("employee")) {
				System.out.println("I am employee");
			}
		}else {
			System.out.println("Incorrect Username or Password");
			userLogin();
		}
	}

}
