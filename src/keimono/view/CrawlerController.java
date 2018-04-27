package keimono.view;

import java.io.IOException;
import java.util.ArrayList;
import org.apache.commons.net.ftp.FTPClient;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;

import javafx.scene.layout.Pane;
import keimono.Main;
import keimono.model.*;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Button;

public class CrawlerController {

	private String[] relevantWords;
	private ArrayList<String> keywords = new ArrayList<String>();

	@FXML
	private Pane CrawlerPane;
	@FXML
	private Button parseBtn;
	@FXML
	private Button cancelBtn;
	@FXML
	private Button backBtn;
	@FXML
	private Button resultsBtn;
	@FXML
	private Label status;
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
	private String selectedDirectory;
	private KeywordListHandler keywordHandler;
	private AnalyzeDirectory task;
	private ArrayList<Article> taggedArticles;


	@FXML
	private void initialize() {
		try {
			keywordHandler = new KeywordListHandler();
		} catch (IOException e) {
			System.out.println("Problem in keywordListHandler");
			e.printStackTrace();
		}
		setRelevantWords();
		taggedArticles = new ArrayList<Article>();
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
		//System.out.println("Tagger addr: "+tagger);
	}

	@FXML
	private void beginParse(){
		parseBtn.setDisable(true);
		backBtn.setDisable(true);
		crawlPercent.setProgress(0);
		cancelBtn.setDisable(false);

		//create task
		task = new AnalyzeDirectory(ftpClient, relevantWords, tagger, selectedDirectory);
		//unbind progress property and rebind to task progress
		crawlPercent.progressProperty().unbind();
		crawlPercent.progressProperty().bind(task.progressProperty());
		//unbind label text property and rebind to task message
		status.textProperty().unbind();
		status.textProperty().bind(task.messageProperty());

		//handle completed task
		task.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, new EventHandler<WorkerStateEvent>(){
			@Override
			public void handle(WorkerStateEvent t){
				taggedArticles = task.getValue();
				status.textProperty().unbind();
				status.setText("Finished");
				crawlPercent.progressProperty().unbind();
				crawlPercent.setProgress(1);
				cancelBtn.setDisable(true);
				backBtn.setDisable(false);
				resultsBtn.setDisable(false);
			}
		});

		//start the Task
		new Thread(task).start();
	}

	@FXML
	private void cancelParse(){
		parseBtn.setDisable(false);
		cancelBtn.setDisable(true);
		backBtn.setDisable(false);
		resultsBtn.setDisable(false);
		task.cancel(true);
		crawlPercent.progressProperty().unbind();
		status.textProperty().unbind();
		crawlPercent.setProgress(0);
	}

	@FXML
	private void showMain() {
		mainApp.showMainView();
	}

	@FXML
	private void showResults() {
		mainApp.showResults(taggedArticles);
	}

}