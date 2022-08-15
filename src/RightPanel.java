import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.*;
import java.util.regex.Pattern;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class RightPanel extends JPanel{
	
	JPanel mainPanel, headingPanel, panel;
	JScrollPane scrollPane;
	JTextField searchField;
	JButton searchButton;
	Set<String> keySet;
	int width, height;
	
	public RightPanel(JFrame frame) {
		this.width = frame.getWidth();
		this.height = frame.getHeight();
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
	
	void addButton(TreeMap<String, JButton> buttonList) {
		keySet = buttonList.keySet();
		for(String key: keySet) {
			panel.add(buttonList.get(key));
		}
	}
	
	void addButton(TreeMap<String, JButton> buttonList, boolean search) {
		if(search == true) {
			keySet = buttonList.keySet(); // lay danh sach ten cac bai hat
			
			// phan de nhap ten bai hat can tim
			searchField = new JTextField();
			searchField.setBounds(width/5, 7, width/5, height/20);
			searchField.setFont(new Font(null, Font.BOLD, 25));
			searchField.setBorder(null);
			searchField.setBorder(new EmptyBorder(2, 3, 2, 3));
			
			// nut tim kiem
			searchButton = new JButton("Search");
			searchButton.setBounds(width*3/7, 7, 100, height/20);
			searchButton.setBackground(Color.lightGray);
			searchButton.setForeground(Color.darkGray);
			searchButton.setFont(new Font(null, Font.BOLD, 25));
			searchButton.setFocusable(false);
			searchButton.setBorder(null);
			searchButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					panel.removeAll(); // xoa het moi khi an nut search
					if(searchField.getText().isEmpty() == false) {
						for(String key: keySet) {
							if(((unAccent(key)).toLowerCase()).contains(unAccent(searchField.getText()).toLowerCase())) { // tim kiem khong phan biet chu hoa
								panel.add(buttonList.get(key)); // them cac bai hat tim kiem duoc vao panel
							}
						}
					}
					panel.revalidate();
					panel.repaint();
				}
			
			});
			
			headingPanel.add(searchField);
			headingPanel.add(searchButton);
		} else {
			addButton(buttonList);
		}
	}
	
	void addButton(TreeMap<String, JButton> buttonList, TreeSet<String> likedList) {
		for(String key: likedList) {
			panel.add(buttonList.get(key)); // add cac button co key tuong ung voi danh sach thich
		}
	}
	
	String unAccent(String s) {
		String temp = Normalizer.normalize(s, Form.NFD);
		return temp.replaceAll("\\p{M}","");
	}
}
