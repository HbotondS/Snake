package com.hegyi.botond;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class GameScene extends Scene {
	public static final int PIXELSIZE = 25;

	private GraphicsContext gc;
	private Canvas canvas;
	private final int WIDTH = 1000;
	private final int HEIGHT = 700;

	private Food food;
	private Snake snake;

	private boolean paused = true;

	public GameScene(Parent root) {
		super(root);

		canvas = new Canvas(WIDTH, HEIGHT);
		((Pane) root).getChildren().add(canvas);

		gc = canvas.getGraphicsContext2D();

		food = new Food(PIXELSIZE, PIXELSIZE);
		snake = new Snake(new Point2D(PIXELSIZE, 0),
				new Point2D(0, 0));

		initFirstFrame();

		new myTimer().start();
	}

	private void initFirstFrame() {
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, WIDTH, HEIGHT);

		food.setRandomPosition(WIDTH, HEIGHT);
		food.render(gc);

		snake.render(gc);

		paintGrid(gc);
	}

	private void paintGrid(GraphicsContext gc) {
		gc.setStroke(Color.GRAY);
		for (int i = 0; i < WIDTH; i += PIXELSIZE) {
			gc.strokeLine(i, 0, i, HEIGHT);
		}
		for (int i = 0; i < HEIGHT; i += PIXELSIZE) {
			gc.strokeLine(0, i, WIDTH, i);
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
