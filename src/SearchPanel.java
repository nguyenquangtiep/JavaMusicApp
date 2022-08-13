import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class SearchPanel extends JPanel{
	JFrame frame;
	SharingData data;
	int width, height;
	JPanel panel, headingPanel, mainPanel;
	JScrollPane scrollPane;
	TreeMap<String, JButton> buttonList;
	JTextField searchField;
	JButton searchButton;
	
	public SearchPanel(JFrame frame, SharingData data, TreeMap<String, JButton> buttonList) {
		this.frame = frame;
		this.data = data;
		this.width = frame.getWidth();
		this.height = frame.getHeight();
		this.buttonList = buttonList;
		createGUI(); // tao giao dien
	}
	
	void createGUI() {
		this.setBounds(width/5, 0, width*4/5, height*5/6);
		this.setLayout(null);
		
		// phan de nhap ten bai hat can tim
		searchField = new JTextField();
		searchField.setBounds(width/5, 7, width/5, height/20);
		searchField.setFont(new Font(null, Font.LAYOUT_LEFT_TO_RIGHT, 25));
		searchField.setBorder(null);
		
		// nut tim kiem
		searchButton = new JButton("Search");
		searchButton.setBounds(width*3/7, 7, 100, height/20);
		searchButton.setFont(new Font(null, Font.BOLD, 25));
		searchButton.setFocusable(false);
		searchButton.setBorder(null);
		
		// panel chinh
		mainPanel = new JPanel();
		mainPanel.setBounds(0, this.height/15, this.width*4/5-15, this.height*14/15);
		mainPanel.setBackground(new Color(105, 105, 105));
		
		// panel phia tren
		headingPanel = new JPanel();
		headingPanel.setBounds(0, 0, width*4/5, height/15);
		headingPanel.setBackground(Color.gray);
		headingPanel.setLayout(null);
		headingPanel.add(searchField);
		headingPanel.add(searchButton);
		
		panel = new JPanel(); // panel de add button
		panel.setBackground(new Color(105, 105, 105));
		panel.setLayout(new GridLayout(0, 3, 30, 30));
		panel.setBorder(new EmptyBorder(0, 20, 0, 20));
		
		Set<String> keySet = buttonList.keySet(); // lay danh sach ten cac bai hat
		
		searchButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				panel.removeAll(); // xoa het moi khi an nut search
				if(searchField.getText().isEmpty() == false) {
					for(String key: keySet) {
						if((key.toLowerCase()).contains(searchField.getText().toLowerCase())) { // tim kiem khong phan biet chu hoa
							panel.add(buttonList.get(key)); // them cac bai hat tim kiem duoc vao panel
						}
					}
				}
				panel.revalidate();
				panel.repaint();
			}
		
		});
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
		return "Search";
	}
}
