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
	private static final int LEVEL_DURATION = 3*60*1000;
	private Timer myLevelTimer;
	private Level myLevel;
	private Stage myStage;
	
	/*
	 * Returns the title of the game.
	 */
	public String getTitle() {
		return TITLE;
	}
	
	public void init() {
		// TODO: need to init a timer to switch levels
		startLevel(new TableLevel(), myStage);
		myStage.show();
	}
	
	public void startLevel(Level level, Stage stage) {
		myStage.setScene(level.getScene());
		level.init(stage);
	}
	
	public void scheduleLevelTimer() {
		myLevelTimer = new Timer(LEVEL_DURATION, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				myLevelTimer.stop();
				startLevel(new CustomerLevel(), myStage);
			}
		});
		myLevelTimer.start();
	}
}
