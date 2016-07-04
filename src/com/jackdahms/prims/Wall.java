package com.jackdahms.prims;

public class Wall {
	
	int row, col;
	boolean horizontal;
	boolean passage; //true if allows for passage
	Prims gen;
	
	Wall(int row, int col, boolean horizontal, Prims gen) {
		this.row = row;
		this.col = col;
		this.horizontal = horizontal;
		this.passage = false;
		this.gen = gen;
	}
	
	void setPassage(boolean passage) {
		this.passage = passage;
		if (horizontal) {
			if (passage) gen.horizontalWalls[row][col] = 0;
			else gen.horizontalWalls[row][col] = 1;
		} else {
			if (passage) gen.verticalWalls[row][col] = 0;
			else gen.verticalWalls[row][col] = 1;
		}
	}
	
	boolean getPassage() {
		return passage;
	}
	
	public boolean equals(Object obj) {
		Wall other = (Wall) obj;
		return horizontal == other.horizontal && col == other.col && row == other.row;
	}

}
