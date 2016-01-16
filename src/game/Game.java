package game;

import java.util.ArrayList;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.Timer;
import java.util.TimerTask;

public class Game {
	public static final String TITLE = "Sushi Survivor: Survival of the Sushiest";
	private ArrayList<String> myInput = new ArrayList<String>();
	private static final int LEVEL_DURATION = 2*1000;
	private Timer myLevelTimer;
	private Level myLevel;
	
	/*
	 * Returns the title of the game.
	 */
	public String getTitle() {
		return TITLE;
	}
	
	public void init(Stage stage) {
		// TODO: need to init a timer to switch levels
		TableLevel level1 = new TableLevel();
		System.out.println("created level 1");
		stage.setScene(level1.getScene());
		System.out.println("set scene");
		startLevel(level1, stage);
		System.out.println("started level 1");
		stage.show();
	}
	
	public void startLevel(Level level, Stage stage) {
		level.init(stage);
	}
	
	public void switchLevel(Stage stage, Level nextLevel) {
		//clear input
		//cancel all timers
		//level.init();
	}
}
