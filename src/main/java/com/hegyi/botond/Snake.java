package com.hegyi.botond;

import com.sun.javafx.scene.traversal.Direction;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Snake implements Renderable {
	private ArrayList<GameObject> body = new ArrayList<GameObject>();
	private int bodySize;

	private Direction direction;

	public Snake(Point2D head, Point2D tail, int bodySize) {
		this.bodySize = bodySize;
		body.add(new GameObject(head, bodySize, bodySize));
		body.add(new GameObject(tail, bodySize, bodySize));
	}

	public Point2D headPosition() {
		return body.get(0).getPosition();
	}

	public Point2D getBodyPosition(int index) {
		return body.get(index).getPosition();
	}

	public int getLength() {
		return body.size();
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public void move() {
		for (int i = getLength() - 1; i > 0; --i) {
			body.set(i, body.get(i-1));
		}

		switch (direction) {
			case UP: {
				headPosition().subtract(bodySize, 0);
				break;
			}
			case DOWN: {
				headPosition().add(bodySize, 0);
				break;
			}
			case LEFT: {
				headPosition().subtract(0, bodySize);
				break;
			}
			case RIGHT: {
				headPosition().add(0, bodySize);
				break;
			}
		}
	}

	public void grow() {
		body.add(new GameObject());
	}

	public boolean intersect(GameObject other) {
		for (int i = 0; i < getLength(); i++) {
			if (body.get(i).intersect(other)) {
				return true;
			}
		}
		return false;
	}

	public boolean collide() {
		for (int i = 1; i < getLength(); i++) {
			if (headPosition().equals(getBodyPosition(i))) {
				return true;
			}
		}
		return false;
	}

	public void render(GraphicsContext gc) {
		gc.setFill(Color.LIMEGREEN);
		for (int i = 0; i < getLength(); i++) {
			body.get(i).render(gc);
		}
	}
}
