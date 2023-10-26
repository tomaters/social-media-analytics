package controller.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import controller.DBUtil;
import main.SocialMediaAnalyticsMain;
import model.Accounts.UsersVO;

public class UsersDAO {
	// check login
	public boolean login(String username, String password) throws Exception {
		String selectStatement = "SELECT * FROM users WHERE user_id = ? AND user_pass = ?";
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		boolean checkLogin = false; 
		
		try {
			connection = DBUtil.makeConnection();
			preparedStatement = connection.prepareStatement(selectStatement);
			preparedStatement.setString(1, username);
			preparedStatement.setString(2, password);
			resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				checkLogin = true;
			}
		} catch(SQLException e) {
			System.out.println("SQL Error");
		} catch(Exception e) {
			System.out.println("Java Error");
		} finally {
			try {
				if(resultSet != null) resultSet.close();
				if(preparedStatement != null) preparedStatement.close();
				if(connection != null) connection.close();
			} catch(SQLException e) {
				System.out.println("SQL Error");
			}
		}
		return checkLogin;
	}
	// create account
	public void createAccount(UsersVO usersVO) throws Exception {
		String insertStatement = "INSERT INTO users VALUES(?, ?, ?, ?)";
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		try {
			connection = DBUtil.makeConnection();
			preparedStatement = connection.prepareStatement(insertStatement);
			preparedStatement.setString(1, usersVO.getName());
			preparedStatement.setString(2, usersVO.getUsername());
			preparedStatement.setString(3, usersVO.getPassword());
			preparedStatement.setString(4, usersVO.getEmail());
			
			int result = preparedStatement.executeUpdate();
			if(result == 1) {
				System.out.println("[Sign up complete]");
			} else {
				System.out.println("[Sign up failed. Try again]");
			}
		} catch(SQLException e) {
			System.out.println("SQL Error");
		} finally {
			try {
				if(connection != null) connection.close();
				if(preparedStatement != null) preparedStatement.close();
			} catch(SQLException e) {
				System.out.println("SQL Error");
			}
		}
	}
	// check for duplicate usernames
	public boolean checkUsernameOverlap(String username) throws Exception {
		String selectStatement = "SELECT * FROM users WHERE user_id = ?";
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		boolean usernameOverlaps = false;
		
		try {
			connection = DBUtil.makeConnection();
			preparedStatement = connection.prepareStatement(selectStatement);
			preparedStatement.setString(1, username);
			resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				usernameOverlaps = true;
				System.out.println("That username already exists");
			}
		} catch(SQLException e) {
			System.out.println("SQL Error");
		} catch(Exception e) {
			System.out.println("Java Error");
		} finally {
			try {
				if(resultSet != null) resultSet.close();
				if(preparedStatement != null) preparedStatement.close();
				if(connection != null) connection.close();
			} catch(SQLException e) {
				System.out.println("SQL Error");
			}
		}
		return usernameOverlaps;
	} 
	// view all usernames
	public void viewAllUsers() throws Exception { 
		String selectStatement = "SELECT user_id, user_email FROM users ORDER BY user_id";
		UsersVO usersVO = new UsersVO();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int count = 1;
		
		try {
			connection = DBUtil.makeConnection();
			preparedStatement = connection.prepareStatement(selectStatement);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()) {
				usersVO.setUsername(resultSet.getString("user_id"));
				System.out.println(count + ". " + usersVO.getUsername());
				count++;
			}
		} catch(SQLException e) {
			System.out.println("SQL Error");
		} catch(Exception e) {
			System.out.println("Java Error");
		} finally {
			try {
				if(resultSet != null) resultSet.close();
				if(preparedStatement != null) preparedStatement.close();
				if(connection != null) connection.close();
			} catch(SQLException e) {
				System.out.println("SQL Error");
			}
		}
	}
	// check if username matches what is entered
	public boolean checkUsername(String username) throws Exception {
		String selectStatement = "SELECT * FROM users WHERE user_id = ?";
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		boolean usernameExists = false;
		
		try {
			connection = DBUtil.makeConnection();
			preparedStatement = connection.prepareStatement(selectStatement);
			preparedStatement.setString(1, username);
			resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				usernameExists = true;
			}
		} catch(SQLException e) {
			System.out.println("SQL Error");
		} catch(Exception e) {
			System.out.println("Java Error");
		} finally {
			try {
				if(resultSet != null) resultSet.close();
				if(preparedStatement != null) preparedStatement.close();
				if(connection != null) connection.close();
			} catch(SQLException e) {
				System.out.println("SQL Error");
			}
		}
		return usernameExists;
	} 
	// view account information, used by both User and Admin
	public void viewAccountInfo(String username) throws Exception { // view user info for selected account
		String selectStatement = "SELECT * FROM users WHERE user_id = ?";
		UsersVO usersVO = new UsersVO();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connection = DBUtil.makeConnection();
			preparedStatement = connection.prepareStatement(selectStatement);
			preparedStatement.setString(1, username);
			resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				usersVO.setName(resultSet.getString("user_name"));
				usersVO.setUsername(resultSet.getString("user_id"));
				usersVO.setEmail(resultSet.getString("user_email"));
				System.out.printf("[Name]: %s%n[Username]: %s%n[Email]: %s%n", usersVO.getName(), usersVO.getUsername(), usersVO.getEmail());
			}
		} catch(SQLException e) {
			System.out.println("SQL Error");
		} catch(Exception e) {
			System.out.println("Java Error");
		} finally {
			try {
				if(resultSet != null) resultSet.close();
				if(preparedStatement != null) preparedStatement.close();
				if(connection != null) connection.close();
			} catch(SQLException e) {
				System.out.println("SQL Error");
			}
		}
	}
	// delete account
	public void deleteAccount(String username) throws Exception {
		String deleteStatement = "DELETE FROM users WHERE user_id = ?";
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		try {
			connection = DBUtil.makeConnection();
			preparedStatement = connection.prepareStatement(deleteStatement);
			preparedStatement.setString(1, username);
			int result = preparedStatement.executeUpdate();
			if (result == 1) {
				System.out.println("[User account deleted successfully]");
			} else {
				System.out.println("[User failed to delete. Try again]");
			}
		} catch (SQLException e) {
			System.out.println("SQL Error");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Java error");
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				System.out.println("SQL Error");
			}
		}
	}
	// edit account details
	public void editAccountInfo(String userVariable, String newAccountDetail, String username) throws Exception {
		String updateStatement = "UPDATE users SET " + userVariable + " = ? WHERE user_id = ?";
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		try {
			connection = DBUtil.makeConnection();
			preparedStatement = connection.prepareStatement(updateStatement);
			preparedStatement.setString(1, newAccountDetail);
			preparedStatement.setString(2, username);
			
			int result = preparedStatement.executeUpdate();
			if(result == 1) {
				System.out.println("[User information update successful]");
				// is username was changed, apply change to main userVO
				if(userVariable.equals("user_id")){
					SocialMediaAnalyticsMain.getUsersVO().setUsername(newAccountDetail);
				}
			} else {
				System.out.println("[User information update failed. Try again]");
			}
		} catch (SQLException e) {
			System.out.println("SQL Error");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Java error");
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				System.out.println("SQL Error");
			}
		}
	}
}
