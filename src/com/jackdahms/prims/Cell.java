package com.jackdahms.prims;

public class Cell {
	
	int row, col;
	Prims gen;
	
	Cell(int row, int col, Prims gen) {
		this.row = row; 
		this.col = col;
		this.gen = gen;
	}
	
	void setVisited(boolean visited){
		if (visited) {
			gen.cells[row][col] = 1;
		} else {
			gen.cells[row][col] = 0;
		}
	}
	
	boolean getVisited() {
		return gen.cells[row][col] == 1;
	}
	
	public boolean equals(Object obj) {
		Cell other = (Cell) obj;
		return row == other.row && col == other.col;
	}

}
