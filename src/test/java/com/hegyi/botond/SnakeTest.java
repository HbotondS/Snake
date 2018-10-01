package com.hegyi.botond;

import com.sun.javafx.scene.traversal.Direction;
import javafx.geometry.Point2D;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SnakeTest {
	private Snake snake;
	private Point2D head;
	private Point2D tail;

	@Before
	public void init() {
		head = new Point2D(1, 0);
		tail = new Point2D(0, 0);
		snake = new Snake(head, tail, 1);
		snake.setDirection(Direction.DOWN);
	}

	@Test
	public void test_headPosition() {
		Assert.assertEquals(head, snake.getHead().getPosition());
	}

	@Test
	public void test_getBodyPosition() {
		Assert.assertEquals(tail, snake.getBody(1).getPosition());
	}

	@Test
	public void test_getLength() {
		Assert.assertEquals(2, snake.getLength());
	}

	@Test
	public void test_getDirection() {
		Assert.assertEquals(Direction.DOWN, snake.getDirection());
	}

	@Test
	public void test_grow() {
		snake.grow();

		Assert.assertEquals(3, snake.getLength());
	}

	@Test
	public void test_collide() {
		snake = new Snake(new Point2D(0, 0), tail, 25);

		Assert.assertTrue(snake.collide());
	}

	@Test
	public void test_move_right() {
		snake.setDirection(Direction.RIGHT);
		snake.move();

		Assert.assertEquals(new Point2D(2, 0), snake.getHead().getPosition());
		Assert.assertEquals(new Point2D(1, 0), snake.getBody(1).getPosition());
	}

	@Test
	public void test_move_left() {
		snake.setDirection(Direction.LEFT);
		snake.move();

		Assert.assertEquals(new Point2D(0, 0), snake.getHead().getPosition());
		Assert.assertEquals(new Point2D(1, 0), snake.getBody(1).getPosition());
	}

	@Test
	public void test_move_down() {
		snake.setDirection(Direction.DOWN);
		snake.move();

		Assert.assertEquals(new Point2D(1, 1), snake.getHead().getPosition());
		Assert.assertEquals(new Point2D(1, 0), snake.getBody(1).getPosition());
	}

	@Test
	public void test_move_up() {
		snake.setDirection(Direction.UP);
		snake.move();

		Assert.assertEquals(new Point2D(1, -1), snake.getHead().getPosition());
		Assert.assertEquals(new Point2D(1, 0), snake.getBody(1).getPosition());
	}

	@Test
	public void testHeadDirection() {
		snake.move();
		Assert.assertEquals(Direction.DOWN, snake.getHead().getDirection());
	}

	@Test
	public void testBodyDirection() {
		snake.setDirection(Direction.UP);
		snake.move();
		snake.move();
		Assert.assertEquals(Direction.UP, snake.getBody(1).getDirection());
	}
}
