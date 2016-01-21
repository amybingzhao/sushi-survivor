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
	private static final int LEVEL_DURATION = 10 * 1000;
	//private static final int LEVEL_DURATION = 2 * 1000 * 60;
	private Timer myLevelTimer;
	private Level myLevel;
	private Stage myStage;
	private Label progressText;
	private ProgressBar loadProgress;
	private static final int SPLASH_WIDTH = 500;
	private static final int SPLASH_HEIGHT = 250;
	//private Pane splashLayout;
	private Group splashLayout;
	private static final String SPLASH_IMAGE = "splashBackground.jpg";
	private static final int BOTTOM_BORDER = 35;
	/*
	 * Returns the title of the game.
	 */
	public String getTitle() {
		return TITLE;
	}

	public void init(Stage stage) {
		myStage = stage;
		scheduleTableLevelTimer();
		startLevel(new TableLevel((double) 0), myStage);
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
						System.out.println("level's game over status: " + String.valueOf(myLevel.isGameOver()));
						if (myLevel.isGameOver() == false) {
							myLevel.setStopLevel(true);
							double numStartingFish = myLevel.getSushi().getNumFish();
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
						endGame();
						System.out.println("customer timer stopped");
					}
				});
			}
		}, LEVEL_DURATION);
	};
	
	public void endGame() {
		myLevel.setStopLevel(true);
		myLevel.setGameOver(true);
		if (myLevel.getSushi().getNumFish() > 0) {
			myLevel.setWin(true);
		} else {
			myLevel.setWin(false);
		}
		myLevel.gameOver();
	}
	
	public void showSplash(Stage stage) {
		final Task<ObservableList<String>> loadTask = new Task<ObservableList<String>>() {

			@Override
			protected ObservableList<String> call() throws Exception {
				// TODO Auto-generated method stub
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
	
	private void hideStageAndInitGame(Stage stage) {
		stage.hide();
		init(new Stage());
	}
	
	public void generateSplash(Stage stage, Task<?> task, InitCompletionHandler initCompletionHandler) {
		progressText.textProperty().bind(task.messageProperty());
		loadProgress.progressProperty().bind(task.progressProperty());
		task.stateProperty().addListener((observableValue, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                loadProgress.progressProperty().unbind();
                loadProgress.setProgress(1);
                stage.toFront();
                /**/
                initCompletionHandler.complete();
            } // todo add code to gracefully handle other task states.
        });
		
		stage.show();	
	}
	
	public void createReadyMessage() {
		System.out.println("trying to show this");
		Label readyLabel = new Label("Press any key to start!");
		readyLabel.setMinWidth(SPLASH_WIDTH);
		readyLabel.setMinHeight(SPLASH_HEIGHT-BOTTOM_BORDER);
		readyLabel.setFont(Font.font("Arial"));
		readyLabel.setAlignment(Pos.BOTTOM_CENTER);
        splashLayout.getChildren().add(readyLabel);
	}
	
	public Scene createSplashScene(Stage stage) {
		ImageView splash = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream(SPLASH_IMAGE)));
		//splashLayout = new VBox();
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


