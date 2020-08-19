package com.revature.app.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.revature.app.models.BankAccount;
import com.revature.app.utils.ConnectionUtility;

public class BankAccountDAO implements IBankAccountDAO {

	@Override
	public List<BankAccount> findAllBankByUserId(int userId) {
		// Making connection to the database
		try (Connection connection = ConnectionUtility.getConnection()) {
			// querying to get the user with the matching username and password
			// ? is the placeholder.
			String sql = "SELECT * FROM bank_account ba WHERE ba.user_id = ? ORDER BY ba.account_id asc;";

			// create a preparedStatement
			PreparedStatement statement = connection.prepareStatement(sql);

			// Set username and password into the query above
			statement.setInt(1, userId);

			// Executing the query
			ResultSet result = statement.executeQuery();

			List<BankAccount> bankAccounts = new ArrayList<>();
			// Looping through the resultSet if it's not empty.
			while (result.next()) {
				// Mapping the result from the query into UserInformation POJO
				bankAccounts.add(mapBankAccounts(result));
			}
			return bankAccounts;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean updateAvailableAmount(int accountId, double availableAmount) {
		try (Connection connection = ConnectionUtility.getConnection()) {
			// querying to get the user with the matching username and password
			// ? is the placeholder.
			String sql = "UPDATE bank_account SET available_amount = ? WHERE account_id = ?;";

			// create a preparedStatement
			PreparedStatement statement = connection.prepareStatement(sql);

			// Set username and password into the query above
			statement.setDouble(1, availableAmount);
			statement.setInt(2, accountId);
			statement.execute();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean updateBankAccountStatus(int accountId, String accountStatus) {
		try (Connection connection = ConnectionUtility.getConnection()) {
			// querying to get the user with the matching username and password
			// ? is the placeholder.
			String sql = "UPDATE bank_account SET account_status = ? WHERE account_id = ?;";

			// create a preparedStatement
			PreparedStatement statement = connection.prepareStatement(sql);

			// Set username and password into the query above
			statement.setString(1, accountStatus);
			statement.setInt(2, accountId);
			statement.execute();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public int createNewBankAccount(BankAccount bankAccount) {
		try (Connection connection = ConnectionUtility.getConnection()) {
			// querying to get the user with the matching username and password
			// ? is the placeholder.
			String sql = "INSERT INTO bank_account ( account_status ,account_type, available_amount , user_id ) VALUES (?, ?, ?, ?) RETURNING account_id;";

			// create a preparedStatement
			PreparedStatement statement = connection.prepareStatement(sql);

			// Set username and password into the query above
			statement.setString(1, bankAccount.getAccountStatus());
			statement.setString(2, bankAccount.getAccountType());
			statement.setDouble(3, bankAccount.getAvailableAmount());
			statement.setInt(4, bankAccount.getUserId());
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				return result.getInt("account_id");
			}
			connection.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
		return 0;
	}

	private BankAccount mapBankAccounts(ResultSet result) throws SQLException {
		BankAccount bankAccount = new BankAccount(result.getInt("account_id"), result.getDouble("available_amount"),
				result.getString("account_status"), result.getString("account_type"), result.getInt("user_id"));
		return bankAccount;
	}

	@Override
	public List<BankAccount> getAllPendingAccounts() {
		try (Connection connection = ConnectionUtility.getConnection()) {
			// querying to get the user with the matching username and password
			// ? is the placeholder.
			String sql = "SELECT * FROM bank_account ba WHERE ba.account_status = ? ORDER BY ba.account_id;";

			// create a preparedStatement
			PreparedStatement statement = connection.prepareStatement(sql);

			// Set username and password into the query above
			statement.setString(1, "pending");

			// Executing the query
			ResultSet result = statement.executeQuery();

			List<BankAccount> bankAccounts = new ArrayList<>();
			// Looping through the resultSet if it's not empty.
			while (result.next()) {
				// Mapping the result from the query into UserInformation POJO
				bankAccounts.add(mapBankAccounts(result));
			}
			return bankAccounts;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public BankAccount findAccountByUserIdAndAccountId(int userID, int accountID) {
		try (Connection connection = ConnectionUtility.getConnection()) {
			// querying to get the user with the matching username and password
			// ? is the placeholder.
			String sql = "SELECT * FROM bank_account ba WHERE ba.user_id = ? AND ba.account_id = ?;";

			// create a preparedStatement
			PreparedStatement statement = connection.prepareStatement(sql);

			// Set username and password into the query above
			statement.setInt(1, userID);
			statement.setInt(2, accountID);

			// Executing the query
			ResultSet result = statement.executeQuery();

			BankAccount bankAccount = null;
			// Looping through the resultSet if it's not empty.
			while (result.next()) {
				// Mapping the result from the query into UserInformation POJO
				bankAccount = mapBankAccounts(result);
			}
			return bankAccount;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
