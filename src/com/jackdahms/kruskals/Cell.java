package com.jackdahms.kruskals;

public class Cell {

	int row, col;
	
	Cell(int row, int col) {
		this.row = row; 
		this.col = col;
	}	
	
	public boolean equals(Object obj) {
		Cell other = (Cell) obj;
		return row == other.row && col == other.col;
	}
}
