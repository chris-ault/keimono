package keimono.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.lang.Exception;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

import keimono.Main;

public class SplashScreenController {
	@FXML
	private Label status;
	@FXML
	private Label error;
	@FXML
	private Button quitBtn;

	private Main mainApp;
	private Stage stage;
	//ftp connection setup
	static String server = "www.crawler.giize.com";
	static int port = 21;
	static String user = "spiderftp";
	static String pass = "hello123";

	public void initialize(){

		quitBtn.setVisible(false);
		error.setVisible(false);
		status.setVisible(true);
		status.setText("Starting Program");

	}

	public void initializeFTP() {

		FTPClient ftpClient = new FTPClient();
		status.setText("Connecting to FTP server...");

		try{
			ftpClient.connect(server, port);
			ftpClient.login(user, pass);
			ftpClient.enterLocalPassiveMode();
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

			status.setText("Connecting to FTP server...connected");
			//passes connected client back to main
			mainApp.setFTP(ftpClient);
			initializeTagger();
		} catch (IOException e){
			status.setText("Connecting to FTP server...failed");
			error.setVisible(true);
			error.setText("Error: " + e.getMessage());
			quitBtn.setVisible(true);
		}

	}

	private void initializeTagger() {

		MaxentTagger tagger;
		status.setText("Initializing tagger...");
		try {
			tagger = new MaxentTagger("rsc\\english-bidirectional-distsim.tagger");
			//passes tagger back to main
			mainApp.setTagger(tagger);

			status.setText("Initializing tagger...done");
			//required initializing done, close the window
			stage.close();
		} catch (Exception e) {
			status.setText("Initializing tagger...failed");
			error.setVisible(true);
			error.setText("Error: " + e.getMessage());
			quitBtn.setVisible(true);
		}

	}

	@FXML
	private void closeProgram(ActionEvent e){
		System.exit(0);
	}

	/**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    public void setStage(Stage stage){
    	this.stage = stage;
    }

}
