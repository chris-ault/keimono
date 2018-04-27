package keimono.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FTPConfigReader {

	private File configFile;
	private static String filePath = "config/config_ftp.txt";

	public FTPConfigReader() throws IOException{
		this.configFile = new File(filePath);
	}

	public ArrayList<String> getConfiguration(){
		String line;
		ArrayList<String> config = new ArrayList<String>();
		try {
			FileReader fr = new FileReader(configFile);
			BufferedReader br = new BufferedReader(fr);
			while((line = br.readLine())!=null){
				if(line != null && line.charAt(0)!= '#'){
					String[] readLine = line.split(":");
					config.add(readLine[1]);
				}
			}
			br.close();
			return config;
		} catch (IOException e){
			e.printStackTrace();
		}
		return null;
	}

}
