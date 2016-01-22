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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
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
	private Group myRoot;
	private Label scoreLabel;
	private Game myGame;
	
	public void init(Stage stage, Game game) {
		myStage = stage;
		myRoot = new Group();
		myGame = game;
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
					if (isStopLevel() == true) {
						cancelTimers();
						stop();
						if (isGameOver() == true) {
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
	
	private void cancelTimers() {
		myUpdateSpeedTimer.cancel();
		myUpdateSpeedTimer.purge();
		mySpriteSpawnTimer.cancel();
		mySpriteSpawnTimer.purge();
	}
	protected int getCanvasHeight() {
		return CANVAS_HEIGHT;
	}
	
	protected int getCanvasWidth() {
		return CANVAS_WIDTH;
	}
	
	protected GraphicsContext getGraphicsContext() {
		return myGc;
	}
	
	private Scene getMyScene() {
		return myScene;
	}
	
	private void initLevelState() {
		myInput = new ArrayList<String>();
		spriteSpeed = INIT_SPRITE_SPEED;
		setScoreLabel(new Label("Score: " + Integer.toString((int) sushi.getNumFish())));
		getScoreLabel().setFont(Font.font("Arial", FontWeight.BOLD, 20));
		getScoreLabel().setAlignment(Pos.TOP_LEFT);
		myRoot.getChildren().add(getScoreLabel());
		start = false;
		setGameOver(false);
		win = false;
		setStopLevel(false);
	}
	
	private void checkForReady() {
		if (myInput.contains("ENTER")) {
			start = true;
			scheduleUpdateTimer();
			scheduleSpriteTimer();
		}
	}
	
	public void addBackground(String filename) {
//		BackgroundImage background = new BackgroundImage(new Image(filename), null, null, null, null);
		Image background = new Image(getClass().getClassLoader().getResourceAsStream(filename));
		myGc.drawImage(background, 0, 0);
	}
	
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
	
	public Scene getScene() {
		return myScene;
	}
	
	protected abstract String getInstructions();
	
	private void scheduleUpdateTimer() {
		myUpdateSpeedTimer = new Timer();
		myUpdateSpeedTimer.schedule(new TimerTask() {
			public void run() {
				Platform.runLater(new Runnable() {
					public void run() {
						sushi.setSpeed(sushi.getSpeed() + 0.3);
						spriteSpeed = spriteSpeed + 0.3;
						System.out.println("update timer timed out");
					}
				});
			}
		}, UPDATE_DURATION, UPDATE_DURATION);	
	}
	
	private void scheduleSpriteTimer() {
		mySpriteSpawnTimer = new Timer();
		mySpriteSpawnTimer.schedule(new TimerTask() {
			public void run() {
				Platform.runLater(new Runnable() {
					public void run() {
						replaceSprites();
						System.out.println("sprite timer timed out");
					}
				});
			}
		}, SPRITE_SPAWN_INTERVAL, SPRITE_SPAWN_INTERVAL);	
	}
	
	private void scheduleCheatTimer(String cheat, double origSpeed, Level curLevel) {
		Timer cheatTimer = new Timer();
		cheatTimer.schedule(new TimerTask() {
			public void run() {
				Platform.runLater(new Runnable() {
					public void run() {
						if (cheat.equals("W")) {
							curLevel.setSpriteSpeed(origSpeed);
							System.out.println("cheat timer timed out on W, cur speed: " + Double.toString(curLevel.getSpriteSpeed()) + "orig speed: " + Double.toString(origSpeed));
						} else if (cheat.equals("E")) {
							curLevel.getSushi().setSpeed(origSpeed);
							System.out.println("cheat timer timed out on E");
						}
						System.out.println("cheat timer timed out");
					}
				});
			}
		}, CHEAT_DURATION);	
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
		Sprite sprite = new Sprite();
		sprite.setImage(filename);
		double x = generateRandomX(sprite);
		double y = generateRandomY(sprite);
		sprite.setPosition(x, y);
		return sprite;
	}
	
	protected abstract double generateRandomX(Sprite sprite);
	protected abstract double generateRandomY(Sprite sprite);
	
	protected abstract void replaceSprites();
	public void replaceOutOfBoundsSprites(ArrayList<Sprite> sprites, String filename) {
		for (int i = 0; i < sprites.size(); i++) {
			Sprite s = sprites.get(i);
			if (outOfBounds(s)) {
				sprites.remove(i);
				addMoreSprites(sprites, filename);
			}
		}		
	}
	
	public void addSpritesToGetNumSpritesPerType(ArrayList<Sprite> sprites, String filename) {
		int diff = NUM_SPRITES_PER_TYPE - sprites.size();
		if (diff > 0) {
			addMoreSprites(sprites, filename);
		}
	}
	protected abstract boolean outOfBounds(Sprite s);
	
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
		checkInputForCheats(myInput);
	}
	
	protected abstract void updateCanvas();
		
	protected abstract void updateSushiAndScore();
	
	public void gameOver() {
		createGameOverScene();
	}
	
	private void createGameOverScene() {
		Group root = new Group();
		initScene(root);
		Label gameOverLabel = createGameOverLabel();
		Button restart = createRestartButton();
		root.getChildren().addAll(gameOverLabel, restart);
		myScene.setFill(Color.GRAY);
		myStage.setScene(myScene);
		myStage.show();
	}
	
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

	public boolean isGameOver() {
		return gameOver;
	}

	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}
	
	public boolean hasWon() {
		return win;
	}
	
	public void setWin(boolean win) {
		this.win = win;
	}

	public boolean isStopLevel() {
		return stopLevel;
	}

	public void setStopLevel(boolean stopLevel) {
		this.stopLevel = stopLevel;
	}

	public Label getScoreLabel() {
		return scoreLabel;
	}

	public void setScoreLabel(Label scoreLabel) {
		this.scoreLabel = scoreLabel;
	}
	
	public void setSushi(Sushi s) {
		this.sushi = s;
	}
	
	public Sushi getSushi() {
		return sushi;
	}
	
	protected void checkInputForCheats(ArrayList<String> input) {
				if (input.contains("Q")) {
					clearLists();
					cancelTimers();
					scheduleUpdateTimer();
					scheduleSpriteTimer();
					input.remove("Q");
				}
				if (input.contains("W")) {
					double curSpriteSpeed = this.getSpriteSpeed();
					System.out.println("orig speed: " + Double.toString(curSpriteSpeed));
					scheduleCheatTimer("W", curSpriteSpeed, this);
					this.setSpriteSpeed(curSpriteSpeed- 2.0);
					System.out.println("new speed: " + Double.toString(this.getSpriteSpeed()));
					input.remove("W");
				}
				if (input.contains("E")) {
					double curSushiSpeed = this.getSushi().getSpeed();
					scheduleCheatTimer("E", curSushiSpeed, this);
					this.getSushi().setSpeed(this.getSushi().getSpeed() + 3.0);
					input.remove("E");
				}
	}
	
	protected abstract void clearLists();
	
	public double getSpriteSpeed() {
		return spriteSpeed;
	}
	
	public void setSpriteSpeed(double s) {
		if (s > 0) {
			this.spriteSpeed = s;
		} else {
			this.spriteSpeed = 0.2;
		}
	}

}
