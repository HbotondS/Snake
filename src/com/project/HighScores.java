package com.project;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class HighScores {
	private ArrayList<HighScore> scores = new ArrayList<>();

	public HighScores(String fileName) {
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File(fileName));
		} catch (FileNotFoundException ex) {
			System.out.println("The file doesn't found.");
			System.exit(1);
		}
		for (int i = 0; scanner.hasNextLine(); i++) {
			StringTokenizer stk = new StringTokenizer(scanner.nextLine(), " ");
			HighScore temp = new HighScore();
			temp.setName(stk.nextToken());
			temp.setScore(Integer.parseInt(stk.nextToken()));

			scores.add(temp);
		}
	}

	public int getHighScore() {
		return scores.get(0).getScore();
	}

	public HighScore getscore(int index) {
		return scores.get(index);
	}

	public void save(HighScore score) {
		scores.add(score);
		Collections.sort(scores, new MyComparator());
	}

	private static class MyComparator implements Comparator<HighScore> {
		@Override
		public int compare(HighScore o1, HighScore o2) {
			return o2.getScore() - o1.getScore();
		}
	}
}
