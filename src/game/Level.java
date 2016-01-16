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

public abstract class Level {
	public static int NUM_SPRITES_PER_TYPE;
	public static final int CANVAS_WIDTH = 1024;
	public static final int CANVAS_HEIGHT = 512;
	public static final long levelDuration = 2*1000;
	public static long myUpdateDuration = 200;
	private Timer myUpdateTimer;
	public GraphicsContext myGc;
	public Scene myScene;
	
	public void init(Stage stage) {
		//initUpdateTimer();
		System.out.println("update timer inited");
		Group root = new Group();
		initScene(root);
		//populateSceneWithSprites();
		
		new AnimationTimer() {
			public void handle(long currentNanoTime) {
				//checkCollisions();
				//checkInput(gc);
				//updateCanvas(gc);
				//updateSushi();
			}
		}.start();
		
		stage.setScene(myScene);
	}
	
	public Scene getScene() {
		return myScene;
	}
	
	// TODO: update timer method isnt working 
	private void initUpdateTimer() {
		System.out.println("hi");
		myUpdateTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				// want to increase game speed
				System.out.println("Update timed out");
			}
		}, myUpdateDuration, myUpdateDuration);
		System.out.println("done with initupdatetimer");
	}
	
	private void initScene(Group root) {
		Scene scene = new Scene(root);
		Canvas canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
		root.getChildren().add(canvas);
		myScene = scene;
		myGc = canvas.getGraphicsContext2D();
		System.out.println("inited scene");
	}
	
	protected abstract void populateSceneWithSprites();
	
	private void checkCollisions() {
		
	}
	
	private void checkInput(GraphicsContext gc) {
		
	}
	
	private void updateCanvas(GraphicsContext gc) {
		// leave empty and just override in individual levels?
	}
	
	protected abstract void moveSpritesForward(ArrayList<Sprite> sprites);
	
	private void updateSushi() {
		
	}
}
