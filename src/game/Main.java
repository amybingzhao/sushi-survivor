package game;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
	public static void main(String[] args) {
		launch(args);
	}
	
	public void start(Stage theStage) {
		Game myGame = new Game();
		theStage.setTitle(myGame.getTitle());
		
		theStage.show();
	}
	
}
