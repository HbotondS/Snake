package com.hegyi.botond.controllers;

import com.hegyi.botond.MyLogger;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import java.util.prefs.Preferences;

public class SettingsViewController extends GoBack {
	@FXML
	private Button backBtn;
	@FXML
	private TextField upText;
	@FXML
	private TextField downText;
	@FXML
	private TextField rightText;
	@FXML
	private TextField leftText;

	private Preferences prefs;

	private final String UP = "UP";
	private final String DOWN = "DOWN";
	private final String RIGHT = "RIGHT";
	private final String LEFT = "LEFT";

	public SettingsViewController() {
		prefs = Preferences.userRoot().node(SettingsViewController.class.getName());

		setDefCont();
	}

	// set the default controls
	private void setDefCont() {
		prefs.put(UP, UP);
		prefs.put(DOWN, DOWN);
		prefs.put(RIGHT, RIGHT);
		prefs.put(LEFT, LEFT);
	}

	// set controls text
	private void setContText() {
		upText.setText(prefs.get(UP, ""));
		downText.setText(prefs.get(DOWN, ""));
		rightText.setText(prefs.get(RIGHT, ""));
		leftText.setText(prefs.get(LEFT, ""));
	}

	// reset the controls to the default values
	public void reset() {
		setDefCont();
		setContText();
	}

	@FXML
	public void initialize() {
		// this method is called every time when the user open the settings view
		setContText();
	}

	public void changeText(KeyEvent keyEvent) {
		Node textField = (Node) keyEvent.getSource();
		((TextField) textField).setText(keyEvent.getCode().toString());

		if (textField.getId().equals(upText.getId())) {
			MyLogger.INFO("up stored " + keyEvent.getCode());
			prefs.put(UP, keyEvent.getCode().toString());
		} else {
			if (textField.getId().equals(downText.getId())) {
				MyLogger.INFO("down stored " + keyEvent.getCode());
				prefs.put(DOWN, keyEvent.getCode().toString());
			} else {
				if (textField.getId().equals(rightText.getId())) {
					MyLogger.INFO("right stored " + keyEvent.getCode());
					prefs.put(RIGHT, keyEvent.getCode().toString());
				} else {
					if (textField.getId().equals(leftText.getId())) {
						MyLogger.INFO("left stored: " + keyEvent.getCode());
						prefs.put(LEFT, keyEvent.getCode().toString());
					}
				}
			}
		}
	}
}
