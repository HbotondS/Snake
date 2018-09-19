package com.hegyi.botond;

import javafx.geometry.Point2D;
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

		Food food = new Food(PIXELSIZE, PIXELSIZE);
		food.setRandomPosition(1000, 700);
		food.render(gc);

		Snake snake = new Snake(new Point2D(PIXELSIZE, 0),
				new Point2D(0, 0));
		snake.render(gc);

		paintGrid(gc);
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
