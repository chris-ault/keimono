package keimono;

import java.io.IOException;

import javafx.application.Preloader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import keimono.view.SplashScreenController;

public class KeimonoPreloader extends Preloader {

	private Stage preloaderStage;
	private Scene scene;
	private AnchorPane splashScreen;
	private SplashScreenController controller;

	public KeimonoPreloader(){

	}

	@Override
	public void init() throws Exception{

		try{
			//load initializer view
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/SplashScreen.fxml"));
			splashScreen = (AnchorPane) loader.load();

			//set up scene
			scene = new Scene(splashScreen);
			//get controller
			controller = loader.getController();

		} catch (IOException e){
			e.printStackTrace();
		}

	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		this.preloaderStage = primaryStage;
		preloaderStage.setTitle("Keimono Initializer");
		preloaderStage.setScene(scene);
		preloaderStage.show();

	}

	@Override
	public void handleApplicationNotification(PreloaderNotification info){
		if(((ProgressNotification)info).getProgress() == 1){
			controller.setStatus("Connecting to FTP server...");
			controller.setProgressBar(0.25);
		} else if (((ProgressNotification)info).getProgress() == 2){
			controller.setStatus("Connecting to FTP server...connected");
			controller.setProgressBar(0.50);
		} else if (((ProgressNotification)info).getProgress() == 3){
			controller.setStatus("Loading NLP tagger...");
			controller.setProgressBar(0.75);
		} else if (((ProgressNotification)info).getProgress() == 4){
			controller.setStatus("Loading NLP tagger...done");
			controller.setProgressBar(1.00);
		} else if (((ProgressNotification)info).getProgress() == -1){
			controller.errorDected("Failed to connect to Server");
		} else if (((ProgressNotification)info).getProgress() == -2){
			controller.errorDected("Failed to load NLP Tagger");
		}
	}

	@Override
	public void handleStateChangeNotification(StateChangeNotification info){
		StateChangeNotification.Type type = info.getType();
		switch(type){
			case BEFORE_LOAD:
				break;
			case BEFORE_INIT:
				controller.setStatus("Initializing");
				controller.setProgressBar(0);
				break;
			case BEFORE_START:
				controller.setStatus("Done");
				controller.setProgressBar(1.00);
				preloaderStage.hide();
				break;
		}
	}

}
