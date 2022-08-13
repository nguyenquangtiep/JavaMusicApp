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
	JPanel left, top, bot;
	JLayeredPane myPane;
	HomePanel homePanel;
	SearchPanel searchPanel;
	LibraryPanel libraryPanel;
	JButton homeButton, searchButton, libraryButton, backButton, loginButton;
	Stack<JPanel> panelStack = new Stack<JPanel>();
	TreeSet<String> likedList = new TreeSet<String>();
	TreeMap<String, File> fileList;
	TreeSet<String> nameList;
	JPanel previous;
	Clip clip;
	AudioInputStream audioStream;
	JButton loopButton, playButton, nextButton, previousButton;
	JSlider slider;
	Timer timer;
	JLabel totalTime, currentTime, label;
	String id;
	Boolean checkLikedList = false;
	JMenuBar accountBar;

	public MainFrame(SharingData data, String id) {
		this.data = data;
		this.nameList = data.getNameList();
		this.fileList = data.getFileList();
		this.id = id;
		createGUI();
	}
	
	// tao frame
	void createGUI(){
		
		createComponents();
		
		left = createLeftPanel(); // tao panel ben trai
		left.add(homeButton);
		left.add(searchButton);
		left.add(libraryButton);
		
		bot = createBotPanel(null); // tao panel phia duoi
		
		myPane = new JLayeredPane(); // JLayeredPane de sap xep thu tu cac panel
		myPane.setBounds(0, 0, width, height);
		myPane.add(left);
		myPane.add(bot, JLayeredPane.POPUP_LAYER);
		
		HomePanel beginPanel = new HomePanel(this, data, createButtonList()); // tao panel ban dau
		myPane.add(beginPanel);
		
		panelStack.push(beginPanel); // them phan tu mac dinh vao stack
		
		this.add(backButton);
		this.add(myPane);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(width, height);
		this.setLayout(null);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setTitle("Sportify");
		this.setVisible(true);
	}

	// tao cac components
	void createComponents() {
		
		accountBar = new JMenuBar();
		
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
		
		// nut play/stop nhac
		playButton = new JButton("Play");
		playButton.setBackground(new Color(77, 77, 77));
		playButton.setForeground(Color.white);
		playButton.setFocusable(false);
		playButton.setBounds(width/2, 0, 125, 25);
		playButton.addActionListener(this);
		
		// nut lap khi phat nhac
		loopButton = new JButton("Loop");
		loopButton.setBounds(width/2+260, 0, 125, 25);
		loopButton.setBackground(new Color(77, 77, 77));
		loopButton.setForeground(Color.white);
		loopButton.setFocusable(false);
		loopButton.addActionListener(this);
		
		// nut chuyen bai tiep theo
		nextButton = new JButton("Next");
		nextButton.setBackground(new Color(77, 77, 77));
		nextButton.setForeground(Color.white);
		nextButton.setFocusable(false);
		nextButton.setBounds(width/2+130, 0, 125, 25);
		nextButton.addActionListener(this);
		
		// nut chuyen bai truoc do
		previousButton = new JButton("Previous");
		previousButton.setBackground(new Color(77, 77, 77));
		previousButton.setForeground(Color.white);
		previousButton.setFocusable(false);
		previousButton.setBounds(width/2-130, 0, 125, 25);
		previousButton.addActionListener(this);
		
		// thanh truot khi audio chay
		slider = new JSlider();
		slider.setBounds(width/2-80, 30, width/3, 50);
		slider.setBackground(new Color(128, 128, 128));
		slider.setFont(new Font(null, Font.BOLD, 15));
		slider.setForeground(Color.white);
		
		// hien thi thoi gian phat 1 bai hat duoc chon
		totalTime = new JLabel("--:--");
		totalTime.setBounds(width/2+350, 35, 100, 30);
		totalTime.setFont(new Font(null, Font.BOLD, 20));
		
		// hien thi thoi gian hien tai khi phat audio
		currentTime = new JLabel("--:--");
		currentTime.setBounds(width/2-130, 35, 100, 30);
		currentTime.setFont(new Font(null, Font.BOLD, 20));
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
			button.setBorderPainted(false);
			button.setLayout(new GridLayout(3, 1));
			button.setFocusable(false);
			button.add(label);
			button.setActionCommand(name);
			button.addActionListener(this);
			bList.put(name, button);
		}
		return bList;
	}
	
	public int getWidth() {
		return width;
	}
 
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
	
	// tao panel phia duoi
	JPanel createBotPanel(String name) {
		JPanel botPanel = new JPanel();
		botPanel.setLayout(null);
		JButton likeButton = new JButton(); // tao 1 nut like tuong ung
		this.name = name;
		if(name != null) {
			slider.setEnabled(true);
			
			likeButton.setBackground(new Color(77, 77, 77));
			likeButton.setFont(new Font(null, Font.BOLD, 30));
			likeButton.setForeground(Color.white);
			likeButton.setFocusable(false);
			likeButton.setBounds(width-150, 5, 125, height/10);
			likeButton.setEnabled(true);
			likeButton.addActionListener(this);
			
			String[] tokens = name.split("-");
			songName = tokens[0].trim();
			singerName = tokens[1].trim();
			
			JLabel songLabel = new JLabel(songName); // label hien thi ten bai hat
			songLabel.setFont(new Font(null, Font.BOLD, 20));
			songLabel.setForeground(Color.white);
			songLabel.setBounds(100, 10, width/3, 25);
			
			JLabel singerLabel = new JLabel(singerName); // label hien thi nguoi hat
			singerLabel.setFont(new Font(null, Font.BOLD, 15));
			singerLabel.setForeground(Color.white);
			singerLabel.setBounds(100, 45, width/3, 20);
			
			// set cac button enable
			playButton.setEnabled(true);
			loopButton.setEnabled(true);
			previousButton.setEnabled(true);
			nextButton.setEnabled(true);
			likeButton.setEnabled(true);
			
			// chay clip khi tao bot panel
			try {
				audioStream = AudioSystem.getAudioInputStream(fileList.get(name));
				clip = AudioSystem.getClip();
				clip.open(audioStream);
				clip.start();
			} catch (LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedAudioFileException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			playButton.setText("Stop");
			
			if(likedList.contains(name)) {
				likeButton.setText("Liked");
			} else {
				likeButton.setText("Like");
			}
			
			likeButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					if(likedList.contains(name)) {
						likeButton.setText("Like");
						likedList.remove(name);
					} else {
						likeButton.setText("Liked");
						likedList.add(name);
					}
				}
				
			});
			int timeTotal = (int) (clip.getMicrosecondLength()/1000000);
			int minuteTotal, secondsTotal;
			minuteTotal = timeTotal / 60; // tinh so phut
			secondsTotal = timeTotal - minuteTotal*60; // tinh so giay con lai
			
			slider.setMinimum(0);
			slider.setMaximum((int) (clip.getMicrosecondLength() - clip.getMicrosecondLength()%1000000));
			
			// tao rieng 1 changelistener
			ChangeListener changeListener = new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					// TODO Auto-generated method stub
					clip.setMicrosecondPosition(((JSlider)e.getSource()).getValue()); // set vi tri phat khi keo slider
				}
				
			};
			slider.setEnabled(true);
			slider.addMouseListener(new MouseListener() {

				@Override
				public void mouseClicked(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mousePressed(MouseEvent e) {
					// TODO Auto-generated method stub
					slider.addChangeListener(changeListener); // add change listener khi an giu thanh slider
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub
					slider.removeChangeListener(changeListener); // remove change listener khi tha chuot
				}

				@Override
				public void mouseEntered(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				
			});
			
			// hien thi thoi gian cua bai hat
			if(minuteTotal < 10) {
				if(secondsTotal < 10) {
					totalTime.setText("0"+minuteTotal+":0"+secondsTotal);
				} else {
					totalTime.setText("0"+minuteTotal+":"+secondsTotal);
				}
			} else {
				if(secondsTotal < 10) {
					totalTime.setText(""+minuteTotal+":0"+secondsTotal);
				} else {
					totalTime.setText(""+minuteTotal+":"+secondsTotal);
				}
			}
			
			currentTime.setText("00:00");
			// moi 1 giay chay timer 1 lan
			timer = new Timer(1000, new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					
					int timeCurrent = (int) ((int) (clip.getMicrosecondPosition()/1000000.0) + ((clip.getMicrosecondPosition()%1000000 >= 500000) ? 1 : 0));
					int minuteCurrent, secondsCurrent;
					
					slider.setValue(timeCurrent*1000000);
					
					// hien thi thoi gian dang chay
					minuteCurrent = timeCurrent / 60;
					secondsCurrent = timeCurrent % 60;
					if(minuteCurrent < 10) {
						if(secondsCurrent < 10) {
							currentTime.setText("0"+minuteCurrent+":0"+secondsCurrent);
						} else {
							currentTime.setText("0"+minuteCurrent+":"+secondsCurrent);
						}
					} else {
						if(secondsCurrent < 10) {
							currentTime.setText(""+minuteCurrent+":0"+secondsCurrent);
						} else {
							currentTime.setText(""+minuteCurrent+":"+secondsCurrent);
						}
					}
					
					if(slider.getValue() == slider.getMaximum()) {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						if(loopButton.getText() == "Cancel Loop") {
							if(clip.isRunning() == false) {
								clip.start();
								clip.setMicrosecondPosition(0);
								currentTime.setText("00:00");
								slider.setValue(0);
								timer.restart();
							}
						} else {
							myPane.remove(bot);
							bot = createBotPanel(null);
							myPane.add(bot, JLayeredPane.POPUP_LAYER);
							myPane.repaint();
							playButton.setText("Play");
							slider.setValue(0);
							currentTime.setText("--:--");
							timer.stop();
						}
					}
					
				}
				
				
			});
			timer.start();
			
			botPanel.add(songLabel);
			botPanel.add(singerLabel);
		} else {
			totalTime.setText("--:--");
			currentTime.setText("--:--");
			slider.setValue(0);
			slider.setEnabled(false);
			previousButton.setEnabled(false);
			nextButton.setEnabled(false);
			loopButton.setEnabled(false);
			playButton.setEnabled(false);
		}
		
		// add cac components
		botPanel.add(currentTime);
		botPanel.add(slider);
		botPanel.add(loopButton);
		botPanel.add(playButton);
		botPanel.add(nextButton);
		botPanel.add(previousButton);
		botPanel.add(totalTime);
		botPanel.add(likeButton);
		
		// tao GUI
		botPanel.setBounds(0, height*5/6, width, height/6);
		botPanel.setBackground(new Color(128, 128, 128));
		botPanel.setLayout(null);
		return botPanel;
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
				homePanel = new HomePanel(this, data, createButtonList());
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
				searchPanel = new SearchPanel(this, data, createButtonList());
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
				libraryPanel = new LibraryPanel(this, data, likedList, createButtonList());
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
			if(timer != null) {
				timer.stop(); // dung timer neu co timer dang chay
			}
			if(e.getActionCommand() != name) {
				myPane.remove(bot); // xoa bot panel cu di
				if(clip != null) {
					clip.close(); // dong clip khi co clip dang chay
				}
				bot = createBotPanel(e.getActionCommand()); // tao bot moi
				myPane.add(bot, JLayeredPane.POPUP_LAYER); // them vao pane
				myPane.repaint();
			} else {
				// neu chon lai bai dang phat thi no se phat lai tu dau
				slider.setValue(0);
				clip.setMicrosecondPosition(0);
				timer.restart();
			}
			if(libraryButton.isSelected()) {
				checkLikedList = true;
			} else {
				checkLikedList = false;
			}
		}
		
		// set text khi an nut play
		if(e.getSource() == playButton) {
			if(playButton.getText() == "Stop") {
				playButton.setText("Play");
			} else {
				playButton.setText("Stop");
			}
		}
		
		// set text khi an nut loop
		if(e.getSource() == loopButton) {
			if(loopButton.getText() == "Loop") {
				loopButton.setText("Cancel Loop");
			} else {
				loopButton.setText("Loop");
			}
		}
		
		// xu ly khi an nut chuyen bai hat tiep theo
		if(e.getSource() == nextButton) {
			if(timer != null) {
				timer.stop(); // dung timer khi timer dang chay
			}
			if(checkLikedList) {
				name = likedList.higher(name); // neu dang o library thi se chuyen den bai hat tiep cua library
			} else {
				name = nameList.higher(name); // neu khong thi se chuyen den bai hat tiep cua home
			}
			myPane.remove(bot);
			if(clip != null) {
				clip.close();
			}
			bot = createBotPanel(name);
			myPane.add(bot, JLayeredPane.POPUP_LAYER);
			myPane.repaint();
		}
		
		// xu ly previous tuong tu next
		if(e.getSource() == previousButton) {
			if(timer != null) {
				timer.stop();
			}
			if(checkLikedList) {
				name = likedList.lower(name);
			} else {
				name = nameList.lower(name);
			}
			myPane.remove(bot);
			if(clip != null) {
				clip.close();
			}
			bot = createBotPanel(name);
			myPane.add(bot, JLayeredPane.POPUP_LAYER);
			myPane.repaint();
		}
		
		if(clip != null) {
			// kiem tra text cua nut play sau khi an
			if(playButton.getText() == "Stop") {
				if(clip != null) {
					// neu an nut play sau khi het nhac thi no se phat lai bai hat do
					if(clip.isRunning() == false) clip.setMicrosecondPosition(0);
					timer.restart();
				}
				clip.start();
				timer.start();
			} else {
				clip.stop();
				timer.stop();
			}
		}
		
		// neu dang chon nut library thi se set cac cai dat tuong ung
		if(checkLikedList) {
			// kiem tra neu khong co bai hat nao phia sau thi khong an duoc next
			if(name != null && likedList.higher(name) == null) {
				nextButton.setEnabled(false);
			}
			// kiem tra neu khong co bai hat nao phia truoc thi khong an duoc previous
			if(name != null && likedList.lower(name) == null) {
				previousButton.setEnabled(false);
			}
			
		} else {
			// neu chua an chon bai hat thi khong an duoc 2 nut
			if(name != null) {
				nextButton.setEnabled(true);
				previousButton.setEnabled(true);
			}
			// neu bai hat dang chon la bai hat dau thi khong an duoc previous
			if(name == nameList.first()) {
				previousButton.setEnabled(false);
			}
			// neu bai hat dang chon la bai hat cuoi thi khong an duoc next
			if(name == nameList.last()) {
				nextButton.setEnabled(false);
			}
		}
	}
	
}
