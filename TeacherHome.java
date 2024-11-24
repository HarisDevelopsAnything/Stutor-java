package stutor;

import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;

import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class TeacherHome extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	static String username = null;
	static String userID=null;
	static ServerConnector teacherConn= null;
	static ServerConnector studentConn= null;
	static ServerConnector attendConn= null;
	static ServerConnector fileConn= null;
	static DefaultTableModel attendanceTable;
	static DefaultTableModel notesTable;
	static JPanel messages;
	static File selectedFile= null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
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
	public TeacherHome() throws IOException {
		teacherConn= new ServerConnector("user_data");
		attendConn= new ServerConnector("attendance");
		fileConn= new ServerConnector("files");
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
		
		// Classroom Tab Code (Add the following to the TeacherHome constructor)

		// Panel for Classroom Tab
		// Classroom Tab Code (Add the following to the TeacherHome constructor)

		// Panel for Classroom Tab
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(new BorderLayout());  // Using BorderLayout for easy component placement
		messages= new JPanel(new VerticalFlowLayout(10));
		JScrollPane jsp= new JScrollPane(messages);
		panel_1.add(jsp, BorderLayout.CENTER);
		// ComboBox for recipient selection (A, B, C, or DM)
		JComboBox<String> recipientComboBox = new JComboBox<>();
		JTextField studentIDField= new JTextField(8);
		recipientComboBox.addItem("A");
		recipientComboBox.addItem("B");
		recipientComboBox.addItem("C");
		recipientComboBox.addItem("DM (Direct Message)");
		refreshMessages();

		// JTextArea for message input (long message bar)
		JTextField messageTextArea = new JTextField(20);  // 5 rows, 40 columns for message area

		// Send Message Button
		JButton sendMessageButton = new JButton("Send Message");
		sendMessageButton.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        String recipient = (String) recipientComboBox.getSelectedItem();
		        String message = messageTextArea.getText();
		        ResultSet rs= teacherConn.executeQuery("SELECT dept from teacher where teacherid='"+userID+"';");
		        String dept="CSE";
		        try {
					if(rs.next()) {
						dept= rs.getString("dept");
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		        if (message.isEmpty()) {
		            JOptionPane.showMessageDialog(panel_1, "Please enter a message before sending.");
		        } else {
		            String recipientID = "";  // Store the recipient ID for DM
		            ArrayList<String> recipients= null; //for multi recipient
		            if ("DM (Direct Message)".equals(recipient)) {
		                recipientID = studentIDField.getText();  // Get the student ID entered
		                if (recipientID.isEmpty()) {
		                    JOptionPane.showMessageDialog(panel_1, "Please enter the student ID for DM.");
		                    return;  // Stop further processing if DM ID is missing
		                }
		                sendMessage(recipientID, message);
		            }
		            else {
		            	ResultSet st= teacherConn.executeQuery("SELECT rollno from student WHERE class='"+recipientComboBox.getSelectedItem()+"' AND dept='"+dept+"';");
		            	try {
							while(st.next()) {
								sendMessage(st.getString("rollno"), message);
							}
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
		            }
		            
		            
		            JOptionPane.showMessageDialog(panel_1, "Message sent to " + recipient + " (Student ID: " + recipientID + ")");
		            messageTextArea.setText("");  // Clear the message area after sending
		        }
		        refreshMessages();
		    }
		});

		// Panel for south section (holding the recipient ComboBox, message area, and Send button)
		JPanel southPanel = new JPanel();
		southPanel.setLayout(new FlowLayout());
		southPanel.add(new JLabel("Select Recipient:"));
		southPanel.add(recipientComboBox);
		southPanel.add(studentIDField);
		studentIDField.setVisible(false);
		southPanel.add(messageTextArea);
		southPanel.add(sendMessageButton);  // Place the send button at the bottom

		// JTextField for entering the student ID (only visible when DM is selected)
		 // Text field for entering the student ID
		studentIDField.setVisible(false);  // Initially hide the student ID field

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
		panel_1.add(southPanel, BorderLayout.SOUTH);
		// Add the components to the Classroom panel
		 // Add the student ID field above the message area (only visible for DM)

		

		// Add Classroom tab to the Tabbed Pane
		tabbedPane.addTab("Classroom", null, panel_1, null);

		
		// Panel for Notes Tab
		// Notes Tab - Changes and Fixes
		// Fixing the duplicate Notes tab issue by removing the extra additions of the same tab.

		        // Panel for Notes Tab
		        JPanel panel_2 = new JPanel();
		        panel_2.setLayout(new BorderLayout());

		        // Notes Section
		        try {
		            ResultSet nres = fileConn.executeQuery("SELECT * FROM notes WHERE teacherid=" + userID);

		            ArrayList<String> nname = new ArrayList<>();
		            ArrayList<String> ndou = new ArrayList<>();
		            ArrayList<String> nfilePaths = new ArrayList<>();

		            while (nres.next()) {
		                nname.add(nres.getString("name"));
		                ndou.add(nres.getString("added"));
		                nfilePaths.add(nres.getString("filepath"));
		            }

		            int rows = nname.size();
		            Object[][] notesData = new Object[rows][3];  // Adding a 3rd column for the buttons

		            // Populate the table data
		            for (int i = 0; i < rows; i++) {
		                notesData[i][0] = nname.get(i); // File name
		                notesData[i][1] = ndou.get(i);  // Date of upload

		                // Create an "Open File" button in the last column
		                final int index = i;  // Required to make 'index' final for lambda expression
		                JButton openButton = new JButton("Open File");
		                openButton.addActionListener(e -> {
		                    try {
		                        System.out.println(nfilePaths.get(index));
		                        Desktop.getDesktop().open(new java.io.File(nfilePaths.get(index)));
		                    } catch (IOException ex) {
		                        ex.printStackTrace();
		                    }
		                });

		                notesData[i][2] = openButton;  // Action column
		            }

		            // Create the table with new data (file names, dates, and buttons)
		            String[] columnNames = {"Name", "Date of Upload", "Action"};
		            notesTable = new DefaultTableModel(notesData, columnNames);

		            // JTable setup
		            JTable nt = new JTable(notesTable);
		            nt.setDefaultEditor(Object.class, null); // Disable cell editing

		            // Make sure the action buttons (Open File) are displayed correctly
		            nt.getColumnModel().getColumn(2).setCellRenderer(new ButtonRenderer());
		            nt.getColumnModel().getColumn(2).setCellEditor(new ButtonEditor(new JCheckBox()));

		            // Wrap the table in a JScrollPane and add it to the panel
		            JScrollPane scrollPane = new JScrollPane(nt);
		            panel_2.add(scrollPane, BorderLayout.CENTER);

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

		        panel_2.add(bottom, BorderLayout.SOUTH);

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

		     
		
		     
		
		JComboBox<Integer> semChoose= new JComboBox<>();
		for(int i=1;i<=8;i++)
			semChoose.addItem(i);
		semChoose.setSelectedIndex(0);
		
		JComboBox<Character> classChoose= new JComboBox<>();
		ResultSet temp= teacherConn.executeQuery("select classes from teacher where teacherid='"+userID+"';");
		try {
			if(temp.next()) {
				String classes[]= temp.getString("classes").split(",");
				for(String c : classes)
					classChoose.addItem(c.charAt(0));
			}
		}
		catch(SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		JPanel panel_5 = new JPanel();
		panel_5.setLayout(new BorderLayout());
		panel_5.add(new JLabel("Student attendance"), BorderLayout.NORTH);
		JPanel centre5= new JPanel(new GridLayout());
		JPanel studentIn = new JPanel();
		centre5.add(studentIn);
		studentIn.add(new JLabel("Enter the roll no.: "));
		JTextField rollNoInput = new JTextField(8);
		studentIn.add(rollNoInput);
		JButton getAttendance = new JButton("Get details");
		getAttendance.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshAttendance(rollNoInput.getText());
			}
		});
		studentIn.add(getAttendance);
		
		panel_5.add(centre5);
		String cols[]= {"Date","H1","H2","H3","H4","H5","H6","H7","H8"};
		attendanceTable = new DefaultTableModel(null, cols);
		//actually updating attendance
		try {
			ResultSet rs= attendConn.executeQuery("SELECT * from student_attendance WHERE rollno="+userID);
			while(rs.next()) {
				System.out.println(rs);
				Object row[]= {rs.getString("class_date"), rs.getString("p1"), rs.getString("p2"), rs.getString("p3"), rs.getString("p4"), rs.getString("p5"), rs.getString("p6"), rs.getString("p7"), rs.getString("p8")};
				attendanceTable.addRow(row);
			}
		}
		catch(SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		JTable jt = new JTable(attendanceTable);
		JScrollPane jsp1= new JScrollPane(jt);
		centre5.add(jsp1);
		tabbedPane.addTab("Attendance", null, panel_5, null);
		
		
		
		JLabel lblWelcomeBackUser = new JLabel("Dr. "+username + " | Teacher");
		contentPane.add(lblWelcomeBackUser, BorderLayout.NORTH);
	}
	private void refreshTestsTable() {
		// TODO Auto-generated method stub
		
	}

	private void refreshAssignmentsTable() {
		// TODO Auto-generated method stub
		
	}

	void refreshAttendance(String rno) {
		try {
			attendanceTable.setRowCount(0);
			ResultSet rs= attendConn.executeQuery("SELECT * from student_attendance WHERE rollno='"+rno+"'");
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
	public void refreshMessages() {
    	ResultSet rs = fileConn.executeQuery("SELECT * from messages where rec='"+userID+"' OR sender='"+userID+"';");
    	try {
	    	while(rs.next()) {
	    		messages.add(new MessageBubble(rs.getString("sender"),rs.getString("rec"),rs.getString("message"),rs.getString("dom"),userID));
	    	}
    	}
    	catch(Exception e) {
    		System.out.println(e.getMessage());
    	}
    }
    public void sendMessage(String userID, String message) {
    	System.out.println(userID+" "+message);
    	if(userID.length()==5 && message.length()!=0) {
    		String rec="";
    		try {
    			ResultSet test= teacherConn.executeQuery("SELECT * from common WHERE uid="+userID);
    			if(test.next()) {
    				rec= test.getString("name");
    			}
    			else {
    				JOptionPane.showMessageDialog(null, "No such user!");
    				return;
    			}
    			Date currDate= new Date();
    			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    			String date= sdf.format(currDate);
    			fileConn.executeUpdates("INSERT into messages () values('"+TeacherHome.userID+"','"+userID+"','"+message+"','"+date+"');");
    			JOptionPane.showMessageDialog(null, "Sent message to "+rec);
    		}
    		catch(Exception e) {
    			
    		}
    		
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
		notesTable.setRowCount(0);
		for (int i = 0; i < rows; i++) {
			String [] t= {nname.get(i), ndou.get(i)};
			notesTable.addRow(t);  // First column - Name
			  // Second column - Date of upload
		}
		
		
	}catch(SQLException e){
		System.err.println("SQLEXception");
	}
	}
		
	}
