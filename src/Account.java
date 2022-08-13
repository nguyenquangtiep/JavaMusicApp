import java.io.*;
import java.util.*;

public class Account {
	
	HashMap<String, String> loginInfo = new HashMap<String, String>();
	HashMap<String, String> username = new HashMap<String, String>();
	String accountURL = "Account.txt";
	FileInputStream fis;
	InputStreamReader reader;
	BufferedReader bReader;
	
	public Account() {
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
				username.put(tokens[0], tokens[2]);
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

	public HashMap<String, String> getUsername() {
		return username;
	}
	
}