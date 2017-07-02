package com.jackdahms.kruskals;

import com.jackdahms.Cell;

public class Set {

	Cell[] cells;
	
	Set() {
		cells = new Cell[1];
	}
	
	Set(Cell first) {
		cells = new Cell[1];
		cells[0] = first;
	}
	
	Set(Cell[] cells) {
		this.cells = cells;
	}
	
	//intended to be used immediately after calling constructor
	void add(Cell c) {
		cells[0] = c;
	}
	
	void merge(Set other) {
		int size = cells.length + other.cells.length; //find size of new array
		Cell[] newSet = new Cell[size]; //instantiate new array
		for (int i = 0; i < cells.length; i++) { //read in original array
			newSet[i] = cells[i];
		}
		for (int i = cells.length; i < size; i++) { //read in other array
			newSet[i] = other.cells[i - cells.length];
		}
		cells = newSet;
	}
	
	boolean contains(Cell c) { 
		for (int i = 0; i < cells.length; i++) { 
			if (cells[i].equals(c)) { 
				return true; 
			} 
		} 
		return false; 
	}
	
}
