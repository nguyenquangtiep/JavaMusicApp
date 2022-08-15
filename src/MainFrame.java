import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MainFrame extends JFrame implements ActionListener{
	final int width = 1280;
	final int height = 720;
	SharingData data;
	FileInputStream fis;
	InputStreamReader reader;
	BufferedReader bReader;
	String name, songName, singerName;
	JButton button;
	JPanel left, top;
	BotPanel bot;
	JLayeredPane myPane;
	RightPanel searchPanel, homePanel, libraryPanel;
	JButton homeButton, searchButton, libraryButton, backButton, loginButton;
	Stack<JPanel> panelStack = new Stack<JPanel>();
	TreeSet<String> likedList = new TreeSet<String>();
	TreeMap<String, File> fileList;
	TreeSet<String> nameList;
	JPanel previous;
	JLabel label;
	String id;
	Boolean checkLikedList = false;
	JMenuBar bar;
	JMenu accountMenu;
	JMenuItem logoutItem;
	JMenuItem profileItem;
	File file;
	BufferedWriter bWriter;

	public MainFrame(SharingData data, String id) {
		this.data = data;
		this.nameList = data.getNameList();
		this.fileList = data.getFileList();
		this.id = id;
		createGUI();
		inputLikedList();
	}
	
	// tao frame
	void createGUI(){
		
		createComponents();
		
		left = createLeftPanel(); // tao panel ben trai
		left.add(homeButton);
		left.add(searchButton);
		left.add(libraryButton);
		
		bot = new BotPanel(this, data);
		bot.setLikedList(likedList);
		bot.setFullList(nameList);
		
		myPane = new JLayeredPane(); // JLayeredPane de sap xep thu tu cac panel
		myPane.setBounds(0, 0, width, height);
		myPane.add(left);
		myPane.add(bot, JLayeredPane.POPUP_LAYER);
		
		// tao panel ban dau
		RightPanel beginPanel = new HomePanel(this);
		beginPanel.addButton(createButtonList());
		myPane.add(beginPanel);
		panelStack.push(beginPanel); // them phan tu mac dinh vao stack
		
		accountMenu.add(profileItem);
		accountMenu.add(logoutItem);
		bar.add(accountMenu);
		
		this.add(bar);
		this.add(backButton);
		this.add(myPane);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(width, height);
		this.setLayout(null);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setTitle("Sportify");
		this.setVisible(true);
		this.addWindowListener(new WindowAdapter() {
			
		    public void windowClosing(WindowEvent e) {
		        storeData();
		    }
		    
		});
	}

	// tao cac components
	void createComponents() {
		bar = new JMenuBar();
		bar.setBorder(null);
		bar.setBounds(width*3/4, 10, 150, 30);
		
		accountMenu = new JMenu();
		accountMenu.setText(id);
		accountMenu.setFont(new Font(null, Font.BOLD, 25));
		accountMenu.setBackground(Color.white);
		accountMenu.setPreferredSize(new Dimension(150, 30));
		
		logoutItem = new JMenuItem("Log out");
		logoutItem.setPreferredSize(new Dimension(170, 30));
		logoutItem.setFont(new Font(null, Font.BOLD, 15));
		logoutItem.addActionListener(this);
		
		profileItem = new JMenuItem("Profile");
		profileItem.setPreferredSize(new Dimension(170, 30));
		profileItem.setFont(new Font(null, Font.BOLD, 15));
		profileItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JOptionPane.showMessageDialog(null, "This function has not been completed yet.", "Working...", JOptionPane.INFORMATION_MESSAGE);
			}
		
		});
		
		// nut tro lai
		backButton = new JButton("Back");
		backButton.setBounds(width/4, 10, 100, 25);
		backButton.setForeground(new Color(105, 105, 105));
		backButton.setBackground(new Color(77, 77, 77));
		backButton.setFocusable(false);
		backButton.setEnabled(false);
		backButton.setBorderPainted(false);
		backButton.addActionListener(this);
		
		// nut home
		homeButton = new JButton("Home");
		homeButton.setBounds(0, 25, width/5, 50);
		homeButton.setFont(new Font(null, Font.ITALIC, 30));
		homeButton.setForeground(Color.white);
		homeButton.setBackground(Color.black);
		homeButton.setFocusable(false);
		homeButton.setBorderPainted(false);
		homeButton.addActionListener(this);

		// nut tim kiem
		searchButton = new JButton("Search");
		searchButton.setBounds(0, 75, width/5, 50);
		searchButton.setFont(new Font(null, Font.ITALIC, 30));
		searchButton.setForeground(new Color(105, 105, 105));
		searchButton.setBackground(Color.black);
		searchButton.setFocusable(false);
		searchButton.setBorderPainted(false);
		searchButton.addActionListener(this);

		// nut thu vien
		libraryButton = new JButton("Your Library");
		libraryButton.setBounds(0, 125, width/5, 50);
		libraryButton.setFont(new Font(null, Font.ITALIC, 30));
		libraryButton.setForeground(new Color(105, 105, 105));
		libraryButton.setBackground(Color.black);
		libraryButton.setFocusable(false);
		libraryButton.setBorderPainted(false);
		libraryButton.addActionListener(this);
	}

	void inputLikedList() {
		file = new File("LikedSongs\\"+id+".txt");
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			bReader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8));
			String line;
			while((line = bReader.readLine()) != null) {
				if(line.isEmpty()) continue;
				likedList.add(line);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				bReader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	// tao danh sach cac nut la cac bai hat
	public TreeMap<String, JButton> createButtonList() {
		TreeMap<String, JButton> bList = new TreeMap<String, JButton>();
		for(String name: nameList) {
			label = new JLabel(name, JLabel.CENTER);
			label.setFont(new Font(null, Font.CENTER_BASELINE, 25));
			label.setForeground(Color.white);
			button = new JButton();
			button.setPreferredSize(new Dimension(200, 300));
			button.setBackground(Color.DARK_GRAY);
			button.setLayout(new GridLayout(3, 1));
			button.setFocusable(false);
			button.add(label);
			button.setActionCommand(name);
			button.addActionListener(this);
			bList.put(name, button);
		}
		return bList;
	}
	
	@Override
	public int getWidth() {
		return width;
	}
 
	@Override
	public int getHeight() {
		return height;
	}

	// tao panel ben trai
	JPanel createLeftPanel() {
		JPanel left = new JPanel();
		left.setBounds(0, 0, width/5, height);
		left.setBackground(new Color(0, 0, 0));
		left.setLayout(null);
		return left;
	}
	
	void storeData() {
		file = new File("LikedSongs\\"+id+".txt");
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		try {
			bWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
			for(String name: likedList) {
				bWriter.write(name+"\n");
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			try {
				if(bWriter != null) {
					bWriter.close();
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		// xu ly khi an vao nut home
		if(e.getSource() == homeButton) {
			// set mau chu moi khi an
			homeButton.setForeground(Color.white);
			searchButton.setForeground(new Color(105, 105, 105));
			libraryButton.setForeground(new Color(105, 105, 105));
			// previous = phan tu cuoi cung cua stack
			previous = panelStack.peek();
			if(previous.getName() != "Home") {
				myPane.remove(previous);
				homePanel = new HomePanel(this);
				homePanel.addButton(createButtonList());
				panelStack.push(homePanel);
				myPane.add(homePanel);
				myPane.repaint(); // ve lai sau khi add
			}
			libraryButton.setSelected(false);
		}
		
		// xu ly khi an vao nut search
		if(e.getSource() == searchButton) {
			// set mau chu khi an
			homeButton.setForeground(new Color(105, 105, 105));
			searchButton.setForeground(Color.white);
			libraryButton.setForeground(new Color(105, 105, 105));
			// previous = phan tu cuoi cua stack
			previous = panelStack.peek();
			if(previous.getName() != "Search") {
				myPane.remove(previous);
				searchPanel = new SearchPanel(this);
				searchPanel.addButton(createButtonList(), true);
				panelStack.push(searchPanel);
				myPane.add(searchPanel);
				myPane.repaint();
			}
			libraryButton.setSelected(false);
		}
		
		// xu ly khi an nut library
		if(e.getSource() == libraryButton) {
			//set mau chu cac nut
			homeButton.setForeground(new Color(105, 105, 105));
			searchButton.setForeground(new Color(105, 105, 105));
			libraryButton.setForeground(Color.white);
			// previous = phan tu cuoi cua stack
			previous = panelStack.peek();
			if(previous.getName() != "Library") {
				myPane.remove(previous);
				libraryPanel = new LibraryPanel(this);
				libraryPanel.addButton(createButtonList(), likedList);
				panelStack.push(libraryPanel);
				myPane.add(libraryPanel);
				myPane.repaint();
			}
			libraryButton.setSelected(true);
		}
		
		// xu ly khi an nut quay lai
		if(e.getSource() == backButton) {
			previous = panelStack.pop();
			myPane.remove(previous); // xoa panel dang hien thi
			previous = panelStack.peek(); // lay panel ngay truoc do
			myPane.add(previous); // them vao pane
			// set mau chu cac nut khi an back
			switch(previous.getName()) {
				case "Home":
					homeButton.setForeground(Color.white);
					searchButton.setForeground(new Color(105, 105, 105));
					libraryButton.setForeground(new Color(105, 105, 105));
					break;
				case "Search":
					homeButton.setForeground(new Color(105, 105, 105));
					searchButton.setForeground(Color.white);
					libraryButton.setForeground(new Color(105, 105, 105));
					break;
				case "Library":
					homeButton.setForeground(new Color(105, 105, 105));
					searchButton.setForeground(new Color(105, 105, 105));
					libraryButton.setForeground(Color.white);
					break;
				default:
					break;
			}
			myPane.repaint();
		}
		
		// kiem tra neu stack chi con panel ban dau thi khong the an nut quay lai
		if(panelStack.size() == 1) {
			backButton.setEnabled(false);
			backButton.setForeground(new Color(105, 105, 105));
		} else {
			backButton.setEnabled(true);
			backButton.setForeground(Color.white);
		}
		
		// lay ra command tu cac button khi an trong danh sach cac bai hat
		if(nameList.contains(e.getActionCommand())) {
			
			if(e.getActionCommand() != name) {
				if(libraryButton.isSelected()) {
					bot.setPlayInLikedList(true);
					bot.playSong(e.getActionCommand());
				} else {
					bot.setPlayInLikedList(false);
					bot.playSong(e.getActionCommand());
				}
			}
		}
		
		// luu lai danh sach thich khi thoat
		if(e.getSource() == logoutItem) {
			storeData();
			bot.backToInit();
			this.setVisible(false);
			new LoginFrame(new Account());
		}
	}
	
}
