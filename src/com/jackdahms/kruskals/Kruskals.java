package com.jackdahms.kruskals;

import java.util.ArrayList;
import java.util.Collections;

import com.jackdahms.Cell;
import com.jackdahms.Generator;

public class Kruskals extends Generator {
		
	public void generate() {
		//create a list of all walls in random order
		ArrayList<Wall> walls = new ArrayList<Wall>();
		for (int i = 0; i < height - 1; i++) {
			for (int k = 0; k < width - 1; k++) {
				walls.add(new Wall(i, k, true));
				walls.add(new Wall(i, k, false));
			}
		}
		for (int i = 0; i < height - 1; i++) {
			walls.add(new Wall(i, width - 1, true));
		}
		for (int k = 0; k < width - 1; k++) {
			walls.add(new Wall(height - 1, k, false));
		}
		Collections.shuffle(walls); //shake it up
		
		//create a set for each cell
		ArrayList<Set> sets = new ArrayList<Set>();
		Cell[][] cells = new Cell[height][width];
		for (int i = 0; i < height; i++) {
			for (int k = 0; k < width; k++) {
				Cell c = new Cell(i, k, this); //create cell
				cells[i][k] = c; //add cell to array
				sets.add(new Set(c)); //add set to setlist
			}
		}
		
		//for each wall in a random order
		for (Wall wall : walls) {
			if (wall.horizontal) {
				//the cells to attempt to join
				Cell cellA = new Cell(wall.row, wall.col, this);
				Cell cellB = new Cell(wall.row + 1, wall.col, this);
				Set setA = null; //the set that contains A
				Set setB = null;
				for (Set s : sets) {
					if (s.contains(cellA)) {
						setA = s;
					}
					if (s.contains(cellB)) {
						setB = s;
					}
				}
				if (!setA.equals(setB)) { //if cellA and cellB are in distinct sets
					horizontalWalls[wall.row][wall.col] = 0; //remove wall
					setA.merge(setB);
					sets.remove(setB);
				}
			} else { //vertical wall
				Cell cellA = new Cell(wall.row, wall.col, this);
				Cell cellB = new Cell(wall.row, wall.col + 1, this);
				Set setA = null; //the set that contains A
				Set setB = null;
				for (Set s : sets) {
					if (s.contains(cellA)) {
						setA = s;
					}
					if (s.contains(cellB)) {
						setB = s;
					}
				}
				if (!setA.equals(setB)) { //if cellA and cellB are in distinct sets
					verticalWalls[wall.row][wall.col] = 0; //remove wall
					setA.merge(setB);
					sets.remove(setB);
				}
			}
		}
	}
	
	public String toString() {
		return "Kruskal's Algorithm";
	}

}
