package controller.datamanagers;

import java.util.InputMismatchException;
import java.util.Scanner;

import controller.DAO.AccountAnalyticsDAO;
// import controller.DAO.PlatformsDAO;
import controller.DAO.UsersDAO;
import main.SocialMediaAnalyticsMain;
import model.Accounts.UsersVO;

// variables: name, id, pw, email
public class UsersManager {
	// final variables for switch-case menu of editAccountDetails
	private final int updateName = 1;
	private final int updateUsername = 2;
	private final int updatePassword = 3;
	private final int updateEmail = 4;
	private final int returnToMainMenu = 5;
	private static final String[] column_names = {"user_name", "user_id", "user_pass", "user_email"};
	public static Scanner scan = new Scanner(System.in);
	
	// create an account
	public void createAccount() throws Exception {
		UsersVO usersVO = new UsersVO();
		UsersDAO usersDAO = new UsersDAO();
		AccountAnalyticsDAO accountAnalyticsDAO = new AccountAnalyticsDAO();
		String name = null;
		String username = null;
		String password = null;
		String email = null;
		boolean usernameOverlaps = false;
		System.out.println("[Create an account] Enter user information");
		System.out.println("-------------------------------------------------------------");
		do { // loop to check name length
			System.out.println("Name (max 20 characters):");
			name = scan.nextLine();
			if(name.length() > 20) {
				System.out.println("The name is too long");
				continue;
			}
			break;
		} while(true);
		
		do { // loop to check for username overlap and username length
			usernameOverlaps = false;
			System.out.println("Username (6-20 characters):");
			username = scan.nextLine();
			if(username.length() < 6 || username.length() > 20) {
				System.out.println("The username must be between 6 and 20 characters");
				usernameOverlaps = true;
				continue;
			}
			usernameOverlaps = usersDAO.checkUsernameOverlap(username); // will return true if it does overlap
		} while(usernameOverlaps);
		
		do { // loop to check max password length
			System.out.println("Password (max 20 characters):");
			password = scan.nextLine();		
			if(password.length() > 20) {
				System.out.println("The password is too long");
				continue;
			}
			break;
		} while(true);
		
		do { // loop to check max email length
			System.out.println("Email (max 20 characters):");
			email = scan.nextLine();
			if(email.length() > 20) {
				System.out.println("The email is too long");
				continue;
			}
			break;
		} while(true);
		// create user
		usersVO.setName(name);
		usersVO.setUsername(username);
		usersVO.setPassword(password);
		usersVO.setEmail(email);
		// insert user tuple into database
		usersDAO.createAccount(usersVO);
		// inser default account_analytics tuple into database
		accountAnalyticsDAO.insertDefaultRow(username);
		System.out.println("-------------------------------------------------------------");
	}
	
	public boolean login(String username, String password) throws Exception {
		UsersDAO usersDAO = new UsersDAO();
		boolean checkLogin = usersDAO.login(username, password);
		if(!checkLogin) {
			System.out.println("The username and password you entered is incorrect");
			System.out.println("-------------------------------------------------------------");
		}
		return checkLogin;
	}
	
	public String viewAllUsers() throws Exception { // for admin only, returns String username selected or breaks
		String username = null;
		UsersDAO usersDAO = new UsersDAO();
		while(true) {
			usersDAO.viewAllUsers();
			System.out.println("Enter username of account to view ('X' to close)");
			username = scan.nextLine();
			if(username.toLowerCase().trim().equals("x")) break;
			if(usersDAO.checkUsername(username)) { // if username exists, method will return true
				break;				
			}
			System.out.println("That username does not exist. Try again");
		}
		return username;
	}
	
	public void deleteAccountFromAdmin(String username) throws Exception { // deleting from admin does not require re-authentication
		UsersDAO usersDAO = new UsersDAO();
		
		// not necessary after ON DELETE CASCADE
//		PlatformsDAO platformsDAO = new PlatformsDAO();
//		AccountAnalyticsDAO accountAnalyticsDAO = new AccountAnalyticsDAO(); 
		viewAccountInfo(username);
		System.out.println("[Enter 'Y' to delete this account]");
		String input = scan.nextLine();
		if(input.toLowerCase().trim().equals("y")) {
			
			// needed to delete foreign key tuples first, but now not necessary after ON DELETE CASCADE
//			platformsDAO.deleteAllPlatforms(username);
//			accountAnalyticsDAO.deleteAllAnalytics(username); 
			
			// delete account
			usersDAO.deleteAccount(username);
		}
		else System.out.println("[Deletion cancelled]");
		System.out.println("-------------------------------------------------------------");
	}
	
	public void viewAccountInfo(String username) throws Exception { // used by Admin and User to view user info
		System.out.println("[Displaying user info]");
		UsersDAO usersDAO = new UsersDAO();
		usersDAO.viewAccountInfo(username);
		System.out.println("-------------------------------------------------------------");
	}
	
	public void editAccountInfo(String username) throws Exception { // select what to edit
		UsersDAO usersDAO = new UsersDAO();
		System.out.println("[Displaying user info]");
		usersDAO.viewAccountInfo(username);
		System.out.println("-------------------------------------------------------------");
		System.out.println("[Edit user account information]");
		int input = 0;
		String newAccountDetail = null;
		String userVariable = null;
		while(true) {
			System.out.println("[1] Update name");
			System.out.println("[2] Update username");
			System.out.println("[3] Update password");
			System.out.println("[4] Update email");
			System.out.println("[5] Return to main menu");
			try {
				input = scan.nextInt();
				scan.nextLine(); // clear buffer
			} catch(InputMismatchException e) {
				System.out.println("Enter a number between 1 and 5");
				break;
			} if(input < updateName || input > returnToMainMenu) {
				System.out.println("Enter a number between 1 and 5");
				break;
			}
			switch(input) {
			// code to change user_name value of user in SQL
			case updateName : 
				userVariable = column_names[0]; 
				while(true) { // check that name is not too long
					System.out.println("Enter new name (max 20 characters):");
					newAccountDetail = scan.nextLine().trim();
					if(newAccountDetail.length() > 20) {
						System.out.println("The name is too long");
						continue;
					}
					break;
				} 
				usersDAO.editAccountInfo(userVariable, newAccountDetail, username);
				break;
			// code to change user_id value of user in SQL
			case updateUsername : // to change username, it must not already exist
				userVariable = column_names[1];
				boolean usernameOverlaps = false;
				do { // loop to check for username overlap and username length
					System.out.println("Enter new username (6-20 characters):");
					newAccountDetail = scan.nextLine().trim();
					if(newAccountDetail.length() < 6 || newAccountDetail.length() > 20) {
						System.out.println("The username must be between 6 and 20 characters");
						usernameOverlaps = true;
						continue;
					}
					usernameOverlaps = usersDAO.checkUsernameOverlap(newAccountDetail); // will return true if it does overlap
				} while(usernameOverlaps);
				// if code reaches here, username does not overlap; replace username
				usersDAO.editAccountInfo(userVariable, newAccountDetail, username);
				break;
			// code to change user_pass value of user in SQL
			case updatePassword : 
				userVariable = column_names[2];
				while(true) { // loop to check max password length
					System.out.println("Enter new password (max 20 characters):");
					newAccountDetail = scan.nextLine().trim();		
					if(newAccountDetail.length() > 20) {
						System.out.println("The password is too long");
						continue;
					} break;
				} 
				usersDAO.editAccountInfo(userVariable, newAccountDetail, username);
				break;
			// code to change user_email value of user in SQL	
			case updateEmail : 
				userVariable = column_names[3];
				while(true) { // loop to check max email length
					System.out.println("Enter new email (max 20 characters):");
					newAccountDetail = scan.nextLine().trim();
					if(newAccountDetail.length() > 20) {
						System.out.println("The email is too long");
						continue;
					} break;
				}
				usersDAO.editAccountInfo(userVariable, newAccountDetail, username);
				break;
			case returnToMainMenu : 
				System.out.println("[Returning to main menu]");
				System.out.println("-------------------------------------------------------------");
				return;
			}
			System.out.println("-------------------------------------------------------------");
			System.out.println("[Displaying updated user info]");
			// need to call main usersVO because username may have been changed
			usersDAO.viewAccountInfo(SocialMediaAnalyticsMain.getUsersVO().getUsername());
			System.out.println("-------------------------------------------------------------");
		}
	}
	
	public void deleteAccount() throws Exception { // deleting from user requires re-authentication
		System.out.println("[Delete your account]");
		UsersDAO usersDAO = new UsersDAO();
		String _username = null;
		String password = null;
		System.out.println("Enter username:");
		_username = scan.nextLine();
		System.out.println("Enter password:");
		password = scan.nextLine();
		// get usersVO from main, which has login details stored to check with inputs
		if(_username.equals(SocialMediaAnalyticsMain.getUsersVO().getUsername()) && password.equals(SocialMediaAnalyticsMain.getUsersVO().getPassword())) {
			usersDAO.deleteAccount(_username);
			// set main usersVO logged in status to false to return to login screen
			SocialMediaAnalyticsMain.getUsersVO().setLoggedIn(false);
			System.out.println("Returning to login menu...");
			System.out.println("-------------------------------------------------------------");
		}
		else {
			System.out.println("[User details are incorrect. Try again]");
			System.out.println("-------------------------------------------------------------");
		}
	}
}
