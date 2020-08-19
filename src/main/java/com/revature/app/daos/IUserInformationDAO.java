package com.revature.app.daos;

import com.revature.app.models.UserInformation;

public interface IUserInformationDAO {

	public UserInformation findUserByUsername(String username);
	
	public UserInformation logginIn (String username, String password);
	
	public int insertNewUser(UserInformation user);
	
	public boolean updateUserRole(String username, String userRole);
}
