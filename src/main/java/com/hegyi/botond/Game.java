package com.hegyi.botond;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Game extends Canvas {
	public static final int PIXELSIZE = 25;

	public Game() {
		setWidth(1000);
		setHeight(700);
		GraphicsContext gc = getGraphicsContext2D();
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, getWidth(), getHeight());

		paintGrid(gc);

		Food food = new Food(PIXELSIZE, PIXELSIZE);
		food.setRandomPosition(1000, 700);
		food.render(gc);
	}

	private void paintGrid(GraphicsContext gc) {
		gc.setStroke(Color.GRAY);
		for (int i = 0; i < getWidth(); i += PIXELSIZE) {
			gc.strokeLine(i, 0, i, getHeight());
		}
		for (int i = 0; i < getHeight(); i += PIXELSIZE) {
			gc.strokeLine(0, i, getWidth(), i);
		}
	}

}
