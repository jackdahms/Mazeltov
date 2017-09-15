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
	
	//can not be a constructor, because maze can change after generator is constructed
	public void updateProperties(Maze maze) {
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
