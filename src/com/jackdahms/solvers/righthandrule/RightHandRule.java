package com.jackdahms.solvers.righthandrule;

import com.jackdahms.solvers.Solver;

public class RightHandRule extends Solver {

	Direction RIGHT = Direction.RIGHT;
	Direction UP = Direction.UP;
	Direction LEFT = Direction.LEFT;
	Direction DOWN = Direction.DOWN;
	
	enum Direction {
		
		RIGHT(0, 1),
		UP(0, 0),
		LEFT(0, 0),
		DOWN(1, 0);
		
		int rowIncrement;
		int colIncrement;
		
		Direction(int rowIncrement, int colIncrement) {
			this.rowIncrement = rowIncrement;
			this.colIncrement = colIncrement;
		}
		
		Direction right() {
			Direction start;
			if (this == RIGHT) {
				start = DOWN;
			} else if (this == UP) {
				start = RIGHT;
			} else if (this == LEFT) {
				start = UP;
			} else { //this == DOWN
				start = LEFT;
			}
			return start;
		}
		
		Direction next() {
			Direction next;
			if (this == RIGHT) {
				next = UP;
			} else if (this == UP) {
				next = LEFT;
			} else if (this == LEFT) {
				next = DOWN;
			} else { //this == DOWN
				next = RIGHT;
			}
			return next;
		}
		
	}


	@Override
	public void solve() {
		Direction direction = DOWN;
		
		int x = startx;
		int y = starty;
	
		try {
			do {
				direction = nextMove(x, y, direction, 0);
				if (direction == RIGHT) {
					x++;
				} else if (direction == UP) {
					y--;
				} else if (direction == LEFT) {
					x--;
				} else if (direction == DOWN) { //direction == DOWN
					y++;
				}
				pathX.add(x);
				pathY.add(y);
			}
			while (x != finishx || y != finishy);
		} catch (Exception e) {
			System.err.println("Invalid start!");
		}
	}
	
	/**
	 * Returns the direction to move in. Throws exception if no moves possible.
	 * @throws Exception 
	 */
	private Direction nextMove(int x, int y, Direction direction, int attempt) throws Exception {
		Direction next = null;
		if (attempt == 0) {
			if (wall(x, y, direction.right())) {
				next = nextMove(x, y, direction.right(), ++attempt);
			} else {
				next = direction.right();
			}
		} else if (attempt < 4) {
			if (wall(x, y, direction.next())) {
				next = nextMove(x, y, direction.next(), ++attempt);
			} else {
				next = direction.next();
			}
		} else {
			throw new Exception("Invalid location during solution!");
		}
		return next;
	}
	
	/**
	 * Returns true if there is a wall in the given direction from the cell coordinates
	 */
	private boolean wall(int x, int y, Direction direction) {
		int wall;
		if (x == 0 && direction == LEFT) {
			wall = 1;
		} else if (x == width - 1 && direction == RIGHT) {
			wall = 1;
		} else if (y == 0 && direction == UP) {
			wall = 1;
		} else if (y == height -1 && direction == DOWN) {
			wall = 1;
		} else {
			if (direction == RIGHT) {
				wall = verticalWalls[y][x];
			} else if (direction == UP) {
				wall = horizontalWalls[y - 1][x];
			} else if (direction == LEFT) {
				wall = verticalWalls[y][x - 1];
			} else { //direction == DOWN
				wall = horizontalWalls[y][x];
			}
		}
		return wall == 1;
	}
	
	public String toString() {
		return "Right Hand Rule";
	}

}
