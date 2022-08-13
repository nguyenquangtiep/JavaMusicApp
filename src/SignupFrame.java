import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import javax.swing.*;
import javax.swing.border.*;

public class SignupFrame extends JFrame implements ActionListener{
	
	JPanel mainPanel;
	JLabel headingLabel, messageLabel, idLabel, passwordLabel, signupLabel, userLabel, loginLabel;
	JButton signupButton, loginButton;
	JTextField idField, usernameField;
	JPasswordField passwordField;
	String accountURL = "IDandPassword.txt";
	File file;
	FileWriter fileWr = null;
	BufferedWriter bufWr = null;
	PrintWriter printWr = null;
	Account account;
	Set<String> username;
	
	public SignupFrame(Account account) {
		this.account = account;
		this.username = account.getUsername().keySet();
		createGUI();
	}
	
	void createGUI() {
		createComponents();
		this.add(mainPanel);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setSize(500, 500);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	void createComponents() {
		mainPanel = new JPanel();
		headingLabel = new JLabel("Sportify");
		messageLabel = new JLabel("Sign up for a free Sportify account.");
		idLabel = new JLabel("ID:");
		passwordLabel = new JLabel("Password:");
		idField = new JTextField();
		passwordField = new JPasswordField();
		signupButton = new JButton("SIGN UP");
		userLabel = new JLabel("Username:");
		usernameField = new JTextField();
		loginLabel = new JLabel("Already on Sportify?");
		loginButton = new JButton("LOGIN");
		
		mainPanel.setBounds(0, 0, 500, 500);
		mainPanel.setBackground(new Color(50, 50, 50));
		mainPanel.setLayout(null);
		
		headingLabel.setFont(new Font(null, Font.BOLD, 45));
		headingLabel.setForeground(Color.white);
		headingLabel.setBounds(160, 10, 170, 50);
		messageLabel.setFont(new Font(null, Font.BOLD, 20));
		messageLabel.setForeground(Color.white);
		messageLabel.setBounds(80, 80, 350, 35);
		userLabel.setBounds(50, 150, 110, 20);
		userLabel.setForeground(Color.white);
		userLabel.setFont(new Font(null, Font.BOLD, 20));
		idLabel.setBounds(50, 200, 110, 20);
		idLabel.setForeground(Color.white);
		idLabel.setFont(new Font(null, Font.BOLD, 20));
		passwordLabel.setBounds(50, 250, 110, 20);
		passwordLabel.setForeground(Color.white);
		passwordLabel.setFont(new Font(null, Font.BOLD, 20));
		loginLabel.setBounds(100, 390, 200, 50);
		loginLabel.setForeground(Color.LIGHT_GRAY);
		loginLabel.setFont(new Font(null, Font.BOLD, 18));
		
		idField.setBounds(170, 195, 260, 30);
		idField.setFont(new Font(null, Font.BOLD, 25));
		idField.setBorder(new EmptyBorder(2, 3, 2, 3));
		passwordField.setBounds(170, 245, 260, 30);
		passwordField.setFont(new Font(null, Font.BOLD, 25));
		passwordField.setBorder(new EmptyBorder(2, 3, 2, 3));
		usernameField.setBounds(170, 145, 260, 30);
		usernameField.setFont(new Font(null, Font.BOLD, 25));
		usernameField.setBorder(new EmptyBorder(2, 3, 2, 3));
		
		signupButton.setBounds(100, 320, 300, 50);
		signupButton.setFocusable(false);
		signupButton.setBorder(null);
		signupButton.setFont(new Font(null, Font.BOLD, 30));
		signupButton.addActionListener(this);
		loginButton.setBounds(300, 400, 100, 30);
		loginButton.setFocusable(false);
		loginButton.setBorder(null);
		loginButton.setFont(new Font(null, Font.BOLD, 20));
		loginButton.addActionListener(this);
		
		mainPanel.add(headingLabel);
		mainPanel.add(messageLabel);
		mainPanel.add(idLabel);
		mainPanel.add(passwordLabel);
		mainPanel.add(idField);
		mainPanel.add(passwordField);
		mainPanel.add(signupButton);
		mainPanel.add(usernameField);
		mainPanel.add(userLabel);
		mainPanel.add(loginLabel);
		mainPanel.add(loginButton);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		if(e.getSource() == signupButton) {
			String username = usernameField.getText();
			String id = idField.getText();
			String password = String.valueOf(passwordField.getPassword());
			if(username.contains(" ")) {
				JOptionPane.showMessageDialog(null, "Name cannot contain spaces", "Invalid", JOptionPane.WARNING_MESSAGE);
			} else if(id.contains(" ")) {
				JOptionPane.showMessageDialog(null, "ID cannot contain spaces", "Invalid", JOptionPane.WARNING_MESSAGE);
			} else if(password.contains(" ")) {
				JOptionPane.showMessageDialog(null, "Password cannot contain spaces", "Invalid", JOptionPane.WARNING_MESSAGE);
			} else {
				if(username.isEmpty() || id.isEmpty() || password.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Please fill in all the information !", "Invalid", JOptionPane.WARNING_MESSAGE);
				} else {
					if(this.username.contains(username)) {
						JOptionPane.showMessageDialog(null, "Account already exists.", "Invalid", JOptionPane.ERROR_MESSAGE);
					} else {
						file = new File(accountURL);
						try {
							fileWr = new FileWriter(file, true);
							bufWr = new BufferedWriter(fileWr);
							printWr = new PrintWriter(bufWr);
							printWr.println("");
							printWr.print(id+" "+password+" "+username);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} finally {
							printWr.close();
						}
						JOptionPane.showMessageDialog(null, "Account created successfully !", "Done", JOptionPane.INFORMATION_MESSAGE);
						this.setVisible(false);
						new LoginFrame(new Account());
					}
				}
			}
		}
		
		if(e.getSource() == loginButton) {
			this.setVisible(false);
			new LoginFrame(new Account());
		}
	}
}
