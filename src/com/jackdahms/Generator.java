package com.jackdahms;

public abstract class Generator {
	
	public int[][] cells;
	public int[][] horizontalWalls;
	public int[][] verticalWalls;
		
	public int width;
	public int height;
	
	public void setAllProperties(Maze maze) {
		this.cells = maze.cells;
		this.horizontalWalls = maze.horizontalWalls;
		this.verticalWalls = maze.verticalWalls;
		this.width = maze.width;
		this.height = maze.height;
	}
	
	public abstract void generate();

}
