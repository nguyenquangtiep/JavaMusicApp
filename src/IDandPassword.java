import java.io.*;
import java.util.*;

public class IDandPassword {
	
	HashMap<String, String> loginInfo = new HashMap<String, String>();
	String accountURL = "IDandPassword.txt";
	FileInputStream fis;
	InputStreamReader reader;
	BufferedReader bReader;
	
	public IDandPassword() {
		inputLoginInfo();
	}

	void inputLoginInfo() {
		String line;
		String[] tokens;
		try {
			fis = new FileInputStream(accountURL);
			reader = new InputStreamReader(fis);
			bReader = new BufferedReader(reader);
			while((line = bReader.readLine()) != null) {
				if(line.isEmpty()) {
					continue;
				}
				tokens = line.split("[\\W]");
				loginInfo.put(tokens[0], tokens[1]);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public HashMap<String, String> getLoginInfo() {
		return loginInfo;
	}
	
}