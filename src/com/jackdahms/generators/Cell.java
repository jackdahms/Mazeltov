package com.jackdahms.generators;

public class Cell {

	public int row, col;
	Generator gen;
	
	public Cell(int row, int col, Generator gen) {
		this.row = row; 
		this.col = col;
		this.gen = gen;
	}
	
	public void setVisited(boolean visited){
		if (visited) {
			gen.cells[row][col] = 1;
		} else {
			gen.cells[row][col] = 0;
		}
	}
	
	public boolean getVisited() {
		return gen.cells[row][col] == 1;
	}
	
	public boolean equals(Object obj) {
		Cell other = (Cell) obj;
		return row == other.row && col == other.col;
	}
	
}
