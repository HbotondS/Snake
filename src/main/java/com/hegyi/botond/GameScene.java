package com.hegyi.botond;

import com.hegyi.botond.controllers.SettingsViewController;
import com.sun.javafx.scene.traversal.Direction;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.prefs.Preferences;

public class GameScene extends Scene {
	public static final int PIXELSIZE = 25;

	private GraphicsContext gc;
	private Canvas canvas;
	private final int WIDTH = 1000;
	private final int HEIGHT = 700;

	private Food food;
	private Snake snake;

	private boolean inGame = false;
	private boolean paused = false;
	private boolean gameOver = false;

	private int score = 0;
	private int foodPoint = 100;

	private myTimer timer;
	private long time;

	private Preferences prefs;

	private final String UP = "UP";
	private final String DOWN = "DOWN";
	private final String RIGHT = "RIGHT";
	private final String LEFT = "LEFT";

	private Label pauseLabel;
	private Label gameOverLabel;
	private Label scoreLabel;
	private Label inGameScoreLabel;

	private MyHandlerForArrows myHandlerForArrows = new MyHandlerForArrows();
	private MyHandlerForEsc myHandlerForEsc = new MyHandlerForEsc();

	public GameScene(Parent root, long time) {
		this(root);
		this.time = time;
	}

	public GameScene(Parent root) {
		super(root);
		prefs = Preferences.userRoot().node(SettingsViewController.class.getName());

		canvas = new Canvas(WIDTH, HEIGHT);
		((Pane) root).getChildren().add(canvas);

		gc = canvas.getGraphicsContext2D();

		food = new Food(PIXELSIZE, PIXELSIZE);

		timer = new myTimer();

		// check user inputs on the first screen
		addEventHandler(KeyEvent.KEY_PRESSED, myHandlerForArrows);
		addEventHandler(KeyEvent.KEY_PRESSED, myHandlerForEsc);

		initLabels();

		initScreen();
	}

	public boolean isGameOver() {
		return gameOver;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public Snake getSnake() {
		return snake;
	}

	private void initLabels() {
		pauseLabel = new Label("Paused");
		pauseLabel.setLayoutX(WIDTH/2f - 25);
		pauseLabel.setLayoutY(HEIGHT/2f);
		pauseLabel.getStylesheets().add(getClass().getClassLoader().getResource("styles/overallStyle.css").toString());

		gameOverLabel = new Label("Game Over!");
		gameOverLabel.setLayoutX(WIDTH/2f - 75);
		gameOverLabel.setLayoutY(HEIGHT/2f - 40);
		gameOverLabel.getStylesheets().add(getClass().getClassLoader().getResource("styles/overallStyle.css").toString());

		scoreLabel = new Label();
		scoreLabel.setLayoutX(WIDTH/2f - 125);
		scoreLabel.setLayoutY(HEIGHT/2f - 10);
		scoreLabel.getStylesheets().add(getClass().getClassLoader().getResource("styles/overallStyle.css").toString());

		if (prefs.getBoolean("renderScore", true)) {
			inGameScoreLabel = new Label();
			inGameScoreLabel.setId("inGameScoreLabel");
			inGameScoreLabel.setLayoutX(0);
			inGameScoreLabel.setLayoutY(0);
			inGameScoreLabel.getStylesheets().add(getClass().getClassLoader().getResource("styles/overallStyle.css").toString());
			((AnchorPane) getRoot()).getChildren().add(inGameScoreLabel);
		}
	}

	private void initScreen() {
		score = 0;
		if (prefs.getBoolean("renderScore", true)) {
			inGameScoreLabel.setText("Score: " + score + "pt.");
		}

		renderBackground();

		initSnake();

		food.setRandomPosition(WIDTH, HEIGHT);

		renderGameElements();
	}

	private void renderBackground() {
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, WIDTH, HEIGHT);
		renderGrid(gc);
	}

	private void initSnake() {
		snake = new Snake(new Point2D(WIDTH / 2f, HEIGHT / 2f),
				new Point2D(WIDTH / 2f - PIXELSIZE, HEIGHT / 2f), PIXELSIZE);
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

	private boolean checkSnake() {
		double posX = snake.getHead().getPosition().getX();
		double posY = snake.getHead().getPosition().getY();
		return posX >= WIDTH || posX < 0 || posY >= HEIGHT || posY < 0;
	}

	private void renderGameElements() {
		snake.render(gc);
		food.render(gc);
		snake.render(gc);
	}

	private void renderGameOverMsg() {
		scoreLabel.setText("Your score: " + score);

		// TODO: add button for save
		Button restartBtn = new Button("Restart");
		restartBtn.setLayoutX(WIDTH/2f - 125);
		restartBtn.setLayoutY(HEIGHT/2f + 50);
		restartBtn.getStylesheets().add(getClass().getClassLoader().getResource("styles/GameOverStyle.css").toString());

		Button exitBtn = new Button("Exit");
		exitBtn.setLayoutX(WIDTH/2f - 50);
		exitBtn.setLayoutY(HEIGHT/2f + 100);
		exitBtn.getStylesheets().add(getClass().getClassLoader().getResource("styles/GameOverStyle.css").toString());

		Button backBtn = new Button("Back");
		backBtn.setLayoutX(WIDTH/2f + 30);
		backBtn.setLayoutY(HEIGHT/2f + 50);
		backBtn.getStylesheets().add(getClass().getClassLoader().getResource("styles/GameOverStyle.css").toString());

		exitBtn.setOnMouseClicked(e -> System.exit(0));
		restartBtn.setOnMouseClicked(e -> {
			gameOver = false;
			((AnchorPane) getRoot()).getChildren().removeAll(gameOverLabel, scoreLabel, restartBtn, exitBtn, backBtn);

			food.setRandomPosition(WIDTH, HEIGHT);
			addEventHandler(KeyEvent.KEY_PRESSED, myHandlerForArrows);
			initScreen();
		});
		backBtn.setOnMouseClicked(e -> {
			Stage stage = (Stage) getWindow();
			Parent root = null;
			try {
				root = FXMLLoader.load(getClass().
						getClassLoader().
						getResource("views/WelcomeView.fxml"));
			} catch (IOException e1) {
				MyLogger.WARN("views/WelcomeView.fxml file not found");
				System.exit(0);
			}
			stage.setScene(new Scene(root));
			stage.centerOnScreen();
			stage.show();
		});

		((AnchorPane) getRoot()).getChildren().addAll(gameOverLabel, scoreLabel, restartBtn, exitBtn, backBtn);
	}

	private class myTimer extends AnimationTimer {
		private long lastUpdate = 0;

		@Override
		public void start() {
			super.start();
			inGame = true;
		}

		@Override
		public void handle(long now) {
			// if the game isn't paused it will refresh the screen in every 100 milliseconds
			if (now - lastUpdate >= time) {
				addEventHandler(KeyEvent.KEY_PRESSED, myHandlerForArrows);
				lastUpdate = now;

				snake.move();
				if (snake.getHead().intersect(food)) {
					do {
						food.setRandomPosition(WIDTH, HEIGHT);
					} while (snake.intersect(food));
					score += foodPoint;
					snake.grow();

					if (prefs.getBoolean("renderScore", true)) {
						inGameScoreLabel.setText("Score: " + score + "pt.");
					}
				}

				renderGameElements();
				if (snake.collide() || checkSnake()) {
					gameOver = true;
				}

				if (gameOver) {
					// stop the timer
					this.stop();
					renderGameOverMsg();
				}
			}
		}
	}

	private class MyHandlerForEsc implements EventHandler<KeyEvent> {
		@Override
		public void handle(KeyEvent event) {
			KeyCode kc = event.getCode();
			if (kc == KeyCode.ESCAPE && inGame) {
				if (paused) {
					timer.start();
					((AnchorPane) getRoot()).getChildren().remove(pauseLabel);
				} else {
					timer.stop();
					((AnchorPane) getRoot()).getChildren().add(pauseLabel);
				}
				paused = !paused;
			}
		}
	}

	private class MyHandlerForArrows implements EventHandler<KeyEvent> {
		@Override
		public void handle(KeyEvent event) {
			KeyCode kc = event.getCode();

			String key = kc.toString();
			if ((key.equals(prefs.get(RIGHT, ""))
					|| key.equals(prefs.get(LEFT, ""))
					|| key.equals(prefs.get(UP, ""))
					|| key.equals(prefs.get(DOWN, "")))
					&& !gameOver) {
				timer.start();
				paused = false;
			}
			if (key.equals(prefs.get(RIGHT, "")) && snake.getDirection() != Direction.LEFT) {
				snake.setDirection(Direction.RIGHT);
			} else {
				if (key.equals(prefs.get(LEFT, "")) && snake.getDirection() != Direction.RIGHT) {
					snake.setDirection(Direction.LEFT);
				} else {
					if (key.equals(prefs.get(DOWN, "")) && snake.getDirection() != Direction.UP) {
						snake.setDirection(Direction.DOWN);
					} else {
						if (key.equals(prefs.get(UP, "")) && snake.getDirection() != Direction.DOWN) {
							snake.setDirection(Direction.UP);
						}
					}
				}
			}

			removeEventHandler(KeyEvent.KEY_PRESSED, this);
		}
	}
}
