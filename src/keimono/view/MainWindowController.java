package keimono.view;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.apache.commons.net.SocketClient;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.scene.control.Button;

import javafx.scene.control.ListView;
import keimono.Main;
import keimono.model.KeywordListHandler;

public class MainWindowController {

	static String server = "www.crawler.giize.com";
    static int port = 21;
    static String user = "spiderftp";
    static String pass = "hello123";
    private static int problemFile=0;
	//static String[] relevantWords = {"database","security","appealing","Spyware","problem","rescue","suffer","infection","infecting","network","administrator","exploit","stolen","breach"};


	@FXML
	private Button aBtn;
	@FXML
	private Button bBtn;
	@FXML
	private Button cBtn;
	@FXML
	private ListView<String> keywordsList;
	@FXML
	private ListView<String> siteList;

	private ArrayList<String> slist = new ArrayList<String>();
	private ArrayList<String> klist = new ArrayList<String>();
	// Reference to the main application.
    private Main mainApp;
	private FTPClient ftpClient;

	
	Boolean siteSelected;

   @FXML
    private void initialize() {
    	siteSelected = false;
    	mainApp = null;
    	ftpClient = null;
    	keywordsList.setEditable(false);
    	private KeywordListHandler keywordHandler;
    	try {
    		keywordHandler = new KeywordListHandler();
    	} catch(IOException e){
    		System.out.println("Problem in KeywordListHandler");

    	}
    	listKeywords();


    }


	/**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

	public void setClient(FTPClient ftpClient) {
		this.ftpClient = ftpClient;
		System.out.println("User addr: "+ftpClient);
	}


	public void listDir() throws IOException, UnknownHostException{
        try {

         // lists files and directories in the current working directory
            String rootDir = "/data/2018/";
            String curDir = rootDir;//		+	"techrepublic.com/";
            FTPFile[] files = ftpClient.listFiles(curDir);//+"*.json");

            // iterates over the files and prints details for each
            //DateFormat dateFormater = new SimpleDateFormat("MM-dd-YYYY HH:mm:ss");
           //Directory Listing
            for (FTPFile file : files) {
                String details = file.getName();
                if (file.isDirectory()) {
                    details = "[" + details + "]";
                }
                details += "\t\t" + file.getSize()+"bytes";
                //details += "\t\t" + dateFormater.format(file.getTimestamp().getTime());
                System.out.println(details);
                slist.add(details);
                //System.out.println("Updating list fx element");
            }
            siteList.getItems().addAll(slist);
        }catch(UnknownHostException ho){
        	System.out.println("Error server not found");
        }
	}


	public void listKeywords() {
		klist = keywordHandler.getKeywordList();
		keywordsList.getItems().addAll(klist);
	}
	
	@FXML
    private void selectSite() {
    	ObservableList<Integer> selectedIndices =
    			siteList.getSelectionModel().getSelectedIndices();
    	if (!selectedIndices.isEmpty()) {
	    	//System.out.println(MedicationList.medications[(int) selectedIndices.get(0)].getName());
    		String site = slist.get((int) selectedIndices.get(0));
	    	System.out.println(site.substring(site.indexOf("[")+1,site.indexOf("]")));
	    	mainApp.setSelectedDirectory(site.substring(site.indexOf("[")+1,site.indexOf("]")));
	    	siteSelected = true;
	    	//confirmText.setText("Prescription "+MedicationList.medications[(int) selectedIndices.get(0)].getName()+" Added");
    	}
	}
	
	@FXML
	private void parseSite(){
		if(siteSelected){
			mainApp.showCrawler();
			
		}
	}
}
