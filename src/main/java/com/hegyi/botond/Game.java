package com.hegyi.botond;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Game extends Canvas {
	public static final int PIXELSIZE = 25;

	private GraphicsContext gc;

	private Food food;
	private Snake snake;

	private boolean paused = true;

	public Game() {
		setWidth(1000);
		setHeight(700);
		gc = getGraphicsContext2D();

		food = new Food(PIXELSIZE, PIXELSIZE);
		snake = new Snake(new Point2D(PIXELSIZE, 0),
				new Point2D(0, 0));

		initFirstFrame();

		new myTimer().start();
	}

	private void initFirstFrame() {
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, getWidth(), getHeight());

		food.setRandomPosition(1000, 700);
		food.render(gc);

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

	private class myTimer extends AnimationTimer {
		@Override
		public void handle(long now) {
			if (!paused) {
				gc.setFill(Color.BLACK);
				gc.fillRect(0, 0, getWidth(), getHeight());

				if (snake.intersect(food)) {
					food.setRandomPosition(1000, 700);
				}

				food.render(gc);

				snake.render(gc);

				paintGrid(gc);
				snake.move();

				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
