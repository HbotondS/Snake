package com.hegyi.botond.controllers;

import com.hegyi.botond.GameScene;
import com.hegyi.botond.MyLogger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class WelcomeViewController {
	@FXML
	private Button startBtn;
	@FXML
	private Button settingsBtn;
	@FXML
	private Button scoreBtn;

	private Parent root;
	private Stage parentView;

	private long difficulty = 100_000_000;

	public void exit() {
		System.exit(0);
	}

	public void startBtnAction() {
		AnchorPane root = new AnchorPane();

		parentView = (Stage) startBtn.getScene().getWindow();
		parentView.setScene(new GameScene(root, difficulty));
		parentView.centerOnScreen();
		parentView.show();
	}

	public void settingsBtnAction() {

		parentView = (Stage) settingsBtn.getScene().getWindow();

		setView(parentView, "views/SettingsView.fxml");
	}

	public void scoreBtnAction() {
		parentView = (Stage) scoreBtn.getScene().getWindow();

		setView(parentView, "views/ScoreView.fxml");
	}

	private void setView(Stage stage, String location) {
		try {
			root = FXMLLoader.load(getClass().getClassLoader().getResource(location));
		} catch (Exception ex) {
			MyLogger.WARN(location + " file not found");
			System.exit(0);
		}

		stage.setScene(new Scene(root));
		stage.show();
	}
}
