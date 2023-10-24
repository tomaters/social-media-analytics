package controller.datamanagers;

import java.util.Scanner;

import controller.DAO.UsersDAO;
import model.Accounts.UsersVO;

// variables: name, id, pw, email
public class UsersManager {
	
	public static Scanner scan = new Scanner(System.in);
	
	public void createAccount() throws Exception {
		UsersVO usersVO = new UsersVO();
		UsersDAO usersDAO = new UsersDAO();
		
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
		usersDAO.createAccount(usersVO);
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
	
	public String viewAllUsers() throws Exception { // for admin only, returns String username delected or breaks
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
	
	public void viewAccountInfoFromAdmin(String username) {
		System.out.println("Displaying user info:");
		UsersDAO usersDAO = new UsersDAO();
		usersDAO.viewAccountInfoFromAdmin(username);
	}
	
	public void deleteAccountFromAdmin(String username) {
		UsersDAO usersDAO = new UsersDAO();
		viewAccountInfoFromAdmin(username);
		System.out.println("Enter 'Y' to delete this account");
		String input = scan.nextLine();
		if(input.toLowerCase().trim().equals("y")) {
			usersDAO.deleteAccount(username);
		}
		else System.out.println("Deletion cancelled");
	}
	
	public void viewAccount() {
		
	}
	
	public void editAccount() {
		
	}
	
	public void deleteAccount() {
		
	}
}
