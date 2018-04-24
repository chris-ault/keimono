package keimono.model;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class KeywordListHandler {

	private File keywordFile;
	private static String filePath = "rsc/txts/Keywords.txt";

	public KeywordListHandler() throws IOException {

		this.keywordFile = new File(filePath);

	}

	public ArrayList<String> getKeywordList() {

		String keyword;
		ArrayList<String> keywords = new ArrayList<String>();
		try {
			FileReader fr = new FileReader(keywordFile);
			BufferedReader br = new BufferedReader(fr);
			while((keyword = br.readLine())!= null){
				if(keyword != null){
					keywords.add(keyword);
				}
			}
			br.close();
		} catch (IOException e){
			e.printStackTrace();
		}
		return keywords;

	}

	public void updateKeywordList(ArrayList<String> updatedList){

		try {
			//construct temporary file that will be renamed to original filename
			File tempFile = new File(keywordFile.getAbsolutePath() + ".tmp");
			FileWriter fw = new FileWriter(tempFile);
			PrintWriter pw = new PrintWriter(fw);
			//write updated list to temp file
			for(String s : updatedList){
				pw.println(s);
				pw.flush();
			}
			pw.close();
			//delete original file
			if (!keywordFile.delete()){
				System.out.println("Could not delete file");
				return;
			}
			//rename new file to the filename of the original
			if(!tempFile.renameTo(keywordFile)){
				System.out.println("Could not rename file");
			}
		} catch (IOException e){
			e.printStackTrace();
		}

	}

}
