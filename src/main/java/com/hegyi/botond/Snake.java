package com.hegyi.botond;

import com.sun.javafx.scene.traversal.Direction;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.LinkedList;
import java.util.Queue;

public class Snake implements Renderable {
	private LinkedList<GameObject> body = new LinkedList<>();
	private int bodySize;

	private Direction direction;

	public Snake(Point2D head, Point2D tail, int bodySize) {
		this.bodySize = bodySize;
		body.add(new GameObject(head, bodySize));
		body.add(new GameObject(tail, bodySize));
	}

	public Queue<GameObject> getBody() {
		return body;
	}

	public Point2D getHeadPosition() {
		return body.getFirst().getPosition();
	}

	public void setHeadPosition(Point2D newPos) {
		body.set(0, new GameObject(newPos));
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
		body.removeLast();

		switch (direction) {
			case UP: {
				Point2D newPos = getHeadPosition().subtract(0, bodySize);
				body.addFirst(new GameObject(newPos, bodySize));
				break;
			}
			case DOWN: {
				Point2D newPos = getHeadPosition().add(0, bodySize);
				body.addFirst(new GameObject(newPos, bodySize));
				break;
			}
			case LEFT: {
				Point2D newPos = getHeadPosition().subtract(bodySize, 0);
				body.addFirst(new GameObject(newPos, bodySize));
				break;
			}
			case RIGHT: {
				Point2D newPos = getHeadPosition().add(bodySize, 0);
				body.addFirst(new GameObject(newPos, bodySize));
				break;
			}
		}
	}

	public void grow() {
		body.add(new GameObject());
	}

	public boolean intersect(GameObject other) {
		for (int i = 0; i < getLength(); i++) {
			if (other.intersect(body.get(i))) {
				if (i == 0) {
					grow();
				}
				return true;
			}
		}
		return false;
	}

	public boolean collide() {
		for (int i = 1; i < getLength(); i++) {
			if (getHeadPosition().equals(getBodyPosition(i))) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void render(GraphicsContext gc) {
		gc.setFill(Color.LIMEGREEN);
		for (int i = 0; i < getLength(); i++) {
			body.get(i).render(gc);
		}
	}
}
