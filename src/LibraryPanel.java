import java.awt.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class LibraryPanel extends JPanel{
	JFrame frame;
	SharingData data;
	int width, height;
	JPanel panel;
	JScrollPane scrollPane;
	TreeSet<String> likedList;
	TreeMap<String, JButton> buttonList;
	JPanel mainPanel, headingPanel;
	
	public LibraryPanel(JFrame frame, SharingData data, TreeSet<String> likedList, TreeMap<String, JButton> buttonList) {
		this.frame = frame;
		this.data = data;
		this.width = frame.getWidth();
		this.height = frame.getHeight();
		this.likedList = likedList; // lay danh sach ten cac bai hat duoc yeu thich
		this.buttonList = buttonList; // lay danh sach cac button tuong ung voi ten bai hat
		createGUI(); // tao giao dien
	}
	
	void createGUI() {
		this.setBounds(width/5, 0, width*4/5, height*5/6);
		this.setLayout(null);
		
		// panel chinh
		mainPanel = new JPanel();
		mainPanel.setBounds(0, this.height/15, this.width*4/5-15, this.height*14/15);
		mainPanel.setBackground(new Color(105, 105, 105));
		
		// panel phia tren
		headingPanel = new JPanel();
		headingPanel.setBounds(0, 0, width*4/5, height/15);
		headingPanel.setBackground(Color.gray);
		headingPanel.setLayout(null);
		
		panel = new JPanel(); // panel add cac button
		panel.setBackground(new Color(105, 105, 105));
		panel.setLayout(new GridLayout(0, 3, 30, 30));
		panel.setBorder(new EmptyBorder(0, 20, 0, 20));
		
		for(String key: likedList) {
			panel.add(buttonList.get(key)); // add cac button co key tuong ung voi danh sach thich
		}
		
		scrollPane = new JScrollPane(); // tao thanh truot
		scrollPane.setPreferredSize(new Dimension(width*4/5-20, height*19/25));
		scrollPane.getVerticalScrollBar().setUnitIncrement(20);
		scrollPane.setViewportView(panel);
		scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		mainPanel.add(scrollPane);
		this.add(mainPanel);
		this.add(headingPanel);
	}
	
	public String getName() {
		return "Library";
	}
}
