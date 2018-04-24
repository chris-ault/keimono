package keimono.view;

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
import javafx.fxml.FXML;

import javafx.scene.layout.Pane;
import keimono.Main;
import keimono.model.KeywordListHandler;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Button;

public class crawlerController {

    private static int problemFile=0;
	private String[] relevantWords;
	private ArrayList<String> keywords = new ArrayList<String>();

	@FXML
	private Pane CrawlerPane;
	@FXML
	private Button parseBtn;
	@FXML
	private ProgressBar crawlPercent;
	@FXML
	private  Label numOfArticles;
	@FXML
	private Label wordsPerSecond;
	@FXML
	private Label articlesPerMinute;
	@FXML
	private Label timeRemainString;
	@FXML
	private Label articlesComplete;

	private Main mainApp;
	private FTPClient ftpClient;
	private MaxentTagger tagger;
	private static int fileCount=0;
	private static int filesCompleted=0;
	private String selectedDirectory;
	private KeywordListHandler keywordHandler;


	@FXML
	private void initialize() {
		try {
			keywordHandler = new KeywordListHandler();
		} catch (IOException e) {
			System.out.println("Problem in keywordListHandler");
			e.printStackTrace();
		}
		setRelevantWords();
	}

	private void setRelevantWords(){
		keywords = keywordHandler.getKeywordList();
		relevantWords = new String[keywords.size()];
		keywords.toArray(relevantWords);
	}


	public void setSelectedDirectory(String directory){
		this.selectedDirectory = directory;
		System.out.println("Selected directory: " + directory);
	}

	public void setMainApp(Main main){
		this.mainApp = main;

	}

	public void setClient(FTPClient ftpClient) {
		this.ftpClient = ftpClient;
		System.out.println("FTP addr: "+ftpClient);
		}

	public void setTagger(MaxentTagger tagger) {
		this.tagger = tagger;
		System.out.println("Tagger addr: "+tagger);
	}

	@FXML
	public void nlParse() {
		parseBtn.setVisible(false);

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
	}

	private void analyzeDir(String dir,FTPFile files[]) {

		//int i = 0;

		System.out.println("File count: " + files.length);
		int fileCount=(int)files.length;
		setArticleCount(""+fileCount);

		for(int i = 0; i < files.length; ++i){
			//update progress on progress bar
			double progress = i/files.length;
			crawlPercent.setProgress(progress);
			float complete =  	(float)i / (float)files.length*(float)100;
			System.out.printf("\n\t\tFile %d out of %d in directory\n %.4f%% complete\tWith %d problem files\n", i, files.length,complete,problemFile);
			//System.out.printf(i + " out of " + files.length +" "+ (double)i/(double)files.length*(float)100 +"% complete");
			System.out.println(dir+files[i].getName().toLowerCase());
			downloadAnalyzeDisplay(dir+files[i].getName());
		}

	}

	private  void setArticleCount(String string) {
		numOfArticles.setText(string);
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
        try {
		System.out.println("The file to test is ["+address+"]");
		boolean online = true;
		if(online ) {
		// APPROACH #2: using InputStream retrieveFileStream(String)
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
        }
        if(!online) {
	//String sample = "This is a small piece of sample text";
	//build stuff needed for Gson
	//String path = "article__1522392853.html.json";
    BufferedReader bufferedReader = new BufferedReader(new FileReader(address));
	theFile = RBSI(bufferedReader);  //This parses a file reader buffered reader, we now have a string instead
        }

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
	articlesComplete.setText(""+filesCompleted);
	//Increment progress when file is parsed
	crawlPercent.setProgress((float)filesCompleted/(float)fileCount);
    }
    else {
    	System.out.println("Incomplete download! Error count is "+ ++problemFile +"\n\n");;
    	filesCompleted++;
    	articlesComplete.setText(""+filesCompleted);

    	//I'd like to still increment progress even if a file isn't parsed
    	crawlPercent.setProgress((float)filesCompleted/(float)fileCount);

    }
    } catch (JsonSyntaxException j) {
    	problemFile++;

    	System.out.println("Json Problems in file "+address+" & "+ problemFile+" problem files\n\n");

    }

    } catch (IOException ex) {
        System.out.println("Error: " + ex.getMessage());
        ex.printStackTrace();
    }
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

	public void handleWindowShownEvent(){
		nlParse();
	}



}
