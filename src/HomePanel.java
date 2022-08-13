import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class HomePanel extends JPanel{
	JFrame frame;
	SharingData data;
	TreeMap<String, JButton> buttonList;
	JButton button;
	int width, height;
	JPanel panel;
	JScrollPane scrollPane;
	JButton newButton;
	JPanel mainPanel, headingPanel;
	
	public HomePanel(JFrame frame, SharingData data, TreeMap<String, JButton> buttonList) {
		this.width = frame.getWidth();
		this.height = frame.getHeight();
		this.data = data;
		this.frame = frame;
		this.buttonList = buttonList; // lay danh sach cac nut tuong ung voi ten cua no
		createGUI(); // tao giao dien home
	}

	void createGUI() {
		this.setBounds(width/5, 0, width*4/5, height*5/6);
		this.setLayout(null);
		
		// phan panel chinh
		mainPanel = new JPanel();
		mainPanel.setBounds(0, this.height/15, this.width*4/5-15, this.height*14/15);
		mainPanel.setBackground(new Color(105, 105, 105));
		
		// phan panel phia tren
		headingPanel = new JPanel();
		headingPanel.setBounds(0, 0, width*4/5, height/15);
		headingPanel.setBackground(Color.gray);
		headingPanel.setLayout(null);
		
		// tao panel de add button
		panel = new JPanel();
		panel.setBackground(new Color(105, 105, 105));
		panel.setLayout(new GridLayout(0, 3, 30, 30));
		panel.setBorder(new EmptyBorder(0, 20, 0, 20));
		
		// add vao panel tren
		Set<String> keySet = buttonList.keySet();
		for(String key: keySet) {
			panel.add(buttonList.get(key));
		}
		
		scrollPane = new JScrollPane(); // tao thanh truot
		scrollPane.setPreferredSize(new Dimension(width*4/5-20, height*19/25));
		scrollPane.getVerticalScrollBar().setUnitIncrement(20);
		scrollPane.setViewportView(panel);
		scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		
		// add components
		mainPanel.add(scrollPane);
		this.add(mainPanel);
		this.add(headingPanel);
	}
	
	public String getName() {
		return "Home";
	}
}
