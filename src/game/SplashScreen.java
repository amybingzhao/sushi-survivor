// This entire file is part of my masterpiece.
// Amy Zhao (abz3)

// For my code masterpiece, I extracted this class from the Game class since it's not part of the game flow or mechanics in an attempt to solve the
// code smells issue of bloating the Game class into a large class. I think this is a better design because the Game and SplashScreen objects have
// separate purposes and mostly unrelated functions. Additionally, separating the classes makes it more obvious that the two do not necessarily
// depend on each other (i.e. you can change the SplashScreen image file and thus turn it into a splash screen for a different game that uses the 
// same algorithm to create itself and transition to the desired game.), making them more extensible.

// I also tried to remove most of the global variables used in this class to make dependencies both between and within methods more obvious, and split
// methods into smaller methods, hiding the details of creating the scene, progress text, progress bar, and layout of the splash screen such that these
// separate components can be more easily customized individually and so that the larger method that calls all of these create methods is more readable.

package game;

import game.Game.InitCompletionHandler;
import javafx.animation.FadeTransition;
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

public class SplashScreen {
	private static final int SPLASH_WIDTH = 500;
	private static final int SPLASH_HEIGHT = 250;
	private static final String SPLASH_IMAGE = "splashBackground.jpg";
	private static final int BOTTOM_BORDER = 35;
	private Game myGame;

	public SplashScreen(Game game) {
		myGame = game;
	}
	/*
	 * Displays the splash screen for the game.
	 */
	public void startSplashScreen(Stage stage) {
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
		
		Group splashLayout = createSplashLayout();
		Scene scene = createSplashScene(stage, splashLayout);
		ProgressBar loadProgress = createProgressBar();
		Label progressText = createProgressText();
		ImageView splashImage = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream(SPLASH_IMAGE)));
		splashLayout.getChildren().addAll(splashImage, loadProgress, progressText);		
		
		showProgressBar(stage, loadTask, () -> waitForPlayerReady(scene, stage, splashLayout), loadProgress, progressText);
		
		new Thread(loadTask).start();
	}
	
	/*
	 * Initializes the group that the splash screen's layout is built on.
	 */
	private Group createSplashLayout() {
		Group splashLayout = new Group();
		splashLayout.setEffect(new DropShadow());
		return splashLayout;
	}
	
	/*
	 * Initializes the progress bar for the splash screen.
	 */
	private ProgressBar createProgressBar() {
		ProgressBar loadProgress = new ProgressBar();
		loadProgress.setPrefWidth(SPLASH_WIDTH);
		return loadProgress;
	}
	
	/*
	 * Creates the progress text that is shown on the splash screen before and when done loading.
	 */
	private Label createProgressText() {
		Label progressText = new Label("Loading game...");
		progressText.setMinWidth(SPLASH_WIDTH);
		progressText.setAlignment(Pos.CENTER);
		return progressText;
	}
	
	/*
	 * Initializes the scene for the splash screen.
	 */
	public Scene createSplashScene(Stage stage, Group root) {
		Scene splashScene = new Scene(root);
		stage.setHeight(SPLASH_HEIGHT);
		stage.setWidth(SPLASH_WIDTH);
		stage.setScene(splashScene);
		return splashScene;
	}
	
	/*
	 * Generates the splash screen.
	 */
	public void showProgressBar(Stage stage, Task<?> task, InitCompletionHandler initCompletionHandler, ProgressBar loadProgress, Label progressText) {
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
	 * Sets onKeyPressed to begin the game once the player indicates he or she is ready.
	 */
	public void waitForPlayerReady(Scene scene, Stage stage, Group splashLayout) {	
		createReadyMessage(splashLayout);
		scene.setOnKeyPressed(
				new EventHandler<KeyEvent>() {
					public void handle(KeyEvent e) {
						fadeAndCloseSplashScreen(stage, splashLayout);
					}
				});
	}
	
	/*
	 * Creates the ready message for the splash screen.
	 */
	public void createReadyMessage(Group splashLayout) {
		Label readyLabel = new Label("Press any key to start!");
		readyLabel.setMinWidth(SPLASH_WIDTH);
		readyLabel.setMinHeight(SPLASH_HEIGHT-BOTTOM_BORDER);
		readyLabel.setFont(Font.font("Arial"));
		readyLabel.setAlignment(Pos.BOTTOM_CENTER);
        splashLayout.getChildren().add(readyLabel);
	}
	
	/*
	 * Fades the splash screen and continues onto hiding the splash screen and initializing the game.
	 */
	private void fadeAndCloseSplashScreen(Stage stage, Group splashLayout) {
		FadeTransition fadeSplash = new FadeTransition(Duration.seconds(1.2), splashLayout);
        fadeSplash.setFromValue(1.0);
        fadeSplash.setToValue(0.0);
        fadeSplash.setOnFinished(actionEvent -> hideStageAndInitGame(stage));
        fadeSplash.play();
	}
	
	/*
	 * Hides the stage for the splash screen and initializes the game.
	 */
	private void hideStageAndInitGame(Stage stage) {
		stage.hide();
		myGame.init(new Stage());
	}	
}
