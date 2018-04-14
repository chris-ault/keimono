package keimono;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import com.google.gson.*;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class NLPPostaggerTest {

	public static void main(String[] args) throws IOException, ClassNotFoundException{

		//
		//These are relevant words to seek for they are all converted to lower case before comparison.
		//
		String[] relevantWords = {"database","state","appealing","Spyware","problem","rescue","suffer","infection","infecting","network","administrator","exploit","stolen"};
			
		//Initialize the tagger
		MaxentTagger tagger = new MaxentTagger("rsc\\english-bidirectional-distsim.tagger");

		
		
		
        String server = "www.crawler.giize.com";
        int port = 21;
        String user = "spiderftp";
        String pass = "hello123";

        FTPClient ftpClient = new FTPClient();
        try {

            ftpClient.connect(server, port);
            ftpClient.login(user, pass);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            
         // lists files and directories in the current working directory
            FTPFile[] files = ftpClient.listFiles("/data/2018/techrepublic.com/resource-library_whitepapers__1522883673.html.json");
             
            // iterates over the files and prints details for each
            DateFormat dateFormater = new SimpleDateFormat("MM-dd-YYYY HH:mm:ss");
             
            for (FTPFile file : files) {
                String details = file.getName();
                if (file.isDirectory()) {
                    details = "[" + details + "]";
                }
                details += "\t\t" + file.getSize()+"bytes";
                //details += "\t\t" + dateFormater.format(file.getTimestamp().getTime());
                System.out.println(details);
            } 
            

         // APPROACH #2: using InputStream retrieveFileStream(String)
            String remoteFile2 = "/data/2018/techrepublic.com/resource-library_whitepapers__1522883673.html.json";
           File downloadFile2 = new File("crawler2.0.txt");
            OutputStream outputStream2 = new BufferedOutputStream(new FileOutputStream(downloadFile2));
            InputStream inputStream = ftpClient.retrieveFileStream(remoteFile2);
            byte[] bytesArray = new byte[4096];
            String theFile = ""; // Not setting an an encoding for UTF-8 encoding;
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(bytesArray)) != -1) {
                //outputStream2.write(bytesArray, 0, bytesRead); 				 // Don't need to save the file anymore

            	theFile += new String(bytesArray, guessEncoding(bytesArray)); 	// This is more than likely just "UTF-8" but this is safer
            }
            System.out.println("\n\n---Encoding of File appears to be" + guessEncoding(bytesArray));
            boolean  success = ftpClient.completePendingCommand();
            if (success) {
                System.out.println("\n\n---File has been stored as string successfully. String Follows...");
                System.out.println(theFile);
            }
            //outputStream2.close();
            inputStream.close();
            
            //Logout just to keep the server happy in case we crash later
            ftpClient.logout();
            ftpClient.disconnect();		
		

		//String sample = "This is a small piece of sample text";
		//build stuff needed for Gson
		//String path = "article__1522392853.html.json";
        //BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
		//String article = parse(RBSI(bufferedReader));  //This parses a file reader buffered reader, we now have a string instead
        
            
            
        //  This parses the json string and removes non printable's that cause trouble
        String article = parse(theFile.replaceAll("\\p{C}", ""));
        
		String tagged = tagger.tagString(article);
		int count = StringUtils.countMatches(tagged, "_"); 				//How many parts of speech?
		System.out.println("\n\n\n---File as a string parsed for unprintables follows...");
		System.out.println(article);
				System.out.println("\n\n\n---Parsed article text follows...");
		System.out.println(tagged);
		System.out.println("\n\n---Relevancy info follows...");
		Boolean good = false;
		int positive = 0;
		 double relevancy = 0.0 ;
		for(int i=0;i<count;i++) {										//Loop for each part
		String[] group = tagged.split(" ");								//Separate words
		//System.out.println(group[i]);
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
				    	System.out.printf("\t\t\t\t%.3f%% Relevancy so far, %d count / %d total\n",relevancy,positive,i);
				    }
				}
				good = false;
			}
		}
		 relevancy = (double)positive/(double)count*100.0;
		System.out.printf("%d occurances in %d count,\t %.3f%% Relevancy\n",positive,count,relevancy);	

		
        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
		
        }

	public static String RBSI(BufferedReader buffIn) throws IOException { //ReadBigStringIn
        StringBuilder everything = new StringBuilder();
        String line;
        while( (line = buffIn.readLine()) != null) {
           everything.append(line);
        }
        return everything.toString();
    }


    public static  String parse(String jsonLine) {
        JsonElement jelement = new JsonParser().parse(jsonLine);
        JsonObject  jobject = jelement.getAsJsonObject();
        String result = jobject.get("text").getAsString();
        return result;
    }
    
    
    public static String guessEncoding(byte[] bytes) {
        String DEFAULT_ENCODING = "UTF-8";
        org.mozilla.universalchardet.UniversalDetector detector =
            new org.mozilla.universalchardet.UniversalDetector(null);
        detector.handleData(bytes, 0, bytes.length);
        detector.dataEnd();
        String encoding = detector.getDetectedCharset();
        detector.reset();
        if (encoding == null) {
            encoding = DEFAULT_ENCODING;
        }
        return encoding;
    }
    


}
