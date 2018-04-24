package keimono.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

import javafx.concurrent.Task;
import keimono.view.crawlerController;

public class AnalyzeDirectory extends Task<ArrayList<String>> {

	private static int problemFile=0;
	private final String[] relevantWords;
	private final FTPClient ftpClient;
	private final MaxentTagger tagger;
	private int fileCount=0;
	private int filesCompleted=0;
	private final String selectedDirectory;

	public AnalyzeDirectory(FTPClient client, String[] words, MaxentTagger tagger, String directory){
		this.ftpClient = client;
		this.relevantWords = words;
		this.tagger = tagger;
		this.selectedDirectory = directory;
	}

	@Override
	protected ArrayList<String> call() throws Exception {

		try{
			String rootDir = "/data/2018/";
            String curDir = rootDir	+ selectedDirectory +"/";
			System.out.println(curDir);
			FTPFile[] files = ftpClient.listFiles(curDir+"*.json");

			analyzeDir(curDir,files);

	   } catch (UnknownHostException ho){
		   	System.out.println("Server MIA");
	   } catch (IOException e) {
		   	e.printStackTrace();
	   }

		return null;
	}

	private void analyzeDir(String dir,FTPFile files[]) {

		//int i = 0;

		System.out.println("File count: " + files.length);
		int fileCount=(int)files.length;

		for(int i = 0; i < files.length; ++i){
			if(this.isCancelled()){
				updateMessage("Cancelled");
				break;
			}
			//update progress bar
			this.updateProgress(i, files.length);
			float complete =  (float)i / (float)files.length*(float)100;
			updateMessage(String.format("File %d out of %d in directory %.0f%% complete\tWith %d problem files", i, files.length,complete,problemFile));
			//System.out.printf(i + " out of " + files.length +" "+ (double)i/(double)files.length*(float)100 +"% complete");
			System.out.println(dir+files[i].getName().toLowerCase());
			downloadAnalyzeDisplay(dir+files[i].getName());
		}

	}

	public String RBSI(BufferedReader buffIn) throws IOException { //ReadBigStringIn
        StringBuilder everything = new StringBuilder();
        String line;
        while( (line = buffIn.readLine()) != null) {
           everything.append(line);
        }
        return everything.toString();
    }

	public void downloadAnalyzeDisplay(String address) throws JsonSyntaxException {

		String theFile ="";
	int attempts=0;
	do{
        try {
		System.out.println("The file to test is ["+address+"] on Attempt #"+attempts);
        String remoteFile2 = address;
       //File downloadFile2 = new File("crawler2.0.txt");
        //OutputStream outputStream2 = new BufferedOutputStream(new FileOutputStream(downloadFile2));
        InputStream inputStream = ftpClient.retrieveFileStream(remoteFile2);
        byte[] bytesArray = new byte[100000];
        theFile = ""; // Not setting an an encoding for UTF-8 encoding;
        int bytesRead = -1;
        while ((bytesRead = inputStream.read(bytesArray)) != -1) {
            //outputStream2.write(bytesArray, 0, bytesRead); 				 // Don't need to save the file anymore

        	theFile += new String(bytesArray, "UTF-8"); 	// This is more than likely just "UTF-8" but guess encoding is safe
        }
        //System.out.println("---Encoding of File appears to be" + guessEncoding(bytesArray));
        boolean  success = ftpClient.completePendingCommand();
        if (success) {
            //System.out.println("\n\n---File has been stored as string successfully.");
        } //else ftp layer problem
        //outputStream2.close();
        inputStream.close();


    try {	//Clean that text!
    	/*theFile.replaceAll("\\\\", "");		//System.out.println("Test "+theFile.replaceAll("\\\\", "")); // treats the first argument as a regex, so you have to double escape the backslash
    	theFile.replaceAll("\\p{C}", "");
    	theFile.replaceAll("\\p{Cntrl}", " ");*/
    	theFile = cleanTextContent(theFile);
    	//  This parses the json string and removes non printable's and extra '_' that cause trouble
    if(getLastnCharacters(theFile,2).equals("\"}")) {
	String printable = parse(theFile);
    String article = printable.replaceAll("_", "");
    System.out.print("Tagging article now...");
	String tagged = tagger.tagString(article);
	int count = StringUtils.countMatches(tagged, "_"); 				//How many parts of speech?
	//System.out.println("---File as a string parsed for unprintables follows...");
	//System.out.println(article);
	//System.out.println("---Parsed article text follows...");
	//System.out.println(tagged);
	System.out.println("Tagged, Relevancy info follows...");
	Boolean good = false;
	int positive = 0;
	 double relevancy = 0.0 ;
	for(int i=0;i<count;i++) {										//Loop for each part
	String[] group = tagged.split(" ");								//Separate words
	//System.out.println(group[i]);
	//System.out.println("i:"+i+" < count:"+count);
	String[] pos = group[i].split("_");								//Break groups up
	//System.out.println(pos[0]+pos[1]);  							//pos[1] is the part of speech
	if ( 	pos[1].charAt(0)==78) {									//'N' words
			//System.out.println(pos[0]+" is a noun");
			good = true;
		} else
			if(	pos[1].charAt(0)==86) {								//'V' words
				//System.out.println(pos[0]+" is a Verb");
				good = true;
		}else{
			//System.out.println(pos[0]+" is garbage\n");
		}
		if(good) {												//Good words match relevant words?
			for(int x=0; x<relevantWords.length; ++x){
			    if(relevantWords[x].toLowerCase().compareTo(pos[0].toLowerCase()) == 0){
			   //System.out.println("Matched on "+pos[0]+" !!This is really good!!");
			    relevancy = (double)++positive/(double)i*100.0;
			    	System.out.printf("\t\t\t\t%d count / %d total\n",positive,i);
			    }
			}
			good = false;
		}
	}
	
	relevancy = (double)positive/(double)count*100.0;
	System.out.printf("%d occurances in %d count,\t \n\n",positive,count);
	filesCompleted++;
	//results.Add(site("a","b","c","d",1,2));
	return;
	
	
	//Increment progress when file is parsed
    }else{
    	attempts++;
    }
    if(attempts==3){
    	System.out.println("Incomplete download! Error count is "+ ++problemFile +"\n\n");;
    	filesCompleted++;
    	return;
    }
    } catch (JsonSyntaxException j) {
    	problemFile++;

    	System.out.println("Json Problems in file "+address+" & "+ problemFile+" problem files\n\n");

    }

    } catch (IOException ex) {
        System.out.println("Error: " + ex.getMessage());
        ex.printStackTrace();
    }
	}while(attempts<4);
	}


	 private String cleanTextContent(String text)
	    {
	        // strips off all non-ASCII characters
	        text = text.replaceAll("[^\\x00-\\x7F]", "");

	        // replace all the ASCII control characters with space
	        text = text.replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", " ");

	        // removes non-printable characters from Unicode
	        text = text.replaceAll("\\p{C}", "");

	        // other
	        text = text.replaceAll("\\n"," ");

	        return text.trim();
	    }

	 public String getLastnCharacters(String inputString,
	            int subStringLength){
	int length = inputString.length();
	if(length <= subStringLength){
	return inputString;
	}
	int startIndex = length-subStringLength;
	return inputString.substring(startIndex);
	}

	public  String parse(String jsonLine) throws IOException {
        JsonElement jelement = new JsonParser().parse(jsonLine);
        JsonObject  jobject = jelement.getAsJsonObject();
        //System.out.println(jobject.toString());
        if (!jobject.get("text").isJsonNull()){
        	String result = jobject.get("text").getAsString();
        	return result;
        } else {
        	throw new IOException();
        }
    }

}
