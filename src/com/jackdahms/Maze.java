package com.jackdahms;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Maze {
	
	int width, height;
		
	int[][] map; 
	
	BufferedImage image;
	
	/**
	 * Create a new maze.
	 * @param width		width of cave in pixels
	 * @param height	height of cave in pixels
	 */
	public Maze(int width, int height) {
		this.width = width;
		this.height = height;
		
		map = new int[height][width]; //[rows][columns]
	}
		
	public void mapToImage() {
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D g = image.createGraphics();
		
		g.dispose();
	}

}
