package game;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class TableLevel extends Level {
	private ArrayList<Sprite> knifeList = new ArrayList<Sprite>();
	private ArrayList<Sprite> shrimpList = new ArrayList<Sprite>();
	private static final String TABLE_BACKGROUND_IMAGE = "tableBackground.png";
	private static final String KNIFE_IMAGE = "knife.png";
	private static final String SHRIMP_IMAGE = "shrimp.png";
	private ImageView myBackground;
	private static final String LEVEL_NAME = "Table Level";
	private static final long ONE_SECOND = 1000;
	private static final double END_OF_BACKGROUND = -2048.0;
	
	public TableLevel (double numStartingFish) {
		this.setSushi(new Sushi(0, this.getCanvasHeight()/2, 0));
		this.getSushi().setNumFish(numStartingFish);
	}
	
	/*
	 * Returns the name of this level.
	 */
	public String toString() {
		return LEVEL_NAME;
	}

	/*
	 * Populates the empty scene with initial knife and shrimp sprites.
	 */
	@Override
	protected void populateSceneWithSprites() {
		this.getSushi().render(this.getGraphicsContext());
		populateSpriteArrayList(KNIFE_IMAGE, knifeList);
		populateSpriteArrayList(SHRIMP_IMAGE, shrimpList);
	}

	/*
	 * Moves an arraylist of sprites to the left across the canvas.
	 */
	public void moveSpritesForward(ArrayList<Sprite> sprites) {
		for (int i = 0; i < sprites.size(); i++) {
			Sprite s = sprites.get(i);
			double curX = s.getPosX();
			s.setPosX(curX - this.getSpriteSpeed());
			s.render(this.getGraphicsContext());
		}
	}

	/*
	 * Checks and handles all sprite arraylists for the level for collisions with the Sushi sprite.
	 */
	@Override
	protected void checkListCollisions() {
		if(checkSpriteCollisions(knifeList)) {
			knifeList.clear();
			shrimpList.clear();
			setGameOver(true);
			super.setWin(false);
			setStopLevel(true);
		}
		if (checkSpriteCollisions(shrimpList)) {
			updateSushiAndScore();
		}
	}
	
	/*
	 * Sets whether or not the game is over.
	 */
	public void setGameOver(boolean gameOver) {
		if (gameOver) {
			super.setGameOver(true);
		}
	}
	
	/*
	 * Updates the sushi's number of fish and the player's score.
	 */
	@Override
	protected void updateSushiAndScore() {
		this.getSushi().setNumFish(this.getSushi().getNumFish() + 1);
		getScoreLabel().setText("Score: " + Integer.toString((int) this.getSushi().getNumFish()));
	}
	
	/*
	 * Updates the contents of the canvas. If the end of the table has been reached, transition to next level.
	 */
	@Override
	protected void updateCanvas() {
		this.getBackground().setTranslateX(this.getBackground().getTranslateX() - 0.5);
		if (this.getBackground().getTranslateX() == END_OF_BACKGROUND) {
			this.setStopLevel(true);
			Label transLabel = createLevelTransitionMessage();
			this.getRoot().getChildren().add(transLabel);
			scheduleReadyTimer(this.getInput(), this);
		}
		moveSpritesForward(knifeList);
		moveSpritesForward(shrimpList);
	}
	
	/*
	 * Generates a random x-coordinate position for a given sprite in the right 2/3s of the canvas.
	 */
	@Override
	protected double generateRandomX(Sprite sprite) {
		return (this.getCanvasWidth()/3) + (this.getCanvasWidth() - sprite.getWidth()) * Math.random();
	}
	
	/*
	 * Generates a random y-coordinate position for a given sprite anywhere on the canvas.
	 */
	@Override
	protected double generateRandomY(Sprite sprite) {
		return (this.getCanvasHeight() - sprite.getHeight()) * Math.random();
	}

	/*
	 * Returns true if sprite has moved out of bounds of the canvas.
	 */
	@Override
	protected boolean outOfBounds(Sprite s) {
		return (s.getPosX() + s.getWidth()) < 0;
	}
	
	/* 
	 * Returns the instructions for the Table Level.
	 */
	@Override
	protected String getInstructions() {
		return "Use the arrow keys to move.\nCollect shrimp and dodge the knives!\nIf you get hit by a knife then it's game over.";
	}
	
	/*
	 * Clears the obstacles of this level (the knives).
	 */
	@Override
	protected void clearObstacles() {
		knifeList.clear();
	}
	
	/*
	 * Replaces sprites so that NUM_SPRITES_PER_TYPE of sprites are always present for each type.
	 */
	@Override
	protected void replaceSprites() {
		// TODO Auto-generated method stub
		replaceOutOfBoundsSprites(knifeList, KNIFE_IMAGE);
		replaceOutOfBoundsSprites(shrimpList, SHRIMP_IMAGE);
		addSpritesToGetNumSpritesPerType(knifeList, KNIFE_IMAGE);
		addSpritesToGetNumSpritesPerType(shrimpList, SHRIMP_IMAGE);
	}

	/*
	 * Returns the file name of the background image for the Table Level.
	 */
	public String getBackgroundImageName() {
		return TABLE_BACKGROUND_IMAGE;
	}
	
	/*
	 * Creates the message that's shown at the end of the Table Level.
	 */
	public Label createLevelTransitionMessage() {
		Label transLabel = new Label("You made it across the table!\nNow you just have to make it past the hungry customer.\n\nPress ENTER to continue. Good luck!");
		transLabel.setWrapText(true);
		transLabel.setMinWidth(getCanvasWidth());
		transLabel.setMinHeight(getCanvasHeight());
		transLabel.setAlignment(Pos.CENTER);
		transLabel.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 20));
		transLabel.setTextFill(Color.GRAY);
		return transLabel;
	}
	
	/*
	 * Returns true if player has indicated he/she is ready for the next level.
	 */
	private boolean readyForNextLevel(ArrayList<String> input) {
		return input.contains("ENTER");
	}
	
	/*
	 * Schedules a timer to check for player input to indicate they're ready for the next level.
	 */
	private void scheduleReadyTimer(ArrayList<String> input, Level curLevel) {
			Timer readyTimer = new Timer();
			readyTimer.schedule(new TimerTask() {
				public void run() {
					Platform.runLater(new Runnable() {
						public void run() {
							if (readyForNextLevel(input)) {
								input.remove("ENTER");
								curLevel.getMyGame().switchToCustomerLevel();
							} else {
								scheduleReadyTimer(input, curLevel);
							}
						}
					});
				}
			}, ONE_SECOND, ONE_SECOND);	
		
	}
	
}
