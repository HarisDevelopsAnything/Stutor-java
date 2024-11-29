package stutor;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.event.*;
import java.beans.Statement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AdminHome extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    static DefaultTableModel userTable;
    static JTable userTableUI;  // Table to display users in the User Management tab
    private JPanel messages;
    static ServerConnector userConn;
    static ServerConnector userdataConn;
    static ServerConnector messageConn;
    static JScrollPane messageScroll;
    /**
     * Launch the application.
     */
    public static void main(String[] args) {
    	userTableUI= new JTable(userTable);
    	userConn= new ServerConnector();
    	userdataConn = new ServerConnector("user_data");
    	messageConn= new ServerConnector("files");
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    AdminHome frame = new AdminHome();
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
    public AdminHome() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 400);
        contentPane = new JPanel();
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        // User Management Tab
        JPanel userManagementPanel = new JPanel();
        userManagementPanel.setLayout(new BorderLayout());
        
        // Table for displaying users
        String[] columnNames = {"User ID", "Username", "Email", "User type"};
        Object[][] data = {};  // Empty data for now; you can populate this with real data
        userTable = new DefaultTableModel(data, columnNames);
        userTableUI= new JTable(userTable);
        JScrollPane scrollPane = new JScrollPane(userTableUI);
        userManagementPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Add New User Button
        JPanel userButtonsPanel = new JPanel(new GridLayout(1,5));
        JButton addStudentButton = new JButton("Add New Student");
        addStudentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Open NewUser.java here
                NewStudent newUserFrame = new NewStudent();
                newUserFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                newUserFrame.setVisible(true);
            }
        });
        JButton addTeacherButton = new JButton("Add New Teacher");
        addTeacherButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Open NewUser.java here
                NewTeacher newUserFrame = new NewTeacher();
                newUserFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                newUserFrame.setVisible(true);
            }
        });
        JButton updateStudentButton = new JButton("Update Student");
        updateStudentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Open NewUser.java here
                UpdateStudent newUserFrame = new UpdateStudent();
                newUserFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                newUserFrame.setVisible(true);
            }
        });
        JButton updateTeacherButton = new JButton("Update Teacher");
        updateTeacherButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Open NewUser.java here
                UpdateTeacher newUserFrame = new UpdateTeacher();
                newUserFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                newUserFrame.setVisible(true);
            }
        });
        JButton removeUserButton = new JButton("Remove User");
        removeUserButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		deleteUser();
        	}
        });
        JButton refreshUsersButton = new JButton("↻");
        refreshUsersButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		refreshUsers();
        	}
        });

        userButtonsPanel.add(addStudentButton);
        userButtonsPanel.add(addTeacherButton);
        userButtonsPanel.add(updateStudentButton);
        userButtonsPanel.add(updateTeacherButton);
        userButtonsPanel.add(removeUserButton);
        userButtonsPanel.add(refreshUsersButton);
        
        userManagementPanel.add(userButtonsPanel, BorderLayout.SOUTH);
        tabbedPane.addTab("User Management", null, userManagementPanel, "Manage Users");

        // Messaging Tab
        JPanel messagingPanel = new JPanel();
        messagingPanel.setLayout(new BorderLayout());
        
        // message area to show bubbles
        messages = new JPanel(new VerticalFlowLayout(10));
        
        messageScroll = new JScrollPane(messages);
        messagingPanel.add(messageScroll, BorderLayout.CENTER);
        refreshMessages();
        // Send Message Button
        JPanel messagingButtonsPanel = new JPanel();
        JTextField userID= new JTextField(5);
        JTextField messageInput= new JTextField(20);
        JButton sendMessageButton = new JButton("Send Message");
     
        sendMessageButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Functionality to send message to all users (implement logic)
                String message = messageInput.getText();
                if (message.isEmpty()) {
                    JOptionPane.showMessageDialog(AdminHome.this, "Please enter a message to send.");
                } else {
                    sendMessage(userID.getText(),messageInput.getText());
                    JOptionPane.showMessageDialog(AdminHome.this, "Message sent successfully!");
                    refreshMessages();
                }
            }
        });
        
        messagingButtonsPanel.add(new JLabel("User ID: "));
        messagingButtonsPanel.add(userID);
        messagingButtonsPanel.add(new JLabel("Message"));
        messagingButtonsPanel.add(messageInput);
        messagingButtonsPanel.add(sendMessageButton);
       
        
        
        messagingPanel.add(messagingButtonsPanel, BorderLayout.SOUTH);
        tabbedPane.addTab("Messaging", null, messagingPanel, "Send Messages to All Users");

        // Set up the admin label in the north of the panel
        JLabel adminLabel = new JLabel("Admin | Welcome");
        contentPane.add(adminLabel, BorderLayout.NORTH);
    }
    public void refreshMessages() {
    	messages.removeAll();
    	ResultSet rs = messageConn.executeQuery("SELECT * from messages where rec='admin' OR sender='admin';");
    	try {
	    	while(rs.next()) {
	    		messages.add(new MessageBubble(rs.getString("sender"),rs.getString("rec"),rs.getString("message"),rs.getString("dom"),"admin"));
	    	}
    	}
    	catch(Exception e) {
    		System.out.println(e.getMessage());
    	}
    	messages.revalidate();
    	GUIMisc.scrollToBottom(messageScroll);
    }
    public void sendMessage(String userID, String message) {
    	if(userID.length()==5 && message.length()!=0) {
    		String rec="";
    		try {
    			ResultSet test= userdataConn.executeQuery("SELECT * from common WHERE uid="+userID);
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
    			messageConn.executeUpdates("INSERT into messages () values('admin','"+userID+"','"+message+"','"+date+"');");
    			
    		}
    		catch(Exception e) {
    			
    		}
    		
    	}
    }
    static void deleteUser() {
    	String du= JOptionPane.showInputDialog("Enter the ID to delete: ");
		String query= "DELETE FROM users WHERE userid='"+du+"';";
		try {
			ResultSet rs= userdataConn.executeQuery("SELECT * from common WHERE uid='"+du+"';");
			if(!rs.next()) {
				JOptionPane.showMessageDialog(null, "No such user!");
				return;
			}
			String usertype= (rs.getString("type").equals("S")?"student":"teacher");
			userConn.executeUpdates("DELETE from "+usertype+"s where id="+du);
			userdataConn.executeUpdates("DELETE from "+usertype+" where rollno="+du);
			userdataConn.executeUpdates("DELETE from common where uid="+du);
			JOptionPane.showMessageDialog(null, "User "+du+" deleted!");
		}
		catch(Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}
    public void refreshUsers() {
    	userTable.setRowCount(0); //empty table
    	try {
    		ResultSet rs1= userConn.executeQuery("SELECT * from students");
    		ResultSet rs2= userConn.executeQuery("SELECT * from teachers");
    		String uid, uname="", mail="", type;
    		while(rs1.next()) {
    			uid= rs1.getString("id");
    			try {
    				ResultSet rs3= userdataConn.executeQuery("SELECT email,name from common where uid='"+uid+"';");
    				if(rs3.next())
    					mail= rs3.getString("email");
    					if(mail.equals(""))
    						mail="Not present!";
    					uname= rs3.getString("name");
    			}
    			catch(Exception e) {
    				mail="Unable to fetch!";
    			}
    			type= "Student";
    			String row[]= {uid, uname, mail, type};
    			userTable.addRow(row);
    		}
    		while(rs2.next()) {
    			uid= rs2.getString("id");
    			try {
    				ResultSet rs3= userdataConn.executeQuery("SELECT email,name from common where uid='"+uid+"';");
    				if(rs3.next())
    					mail= rs3.getString("email");
    					if(mail.equals(""))
    						mail="Not present!";
    					uname= rs3.getString("name");
    			}
    			catch(Exception e) {
    				mail="Unable to fetch!";
    			}
    			type= "Teacher";
    			String row[]= {uid, uname, mail, type};
    			userTable.addRow(row);
    		}
    		
    	}
    	catch(Exception e) {
    		JOptionPane.showMessageDialog(null, "Failed to update user list!");
    	}
    }
}
