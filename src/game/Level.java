package game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public abstract class Level {
	public static final int NUM_SPRITES_PER_TYPE = 5;
	public static final int CANVAS_WIDTH = 1024;
	public static final int CANVAS_HEIGHT = 512;
	public static final int UPDATE_DURATION = 10 * 1000;
	public static final double INIT_SPRITE_SPEED = 2.0;
	private Timer myUpdateSpeedTimer;
	public GraphicsContext myGc;
	public Scene myScene;
	public Sushi sushi;
	private ArrayList<String> myInput;
	public double spriteSpeed;
	private Stage myStage;
	private Canvas myCanvas;
	public boolean gameOver;
	public boolean win;
	public boolean start;
	public boolean stopLevel;
	private Group myRoot;
	private Label scoreLabel;
	
	public void init(Stage stage) {
		myStage = stage;
		myRoot = new Group();
		initScene(myRoot);
		populateSceneWithSprites();
		initLevelState();
		setupKeyEventHandler();
		
		Label readyLabel = createReadyMessage();
		myRoot.getChildren().add(readyLabel);
		
		new AnimationTimer() {
			public void handle(long currentNanoTime) {
				if (start == true) {
					readyLabel.setText("");;
					if (stopLevel == true) {
						myUpdateSpeedTimer.cancel();
						myUpdateSpeedTimer.purge();
						stop();
						if (gameOver == true) {
							gameOver();
						}
					}
					myGc.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
					checkListCollisions();
					checkInput();
					updateCanvas();
					sushi.render(myGc);
				} else {
					checkForReady();
				}
			}
		}.start();
		
		stage.setScene(myScene);
	}
	
	private void initLevelState() {
		myInput = new ArrayList<String>();
		spriteSpeed = INIT_SPRITE_SPEED;
		scoreLabel = new Label("Score: " + Double.toString(sushi.numFish));
		start = false;
		gameOver = false;
		win = false;
		stopLevel = false;
	}
	
	private void checkForReady() {
		if (myInput.contains("ENTER")) {
			start = true;
			scheduleUpdateTimer();
		}
	}
	public Label createReadyMessage() {
		Label readyLabel = new Label("Press ENTER to start!");
		readyLabel.setMinWidth(CANVAS_WIDTH);
		readyLabel.setMinHeight(CANVAS_HEIGHT);
		readyLabel.setAlignment(Pos.CENTER);
		readyLabel.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 35));
		return readyLabel;
	}
	
	public Scene getScene() {
		return myScene;
	}
	
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
		myScene = new Scene(root);
		myCanvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
		root.getChildren().add(myCanvas);
		myGc = myCanvas.getGraphicsContext2D();
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
		createGameOverScene();
	}
	
	private void createGameOverScene() {
		Group root = new Group();
		initScene(root);
		Label gameOverLabel = createGameOverLabel();
		//Button playAgain = new Button();
		String score = Integer.toString((int) sushi.numFish);
		if (win == true) {
			gameOverLabel.setText("You survived!\n" + "Score: " + score);
			//playAgain.setText("Click to play again!");
		} else {
			gameOverLabel.setText("So close yet so far...\n" + "Score: " + score);
			//playAgain.setText("Click to try again!");
		}
		//playAgain.setContentDisplay(ContentDisplay.BOTTOM);;
		root.getChildren().addAll(gameOverLabel);
		myScene.setFill(Color.GRAY);
		myStage.setScene(myScene);
		myStage.show();
		//Platform.exit();
	}
	
	private Label createGameOverLabel() {
		Label gameOverLabel = new Label();
		gameOverLabel.setMinWidth(CANVAS_WIDTH);
		gameOverLabel.setMinHeight(CANVAS_HEIGHT);
		gameOverLabel.setAlignment(Pos.CENTER);
		gameOverLabel.setTextAlignment(TextAlignment.CENTER);
		gameOverLabel.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 35));
		return gameOverLabel;
	}
}
