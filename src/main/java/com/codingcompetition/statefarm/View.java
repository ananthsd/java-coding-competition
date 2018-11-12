package com.codingcompetition.statefarm;

import com.sothawo.mapjfx.Coordinate;
import com.sothawo.mapjfx.MapView;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.application.Application;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class View extends Application {
    public void start(Stage stage) {
        Label basicSearchLabel = new Label("Basic Search");

        Label cityLabel = new Label("City           ");
        TextField cityField = new TextField();
        HBox cityBox = new HBox(cityLabel, cityField);
        cityBox.setSpacing(5);

        Label stateLabel = new Label("State         ");
        TextField stateField = new TextField();
        HBox stateBox = new HBox(stateLabel, stateField);
        stateBox.setSpacing(5);

        VBox basicSearchCriteriaBox = new VBox(cityBox, stateBox);
        basicSearchCriteriaBox.setSpacing(5);

        Button submitBasicSearchButton = new Button("Submit");


        VBox basicSearchBox = new VBox(basicSearchLabel,
                                    basicSearchCriteriaBox,
                                    submitBasicSearchButton);
        basicSearchBox.setSpacing(5);

        // Advanced Search

        Label advancedSearchLabel = new Label("Advanced Search");

        Label latitudeLabel = new Label("Latitude    ");
        TextField latitudeField = new TextField();
        HBox latitudeBox = new HBox(latitudeLabel, latitudeField);
        latitudeBox.setSpacing(5);

        Label longitudeLabel = new Label("Longitude ");
        TextField longitudeField = new TextField();
        HBox longitudeBox = new HBox(longitudeLabel, longitudeField);
        longitudeBox.setSpacing(5);

        VBox advancedSearchCriteriaBox = new VBox(latitudeBox, longitudeBox);
        advancedSearchCriteriaBox.setSpacing(5);

        Button submitAdvancedSearchCriteriaButton = new Button("Submit");


        VBox advancedSearchBox = new VBox(advancedSearchLabel,
                                    advancedSearchCriteriaBox,
                                    submitAdvancedSearchCriteriaButton);
        advancedSearchBox.setSpacing(5);

        VBox searchBox = new VBox(basicSearchBox, advancedSearchBox);
        searchBox.setSpacing(40);

        // BorderPane b = new BorderPane();
        MapView m = new MapView();
        m.setAnimationDuration(500);
        // b.setCenter(m);
        m.setCenter(new Coordinate(33.7488889D, -84.3880556D));
        m.setZoom(10D);
        m.setMinSize(700D, 800D);
        m.initialize();
        ViewController.cacheMapOffline(m);
        HBox mainBox = new HBox(searchBox, m);
        mainBox.setSpacing(10);
        mainBox.setPadding(new Insets(10, 10, 10, 10));


        submitBasicSearchButton.setOnAction(event -> {
            ViewController.performBasicSearch(cityField.getText(), stateField.getText(), m);
        });

        submitAdvancedSearchCriteriaButton.setOnAction(event -> {

            ViewController.performAdvancedSearch(Double.parseDouble(latitudeField.getText()),
                    Double.parseDouble(longitudeField.getText()), m);



        });

        m.setOnZoom(event -> {
            ViewController.refreshMarkers(m);
        });

        Scene s = new Scene(mainBox);

        stage.setTitle("Risk Prediction");
        stage.setScene(s);
        stage.show();





    }

    public static void main(String[] args) {
        launch(args);
    }

}
