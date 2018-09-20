package com.hegyi.botond;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;

import java.util.Random;

import static com.hegyi.botond.Game.PIXELSIZE;

public class GameObject implements Renderable {
	protected Point2D position;
	protected double width;
	protected double height;
	protected boolean alive;

	public GameObject() {
		position = new Point2D(0, 0);
		alive = true;
	}

	public GameObject(double width, double height) {
		position = new Point2D(0, 0);
		this.width = width;
		this.height = height;
		alive = true;
	}

	public GameObject(Point2D position, double width, double height) {
		this.position = position;
		this.width = width;
		this.height = height;
		alive = true;
	}

	public Point2D getPosition() {
		return position;
	}

	public void setPosition(Point2D position) {
		this.position = position;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public boolean isAlive() {
		return alive;
	}

	public void die() {
		alive = false;
	}

	public void render(GraphicsContext gc) {
		if (alive) {
			gc.fillRect(position.getX(), position.getY(), width, height);
		}
	}

	public Rectangle2D getBoundary() {
		return new Rectangle2D(position.getX(), position.getY(), width, height);
	}

	public boolean intersect(GameObject other) {
		return other.getBoundary().intersects(this.getBoundary());
	}

	public void setRandomPosition(int width, int height) {
		Random random = new Random();
		setPosition(new Point2D(random.nextInt((width) / PIXELSIZE) * PIXELSIZE,
				random.nextInt((height) / PIXELSIZE) * PIXELSIZE));
	}
}
