package controller.datamanagers;

import java.util.Scanner;

import controller.DAO.AccountAnalyticsDAO;
import main.SocialMediaAnalyticsMain;

// variables: analytics_id, id, total subscribers/engagement/income
public class AccountAnalyticsManager {
	
	public static Scanner scan = new Scanner(System.in);
	private String accountUsername = SocialMediaAnalyticsMain.getUsersVO().getUsername();
	
	public void viewTotalAnalytics() throws Exception {
		AccountAnalyticsDAO accountAnalyticsDAO = new AccountAnalyticsDAO();
		System.out.println("[View account analytics]");
		System.out.println("-------------------------------------------------------------");
		System.out.println("[Analytics by platform]");
		accountAnalyticsDAO.viewEachAnalytics(accountUsername);
		System.out.println("-------------------------------------------------------------");
		System.out.println("[Total analytics]");
		accountAnalyticsDAO.viewTotalAnalytics(accountUsername);
		System.out.println("-------------------------------------------------------------");
	}
	
	public void viewTotalAnalyticsFromAdmin(String username) throws Exception {
		AccountAnalyticsDAO accountAnalyticsDAO = new AccountAnalyticsDAO();
		System.out.println("[View account analytics]");
		System.out.println("-------------------------------------------------------------");
		System.out.println("[Analytics by platform]");
		accountAnalyticsDAO.viewEachAnalytics(username);
		System.out.println("-------------------------------------------------------------");
		System.out.println("[Total analytics]");
		accountAnalyticsDAO.viewTotalAnalytics(username);
		System.out.println("-------------------------------------------------------------");
	}
}
