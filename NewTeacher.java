package stutor;

import java.awt.EventQueue;

import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Font;
import java.awt.*;
import javax.swing.*;

public class NewTeacher extends JFrame {

        private static final long serialVersionUID = 1L;
        private JPanel contentPane;
        private JTextField firstName;
        private JTextField lastName;
        private JTextField teacherID;
        private JTextField mobNo;
        private JTextField fatherName;
        private JTextField email;
        private JTextField loginID;
        static JTextField sal;
        private JPasswordField passwordField;
        static JComboBox<String> branch;
        static  JComboBox<String> gender;
        static JSpinner age;
        static JSpinner exp;
        static JComboBox<String> sec;
        static JComboBox<Integer> semSelect;
        static JRadioButton studentSel;
        static JTextArea addr;
        //servers
    	static ServerConnector userdataConn= null;
    	static ServerConnector userConn= null;
        /**
         * Launch the application.
         */
        public static void main(String[] args) {
        		userdataConn= new ServerConnector("user_data");
        		userConn= new ServerConnector();  //default to normal login db
                EventQueue.invokeLater(new Runnable() {
                        public void run() {
                                try {
                                        NewTeacher frame = new NewTeacher();
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
        public NewTeacher() {
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                setTitle("Create new teacher");
                setBounds(100, 100, 600, 450);
                contentPane = new JPanel();
                contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
                contentPane.setFont(new Font("Tahoma", Font.PLAIN, 30));
                setContentPane(contentPane);
                contentPane.setLayout(new BorderLayout(0, 0));

                JPanel panel = new JPanel();
                panel.setBackground(SystemColor.desktop);
                contentPane.add(panel, BorderLayout.NORTH);

                JLabel lblCreateNewStutor = new JLabel("Create new Stutor user");
                lblCreateNewStutor.setFont(new Font("Product Sans", Font.PLAIN, 10));
                lblCreateNewStutor.setForeground(new Color(255, 255, 255));
                panel.add(lblCreateNewStutor);

                JPanel panel_1 = new JPanel();
                contentPane.add(panel_1, BorderLayout.CENTER);
                panel_1.setLayout(new GridLayout(11, 2, 0, 0));

                JLabel lblNewLabel_10 = new JLabel("Personal details:");
                lblNewLabel_10.setFont(new Font("Tahoma", Font.PLAIN, 15));
                panel_1.add(lblNewLabel_10);

                JPanel panel_2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
                panel_1.add(panel_2);

                JLabel lblNewLabel = new JLabel("First name:");
                panel_2.add(lblNewLabel);

                firstName = new JTextField();
                panel_2.add(firstName);
                firstName.setColumns(10);

                JLabel lblNewLabel_1 = new JLabel("Last name:");
                panel_2.add(lblNewLabel_1);

                lastName = new JTextField();
                panel_2.add(lastName);
                lastName.setColumns(10);

                JLabel lblNewLabel_8 = new JLabel("Gender:");
                panel_2.add(lblNewLabel_8);

                gender = new JComboBox<>();
                gender.addItem("Male");
                gender.addItem("Female");
                gender.addItem("Prefer not to say");
                panel_2.add(gender);

                JPanel panel_3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
                panel_1.add(panel_3);

                JLabel lblNewLabel_2 = new JLabel("Age:");
                panel_3.add(lblNewLabel_2);

                age = new JSpinner();
                panel_3.add(age);
                
                JLabel lblNewLabel_21 = new JLabel("Exp:");
                panel_3.add(lblNewLabel_21);

                exp = new JSpinner();
                panel_3.add(exp);

                JLabel lblNewLabel_3 = new JLabel("Dept.:");
                panel_3.add(lblNewLabel_3);

                branch = new JComboBox<String>();
                branch.addItem("CSE");
                branch.addItem("ECE");
                branch.addItem("EEE");
                branch.setSelectedIndex(0);
                panel_3.add(branch);
/*
                JLabel lblNewLabel_3_1 = new JLabel("Section:");
                panel_3.add(lblNewLabel_3_1);

                sec = new JComboBox<String>();
                sec.addItem("A");
                sec.addItem("B");
                sec.addItem("C");
                sec.setSelectedIndex(0);
                panel_3.add(sec);

                JLabel lblNewLabel_6 = new JLabel("Sem:");
                panel_3.add(lblNewLabel_6);

                semSelect = new JComboBox<>();
                for(int i=1;i<=8;i++)
                	semSelect.addItem(i);
                panel_3.add(semSelect);
                */
                
                

                JLabel lblNewLabel_4 = new JLabel("Teacher ID.:");
                panel_3.add(lblNewLabel_4);

                teacherID = new JTextField();
                panel_3.add(teacherID);
                teacherID.setColumns(10);

                JPanel panel_4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
                panel_1.add(panel_4);

                JLabel lblNewLabel_7 = new JLabel("Father's name:");
                panel_4.add(lblNewLabel_7);

                fatherName = new JTextField();
                panel_4.add(fatherName);
                fatherName.setColumns(10);

                JLabel lblNewLabel_5 = new JLabel("Mobile no.: +91");
                panel_4.add(lblNewLabel_5);

                mobNo = new JTextField();
                panel_4.add(mobNo);
                mobNo.setColumns(10);

                JPanel panel_5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
                panel_1.add(panel_5);

                JLabel lblNewLabel_9 = new JLabel("Email:");
                panel_5.add(lblNewLabel_9);

                email = new JTextField();
                panel_5.add(email);
                email.setColumns(20);
                
                JPanel panel_6 = new JPanel(new FlowLayout(FlowLayout.LEFT));
                panel_6.add(new JLabel("Address:"));
                panel_1.add(panel_6);
                
                addr= new JTextArea(16,5);
                JScrollPane jsp= new JScrollPane(addr);
                panel_1.add(jsp);
                
                panel_5.add(new JLabel("Salary: INR "));
                sal= new JTextField(8);
                panel_5.add(sal);
                
                
                JPanel panel_61 = new JPanel(new FlowLayout(FlowLayout.LEFT));
                panel_1.add(panel_61);
                /*
                ButtonGroup bg= new ButtonGroup();
                JRadioButton teacherSel = new JRadioButton("Teacher");
                bg.add(teacherSel);
                panel_6.add(teacherSel);

                JRadioButton studentSel = new JRadioButton("Student");
                studentSel.setSelected(true);
                bg.add(studentSel);
                panel_6.add(studentSel);
                */

                JLabel lblNewLabel_10_1 = new JLabel("Credentials (for login):");
                panel_1.add(lblNewLabel_10_1);
                lblNewLabel_10_1.setFont(new Font("Tahoma", Font.PLAIN, 15));

                JPanel panel_7 = new JPanel();
                panel_1.add(panel_7);

                JLabel lblNewLabel_11 = new JLabel("Login ID:");
                panel_7.add(lblNewLabel_11);

                loginID = new JTextField();
                panel_7.add(loginID);
                loginID.setColumns(10);

                JLabel lblNewLabel_12 = new JLabel("Password:");
                panel_7.add(lblNewLabel_12);

                passwordField = new JPasswordField();
                passwordField.setColumns(10);
                panel_7.add(passwordField);

                JButton btnNewButton = new JButton("Register");
                btnNewButton.addActionListener(new ActionListener() {
                	public void actionPerformed(ActionEvent e) {
                		inputValidator();
                	}
                });
                panel_1.add(btnNewButton);

        }
        public boolean passwordCheck(String pwd) {
        	// Check if the password length is greater than 8
            if (pwd.length() <= 8) {
                return false;
            }

            // Regular expression to check if the password contains at least one number
            Pattern numberPattern = Pattern.compile(".*\\d.*");
            Matcher numberMatcher = numberPattern.matcher(pwd);
            if (!numberMatcher.matches()) {
                return false;
            }

            // Regular expression to check if the password contains at least one special character
            Pattern specialCharPattern = Pattern.compile(".*[!@#$%^&*(),.?\":{}|<>].*");
            Matcher specialCharMatcher = specialCharPattern.matcher(pwd);
            if (!specialCharMatcher.matches()) {
                return false;
            }

            // If all conditions are met, return true
            return true;
        }
        public void inputValidator() {
        	if(loginID.getText().length()==5) {
        		if(firstName.getText().length()!=0) {
        			if((int)age.getValue()>25 && (int)age.getValue()<100) {
        				if(teacherID.getText().length()==8) {
        					if(mobNo.getText().length()==10) {
        						if((email.getText().endsWith("@gmail.com") || email.getText().endsWith("@outlook.com") || email.getText().endsWith("@mepcoeng.ac.in")) && !email.getText().contains(" ")) {
        								if(passwordCheck(passwordField.getText())) {
        									if((int)exp.getValue()>=0) {
        										
        									}
        									else
        										JOptionPane.showMessageDialog(null, "Experience can't be negative!");
        								}
        								else
        									JOptionPane.showMessageDialog(null, "Password must:\n1) be of length more than 8 characters\n2) contain spl chars\n3) contain digits");
        						}
        						else
        							JOptionPane.showMessageDialog(null, "Invalid email");
        					}
        					else
        						JOptionPane.showMessageDialog(null, "Invalid mobile number!");
        				}
        				else
        					JOptionPane.showMessageDialog(null, "Roll no. must be 8 characters!");
        			}
        			else
        				JOptionPane.showMessageDialog(null, "Age is invalid!");
        		}
        		else
        			JOptionPane.showMessageDialog(null, "Enter name first!");
        	}
        	else
        		JOptionPane.showMessageDialog(null, "Login ID must be 5 characters!");
        }
        public void addUser() {
        	
        	try {
        		String des="student";
        		boolean loginc= userConn.executeUpdates("insert into students () values ('"+loginID.getText()+"','"+firstName.getText()+"','"+passwordField.getText()+"');");
        		boolean studentc= userdataConn.executeUpdates("insert into teacher (teacherid,exp,sal,dept,classes) values ('"+loginID.getText()+"','"+sec.getSelectedItem()+"', 0, 0.0, '"+branch.getSelectedItem()+"');");
        		boolean commonc= userdataConn.executeUpdates("insert into common (name,age,addr,phno,email,gender,type,uid) values ('"+firstName.getText()+" "+lastName.getText()+"',"+age.getValue()+",'"+addr.getText()+"','"+mobNo.getText()+"','"+email.getText()+"','"+((String) gender.getSelectedItem()).charAt(0)+"','"+des.toUpperCase().charAt(0)+"','"+loginID.getText()+"');");
        		if(loginc && studentc && commonc) {
        			JOptionPane.showMessageDialog(null, "Student "+firstName.getText()+" "+lastName.getText()+" added successfully!");
        		}
        		else {
        			JOptionPane.showMessageDialog(null, "Duplicate user!");
        		}
        	}
        	catch(Exception e) {
        		System.out.println(e.getMessage());
        	}
        }
}
