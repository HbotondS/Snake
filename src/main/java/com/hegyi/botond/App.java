package com.hegyi.botond;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class App extends Application {
	public static final int WIDTH = 400;
	public static final int HEIGHT = 400;

	public void start(Stage primaryStage) {
		Parent root = null;
		try {
			root = FXMLLoader.load(getClass()
					.getClassLoader()
					.getResource("views/WelcomeView.fxml"));

		} catch (Exception ex) {
			MyLogger.WARN("views/WelcomeView.fxml file not found.");
			System.exit(0);
		}
		try {
			primaryStage.getIcons()
					.add(new Image(getClass()
							.getClassLoader()
							.getResourceAsStream("images/snake_icon.png")));
		} catch (Exception ex) {
			MyLogger.WARN("images/snake_icon.png file not found.");
			System.exit(0);
		}
		primaryStage.setTitle("Snake");
		primaryStage.setScene(new Scene(root, WIDTH, HEIGHT));
		primaryStage.setResizable(false);
		primaryStage.centerOnScreen();
		primaryStage.show();
	}
}
