package com.jackdahms.prims;

import java.util.ArrayList;
import java.util.function.Consumer;

import com.jackdahms.Cell;
import com.jackdahms.Generator;

public class Prims extends Generator {

	public void generate() {
		ArrayList<Wall> walls = new ArrayList<Wall>();
		
		Consumer<Wall> addWall = wall -> {
			if (!wall.getPassage() && !walls.contains(wall)) walls.add(wall);
		};
		
		Cell[][] cells = new Cell[height][width];
		
		for (int i = 0; i < height; i++) {
			for (int k = 0; k < width; k++) {
				cells[i][k] = new Cell(i, k, this);
			}
		}
		
		Cell current = new Cell(starty, startx, this);
		
		do {
			if (!current.getVisited()) {
				int row = current.row;
				int col = current.col;
				
				//adds walls to list 
				if (row == 0) {
					addWall.accept(new Wall(row, col, true, this));
				} else if (row == height - 1) {
					addWall.accept(new Wall(row - 1, col, true, this));
				} else {
					addWall.accept(new Wall(row, col, true, this));
					addWall.accept(new Wall(row - 1, col, true, this));
				}
				
				if (col == 0) {
					addWall.accept(new Wall(row, col, false, this));
				} else if (col == width - 1) {
					addWall.accept(new Wall(row, col - 1, false, this));
				} else {
					addWall.accept(new Wall(row, col, false, this));
					addWall.accept(new Wall(row, col - 1, false, this));
				}
			}
			current.setVisited(true);
			//get random wall
			Wall wall = walls.get((int)(walls.size() * Math.random()));
			//get the two cells it divides
			Cell a = cells[wall.row][wall.col];
			Cell b = wall.horizontal ? cells[wall.row + 1][wall.col] : cells[wall.row][wall.col + 1];
			
			if (a.getVisited() && b.getVisited()) { //if both are visited
				walls.remove(wall);
			} else { //if one is visited
				//clear the wall
				wall.setPassage(true);
				//visit the unvisited cell
				if (a.getVisited()) {
					current = b;
				} else {
					current = a;
				}	
			}			
		} while (walls.size() > 0);
	}
	
	public String toString() {
		return "Classical Prim's Algorithm";
	}

}
