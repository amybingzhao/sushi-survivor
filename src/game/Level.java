package game;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

public class Level {
	public static int NUM_SPRITES_PER_TYPE;
	public static final int CANVAS_WIDTH = 1024;
	public static final int CANVAS_HEIGHT = 512;
	public static final long levelDuration = 2*1000;
	public static long updateDuration = 200;
	private Timer updateTimer;
	public GraphicsContext gc;
	
	public void init(Stage stage) {
		initUpdateTimer();
		Group root = new Group();
		Scene scene = initScene(root);
		//generateSprites();
		
		new AnimationTimer() {
			public void handle(long currentNanoTime) {
				//checkCollisions();
				//checkInput(gc);
				//updateCanvas(gc);
				//updateSushi();
			}
		}.start();
		
		stage.setScene(scene);
	}
	
	private void initUpdateTimer() {
		updateTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				this.handleTimer();
			}

			private void handleTimer() {
				// TODO Auto-generated method stub
				//update sprites				
			}
		}, updateDuration, updateDuration);
	}
	
	private Scene initScene(Group root) {
		Scene scene = new Scene(root);
		Canvas canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
		root.getChildren().add(canvas);
		gc = canvas.getGraphicsContext2D();
		return scene;
	}
	
	private void generateSprites() {
		
	}
	
	private void checkCollisions() {
		
	}
	
	private void checkInput(GraphicsContext gc) {
		
	}
	
	private void updateCanvas(GraphicsContext gc) {
		// leave empty and just override in individual levels?
	}
	
	private void updateSushi() {
		
	}
}
