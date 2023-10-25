package main;

import java.util.InputMismatchException;
import java.util.Scanner;

import controller.datamanagers.AccountAnalyticsManager;
import controller.datamanagers.PlatformsManager;
import controller.datamanagers.UsersManager;
import model.Accounts.AdminVO;
import model.Accounts.UsersVO;
import view.MenuViewer;
import view.selectioninterfaces.AdminAccountSelection;
import view.selectioninterfaces.AdminMenuSelection;
import view.selectioninterfaces.LoginMenuSelection;
import view.selectioninterfaces.MainMenuSelection;
import view.selectioninterfaces.UserAccountSelection;
import view.selectioninterfaces.UserPlatformSelection;
import view.selectioninterfaces.ViewPlatformSelection;

public class SocialMediaAnalyticsMain {

	public static Scanner scan = new Scanner(System.in);
	private static UsersVO usersVO;
	private static AdminVO adminVO;
	// condition to close program
	private static boolean closeFlag = false; 
	
	public static void main(String[] args) throws Exception {
		// run loop until condition is flipped
		while(!closeFlag) {
			loginMenu();
			if(adminVO != null && adminVO.isAdminLoggedIn()) {
				adminMenu();
			}
			if(usersVO != null && usersVO.isLoggedIn()) {
				mainMenu();
			}			
		}
			MenuViewer.scan.close();
			System.out.println("[Program closed]");
	}

	private static void loginMenu() {
		int input = 0; // selection to navigate menu selections		
		while(!closeFlag) {
			try {
				MenuViewer.viewLoginMenu(); // loginMenu: create account, login, close
				try {
					input = MenuViewer.scan.nextInt();				
				} catch(InputMismatchException e) { }
				MenuViewer.scan.nextLine(); // clear scanner buffer
				// ensure that a number from the menu is selected
				if(input < LoginMenuSelection.createAccount || input > LoginMenuSelection.close) {
					System.out.println("Please enter a number in the menu");
					System.out.println("-------------------------------------------------------------");
					continue;
				}
				switch(input) {
				case LoginMenuSelection.createAccount : createAccount(); break;
				case LoginMenuSelection.login : login(); return;
				case LoginMenuSelection.close : 	
					closeFlag = true;
				}
			} catch(Exception e) {
				System.out.println("Error");
				return;
			}
		}
	}

	private static void createAccount() throws Exception {
		UsersManager usersManager = new UsersManager();
		usersManager.createAccount();
	}
	
	private static void login() throws Exception {
		String username = null;
		String password = null;
		// construct users to set up booleans for login conditions
		usersVO = new UsersVO();
		adminVO = new AdminVO();
		UsersManager usersManager = new UsersManager();
		boolean checkLogin = false; 
		
		// do-while statement to allow login attempts
		while(!checkLogin) {
			System.out.println("Login to user account (enter 'X' to cancel)");
			System.out.println("Username:");
			username = scan.nextLine();
			// if 'X' is entered, return; setLoggedIn remains false
			if(username.toLowerCase().trim().equals("x")) return;
			System.out.println("Password:");
			password = scan.nextLine();
			// if id/pw matches admin login, set boolean true and return
			if(username.equals(adminVO.getUsername()) && password.equals(adminVO.getPassword())) {
				adminVO.setAdminLoggedIn(true);
				return;
			}
			// call method to check login credentials; if true, exit loop
			checkLogin = usersManager.login(username, password);
		}
		// if loop reaches here, checkLogin is true and login worked; setLoggedin true and assign field variables
		System.out.println("[Login successful]");
		System.out.println("-------------------------------------------------------------");
		usersVO.setLoggedIn(true);
		usersVO.setUsername(username);
		usersVO.setPassword(password);
	}

	private static void adminMenu() throws Exception { // FOR ADMIN, viewing list of all accounts
		UsersManager usersManager = new UsersManager();
		while(adminVO.isAdminLoggedIn()) {
			int input = 0;
			MenuViewer.viewAdminMenu();
			try {
				input = MenuViewer.scan.nextInt();				
			} catch(InputMismatchException e) { }
			MenuViewer.scan.nextLine(); // clear scanner buffer
			// ensure that a number from the menu is selected
			if(input < AdminMenuSelection.viewUserFromAdmin || input > AdminMenuSelection.close) {
				System.out.println("Please enter a number in the menu");
				System.out.println("-------------------------------------------------------------");
				continue;
			}
			switch(input) {
			case AdminMenuSelection.viewUserFromAdmin : 
				String username = usersManager.viewAllUsers(); // displays all users; in this method, need to select account (username) or break early
				if(username.toLowerCase().trim().equals("x")) break;
				viewUsersInfo(username); 
				break;
			case AdminMenuSelection.logout :
				adminVO.setAdminLoggedIn(false);
				System.out.println("[Admin logout successful]");
				System.out.println("-------------------------------------------------------------");
				break;
			case AdminMenuSelection.close : 
				adminVO.setAdminLoggedIn(false);
				usersVO.setLoggedIn(false);
				closeFlag = true; 
			}			
		}
	}

	private static void viewUsersInfo(String username) throws Exception { // FOR ADMIN, viewing / managing info of one account
		UsersManager usersManager = new UsersManager();
		AccountAnalyticsManager accountAnalyticsManager = new AccountAnalyticsManager();
		while(adminVO.isAdminLoggedIn()) {
			int input = 0;
			MenuViewer.viewUsersInfo();
			try {
				input = MenuViewer.scan.nextInt();				
			} catch(InputMismatchException e) { }
			MenuViewer.scan.nextLine(); // clear scanner buffer
			// ensure that a number from the menu is selected
			if(input < AdminAccountSelection.viewAccountInfoFromAdmin || input > AdminAccountSelection.returnToAdminMenu) {
				System.out.println("Please enter a number in the menu");
				System.out.println("-------------------------------------------------------------");
				continue;
			}
			switch(input) {
			case AdminAccountSelection.viewAccountInfoFromAdmin : 
				usersManager.viewAccountInfo(username);
				break;
			case AdminAccountSelection.viewPlatformAnalyticsFromAdmin :
				accountAnalyticsManager.viewTotalAnalyticsFromAdmin(username);
				break;
			case AdminAccountSelection.deleteAccountFromAdmin : 
				usersManager.deleteAccountFromAdmin(username);
				return;
			case AdminAccountSelection.returnToAdminMenu : 
				return;
			}			
		}
	}
	
	public static void mainMenu() throws Exception { // FOR USER, manage platforms / account (secondary menus)
		while(usersVO.isLoggedIn()) {
			int input = 0;
			MenuViewer.viewMainMenu();
			try {
				input = MenuViewer.scan.nextInt();				
			} catch(InputMismatchException e) { }
			MenuViewer.scan.nextLine(); // clear scanner buffer
			// ensure that a number from the menu is selected
			if(input < MainMenuSelection.managePlatforms || input > MainMenuSelection.close) {
				System.out.println("Please enter a number in the menu");
				System.out.println("-------------------------------------------------------------");
				continue;
			}
			switch(input) {
			case MainMenuSelection.managePlatforms : 
				managePlatforms(); // secondary menu
				break;
			case MainMenuSelection.manageAccount :
				manageAccount(); // secondary menu
				break;
			case MainMenuSelection.logout : // log out, return to login screen
				usersVO.setLoggedIn(false);
				System.out.println("Logout successful");
				System.out.println("-------------------------------------------------------------");
				break;
			case MainMenuSelection.close : // close program
				usersVO.setLoggedIn(false);
				closeFlag = true;
				System.out.println("close");
			}
		}			
	}

	private static void managePlatforms() throws Exception { // FOR USER, secondary menu to view / manage platforms
		AccountAnalyticsManager accountAnalyticsManager = new AccountAnalyticsManager();
		PlatformsManager platformsManager = new PlatformsManager();
		while(usersVO.isLoggedIn()) {
			int input = 0;
			MenuViewer.viewPlatform();
			try {
				input = MenuViewer.scan.nextInt();				
			} catch(InputMismatchException e) { }
			MenuViewer.scan.nextLine(); // clear scanner buffer
			// ensure that a number from the menu is selected
			if(input < UserPlatformSelection.viewPlatform || input > UserPlatformSelection.returnToMainMenu) {
				System.out.println("Please enter a number in the menu");
				System.out.println("-------------------------------------------------------------");
				continue;
			}
			switch(input) {
			case UserPlatformSelection.viewPlatform : 
				System.out.println("View user platforms");
				System.out.println("-------------------------------------------------------------");
				platformsManager.checkPlatforms(SocialMediaAnalyticsMain.getUsersVO().getUsername()); // show existing platforms
				String platformSelection = null;
				// loop to assign platFormselection one of the five available platforms selected
				while(platformSelection == null) {
					int _input = 0;
					
					System.out.println("[Select Platform to view]");
					System.out.println("[1] YouTube");
					System.out.println("[2] Instagram");
					System.out.println("[3] Facebook");
					System.out.println("[4] Twitter");
					System.out.println("[5] TikTok");
					try {
						_input = scan.nextInt();						
						scan.nextLine(); // clear buffer
					} catch(InputMismatchException e) {
						System.out.println("Enter a number in the options");
					} if(_input < ViewPlatformSelection.YouTube || _input > ViewPlatformSelection.TikTok) {						
						System.out.println("Enter a number in the options");
					}
					switch(_input) {
					case ViewPlatformSelection.YouTube : platformSelection = "YouTube"; break;
					case ViewPlatformSelection.Instagram : platformSelection = "Instagram"; break;
					case ViewPlatformSelection.Facebook : platformSelection = "Facebook"; break;
					case ViewPlatformSelection.Twitter : platformSelection = "Twitter"; break;
					case ViewPlatformSelection.TikTok : platformSelection = "TikTok";
					}
				}				
				System.out.println("-------------------------------------------------------------");
				System.out.println("[Viewing " + platformSelection + " analytics for " + usersVO.getUsername() + "]");
				System.out.println("-------------------------------------------------------------");
				platformsManager.viewPlatform(platformSelection);
				break;
			case UserPlatformSelection.viewTotalAnalytics :
				accountAnalyticsManager.viewTotalAnalytics();
				break;
			case UserPlatformSelection.addPlatform :
				platformsManager.addPlatform();
				break;
			case UserPlatformSelection.editPlatform :
				platformsManager.editPlatform();
				break;
			case UserPlatformSelection.deletePlatform :
				platformsManager.deletePlatform();
				break;
			case UserPlatformSelection.returnToMainMenu : 
				return;
			}
		}	
	}

	private static void manageAccount() throws Exception { // FOR USER, secondary menu to manage account details
		UsersManager usersManager = new UsersManager();
		while(usersVO.isLoggedIn()) {
			int input = 0;
			MenuViewer.viewUserAccount();
			try {
				input = MenuViewer.scan.nextInt();				
			} catch(InputMismatchException e) { }
			MenuViewer.scan.nextLine(); // clear scanner buffer
			// ensure that a number from the menu is selected
			if(input < UserAccountSelection.viewAccountInfo || input > UserAccountSelection.returnToMainMenu) {
				System.out.println("Please enter a number in the menu");
				System.out.println("-------------------------------------------------------------");
				continue;
			}
			switch(input) {
			case UserAccountSelection.viewAccountInfo : 
				usersManager.viewAccountInfo(usersVO.getUsername());
				break;
			case UserAccountSelection.editAccountInfo :
				usersManager.editAccountInfo(usersVO.getUsername());
				break;
			case UserAccountSelection.deleteAccount :
				usersManager.deleteAccount();
				return; // if account is deleted, return to login menu
			case UserAccountSelection.returnToMainMenu : 
				return;
			}
		}	
	}
	
	public static UsersVO getUsersVO() {
		return usersVO;
	}

	public static void setUsersVO(UsersVO usersVO) {
		SocialMediaAnalyticsMain.usersVO = usersVO;
	}
}