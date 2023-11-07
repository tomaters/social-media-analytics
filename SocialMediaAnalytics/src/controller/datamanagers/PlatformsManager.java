package controller.datamanagers;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

// import controller.DAO.AccountAnalyticsDAO; (CHANGED TO TRIGGERS)
import controller.DAO.PlatformsDAO;
import main.SocialMediaAnalyticsMain;
import model.PlatformsVO;
import view.selectioninterfaces.ViewPlatformSelection;

// variables: platform id, platform name, user id, subscribers, views, likes, comments, engagement, income
public class PlatformsManager {
	
	private final int NUM_PLATFORMS = 5;
	private final int NUM_INPUT_DATA = 5;
	public static Scanner scan = new Scanner(System.in);
	private String accountUsername = SocialMediaAnalyticsMain.getUsersVO().getUsername();
	
	// show details for selected platform
	public void viewPlatform(String platformSelection) throws Exception {
		PlatformsDAO platformsDAO = new PlatformsDAO();
		boolean platformExists = false; 
		platformExists = platformsDAO.viewPlatform(platformSelection, SocialMediaAnalyticsMain.getUsersVO().getUsername());
		if(!platformExists) {
			System.out.println("You do not have any data for this platform");
		}
		System.out.println("-------------------------------------------------------------");
	}
	// returns status of platforms user has
	public void checkPlatforms(String username) throws Exception { 
		PlatformsDAO platformsDAO = new PlatformsDAO();
		ArrayList<String> userPlatforms = new ArrayList<String>();
		userPlatforms = platformsDAO.checkPlatforms(username);
		if(userPlatforms.size() == NUM_PLATFORMS) { // if all 5 have been entered, return
			System.out.printf("Platforms registered by %s%n", accountUsername);
			for(String platform : userPlatforms) System.out.printf("- %s%n", platform);
			System.out.println("You have all available platforms registered");
			System.out.println("-------------------------------------------------------------");
			return;
		} 			
		if(userPlatforms.size() == 0) { // if none have been entered yet
			System.out.println("You do not have any platforms registered in this application");
			System.out.println("-------------------------------------------------------------");
		}
		else { // if 1-4 of 5 platforms have been entered
			System.out.println("You have the following platforms registered in this application:");
			for(String platform : userPlatforms) System.out.printf("- %s%n", platform);
			System.out.println("-------------------------------------------------------------");
		}
	}
	// add a platform to user account
	public void addPlatform() throws Exception {
		checkPlatforms(accountUsername); // if platforms are full, return
		PlatformsDAO platformsDAO = new PlatformsDAO();
		PlatformsVO platformsVO = new PlatformsVO();
		// AccountAnalyticsDAO accountAnalyticsDAO = new AccountAnalyticsDAO();
		String platformSelection = null;
		boolean platformExists = false;
		// method to make user select one of the available platforms
		platformSelection = selectPlatform();
		// check if platform details to add already exists
		platformExists = platformsDAO.doesPlatformExist(platformSelection, accountUsername); 
		System.out.println("-------------------------------------------------------------");
		if(platformExists) { // return if platform already exists
			System.out.printf("You already have %s registered in the account%n", platformSelection);
			System.out.println("Returning to manage platform menu...");
			System.out.println("-------------------------------------------------------------");
			return;
		} 
		// input new platform details (subscribers [0], views [1], likes [2], comments [3], income [4])
		int[] addPlatformData = new int[NUM_INPUT_DATA];
		System.out.printf("[Enter information to add a new %s platform]%n", platformSelection);
		addPlatformData = inputPlatformData(); // method retrieves data 
		// insert values into platformsVO
		platformsVO.setPlatform_name(platformSelection);
		platformsVO.setUser_id(accountUsername);
		// take array and add input integers into platformsVO
		platformsVO.setSubscribers(addPlatformData[0]);
		platformsVO.setViews(addPlatformData[1]);
		platformsVO.setLikes(addPlatformData[2]);
		platformsVO.setComments(addPlatformData[3]);
		platformsVO.setIncome(addPlatformData[4]);
		// SQL method to add platform to database
		platformsDAO.addPlatform(platformsVO);
		// update engagement variable
//		platformsDAO.calculateEngagement();
		// update totals in account_analytics (CHANGED TO TRIGGER)
		// accountAnalyticsDAO.updateStatistics(accountUsername);
		System.out.println("-------------------------------------------------------------");
	}
	// edit platform in user account
	public void editPlatform() throws Exception {
		checkPlatforms(accountUsername); // if platforms are full, return
		PlatformsDAO platformsDAO = new PlatformsDAO();
		PlatformsVO platformsVO = new PlatformsVO();
		// AccountAnalyticsDAO accountAnalyticsDAO = new AccountAnalyticsDAO();
		String platformSelection = null;
		boolean platformExists = false;
		// method to make user select one of the available platforms
		platformSelection = selectPlatform();
		// method to check if platform details to edit exists
		platformExists = platformsDAO.doesPlatformExist(platformSelection, accountUsername); 
		System.out.println("-------------------------------------------------------------");
		if(!platformExists) { // return if platform data does not exist
			System.out.printf("You do not have any data for %s registered%n", platformSelection);
			System.out.println("Returning to manage platform menu...");
			return;
			}
		// input new platform details (subscribers [0], views [1], likes [2], comments [3], income [4])
		int[] editPlatformData = new int[NUM_INPUT_DATA];
		System.out.printf("[Enter information to edit %s details]%n", platformSelection);
		editPlatformData = inputPlatformData(); // method retrieves data 
		// insert values into platformsVO
		platformsVO.setPlatform_name(platformSelection);
		platformsVO.setUser_id(accountUsername);
		// take array and add input integers into platformsVO
		platformsVO.setSubscribers(editPlatformData[0]);
		platformsVO.setViews(editPlatformData[1]);
		platformsVO.setLikes(editPlatformData[2]);
		platformsVO.setComments(editPlatformData[3]);
		platformsVO.setIncome(editPlatformData[4]);
		// SQL method to add platform to database
		platformsDAO.editPlatform(platformsVO);
		// update engagement variable
//		platformsDAO.calculateEngagement();
		// update totals in account_analytics (CHANGED TO TRIGGER)
		// accountAnalyticsDAO.updateStatistics(accountUsername);
		System.out.println("-------------------------------------------------------------");
	}
	// delete platform in user account
	public void deletePlatform() throws Exception {
		System.out.println("[Delete platform data]");
		PlatformsDAO platformsDAO = new PlatformsDAO();
		// AccountAnalyticsDAO accountAnalyticsDAO = new AccountAnalyticsDAO();
		String platformSelection = null;
		checkPlatforms(accountUsername);
		// method to make user select one of the available platforms
		platformSelection = selectPlatform();
		platformsDAO.deletePlatform(accountUsername, platformSelection);
		// update totals in account_analytics (CHANGED TO TRIGGER)
		// accountAnalyticsDAO.updateStatistics(accountUsername);
		System.out.println("-------------------------------------------------------------");
	}
	// method to return platform data input by user
	public int[] inputPlatformData() throws Exception {
		// input new platform details
		int[] inputPlatformData = new int[NUM_INPUT_DATA];
		int subscribers = 0;
		int views = 0;
		int likes = 0;
		int comments = 0;
		int income = 0;
		for(;true;) {
			try {
				System.out.println("Enter number of subscribers:");
				subscribers = scan.nextInt();
				System.out.println("Enter number of views:");
				views = scan.nextInt();
				System.out.println("Enter number of likes:");
				likes = scan.nextInt();
				System.out.println("Enter number of comments:");
				comments = scan.nextInt();	
				System.out.println("Enter platform income:");
				income = scan.nextInt();
				scan.nextLine(); // clear buffer
			} catch(InputMismatchException e) {
				System.out.println("Please enter an integer. Try again");
				System.out.println("-------------------------------------------------------------");
				scan.nextLine(); // clear buffer
				continue;
			}
			if(subscribers > Integer.MAX_VALUE || views > Integer.MAX_VALUE || likes > Integer.MAX_VALUE || comments > Integer.MAX_VALUE
					|| (views + likes + comments) > Integer.MAX_VALUE || income > Integer.MAX_VALUE) {
				System.out.println("The number is too large. Try again");
				System.out.println("-------------------------------------------------------------");
				scan.nextLine(); // clear buffer
				continue;
			}
			// if code reaches here, the input data does not have any issues
			inputPlatformData[0] = subscribers;
			inputPlatformData[1] = views;
			inputPlatformData[2] = likes;
			inputPlatformData[3] = comments;
			inputPlatformData[4] = income;			
			break; 
		}
		return inputPlatformData;
	}
	//method to return a platform selected by the user
	public String selectPlatform() {
		String platformSelection = null;
		while(platformSelection == null) { // enrsure that a set platform is input into platformSelection
			int input = 0;
			
			System.out.println("[Select a platform]");
			System.out.println("[1] YouTube");
			System.out.println("[2] Instagram");
			System.out.println("[3] Facebook");
			System.out.println("[4] Twitter");
			System.out.println("[5] TikTok");
			try {
				input = scan.nextInt();						
				scan.nextLine(); // clear buffer
			} catch(InputMismatchException e) {
				System.out.println("Enter a number in the options");
			} if(input < ViewPlatformSelection.YouTube || input > ViewPlatformSelection.TikTok) {						
				System.out.println("Enter a number in the options");
			}
			// assign platformSelection according to user selection
			switch(input) {
			case ViewPlatformSelection.YouTube : platformSelection = "YouTube"; break;
			case ViewPlatformSelection.Instagram : platformSelection = "Instagram"; break;
			case ViewPlatformSelection.Facebook : platformSelection = "Facebook"; break;
			case ViewPlatformSelection.Twitter : platformSelection = "Twitter"; break;
			case ViewPlatformSelection.TikTok : platformSelection = "TikTok";
			}
		}
		return platformSelection;
	}
}
