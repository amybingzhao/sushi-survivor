package game;

import java.util.ArrayList;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.Timer;
import java.util.TimerTask;

public class Game {
	public static final String TITLE = "Sushi Survivor: Survival of the Sushiest";
	private ArrayList<String> input = new ArrayList<String>();
	private ArrayList<Sprite> knifeList = new ArrayList<Sprite>();
	private ArrayList<Sprite> shrimpList = new ArrayList<Sprite>();
	private static final int LEVEL_DURATION = 2*1000;
	private Timer levelTimer;
	
	/*
	 * Returns the title of the game.
	 */
	public String getTitle() {
		return TITLE;
	}
	
	public Scene initScene(Group root) {
		Scene s = new Scene(root);
		
		return s;
	}
	
	public void switchLevel(Stage stage, Level level) {
		//clear input
		//cancel all timers
		//level.init();
	}
}
