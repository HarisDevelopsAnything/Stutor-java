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
import javax.swing.DefaultCellEditor;
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
	static JScrollPane messageScroll;
	static JTextField rollNoInput;
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
		ResultSet rs2= teacherConn.executeQuery("SELECT * from common WHERE uid="+userID);
		try {
			if(rs2.next()) {
				username= rs2.getString("name");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		messageScroll= new JScrollPane(messages);
		panel_1.add(messageScroll, BorderLayout.CENTER);
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
		            String[] columnNames = {"Name", "Date of Upload"};
		            notesTable = new DefaultTableModel(notesData, columnNames);

		            // JTable setup
		            JTable nt = new JTable(notesTable);
		            nt.setDefaultEditor(Object.class, null); // Disable cell editing

		            // Make sure the action buttons (Open File) are displayed correctly
		            

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
		// Attendance Tab
		JPanel panel_5 = new JPanel();
		panel_5.setLayout(new BorderLayout());
		panel_5.add(new JLabel("Student Attendance"), BorderLayout.NORTH);

		// Attendance Filter Panel
		JPanel filterPanel = new JPanel(new FlowLayout());
		JComboBox<Integer> semChoose1 = new JComboBox<>();
		for (int i = 1; i <= 8; i++) semChoose1.addItem(i);
		filterPanel.add(new JLabel("Semester: "));
		filterPanel.add(semChoose1);

		rollNoInput = new JTextField(8);
		filterPanel.add(new JLabel("Enter Roll No.: "));
		filterPanel.add(rollNoInput);

		JButton getAttendance = new JButton("Get Details");
		filterPanel.add(getAttendance);
		panel_5.add(filterPanel, BorderLayout.NORTH);

		// Attendance Table
		String cols[] = {"Date", "H1", "H2", "H3", "H4", "H5", "H6", "H7", "H8"};
		attendanceTable = new DefaultTableModel(null, cols);
		JTable attendanceTableUI = new JTable(attendanceTable);
		attendanceTableUI.setDefaultEditor(Object.class, null); // Initially non-editable

		JScrollPane jsp1 = new JScrollPane(attendanceTableUI);
		panel_5.add(jsp1, BorderLayout.CENTER);

		// Save Changes Button
		JButton saveChangesButton = new JButton("Save Changes");
		panel_5.add(saveChangesButton, BorderLayout.SOUTH);

		tabbedPane.addTab("Attendance", null, panel_5, null);

		// Add Action Listeners
		getAttendance.addActionListener(e -> {
		    String rollNo = rollNoInput.getText();
		    int semester = (Integer) semChoose1.getSelectedItem();
		    if (!rollNo.isEmpty()) {
		        refreshAttendance(rollNo, semester);
		        attendanceTableUI.setDefaultEditor(Object.class, new DefaultCellEditor(new JTextField())); // Enable editing
		    } else {
		        JOptionPane.showMessageDialog(this, "Please enter a valid roll number.");
		    }
		});

		saveChangesButton.addActionListener(e -> {
		    saveAttendanceChanges(rollNoInput.getText(), (Integer) semChoose1.getSelectedItem());
		});
	}
		// Refresh Attendance Method
	void refreshAttendance(String rno, int sem) {
	    try {
	    	addEmptyAttendance(rno,sem);
	        // Clear the table first
	        attendanceTable.setRowCount(0);

	        // Get the latest date from the attendance data
	        ResultSet rs = attendConn.executeQuery("SELECT MAX(class_date) AS last_date FROM student_attendance WHERE rollno='" + rno + "' AND sem="+sem);
	        String lastDate = null;
	        if (rs.next()) {
	            lastDate = rs.getString("last_date");
	        }

	        // Get the current date (today)
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	        String currentDate = sdf.format(new Date());

	        // Add attendance for existing dates
	        ResultSet attendanceResult = attendConn.executeQuery("SELECT * FROM student_attendance WHERE rollno='" + rno + "' AND sem="+sem+" AND class_date >= '" + lastDate + "' AND class_date <= '" + currentDate + "' ORDER BY class_date");
	        while (attendanceResult.next()) {
	            Object row[] = {attendanceResult.getString("class_date"),
	                            attendanceResult.getString("p1"),
	                            attendanceResult.getString("p2"),
	                            attendanceResult.getString("p3"),
	                            attendanceResult.getString("p4"),
	                            attendanceResult.getString("p5"),
	                            attendanceResult.getString("p6"),
	                            attendanceResult.getString("p7"),
	                            attendanceResult.getString("p8")};
	            attendanceTable.addRow(row);
	        }

	        // Add rows for dates between last recorded date and today (for empty attendance)
	        // You can use a loop to add these rows
	        LocalDate startDate = LocalDate.parse(lastDate);
	        LocalDate endDate = LocalDate.parse(currentDate);

	        while (startDate.isBefore(endDate)) {
	            startDate = startDate.plusDays(1);
	            String formattedDate = startDate.toString();  // "yyyy-MM-dd"
	            Object emptyRow[] = {formattedDate, "-", "-", "-", "-", "-", "-", "-", "-"};
	            
	            attendanceTable.addRow(emptyRow);
	        }

	        // Refresh the table view
	        JScrollPane jsp1 = new JScrollPane(new JTable(attendanceTable));
	        // Add the JScrollPane to the panel if required

	    } catch (SQLException e) {
	        JOptionPane.showMessageDialog(null, e.getMessage());
	    }
	}

	void addEmptyAttendance(String rno, int sem) {
	    try {
	        // Get the latest date from the attendance data
	        ResultSet rs = attendConn.executeQuery("SELECT MAX(class_date) AS last_date FROM student_attendance WHERE rollno='" + rno + "'");
	        String lastDate = null;
	        if (rs.next()) {
	            lastDate = rs.getString("last_date");
	        }

	        // Get the current date (today)
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	        String currentDate = sdf.format(new Date());

	        // Get the start and end dates (last recorded date and current date)
	        LocalDate startDate = LocalDate.parse(lastDate);
	        LocalDate endDate = LocalDate.parse(currentDate);

	        // Insert empty attendance records for dates from the last date to today
	        while (startDate.isBefore(endDate)) {
	            startDate = startDate.plusDays(1);
	            String formattedDate = startDate.toString();  // "yyyy-MM-dd"

	            // Prepare the insert query for empty attendance (with "-" for each period)
	            String insertQuery = "INSERT INTO student_attendance (sem, rollno, class_date, p1, p2, p3, p4, p5, p6, p7, p8) " +
	                                 "VALUES ("+sem+", '" + rno + "', '" + formattedDate + "', '-', '-', '-', '-', '-', '-', '-', '-')";

	            // Execute the insert query
	            attendConn.executeUpdates(insertQuery);
	        }

	        

	    } catch (SQLException e) {
	        JOptionPane.showMessageDialog(null, e.getMessage());
	    }
	}
		// Save Attendance Changes Method
		void saveAttendanceChanges(String rollNo, int semester) {
		    for (int i = 0; i < attendanceTable.getRowCount(); i++) {
		        String date = (String) attendanceTable.getValueAt(i, 0);
		        String[] hours = new String[8];
		        for (int j = 1; j <= 8; j++) {
		            hours[j - 1] = (String) attendanceTable.getValueAt(i, j);
		        }
		        try {
			        String query = String.format(
					    "UPDATE student_attendance SET p1='%s', p2='%s', p3='%s', p4='%s', p5='%s', p6='%s', p7='%s', p8='%s' " +
					    "WHERE rollno='%s' AND sem=%d AND cdate='%s'",
					    hours[0], hours[1], hours[2], hours[3], hours[4], hours[5], hours[6], hours[7], rollNo, semester, date
					);
					attendConn.executeUpdates(query);
		        }
		        catch(Exception e) {
		        	System.err.println(e.getMessage());
		        }
		    }
		    JOptionPane.showMessageDialog(null, "Attendance updated successfully!");
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
    	messages.revalidate();
    	GUIMisc.scrollToBottom(messageScroll);
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
