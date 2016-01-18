package game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Game {
	public static final String TITLE = "Sushi Survivor: Survival of the Sushiest";
	private static final int LEVEL_DURATION = 10 * 1000;
	//private static final int LEVEL_DURATION = 2*1000;
	private Timer myLevelTimer;
	private Level myLevel;
	private Stage myStage;
	private long startTime;

	/*
	 * Returns the title of the game.
	 */
	public String getTitle() {
		return TITLE;
	}

	public void init(Stage stage) {
		// TODO: need to init a timer to switch levels
		myStage = stage;
		scheduleTableLevelTimer();
		startLevel(new TableLevel((double) 0), myStage);
		startTime = System.currentTimeMillis();
		myStage.show();
	}

	public void startLevel(Level level, Stage stage) {
		myLevel = level;
		level.init(stage);
		myStage.setScene(level.getScene());
	}

	public void scheduleTableLevelTimer() {
		myLevelTimer = new Timer();

		myLevelTimer.schedule(new TimerTask() {
			public void run() {
				Platform.runLater(new Runnable() {
					public void run() {
						System.out.println("level's game over status: " + String.valueOf(myLevel.gameOver));
						if (myLevel.gameOver == false) {
							myLevel.stopLevel = true;
							double numStartingFish = myLevel.sushi.numFish;
							startLevel(new CustomerLevel(numStartingFish), myStage);
							scheduleCustomerLevelTimer();
							System.out.println("level timer stopped");
						}
					}
				});
			}
		}, LEVEL_DURATION);
	};

	public void scheduleCustomerLevelTimer() {
		myLevelTimer = new Timer();

		myLevelTimer.schedule(new TimerTask() {
			public void run() {
				Platform.runLater(new Runnable() {
					public void run() {
						myLevel.stopLevel = true;
						myLevel.gameOver = true;
						myLevel.win = true;
						myLevel.gameOver();
						System.out.println("customer timer stopped");
					}
				});
			}
		}, LEVEL_DURATION);
	};

}

