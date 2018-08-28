package com.project;

import java.util.Random;

import static com.project.Contents.PIXELSIZE;
import static com.project.Contents.WIDTH;
import static com.project.Contents.HEIGHT;

public class Food {
    private int x;
    private int y;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setrandomPosition() {
        Random random = new Random();
        this.x = random.nextInt((WIDTH-25) / PIXELSIZE) * PIXELSIZE;
        this.y = random.nextInt((HEIGHT-25) / PIXELSIZE) * PIXELSIZE;
    }
}
