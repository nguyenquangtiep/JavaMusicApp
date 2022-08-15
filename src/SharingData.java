import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import javax.sound.sampled.*;
import javax.swing.*;

public class SharingData {
	
	String nameURL = "AudioName.txt";
	TreeMap<String, File> fileList; // danh sach cac file tuong ung voi ten bai hat
	TreeSet<String> nameList; // danh sach ten cac bai hat
	FileInputStream fis;
	InputStreamReader reader;
	BufferedReader bReader;
	File file;
	String name;
	
	public SharingData() {
		nameList = createNameList();
		createAudioList(); // tao danh sach file audio
	}
	
	void createAudioList() {
		fileList = new TreeMap<String, File>();
			for(String name: nameList) {
				if(name != null) {
					file = new File("ListAudio\\"+name+".wav");
					if(file.exists()) {
						fileList.put(name, file);
					}
				}
			}
	}
	
	// tao danh sach ten cac bai hat duoc luu trong file
	public TreeSet<String> createNameList() {
		TreeSet<String> nameList = new TreeSet<String>();
		try {
			fis = new FileInputStream(nameURL);
			reader = new InputStreamReader(fis, StandardCharsets.UTF_8);
			bReader = new BufferedReader(reader); 
			while((name = bReader.readLine()) != null) {
				if(name.isEmpty()) {
					continue;
				}
				nameList.add(name);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(fis != null) {
					fis.close();
				}
				if(reader != null) {
					reader.close();
				}
				if(bReader != null) {
					bReader.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return nameList;
	}
	
	public TreeMap<String, File> getFileList() {
		return fileList;
	}

	public TreeSet<String> getNameList() {
		return nameList;
	}
	
	
}
