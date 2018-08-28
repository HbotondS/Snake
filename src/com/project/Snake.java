package com.project;


import static com.project.Contents.*;

public class Snake {

    private int[] snakeX = new int[WIDTH * HEIGHT];
    private int[] snakeY = new int[WIDTH * HEIGHT];

    private int length = 2;

    private boolean isMovingRight = false;
    private boolean isMovingLeft = false;
    private boolean isMovingUp = false;
    private boolean isMovingDown = false;

    public Snake(int x0, int y0, int x1, int y1) {
        snakeX[0] = x0;
        snakeY[0] = y0;
        snakeX[1] = x1;
        snakeY[1] = y1;
    }

    public int getHeadX() {
        return snakeX[0];
    }

    public int getHeadY() {
        return snakeY[0];
    }

    public void setHeadX(int index) {
        snakeX[0] = index;
    }

    public void setHeadY(int index) {
        snakeY[0] = index;
    }

    public int getBodyX(int index) {
        return snakeX[index];
    }

    public int getBodyY(int index) {
        return snakeY[index];
    }

    public int getLength() {
        return length;
    }

    public boolean isMovingRight() {
        return isMovingRight;
    }

    public boolean isMovingLeft() {
        return isMovingLeft;
    }

    public boolean isMovingUp() {
        return isMovingUp;
    }

    public boolean isMovingDown() {
        return isMovingDown;
    }

    public void setMovingRight(boolean movingRight) {
        isMovingRight = movingRight;
    }

    public void setMovingLeft(boolean movingLeft) {
        isMovingLeft = movingLeft;
    }

    public void setMovingUp(boolean movingUp) {
        isMovingUp = movingUp;
    }

    public void setMovingDown(boolean movingDown) {
        isMovingDown = movingDown;
    }

    public void grow() {
        ++length;
    }

    public void move() {
        if(isMovingRight || isMovingLeft || isMovingUp || isMovingDown) {
            for (int i = length - 1; i > 0; i--) {
                snakeX[i] = snakeX[i - 1];
                snakeY[i] = snakeY[i - 1];
            }
        }

        if(isMovingRight) {
            snakeX[0] += PIXELSIZE;
        }
        if(isMovingLeft) {
            snakeX[0] -= PIXELSIZE;
        }
        if(isMovingUp) {
            snakeY[0] -= PIXELSIZE;
        }
        if(isMovingDown) {
            snakeY[0] += PIXELSIZE;
        }
    }

    public boolean Die() {
        for (int i = 1; i < length; i++) {
            if(snakeX[0] == snakeX[i] && snakeY[0] == snakeY[i]) {
                return true;
            }
        }
        return false;
    }
}
