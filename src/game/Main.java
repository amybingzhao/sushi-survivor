package game;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
	public static void main(String[] args) {
		launch(args);
	}
	
	public void start(Stage theStage) {
		loadSplashScreen(new Game(), theStage);
	}	
	
	public void loadSplashScreen(Game game, Stage stage) {
		SplashScreen s = new SplashScreen(game);
		s.startSplashScreen(stage);
	}
}
