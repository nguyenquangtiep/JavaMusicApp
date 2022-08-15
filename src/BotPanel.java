import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class BotPanel extends JPanel implements ActionListener{

	String name, songName, singerName;
	SharingData data;
	int width, height;
	JLabel currentTime, totalTime, songLabel, singerLabel;
	JSlider slider;
	JButton backButton, playButton, nextButton, loopButton, likeButton;
	String[] tokens;
	TreeMap<String, File> fileList;
	AudioInputStream audioStream;
	Clip clip;
	TreeSet<String> likedList;
	TreeSet<String> fullList;
	Timer timer;
	boolean flag = false;
	
	public BotPanel(JFrame frame, SharingData data) {
		this.width = frame.getWidth();
		this.height = frame.getHeight();
		this.fileList = data.getFileList();
		createGUI();
	}
	
	void createGUI() {
		this.setBounds(0, height*5/6, width, height/6);
		this.setBackground(new Color(128, 128, 128));
		this.setLayout(null);
		createComponents();
		addComponents();
	}
	
	void createComponents() {
		// nut play/stop nhac
		playButton = new JButton("Play");
		playButton.setBackground(new Color(77, 77, 77));
		playButton.setForeground(Color.white);
		playButton.setFocusable(false);
		playButton.setBounds(width/2, 0, 125, 25);
		playButton.setEnabled(false);
		playButton.addActionListener(this);
		
		// nut lap khi phat nhac
		loopButton = new JButton("Loop");
		loopButton.setBounds(width/2+260, 0, 125, 25);
		loopButton.setBackground(new Color(77, 77, 77));
		loopButton.setForeground(Color.white);
		loopButton.setFocusable(false);
		loopButton.setEnabled(false);
		loopButton.addActionListener(this);
		
		// nut chuyen bai tiep theo
		nextButton = new JButton("Next");
		nextButton.setBackground(new Color(77, 77, 77));
		nextButton.setForeground(Color.white);
		nextButton.setFocusable(false);
		nextButton.setBounds(width/2+130, 0, 125, 25);
		nextButton.setEnabled(false);
		nextButton.addActionListener(this);
		
		// nut chuyen bai truoc do
		backButton = new JButton("Back");
		backButton.setBackground(new Color(77, 77, 77));
		backButton.setForeground(Color.white);
		backButton.setFocusable(false);
		backButton.setBounds(width/2-130, 0, 125, 25);
		backButton.setEnabled(false);
		backButton.addActionListener(this);
		
		// nut like de them bai hat vao library
		likeButton = new JButton("Like");
		likeButton.setBackground(new Color(77, 77, 77));
		likeButton.setFont(new Font(null, Font.BOLD, 30));
		likeButton.setForeground(Color.white);
		likeButton.setFocusable(false);
		likeButton.setBounds(width-150, 5, 125, height/10);
		likeButton.setEnabled(false);
		likeButton.addActionListener(this);
		
		// thanh truot khi audio chay
		slider = new JSlider();
		slider.setBounds(width/2-80, 30, width/3, 50);
		slider.setBackground(new Color(128, 128, 128));
		slider.setFont(new Font(null, Font.BOLD, 15));
		slider.setForeground(Color.white);
		slider.setValue(0);
		slider.setEnabled(false);
		
		// hien thi thoi gian phat 1 bai hat duoc chon
		totalTime = new JLabel("--:--");
		totalTime.setBounds(width/2+350, 35, 100, 30);
		totalTime.setFont(new Font(null, Font.BOLD, 20));
		
		// hien thi thoi gian hien tai khi phat audio
		currentTime = new JLabel("--:--");
		currentTime.setBounds(width/2-130, 35, 100, 30);
		currentTime.setFont(new Font(null, Font.BOLD, 20));
		
		// label hien thi ten bai hat
		songLabel = new JLabel(); 
		songLabel.setFont(new Font(null, Font.BOLD, 20));
		songLabel.setForeground(Color.white);
		songLabel.setBounds(100, 10, width/3, 25);
		
		// label hien thi nguoi hat
		singerLabel = new JLabel();
		singerLabel.setFont(new Font(null, Font.BOLD, 15));
		singerLabel.setForeground(Color.white);
		singerLabel.setBounds(100, 45, width/3, 20);
	}
	
	void addComponents() {
		this.add(backButton);
		this.add(playButton);
		this.add(nextButton);
		this.add(loopButton);
		this.add(likeButton);
		this.add(slider);
		this.add(totalTime);
		this.add(currentTime);
		this.add(songLabel);
		this.add(singerLabel);
	}
	
	public void backToInit() {
		if(clip != null) clip.close();
		if(timer != null) timer.stop();
		createGUI();
	}
	
	public void setLikedList(TreeSet<String> likedList) {
		this.likedList = likedList;
	}

	public void setFullList(TreeSet<String> fullList) {
		this.fullList = fullList;
	}
	
	public void setPlayInLikedList(boolean flag) {
		this.flag = flag;
	}

	public void playSong(String name) {
		this.name = name;
		if(name != null) {
			setEnableButton();
			runBotPanel();
		}
	}
	
	void runBotPanel() {
			
		// set enable cac button
		playButton.setEnabled(true);
		loopButton.setEnabled(true);
		likeButton.setEnabled(true);
		
		tokens = name.split("-");
		songName = tokens[0].trim();
		singerName = tokens[1].trim();
		songLabel.setText(songName);
		singerLabel.setText(singerName);
		
		slider.setEnabled(true);
		currentTime.setText("00:00");
		playButton.setText("Stop");
		
		if(likedList.contains(name)) {
			likeButton.setText("Liked");
		} else {
			likeButton.setText("Like");
		}
		
		if(clip != null) {
			clip.close();
		}
		runClip();
		slider.setMinimum(0);
		slider.setMaximum((int) (clip.getMicrosecondLength() - clip.getMicrosecondLength()%1000000));
		setSliderDrag();
		showTotalTime();
		if(timer == null) {
			runTimer();
		} else {
			timer.restart();
		}
		
	}
	
	void setEnableButton() {
		if(flag) {
			if(likedList.higher(name) == null) {
				nextButton.setEnabled(false);
			} else {
				nextButton.setEnabled(true);
			}
			if(likedList.lower(name) == null) {
				backButton.setEnabled(false);
			} else {
				backButton.setEnabled(true);
			}
		} else {
			if(fullList.higher(name) == null) {
				nextButton.setEnabled(false);
			} else {
				nextButton.setEnabled(true);
			}
			if(fullList.lower(name) == null) {
				backButton.setEnabled(false);
			} else {
				backButton.setEnabled(true);
			}
		}
	}
	
	void setSliderDrag() {
		// tao rieng 1 changelistener
		ChangeListener changeListener = new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				if(clip != null) {
					if(clip.isRunning() == false) runBotPanel();
				}
				clip.setMicrosecondPosition(((JSlider)e.getSource()).getValue()); // set vi tri phat khi keo slider
			}
			
		};
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
	}
	
	void runClip() {
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
	}

	void showTotalTime() {
		int timeTotal = (int) (clip.getMicrosecondLength()/1000000);
		int minuteTotal, secondsTotal;
		minuteTotal = timeTotal / 60; // tinh so phut
		secondsTotal = timeTotal - minuteTotal*60; // tinh so giay con lai
		
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
	}
	
	void showCurrentTime() {
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
	}
	
	void runTimer() {
		initTimer();
		timer.start();
	}
	
	void initTimer() {
		// moi 1 giay chay timer 1 lan
		timer = new Timer(1000, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				showCurrentTime();
				
				if(slider.getValue() == slider.getMaximum()) {
					if(loopButton.getText() == "Cancel Loop") {
						if(clip.isRunning() == false) {
							clip.start();
							clip.setMicrosecondPosition(0);
							currentTime.setText("00:00");
							slider.setValue(0);
							timer.restart();
						}
					} else {
						if(clip.isRunning()) clip.close();
						playButton.setText("Play");
						slider.setValue(0);
						currentTime.setText("00:00");
						timer.stop();
					}
				}
				
			}
			
		});
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		// set text khi an nut play
		if(e.getSource() == playButton) {
			if(playButton.getText() == "Stop") {
				playButton.setText("Play");
				clip.stop();
				timer.stop();
			} else {
				playButton.setText("Stop");
				clip.start();
				timer.start();
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
			if(flag) {
				if(likedList.higher(name) != null) {
					name = likedList.higher(name);
					playSong(name);
				}
			} else {
				if(fullList.higher(name) != null) {
					name = fullList.higher(name);
					playSong(name);
				}
			}
		}
		
		// xu ly previous tuong tu next
		if(e.getSource() == backButton) {
			if(flag) {
				if(likedList.lower(name) != null) {
					name = likedList.lower(name);
					playSong(name);
				}
			} else {
				if(fullList.lower(name) != null) {
					name = fullList.lower(name);
					playSong(name);
				}
			}
		}
		
		// them vao liked list khi an nut like
		if(e.getSource() == likeButton) {
			if(likedList.contains(name)) {
				likeButton.setText("Like");
				likedList.remove(name);
			} else {
				likeButton.setText("Liked");
				likedList.add(name);
			}
		}
	}
}
