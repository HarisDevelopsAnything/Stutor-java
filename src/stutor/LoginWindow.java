package stutor;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.UIManager;
import javax.swing.SpringLayout;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JPasswordField;

public class LoginWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
	private JPasswordField passwordField;

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
		setFont(new Font("Product Sans", Font.PLAIN, 12));
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(UIManager.getColor("Table.dropLineColor"));
		panel.setBounds(0, 0, 440, 64);
		contentPane.add(panel);
		
		JLabel lblStutorA = new JLabel("Stutor - a place where students and teachers collaborate.");
		lblStutorA.setFont(new Font("Product Sans", Font.BOLD, 15));
		lblStutorA.setForeground(Color.WHITE);
		panel.add(lblStutorA);
		
		JLabel lblLoginToYour = new JLabel("Login to your account");
		lblLoginToYour.setForeground(Color.WHITE);
		lblLoginToYour.setFont(new Font("Product Sans", Font.BOLD, 17));
		panel.add(lblLoginToYour);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(0, 64, 440, 203);
		contentPane.add(panel_1);
		SpringLayout sl_panel_1 = new SpringLayout();
		panel_1.setLayout(sl_panel_1);
		
		JLabel lblUserId = new JLabel("User ID:");
		panel_1.add(lblUserId);
		
		JLabel lblPassword = new JLabel("Password:");
		sl_panel_1.putConstraint(SpringLayout.WEST, lblUserId, 0, SpringLayout.WEST, lblPassword);
		sl_panel_1.putConstraint(SpringLayout.NORTH, lblPassword, 80, SpringLayout.NORTH, panel_1);
		sl_panel_1.putConstraint(SpringLayout.WEST, lblPassword, 110, SpringLayout.WEST, panel_1);
		panel_1.add(lblPassword);
		
		textField = new JTextField();
		sl_panel_1.putConstraint(SpringLayout.NORTH, textField, 40, SpringLayout.NORTH, panel_1);
		sl_panel_1.putConstraint(SpringLayout.WEST, textField, 19, SpringLayout.EAST, lblUserId);
		sl_panel_1.putConstraint(SpringLayout.NORTH, lblUserId, 2, SpringLayout.NORTH, textField);
		panel_1.add(textField);
		textField.setColumns(10);
		
		JButton btnNewButton = new JButton("Login");
		sl_panel_1.putConstraint(SpringLayout.SOUTH, btnNewButton, -48, SpringLayout.SOUTH, panel_1);
		sl_panel_1.putConstraint(SpringLayout.EAST, btnNewButton, -180, SpringLayout.EAST, panel_1);
		panel_1.add(btnNewButton);
		
		passwordField = new JPasswordField();
		sl_panel_1.putConstraint(SpringLayout.NORTH, passwordField, -3, SpringLayout.NORTH, lblPassword);
		sl_panel_1.putConstraint(SpringLayout.WEST, passwordField, 8, SpringLayout.EAST, lblPassword);
		sl_panel_1.putConstraint(SpringLayout.EAST, passwordField, 0, SpringLayout.EAST, textField);
		panel_1.add(passwordField);
	}
}
