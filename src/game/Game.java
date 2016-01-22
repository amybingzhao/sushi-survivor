package game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
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
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Game {
	private static final String TITLE = "Sushi Survivor: Survival of the Sushiest";
	private static final int LEVEL_DURATION = 20 * 1000;
	private Timer myLevelTimer;
	private Level myLevel;
	private Stage myStage;
	private Label progressText;
	private ProgressBar loadProgress;
	private static final int SPLASH_WIDTH = 500;
	private static final int SPLASH_HEIGHT = 250;
	private Group splashLayout;
	private static final String SPLASH_IMAGE = "splashBackground.jpg";
	private static final int BOTTOM_BORDER = 35;
	private Game myGame;
	private boolean gameInit = false;
	
	/*
	 * Returns the title of the game.
	 */
	public String getTitle() {
		return TITLE;
	}

	/*
	 * Initializes the game.
	 * @param: stage is the stage on which the game is to be played.
	 * @param: game is the game that the level is a part of.????????????????????????????
	 */
	public void init(Stage stage, Game game) {
		if (myLevelTimer != null) {
			myLevelTimer.cancel();
		}
		myStage = stage;
		myGame = game;
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
			startLevel(new CustomerLevel(numStartingFish), myStage, myGame);
		}
	}
	
	/*
	 * Returns the timer for the level.
	 */
	public Timer getLevelTimer() {
		return myLevelTimer;
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
	
	/*
	 * Displays the splash screen for the game.
	 */
	public void showSplash(Stage stage) {
		final Task<ObservableList<String>> loadTask = new Task<ObservableList<String>>() {

			@Override
			protected ObservableList<String> call() throws Exception {
				ObservableList<String> tasksToLoad = FXCollections.observableArrayList(
						"Generating backgrounds", "Loading sprites", "Creating sushi", "Loading levels"
						);
				
				for (int i = 0; i < tasksToLoad.size(); i++) {
					Thread.sleep(400);
					String nextTask = tasksToLoad.get(i);
					updateMessage(nextTask + "...");
				}
				
				return tasksToLoad;
			}
		};
		Scene scene = createSplashScene(stage);
		generateSplash(stage, loadTask, () -> waitForPlayerReady(scene, stage));
		
		new Thread(loadTask).start();
	}
	
	/*
	 * Sets onKeyPressed to begin the game once the player indicates he or she is ready.
	 */
	public void waitForPlayerReady(Scene scene, Stage stage) {	
		createReadyMessage();
		scene.setOnKeyPressed(
				new EventHandler<KeyEvent>() {
					public void handle(KeyEvent e) {
						FadeTransition fadeSplash = new FadeTransition(Duration.seconds(1.2), splashLayout);
		                fadeSplash.setFromValue(1.0);
		                fadeSplash.setToValue(0.0);
		                fadeSplash.setOnFinished(actionEvent -> hideStageAndInitGame(stage));
		                fadeSplash.play();
					}
				});
	}		
	
	/*
	 * Hides the stage for the splash screen and initializes the game.
	 */
	private void hideStageAndInitGame(Stage stage) {
		stage.hide();
		if (gameInit == false) {
			gameInit = true;
			init(new Stage(), new Game());
		}
	}
	
	/*
	 * Generates the splash screen.
	 */
	public void generateSplash(Stage stage, Task<?> task, InitCompletionHandler initCompletionHandler) {
		progressText.textProperty().bind(task.messageProperty());
		loadProgress.progressProperty().bind(task.progressProperty());
		task.stateProperty().addListener((observableValue, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                loadProgress.progressProperty().unbind();
                loadProgress.setProgress(1);
                stage.toFront();
               
                initCompletionHandler.complete();
            }
        });
		
		stage.show();	
	}
	
	/*
	 * Creates the ready message for the splash screen.
	 */
	public void createReadyMessage() {
		Label readyLabel = new Label("Press any key to start!");
		readyLabel.setMinWidth(SPLASH_WIDTH);
		readyLabel.setMinHeight(SPLASH_HEIGHT-BOTTOM_BORDER);
		readyLabel.setFont(Font.font("Arial"));
		readyLabel.setAlignment(Pos.BOTTOM_CENTER);
        splashLayout.getChildren().add(readyLabel);
	}
	
	/*
	 * Initializes the scene for the splash screen.
	 */
	public Scene createSplashScene(Stage stage) {
		ImageView splash = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream(SPLASH_IMAGE)));
		splashLayout = new Group();
		splashLayout.setEffect(new DropShadow());
		loadProgress = new ProgressBar();
		loadProgress.setPrefWidth(SPLASH_WIDTH);
		progressText = new Label("Loading game...");
		progressText.setMinWidth(SPLASH_WIDTH);
		progressText.setAlignment(Pos.CENTER);
		splashLayout.getChildren().addAll(splash, loadProgress, progressText);
		
		Scene splashScene = new Scene(splashLayout);
		stage.setHeight(SPLASH_HEIGHT);
		stage.setWidth(SPLASH_WIDTH);
		stage.setScene(splashScene);
		return splashScene;
	}
	
	public interface InitCompletionHandler {
        public void complete();
    }
}


