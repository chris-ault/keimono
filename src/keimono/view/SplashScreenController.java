package keimono.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class SplashScreenController {
	@FXML
	private Label status;
	@FXML
	private Label error;
	@FXML
	private Button quitBtn;
	@FXML
	private ProgressBar progressBar;

	public void initialize(){

		quitBtn.setVisible(false);
		error.setVisible(false);
		status.setVisible(true);
		status.setText("Starting Program");

	}

	public void setStatus(String text){
		status.setText(text);
	}

	public void setProgressBar(double prog){
		progressBar.setProgress(prog);
	}

	public void errorDected(String text){
		status.setText(status.getText() + "failed");
		error.setVisible(true);
		error.setText("Error: " + text);
		quitBtn.setVisible(true);
	}

	@FXML
	private void closeProgram(ActionEvent e){
		System.exit(0);
	}


}
