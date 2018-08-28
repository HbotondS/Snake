package com.project;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class GameOver extends JFrame{
    private JPanel mainPanel;
    private JLabel gameOver;
    private JButton Save;
    private JButton cancelButton;
    private JLabel scoreLabel;
	private JLabel timeLabel;
	private JLabel newScore;

	private HighScores scores;

	public GameOver(int score, double time) {

        scoreLabel.setText("Score: " + String.valueOf(score));
        timeLabel.setText("Your time " + time + " sec");
        newScore.setVisible(false);

        scores = new HighScores("Resources/highscores.save");
        if (scores.getHighScore() < score) {
        	newScore.setVisible(true);
		}

		cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int dialog = JOptionPane.showConfirmDialog(null, "Are you sure you want to quit without saving?",
                        "Warning", JOptionPane.YES_NO_OPTION);
                if(dialog == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
                if(dialog == JOptionPane.NO_OPTION) {
                    //DO NOTHING
                }
            }
        });

        Save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int g = -1;
                while (g < 0) {
					String input = JOptionPane.showInputDialog("Enter your name");
					if (input != null) {
						if (input.length() > 0) {
							g++;
							Writer writer = null;
							try {
								writer = new BufferedWriter(new OutputStreamWriter(
										new FileOutputStream("Resources/highscores.save"), "UTF-8"));
								scores.save(new HighScore(input, score));
								for (int i = 0; i < 10; i++) {
									writer.write(scores.getscore(i).getName() +
											" " + scores.getscore(i).getScore() + "\n");
								}
							} catch (IOException ex) {
								//report
							} finally {
								try {
									writer.close();
								} catch (Exception ex) {
									//ignore
								}
							}
						}
					} else {
						break;
					}
				}
				System.exit(1);
            }
        });
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
