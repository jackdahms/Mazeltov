package com.jackdahms;

import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class Maze {
	
	int width, height;
		
	int[][] map; 
	
	WritableImage image;
	PixelWriter writer;
	
	/**
	 * Create a new maze.
	 * @param width		width of cave in pixels
	 * @param height	height of cave in pixels
	 */
	public Maze(int width, int height) {
		this.width = width;
		this.height = height;
		image = new WritableImage(width, height);
		
		map = new int[height][width]; //[rows][columns]
	}
		
	public void mapToImage() {
		image = new WritableImage(width, height);
		writer = image.getPixelWriter();
		
		for (int i = 0; i < width / 2; i++) {
			for (int k = 0; k < height / 2; k++) {
				writer.setColor(i, k, Color.BLACK);
			}
		}
		
		for (int i = width / 2; i < width; i++) {
			for (int k = height / 2; k < height; k++) {
				writer.setColor(i, k, Color.BLACK);
			}
		}
	}

}
