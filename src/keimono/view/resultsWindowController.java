package keimono.view;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.scene.control.Button;

import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableColumn;
import keimono.Main;
import keimono.model.*;

public class resultsWindowController {
	@FXML
	private Button homeButton;
	@FXML
	private Button detailsButtons;
	@FXML
	private TableView<Article> articleTable;
	@FXML
	private TableColumn<Article, String> titleColumn;
	@FXML
	private TableColumn<Article, Integer> hitColumn;

	ArrayList<Article> articles;
	private Main mainApp;
	
	   @FXML
	    private void initialize() {
	   articles = new ArrayList<Article>();
	   
		   
	   }

	public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
		
	}

	public void setArticles(ArrayList<Article> articles2) {
		this.articles = articles2;
	}
	
	public void listArticles(){
	   ObservableList<Article> oList = FXCollections.observableArrayList(articles);
		//oList.addAll(articles);
		articleTable.setItems(oList);
	   //articleTable.getItems().addAll(oList);
	   titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());

	   hitColumn.setCellValueFactory(cellData -> cellData.getValue().hitcountProperty().asObject());
	    //articleTable.getColumns().addAll(titleColumn,hitColumn);
		System.out.println(articles);
	}
	public void goHome(){
		mainApp.showMainView();
		
	}
	@FXML
	public void showDetails(){
		
	}
	
}
