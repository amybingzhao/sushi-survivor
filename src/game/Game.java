package game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Timer;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Game {
	public static final String TITLE = "Sushi Survivor: Survival of the Sushiest";
	private static final int LEVEL_DURATION = 1 * 60 * 1000;
	//private static final int LEVEL_DURATION = 2*1000;
	private Timer myLevelTimer;
	private Level myLevel;
	private Stage myStage;
	
	/*
	 * Returns the title of the game.
	 */
	public String getTitle() {
		return TITLE;
	}
	
	public void init(Stage stage) {
		// TODO: need to init a timer to switch levels
		myStage = stage;
		scheduleLevelTimer();
		//startLevel(new TableLevel(), myStage);
		startLevel(new CustomerLevel(), myStage);
		myStage.show();
	}
	
	public void startLevel(Level level, Stage stage) {
		myStage.setScene(level.getScene());
		System.out.println("Scene set");
		level.init(stage);
	}
	
	public void scheduleLevelTimer() {
		myLevelTimer = new Timer(LEVEL_DURATION, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				startLevel(new CustomerLevel(), myStage);
				myLevelTimer.stop();
				System.out.println("level timer stopped");
			}
		});
		myLevelTimer.start();
		System.out.println("Level timer started");
	}
}
