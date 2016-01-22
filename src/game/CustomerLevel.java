package game;

import java.util.ArrayList;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.stage.Stage;

public class CustomerLevel extends Level {
	private ArrayList<Sprite> soySauceList = new ArrayList();
	private Sprite chopsticks = new Sprite();
	private double chopstickDirection;
	private static final String CUSTOMER_BACKGROUND_IMAGE = "customerBackground.jpg";
	private static final String SOYSAUCE_IMAGE = "soysauce.png";
	private static final String CHOPSTICKS_IMAGE = "chopsticks.png";
	private final double UPWARDS = -1;
	private final double DOWNWARDS = 1;
	private static final double BOTTOM_BORDER = 5;
	private static final String LEVEL_NAME = "Customer Level";
	
	// TODO: create constructor with sushi or numFish or something
	public CustomerLevel(double numStartingFish) {
		super.setSushi(new Sushi(this.getCanvasWidth()/2, this.getCanvasHeight(), 0));//NEED TO RETAIN NUM FISH SOMEHOW
		Sushi s = this.getSushi();
		s.setPosY(this.getCanvasHeight() - s.getHeight() - BOTTOM_BORDER);
		s.setNumFish(numStartingFish);
	}

	/*
	 * Returns the name of this level.
	 */
	public String toString() {
		return LEVEL_NAME;
	}

	@Override
	protected void populateSceneWithSprites() {
		this.getSushi().render(this.getGraphicsContext());
		initChopsticks();
		populateSpriteArrayList(SOYSAUCE_IMAGE, soySauceList);
	}
	
	private void initChopsticks() {
		chopsticks = new Chopsticks(this.getCanvasWidth()/2, 0);
		chopsticks.setPosY(0 - chopsticks.getHeight());
		chopsticks.render(this.getGraphicsContext());
	}
	
	public void moveSpritesForward(ArrayList<Sprite> sprites) {
		// TODO Auto-generated method stub
		for (int i = 0; i < sprites.size(); i++) {
			Sprite s = sprites.get(i);
			double curY = s.getPosY();
			s.setPosY(curY + this.getSpriteSpeed());
			s.render(this.getGraphicsContext());
		}
	}
	
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

	@Override
	protected void checkListCollisions() {
		if (checkSpriteCollisions(soySauceList)) {
			this.getSushi().setSpeed(this.getSushi().getSpeed() - 0.5);
		}
		if (this.getSushi().intersects(chopsticks.getBoundary())) {
			updateSushiAndScore();
		}
		
	}

	@Override
	protected void updateSushiAndScore() {
		if (!isGameOver()) {
			this.getSushi().setNumFish(this.getSushi().getNumFish() - 2);
			if (this.getSushi().getNumFish() <= 0) {
				setGameOver(true);
				getScoreLabel().setText("Score: 0");
				this.getSushi().setNumFish(0);
				getMyGame().getLevelTimer().cancel();
				getMyGame().endGame(this);
			} else {
				getScoreLabel().setText("Score: " + Integer.toString((int) this.getSushi().getNumFish()));
			}
		}
	}

	@Override
	protected void updateCanvas() {
		//addBackground(CUSTOMER_BACKGROUND_IMAGE);
		moveSpritesForward(soySauceList);
		Sushi s = this.getSushi();
		moveChopsticks();
	}

	@Override
	protected double generateRandomX(Sprite sprite) {
		// TODO Auto-generated method stub
		return (this.getCanvasWidth() - sprite.getWidth()) * Math.random();
	}

	@Override
	protected double generateRandomY(Sprite sprite) {
		return (this.getCanvasHeight() - sprite.getHeight() - this.getCanvasHeight()/3) * Math.random();
	}

	@Override
	protected boolean outOfBounds(Sprite s) {
		return (s.getHeight() + s.getPosY()) > this.getCanvasHeight();
	}

	@Override
	protected String getInstructions() {
		return "Use the left and right arrow keys to move.\nSoy sauce will slow you down.\nThe customer's chopsticks will steal your shrimp!";
	}

	@Override
	protected void clearObstacles() {
		soySauceList.clear();
	}

	@Override
	protected void replaceSprites() {
		// TODO Auto-generated method stub
		replaceOutOfBoundsSprites(soySauceList, SOYSAUCE_IMAGE);
		addSpritesToGetNumSpritesPerType(soySauceList, SOYSAUCE_IMAGE);
	}
	
	public String getBackgroundImageName() {
		return CUSTOMER_BACKGROUND_IMAGE;
	}
	
}
