package stutor;

import java.awt.EventQueue;
import java.awt.GridLayout;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class TeacherHome extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	static String username = null;
	static String userID=null;
	static ServerConnector teacherConn= null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		teacherConn= new ServerConnector("user_data");
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TeacherHome frame = new TeacherHome();
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
	public TeacherHome() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 544, 317);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(12,1,1,1));
		JLabel nameLabel=null, rollLabel, classLabel, cgpaLabel, rankLabel, ageLabel, addrLabel, emailLabel, genderLabel, phnoLabel;
		try {
			ResultSet rs= teacherConn.executeQuery("SELECT * from teacher WHERE teacherid="+userID);
			if(rs.next()) {
				nameLabel= new JLabel("Name: "+username);
				rollLabel= new JLabel("Roll no.: "+userID);
				classLabel= new JLabel("Classes handled: "+rs.getString("dept")+" - "+rs.getString("classes"));
				cgpaLabel= new JLabel("Salary: "+rs.getString("sal"));
				rankLabel= new JLabel("Experience: "+rs.getString("exp")+ " years");
				panel.add(nameLabel);
				panel.add(rollLabel);
				panel.add(classLabel);
				panel.add(cgpaLabel);
				panel.add(rankLabel);
			}
			panel.add(new JLabel("Personal details:"));
			ResultSet rs1= teacherConn.executeQuery("SELECT * from common WHERE uid="+userID);
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
		tabbedPane.addTab("Classroom", null, panel_1, null);
		
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("Notes", null, panel_2, null);
		
		JPanel panel_3 = new JPanel();
		tabbedPane.addTab("Tests", null, panel_3, null);
		
		JPanel panel_4 = new JPanel();
		tabbedPane.addTab("Assignments", null, panel_4, null);
		
		JPanel panel_5 = new JPanel();
		tabbedPane.addTab("Attendance", null, panel_5, null);
		
		JLabel lblWelcomeBackUser = new JLabel("Dr. "+username + " | Teacher");
		contentPane.add(lblWelcomeBackUser, BorderLayout.NORTH);
		
	}

}
