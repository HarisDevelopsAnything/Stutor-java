package stutor;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JLabel;
import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import java.awt.GridLayout;
import javax.swing.JComboBox;
import java.awt.Font;
import java.awt.*;
import javax.swing.*;

public class UpdateStudent extends JFrame {

        private static final long serialVersionUID = 1L;
        private JPanel contentPane;
        private JTextField firstName;
        private JTextField lastName;
        private JTextField rollNo;
        private JTextField mobNo;
        private JTextField fatherName;
        private JTextField email;
        private JTextField loginID;
        private JTextField passwordField;
        static JComboBox<String> branch;
        static  JComboBox<String> gender;
        static JSpinner age;
        static JComboBox<String> sec;
        static JTextArea addr;
        //servers
    	static ServerConnector userdataConn= null;
    	static ServerConnector userConn= null;
        /**
         * Launch the application.
         */
        public static void main(String[] args) {
        		  //default to normal login db
                EventQueue.invokeLater(new Runnable() {
                        public void run() {
                                try {
                                        UpdateStudent frame = new UpdateStudent();
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
        public UpdateStudent() {
        	userdataConn= new ServerConnector("user_data");
    		userConn= new ServerConnector("users");
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                setBounds(100, 100, 600, 450);
                contentPane = new JPanel();
                contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
                contentPane.setFont(new Font("Tahoma", Font.PLAIN, 30));
                setContentPane(contentPane);
                contentPane.setLayout(new BorderLayout(0, 0));

                JPanel panel = new JPanel();
                panel.setBackground(SystemColor.desktop);
                contentPane.add(panel, BorderLayout.NORTH);

                JLabel lblCreateNewStutor = new JLabel("Update details of existing Stutor user");
                lblCreateNewStutor.setFont(new Font("Product Sans", Font.PLAIN, 10));
                lblCreateNewStutor.setForeground(new Color(255, 255, 255));
                panel.add(lblCreateNewStutor);

                JPanel panel_1 = new JPanel();
                contentPane.add(panel_1, BorderLayout.CENTER);
                panel_1.setLayout(new GridLayout(10, 2, 0, 0));

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

                JLabel lblNewLabel_3 = new JLabel("Dept.:");
                panel_3.add(lblNewLabel_3);

                branch = new JComboBox<String>();
                branch.addItem("CSE");
                branch.addItem("ECE");
                branch.addItem("EEE");
                branch.setSelectedIndex(0);
                panel_3.add(branch);

                JLabel lblNewLabel_3_1 = new JLabel("Section:");
                panel_3.add(lblNewLabel_3_1);

                sec = new JComboBox<String>();
                sec.addItem("A");
                sec.addItem("B");
                sec.addItem("C");
                sec.setSelectedIndex(0);
                panel_3.add(sec);

                

                JLabel lblNewLabel_4 = new JLabel("Roll no.:");
                panel_3.add(lblNewLabel_4);

                rollNo = new JTextField();
                panel_3.add(rollNo);
                rollNo.setColumns(10);

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
                

                JLabel lblNewLabel_10_1 = new JLabel("Enter the ID to update:");
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

                passwordField = new JTextField();
                passwordField.setColumns(10);
                panel_7.add(passwordField);
                
                JPanel panel_8= new JPanel(new GridLayout(1,2));
                JButton btnNewButton = new JButton("Fetch details");
                btnNewButton.addActionListener(new ActionListener() {
                	public void actionPerformed(ActionEvent e) {
                		getCurrentDetails();
                	}
                });
                panel_8.add(btnNewButton);
                JButton btnNewButton1 = new JButton("Update details");
                btnNewButton1.addActionListener(new ActionListener() {
                	public void actionPerformed(ActionEvent e) {
                		setCurrentDetails();
                		enableAllComponents();
                	}
                });
                panel_8.add(btnNewButton1);
                panel_1.add(panel_8);
                disableAllComponents();

        }
        public void disableAllComponents() {
            // Disable JTextFields
            firstName.setEnabled(false);
            lastName.setEnabled(false);
            rollNo.setEnabled(false);
            mobNo.setEnabled(false);
            fatherName.setEnabled(false);
            email.setEnabled(false);

            // Disable JPasswordField
            passwordField.setEnabled(false);

            // Disable JComboBoxes
            branch.setEnabled(false);
            gender.setEnabled(false);
            sec.setEnabled(false);

            // Disable JSpinner
            age.setEnabled(false);

            // Disable JTextArea
            addr.setEnabled(false);
        }
        public void enableAllComponents() {
            // Enable JTextFields
            firstName.setEnabled(true);
            lastName.setEnabled(true);
            rollNo.setEnabled(true);
            mobNo.setEnabled(true);
            fatherName.setEnabled(true);
            email.setEnabled(true);
            loginID.setEnabled(true);

            // Enable JPasswordField
            passwordField.setEnabled(true);

            // Enable JComboBoxes
            branch.setEnabled(true);
            gender.setEnabled(true);
            sec.setEnabled(true);

            // Enable JSpinner
            age.setEnabled(true);

            // Enable JTextArea
            addr.setEnabled(true);
        }
        public void getCurrentDetails() {
        	if(loginID.getText().length()==5)
        	{
        	try {
        		ResultSet rs1= userConn.executeQuery("SELECT * from students WHERE id="+loginID.getText());
        		ResultSet rs2= userdataConn.executeQuery("SELECT * from student WHERE rollno="+loginID.getText());
        		ResultSet rs3= userdataConn.executeQuery("SELECT * from common WHERE uid="+loginID.getText());
        		if(rs1.next()) {
        			passwordField.setText(rs1.getString("password"));
        		}
        		else {
        			JOptionPane.showMessageDialog(null, "Student does not exist!\n If the ID was of a teacher, use Update Teacher feature to update their details!");
        			return;
        		}
        		if(rs2.next()) {
        			rollNo.setText(rs2.getString("rollno"));
        			sec.setSelectedItem(rs2.getString("class"));
        			branch.setSelectedItem(rs2.getString("dept"));
        		}
        		if(rs3.next()) {
        			String name[]= new String[2];
        			Arrays.fill(name, "");
        			int splitPoint = rs3.getString("name").indexOf(" ");
        			if(splitPoint==-1) {
        				name[0]= rs3.getString("name");
        				name[1]= "";
        			}
        			else {
        				name[0]= rs3.getString("name").substring(0,splitPoint);
        				name[1]= rs3.getString("name").substring(splitPoint+1);
        			}
        			fatherName.setText(rs3.getString("father"));
        			firstName.setText(name[0]);
        			lastName.setText(name[1]);
        			addr.setText(rs3.getString("addr"));
        			age.setValue(Integer.parseInt(rs3.getString("age")));
        			
        			mobNo.setText(rs3.getString("phno"));
        			email.setText(rs3.getString("email"));
        			if(rs3.getString("gender").equals("M"))
        				gender.setSelectedItem("Male");
        			else
        				gender.setSelectedItem("Female");
        			enableAllComponents();
        		}
        	}
        	catch(Exception e) {
        		JOptionPane.showMessageDialog(null, "Error fetching user details!");
        		e.printStackTrace();
        		disableAllComponents();
        	}
        	}
        	else
        		JOptionPane.showMessageDialog(null, "Invalid login ID!");
        }
        public void setCurrentDetails() {
        	inputValidator();
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
        			if((int)age.getValue()>16 && (int)age.getValue()<100) {
        				if(rollNo.getText().length()==5) {
        					if(mobNo.getText().length()==10) {
        						if((email.getText().endsWith("@gmail.com") || email.getText().endsWith("@outlook.com") || email.getText().endsWith("@mepcoeng.ac.in")) && !email.getText().contains(" ")) {
        								if(passwordCheck(passwordField.getText())) {
        									updateUser();
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
        					JOptionPane.showMessageDialog(null, "Roll no. must be 5 characters!");
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
        public void updateUser() {
            try {
                String des = "student";
                
                // Update the students table with the new values
                boolean loginc = userConn.executeUpdates("UPDATE students SET username = '"+firstName.getText()+"', password = '"+passwordField.getText()+"' WHERE id = '"+loginID.getText()+"';");
                
                // Update the student table with the new values
                boolean studentc = userdataConn.executeUpdates("UPDATE student SET class = '"+sec.getSelectedItem()+"', crank = 0, cgpa = 0.0, dept = '"+branch.getSelectedItem()+"' WHERE rollno = '"+loginID.getText()+"';");
                
                // Update the common table with the new values
                boolean commonc = userdataConn.executeUpdates("UPDATE common SET name = '"+firstName.getText()+" "+lastName.getText()+"', age = "+age.getValue()+", addr = '"+addr.getText()+"', phno = '"+mobNo.getText()+"', email = '"+email.getText()+"', gender = '"+((String) gender.getSelectedItem()).charAt(0)+"', type = '"+des.toUpperCase().charAt(0)+"', father='"+fatherName.getText()+"' WHERE uid = '"+loginID.getText()+"';");

                // If all updates were successful
                if(loginc && studentc && commonc) {
                    JOptionPane.showMessageDialog(null, "Student "+firstName.getText()+" "+lastName.getText()+" updated successfully!");
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to update user! Please check if the user exists.");
                }
            } catch(Exception e) {
                System.out.println(e.getMessage());
            }
        }

}
