package project.save;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileReader {
	FileInputStream file = null;
	public FileReader(String fileName) {
		 try {
			file = new FileInputStream(fileName);
			try {
				file.read();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		 
	}
	

}
