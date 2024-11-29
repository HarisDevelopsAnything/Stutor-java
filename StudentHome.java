package stutor;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class StudentHome extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	static String username = null;
	static String userID= null;
	static String dept= "CSE";
	static JComboBox<Integer> semChoose;
	static JComboBox<Integer> semChoose1;
	static ServerConnector studentConn= null;
	static ServerConnector attendConn= null;
	static ServerConnector scoreConn = null;
	static ServerConnector fileConn= null;
	static DefaultTableModel notesTable;
	static DefaultTableModel cat1Table;
	static DefaultTableModel cat2Table;
	static DefaultTableModel cat3Table;
	static DefaultTableModel semTable;
	static JScrollPane messageScroll;
	static JPanel fileList;
	static JPanel messages;
	static ServerConnector subjectConn= null;
	static DefaultTableModel attendanceTable;
	static File selectedFile= null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		studentConn= new ServerConnector("user_data");
		scoreConn= new ServerConnector("student_scores");
		attendConn= new ServerConnector("attendance");
		fileConn= new ServerConnector("files");
		subjectConn= new ServerConnector("subjects");
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
		fileList = new JPanel();
		fileList.setLayout(new VerticalFlowLayout(5));
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
				rankLabel= new JLabel("Rank: "+rs.getString("crank"));
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
		
		JPanel panel_12 = new JPanel();
		panel_12.setLayout(new BorderLayout());  // Using BorderLayout for easy component placement
		messages= new JPanel(new VerticalFlowLayout(10));
		messageScroll= new JScrollPane(messages);
		panel_12.add(messageScroll, BorderLayout.CENTER);
		// ComboBox for recipient selection (A, B, C, or DM)
		JComboBox<String> recipientComboBox = new JComboBox<>();
		JTextField studentIDField= new JTextField(8);
		recipientComboBox.addItem("DM (Direct Message)");
		

		// JTextArea for message input (long message bar)
		JTextField messageTextArea = new JTextField(20);  // 5 rows, 40 columns for message area

		// Send Message Button
		JButton sendMessageButton = new JButton("Send Message");
		sendMessageButton.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        String recipient = (String) recipientComboBox.getSelectedItem();
		        String message = messageTextArea.getText();
		        ResultSet rs= studentConn.executeQuery("SELECT dept from student where rollno='"+userID+"';");
		        dept="CSE";
		        try {
					if(rs.next()) {
						dept= rs.getString("dept");
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		        if (message.isEmpty()) {
		            JOptionPane.showMessageDialog(panel_12, "Please enter a message before sending.");
		        } else {
		            String recipientID = "";
		            if ("DM (Direct Message)".equals(recipient)) {
		                recipientID = studentIDField.getText();  // Get the student ID entered
		                if (recipientID.isEmpty()) {
		                    JOptionPane.showMessageDialog(panel_12, "Please enter the teacher ID for DM.");
		                    return;  // Stop further processing if DM ID is missing
		                }
		                sendMessage(recipientID, message);
		                refreshMessages();
		            }
		            
		            JOptionPane.showMessageDialog(panel_12, "Message sent to " + recipient + " (Teacher ID: " + recipientID + ")");
		            messageTextArea.setText("");  // Clear the message area after sending
		        }
		    }
		});

		// Panel for south section (holding the recipient ComboBox, message area, and Send button)
		JPanel southPanel = new JPanel();
		southPanel.setLayout(new FlowLayout());
		southPanel.add(new JLabel("Select Recipient:"));
		southPanel.add(recipientComboBox);
		southPanel.add(studentIDField);
		studentIDField.setVisible(true);
		southPanel.add(messageTextArea);
		southPanel.add(sendMessageButton);  // Place the send button at the bottom

		// JTextField for entering the student ID (only visible when DM is selected)
		 // Text field for entering the student ID
		studentIDField.setVisible(true);  // Initially hide the student ID field

		// Add a listener to the ComboBox to handle DM selection
		recipientComboBox.addItemListener(new ItemListener() {
		    @Override
		    public void itemStateChanged(ItemEvent e) {
		        if ("DM (Direct Message)".equals(recipientComboBox.getSelectedItem())) {
		            studentIDField.setVisible(true);  // Show the student ID field for DM
		        } else {
		            studentIDField.setVisible(false);  // Hide the student ID field for other recipients
		        }
		    }
		});
		panel_12.add(southPanel, BorderLayout.SOUTH);
		refreshMessages();
		// Add Classroom tab to the Tabbed Pane
		tabbedPane.addTab("Classroom", null, panel_12, null);
		
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(new BorderLayout());
		panel_1.add(new JLabel("Subject wise scores"), BorderLayout.NORTH);
		JPanel center = new JPanel(new BorderLayout());
		JPanel chooser = new JPanel();
		semChoose = new JComboBox<>();
		for(int i=1;i<=8;i++)
		semChoose.addItem(i);
		semChoose.setSelectedItem(1); //default 1st sem mark
		String ecols[]= {"Course code","Course name","Score","Result"};
		JTabbedPane scorePanel = new JTabbedPane();
		JPanel cat1 = new JPanel();
		cat1.setLayout(new BorderLayout());
		cat1Table= new DefaultTableModel(null, ecols);
		JTable jt1= new JTable(cat1Table);
		cat1Table.addRow(ecols);
		cat1.add(jt1);
		JPanel cat2= new JPanel();
		cat2.setLayout(new BorderLayout());
		cat2Table= new DefaultTableModel(null,ecols);
		JTable jt2= new JTable(cat2Table);
		cat2Table.addRow(ecols);
		cat2.add(jt2, BorderLayout.CENTER);
		JPanel cat3= new JPanel();
		cat3.setLayout(new BorderLayout());
		cat3Table= new DefaultTableModel(null,ecols);
		JTable jt3= new JTable(cat3Table);
		cat3Table.addRow(ecols);
		cat3.add(jt3);
		JPanel sem= new JPanel();
		sem.setLayout(new BorderLayout());	
		String scols[]= {"Course code","Course name","Score","Credits","Result"};
		semTable= new DefaultTableModel(null,ecols);
		JTable jt4= new JTable(semTable);
		semTable.addRow(scols);
		sem.add(jt4);
		scorePanel.addTab("CAT 1", null, cat1, null);
		scorePanel.addTab("CAT 2", null, cat2, null);
		scorePanel.addTab("CAT 3", null, cat3, null);
		scorePanel.addTab("End sem", null, sem, null);
		
		chooser.add(new JLabel("Select semester: "));
		chooser.add(semChoose);
		center.add(chooser, BorderLayout.NORTH);
		center.add(scorePanel, BorderLayout.CENTER);
		panel_1.add(center, BorderLayout.CENTER);
		tabbedPane.addTab("Scores", null, panel_1, null);
		refreshScores(1);
		semChoose.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				refreshScores((int)semChoose.getSelectedItem());
			}
		});
		// Panel for Notes Tab
        JPanel panel_2 = new JPanel();
        panel_2.setLayout(new BorderLayout());
        JScrollPane jsp1= new JScrollPane(fileList);
        panel_2.add(jsp1,BorderLayout.CENTER);
        // Notes Section
        try {
        	ArrayList<String> teacherID = new ArrayList<>();
        	dept="CSE";
        	ResultSet rs= studentConn.executeQuery("SELECT * from student where rollno='"+userID+"';");
        	if(rs.next())
        	dept= rs.getString("dept");
        	System.out.println(dept);
        	ResultSet rs1= studentConn.executeQuery("SELECT * from teacher where dept='"+dept+"';");
        	while(rs1.next()) {
        		teacherID.add(rs1.getString("teacherid"));
        	}
        	for(String tid: teacherID) {
	            ResultSet nres = fileConn.executeQuery("SELECT * FROM notes WHERE teacherid='"+tid+"';");
	            ArrayList<String> nname = new ArrayList<>();
	            ArrayList<String> ndou = new ArrayList<>();
	            ArrayList<String> nfilePaths = new ArrayList<>();
	
	            while (nres.next()) {
	                nname.add(nres.getString("name"));
	                ndou.add(nres.getString("added"));
	                nfilePaths.add(nres.getString("filepath"));
	            }
	
	            int rows = nname.size();
	            for(int i=0;i<rows;i++) {
	            	JPanel row= new JPanel();
	            	row.add(new JLabel(nname.get(i)));
	            	row.add(new JLabel("<html><p>"+ndou.get(i)+"</p></html>"));
	            	JButton filebtn= new JButton("Open file");
	            	final int j=i;
	            	filebtn.addActionListener(new ActionListener() {
	            		public void actionPerformed(ActionEvent e) {
	            			
	            			try {
								Desktop.getDesktop().open(new File(nfilePaths.get(j)));
							} catch (IOException e1) {
								//TODO Auto-generated catch block
								e1.printStackTrace();
							}
	            		}
	            	});
	            	row.add(filebtn);
	            	fileList.add(row);
	            }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("SQLException: " + e.getMessage());
        }

        // Bottom panel for uploading files
        JPanel bottom = new JPanel();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));

        JButton fileButton = new JButton("Select File");
        bottom.add(fileButton);

        JLabel titleLabel = new JLabel("Enter a title:");
        titleLabel.setFont(new Font("Ubuntu", Font.PLAIN, 20));
        bottom.add(titleLabel);

        JTextField titleField = new JTextField(8);
        bottom.add(titleField);

        JButton nupload = new JButton("Upload");
        bottom.add(nupload);

        //panel_2.add(bottom, BorderLayout.SOUTH);

        // File selection button action listener
        fileButton.addActionListener(e -> {
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                selectedFile = fileChooser.getSelectedFile();
                String fileName = selectedFile.getName();
                String filePath = selectedFile.getAbsolutePath();
                
                // Validate file extension (only PDF or DOCX for example)
                if (!(fileName.endsWith(".pdf") || fileName.endsWith(".docx") || fileName.endsWith(".txt"))) {
                    JOptionPane.showMessageDialog(this, "Only PDF, TXT and DOCX files are allowed.");
                    return; // Stop further processing if invalid file type
                }

                JOptionPane.showMessageDialog(this, "Selected File: " + fileName);
                int resp= JOptionPane.showOptionDialog(this, "Do you want a preview?", "Added 1 file", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,null,new Object[] {"Yes","No"},"No");
                if(resp== JOptionPane.YES_OPTION) {
                try {
                    Desktop.getDesktop().open(selectedFile);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                }
            } else {
                JOptionPane.showMessageDialog(this, "No file selected.");
            }
        });

        // Upload file action listener
        nupload.addActionListener(e -> {
            String title = titleField.getText();
            if (selectedFile != null && !title.isEmpty()) {
                String query = "INSERT INTO notes (name, filepath, teacherid, added, comment) VALUES ('" 
                                + selectedFile.getName() + "', '" 
                                + selectedFile.getAbsolutePath().replace("\\", "/") + "', '" 
                                + userID + "', '" 
                                + LocalDate.now() + "', '" 
                                + title + "');";
                fileConn.executeUpdates(query);
                refreshFileTable(); // Refresh the table after uploading the file
                JOptionPane.showMessageDialog(this, "File uploaded successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Please select a file and provide a title.");
            }
        });

        // Add the panel to the tabbed pane
        tabbedPane.addTab("Documents", null, panel_2, null);

		
		JPanel panel_5 = new JPanel();
		panel_5.setLayout(new BorderLayout());
		panel_5.add(new JLabel("Student attendance"), BorderLayout.NORTH);
		JPanel centre5= new JPanel(new GridLayout());
		JPanel semChooser= new JPanel();
		semChooser.add(new JLabel("Select semester"));
		semChoose1= new JComboBox<>();
		for(int i=1;i<=8;i++)
		semChoose1.addItem(i);
		semChooser.add(semChoose1);
		semChoose1.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				refreshAttendance((int)semChoose.getSelectedItem());
			}
		});
		centre5.add(semChooser);
		panel_5.add(centre5);
		String cols[]= {"Date","H1","H2","H3","H4","H5","H6","H7","H8"};
		attendanceTable = new DefaultTableModel(null, cols);
		//actually updating attendance
		try {
			ResultSet rs= attendConn.executeQuery("SELECT * from student_attendance WHERE rollno="+userID);
			while(rs.next()) {
				System.out.println(rs);
				String row[]= {rs.getString("class_date"), rs.getString("p1"), rs.getString("p2"), rs.getString("p3"), rs.getString("p4"), rs.getString("p5"), rs.getString("p6"), rs.getString("p7"), rs.getString("p8")};
				attendanceTable.addRow(row);
			}
		}
		catch(SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		JTable jt = new JTable(attendanceTable);
		jt.setDefaultEditor(Object.class, null);
		JScrollPane jsp2= new JScrollPane(jt);
		centre5.add(jsp2);
		tabbedPane.addTab("Attendance", null, panel_5, null);
		
		JLabel lblWelcomeBackUser = new JLabel(username+ " | Student");
		contentPane.add(lblWelcomeBackUser, BorderLayout.NORTH);
	}
	public void refreshMessages() {
		messages.removeAll();
    	ResultSet rs = fileConn.executeQuery("SELECT * from messages where rec='"+userID+"' OR sender='"+userID+"';");
    	try {
	    	while(rs.next()) {
	    		messages.add(new MessageBubble(rs.getString("sender"),rs.getString("rec"),rs.getString("message"),rs.getString("dom"),userID));
	    	}
    	}
    	catch(Exception e) {
    		System.out.println(e.getMessage());
    	}
    	messages.revalidate();
    	GUIMisc.scrollToBottom(messageScroll);
    }
    public void sendMessage(String userID, String message) {
    	System.out.println(userID+" "+message);
    	if(userID.length()==5 && message.length()!=0) {
    		String rec="";
    		try {
    			ResultSet test= studentConn.executeQuery("SELECT * from common WHERE uid="+userID);
    			if(test.next()) {
    				rec= test.getString("name");
    				if(!test.getString("type").equals("T")) {
    					JOptionPane.showMessageDialog(null, "You can send messages only to teachers!");
    					return;
    				}
    			}
    			else {
    				JOptionPane.showMessageDialog(null, "No such user!");
    				return;
    			}
    			Date currDate= new Date();
    			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    			String date= sdf.format(currDate);
    			fileConn.executeUpdates("INSERT into messages () values('"+StudentHome.userID+"','"+userID+"','"+message+"','"+date+"');");
    			JOptionPane.showMessageDialog(null, "Sent message to "+rec);
    		}
    		catch(Exception e) {
    			
    		}
    		
    	}
    }

	void refreshAttendance(int sem) {
		try {
			attendanceTable.setRowCount(0);
			ResultSet rs= attendConn.executeQuery("SELECT * from student_attendance WHERE rollno='"+userID+"' AND sem="+sem);
			while(rs.next()) {
				System.out.println(rs);
				String row[]= {rs.getString("class_date"), rs.getString("p1"), rs.getString("p2"), rs.getString("p3"), rs.getString("p4"), rs.getString("p5"), rs.getString("p6"), rs.getString("p7"), rs.getString("p8")};
				attendanceTable.addRow(row);
			}
		}
		catch(SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}
	static void refreshFileTable() {
		try {
			ResultSet nres=fileConn.executeQuery("select * from notes where teacherid="+userID);
			ArrayList<String> nname = new ArrayList<String>();
			ArrayList<String> ndou = new ArrayList<String>();
			while (nres.next()) { 
				nname.add(nres.getString("name"));
				ndou.add(nres.getString("added"));
			}
			int rows = nname.size();
			notesTable.setRowCount(0); //refresh table
			for (int i = 0; i < rows; i++) {
				String [] t= {nname.get(i), ndou.get(i)};
				notesTable.addRow(t);  // First column - Name
				  // Second column - Date of upload
			}
			
			
		}catch(SQLException e){
			System.err.println("SQLEXception");
		}
		}
	void refreshScores(int sem) {
		cat1Table.setRowCount(0);
		cat2Table.setRowCount(0);
		cat3Table.setRowCount(0);
		semTable.setRowCount(0);
		String q1= "SELECT * from cat where cat=1 AND sem="+semChoose.getSelectedItem()+" AND rollno='"+userID+"';";
		String q2= "SELECT * from cat where cat=2 AND sem="+semChoose.getSelectedItem()+" AND rollno='"+userID+"';";
		String q3= "SELECT * from cat where cat=3 AND sem="+semChoose.getSelectedItem()+" AND rollno='"+userID+"';";
		String s= "SELECT * from sem where sem="+semChoose.getSelectedItem()+" AND rollno='"+userID+"';";
		String subsql= "SELECT * from "+dept.toLowerCase()+";";
		String subj[]= new String[6];
		String cc[]= new String[6];
		int cred[]= new int[6];
		int i=0;
		ResultSet subs= subjectConn.executeQuery(subsql);
		try {
			while(subs.next()) {
				subj[i]= subs.getString("subname");
				cc[i]= subs.getString("subcode");
				cred[i]= Integer.parseInt(subs.getString("credits"));
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResultSet rs1= scoreConn.executeQuery(q1);
		ResultSet rs2= scoreConn.executeQuery(q2);
		ResultSet rs3= scoreConn.executeQuery(q3);
		ResultSet rs4= scoreConn.executeQuery(s);
		try {
			i=0;
			if(rs1.next()) {
				while(i<6) {
				String[] rowdata= {cc[i],subj[i],rs1.getString("sub"+(i+1)),Double.parseDouble(rs1.getString("sub"+(i+1)))>50?"Pass":"Fail"};
				cat1Table.addRow(rowdata);
				i++;
				}
			}
			i=0;
			while(rs2.next()) {
				String[] rowdata= {cc[i],subj[i],rs1.getString("sub"+(i+1)),Double.parseDouble(rs1.getString("sub"+(i+1)))>50?"Pass":"Fail"};
				cat2Table.addRow(rowdata);
				i++;
			}
			i=0;
			while(rs3.next()) {
				String[] rowdata= {cc[i],subj[i],rs1.getString("sub"+(i+1)),Double.parseDouble(rs1.getString("sub"+(i+1)))>50?"Pass":"Fail"};
				cat3Table.addRow(rowdata);
				i++;
			}
			while(rs4.next()) {
				String[] rowdata= {cc[i],subj[i],rs1.getString("sub"+(i+1)),Integer.toString(cred[i]),Double.parseDouble(rs1.getString("sub"+(i+1)))>50?"Pass":"Fail"};
				semTable.addRow(rowdata);
				i++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}

}
