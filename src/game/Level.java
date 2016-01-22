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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public abstract class Level {
	private static final int ONE_SECOND = 1000;
	private static final int NUM_SPRITES_PER_TYPE = 5;
	private static final int CANVAS_WIDTH = 1024;
	private static final int CANVAS_HEIGHT = 512;
	private static final int UPDATE_DURATION = 10 * ONE_SECOND;
	private static final double INIT_SPRITE_SPEED = 2.0;
	private static final int SPRITE_SPAWN_INTERVAL = ONE_SECOND;
	private static final int CHEAT_DURATION = 2 * ONE_SECOND;
	private static final String TABLE_LEVEL_NAME = "Table Level";
	private static final String CUSTOMER_LEVEL_NAME = "Customer Level";
	private Timer myUpdateSpeedTimer;
	private Timer mySpriteSpawnTimer;
	private GraphicsContext myGc;
	private Scene myScene;
	private Sushi sushi;
	private ArrayList<String> myInput;
	private double spriteSpeed;
	private Stage myStage;
	private Canvas myCanvas;
	private boolean gameOver;
	private boolean win;
	private boolean start;
	private boolean stopLevel;
	private StackPane myRoot;
	private Label scoreLabel;
	private Game myGame;
	private ImageView myBackground;
	
	/*
	 * Initializes the level and begins the animation timer.
	 * @param: stage is the stage on which the level will be displayed.
	 * @param: game is the game that created the level.   
	 */
	public void init(Stage stage, Game game) {
		myStage = stage;
		myStage.setWidth(CANVAS_WIDTH);
		StackPane root = new StackPane();
		myGame = game;
		initScene(root);
		populateSceneWithSprites();
		initLevelState();
		setupKeyEventHandler();
		
		Label readyLabel = createReadyMessage();
		myRoot.getChildren().add(readyLabel);
		
		
		new AnimationTimer() {
			public void handle(long currentNanoTime) {
				if (start == true) {
					readyLabel.setText("");;
					if (isStopLevel() == true) {
						cancelTimers();
						stop();
						if (isGameOver() == true) {
							gameOver();
							if (this.toString().equals(CUSTOMER_LEVEL_NAME)) {
								myGame.getLevelTimer().cancel();
							}
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
	
	/*
	 * Cancels recurring timers from this level.
	 */
	private void cancelTimers() {
		myUpdateSpeedTimer.cancel();
		myUpdateSpeedTimer.purge();
		mySpriteSpawnTimer.cancel();
		mySpriteSpawnTimer.purge();
	}
	
	/*
	 * Returns the canvas height.
	 */
	protected int getCanvasHeight() {
		return CANVAS_HEIGHT;
	}
	
	/*
	 * Returns the canvas width.
	 */
	protected int getCanvasWidth() {
		return CANVAS_WIDTH;
	}
	
	/*
	 * Returns the graphics context for the level's canvas.
	 */
	protected GraphicsContext getGraphicsContext() {
		return myGc;
	}
	
	/*
	 * Initializes the level state.
	 */
	private void initLevelState() {
		myInput = new ArrayList<String>();
		spriteSpeed = INIT_SPRITE_SPEED;
		setScoreLabel(new Label("Score: " + Integer.toString((int) sushi.getNumFish())));
		getScoreLabel().setFont(Font.font("Arial", FontWeight.BOLD, 20));
		myRoot.getChildren().add(getScoreLabel());
		getScoreLabel().setAlignment(Pos.TOP_LEFT);
		start = false;
		setGameOver(false);
		win = false;
		setStopLevel(false);
	}
	
	/*
	 * Schedules recurring timers once player indicates they're ready for the level to begin.
	 */
	private void checkForReady() {
		if (myInput.contains("ENTER")) {
			start = true;
			scheduleUpdateTimer();
			scheduleSpriteTimer();
			if (this.toString().equals(CUSTOMER_LEVEL_NAME)) {
				myGame.scheduleCustomerLevelTimer(this);
			}
		}
	}
	
	/*
	 * Returns the level's root stack pane.
	 */
	public StackPane getRoot() {
		return myRoot;
	}
	
	/*
	 * Creates message to be displayed while waiting for player to be ready.
	 */
	public Label createReadyMessage() {
		Label readyLabel = new Label(getInstructions() + "\n\n" + "Press ENTER to start!");
		readyLabel.setWrapText(true);
		readyLabel.setMinWidth(CANVAS_WIDTH);
		readyLabel.setMinHeight(CANVAS_HEIGHT);
		readyLabel.setAlignment(Pos.CENTER);
		readyLabel.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 20));
		readyLabel.setTextFill(Color.GRAY);
		return readyLabel;
	}
	
	/* 
	 * Returns the scene this level is built on.
	 */
	public Scene getScene() {
		return myScene;
	}
	
	/*
	 * Returns the instructions for the particular level being played.
	 */
	protected abstract String getInstructions();
	
	/*
	 * Schedules the timer for updating sprite speeds.
	 */
	private void scheduleUpdateTimer() {
		myUpdateSpeedTimer = new Timer();
		myUpdateSpeedTimer.schedule(new TimerTask() {
			public void run() {
				Platform.runLater(new Runnable() {
					public void run() {
						sushi.setSpeed(sushi.getSpeed() + 0.5);
						spriteSpeed = spriteSpeed + 0.5;
					}
				});
			}
		}, UPDATE_DURATION, UPDATE_DURATION);	
	}
	
	/*
	 * Schedules the timer for spawning new sprites.
	 */
	private void scheduleSpriteTimer() {
		mySpriteSpawnTimer = new Timer();
		mySpriteSpawnTimer.schedule(new TimerTask() {
			public void run() {
				Platform.runLater(new Runnable() {
					public void run() {
						replaceSprites();
					}
				});
			}
		}, SPRITE_SPAWN_INTERVAL, SPRITE_SPAWN_INTERVAL);	
	}
	
	/*
	 * Schedules the timer to cancel effects of a particular cheat once it expires.
	 */
	private void scheduleCheatTimer(String cheat, double origSpeed, Level curLevel) {
		Timer cheatTimer = new Timer();
		cheatTimer.schedule(new TimerTask() {
			public void run() {
				Platform.runLater(new Runnable() {
					public void run() {
						if (cheat.equals("W")) {
							curLevel.setSpriteSpeed(origSpeed);
						} else if (cheat.equals("E")) {
							curLevel.getSushi().setSpeed(origSpeed);
						}
					}
				});
			}
		}, CHEAT_DURATION);	
	}
	
	/* 
	 * Initializes the scene for this level.
	 * @param root is the Stack Pane that the scene's contents will be added to.
	 */
	private void initScene(StackPane root) {
		myRoot = root;
		myScene = new Scene(myRoot);
		myBackground = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream(getBackgroundImageName())));
		myCanvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
		myRoot.getChildren().addAll(myBackground, myCanvas);
		myRoot.setAlignment(Pos.TOP_LEFT);
		myGc = myCanvas.getGraphicsContext2D();
	}
	
	/*
	 * Initializes onKeyPressed and onKeyReleased methods. Keycodes are added to input when pressed, removed when released.
	 */
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
	
	/*
	 * Returns the filename for the background image of the level.
	 */
	protected abstract String getBackgroundImageName();
	
	/*
	 * Populates the empty scene with initial sprites.
	 */
	protected abstract void populateSceneWithSprites();
	
	/*
	 * Populates a given arraylist with a single type of sprites.
	 * @param: filename is the file name of the image to be used for the sprites.
	 * @param: array is the arraylist to be populated.
	 */
	public void populateSpriteArrayList(String filename, ArrayList<Sprite> array) {
		for (int i = 0; i < NUM_SPRITES_PER_TYPE; i++) {
			Sprite s = generateSprite(filename);
			array.add(s);
			s.render(myGc);
		}
	}

	/*
	 * Creates a sprite with a given image and sets its position randomly.
	 * @param: filename is the file name of the image to be used for the sprite.
	 */
	public Sprite generateSprite(String filename) {
		Sprite sprite = new Sprite();
		sprite.setImage(filename);
		double x = generateRandomX(sprite);
		double y = generateRandomY(sprite);
		sprite.setPosition(x, y);
		return sprite;
	}
	
	/*
	 * Generates a random x-coordinate position for a given sprite.
	 */
	protected abstract double generateRandomX(Sprite sprite);
	
	/*
	 * Generates a random y-coordinate position for a given sprite.
	 */
	protected abstract double generateRandomY(Sprite sprite);
	
	/*
	 * Replace sprites so that the scene always has NUM_SPRITES_PER_TYPE of sprites for each type of sprite.
	 */
	protected abstract void replaceSprites();
	
	/*
	 * Replace the sprites that've moved out of the bounds of the canvas.
	 * @param: sprites is the arraylist of sprites to update.
	 * @param: filename is the file name of the image to be used for the sprite.
	 */
	public void replaceOutOfBoundsSprites(ArrayList<Sprite> sprites, String filename) {
		for (int i = 0; i < sprites.size(); i++) {
			Sprite s = sprites.get(i);
			if (outOfBounds(s)) {
				sprites.remove(i);
				addMoreSprites(sprites, filename);
			}
		}		
	}
	
	/*
	 * Adds one sprite to an arraylist of sprites if the arraylist has < NUM_SPRITES_PER_TYPE of that type of sprite.
	 * @param: sprites is the arraylist to be updated.
	 * @param: filename is the file name of the image to be used for the sprite.
	 */
	public void addSpritesToGetNumSpritesPerType(ArrayList<Sprite> sprites, String filename) {
		int diff = NUM_SPRITES_PER_TYPE - sprites.size();
		if (diff > 0) {
			addMoreSprites(sprites, filename);
		}
	}
	
	/*
	 * Determines if a sprite is out of the bounds of the canvas.
	 */
	protected abstract boolean outOfBounds(Sprite s);
	
	/*
	 * Returns the background image for the level.
	 */
	protected ImageView getBackground() {
		return myBackground;
	}
	
	/*
	 * Creates a new sprite to be added to the canvas.
	 */
	public void addMoreSprites(ArrayList<Sprite> sprites, String filename) {
		Sprite s = generateSprite(filename);
		switch (this.toString()) {
		case "Table Level":
			s.setPosX(CANVAS_WIDTH);
			break;
		case "Customer Level":
			s.setPosY(0);
		}
		sprites.add(s);
		s.render(myGc);
	}
	
	/*
	 * Checks all sprite arraylists for collisions with the sushi sprite.
	 */
	protected abstract void checkListCollisions();
	
	/*
	 * Checks a given arraylist for collisions with the sushi sprite.
	 */
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
	
	/*
	 * Checks player key input and handles it.
	 */
	private void checkInput() {
		sushi.handleInput(myInput, CANVAS_WIDTH, CANVAS_HEIGHT, this.toString());
		checkInputForCheats(myInput);
	}
	
	/*
	 * Returns an arraylist of player key input.
	 */
	protected ArrayList<String> getInput() {
		return myInput;
	}
	
	/*
	 * Updates the contents of the canvas.
	 */
	protected abstract void updateCanvas();
	
	/*
	 * Updates the sushi's number of fish and the player's score.
	 */
	protected abstract void updateSushiAndScore();
	
	/*
	 * Game over.
	 */
	public void gameOver() {
		createGameOverScene();
	}
	
	/*
	 * Creates the game over scene.
	 */
	private void createGameOverScene() {
		StackPane root = new StackPane();
		initScene(root);
		Label gameOverLabel = createGameOverLabel();
		Button restart = createRestartButton();
		root.getChildren().addAll(gameOverLabel, restart);
		myScene.setFill(Color.GRAY);
		myStage.setScene(myScene);
		myStage.show();
	}
	
	/*
	 * Creates the restart button.
	 */
	private Button createRestartButton() {
		Button restart = new Button();
		restart.setText("Restart");
		restart.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				cancelTimers();
				myGame.init(myStage, new Game());
			}
		});
		restart.setAlignment(Pos.CENTER);
		return restart;
	}
	
	/*
	 * Returns the Game that created this level.
	 */
	protected Game getMyGame() {
		return myGame;
	}
	
	/*
	 * Creates the game over message.
	 */
	private Label createGameOverLabel() {
		Label gameOverLabel = new Label();
		gameOverLabel.setMinWidth(CANVAS_WIDTH);
		gameOverLabel.setMinHeight(CANVAS_HEIGHT);
		gameOverLabel.setAlignment(Pos.CENTER);
		gameOverLabel.setTextAlignment(TextAlignment.CENTER);
		gameOverLabel.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 35));
		gameOverLabel.setTextFill(Color.GRAY);
		String score = Integer.toString((int) sushi.getNumFish());
		if (win == true) {
			gameOverLabel.setText("You survived!\n" + "Score: " + score);
		} else {
			gameOverLabel.setText("So close yet so far...\n" + "Score: " + score);
		}
		return gameOverLabel;
	}

	/*
	 * Returns whether or not the game is over.
	 */
	public boolean isGameOver() {
		return gameOver;
	}

	/*
	 * Sets whether or not the game is over.
	 */
	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}
	
	/*
	 * Returns whether or not the player has won.
	 */
	public boolean hasWon() {
		return win;
	}
	
	/*
	 * Sets whether or not the player has won.
	 */
	public void setWin(boolean win) {
		this.win = win;
	}

	/*
	 * Returns whether or not to stop the level.
	 */
	public boolean isStopLevel() {
		return stopLevel;
	}

	/*
	 * Sets whether or not to stop the level.
	 */
	public void setStopLevel(boolean stopLevel) {
		this.stopLevel = stopLevel;
	}

	/*
	 * Returns the label that displays the player's score.
	 */
	public Label getScoreLabel() {
		return scoreLabel;
	}

	/*
	 * Sets the label that displays the player's score.
	 */
	public void setScoreLabel(Label scoreLabel) {
		this.scoreLabel = scoreLabel;
	}
	
	/*
	 * Sets the level's Sushi sprite.
	 */
	public void setSushi(Sushi s) {
		this.sushi = s;
	}
	
	/*
	 * Gets the level's Sushi sprite.
	 */
	public Sushi getSushi() {
		return sushi;
	}
	
	/*
	 * Checks input for player use of cheat codes.
	 */
	protected void checkInputForCheats(ArrayList<String> input) {
				if (input.contains("Q")) {
					clearObstacles();
					cancelTimers();
					scheduleUpdateTimer();
					scheduleSpriteTimer();
					input.remove("Q");
				}
				if (input.contains("W")) {
					double curSpriteSpeed = this.getSpriteSpeed();
					scheduleCheatTimer("W", curSpriteSpeed, this);
					this.setSpriteSpeed(curSpriteSpeed- 2.0);
					input.remove("W");
				}
				if (input.contains("E")) {
					double curSushiSpeed = this.getSushi().getSpeed();
					scheduleCheatTimer("E", curSushiSpeed, this);
					this.getSushi().setSpeed(this.getSushi().getSpeed() + 3.0);
					input.remove("E");
				}
				if (input.contains("SPACE") && this.toString().equals(TABLE_LEVEL_NAME)) {
					input.remove("SPACE");
					setStopLevel(true);
					myGame.switchToCustomerLevel();
				}
	}
	
	/*
	 * Clears the obstacles of the level.
	 */
	protected abstract void clearObstacles();
	
	/*
	 * Returns the current speed of the level's non-Sushi sprites.
	 */
	public double getSpriteSpeed() {
		return spriteSpeed;
	}
	
	/*
	 * Sets the speed of the level's non-Sushi sprites.
	 */
	public void setSpriteSpeed(double s) {
		if (s > 0) {
			this.spriteSpeed = s;
		} else {
			this.spriteSpeed = 0.2;
		}
	}

}
