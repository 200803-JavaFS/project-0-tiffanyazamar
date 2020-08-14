package com.revature.app.services;

import com.revature.app.daos.IUserInformationDAO;
import com.revature.app.daos.UserInformationDAO;
import com.revature.app.models.UserInformation;

public class EmployeeService {

	private IUserInformationDAO userInformationDAO = new UserInformationDAO();
	
	public UserInformation findUserByUsername(String username) {
		
		UserInformation user = userInformationDAO.findUserByUsername(username);
		return user;
		
	}

}
