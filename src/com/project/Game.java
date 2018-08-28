package com.project;

import javax.swing.*;

public class Game extends JFrame{
    private ImageIcon icon = new ImageIcon("Resources/snake_icon.png");

    public Game(int gameMode, MyColor headColor, MyColor bodyColor) {
        setTitle("Game");
        setSize(1005, 705);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(new Contents(gameMode, headColor, bodyColor));
        setIconImage(icon.getImage());
        setVisible(true);
    }
}
