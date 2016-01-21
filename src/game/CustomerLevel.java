package game;

import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.stage.Stage;

public class CustomerLevel extends Level {
	private ArrayList<Sprite> soySauceList = new ArrayList();
	private Sprite chopsticks = new Sprite();
	private double chopstickDirection;
	private final double UPWARDS = -1;
	private final double DOWNWARDS = 1;
	private static final String CUSTOMER_BACKGROUND_IMAGE = "customerBackground.jpg";
	private static final String SOYSAUCE_IMAGE = "soysauce.png";
	private static final String CHOPSTICKS_IMAGE = "chopsticks.png";
	
	// TODO: create constructor with sushi or numFish or something
	public CustomerLevel(double numStartingFish) {
		super.setSushi(new Sushi(this.getCanvasWidth()/2, this.getCanvasHeight(), 0));//NEED TO RETAIN NUM FISH SOMEHOW
		Sushi s = this.getSushi();
		s.setPosY(this.getCanvasHeight() - s.getHeight());
		s.setNumFish(numStartingFish);
	}
	
	public String toString() {
		return "Customer Level";
	}

	@Override
	protected void populateSceneWithSprites() {
		// TODO Auto-generated method stub
		addBackground(CUSTOMER_BACKGROUND_IMAGE);
		this.getSushi().render(this.getGraphicsContext());
		initChopsticks();
		populateSpriteArrayList(SOYSAUCE_IMAGE, soySauceList);
	}
	
	private void initChopsticks() {
		chopsticks.setImage(CHOPSTICKS_IMAGE);
		chopsticks.setPosX(this.getCanvasWidth()/2);
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
		else if (chopsticks.getPosY() >= this.getCanvasHeight() - chopsticks.getHeight() - this.getSushi().getHeight() + 1) {
			chopstickDirection = this.getSpriteSpeed() * UPWARDS;
		}
		chopsticks.setPosY(chopsticks.getPosY() + chopstickDirection);
		chopsticks.render(this.getGraphicsContext());
	}

	@Override
	protected void checkListCollisions() {
		// TODO Auto-generated method stub
		if (checkSpriteCollisions(soySauceList)) {
			this.getSushi().setSpeed(this.getSushi().getSpeed() - 0.5);
			System.out.println("ran into soy sauce");
		}
		if (this.getSushi().intersects(chopsticks.getBoundary())) {
			updateSushiAndScore();
		}
		
	}

	@Override
	protected void updateSushiAndScore() {
		// TODO Auto-generated method stub
		this.getSushi().setNumFish(this.getSushi().getNumFish() - 2);
		if (this.getSushi().getNumFish() <= 0) {
			super.setGameOver(true);
		}
		getScoreLabel().setText("Score: " + Integer.toString((int) this.getSushi().getNumFish()));
		System.out.println("numFish = " + this.getSushi().getNumFish());	
	}

	@Override
	protected void updateCanvas() {
		// TODO Auto-generated method stub
		addBackground(CUSTOMER_BACKGROUND_IMAGE);
		moveSpritesForward(soySauceList);
		replaceOutOfBoundsSprites(soySauceList, SOYSAUCE_IMAGE);
		moveChopsticks();
	}

	@Override
	protected double generateRandomX(Sprite sprite) {
		// TODO Auto-generated method stub
		return (this.getCanvasWidth() - sprite.getWidth()) * Math.random();
	}

	@Override
	protected double generateRandomY(Sprite sprite) {
		// TODO Auto-generated method stub
		return (this.getCanvasHeight() - sprite.getHeight() - this.getCanvasHeight()/3) * Math.random();
	}

	@Override
	protected boolean outOfBounds(Sprite s) {
		// TODO Auto-generated method stub
		return (s.getHeight() + s.getPosY()) > this.getCanvasHeight();
	}

	@Override
	protected String getInstructions() {
		// TODO Auto-generated method stub
		return "Use the left and right arrow keys to move.\nSoy sauce will slow you down.\nThe customer's chopsticks will steal your shrimp!";
	}
}
