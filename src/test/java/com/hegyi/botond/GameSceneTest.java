package com.hegyi.botond;

import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.junit.Assert;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import java.util.concurrent.TimeUnit;

public class GameSceneTest extends ApplicationTest {
	private AnchorPane anchorPane;
	private GameScene gameScene;

	@Override
	public void start(Stage stage) {
		anchorPane = new AnchorPane();
		anchorPane.setId("desktop");
		gameScene = new GameScene(anchorPane);
		stage.setScene(gameScene);
		stage.show();
	}

	@Test
	public void testSnakeRunOut() {
		press(KeyCode.RIGHT);
		sleep(500, TimeUnit.MILLISECONDS);
		Assert.assertTrue(gameScene.isGameOver());
	}

	@Test
	public void testSnakeCollide() {
		gameScene.setTime(200_000_000);
		press(KeyCode.RIGHT);
		gameScene.getSnake().grow();
		sleep(100);
		press(KeyCode.DOWN).press(KeyCode.LEFT);
		sleep(1, TimeUnit.SECONDS);
		Assert.assertTrue(gameScene.isGameOver());
	}
}
