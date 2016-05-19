package com.jackdahms;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Mazeltov extends Application {
	
	int SCENE_WIDTH = 1000;
	int SCENE_HEIGHT = 600;
	
	Canvas canvas;
	
	private void repaint(GraphicsContext g, long currentNanoTime) {
		g.setFill(Color.BLACK);
		g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	public void start(Stage stage) {
		GridPane grid = new GridPane();
		grid.setGridLinesVisible(true);
	
		canvas = new Canvas(400, 400);
		GraphicsContext g = canvas.getGraphicsContext2D();
		canvas.widthProperty().addListener(observable -> repaint(g, 0));
		canvas.heightProperty().addListener(observable -> repaint(g, 0));
		GridPane.setHgrow(canvas, Priority.ALWAYS);
		GridPane.setVgrow(canvas, Priority.ALWAYS);
		grid.add(canvas, 0, 0);
		new AnimationTimer() {
			public void handle(long currentNanoTime) {
				repaint(g, currentNanoTime);
			}
		}.start();
		
		Button b = new Button("hey");
		b.setOnAction(actionEvent -> System.out.println("hey"));
				
		Scene scene = new Scene(grid, SCENE_WIDTH, SCENE_HEIGHT);
		try {
			scene.getStylesheets().add(Mazeltov.class.getResource("/com/jackdahms/mazeltov.css").toExternalForm());
		} catch (Exception e) {
			System.err.println("Could not load stylesheet...");
		}
		stage.setTitle("Mazeltov");
		stage.setScene(scene);
		stage.show();
	}
}
