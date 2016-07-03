package com.jackdahms.kruskals;

public class Set {

	Cell[] cells;
	
	Set(Cell first) {
		cells = new Cell[1];
		cells[0] = first;
	}
	
	Set(Cell[] cells) {
		this.cells = cells;
	}
	
	Set merge(Set other) {
		int size = cells.length + other.cells.length; //find size of new array
		Cell[] newSet = new Cell[size]; //instantiate new array
		for (int i = 0; i < cells.length; i++) { //read in original array
			newSet[i] = cells[i];
		}
		for (int i = cells.length; i < size; i++) { //read in other array
			newSet[i] = other.cells[i];
		}
		return new Set(newSet);
	}
	
//	boolean contains(int row, int col) { 
//		for (int i = 0; i < rows.length; i++) { 
//			if (rows[i] == row && cols[i] == col) { 
//				return true; 
//			} 
//		} 
//		return false; 
//	}
	
}
