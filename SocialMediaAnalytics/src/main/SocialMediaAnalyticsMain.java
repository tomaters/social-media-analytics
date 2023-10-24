package main;

import view.MenuViewer;
import view.selectioninterfaces.LoginMenuSelection;

public class SocialMediaAnalyticsMain {

	public static void main(String[] args) {
	loginMenu();	
	}

	private static void loginMenu() {
		int input = 0; // selection to navigate menu selections
		boolean closeFlag = false; 
		
		while(!closeFlag) {
			try {
				MenuViewer.viewLoginMenu(); // loginMenu: create account, login, close
				input = MenuViewer.scan.nextInt();
				MenuViewer.scan.nextLine(); // clear scanner buffer
				// ensure that a number from the menu is selected
				if(input < LoginMenuSelection.createAccount || input > LoginMenuSelection.close) {
					System.out.println("Please enter a number in the menu");
					continue;
				}
				 
				switch(input) {
				case LoginMenuSelection.createAccount : createAccount(); break;
				case LoginMenuSelection.login : login(); break;
				case LoginMenuSelection.close : 
					System.out.println("Program closed");
					closeFlag = true;
				}
				
			} catch(Exception e) {
				System.out.println("Error");
				return;
			}
		}
		
	}

	private static void createAccount() throws Exception {
		
	}
	
	private static void login() throws Exception {
		
	}
}