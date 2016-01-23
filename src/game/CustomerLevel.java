package game;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;

public class CustomerLevel extends Level {
	private ArrayList<Sprite> soySauceList = new ArrayList<Sprite>();
	private Sprite chopsticks = new Sprite();
	private double chopstickDirection;
	private static final String CUSTOMER_BACKGROUND_IMAGE = "customerBackground.jpg";
	private static final String SOYSAUCE_IMAGE = "soysauce.png";
	private static final String CHOPSTICKS_IMAGE = "chopsticks.png";
	private final double UPWARDS = -1;
	private final double DOWNWARDS = 1;
	private static final double BOTTOM_BORDER = 5;
	private static final String LEVEL_NAME = "Customer Level";
	private boolean recentlyHit = false;
	private Timer myRecencyTimer;
	private static final long ONE_SECOND = 1000;
	
	public CustomerLevel(double numStartingFish) {
		super.setSushi(new Sushi(this.getCanvasWidth()/2, this.getCanvasHeight(), 0));
		Sushi s = this.getSushi();
		s.setPosY(this.getCanvasHeight() - s.getHeight() - BOTTOM_BORDER);
		s.setNumFish(numStartingFish);
	}

	/*
	 * Initializes chopstick sprite.
	 */
	private void initChopsticks() {
		chopsticks = new Sprite();
		chopsticks.setImage(CHOPSTICKS_IMAGE);
		chopsticks.setPosX(this.getCanvasWidth()/2);
		chopsticks.setPosY(0 - chopsticks.getHeight());
		chopsticks.render(this.getGraphicsContext());
	}
	
	/* 
	 * Moves chopsticks sprite up and down.
	 */
	private void moveChopsticks() {
		if (chopsticks.getPosY() <= 0 - chopsticks.getHeight()) {
			chopsticks.setPosX(this.getSushi().getPosX());
			chopstickDirection = this.getSpriteSpeed() * DOWNWARDS;
		}
		else if (chopsticks.getPosY() >= this.getCanvasHeight() - chopsticks.getHeight() - this.getSushi().getHeight()) {
			chopstickDirection = this.getSpriteSpeed() * UPWARDS;
		}
		chopsticks.setPosY(chopsticks.getPosY() + chopstickDirection);
		chopsticks.render(this.getGraphicsContext());
	}
	
	/*
	 * Populates the empty scene with initial soysauce and chopstick sprites.
	 */
	@Override
	protected void populateSceneWithSprites() {
		this.getSushi().render(this.getGraphicsContext());
		initChopsticks();
		populateSpriteArrayList(SOYSAUCE_IMAGE, soySauceList);
	}
	
	/*
	 * Moves an arraylist of sprites down across the canvas.
	 */
	public void moveSpritesForward(ArrayList<Sprite> sprites) {
		// TODO Auto-generated method stub
		for (int i = 0; i < sprites.size(); i++) {
			Sprite s = sprites.get(i);
			double curY = s.getPosY();
			s.setPosY(curY + this.getSpriteSpeed());
			s.render(this.getGraphicsContext());
		}
	}
	
	/*
	 * Updates the contents of the canvas.
	 */
	@Override
	protected void updateCanvas() {
		//addBackground(CUSTOMER_BACKGROUND_IMAGE);
		moveSpritesForward(soySauceList);
		moveChopsticks();
	}

	/*
	 * Checks and handles all sprite arraylists for the level for collisions with the Sushi sprite.
	 */
	@Override
	protected void checkListCollisions() {
		if (checkSpriteCollisions(soySauceList)) {
			this.getSushi().setSpeed(this.getSushi().getSpeed() - 0.5);
		}
		if (this.getSushi().intersects(chopsticks.getBoundary())) {
			updateSushiAndScore();
		}
		
	}

	/*
	 * Updates the sushi's number of fish and the player's score. If number of fish becomes <= 0, then player loses.
	 */
	@Override
	protected void updateSushiAndScore() {
		if (!isGameOver() && !recentlyHit) {
			this.getSushi().setNumFish(this.getSushi().getNumFish() - 2);
			if (this.getSushi().getNumFish() <= 0) {
				setGameOver(true);
				getScoreLabel().setText("Score: 0");
				this.getSushi().setNumFish(0);
				if (myRecencyTimer != null) {myRecencyTimer.cancel();};
				getMyGame().getLevelTimer().cancel();
				getMyGame().endGame(this);
			} else {
				getScoreLabel().setText("Score: " + Integer.toString((int) this.getSushi().getNumFish()));
			}
			recentlyHit = true;
			scheduleRecentlyHitTimer();
		}
	}

	/*
	 * Schedules a timer so that for 2 seconds the sushi won't count as being "hit".
	 */
	private void scheduleRecentlyHitTimer() {
		myRecencyTimer = new Timer();
		myRecencyTimer.schedule(new TimerTask() {
			public void run() {
				Platform.runLater(new Runnable() {
					public void run() {
						recentlyHit = false;
					}
				});
			}
		}, ONE_SECOND * 2);	
	}
	
	/*
	 * Clears the obstacles of this level (soysauce).
	 */
	@Override
	protected void clearObstacles() {
		soySauceList.clear();
	}

	/*
	 * Replaces sprites so that NUM_SPRITES_PER_TYPE of sprites are always present for each type.
	 */
	@Override
	protected void replaceSprites() {
		// TODO Auto-generated method stub
		replaceOutOfBoundsSprites(soySauceList, SOYSAUCE_IMAGE);
		addSpritesToGetNumSpritesPerType(soySauceList, SOYSAUCE_IMAGE);
	}
	
	/*
	 * Generates a random x-coordinate position for a given sprite.
	 */
	@Override
	protected double generateRandomX(Sprite sprite) {
		// TODO Auto-generated method stub
		return (this.getCanvasWidth() - sprite.getWidth()) * Math.random();
	}

	/*
	 * Generates a random y-coordinate position for a given sprite in the top 2/3s of the canvas.
	 */
	@Override
	protected double generateRandomY(Sprite sprite) {
		return (this.getCanvasHeight() - sprite.getHeight() - this.getCanvasHeight()/3) * Math.random();
	}

	/*
	 * Returns true if sprite has moved out of bounds of the canvas.
	 */
	@Override
	protected boolean outOfBounds(Sprite s) {
		return (s.getHeight() + s.getPosY()) > this.getCanvasHeight();
	}

	/* 
	 * Returns the instructions for the Table Level.
	 */
	@Override
	protected String getInstructions() {
		return "Use the left and right arrow keys to move.\nSoy sauce will slow you down.\nThe customer's chopsticks will steal your shrimp!";
	}

	/*
	 * Returns the file name of the background image for the Table Level.
	 */
	public String getBackgroundImageName() {
		return CUSTOMER_BACKGROUND_IMAGE;
	}
	
	/*
	 * Returns the name of this level.
	 */
	public String toString() {
		return LEVEL_NAME;
	}
}
