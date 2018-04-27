package keimono;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import com.sun.javafx.application.LauncherImpl;
import javafx.application.HostServices;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

import javafx.application.Application;
import javafx.application.Preloader;
import javafx.fxml.FXMLLoader;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.AnchorPane;

import keimono.view.*;
import keimono.model.*;


public class Main extends Application {

	//fxml
	private Stage primaryStage;
	private BorderPane rootLayout;
	private HostServices hostServices = getHostServices();
	//FTP setup and info
	private FTPClient ftpClient;
	static String server = "www.crawler.giize.com";
	static int port = 21;
	static String user = "spiderftp";
	static String pass = "hello123";
	public String selectedDirectory = "";

	//text tagger
	private MaxentTagger tagger;

	@SuppressWarnings("restriction")
	@Override
	public void init() throws Exception {
		//initialize ftp connection
		ftpClient = new FTPClient();
		LauncherImpl.notifyPreloader(this, new Preloader.ProgressNotification(1));
		try{
			ftpClient.connect(server, port);
			ftpClient.login(user, pass);
			ftpClient.enterLocalPassiveMode();
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			LauncherImpl.notifyPreloader(this, new Preloader.ProgressNotification(2));
			System.out.println("Client addr: "+ftpClient);
			TimeUnit.SECONDS.sleep(1);

		} catch (IOException e){
			LauncherImpl.notifyPreloader(this, new Preloader.ProgressNotification(-1));
			stop();
			TimeUnit.SECONDS.sleep(5);
			System.exit(1);
		}
		//initialize tagger
		LauncherImpl.notifyPreloader(this, new Preloader.ProgressNotification(3));
		try {
			tagger = new MaxentTagger("rsc\\english-bidirectional-distsim.tagger");
			LauncherImpl.notifyPreloader(this, new Preloader.ProgressNotification(4));
			TimeUnit.SECONDS.sleep(1);

		} catch (Exception e) {
			LauncherImpl.notifyPreloader(this, new Preloader.ProgressNotification(-2));
			stop();
			TimeUnit.SECONDS.sleep(5);
			System.exit(1);
		}
	}

	@Override
	public void start(Stage primaryStage) {

		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Keimono");

		initRootLayout();

		showMainView();


	}

	private void initRootLayout() {
		try {
			//Load root layout from fxml
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();

			//give controller access to main
			RootLayoutController controller = loader.getController();
			controller.setMainApp(this);

			//Show the scene containing the root layout.
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void showMainView() {
		try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/MainWindow.fxml"));
            AnchorPane personOverview = (AnchorPane) loader.load();

            // Set person overview into the center of root layout.
            rootLayout.setCenter(personOverview);

            // Give the controller access to the main app.
            MainWindowController controller = loader.getController();
            controller.setClient(ftpClient);
            controller.setMainApp(this);

            //meat and potaters
            controller.listDir();


        } catch (IOException e) {
        	System.out.println("Problem in main view (io Exception)");

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


	@SuppressWarnings("restriction")
	public static void main(String[] args) {
		LauncherImpl.launchApplication(Main.class, KeimonoPreloader.class, args);
	}



	public void showEditKeywords() {
		try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/WordsView.fxml"));
            AnchorPane WordsView = (AnchorPane) loader.load();

            // Set person overview into the center of root layout.
            rootLayout.setCenter(WordsView);

            // Give the controller access to the main app.
            WordsViewController controller = loader.getController();
            controller.setMainApp(this);


        } catch (IOException e) {
        	System.out.println("Problem in edit keywords view (io Exception)");

            e.printStackTrace();
        }
	}

	public void showCrawler(String directory){

		try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/Crawler.fxml"));
            AnchorPane CrawlerView = (AnchorPane) loader.load();

            // Set person overview into the center of root layout.
            rootLayout.setCenter(CrawlerView);

            // Give the controller access to the main app.
            CrawlerController controller = loader.getController();
            controller.setMainApp(this);
            controller.setClient(ftpClient);
            controller.setTagger(tagger);
            controller.setSelectedDirectory(directory);


        } catch (IOException e) {
        	System.out.println("Problem in crawler view (io Exception)");

            e.printStackTrace();
        }
	}

	public void showResults(ArrayList<Article> articles){

		try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/ResultsWindow.fxml"));
            AnchorPane resultsWindow = (AnchorPane) loader.load();

            // Set person overview into the center of root layout.
            rootLayout.setCenter(resultsWindow);

            // Give the controller access to the main app.
            ResultsWindowController controller = loader.getController();
            controller.setMainApp(this);
           controller.setArticles(articles);
           controller.listArticles();

        } catch (IOException e) {
        	System.out.println("Problem in crawler view (io Exception)");

            e.printStackTrace();
        }
	}

	public void showArticleDetails(Article article){
		try {
            // Load fxml and create a new stage for the popup dialogue
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/ArticleDetailView.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            //create dialogue stage
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Article Details");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            //set up controller
            ArticleDetailViewController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setHostServices(hostServices);
            controller.setArticle(article);
            controller.displayTitle();
            controller.displayArticleText();
            controller.displayKeywords();

            //show dialog and wait for user to close it
            dialogStage.showAndWait();

        } catch (IOException e) {
        	System.out.println("Problem in article detail view (io Exception)");

            e.printStackTrace();
        }
	}
}
