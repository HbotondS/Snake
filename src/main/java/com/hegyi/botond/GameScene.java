package com.hegyi.botond;

import com.hegyi.botond.controllers.SettingsViewController;
import com.sun.javafx.scene.traversal.Direction;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;
import java.util.prefs.Preferences;

public class GameScene extends Scene {
	public static final int PIXELSIZE = 25;

	private GraphicsContext gc;
	private Canvas canvas;
	private final int WIDTH = 1000;
	private final int HEIGHT = 700;

	private Food food;
	private Snake snake;

	private boolean paused = false;
	private boolean gameOver = false;

	private int score = 0;

	private myTimer timer;

	private Preferences prefs;

	private final String UP = "UP";
	private final String DOWN = "DOWN";
	private final String RIGHT = "RIGHT";
	private final String LEFT = "LEFT";

	public GameScene(Parent root) {
		super(root);
			prefs = Preferences.userRoot().node(SettingsViewController.class.getName());

		canvas = new Canvas(WIDTH, HEIGHT);
		((Pane) root).getChildren().add(canvas);

		gc = canvas.getGraphicsContext2D();

		food = new Food(PIXELSIZE, PIXELSIZE);
		initSnake();

		food.setRandomPosition(WIDTH, HEIGHT);
		renderGameElements();

		initActionHandlers();

		Font.loadFont(getClass().getClassLoader().getResourceAsStream("font/lunchds.ttf"), 30);
		gc.setFont(new Font("Lunchtime Doubly So Regular", 30));

		timer = new myTimer();

		initDialog();
	}

	private void initDialog() {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Instruction");
		alert.setHeaderText(null);
		Pane pane = null;
		try {
			pane = FXMLLoader.load(getClass().getClassLoader().getResource("views/InstructionDialog.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		alert.getDialogPane().setContent(pane);

		alert.showAndWait();
	}

	private void initSnake() {
		snake = new Snake(new Point2D(WIDTH / 2.0, HEIGHT / 2.0),
				new Point2D(WIDTH / 2.0 - PIXELSIZE, HEIGHT / 2.0), PIXELSIZE);
	}

	private void initActionHandlers() {
		this.setOnKeyPressed(e -> {
			KeyCode kc = e.getCode();
			if (kc == KeyCode.ESCAPE) {
				if (paused) {
					timer.start();
				} else {
					timer.stop();
					renderPauseMsg();
				}
				paused = !paused;
			}

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
		});
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

				if (gameOver) {
					// stop the timer
					this.stop();
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
		gc.fillRect(0, 0, WIDTH, HEIGHT);

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

		gc.fillText("Paused!", WIDTH / 2.0, HEIGHT / 2.0);
	}

	private void renderGameOverMsg() {
		setTextCenter();

		gc.fillText("Game over!\nYour score: " + score, WIDTH / 2.0, HEIGHT / 2.0);

		// TODO: add button for save
		Button restartBtn = new Button("Restart");
		restartBtn.setLayoutX(WIDTH/2.0 - 100);
		restartBtn.setLayoutY(HEIGHT/2.0 + 50);

		Button exitBtn = new Button("Exit");
		exitBtn.setLayoutX(WIDTH/2.0 - 25);
		exitBtn.setLayoutY(HEIGHT/2.0 + 100);

		Button backButton = new Button("Back");
		backButton.setLayoutX(WIDTH/2.0 + 50);
		backButton.setLayoutY(HEIGHT/2.0 + 50);

		exitBtn.setOnMouseClicked(e -> System.exit(0));
		restartBtn.setOnMouseClicked(e -> {
			gameOver = false;
			((AnchorPane) getRoot()).getChildren().removeAll(restartBtn, exitBtn, backButton);

			initSnake();

			food.setRandomPosition(WIDTH, HEIGHT);
			renderGameElements();
		});
		backButton.setOnMouseClicked(e -> {
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

		((AnchorPane) getRoot()).getChildren().addAll(restartBtn, exitBtn, backButton);
	}
}
