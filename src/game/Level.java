package game;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public abstract class Level {
	public static int NUM_SPRITES_PER_TYPE = 5;
	public static final int CANVAS_WIDTH = 1024;
	public static final int CANVAS_HEIGHT = 512;
	public static final long levelDuration = 2*1000;
	public static long myUpdateDuration = 200;
	private Timer myUpdateTimer;
	public GraphicsContext myGc;
	public Scene myScene;
	public Sushi sushi = new Sushi(0, CANVAS_HEIGHT/2);
	private ArrayList<String> myInput = new ArrayList<String>();
	
	public void init(Stage stage) {
		//initUpdateTimer();
		Group root = new Group();
		initScene(root);
		setupKeyEventHandler();
		populateSceneWithSprites();
		sushi.render(myGc);
		
		new AnimationTimer() {
			public void handle(long currentNanoTime) {
				myGc.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
				checkListCollisions();
				checkInput();
				updateCanvas();
				sushi.render(myGc);
			}
		}.start();
		
		stage.setScene(myScene);
	}
	
	public Scene getScene() {
		return myScene;
	}
	
	// TODO: update timer method isnt working 
	private void initUpdateTimer() {
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
	
	public void setupKeyEventHandler() {
		myScene.setOnKeyPressed(
				new EventHandler<KeyEvent>() {
					public void handle(KeyEvent e) {
						String code = e.getCode().toString();
						if (!myInput.contains(code)) { myInput.add(code); };
					}
				});
		
		myScene.setOnKeyReleased(
				new EventHandler<KeyEvent>() {
					public void handle(KeyEvent e) {
						String code = e.getCode().toString();
						myInput.remove(code);
					}
				});
	}
	
	protected abstract void populateSceneWithSprites();
	
	protected abstract void checkListCollisions();
	
	public boolean checkSpriteCollisions(ArrayList<Sprite> sprites) {
		for (int i = 0; i < sprites.size(); i++) {
			Sprite s = sprites.get(i);
			if (sushi.intersects(s.getBoundary())) {
				sprites.remove(s);
				return true;
			}
		}
		return false;
	}
	
	private void checkInput() {
		sushi.handleInput(myInput, CANVAS_WIDTH, CANVAS_HEIGHT, this.toString());
	}
	
	protected abstract void updateCanvas();
		
	protected abstract void updateSushi();
	
	public void gameOver() {
		
	}
}
