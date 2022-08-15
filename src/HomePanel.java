import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class HomePanel extends RightPanel{
	
	public HomePanel(JFrame frame) {
		super(frame);
		super.createGUI();
	}
	
	public String getName() {
		return "Home";
	}
}
