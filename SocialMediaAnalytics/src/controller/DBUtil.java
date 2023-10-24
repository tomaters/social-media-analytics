package controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBUtil {

	public static Connection makeConnection() throws Exception {
		Properties properties = new Properties();
		Connection connection = null;
		
		try {
			FileInputStream fileInputStream = new FileInputStream("src/config/db.properties");
			properties.load(fileInputStream);
			String driver = properties.getProperty("driver");
			String url = properties.getProperty("url");
			String username = properties.getProperty("username");
			String password = properties.getProperty("password");
			
			Class.forName(driver);
			connection = DriverManager.getConnection(url, username, password);
		} catch(IOException | ClassNotFoundException | SQLException e) {
			System.out.println("Error");
		}
		return connection;
	}
}
