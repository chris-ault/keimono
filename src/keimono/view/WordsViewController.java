package keimono.view;

import java.io.IOException;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;

import javafx.scene.text.Text;
import javafx.scene.control.TextField;
import javafx.scene.control.ListView;

import keimono.Main;
import keimono.model.KeywordListHandler;

public class WordsViewController {
	@FXML
	private Button homeBTN;
	@FXML
	private TextField wordToAdd;
	@FXML
	private Button addWordBTN;
	@FXML
	private ListView<String> wordList;
	@FXML
	private Button removeWordBTN;
	@FXML
	private Text hiddenText;

	private Main mainApp;
	private KeywordListHandler listHandler;
	private ArrayList<String> klist = new ArrayList<String>();

	public void initialize(){
		try {
			listHandler = new KeywordListHandler();
		} catch (IOException e) {
			System.out.println("Problem in Keyword List Handler");
		}

		listKeywords();
		hiddenText.setVisible(false);
	}

	public void setMainApp(Main main){
		this.mainApp = main;
	}

	private void listKeywords() {
		klist = listHandler.getKeywordList();
		wordList.getItems().addAll(klist);
	}

	@FXML
	public void addKeyword(){

		if(!wordToAdd.getText().trim().equals("")){
			klist.add(wordToAdd.getText());
			wordList.getItems().add(wordToAdd.getText());
			wordToAdd.clear();
		} else {
			hiddenText.setText("Error: No keyword to add");
			hiddenText.setVisible(true);
		}

	}

	@FXML
	public void removeKeyword(){
		int selectedIndex = wordList.getSelectionModel().getSelectedIndex();
		if(selectedIndex >= 0){
			klist.remove(selectedIndex);
			wordList.getItems().remove(selectedIndex);
		} else {
			hiddenText.setText("Error: No keyword selected");
			hiddenText.setVisible(true);
		}
	}

	@FXML
	public void goMain(){
		updateKeywordFile();
		hiddenText.setText("Changes saved");
		hiddenText.setVisible(true);
		mainApp.showMainView();
	}

	private void updateKeywordFile(){
		listHandler.updateKeywordList(klist);
	}
}
