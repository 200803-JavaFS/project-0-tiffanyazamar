package com.revature.app.services;

import com.revature.app.daos.IUserInformationDAO;
import com.revature.app.daos.UserInformationDAO;
import com.revature.app.models.UserInformation;

public class UserService {
	// this variable should always be private and is used to communicate with the user_information table
	private IUserInformationDAO userInformationDAO = new UserInformationDAO();
	
	public UserInformation login(String username, String password) {
		UserInformation user = userInformationDAO.logginIn(username, password);
		return user;
	}
	
}
