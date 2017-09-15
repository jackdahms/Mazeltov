package com.jackdahms.generators.depthfirstsearch;

import java.util.ArrayList;
import java.util.Stack;

import com.jackdahms.generators.Cell;
import com.jackdahms.generators.Generator;

public class DepthFirstSearch extends Generator {
		
	public void generate() {
		Cell[][] cells = new Cell[height][width];
		
		for (int i = 0; i < height; i++) {
			for (int k = 0; k < width; k++) {
				cells[i][k] = new Cell(i, k, this);
			}
		}
				
		Cell current = cells[starty][startx];
		current.setVisited(true);
		
		Stack<Cell> stack = new Stack<Cell>();
		
		do {
			ArrayList<Cell> neighbors = new ArrayList<Cell>();
			ArrayList<Cell> unvisitedNeighbors = new ArrayList<Cell>();
			
			int row = current.row;
			int col = current.col;
			
			if (row == 0) {
				neighbors.add(cells[row + 1][col]);
			} else if (row == height - 1) {
				neighbors.add(cells[row - 1][col]);
			} else {
				neighbors.add(cells[row + 1][col]);
				neighbors.add(cells[row - 1][col]);
			}
			
			if (col == 0) {
				neighbors.add(cells[row][col + 1]);
			} else if (col == width - 1) {
				neighbors.add(cells[row][col - 1]);
			} else {
				neighbors.add(cells[row][col + 1]);
				neighbors.add(cells[row][col - 1]);
			}
			
			for (Cell c : neighbors){
				if (!c.getVisited()) {
					unvisitedNeighbors.add(c);
				}
			}
			
			if (unvisitedNeighbors.size() == 0) { //if all neighbors visited
				current = stack.pop();
			} else { //if any unvisited neighbors
				Cell chosen = unvisitedNeighbors.get((int) (Math.random() * unvisitedNeighbors.size())); //select random neighbor
				
				if (chosen.col > current.col) { //neighbor on the right
					verticalWalls[current.row][current.col] = 0;
				} else if (chosen.col < current.col) { //neighbor on the left
					verticalWalls[chosen.row][chosen.col] = 0; 
				} else if (chosen.row < current.row) { //neighbor on top
					horizontalWalls[chosen.row][chosen.col] = 0;
				} else if (chosen.row > current.row) { //neighbor on bottom
					horizontalWalls[current.row][current.col] = 0;
				}
				
				stack.push(current);
				current = chosen;
				current.setVisited(true);
			}
			
		} while (!stack.empty());
	}
	
	public String toString() {
		return "Depth First Search";
	}
}
