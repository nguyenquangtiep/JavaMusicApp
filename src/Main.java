import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		SharingData data = new SharingData();
//		MainFrame mainFrame = new MainFrame(data);
		IDandPassword idAndPassword = new IDandPassword();
		new LoginFrame(idAndPassword.getLoginInfo());
	}

}