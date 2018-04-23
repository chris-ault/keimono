package keimono.view;

import javafx.fxml.FXML;

import javafx.scene.control.Button;

import javafx.scene.text.Text;
import keimono.Main;
import javafx.scene.control.TextField;

import javafx.scene.control.ListView;

public class WordsViewController {
	@FXML
	private Button homeBTN;
	@FXML
	private TextField wordToAdd;
	@FXML
	private Button addWordBTN;
	@FXML
	private ListView wordList;
	@FXML
	private Button removeWordBTN;
	@FXML
	private Text hiddenText;
	private Main mainApp;

	public void setMainApp(Main main){
		this.mainApp = main;
	}
	
	@FXML
	public void goMain(){
	mainApp.showMainView();
	}
	}
