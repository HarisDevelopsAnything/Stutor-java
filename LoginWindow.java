package stutor;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;


public class LoginWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField unameField;
	private JPasswordField passwordField;
	private boolean isLoggedIn;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginWindow frame = new LoginWindow();
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
	public LoginWindow() {
		setTitle("Stutor | Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 509, 333);
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		ImageIcon img = new ImageIcon("/stutor/icons/stutor-color.png");
		setIconImage(img.getImage());
		JPanel panel = new JPanel();
		panel.setBackground(UIManager.getColor("Button.focus"));
		contentPane.add(panel, BorderLayout.NORTH);
		JLabel lblWelcomeBack = new JLabel("Welcome back!");
		panel.add(lblWelcomeBack);
		JPanel centerPanel= new JPanel(new GridLayout(7,1));
		JLabel lblEnterYourCredentials = new JLabel("Enter your credentials");
		JPanel headingPanel= new JPanel();
		headingPanel.add(lblEnterYourCredentials);
		centerPanel.add(headingPanel);
		
		JPanel row1= new JPanel();
		JLabel lblUsername = new JLabel("Username:");
		row1.add(lblUsername);
		unameField = new JTextField();
		row1.add(unameField);
		unameField.setColumns(10);
		
		JPanel row2= new JPanel();
		JLabel lblPassword = new JLabel("Password: ");
		row2.add(lblPassword);
		passwordField = new JPasswordField();
		passwordField.setColumns(10);
		row2.add(passwordField);
		
		JPanel row3= new JPanel();
		JButton btnLogin = new JButton("Login");
		JButton btnForgotPasswd = new JButton("Forgot password?");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					login();
				}
		});
		btnForgotPasswd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					forgotPasswd();
				}
		});
		row3.add(btnLogin);
		centerPanel.add(row1);
		centerPanel.add(row2);
		centerPanel.add(row3);
		contentPane.add(centerPanel, BorderLayout.CENTER);
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(UIManager.getColor("MenuItem.disabledForeground"));
		contentPane.add(panel_2, BorderLayout.SOUTH);
		
		JLabel lblKeepYourPassword = new JLabel("Keep your password secure. In case of issues, contact admin.");
		panel_2.add(lblKeepYourPassword);
		panel_2.add(btnForgotPasswd);
	}
	void login() {
		JOptionPane.showMessageDialog(null,"Hello, "+ unameField.getText()+". Logging in...");
		ServerConnector loginConn = new ServerConnector(); //defaults to user login db
		loginConn.connect();
		String resp= loginConn.loginManager(unameField.getText(), passwordField.getText());
		if(resp.contains("student")) {
			StudentHome.username= resp.replace("student", "");
			StudentHome.userID= unameField.getText(); 
			StudentHome.main(null);
			setVisible(false);
		}
		else if(resp.contains("teacher")) {
			TeacherHome.username= resp.replace("teacher", "");
			TeacherHome.userID= unameField.getText();
			TeacherHome.main(null);
			setVisible(false);
		}
		else
			JOptionPane.showMessageDialog(null, "Invalid username/password!");
	}
	void forgotPasswd() {
		JOptionPane.showMessageDialog(null, "Please contact admin directly to reset your password.\nKeep your password secure at all times!", "User guidance", JOptionPane.WARNING_MESSAGE);;
	}
}
