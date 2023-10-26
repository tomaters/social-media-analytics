package controller.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import controller.DBUtil;
import model.PlatformsVO;

public class PlatformsDAO {
	// update engagement statistic
	public void calculateEngagement() { // updates platforms (sum of views, likes, comments) into database; applies to all tuples
		String updateStatement = "UPDATE platforms SET engagement = COALESCE(views, 0) + COALESCE(likes, 0) + COALESCE(comments,0)";
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		try {
			connection = DBUtil.makeConnection();
			preparedStatement = connection.prepareStatement(updateStatement);
			

			int result = preparedStatement.executeUpdate();
			if(result > 0){
//				System.out.println("Engagement updated"); // test if it works
			} else {
//				System.out.println("Engagement failed to update");
			}

			
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
	// view platform statistics
	public boolean viewPlatform(String platformSelection, String username) throws Exception {
		String selectStatement = "SELECT * FROM platforms WHERE platform_name = ? AND user_id = ?";
		PlatformsVO platformsVO = null;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		boolean platformExists = false; // if there is no entry, return false
		
		try {
			connection = DBUtil.makeConnection();
			preparedStatement = connection.prepareStatement(selectStatement);
			preparedStatement.setString(1, platformSelection);
			preparedStatement.setString(2, username);
			resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				platformExists = true; // if there is an entry, set boolean to true
				// retrieve and print platform details
				platformsVO = new PlatformsVO();
				platformsVO.setSubscribers(resultSet.getInt("subscribers"));
				platformsVO.setViews(resultSet.getInt("views"));
				platformsVO.setLikes(resultSet.getInt("likes"));
				platformsVO.setComments(resultSet.getInt("comments"));
				platformsVO.setEngagement(resultSet.getInt("engagement"));
				platformsVO.setIncome(resultSet.getInt("income"));
				System.out.printf("Subscribers:\t%d%nViews: \t\t%d%nLikes: \t\t%d%nComments: \t%d%nEngagement: \t%d%nIncome ($): \t%d%n",
					platformsVO.getSubscribers(), platformsVO.getViews(), platformsVO.getLikes(), platformsVO.getComments(), 
					platformsVO.getEngagement(), platformsVO.getIncome());
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
		return platformExists;
	}
	// add platform
	public void addPlatform(PlatformsVO platformsVO) throws Exception {
		String insertStatement = "INSERT INTO platforms(platform_id, platform_name, user_id, subscribers, views, likes, comments, income) "
				+ "VALUES(platform_seq.nextval, ?, ?, ?, ?, ?, ?, ?)";
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		try {
			connection = DBUtil.makeConnection();
			preparedStatement = connection.prepareStatement(insertStatement);
			preparedStatement.setString(1, platformsVO.getPlatform_name());
			preparedStatement.setString(2, platformsVO.getUser_id());			
			preparedStatement.setInt(3, platformsVO.getSubscribers());
			preparedStatement.setInt(4, platformsVO.getViews());
			preparedStatement.setInt(5, platformsVO.getLikes());
			preparedStatement.setInt(6, platformsVO.getComments());
			preparedStatement.setInt(7, platformsVO.getIncome());
			
			int result = preparedStatement.executeUpdate();
			if(result == 1) {
				System.out.printf("Data for %s has successfully been registered%n", platformsVO.getPlatform_name());
			}
			else {
				System.out.println("Platform registration failed. Try again");
			}
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
	// edit platform
	public void editPlatform(PlatformsVO platformsVO, String platformSelection) throws Exception {
		String updateStatement = "UPDATE platforms SET subscribers = ?, views = ?, likes = ?, comments = ?, income = ? WHERE user_id = ? AND platform_name = ?";
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		try {
			connection = DBUtil.makeConnection();
			preparedStatement = connection.prepareStatement(updateStatement);
			preparedStatement.setInt(1, platformsVO.getSubscribers());
			preparedStatement.setInt(2, platformsVO.getViews());
			preparedStatement.setInt(3, platformsVO.getLikes());
			preparedStatement.setInt(4, platformsVO.getComments());
			preparedStatement.setInt(5, platformsVO.getIncome());
			preparedStatement.setString(6, platformsVO.getUser_id());
			preparedStatement.setString(7, platformsVO.getPlatform_name());
			
			int result = preparedStatement.executeUpdate();
			if(result == 1) {
				System.out.println("Platform updated successfully");
			} else System.out.println("Platform failed to update. Try again");
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
	// delete platform
	public void deletePlatform(String username, String platformSelection) throws Exception {
		String deleteStatement = "DELETE FROM platforms WHERE user_id = ? AND platform_name = ?";
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		try {
			connection = DBUtil.makeConnection();
			preparedStatement = connection.prepareStatement(deleteStatement);	
			preparedStatement.setString(1, username);
			preparedStatement.setString(2, platformSelection);
			int result = preparedStatement.executeUpdate();
			
			if(result == 1) {
				System.out.printf("%s data successfully deleted%n", platformSelection);
			} else System.out.println("Platform data failed to delete. Try again");
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
	// delete all platforms (for deleting user)
	public void deleteAllPlatforms(String username) throws Exception {
		String deleteStatement = "DELETE FROM platforms WHERE user_id = ?";
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
	// return ArrayList<String> of the platforms that the user has 
	public ArrayList<String> checkPlatforms(String username) throws Exception {
		String selectStatement = "SELECT platform_name FROM platforms WHERE user_id = ?"; 
		ArrayList<String> userPlatforms = new ArrayList<String>();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connection = DBUtil.makeConnection();
			preparedStatement = connection.prepareStatement(selectStatement);
			preparedStatement.setString(1, username);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()) {
				userPlatforms.add(resultSet.getString("platform_name")); 
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
		return userPlatforms;
	}
	// if user already has a platform, return true
	public boolean doesPlatformExist(String platformSelection, String username) throws Exception{
		boolean platformExists = false;
		String selectStatement = "SELECT * FROM platforms WHERE platform_name = ? AND user_id = ?";
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {	
			connection = DBUtil.makeConnection();
			preparedStatement = connection.prepareStatement(selectStatement);
			preparedStatement.setString(1, platformSelection);
			preparedStatement.setString(2, username);			
			resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) { 
				platformExists = true; 
			}
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
		return platformExists;
	}
}
