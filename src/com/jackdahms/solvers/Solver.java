package com.jackdahms.solvers;

import java.util.List;

import com.jackdahms.Maze;

public abstract class Solver {

	public int[][] cells;
	public int[][] horizontalWalls;
	public int[][] verticalWalls;
	
	public List<Integer> visitedX;
	public List<Integer> visitedY;
	
	public List<Integer> pathX;
	public List<Integer> pathY;
		
	public int width;
	public int height;
	
	public int startx;
	public int starty;
	
	public int finishx;
	public int finishy;
	
	//cannot be in constructor because maze can change after solver is constructed
	public void updateProperties(Maze maze) {
		this.cells = maze.cells;
		this.horizontalWalls = maze.horizontalWalls;
		this.verticalWalls = maze.verticalWalls;
		
		this.visitedX = maze.visitedX;
		this.visitedY = maze.visitedY;
		
		this.pathX = maze.pathX;
		this.pathY = maze.pathY;
		
		this.width = maze.width;
		this.height = maze.height;
		
		this.startx = maze.startx;
		this.starty = maze.starty;
		
		this.finishx = maze.finishx;
		this.finishy = maze.finishy;
		
		visitedX.clear();
		visitedY.clear();
		pathX.clear();
		pathY.clear();
	}
	
	public abstract void solve();
	
}
