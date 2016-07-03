package com.jackdahms.depthfirstsearch;

public class Cell {
	
	int row, col;
	DepthFirstSearch dfs;
	
	Cell(int row, int col, DepthFirstSearch dfs) {
		this.row = row; 
		this.col = col;
		this.dfs = dfs;
	}
	
	void setVisited(boolean visited){
		if (visited) {
			dfs.cells[row][col] = 1;
		} else {
			dfs.cells[row][col] = 0;
		}
	}
	
	boolean getVisited() {
		return dfs.cells[row][col] == 1;
	}

}
