package keimono;

import java.io.IOException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.AnchorPane;

import keimono.view.*;


public class Main extends Application {

	//fxml
	private Stage primaryStage;
	private BorderPane rootLayout;
	private BorderPane initializerWindow;
	//FTP and tagger
	private FTPClient ftpClient;
	private MaxentTagger tagger;

	@Override
	public void start(Stage primaryStage) {

		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Keimono");

		showInitializer();
		//check and make sure everything got initialized
		if(ftpClient == null || tagger == null){
			//if something went wrong, don't do anything else
		} else {
			//otherwise continue
			initRootLayout();

			showMainView();

		}

	}

	private void showInitializer() {
		try{
			//load initializer view
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/SplashScreen.fxml"));
			AnchorPane splashScreen = (AnchorPane) loader.load();

			//create stage for splash screen
			Stage splashStage = new Stage();
			splashStage.setTitle("Keimono Initializer");

			//set up scene
			Scene scene = new Scene(splashScreen);
			splashStage.setScene(scene);
			//give controller access to the main app and scene
			SplashScreenController controller = loader.getController();
			controller.setMainApp(this);
			controller.setStage(splashStage);
			//show stage and begin initializing process
			splashStage.show();
			controller.initializeFTP();

		} catch (IOException e){
			e.printStackTrace();
		}

	}

	private void initRootLayout() {
		try {
			//Load root layout from fxml
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();

			//Show the scene containing the root layout.
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void showMainView() {
		try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/MainWindow.fxml"));
            AnchorPane personOverview = (AnchorPane) loader.load();

            // Set person overview into the center of root layout.
            rootLayout.setCenter(personOverview);

            // Give the controller access to the main app.
            MainWindowController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
        	System.out.println("Problem in main view");

            e.printStackTrace();
        }
	}

    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setFTP(FTPClient client){
    	this.ftpClient = client;
    }

    public void setTagger(MaxentTagger tagger){
    	this.tagger = tagger;
    }

	public static void main(String[] args) {
		launch(args);
	}
}
