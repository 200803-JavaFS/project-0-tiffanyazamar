package com.revature.app.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.app.models.UserInformation;
import com.revature.app.utils.ConnectionUtility;

public class UserInformationDAO implements IUserInformationDAO {
	private static final Logger logger = LogManager.getLogger(IUserInformationDAO.class);

	/*
	 * This method will find user by username
	 */
	@Override
	public UserInformation findUserByUsername(String username) {
		// Making connection to the database
		try (Connection connection = ConnectionUtility.getConnection()) {
			// querying to get the user with the matching username and password
			// ? is the placeholder.
			String sql = "SELECT * FROM user_information ui WHERE ui.username = ?;";

			// create a preparedStatement
			PreparedStatement statement = connection.prepareStatement(sql);

			// Set username and password into the query above
			statement.setString(1, username.toLowerCase());

			// Executing the query
			ResultSet result = statement.executeQuery();

			// Looping through the resultSet if it's not empty.
			while (result.next()) {
				// Mapping the result from the query into UserInformation POJO
				return mapUser(result);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Checks if the username and password match with any existing user.
	 */
	/**
	 *
	 */
	@Override
	public UserInformation logginIn(String username, String password) {
		// Making connection to the database
		try (Connection connection = ConnectionUtility.getConnection()) {
			// querying to get the user with the matching username and password
			// ? is the placeholder.
			String sql = "SELECT * FROM user_information ui WHERE ui.username  = ? AND ui.user_password = ?;";

			// create a preparedStatement
			PreparedStatement statement = connection.prepareStatement(sql);

			// Set username and password into the query above
			statement.setString(1, username.toLowerCase());
			statement.setString(2, password);
			// Executing the query
			ResultSet result = statement.executeQuery();

			// Looping through the resultSet if it's not empty.
			while (result.next()) {
				// Mapping the result from the query into UserInformation POJO
				return mapUser(result);
			}
		} catch (SQLException e) {
			logger.error("Failed to lookup user [{}]", username);
			return null;
		}
		return null;
	}

	@Override
	public int insertNewUser(UserInformation user) {
		try (Connection connection = ConnectionUtility.getConnection()) {
			// querying to get the user with the matching username and password
			// ? is the placeholder.
			String sql = "INSERT INTO user_information (username, user_password, firstname, lastname, user_role) VALUES (?, ?, ?, ?, ?) RETURNING id";

			// create a preparedStatement
			PreparedStatement statement = connection.prepareStatement(sql);

			// Set username and password into the query above
			statement.setString(1, user.getUsername());
			statement.setString(2, user.getUserPassword());
			statement.setString(3, user.getFirstname());
			statement.setString(4, user.getLastname());
			statement.setString(5, user.getUserRole());
			// Executing the query
			ResultSet resultSet = statement.executeQuery();
			while(resultSet.next()) {
				
				return resultSet.getInt("id");
			}

		} catch (SQLException e) {
			logger.error("Failed to create new user [{}]", user.getUsername(), e.getMessage());
			return 0;
		}
		return 0;
	}

	@Override
	public boolean updateUserRole(String username, String userRole) {
		try (Connection connection = ConnectionUtility.getConnection()) {
			// querying to get the user with the matching username and password
			// ? is the placeholder.
			String sql = "UPDATE user_information as ui SET ui.user_role = ? WHERE ui.username = ?";

			// create a preparedStatement
			PreparedStatement statement = connection.prepareStatement(sql);

			// Set user_role into the query above
			statement.setString(1, userRole);
			statement.setString(2, username);
			// Executing the query
			return statement.execute();
		} catch (SQLException e) {
			logger.error("Failed to update role for user [{}]", username);
			return false;
		}
	}

	/**
	 * Maps the result from the user_information table into our UserInformation
	 * object
	 * 
	 * @param result
	 * @return
	 * @throws SQLException
	 */
	private UserInformation mapUser(ResultSet result) throws SQLException {
		UserInformation userInformation = new UserInformation(result.getInt("id"), result.getString("username"),
				result.getString("firstname"), result.getString("lastname"), result.getString("user_role"));
		return userInformation;
	}

}
