
package stutor;

import java.awt.EventQueue;
import java.awt.GridLayout;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import java.io.File;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.mysql.cj.protocol.Resultset;
import com.mysql.cj.xdevapi.Statement;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JFileChooser;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
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
		
		JPanel panel_2 = new JPanel(new BorderLayout());
		tabbedPane.addTab("Notes", null, panel_2, null);
		
		JPanel panel_3 = new JPanel();
		tabbedPane.addTab("Tests", null, panel_3, null);
		
		JPanel panel_4 = new JPanel();
		tabbedPane.addTab("Assignments", null, panel_4, null);
		
		JPanel panel_5 = new JPanel();
		tabbedPane.addTab("Attendance", null, panel_5, null);
		
		JLabel lblWelcomeBackUser = new JLabel("Dr. "+username + " | Teacher");
		contentPane.add(lblWelcomeBackUser, BorderLayout.NORTH);
		


		//Notes
		
		try{
			ServerConnector ncon = new ServerConnector();
			ResultSet nres=ncon.executeQuery("select * from notes");

			ArrayList<String> nname = new ArrayList<String>();
			ArrayList<String> ndou = new ArrayList<String>();
			while (nres.next()) { 
				nname.add(nres.getString("name"));
				ndou.add(nres.getString("date"));
			}
			int rows = nname.size();
			String[][] notesData = new String[rows][2];
			for (int i = 0; i < rows; i++) {
				notesData[i][0] = nname.get(i);  // First column - Name
				notesData[i][1] = ndou.get(i);  // Second column - Date of upload
			}
			String tableTitle[]={"Name", "Date of upload"};
			JTable notesTable = new JTable(notesData, tableTitle);
			panel_2.add(notesTable, BorderLayout.CENTER);

			
		}catch(SQLException e){
			System.err.println("SQLEXception");
		}		

		JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		JButton fileButton = new JButton("Select File");
		// JFileChooser chooseFile = new JFileChooser();
		// chooseFile.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Text Files", "txt"));
		// JLabel fLabel=new JLabel("Enter title : ");
		// JTextField finp=new JTextField();
		// JButton fileSel = new JButton("Submit");
		JLabel dummy=new JLabel();
		JPanel panel_2_2 = new JPanel(new GridLayout(1, 1, 50, 50 ));
		panel_2_2.add(fileButton);
		JLabel titleLabel = new JLabel("Enter a title:");
		titleLabel.setFont(new Font("Ubuntu", Font.PLAIN, 20));
        panel_2_2.add(titleLabel);
		JPanel panel2_2_e =  new JPanel(new GridLayout());

        JTextField titleField = new JTextField();
		JButton nupload=new JButton("Upload");
		
        panel_2_2.add(titleField);
		panel_2_2.add(nupload);
		
		panel_2.add(panel_2_2, BorderLayout.SOUTH);
		fileButton.addActionListener(e -> {
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                String fileName = selectedFile.getName();  // Get the file name
                JOptionPane.showMessageDialog(this, "Selected File: " + fileName);
            } else {
                JOptionPane.showMessageDialog(this, "No file selected.");
            }
        });
		

		try {
			nupload.addActionListener(e ->{
				ServerConnector stt=new ServerConnector();
				System.out.println(username);
				String tf=titleField.getText();
				userID="23bcs108";
				String qrr="insert into notes(name, id, date) values('"+tf+"'','"+userID+"'','2024-01-13')";
				System.out.println(qrr);
				ResultSet ddt=stt.executeQuery(qrr);
			});
		
		} catch (Exception e) {
			System.out.print("noooooo");
		}
		
		// JPanel panel_2_3 = new JPanel(new GridLayout(3,2));
		// panel_2_3.add(fLabel);
		// panel_2_3.add(finp);
		// panel_2_2.add(fileSel);
		// panel_2.add(panel_2_3);

	}

}
