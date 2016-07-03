package com.jackdahms.kruskals;

import java.util.ArrayList;
import java.util.Collections;

import com.jackdahms.Generator;

public class Kruskals extends Generator {
	
	int width;
	int height;
	
	public void generate() {
		ArrayList<Wall> walls = new ArrayList<Wall>();
		//add all walls to a list
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
		Cell[][] cells = new Cell[height][width];
		
		for (int i = 0; i < height; i++) {
			for (int k = 0; k < width; k++) {
				cells[i][k] = new Cell(i, k);
			}
		}
		
		for (Wall wall : walls) { //for each wall in a random order
			//if cells divided by this wall are in different sets
			//remove current wall
			//join sets
		}
	}
	
	public String toString() {
		return "Kruskal's Algorithm";
	}

}
