import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class LoginFrame extends JFrame implements ActionListener{
	
	JPanel mainPanel;
	JLabel headingLabel, messageLabel, idLabel, passwordLabel, signupLabel;
	JTextField idField;
	JPasswordField passwordField;
	JButton loginButton, resetButton, signupButton;
	HashMap<String, String> loginInfo;
	HashMap<String, String> username;
	Account account;
	
	public LoginFrame(Account account) {
		this.loginInfo = account.getLoginInfo();
		this.username = account.getUsername();
		this.account = account;
		createGUI();
	}
	
	void createGUI() {
		createComponents();
		this.add(mainPanel);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setSize(500, 450);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	void createComponents() {
		mainPanel = new JPanel();
		headingLabel = new JLabel("Sportify");
		messageLabel = new JLabel("Log in to continue");
		idLabel = new JLabel("ID:");
		passwordLabel = new JLabel("Password:");
		idField = new JTextField();
		passwordField = new JPasswordField();
		loginButton = new JButton("LOG IN");
		resetButton = new JButton("Reset Password");
		signupLabel = new JLabel("Don't have an account?");
		signupButton = new JButton("SIGNUP");
		
		mainPanel.setBounds(0, 0, 500, 450);
		mainPanel.setBackground(new Color(50, 50, 50));
		mainPanel.setLayout(null);
		
		headingLabel.setFont(new Font(null, Font.BOLD, 45));
		headingLabel.setForeground(Color.white);
		headingLabel.setBounds(160, 10, 170, 50);
		messageLabel.setFont(new Font(null, Font.BOLD, 30));
		messageLabel.setForeground(Color.white);
		messageLabel.setBounds(115, 80, 260, 35);
		idLabel.setBounds(50, 150, 110, 20);
		idLabel.setForeground(Color.white);
		idLabel.setFont(new Font(null, Font.BOLD, 20));
		passwordLabel.setBounds(50, 200, 110, 20);
		passwordLabel.setForeground(Color.white);
		passwordLabel.setFont(new Font(null, Font.BOLD, 20));
		signupLabel.setBounds(100, 350, 250, 50);
		signupLabel.setForeground(Color.LIGHT_GRAY);
		signupLabel.setFont(new Font(null, Font.BOLD, 17));
		
		idField.setBounds(170, 145, 260, 30);
		idField.setFont(new Font(null, Font.BOLD, 25));
		idField.setBorder(new EmptyBorder(2, 3, 2, 3));
		passwordField.setBounds(170, 195, 260, 30);
		passwordField.setFont(new Font(null, Font.BOLD, 25));
		passwordField.setBorder(new EmptyBorder(2, 3, 2, 3));
		
		loginButton.setBounds(100, 280, 300, 50);
		loginButton.setFocusable(false);
		loginButton.setBorder(null);
		loginButton.setFont(new Font(null, Font.BOLD, 30));
		loginButton.addActionListener(this);
		resetButton.setBounds(300, 235, 130, 25);
		resetButton.setFocusable(false);
		resetButton.setBorder(null);
		resetButton.setFont(new Font(null, Font.BOLD, 15));
		resetButton.addActionListener(this);
		signupButton.setBounds(300, 360, 100, 30);
		signupButton.setFocusable(false);
		signupButton.setBorder(null);
		signupButton.setFont(new Font(null, Font.BOLD, 20));
		signupButton.addActionListener(this);
		
		mainPanel.add(headingLabel);
		mainPanel.add(messageLabel);
		mainPanel.add(idLabel);
		mainPanel.add(passwordLabel);
		mainPanel.add(signupLabel);
		mainPanel.add(idField);
		mainPanel.add(passwordField);
		mainPanel.add(loginButton);
		mainPanel.add(resetButton);
		mainPanel.add(signupButton);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		if(e.getSource() == resetButton) {
			passwordField.setText("");
		}
		
		if(e.getSource() == loginButton) {
			String id = idField.getText();
			String password = String.valueOf(passwordField.getPassword());
			if(loginInfo.containsKey(id)) {
				if(loginInfo.get(id).equals(password)) {
					this.setVisible(false);
					new MainFrame(new SharingData(), username.get(id));
				} else {
					JOptionPane.showMessageDialog(null, "Password is incorrect.", "Wrong", JOptionPane.ERROR_MESSAGE);
					passwordField.setText("");
				}
			} else {
				JOptionPane.showMessageDialog(null, "ID is incorrect.", "Wrong", JOptionPane.ERROR_MESSAGE);
			}
		}
		
		if(e.getSource() == signupButton) {
			this.setVisible(false);
			new SignupFrame(account);
		}
	}
}
