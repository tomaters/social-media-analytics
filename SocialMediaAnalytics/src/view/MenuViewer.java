package view;

import java.util.Scanner;

public class MenuViewer {

	public static Scanner scan = new Scanner(System.in);
	// first menu of program requires login. can create account or login to user/admin
	public static void viewLoginMenu() { // LoginMenuSelection interface contains final variables for menu direction
		System.out.println("Log in");
		System.out.println("[1] Create user account"); // createAccount
		System.out.println("[2] Log in to user"); // login
		System.out.println("[3] Close program"); // close
		System.out.println("-------------------------------------------------------------");
	}
	// main menu for user login - view and change platform info or user account info
	public static void viewMainMenu() { // MainMenuSelection
		System.out.println("Social Media Analytics");
		System.out.println("[1] Manage platforms"); // managePlatforms
		System.out.println("[2] Manage user account"); //manageAccount
		System.out.println("[3] Log out"); // logout
		System.out.println("[4] Close program"); // close
		System.out.println("-------------------------------------------------------------");
	}
	// secondary menu for user on view/change platform info
	public static void viewPlatform() { // UserPlatformSelection
		System.out.println("Manage user platforms");
		System.out.println("[1] View user platforms"); // viewPlatforms
		System.out.println("[2] View platform analytics"); // viewAnalytics
		System.out.println("[3] Add user platform"); // addPlatform
		System.out.println("[4] Edit user platform"); // editPlatform
		System.out.println("[5] Delete user platform"); // deletePlatform
		System.out.println("[6] Return to main menu"); // returnToMainMenu
		System.out.println("-------------------------------------------------------------");
	}
	// secondary menu for user on view/change account info
	public static void viewUserAccount() { // UserAccountSelection
		System.out.println("Manage user account");
		System.out.println("[1] View user account"); //viewAccount
		System.out.println("[2] Edit user information"); //editAccount
		System.out.println("[3] Delete user account"); // deleteAccount
		System.out.println("[4] Return to main menu"); // returnToMainMenu
		System.out.println("-------------------------------------------------------------");
	}
	// main menu for admin login - view all accounts
	public static void viewAdminMenu() { // AdminMenuSelection
		System.out.println("Admin Menu"); 
		System.out.println("[1] View all users information"); // viewUserFromAdmin
		System.out.println("[2] Log out"); // logout
		System.out.println("[3] Close program"); // close
		System.out.println("-------------------------------------------------------------");
	}
	// secondary menu for admin - upon selection of account, view account info, view platform info, delete account
	public static void viewUsersInfo() { // AdminAccountSelection
		// show all user information
		System.out.println("[Admin] User information"); 
		System.out.println("[1] View account information"); // viewAccountInfoFromAdmin
		System.out.println("[2] View account platform analytics"); // viewPlatformAnalyticsFromAdmin
		System.out.println("[3] Delete user account"); // deleteAccountFromAdmin
		System.out.println("[4] Return to main menu"); // returnToAdminMenu
		System.out.println("-------------------------------------------------------------");
	}
}

