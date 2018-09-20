package com.hegyi.botond;

import com.sun.javafx.scene.traversal.Direction;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Snake implements Renderable {
	private ArrayList<Point2D> body = new ArrayList<Point2D>();
	private int bodySize;

	private Direction direction;

	public Snake(Point2D head, Point2D tail, int bodySize) {
		this.bodySize = bodySize;
		body.add(head);
		body.add(tail);
	}

	public ArrayList<Point2D> getBody() {
		return body;
	}

	public Point2D getHeadPosition() {
		return body.get(0);
	}

	public void setHeadPosition(Point2D newPos) {
		body.set(0, newPos);
	}

	public Point2D getBodyPosition(int index) {
		return body.get(index);
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
				body.set(0, getHeadPosition().subtract(0, bodySize));
				break;
			}
			case DOWN: {
				body.set(0, getHeadPosition().add(0, bodySize));
				break;
			}
			case LEFT: {
				body.set(0, getHeadPosition().subtract(bodySize, 0));
				break;
			}
			case RIGHT: {
				body.set(0, getHeadPosition().add(bodySize, 0));
				break;
			}
		}
	}

	public void grow() {
		body.add(new Point2D(0, 0));
	}

	public boolean intersect(GameObject other) {
		for (int i = 0; i < getLength(); i++) {
			if (other.intersect(new Rectangle2D(body.get(i).getX(), body.get(i).getY(), bodySize, bodySize))) {
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

	public void render(GraphicsContext gc) {
		gc.setFill(Color.LIMEGREEN);
		for (int i = 0; i < getLength(); i++) {
			gc.fillRect(body.get(i).getX(), body.get(i).getY(), bodySize, bodySize);
		}
	}
}
