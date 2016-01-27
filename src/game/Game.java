package game;

import java.util.Timer;
import java.util.TimerTask;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Game {
	private static final String TITLE = "Sushi Survivor: Survival of the Sushiest";
	private static final int LEVEL_DURATION = 60 * 1000;
	private Timer myLevelTimer;
	private Level myLevel;
	private Stage myStage;
	
	/*
	 * Returns the title of the game.
	 */
	public String getTitle() {
		return TITLE;
	}

	/*
	 * Initializes the game.
	 * @param: stage is the stage on which the game is to be played.
	 * @param: newGame is true if a new Game is to be started, false if not
	 */
	public void init(Stage stage) {
		stage.setTitle(TITLE);
		if (myLevelTimer != null) {
			myLevelTimer.cancel();
		}
		myStage = stage;
		startLevel(new TableLevel((double) 0), myStage, this);
		myStage.show();
	}

	/* 
	 * Starts the level.
	 * @param: level is the level to be started.
	 * @param: stage is the stage on which the level is to be played.
	 * @param: game is the game that the level is a part of.
	 */
	public void startLevel(Level level, Stage stage, Game game) {
		myLevel = level;
		level.init(stage, game);
		myStage.setScene(level.getScene());
	}
	
	/*
	 * Switches from current level to the Customer Level.
	 */
	public void switchToCustomerLevel() {
		if (myLevel.isGameOver() == false) {
			double numStartingFish = myLevel.getSushi().getNumFish();
			startLevel(new CustomerLevel(numStartingFish), myStage, this);
		}
	}

	/*
	 * Schedules the timer for the customer level. Once the timer expires, the game is over.
	 */
	public void scheduleCustomerLevelTimer(Level level) {
		myLevelTimer = new Timer();

		myLevelTimer.schedule(new TimerTask() {
			public void run() {
				Platform.runLater(new Runnable() {
					public void run() {
						level.setGameOver(true);
						endGame(level);
					}
				});
			}
		}, LEVEL_DURATION);
	};
	
	/*
	 * Returns the timer for the level.
	 */
	public Timer getLevelTimer() {
		return myLevelTimer;
	}
	
	/*
	 * Sets the level state for ending the game.
	 */
	public void endGame(Level level) {
		level.setStopLevel(true);
		if (level.getSushi().getNumFish() > 0) {
			level.setWin(true);
		} else {
			level.setWin(false);
		}
		level.gameOver();
	}
	
	public interface InitCompletionHandler {
        public void complete();
    }
}


