import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class SearchPanel extends RightPanel{
	
	public SearchPanel(JFrame frame) {
		super(frame);
		super.createGUI();
	}
	
	public String getName() {
		return "Search";
	}
}
