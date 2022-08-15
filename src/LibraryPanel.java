import java.awt.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class LibraryPanel extends RightPanel{
	
	LibraryPanel(JFrame frame) {
		super(frame);
		super.createGUI();
	}
	
	public String getName() {
		return "Library";
	}
}
