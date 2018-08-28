package com.project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import static com.project.JavaFxForm.*;

public class Contents extends JPanel implements ActionListener{
    public static final int PIXELSIZE = 25;
    public static final int WIDTH = 1000;
    public static final int HEIGHT = 700;

    private boolean inGame;

    private Timer timer;

    private Snake snake = new Snake(500, 350, 475, 350);

    private Food food = new Food();

    private int score = 0;

    private long startTime;
    private long endTime;

    private int gameMode;

    private Color bodyColor;
    private Color headColor;

    public Contents(int gameMode, MyColor headColor, MyColor bodyColor) {
		timer = new Timer(300 / speed, this);
    	timer.start();
        startTime = System.currentTimeMillis();
        inGame = true;
        addKeyListener(new Keyboard());
        setFocusable(true);
        food.setrandomPosition();
        this.gameMode = gameMode;

        this.headColor = new Color(headColor.getRED(), headColor.getGREEN(), headColor.getBLUE());
        this.bodyColor = new Color(bodyColor.getRED(), bodyColor.getGREEN(), bodyColor.getBLUE());
    }

    @Override
    public void paint(Graphics g) {

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

		paintRect(Color.RED, food.getX(), food.getY(), g);

		for (int i = 1; i < snake.getLength(); i++) {
			paintRect(bodyColor, snake.getBodyX(i), snake.getBodyY(i), g);
		}

		paintSnakeHead(g);

        paintGrid(g);

        if(!inGame) {
            ImageIcon icon = new ImageIcon("Resources/game_over.jpg");

            JFrame gameOver = new JFrame("Game Over");
            gameOver.setSize(300, 300);
            gameOver.setResizable(false);
            gameOver.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            gameOver.setLocationRelativeTo(null);
            endTime = System.currentTimeMillis();
            gameOver.setContentPane(new GameOver(score, (endTime - startTime) / (double)1000).getMainPanel());
            gameOver.setIconImage(icon.getImage());
            gameOver.setVisible(true);
        }

        g.dispose();
    }

    private void paintRect(Color color, int x, int y, Graphics g) {
    	g.setColor(color);
    	g.fillRect(x, y, PIXELSIZE, PIXELSIZE);
	}

	private void paintSnakeEye(Graphics g) {
		if(!snake.isMovingRight() && !snake.isMovingDown() && !snake.isMovingLeft() && !snake.isMovingUp()
				|| snake.isMovingRight()) {
			g.drawLine(snake.getHeadX() + 15, snake.getHeadY() + 5, snake.getHeadX() + 20, snake.getHeadY() + 5);
			g.drawLine(snake.getHeadX() + 15, snake.getHeadY() + 20, snake.getHeadX() + 20, snake.getHeadY() + 20);
		}

		if (snake.isMovingLeft()) {
			g.drawLine(snake.getHeadX() + 5, snake.getHeadY() + 5, snake.getHeadX() + 10, snake.getHeadY() + 5);
			g.drawLine(snake.getHeadX() + 5, snake.getHeadY() + 20, snake.getHeadX() + 10, snake.getHeadY() + 20);
		}

		if (snake.isMovingUp()) {
			g.drawLine(snake.getHeadX() + 5, snake.getHeadY() + 5, snake.getHeadX() + 5, snake.getHeadY() + 10);
			g.drawLine(snake.getHeadX() + 20, snake.getHeadY() + 5, snake.getHeadX() + 20, snake.getHeadY() + 10);
		}

		if (snake.isMovingDown()) {
			g.drawLine(snake.getHeadX() + 5, snake.getHeadY() + 15, snake.getHeadX() + 5, snake.getHeadY() + 20);
			g.drawLine(snake.getHeadX() + 20, snake.getHeadY() + 15, snake.getHeadX() + 20, snake.getHeadY() + 20);
		}
	}

	private void paintSnakeHead(Graphics g) {
		if(!inGame) {
			paintRect(Color.RED, snake.getHeadX(), snake.getHeadY(), g);
			g.setColor(Color.WHITE);
		} else {
			paintRect(headColor, snake.getHeadX(), snake.getHeadY(), g);
			g.setColor(Color.RED);
		}

		paintSnakeEye(g);
	}

	private void paintGrid(Graphics g) {
		g.setColor(Color.GRAY);
		for (int i = 0; i < WIDTH; i += PIXELSIZE) {
			g.drawLine(i, 0, i, HEIGHT);
		}
		for (int i = 0; i < HEIGHT; i += PIXELSIZE) {
			g.drawLine(0, i, WIDTH, i);
		}
	}

    @Override
    public void actionPerformed(ActionEvent e) {

        if(snake.getHeadX() == food.getX() && snake.getHeadY() == food.getY()) {
            food.setrandomPosition();
            snake.grow();
            score += speed;
        }
        for (int i = 0; i < snake.getLength(); i++) {
            if(snake.getBodyX(i) == food.getX() && snake.getBodyY(i) == food.getY()) {
                food.setrandomPosition();
            }
        }

        snake.move();

        if(inGame) {
            repaint();
        }

        wentOut();

        if(snake.Die()) {
            inGame = false;
        }
    }

    private void wentOut() {
        if (gameMode == gameMode1) {
            if (snake.getHeadX() >= WIDTH || snake.getHeadX() < 0 || snake.getHeadY() >= HEIGHT-PIXELSIZE || snake.getHeadY() < 0) {
                inGame = false;
            }
        } else {
            if (gameMode == gameMode2) {
                if (snake.getHeadX() >= WIDTH) {
                    snake.setHeadX(0);
                } else {
                    if (snake.getHeadX() < 0) {
                        snake.setHeadX(WIDTH - PIXELSIZE);
                    } else {
                        if (snake.getHeadY() >= HEIGHT - PIXELSIZE) {
                            snake.setHeadY(0);
                        } else {
                            if (snake.getHeadY() < 0) {
                                snake.setHeadY(HEIGHT - 2*PIXELSIZE);
                            }
                        }
                    }
                }
            }
        }
    }

    private class Keyboard extends KeyAdapter {
        public void keyPressed(KeyEvent kEve) {
            int key = kEve.getKeyCode();

            if(key == KeyEvent.VK_RIGHT && !snake.isMovingLeft()) {
                snake.setMovingDown(false);
                snake.setMovingUp(false);
                snake.setMovingRight(true);
            }
            if(key == KeyEvent.VK_LEFT && !snake.isMovingRight()) {
                snake.setMovingDown(false);
                snake.setMovingUp(false);
                snake.setMovingLeft(true);
            }
            if(key == KeyEvent.VK_UP && !snake.isMovingDown()) {
                snake.setMovingLeft(false);
                snake.setMovingRight(false);
                snake.setMovingUp(true);
            }
            if(key == KeyEvent.VK_DOWN && !snake.isMovingUp()) {
                snake.setMovingLeft(false);
                snake.setMovingRight(false);
                snake.setMovingDown(true);
            }
        }
    }
}
