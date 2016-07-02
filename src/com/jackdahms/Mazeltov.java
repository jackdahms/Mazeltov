package com.jackdahms;

import java.util.ArrayList;
import java.util.Stack;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.input.MouseEvent;
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
	
	static ArrayList<Generatable> rulesets = new ArrayList<Generatable>();
	
	/**
	 * TODO
	 * generations dropdown
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
		setPropertiesAndDraw();
		
		rulesets.add(() -> {			
			class Cell {
				int row, col;
				
				Cell(int row, int col) {
					this.row = row; 
					this.col = col;
				}
				
				void setVisited(boolean visited){
					if (visited) {
						maze.cells[row][col] = 1;
					} else {
						maze.cells[row][col] = 0;
					}
				}
				
				boolean getVisited() {
					return maze.cells[row][col] == 1;
				}
								
			}

			Cell[][] cells = new Cell[maze.height][maze.width];
						
			for (int i = 0; i < maze.height; i++) {
				for (int k = 0; k < maze.width; k++) {
					cells[i][k] = new Cell(i, k);
				}
			}
		
			BiFunction<Integer, Integer, ArrayList<Cell>> getNeighbors = (row, col) -> {
				ArrayList<Cell> neighbors = new ArrayList<Cell>();
				try {neighbors.add(cells[row - 1][col]);} catch (Exception e) {/*neighbor out of bounds*/}
				try {neighbors.add(cells[row + 1][col]);} catch (Exception e) {/*neighbor out of bounds*/}
				try {neighbors.add(cells[row][col - 1]);} catch (Exception e) {/*neighbor out of bounds*/}
				try {neighbors.add(cells[row][col + 1]);} catch (Exception e) {/*neighbor out of bounds*/}
				return neighbors;
			};
			
			Cell current = cells[maze.starty][maze.startx];
			current.setVisited(true);
			
			//TODO backtrack with cells, not stack
			Stack<Cell> stack = new Stack<Cell>();
			
			do {
				ArrayList<Cell> unvisitedNeighbors = new ArrayList<Cell>();
				for (Cell c : getNeighbors.apply(current.row, current.col)){
					if (!c.getVisited()) {
						unvisitedNeighbors.add(c);
					}
				}
				
				if (unvisitedNeighbors.size() == 0) { //if all neighbors visited
					current = stack.pop();
				} else { //if any unvisited neighbors
					Cell chosen = unvisitedNeighbors.get((int) (Math.random() * unvisitedNeighbors.size()));
					
					if (chosen.col > current.col) { //neighbor on the right
						maze.verticalWalls[current.row][current.col] = 0;
					} else if (chosen.col < current.col) { //neighbor on the left
						maze.verticalWalls[chosen.row][chosen.col] = 0; 
					} else if (chosen.row < current.row) { //neighbor on top
						maze.horizontalWalls[chosen.row][chosen.col] = 0;
					} else if (chosen.row > current.row) { //neighbor on bottom
						maze.horizontalWalls[current.row][current.col] = 0;
					}
					
					stack.push(current);
					current = chosen;
					current.setVisited(true);
				}
				
			} while (!stack.empty());
			
		});
		
		launch(args);
	}
	
	/**
	 * Sets the properties for the maze and draws it
	 */
	public static void setPropertiesAndDraw() {
		maze.setWidth(width);
		maze.setHeight(height);
		maze.wallThickness = wallThickness;
		maze.mapToImage();
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
		pane.add(controlGrid, 1, 0);
				
		Label widthLabel = new Label("WIDTH");
		controlGrid.add(widthLabel, 0, 0);
		
		Spinner<Integer> widthSpinner = new Spinner<Integer>(1, 8000, width, 10); //TODO decide on width min/max
		widthSpinner.setEditable(true);
		widthSpinner.valueProperty().addListener(observable -> width = widthSpinner.getValue());
		controlGrid.add(widthSpinner, 1, 0);

		Label heightLabel = new Label("HEIGHT");
		controlGrid.add(heightLabel, 0, 1);

		Spinner<Integer> heightSpinner = new Spinner<Integer>(1, 8000, height, 10); //TODO decide on height min/max 
		heightSpinner.setEditable(true);
		heightSpinner.valueProperty().addListener(observable -> height = heightSpinner.getValue());
		controlGrid.add(heightSpinner, 1, 1);
		
		Label wallThicknessLabel = new Label("WALL WIDTH");
		controlGrid.add(wallThicknessLabel, 0, 2);
		
		Spinner<Integer> wallThicknessSpinner = new Spinner<Integer>(1, 10, wallThickness, 1);
		wallThicknessSpinner.setEditable(true);
		wallThicknessSpinner.valueProperty().addListener(observable -> wallThickness = wallThicknessSpinner.getValue());
		controlGrid.add(wallThicknessSpinner, 1, 2);
		
		Label generatorLabel = new Label("GENERATOR");
		controlGrid.add(generatorLabel, 0, 3);
		

		Button generateButton = new Button("GENERATE");
		generateButton.setMinWidth(190);
		generateButton.setOnAction(press -> {
			setPropertiesAndDraw();
			maze.generateMaze(rulesets.get(0));
		});
		controlGrid.add(generateButton, 0, 4, 2, 1);
		
		Label solverLabel = new Label("SOLVER");
		controlGrid.add(solverLabel, 0, 5);
				
		Button solveButton = new Button("SOLVE");
		solveButton.setMinWidth(190);
		controlGrid.add(solveButton, 0, 6, 2, 1);
		
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
