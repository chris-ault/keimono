package keimono;

import javafx.fxml.FXML;

import javafx.scene.control.Button;

import javafx.scene.text.Text;

import javafx.scene.control.TextField;

import javafx.scene.control.ListView;

public class wordsViewController {
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
	private Main main;

	public void setMainApp(Main main) {
        this.main = main;
        //patientTable.setItems(main.getPatientData());
        }
	
	
	
}
