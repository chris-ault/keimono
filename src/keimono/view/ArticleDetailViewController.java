package keimono.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.application.HostServices;

import keimono.model.Article;

public class ArticleDetailViewController {
	@FXML
	private TextArea articleText;
	@FXML
	private Label title;
	@FXML
	private ListView<String> keywordsList;
	@FXML
	private Button hyperlinkBtn;
	@FXML
	private Button closeBtn;

	private Stage dialogStage;
	private Article article;
	private HostServices hostServices;

	public void initialize(){

	}

	public void setDialogStage(Stage stage){
		this.dialogStage = stage;
	}

	public void setArticle(Article a){
		this.article = a;
	}

	public void setHostServices(HostServices host){
		this.hostServices = host;
	}

	public void displayKeywords(){
		keywordsList.getItems().addAll(article.getKeywords());
	}

	public void displayTitle(){
		title.setText("Details - " + article.getTitle());
	}

	public void displayArticleText(){
		articleText.setText(article.getText());
	}

	public void closeWindow(){
		dialogStage.close();
	}

	public void goToWebsite(){
		hostServices.showDocument(article.getURL());
	}

}
