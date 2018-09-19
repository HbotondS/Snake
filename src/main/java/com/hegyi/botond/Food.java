package com.hegyi.botond;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Food extends GameObject{
	public Food(double width, double height) {
		super(width, height);
	}

	public Food(Point2D position, double width, double height) {
		super(position, width, height);
	}

	@Override
	public void render(GraphicsContext gc) {
		gc.setFill(Color.RED);
		super.render(gc);
	}
}
