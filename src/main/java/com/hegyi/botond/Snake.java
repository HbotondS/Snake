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

	public Point2D head() {
		return body.get(0).getPosition();
	}

	public Point2D getBody(int index) {
		return body.get(0).getPosition();
	}

	public int getLenght() {
		return body.size();
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public void move() {
		for (int i = getLenght() - 1; i > 0; --i) {
			body.set(i, body.get(i-1));
		}

		switch (direction) {
			case UP: {
				head().add(-PIXELSIZE, 0);
				break;
			}
			case DOWN: {
				head().add(PIXELSIZE, 0);
				break;
			}
			case LEFT: {
				head().add(0, -PIXELSIZE);
				break;
			}
			case RIGHT: {
				head().add(0, PIXELSIZE);
				break;
			}
		}
	}

	public void grow() {
		body.add(new GameObject());
	}

	public boolean collide() {
		for (int i = 1; i < getLenght(); i++) {
			if (head().getX() == getBody(i).getX() &&
				head().getY() == getBody(i).getY()) {
				return true;
			}
		}
		return false;
	}

	public void render(GraphicsContext gc) {
		gc.setFill(Color.LIMEGREEN);
		for (int i = 0; i < getLenght(); i++) {
			body.get(i).render(gc);
		}
	}
}
