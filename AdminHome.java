package stutor;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class AdminHome extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable userTable;  // Table to display users in the User Management tab
    private JTextArea messageTextArea;  // TextArea to write messages in the Messaging tab

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
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

        // Dashboard Tab
        JPanel dashboardPanel = new JPanel();
        dashboardPanel.setLayout(new BorderLayout());
        JLabel dashboardLabel = new JLabel("Welcome to Admin Dashboard");
        dashboardLabel.setFont(new Font("Arial", Font.BOLD, 16));
        dashboardPanel.add(dashboardLabel, BorderLayout.CENTER);
        tabbedPane.addTab("Dashboard", null, dashboardPanel, "Admin Dashboard");

        // User Management Tab
        JPanel userManagementPanel = new JPanel();
        userManagementPanel.setLayout(new BorderLayout());
        
        // Table for displaying users
        String[] columnNames = {"User ID", "Username", "Email", "Department"};
        Object[][] data = {};  // Empty data for now; you can populate this with real data
        userTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(userTable);
        userManagementPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Add New User Button
        JPanel userButtonsPanel = new JPanel();
        JButton addUserButton = new JButton("Add New User");
        addUserButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Open NewUser.java here
                NewStudent newUserFrame = new NewStudent();
                newUserFrame.setVisible(true);
            }
        });
        JButton changePasswordButton = new JButton("Change Password");
        JButton removeUserButton = new JButton("Remove User");

        userButtonsPanel.add(addUserButton);
        userButtonsPanel.add(changePasswordButton);
        userButtonsPanel.add(removeUserButton);
        
        userManagementPanel.add(userButtonsPanel, BorderLayout.SOUTH);
        tabbedPane.addTab("User Management", null, userManagementPanel, "Manage Users");

        // Messaging Tab
        JPanel messagingPanel = new JPanel();
        messagingPanel.setLayout(new BorderLayout());

        // Text Area to write messages
        messageTextArea = new JTextArea(10, 30);
        JScrollPane messageScrollPane = new JScrollPane(messageTextArea);
        messagingPanel.add(messageScrollPane, BorderLayout.CENTER);

        // Send Message Button
        JPanel messagingButtonsPanel = new JPanel();
        JButton sendMessageButton = new JButton("Send Message");
        sendMessageButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Functionality to send message to all users (implement logic)
                String message = messageTextArea.getText();
                if (message.isEmpty()) {
                    JOptionPane.showMessageDialog(AdminHome.this, "Please enter a message to send.");
                } else {
                    // Send the message (implement sending functionality)
                    JOptionPane.showMessageDialog(AdminHome.this, "Message sent successfully!");
                }
            }
        });
        messagingButtonsPanel.add(sendMessageButton);

        messagingPanel.add(messagingButtonsPanel, BorderLayout.SOUTH);
        tabbedPane.addTab("Messaging", null, messagingPanel, "Send Messages to All Users");

        // Set up the admin label in the north of the panel
        JLabel adminLabel = new JLabel("Admin | Welcome");
        contentPane.add(adminLabel, BorderLayout.NORTH);
    }
}
