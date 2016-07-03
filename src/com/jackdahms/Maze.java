package com.jackdahms;

import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class Maze {
	
	int width, height;
	int imageWidth, imageHeight;
	int wallThickness;
	//coordinates of the start, finish, and cursor
	int startx, starty;
	int finishx, finishy;
	int cursorx, cursory;
	
	//whether the start or finish are pressed to be moved
	boolean startPressed;
	boolean finishPressed;
	
	Color startColor;
	Color finishColor;
		
	int[][] horizontalWalls;
	int[][] verticalWalls;
	int[][] cells;
	
	double cellWidth, cellHeight;
	
	WritableImage image;
	PixelWriter writer;
	
	/**
	 * Create a new maze.
	 * @param width			number of cells wide to make maze
	 * @param height		number of cells high to make maze
	 * @param imageWidth	width in pixels of image
	 * @param imageHeight	height in pixels of image
	 */
	public Maze(int width, int height, int imageWidth, int imageHeight) {
		this.width = width;
		this.height = height;
		this.imageWidth = imageWidth;
		this.imageHeight = imageHeight;
		wallThickness = 1;
		startx = 0;
		starty = 0;
		finishx = width - 1;
		finishy = height - 1;
		
		startColor = new Color(0, 1, 0, 0.5);
		finishColor = new Color(1, 0, 0, 0.5);
		
		image = new WritableImage(width, height);
		
		generateWalls();
	}
		
	public void mapToImage() {
		image = new WritableImage(imageWidth, imageHeight);
		writer = image.getPixelWriter();
		
		cellWidth = (double)imageWidth / (double)width;
		cellHeight = (double)imageHeight / (double)height;
		
		//draw start cell
		for (int y = (int) (starty * cellHeight); y < starty * cellHeight + cellHeight; y++) {
			for (int x = (int) (startx * cellWidth); x < startx * cellWidth + cellWidth; x++) {
				writer.setColor(x, y, startColor);
			}
		}
		
		//draw finish cell
		for (int y = (int) (finishy * cellHeight); y < finishy * cellHeight + cellHeight; y++) {
			for (int x = (int) (finishx * cellWidth); x < finishx * cellWidth + cellWidth; x++) {
				try {writer.setColor(x, y, finishColor); } catch (Exception e) {/*prevents occasional rounding errors*/}
//				writer.setColor(x, y, finishColor); //problem with size 66 TODO wtf is the problem here
			}
		}
		
		//draw north and south borders. divide by 2 add one to draw half of wall
		for (int y = 0; y < wallThickness / 2 + 1; y++) {
			for (int x = 0; x < imageWidth; x++) {
				writer.setColor(x, y, Color.BLACK);
				writer.setColor(x, imageHeight - 1 - y, Color.BLACK);
			}
		}
		//draw east and west borders. divide by 2 add one to draw half of wall
		for (int x = 0; x < wallThickness / 2 + 1; x++) {
			for (int y = 0; y < imageHeight; y++) {
				writer.setColor(x, y, Color.BLACK);
				writer.setColor(imageWidth - 1 - x, y, Color.BLACK);
			}
		}
		
		//for each row of horizontal walls
		for (int i = 0; i < horizontalWalls.length; i++) {
			//y values to draw at based on row
			int y1 = (int)((i + 1) * cellHeight - wallThickness / 2); //top of the wall
			int y2 = (int)((i + 1) * cellHeight + wallThickness / 2); //bottom of the wall
			//for each horizontal wall within the row
			for (int k = 0; k < horizontalWalls[i].length; k++) {
				//if the wall is valid
				if (horizontalWalls[i][k] == 1) {
					//x position to start drawing wall at
					int x = (int)(k * cellWidth);
					int x1 = x - wallThickness / 2;
					int x2 = x + (int) (cellWidth + wallThickness / 2); 
					//w for width! add one to the loop so it draws the same thickness as inputed
					for (int w = y1; w < y2 + 1; w++) {
						//l for length!
						for (int l = x1; l < x2 + 1; l++) {
							try {
								writer.setColor(l, w, Color.BLACK);
							} catch (Exception e) {/*in case walls spill over border*/}
						}
					}
					
				}
			}
		}
		
		//for each row of vertical walls
		for (int row = 0; row < verticalWalls.length; row++) {
			//y value based on row
			int y = (int)(row * cellHeight);
			//for each vertical wall within the row
			for (int column = 0; column < verticalWalls[row].length; column++) {
				//if the wall is valid
				if (verticalWalls[row][column] == 1) {
					int x1 = (int)((column + 1) * cellWidth - wallThickness / 2); //left of the wall
					int x2 = (int)((column + 1) * cellWidth + wallThickness / 2); //right of the wall
					//w for width! add one to the loop so it draws the same thickness as inputed
					for (int w = x1; w < x2 + 1; w++) {
						for (int length = -wallThickness / 2; length < cellHeight + wallThickness / 2; length++) {
							try {
								writer.setColor(w, y + length, Color.BLACK);
							} catch (Exception e) {/*in case wall spills over border*/}
						}
					}
				}
			}
		}
	}
	
	public void resize(int imageWidth, int imageHeight) {
		this.imageWidth = imageWidth;
		this.imageHeight = imageHeight;
		
		mapToImage();
	}
	
	/**
	 * Given an (x, y) coordinate pair, this will convert the position in pixels to coordinates in the mazes grid and store them in (cursorx, cursory)
	 * @param x the x position in pixels
	 * @param y the y position in pixels
	 */
	public void cursorPositionToCoords(double x, double y) {
		//convert points to coords
		cursorx = (int) (x / cellWidth);
		cursory = (int) (y / cellHeight);
		//bounds checking
		if (cursorx >= width) cursorx = width - 1;
		else if (cursorx < 0) cursorx = 0;
		if (cursory >= height) cursory = height - 1;
		else if (cursory < 0) cursory = 0;
	}
	

	public void generateWalls() {
		//both initialized to zero
		cells = new int[height][width]; //[rows][columns]
		horizontalWalls = new int[height - 1][width]; //subtract one to account for border
		verticalWalls = new int[height][width - 1];
		
		//so we set everything to one
		for (int i = 0; i < height - 1; i++) {
			for (int k = 0; k < width - 1; k++) {
				horizontalWalls[i][k] = 1;
				verticalWalls[i][k] = 1;
			}
		}
		for (int i = 0; i < height - 1; i++) {
			horizontalWalls[i][width - 1] = 1;
		}
		for (int k = 0; k < width - 1; k++) {
			verticalWalls[height - 1][k] = 1;
		}
	}
	
	public void generateMaze(Generator g) {
		g.setAllProperties(this);
		g.generate();
		cells = g.cells;
		horizontalWalls = g.horizontalWalls;
		verticalWalls = g.verticalWalls;
		mapToImage();
	}
	
	public void pressed(double x, double y) {
		cursorPositionToCoords(x, y);
		if (cursorx == startx && cursory == starty) {
			startPressed = true;
		} else if (cursorx == finishx && cursory == finishy) {
			finishPressed = true;
		}
	}
	
	public void dragged(double x, double y) {
		cursorPositionToCoords(x, y);
		if (startPressed) {
			if (cursorx != finishx || cursory != finishy) {
				startx = cursorx;
				starty = cursory;
				mapToImage();
			}
		} else if (finishPressed) {
			if (cursorx != startx || cursory != starty) {
				finishx = cursorx;
				finishy = cursory;
				mapToImage();
			}
		}
	}
	
	public void released(double x, double y) {
		startPressed = false;
		finishPressed = false;
	}
	
	public void setWidth(int width) {
		this.width = width;
		finishx = width - 1;
		
		generateWalls();
	}
	
	public void setHeight(int height) {
		this.height = height;
		finishy = height - 1;
		
		generateWalls();
	}
	

}
