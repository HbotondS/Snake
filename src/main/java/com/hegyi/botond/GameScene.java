package com.hegyi.botond;

import com.sun.javafx.scene.traversal.Direction;
import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class GameScene extends Scene {
	public static final int PIXELSIZE = 25;

	private GraphicsContext gc;
	private Canvas canvas;
	private final int WIDTH = 1000;
	private final int HEIGHT = 700;

	private Food food;
	private Snake snake;

	private boolean paused = false;
	private boolean gameStarted = false;
	private boolean gameOver = false;

	private int score = 0;

	public GameScene(Parent root) {
		super(root);

		canvas = new Canvas(WIDTH, HEIGHT);
		((Pane) root).getChildren().add(canvas);

		gc = canvas.getGraphicsContext2D();

		food = new Food(PIXELSIZE, PIXELSIZE);
		snake = new Snake(new Point2D(PIXELSIZE, 0),
				new Point2D(0, 0), PIXELSIZE);

		initFirstFrame();

		initActionHandlers();

		Font.loadFont(getClass().getClassLoader().getResourceAsStream("font/lunchds.ttf"), 30);
		gc.setFont(new Font("Lunchtime Doubly So Regular", 30));

		new myTimer().start();
	}

	private void initActionHandlers() {
		this.setOnKeyPressed(e -> {
			switch (e.getCode()) {
				case RIGHT: {
					snake.setDirection(Direction.RIGHT);
					gameStarted = true;
					break;
				}
				case LEFT: {
					snake.setDirection(Direction.LEFT);
					gameStarted = true;
					break;
				}
				case DOWN: {
					snake.setDirection(Direction.DOWN);
					gameStarted = true;
					break;
				}
				case UP: {
					snake.setDirection(Direction.UP);
					gameStarted = true;
					break;
				}
				case ESCAPE: {
					paused = !paused;
				}
			}
		});
	}

	private void initFirstFrame() {
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, WIDTH, HEIGHT);

		food.setRandomPosition(WIDTH, HEIGHT);
		food.render(gc);

		snake.render(gc);

		renderGrid(gc);
	}

	private void renderGrid(GraphicsContext gc) {
		gc.setStroke(Color.GRAY);
		for (int i = 0; i < WIDTH; i += PIXELSIZE) {
			gc.strokeLine(i, 0, i, HEIGHT);
		}
		for (int i = 0; i < HEIGHT; i += PIXELSIZE) {
			gc.strokeLine(0, i, WIDTH, i);
		}
	}

	private class myTimer extends AnimationTimer {
		private long lastUpdate = 0;

		@Override
		public void handle(long now) {
			// if the game isn't paused it will refresh the screen in every 100 milliseconds
			if (gameStarted && !gameOver) {
				if (!paused) {
					if (now - lastUpdate >= 100_000_000) {
						lastUpdate = now;

						if (snake.intersect(food)) {
							food.setRandomPosition(1000, 700);
							score = (snake.getLength() - 2) * 100;
						}

						snake.move();
						checkSnake();
						renderGameElements();
						if (snake.collide()) {
							gameOver = true;
						}
					}
				} else {
					renderPauseMsg();
				}
			} else {
				if (gameOver) {
					renderGameOverMsg();
				}
			}
		}
	}

	private void checkSnake() {
		double posX = snake.getHeadPosition().getX();
		double posY = snake.getHeadPosition().getY();
		if (posX >= WIDTH || posX < 0 || posY >= HEIGHT || posY < 0) {
			gameOver = true;
		}
	}

	private void renderGameElements() {
		// background
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, getWidth(), getHeight());

		food.render(gc);
		snake.render(gc);
		renderGrid(gc);
	}

	private void setTextCenter() {
		gc.setFill(Color.WHITE);
		gc.setTextAlign(TextAlignment.CENTER);
		gc.setTextBaseline(VPos.CENTER);
	}

	private void renderPauseMsg() {
		setTextCenter();

		gc.fillText("Paused!\nYour score:" + score, WIDTH / 2.0, HEIGHT / 2.0);
	}

	private void renderGameOverMsg() {
		setTextCenter();

		gc.fillText("Game over!\nYour score: " + score, WIDTH / 2.0, HEIGHT / 2.0);

		//((Pane) getRoot()).getChildren().add(new Button("Click me!"));
		// TODO: add buttons for restart, save & exit
	}
}
