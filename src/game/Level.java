package game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
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
	public static final int UPDATE_DURATION = 10 * 1000;
	private Timer myUpdateSpeedTimer;
	public GraphicsContext myGc;
	public Scene myScene;
	public Sushi sushi;
	private ArrayList<String> myInput;
	public double spriteSpeed = 2.0;
	
	public void init(Stage stage) {
		scheduleUpdateTimer();
		Group root = new Group();
		initScene(root);
		setupKeyEventHandler();
		populateSceneWithSprites();
		myInput = new ArrayList<String>();
		
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
	private void scheduleUpdateTimer() {
		myUpdateSpeedTimer = new Timer();
		myUpdateSpeedTimer.schedule(new TimerTask() {
			public void run() {
				Platform.runLater(new Runnable() {
					public void run() {
						sushi.speed = sushi.speed + 0.3;
						spriteSpeed = spriteSpeed + 0.3;
						System.out.println("update timer timed out");
					}
				});
			}
		}, UPDATE_DURATION, UPDATE_DURATION);	
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
	
	public void populateSpriteArrayList(String filename, ArrayList<Sprite> array) {
		for (int i = 0; i < NUM_SPRITES_PER_TYPE; i++) {
			Sprite s = generateSprite(filename);
			array.add(s);
			s.render(myGc);
		}
	}

	public Sprite generateSprite(String filename) {
		// TODO Auto-generated method stub
		Sprite sprite = new Sprite();
		sprite.setImage(filename);
		double x = generateRandomX(sprite);
		double y = generateRandomY(sprite);
		sprite.setPosition(x, y);
		return sprite;
	}
	
	protected abstract double generateRandomX(Sprite sprite);
	protected abstract double generateRandomY(Sprite sprite);
	
	public void replaceOutOfBoundsSprites(ArrayList<Sprite> sprites, String filename) {
		for (int i = 0; i < sprites.size(); i++) {
			Sprite s = sprites.get(i);
			if (outOfBounds(s)) {
				sprites.remove(i);
				addMoreSprites(sprites, filename, 1);
			}
		}
		// replace the ones that've been collided with:
		// TODO: make into own method
		int diff = NUM_SPRITES_PER_TYPE - sprites.size();
		addMoreSprites(sprites, filename, diff);
	}
	
	protected abstract boolean outOfBounds(Sprite s);
	
	public void addMoreSprites(ArrayList<Sprite> sprites, String filename, int num) {
		for (int i = 0; i < num; i++) {
			Sprite s = generateSprite(filename);
			switch (this.toString()) {
				case "Table Level":
					s.posX = CANVAS_WIDTH;
					break;
				case "Customer Level":
					s.posY = 0;
			}
			sprites.add(s);
			s.render(myGc);
		}
	}
	
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
