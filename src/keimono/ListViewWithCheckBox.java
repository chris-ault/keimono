package keimono;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Callback;

public class ListViewWithCheckBox extends Application {

	@FXML
	private Button nextBTN;
	@FXML
	private TextField siteToAdd;
	@FXML
	private Button addSiteBTN;
	@FXML
	private ListView<Site> siteList;
	@FXML
	private Button removeSiteBTN;
	
    @Override
    public void start(Stage primaryStage) {
        ListView<Site> siteList = new ListView<>();
        for (int i=1; i<=20; i++) {
            Site site = new Site("Item "+i, false);

            // observe item's on property and display message if it changes:
            site.onProperty().addListener((obs, wasOn, isNowOn) -> {
                System.out.println(site.getName() + " changed on state from "+wasOn+" to "+isNowOn);
            });

            siteList.getItems().add(site);
        }

        siteList.setCellFactory(CheckBoxListCell.forListView(new Callback<Site, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(Site site) {
                return site.onProperty();
            }
        }));

        BorderPane root = new BorderPane(siteList);
        Scene scene = new Scene(root, 250, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    	/*
    public static void main(String[] args) {
        launch(args);
    }	*/
}