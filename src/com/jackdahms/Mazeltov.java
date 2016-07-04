package com.jackdahms;

import java.util.ArrayList;

import com.jackdahms.depthfirstsearch.DepthFirstSearch;
import com.jackdahms.kruskals.Kruskals;
import com.jackdahms.prims.Prims;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class Mazeltov extends Application {
	
	static int SCENE_WIDTH = 1000;
	static int SCENE_HEIGHT = 600;
	static int CONTROL_PANEL_WIDTH = 200;
	
	static int width = 40;
	static int height = 30;
	static int wallThickness = 1;
	
	static Maze maze;
	
	static Canvas canvas;
	GraphicsContext g;
	
	Generator selectedGenerator;
	ArrayList<Generator> generators = new ArrayList<Generator>();
	
	/**
	 * TODO
	 * start and stop can move but dont matter programmatically
	 * solutions dropdown
	 * potentially get rid of ability to move start and finish
	 * vbox instead of gridpane?
	 * spinners not friendly with user-typed input
	 */
	
	private void repaint(long currentNanoTime) {
		g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		g.drawImage(maze.image, 0, 0, canvas.getWidth(), canvas.getHeight());
	}

	public static void main(String[] args) {
		maze = new Maze(width, height, SCENE_WIDTH - CONTROL_PANEL_WIDTH, SCENE_HEIGHT);
		setMazeProperties();
		maze.mapToImage();
		launch(args);
	}
	
	/**
	 * Sets the properties for the maze
	 */
	public static void setMazeProperties() {
		maze.setWidth(width);
		maze.setHeight(height);
		maze.wallThickness = wallThickness;
	}
	
	public void start(Stage stage) {
		GridPane pane = new GridPane();
		pane.setAlignment(Pos.TOP_RIGHT);
		canvas = new Canvas(SCENE_WIDTH - CONTROL_PANEL_WIDTH, SCENE_HEIGHT);
		canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> maze.pressed(e.getX(), e.getY()));
		canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> maze.dragged(e.getX(), e.getY()));
		canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> maze.released(e.getX(), e.getY()));
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
		controlGrid.setId("control-panel");
		int labelColumnWidth = 100;
		controlGrid.getColumnConstraints().add(new ColumnConstraints(labelColumnWidth));
		controlGrid.getColumnConstraints().add(new ColumnConstraints(CONTROL_PANEL_WIDTH - labelColumnWidth - 15)); //15 = 5 padding + 5 vgap + 5 padding
		pane.add(controlGrid, 1, 0);
				
		Label widthLabel = new Label("WIDTH");
		controlGrid.add(widthLabel, 0, 0);
		
		Spinner<Integer> widthSpinner = new Spinner<Integer>(1, 1000, width, 10);
		widthSpinner.setEditable(true);
		widthSpinner.valueProperty().addListener(observable -> width = widthSpinner.getValue());
		controlGrid.add(widthSpinner, 1, 0);

		Label heightLabel = new Label("HEIGHT");
		controlGrid.add(heightLabel, 0, 1);

		Spinner<Integer> heightSpinner = new Spinner<Integer>(1, 750, height, 10);
		heightSpinner.setEditable(true);
		heightSpinner.valueProperty().addListener(observable -> height = heightSpinner.getValue());
		controlGrid.add(heightSpinner, 1, 1);
		
		Label wallThicknessLabel = new Label("WALL THICKNESS");
		controlGrid.add(wallThicknessLabel, 0, 2);
		
		Spinner<Integer> wallThicknessSpinner = new Spinner<Integer>(1, 10, wallThickness, 1);
		wallThicknessSpinner.setEditable(true);
		wallThicknessSpinner.valueProperty().addListener(observable -> wallThickness = wallThicknessSpinner.getValue());
		controlGrid.add(wallThicknessSpinner, 1, 2);
		
		Label generatorLabel = new Label("GENERATOR");
		controlGrid.add(generatorLabel, 0, 3);

		generators.add(new DepthFirstSearch());
		generators.add(new Kruskals());
		generators.add(new Prims());
		selectedGenerator = generators.get(0);
		
		ChoiceBox<Generator> generatorBox = new ChoiceBox<Generator>();
		generatorBox.getItems().addAll(generators);
		generatorBox.setValue(selectedGenerator);
		generatorBox.getSelectionModel().selectedItemProperty().addListener(observable -> selectedGenerator = generatorBox.getSelectionModel().getSelectedItem());
		generatorBox.setMinWidth(CONTROL_PANEL_WIDTH - 10); //10 = 5px padding * 2
		controlGrid.add(generatorBox, 0, 4, 2, 1);
		
		Button generateButton = new Button("GENERATE");
		generateButton.setMinWidth(190);
		generateButton.setOnAction(press -> {
			setMazeProperties();
			maze.generateMaze(selectedGenerator);
		});
		controlGrid.add(generateButton, 0, 5, 2, 1);
		
		Label solverLabel = new Label("SOLVER");
		controlGrid.add(solverLabel, 0, 6);
				
		Button solveButton = new Button("SOLVE");
		solveButton.setMinWidth(190);
		controlGrid.add(solveButton, 0, 8, 2, 1);
		
		Scene scene = new Scene(pane, SCENE_WIDTH, SCENE_HEIGHT);
		controlGrid.requestFocus(); //must be done after scene is constructed
		try {
			scene.getStylesheets().add(Mazeltov.class.getResource("/com/jackdahms/mazeltov.css").toExternalForm());
		} catch (Exception e) {
			System.err.println("Could not load stylesheet...");
		}
		
		InvalidationListener sizeListener = observable -> {
			pane.getChildren().remove(canvas);
			canvas = new Canvas(scene.getWidth() - CONTROL_PANEL_WIDTH, scene.getHeight());
			
			canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> maze.pressed(e.getX(), e.getY()));
			canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> maze.dragged(e.getX(), e.getY()));
			canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> maze.released(e.getX(), e.getY()));
			
			g = canvas.getGraphicsContext2D();
			pane.add(canvas, 0, 0);
			try {
				maze.resize((int)canvas.getWidth(), (int)canvas.getHeight());
			} catch (Exception e) {System.err.println("error in redrawing maze");}
			repaint(0);
		};
		
		scene.widthProperty().addListener(sizeListener);
		scene.heightProperty().addListener(sizeListener);
		stage.setTitle("Mazeltov");
		stage.setScene(scene);
		stage.show();
	}
}
