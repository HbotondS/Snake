package com.hegyi.botond;

import com.sun.javafx.scene.traversal.Direction;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.LinkedList;

public class Snake implements Renderable {
	private LinkedList<MovingGameObject> body = new LinkedList<>();
	private int bodySize;

	private Direction direction;

	public Snake(Point2D head, Point2D tail, int bodySize) {
		this.bodySize = bodySize;
		body.add(new MovingGameObject(head, bodySize));
		body.add(new MovingGameObject(tail, bodySize));
	}

	public MovingGameObject getHead() {
		return body.getFirst();
	}

	public MovingGameObject getBody(int index) {
		return body.get(index);
	}

	public MovingGameObject getTail() {
		return body.getLast();
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
				Point2D newPos = getHead().getPosition().subtract(0, bodySize);
				body.addFirst(new MovingGameObject(newPos, bodySize, direction));
				break;
			}
			case DOWN: {
				Point2D newPos = getHead().getPosition().add(0, bodySize);
				body.addFirst(new MovingGameObject(newPos, bodySize, direction));
				break;
			}
			case LEFT: {
				Point2D newPos = getHead().getPosition().subtract(bodySize, 0);
				body.addFirst(new MovingGameObject(newPos, bodySize, direction));
				break;
			}
			case RIGHT: {
				Point2D newPos = getHead().getPosition().add(bodySize, 0);
				body.addFirst(new MovingGameObject(newPos, bodySize, direction));
				break;
			}
		}
	}

	public void grow() {
		body.add(new MovingGameObject());
	}

	public boolean intersect(GameObject other) {
		for (int i = 0; i < getLength(); i++) {
			if (other.intersect(body.get(i))) {
				return true;
			}
		}
		return false;
	}

	public boolean collide() {
		for (int i = 1; i < getLength(); i++) {
			if (getHead().getPosition().equals(getBody(i).getPosition())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void render(GraphicsContext gc) {
		gc.setFill(Color.LIMEGREEN);
		getHead().render(gc);
	}
}
