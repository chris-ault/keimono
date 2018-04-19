package keimono.view;

import javafx.fxml.FXML;

import javafx.scene.control.Button;

import javafx.scene.control.ListView;
import keimono.main.Main;

public class mainWindowController {
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
	
}
