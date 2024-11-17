package stutor;

import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class StudentHome extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	static String username = null;
	static String userID= null;
	static ServerConnector studentConn= null;
	static ServerConnector attendConn= null;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		studentConn= new ServerConnector("user_data");
		attendConn= new ServerConnector("attendance");
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StudentHome frame = new StudentHome();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public StudentHome() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 544, 317);
		contentPane = new JPanel();

		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(12,1,1,1));
		JLabel nameLabel=null, rollLabel, classLabel, cgpaLabel, rankLabel, ageLabel, addrLabel, emailLabel, genderLabel, phnoLabel;
		try {
			ResultSet rs= studentConn.executeQuery("SELECT * from student WHERE rollno="+userID);
			if(rs.next()) {
				nameLabel= new JLabel("Name: "+username);
				rollLabel= new JLabel("Roll no.: "+userID);
				classLabel= new JLabel("Class: "+rs.getString("dept")+" - "+rs.getString("class"));
				cgpaLabel= new JLabel("CGPA: "+rs.getString("cgpa"));
				rankLabel= new JLabel("Rank: "+rs.getString("rank"));
				panel.add(nameLabel);
				panel.add(rollLabel);
				panel.add(classLabel);
				panel.add(cgpaLabel);
				panel.add(rankLabel);
			}
			panel.add(new JLabel("Personal details:"));
			ResultSet rs1= studentConn.executeQuery("SELECT * from common WHERE uid="+userID);
			if(rs1.next()) {
				ageLabel= new JLabel("Age: "+rs1.getString("age"));
				addrLabel= new JLabel("Address: "+rs1.getString("addr"));
				phnoLabel= new JLabel("Ph. no.: +91 "+rs1.getString("phno"));
				emailLabel= new JLabel("Email: "+rs1.getString("email"));
				genderLabel= new JLabel("Gender: "+rs1.getString("gender"));
				panel.add(ageLabel);
				panel.add(addrLabel);
				panel.add(phnoLabel);
				panel.add(emailLabel);
				panel.add(genderLabel);
			}	
			
		}
		catch(SQLException e) {
			nameLabel.setText("User details not found/incomplete! Contact admin for info.");
			JOptionPane.showMessageDialog(null, "Error when fetching details.\nDetails: "+e.getMessage());
		}
		
		tabbedPane.addTab("Home", null, panel, null);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Scores", null, panel_1, null);
		
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("Home", null, panel_2, null);
		
		JPanel panel_3 = new JPanel();
		tabbedPane.addTab("Tests", null, panel_3, null);
		
		JPanel panel_4 = new JPanel();
		tabbedPane.addTab("Assignments", null, panel_4, null);
		
		JPanel panel_5 = new JPanel();
		tabbedPane.addTab("Attendance", null, panel_5, null);
		
		JLabel lblWelcomeBackUser = new JLabel(username+ " | Student");
		contentPane.add(lblWelcomeBackUser, BorderLayout.NORTH);
		
	}

}
