package com.jackdahms;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class Mazeltov extends Application {
	
	static int SCENE_WIDTH = 1000;
	static int SCENE_HEIGHT = 600;
	
	static int width = 40;
	static int height = 30;
	static int wallThickness = 6;
	
	static Maze maze;
	
	static Canvas canvas;
	GraphicsContext g;
	
	private void repaint(long currentNanoTime) {
		g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		g.drawImage(maze.image, 0, 0, canvas.getWidth(), canvas.getHeight());
	}

	public static void main(String[] args) {
		maze = new Maze(width, height, SCENE_WIDTH - 200, SCENE_HEIGHT);
		maze.wallThickness = wallThickness;
		maze.mapToImage();
		launch(args);
	}
	
	public void start(Stage stage) {
		GridPane pane = new GridPane();
		pane.setAlignment(Pos.TOP_RIGHT);
		canvas = new Canvas(SCENE_WIDTH - 200, SCENE_HEIGHT);
		g = canvas.getGraphicsContext2D();
		GridPane.setHgrow(canvas, Priority.ALWAYS);
		GridPane.setVgrow(canvas, Priority.ALWAYS);
		pane.add(canvas, 0, 0);
		
		new AnimationTimer() {
			public void handle(long currentNanoTime) {
				repaint(currentNanoTime);
			}
		}.start();
		
		GridPane controlGrid = new GridPane();
		controlGrid.setHgap(5);
		controlGrid.setVgap(5);
		controlGrid.setPadding(new Insets(5, 5, 5, 5));
		controlGrid.setMinWidth(180);
		pane.add(controlGrid, 1, 0);
		
		Label widthLabel = new Label("WIDTH");
		widthLabel.setMinWidth(90);
		controlGrid.add(widthLabel, 0, 0);
		
		Spinner<Integer> widthSpinner = new Spinner<Integer>(1, 8000, width, 100); //TODO decide on width min/max
		widthSpinner.setEditable(true);
		widthSpinner.valueProperty().addListener(observable -> width = widthSpinner.getValue());
		controlGrid.add(widthSpinner, 1, 0);

		Label heightLabel = new Label("HEIGHT");
		controlGrid.add(heightLabel, 0, 1);

		Spinner<Integer> heightSpinner = new Spinner<Integer>(1, 8000, height, 100); //TODO decide on height min/max 
		heightSpinner.setEditable(true);
		heightSpinner.valueProperty().addListener(observable -> height = heightSpinner.getValue());
		controlGrid.add(heightSpinner, 1, 1);
		
		Label wallThicknessLabel = new Label("WALL THICKNESS");
		controlGrid.add(wallThicknessLabel, 0, 2);
		
		Spinner<Integer> wallThicknessSpinner = new Spinner<Integer>(1, 10, wallThickness, 1);
		wallThicknessSpinner.setEditable(true);
		wallThicknessSpinner.valueProperty().addListener(observable -> wallThickness = wallThicknessSpinner.getValue());
		wallThicknessSpinner.setMaxWidth(70);
		controlGrid.add(wallThicknessSpinner, 1, 2);
		
		Scene scene = new Scene(pane, SCENE_WIDTH, SCENE_HEIGHT);
		controlGrid.requestFocus(); //must be done after scene is constructed
		try {
			scene.getStylesheets().add(Mazeltov.class.getResource("/com/jackdahms/mazeltov.css").toExternalForm());
		} catch (Exception e) {
			System.err.println("Could not load stylesheet...");
		}
		
		InvalidationListener sizeListener = new InvalidationListener() {
			public void invalidated(Observable arg0) {
				pane.getChildren().remove(canvas);
				canvas = new Canvas(scene.getWidth() - 200, scene.getHeight());
				g = canvas.getGraphicsContext2D();
				pane.add(canvas, 0, 0);
				try {
					maze.resize((int)canvas.getWidth(), (int)canvas.getHeight());
				} catch (Exception e) {System.err.println("error in redrawing maze");}
				repaint(0);
				
			}			
		};
		
		//could have used lambdas here but I didn't want to retype the exact same code
		scene.widthProperty().addListener(sizeListener);
		scene.heightProperty().addListener(sizeListener);
		stage.setTitle("Mazeltov");
		stage.setScene(scene);
		stage.show();
	}
}
