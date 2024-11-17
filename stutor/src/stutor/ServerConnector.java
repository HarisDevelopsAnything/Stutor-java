package stutor;

import java.sql.*;
import javax.swing.*;

public class ServerConnector {
	static String url, user, pwd;
	static Connection connection;
	ServerConnector(){
		url= "jdbc:mysql://localhost:3306/test";
		user= "root";
		pwd= "harisgsk";
		connection= null;
	}
	public static void connect(){
		url= "jdbc:mysql://localhost:3306/users";
		user= "root";
		pwd= "harisgsk";
		
		try {
			connection = DriverManager.getConnection(url, user, pwd);
			JOptionPane.showMessageDialog(null, "Connection to server established!", "Connection succeeded", JOptionPane.INFORMATION_MESSAGE);
		}
		catch(SQLException e) {
			JOptionPane.showMessageDialog(null, "Error connecting to server.\nReason: "+e.getMessage(), "Connection to server failed!", JOptionPane.WARNING_MESSAGE);
		}
	}
	public static String loginManager(String uname, String pwd) {
		try {
			String query= "SELECT * FROM students WHERE id = ?";
			PreparedStatement st= connection.prepareStatement(query);
			st.setString(1, uname);
			ResultSet rs= st.executeQuery();
			while(rs.next()) {
				if(rs.getString("password").equals(pwd)) {
					return "student"+rs.getString("username");
				}
			}
			query= query.replace("students", "teachers");
			st= connection.prepareStatement(query);
			st.setString(1, uname);
			rs= st.executeQuery();
			while(rs.next()) {
				if(rs.getString("password").equals(pwd)) {
					return "teacher"+rs.getString("username");
				}
			}
			return "noone";
		}
		catch(SQLException e) {
			JOptionPane.showMessageDialog(null, "Error getting user details.\nInfo: "+e.getMessage());
			return "noone";
		}
	}
}
