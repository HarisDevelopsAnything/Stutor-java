package stutor;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class StudentHome extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	static String username = null;
	static String userID= null;
	static String dept= null;
	static ServerConnector studentConn= null;
	static ServerConnector attendConn= null;
	static ServerConnector scoreConn = null;
	static ServerConnector fileConn= null;
	static DefaultTableModel notesTable;
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
		
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(new BorderLayout());
		panel_1.add(new JLabel("Subject wise scores"), BorderLayout.NORTH);
		JPanel center = new JPanel(new BorderLayout());
		JPanel chooser = new JPanel(new FlowLayout());
		JComboBox<Integer> semChoose = new JComboBox<>();
		for(int i=1;i<=8;i++)
		semChoose.addItem(i);
		semChoose.setSelectedItem(1); //default 1st sem mark
		String ecols[]= {"Course code","Course name","Score","Result"};
		JTabbedPane scorePanel = new JTabbedPane();
		JPanel cat1 = new JPanel();
		cat1.setLayout(new BorderLayout());
		DefaultTableModel cat1Table= new DefaultTableModel(null, ecols);
		JTable jt1= new JTable(cat1Table);
		cat1Table.addRow(ecols);
		cat1.add(jt1);
		JPanel cat2= new JPanel();
		cat2.setLayout(new BorderLayout());
		DefaultTableModel cat2Table= new DefaultTableModel(null,ecols);
		JTable jt2= new JTable(cat2Table);
		cat2Table.addRow(ecols);
		cat2.add(jt2, BorderLayout.CENTER);
		JPanel cat3= new JPanel();
		cat3.setLayout(new BorderLayout());
		DefaultTableModel cat3Table= new DefaultTableModel(null,ecols);
		JTable jt3= new JTable(cat3Table);
		cat3Table.addRow(ecols);
		cat3.add(jt3);
		
		scorePanel.addTab("CAT 1", null, cat1, null);
		scorePanel.addTab("CAT 2", null, cat2, null);
		scorePanel.addTab("CAT 3", null, cat3, null);
		chooser.add(new JLabel("Select semester"));
		chooser.add(semChoose);
		center.add(chooser, BorderLayout.NORTH);
		center.add(scorePanel, BorderLayout.CENTER);
		panel_1.add(center, BorderLayout.CENTER);
		tabbedPane.addTab("Scores", null, panel_1, null);
		
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
        tabbedPane.addTab("Notes", null, panel_2, null);

     

     // Panel for Tests Tab
        JPanel panel_3 = new JPanel();
        panel_3.setLayout(new BorderLayout());

        // Tests Section
        try {
            ResultSet tres = fileConn.executeQuery("SELECT * FROM tests WHERE teacherid=" + userID);

            ArrayList<String> tname = new ArrayList<>();
            ArrayList<String> tdou = new ArrayList<>();
            ArrayList<String> tfilePaths = new ArrayList<>();

            while (tres.next()) {
                tname.add(tres.getString("name"));
                tdou.add(tres.getString("added"));
                tfilePaths.add(tres.getString("filepath"));
            }

            int rows = tname.size();
            Object[][] testsData = new Object[rows][3];  // Adding a 3rd column for the buttons

            // Populate the table data
            for (int i = 0; i < rows; i++) {
                testsData[i][0] = tname.get(i); // Test name
                testsData[i][1] = tdou.get(i);  // Date of upload

                // Create an "Open File" button in the last column
                final int index = i;  // Required to make 'index' final for lambda expression
                JButton openButton = new JButton("Open Test");
                openButton.addActionListener(e -> {
                    try {
                        Desktop.getDesktop().open(new java.io.File(tfilePaths.get(index)));
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });

                testsData[i][2] = openButton;  // Action column
            }

            // Create the table with new data (test names, dates, and buttons)
            String[] columnNames = {"Name", "Date of Upload", "Action"};
            DefaultTableModel testsTable = new DefaultTableModel(testsData, columnNames);

            // JTable setup
            JTable testTable = new JTable(testsTable);
            testTable.setDefaultEditor(Object.class, null); // Disable cell editing

            // Make sure the action buttons (Open Test) are displayed correctly
            testTable.getColumnModel().getColumn(2).setCellRenderer(new ButtonRenderer());
            testTable.getColumnModel().getColumn(2).setCellEditor(new ButtonEditor(new JCheckBox()));

            // Wrap the table in a JScrollPane and add it to the panel
            JScrollPane testScrollPane = new JScrollPane(testTable);
            panel_3.add(testScrollPane, BorderLayout.CENTER);

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("SQLException: " + e.getMessage());
        }

        // Bottom panel for uploading test files
        JPanel bottomTest = new JPanel();
        JFileChooser testFileChooser = new JFileChooser();
        testFileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));

        JButton testFileButton = new JButton("Select Test");
        bottomTest.add(testFileButton);

        JLabel testTitleLabel = new JLabel("Enter a title:");
        testTitleLabel.setFont(new Font("Ubuntu", Font.PLAIN, 20));
        bottomTest.add(testTitleLabel);

        JTextField testTitleField = new JTextField(8);
        bottomTest.add(testTitleField);

        JButton uploadTestButton = new JButton("Upload Test");
        bottomTest.add(uploadTestButton);

        //panel_3.add(bottomTest, BorderLayout.SOUTH);

        // Test file selection button action listener
        testFileButton.addActionListener(e -> {
            int result = testFileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                selectedFile = testFileChooser.getSelectedFile();
                String fileName = selectedFile.getName();
                String filePath = selectedFile.getAbsolutePath();

                // Validate file extension (only PDF or DOCX for example)
                if (!(fileName.endsWith(".pdf") || fileName.endsWith(".docx") || fileName.endsWith(".txt"))) {
                    JOptionPane.showMessageDialog(this, "Only PDF, TXT, and DOCX files are allowed.");
                    return; // Stop further processing if invalid file type
                }

                JOptionPane.showMessageDialog(this, "Selected File: " + fileName);
                int resp = JOptionPane.showOptionDialog(this, "Do you want a preview?", "Added 1 file", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Yes", "No"}, "No");
                if (resp == JOptionPane.YES_OPTION) {
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

        // Upload test action listener
        uploadTestButton.addActionListener(e -> {
            String title = testTitleField.getText();
            if (selectedFile != null && !title.isEmpty()) {
                String query = "INSERT INTO tests (name, filepath, teacherid, added, comment) VALUES ('" 
                                + selectedFile.getName() + "', '" 
                                + selectedFile.getAbsolutePath().replace("\\", "/") + "', '" 
                                + userID + "', '" 
                                + LocalDate.now() + "', '" 
                                + title + "');";
                fileConn.executeUpdates(query);
                refreshTestsTable(); // Refresh the table after uploading the test
                JOptionPane.showMessageDialog(this, "Test uploaded successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Please select a test file and provide a title.");
            }
        });

        // Add the panel to the tabbed pane
        tabbedPane.addTab("Tests", null, panel_3, null);


        // Panel for Assignments Tab
        JPanel panel_4 = new JPanel();
        panel_4.setLayout(new BorderLayout());

        // Assignments Section
        try {
            ResultSet ares = fileConn.executeQuery("SELECT * FROM assignments WHERE teacherid=" + userID);

            ArrayList<String> aname = new ArrayList<>();
            ArrayList<String> adou = new ArrayList<>();
            ArrayList<String> afilePaths = new ArrayList<>();

            while (ares.next()) {
                aname.add(ares.getString("name"));
                adou.add(ares.getString("added"));
                afilePaths.add(ares.getString("filepath"));
            }

            int rows = aname.size();
            Object[][] assignmentsData = new Object[rows][3];  // Adding a 3rd column for the buttons

            // Populate the table data
            for (int i = 0; i < rows; i++) {
                assignmentsData[i][0] = aname.get(i); // Assignment name
                assignmentsData[i][1] = adou.get(i);  // Date of upload

                // Create an "Open File" button in the last column
                final int index = i;  // Required to make 'index' final for lambda expression
                JButton openButton = new JButton("Open Assignment");
                openButton.addActionListener(e -> {
                    try {
                        Desktop.getDesktop().open(new java.io.File(afilePaths.get(index)));
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });

                assignmentsData[i][2] = openButton;  // Action column
            }

            // Create the table with new data (assignment names, dates, and buttons)
            String[] columnNames = {"Name", "Date of Upload", "Action"};
            DefaultTableModel assignmentsTable = new DefaultTableModel(assignmentsData, columnNames);

            // JTable setup
            JTable assignmentTable = new JTable(assignmentsTable);
            assignmentTable.setDefaultEditor(Object.class, null); // Disable cell editing

            // Make sure the action buttons (Open Assignment) are displayed correctly
            assignmentTable.getColumnModel().getColumn(2).setCellRenderer(new ButtonRenderer());
            assignmentTable.getColumnModel().getColumn(2).setCellEditor(new ButtonEditor(new JCheckBox()));

            // Wrap the table in a JScrollPane and add it to the panel
            JScrollPane assignmentScrollPane = new JScrollPane(assignmentTable);
            panel_4.add(assignmentScrollPane, BorderLayout.CENTER);

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("SQLException: " + e.getMessage());
        }

        // Bottom panel for uploading assignment files
        JPanel bottomAssignment = new JPanel();
        JFileChooser assignmentFileChooser = new JFileChooser();
        assignmentFileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));

        JButton assignmentFileButton = new JButton("Select Assignment");
        bottomAssignment.add(assignmentFileButton);

        JLabel assignmentTitleLabel = new JLabel("Enter a title:");
        assignmentTitleLabel.setFont(new Font("Ubuntu", Font.PLAIN, 20));
        bottomAssignment.add(assignmentTitleLabel);

        JTextField assignmentTitleField = new JTextField(8);
        bottomAssignment.add(assignmentTitleField);

        JButton uploadAssignmentButton = new JButton("Upload Assignment");
        bottomAssignment.add(uploadAssignmentButton);
		
        //panel_4.add(bottomAssignment, BorderLayout.SOUTH);

        // Assignment file selection button action listener
        assignmentFileButton.addActionListener(e -> {
            int result = assignmentFileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                selectedFile = assignmentFileChooser.getSelectedFile();
                String fileName = selectedFile.getName();
                String filePath = selectedFile.getAbsolutePath();

                // Validate file extension (only PDF or DOCX for example)
                if (!(fileName.endsWith(".pdf") || fileName.endsWith(".docx") || fileName.endsWith(".txt"))) {
                    JOptionPane.showMessageDialog(this, "Only PDF, TXT, and DOCX files are allowed.");
                    return; // Stop further processing if invalid file type
                }

                JOptionPane.showMessageDialog(this, "Selected File: " + fileName);
                int resp = JOptionPane.showOptionDialog(this, "Do you want a preview?", "Added 1 file", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Yes", "No"}, "No");
                if (resp == JOptionPane.YES_OPTION) {
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

        // Upload assignment action listener
        uploadAssignmentButton.addActionListener(e -> {
            String title = assignmentTitleField.getText();
            if (selectedFile != null && !title.isEmpty()) {
                String query = "INSERT INTO assignments (name, filepath, teacherid, added, comment) VALUES ('" 
                                + selectedFile.getName() + "', '" 
                                + selectedFile.getAbsolutePath().replace("\\", "/") + "', '" 
                                + userID + "', '" 
                                + LocalDate.now() + "', '" 
                                + title + "');";
                fileConn.executeUpdates(query);
                refreshAssignmentsTable(); // Refresh the table after uploading the assignment
                JOptionPane.showMessageDialog(this, "Assignment uploaded successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Please select an assignment file and provide a title.");
            }
        });

        // Add the panel to the tabbed pane
        tabbedPane.addTab("Assignments", null, panel_4, null);

		
		JPanel panel_5 = new JPanel();
		panel_5.setLayout(new BorderLayout());
		panel_5.add(new JLabel("Student attendance"), BorderLayout.NORTH);
		JPanel centre5= new JPanel(new GridLayout());
		JPanel semChooser= new JPanel();
		semChooser.add(new JLabel("Select semester"));
		semChooser.add(semChoose);
		semChoose.addItemListener(new ItemListener() {
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
		JScrollPane jsp= new JScrollPane(jt);
		centre5.add(jsp);
		tabbedPane.addTab("Attendance", null, panel_5, null);
		
		JLabel lblWelcomeBackUser = new JLabel(username+ " | Student");
		contentPane.add(lblWelcomeBackUser, BorderLayout.NORTH);
	}
	private void refreshAssignmentsTable() {
		// TODO Auto-generated method stub
		
	}

	private void refreshTestsTable() {
		// TODO Auto-generated method stub
		
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
	void refreshScores(int sem) {
		/* yet to implement
		try {
			ResultSet subs= subjectConn.executeQuery("select * from "+);
			ResultSet rs1= scoreConn.executeQuery("select * from cat where rollno='"+userID+"' and sem="+sem+" and cat=1");
			ResultSet rs2= scoreConn.executeQuery("select * from cat where rollno='"+userID+"' and sem="+sem+" and cat=2");
			ResultSet rs3= scoreConn.executeQuery("select * from cat where rollno='"+userID+"' and sem="+sem+" and cat=3");
			while(rs1.next()) {
				cat1Table.addRow(rs1.getString(""))
			}
		}
		catch(Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		*/
	}

}
