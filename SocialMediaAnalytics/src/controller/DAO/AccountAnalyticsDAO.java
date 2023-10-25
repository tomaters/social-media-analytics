package controller.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import controller.DBUtil;

public class AccountAnalyticsDAO {
	// inserts tuple as aggregate of all subscribers, engagement, and income, to be called when adding a new platform
	public void insertStatistics(String username) throws Exception{ 
		String insertStatement = "INSERT INTO account_analytics VALUES(account_analysis_seq.nextval, ?,"
				+ "(SELECT SUM(subscribers) FROM platforms)"
				+ "(SELECT SUM(engagement) FROM platforms)"
				+ "(SELECT SUM(income) FROM platforms)";
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = DBUtil.makeConnection();
			preparedStatement = connection.prepareStatement(insertStatement);			
			preparedStatement.setString(1, username);
		
			int result = preparedStatement.executeUpdate();
			if(result == 1) {
				System.out.println("INSERTED TUPLE INTO ANALYTICS TABLE");
			} else System.out.println("FAILED TO INSERT TUPLE INTO ANALYTICS TABLE");			
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
