package keimono.view;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;

import keimono.Main;

public class RootLayoutController {

	private Main mainApp;

	public void setMainApp(Main main){
		this.mainApp = main;
	}

	// Event Listener on MenuItem.onAction
	@FXML
	public void editKeywords(ActionEvent event) {
		mainApp.showEditKeywords();
	}
}
