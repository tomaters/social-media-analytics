package controller.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import controller.DBUtil;
import model.AccountAnalyticsVO;

public class AccountAnalyticsDAO {
	// inserts a tuple with 0 values; to be called upon creation of an account; stores null into total subscribers/engagement/income
	public void insertDefaultRow(String username) throws Exception{ 
		String insertStatement = "INSERT INTO account_analytics (analytics_id, user_id) VALUES (account_analytics_seq.nextval, ?)";
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = DBUtil.makeConnection();
			preparedStatement = connection.prepareStatement(insertStatement);			
			preparedStatement.setString(1, username);
		
			int result = preparedStatement.executeUpdate();
			if(result == 1) {
//				System.out.println("INSERTED DEFAULT TUPLE INTO ANALYTICS TABLE"); // test if it worked
			} 
//			else System.out.println("FAILED TO INSERT DEFAULT TUPLE INTO ANALYTICS TABLE");			
		} catch(SQLException e) {
			System.out.println("SQL Error");
			e.printStackTrace();
		} catch(Exception e) {
			System.out.println("Java Error");
		} finally {
			try {
				if(preparedStatement != null) preparedStatement.close();
				if(connection != null) connection.close();
			} catch(SQLException e) {
				System.out.println("SQL Error");
			}
		}
	}
	// updates tuple as aggregate of all subscribers, engagement, and income, to be called when adding a new platform 
	public void updateStatistics(String username) throws Exception{ 
		String updateStatement = "UPDATE account_analytics "
				+ "SET total_subscribers = (SELECT SUM(subscribers) FROM platforms), "
				+ "total_engagement = (SELECT SUM(engagement) FROM platforms), "
				+ "total_income = (SELECT SUM(income) FROM platforms) "
				+ "WHERE user_id = ?";
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = DBUtil.makeConnection();
			preparedStatement = connection.prepareStatement(updateStatement);			
			preparedStatement.setString(1, username);
			int result = preparedStatement.executeUpdate();
			if(result == 1) {
//				System.out.println("ANALYTICS UPDATED"); // test
			} 
//			else System.out.println("FAILED TO UPDATE ANALYTICS");			
		} catch(SQLException e) {
			System.out.println("SQL Error");
			e.printStackTrace();
		} catch(Exception e) {
			System.out.println("Java Error");
		} finally {
			try {
				if(preparedStatement != null) preparedStatement.close();
				if(connection != null) connection.close();
			} catch(SQLException e) {
				System.out.println("SQL Error");
			}
		}
	}
	// view platform name, engagement, income subscribers
	public void viewEachAnalytics(String username) throws Exception {
		String selectStatement = "SELECT a.user_id, platform_name, engagement, income, subscribers "
				+ "FROM account_analytics a INNER JOIN platforms p "
				+ "ON a.user_id = p.user_id "
				+ "WHERE a.user_id = ?";
		String platform_name = null;
		int subscribers = 0;
		int income = 0;
		int engagement = 0;
		
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connection = DBUtil.makeConnection();
			preparedStatement = connection.prepareStatement(selectStatement);
			preparedStatement.setString(1, username);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()) {
				platform_name = resultSet.getString("platform_name");
				engagement = resultSet.getInt("engagement");
				subscribers = resultSet.getInt("subscribers");
				income = resultSet.getInt("income");
				System.out.printf("[%s] > Engagement: %d, Subscribers: %d, Income: %d%n",
						platform_name, engagement, subscribers, income);
			}
		} catch(SQLException e) {
			System.out.println("SQL Error");
			e.printStackTrace();
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
	// view aggregate subscribers, engagement, and income for a user
	public void viewTotalAnalytics(String username) throws Exception {
		String selectStatement = "SELECT total_engagement, total_subscribers, total_income FROM account_analytics "
				+ "WHERE user_id = ?";
		AccountAnalyticsVO accountAnalyticsVO = null;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connection = DBUtil.makeConnection();
			preparedStatement = connection.prepareStatement(selectStatement);
			preparedStatement.setString(1, username);
			resultSet = preparedStatement.executeQuery();
			
			while(resultSet.next()) {
				accountAnalyticsVO = new AccountAnalyticsVO();
				accountAnalyticsVO.setUser_id(username);
				accountAnalyticsVO.setTotal_engagement(resultSet.getInt("total_engagement"));
				accountAnalyticsVO.setTotal_subscribers(resultSet.getInt("total_subscribers"));
				accountAnalyticsVO.setTotal_income(resultSet.getInt("total_income"));
				System.out.printf("Account username: %s%nTotal Engagement: %d%nTotal subscribers: %d%nTotal Income: %d%n", 
						username, accountAnalyticsVO.getTotal_engagement(), accountAnalyticsVO.getTotal_subscribers(), accountAnalyticsVO.getTotal_income());
			}
		} catch(SQLException e) {
			System.out.println("SQL Error");
			e.printStackTrace();
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
	// delete all analytics (for deleting user)
	public void deleteAllAnalytics(String username) throws Exception {
		String deleteStatement = "DELETE FROM account_analytics WHERE user_id = ?";
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		try {
			connection = DBUtil.makeConnection();
			preparedStatement = connection.prepareStatement(deleteStatement);	
			preparedStatement.setString(1, username);
			int result = preparedStatement.executeUpdate();
			
			if(result >= 1) {
//				System.out.printf("%s data successfully deleted%n"); // test
			} 
//			else System.out.println("Platform data failed to delete. Try again");
		} catch(SQLException e) {
			System.out.println("SQL Error");
		} catch(Exception e) {
			System.out.println("Java Error");
		} finally {
			try {
				if(preparedStatement != null) preparedStatement.close();
				if(connection != null) connection.close();
			} catch(SQLException e) {
				System.out.println("SQL Error");
			}
		}
	}
}
