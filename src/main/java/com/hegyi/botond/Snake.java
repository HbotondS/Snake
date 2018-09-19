package com.hegyi.botond;

import com.sun.javafx.scene.traversal.Direction;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

import static com.hegyi.botond.Game.PIXELSIZE;

public class Snake implements Renderable {
	private ArrayList<GameObject> body = new ArrayList<GameObject>();

	private Direction direction;

	public Snake(Point2D head, Point2D tail) {
		body.add(new GameObject(head, PIXELSIZE, PIXELSIZE));
		body.add(new GameObject(tail, PIXELSIZE, PIXELSIZE));
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
				headPosition().subtract(PIXELSIZE, 0);
				break;
			}
			case DOWN: {
				headPosition().add(PIXELSIZE, 0);
				break;
			}
			case LEFT: {
				headPosition().subtract(0, PIXELSIZE);
				break;
			}
			case RIGHT: {
				headPosition().add(0, PIXELSIZE);
				break;
			}
		}
	}

	public void grow() {
		body.add(new GameObject());
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
