package keimono.view;

import java.io.IOException;
import java.net.UnknownHostException;

import org.apache.commons.net.SocketClient;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import javafx.fxml.FXML;

import javafx.scene.control.Button;

import javafx.scene.control.ListView;
import keimono.Main;

public class MainWindowController {

	static String server = "www.crawler.giize.com";
    static int port = 21;
    static String user = "spiderftp";
    static String pass = "hello123";
    private static int problemFile=0;
	
	
	@FXML
	private Button aBtn;
	@FXML
	private Button bBtn;
	@FXML
	private Button cBtn;
	@FXML
	private ListView keywordsList;

	// Reference to the main application.
    private Main mainApp;
	private FTPClient ftpClient;

	/**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;

        // Add observable list data to the table
        //personTable.setItems(mainApp.getPersonData());
    }

	public void setClient(FTPClient ftpClient) {
		this.ftpClient = ftpClient;
		System.out.println("User addr: "+ftpClient);	
		}
	
	public void listDir() throws IOException, UnknownHostException{
        try {
        	
            ftpClient.connect(server, port);
            ftpClient.login(user, pass);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);


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
            }
        }catch(UnknownHostException ho){
        	System.out.println("Error server not found");
        }
	}

}
