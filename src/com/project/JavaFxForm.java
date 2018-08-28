package com.project;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.Scanner;

import static java.lang.Math.round;
import static javafx.scene.control.Alert.*;

public class JavaFxForm extends Application {

	private Stage window;
	private Scene scene;
	private VBox content;

	private VBox gameContent = new GameContent();
	private VBox settingsContent = new SettingsContent();
	private VBox scoresContent = new HighScoreContent();

	public static final int gameMode1 = 0;
	public static final int gameMode2 = 1;

	private MyColor headColor = new MyColor(0, 255, 0);
	private MyColor bodyColor = new MyColor(0, 255, 0);

	public static int speed = 2;

	private Button gameBtn = new Button("Game");
	private Button settingsBtn = new Button("Settings");
	private Button highScoreBtn = new Button("High scores");

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		window = primaryStage;

		HBox toolbar = new HBox(5);
		toolbar.getChildren().addAll(gameBtn, settingsBtn, highScoreBtn);
		content = new VBox(70);
		content.getChildren().add(toolbar);

		content.getChildren().add(gameContent);
		scene = new Scene(content, 300, 275);

		window.setScene(scene);
		window.setTitle("Snake");
		window.setResizable(false);;
		window.getIcons().add(new Image("file:Resources/snake_icon.png"));
		window.show();

		setButtonsAction();
	}

	private void setButtonsAction() {
		gameBtn.setOnAction(e -> {
			content.getChildren().removeAll(gameContent, settingsContent, scoresContent);
			content.getChildren().add(gameContent);
			content.setSpacing(70);
		});

		settingsBtn.setOnAction(e -> {
			content.getChildren().removeAll(gameContent, settingsContent, scoresContent);
			content.getChildren().add(settingsContent);
			content.setSpacing(60);
		});

		highScoreBtn.setOnAction(e -> {
			content.getChildren().removeAll(gameContent, settingsContent, scoresContent);
			content.getChildren().add(scoresContent);
			content.setSpacing(20);
		});
	}

	private class GameContent extends VBox {
		private Button gameMode1Btn = new Button("Border Mode");
		private Button gameMode2Btn = new Button("Borderless Mode");
		private Button exitButton = new Button("Exit");

		public GameContent() {
			initComponent();
		}

		private void initComponent() {

			this.setSpacing(20);
			this.setAlignment(Pos.CENTER);
			this.getChildren().addAll(gameMode1Btn, gameMode2Btn, exitButton);

			exitButton.setOnAction(e-> System.exit(0));

			setButtonsAction();
		}

		private void setButtonsAction() {
			gameMode1Btn.setOnAction(event -> {
				new Game(gameMode1, headColor, bodyColor);
				window.hide();
			});

			gameMode2Btn.setOnAction(event -> {
				new Game(gameMode2, headColor, bodyColor);
				window.hide();
			});
		}
	}

	private class SettingsContent extends VBox {
		private Label difLabel = new Label("Difficulty:");
		private Label headColLabel = new Label("Head color:");
		private Label bodyColLabel = new Label("Body color:");

		private ToggleButton dif1 = new ToggleButton("Easy");
		private ToggleButton dif2 = new ToggleButton("Medium");
		private ToggleButton dif3 = new ToggleButton("Hard");

		private ColorPicker pck1 = new ColorPicker();
		private ColorPicker pck2 = new ColorPicker();

		private GridPane grid = new GridPane();

		public SettingsContent() {
			initComponent();
		}

		private void initComponent() {
			setGrid();

			this.setSpacing(20);
			this.setAlignment(Pos.CENTER);
			this.getChildren().add(grid);

			setButtonsAction();

			setColorPickerAction();
		}

		private void setGrid() {
			grid.setAlignment(Pos.TOP_CENTER);
			grid.setHgap(10);
			grid.setVgap(10);
			grid.add(difLabel, 0, 0);
			grid.add(dif1, 0, 1);
			grid.add(dif2, 0, 2);
			grid.add(dif3, 0, 3);

			grid.add(headColLabel, 1, 0);
			grid.add(pck1, 1, 1);

			grid.add(bodyColLabel, 1, 2);
			grid.add(pck2, 1, 3);
		}

		private void setButtonsAction() {
			dif1.setOnAction(event -> {
				dif2.setSelected(false);
				dif3.setSelected(false);
				speed = 1;
			});

			dif2.setOnAction(event -> {
				dif1.setSelected(false);
				dif3.setSelected(false);
				speed = 2;
			});

			dif3.setOnAction(event -> {
				dif1.setSelected(false);
				dif2.setSelected(false);
				speed = 3;

			});
		}

		private void setColorPickerAction() {
			pck1.setOnAction(event -> {
				headColor.setRED((int) round(pck1.getValue().getRed() * 255));
				headColor.setGREEN((int) round(pck1.getValue().getGreen() * 255));
				headColor.setBLUE((int) round(pck1.getValue().getBlue() * 255));
			});

			pck2.setOnAction(event -> {
				bodyColor.setRED((int) round(pck2.getValue().getRed() * 255));
				bodyColor.setGREEN((int) round(pck2.getValue().getGreen() * 255));
				bodyColor.setBLUE((int) round(pck2.getValue().getBlue() * 255));
			});
		}
	}

	private class HighScoreContent extends VBox {

		public HighScoreContent() {
			initComponent();
		}

		private void initComponent() {
			this.setAlignment(Pos.TOP_CENTER);
			TextFlow textFlow = new TextFlow();
			textFlow.setTextAlignment(TextAlignment.CENTER);

			Scanner scanner = null;
			int nb = 1; /**ki hányadik*/
			try {
				scanner = new Scanner(new File("Resources/highscores.save"));
			} catch (FileNotFoundException ex) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setContentText("The save file doesn't found.");
				alert.setHeaderText(null);
				alert.showAndWait();
				System.exit(1);
			}
			while (scanner.hasNextLine()) {
				Text line = new Text(scanner.nextLine());
				line.setStyle("-fx-font-size: 15");
				Text lineNumber = new Text(String.valueOf(nb++) + ". ");
				lineNumber.setStyle("-fx-font-size: 15");
				textFlow.getChildren().addAll(lineNumber, line, new Text("\n"));
			}
			this.getChildren().add(textFlow);
		}
	}
}
