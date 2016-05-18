package com.jackdahms;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Mazeltov extends Application {

	public static void main(String[] args) {
		launch(args);
	}
	
	public void start(Stage stage) {
		GridPane grid = new GridPane();
		
		stage.setTitle("Mazeltov");
		stage.setScene(new Scene(new Group(), 1000, 600));
		stage.show();
	}

}
