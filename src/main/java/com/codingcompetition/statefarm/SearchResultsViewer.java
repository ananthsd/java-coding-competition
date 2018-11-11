package com.codingcompetition.statefarm;

import javafx.scene.Scene;
import javafx.application.Application;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class SearchResultsViewer extends Application {
    public void start(Stage stage) {
        Label basicSearchLabel = new Label("Basic Search");

        Label cityLabel = new Label("City");
        TextField cityField = new TextField();
        HBox cityBox = new HBox(cityLabel, cityField);

        Label stateLabel = new Label("State");
        TextField stateField = new TextField();
        HBox stateBox = new HBox(stateLabel, stateField);

        VBox basicSearchCriteriaBox = new VBox(cityBox, stateBox);

        Button submitBasicSearchButton = new Button("Submit");
        submitBasicSearchButton.setOnAction(event -> {
            // TODO basic search
        });

        VBox basicSearchBox = new VBox(basicSearchLabel,
                                    basicSearchCriteriaBox,
                                    submitBasicSearchButton);

        // Advanced Search

        Label advancedSearchLabel = new Label("Advanced Search");

        Label latitudeLabel = new Label("Latitude");
        TextField latitudeField = new TextField();
        HBox latitudeBox = new HBox(latitudeLabel, latitudeField);

        Label longitudeLabel = new Label("Longitude");
        TextField longitudeField = new TextField();
        HBox longitudeBox = new HBox(longitudeLabel, longitudeField);

        VBox advancedSearchCriteriaBox = new VBox(latitudeBox, longitudeBox);

        Button submitAdvancedSearchCriteriaButton = new Button("Submit");
        submitAdvancedSearchCriteriaButton.setOnAction(event -> {
            // TODO advanced search
        });

        VBox advancedSearchBox = new VBox(advancedSearchLabel,
                                    advancedSearchCriteriaBox,
                                    submitAdvancedSearchCriteriaButton);

        HBox mainBox = new HBox(basicSearchBox, advancedSearchBox);

        Scene s = new Scene(mainBox);

        stage.setScene(s);
        stage.show();



    }

    public static void main(String[] args) {
        launch(args);
    }

}
