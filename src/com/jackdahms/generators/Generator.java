package com.jackdahms.generators;

import com.jackdahms.Maze;

public abstract class Generator {
	
	public int[][] cells;
	public int[][] horizontalWalls;
	public int[][] verticalWalls;
		
	public int width;
	public int height;
	
	public int startx;
	public int starty;
	
	public void setAllProperties(Maze maze) {
		this.cells = maze.cells;
		this.horizontalWalls = maze.horizontalWalls;
		this.verticalWalls = maze.verticalWalls;
		
		this.width = maze.width;
		this.height = maze.height;
		
		this.startx = maze.startx;
		this.starty = maze.starty;
	}
	
	public abstract void generate();

}
