package game;

import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.stage.Stage;

public class CustomerLevel extends Level {
	public ArrayList<Sprite> soySauceList = new ArrayList();
	public Sprite chopsticks = new Sprite();
	private double chopstickDirection;
	private final double UPWARDS = -1;
	private final double DOWNWARDS = 1;
	private static final String CUSTOMER_BACKGROUND_IMAGE = "customerBackground.jpg";
	private static final String SOYSAUCE_IMAGE = "soysauce.png";
	private static final String CHOPSTICKS_IMAGE = "chopsticks.png";
	
	// TODO: create constructor with sushi or numFish or something
	public CustomerLevel(double numStartingFish) {
		sushi = new Sushi(this.getCanvasWidth()/2, this.getCanvasHeight(), 0);//NEED TO RETAIN NUM FISH SOMEHOW
		sushi.posY = this.getCanvasHeight() - sushi.height;
		sushi.numFish = numStartingFish;
	}
	
	public String toString() {
		return "Customer Level";
	}

	@Override
	protected void populateSceneWithSprites() {
		// TODO Auto-generated method stub
		addBackground(CUSTOMER_BACKGROUND_IMAGE);
		sushi.render(this.getGraphicsContext());
		initChopsticks();
		populateSpriteArrayList(SOYSAUCE_IMAGE, soySauceList);
	}
	
	private void initChopsticks() {
		chopsticks.setImage(CHOPSTICKS_IMAGE);
		chopsticks.posX = this.getCanvasWidth()/2;
		chopsticks.posY = 0 - chopsticks.height;
		chopsticks.render(this.getGraphicsContext());
	}
	
	public void moveSpritesForward(ArrayList<Sprite> sprites) {
		// TODO Auto-generated method stub
		for (int i = 0; i < sprites.size(); i++) {
			Sprite s = sprites.get(i);
			double curY = s.posY;
			s.posY = curY + spriteSpeed;
			s.render(this.getGraphicsContext());
		}
	}
	
	private void moveChopsticks() {
		if (chopsticks.posY <= 0 - chopsticks.height) {
			chopsticks.posX = sushi.posX;
			chopstickDirection = spriteSpeed * DOWNWARDS;
		}
		else if (chopsticks.posY >= this.getCanvasHeight() - chopsticks.height - sushi.height + 1) {
			chopstickDirection = spriteSpeed * UPWARDS;
		}
		chopsticks.posY = chopsticks.posY + chopstickDirection;
		chopsticks.render(this.getGraphicsContext());
	}

	@Override
	protected void checkListCollisions() {
		// TODO Auto-generated method stub
		if (checkSpriteCollisions(soySauceList)) {
			sushi.speed = sushi.speed - 0.5;
			System.out.println("ran into soy sauce");
		}
		if (sushi.intersects(chopsticks.getBoundary())) {
			updateSushiAndScore();
		}
		
	}

	@Override
	protected void updateSushiAndScore() {
		// TODO Auto-generated method stub
		sushi.numFish = sushi.numFish - 2;
		scoreLabel.setText("Score: " + Integer.toString((int) sushi.numFish));
		System.out.println("numFish = " + sushi.numFish);	
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
		return (this.getCanvasWidth() - sprite.width) * Math.random();
	}

	@Override
	protected double generateRandomY(Sprite sprite) {
		// TODO Auto-generated method stub
		return (this.getCanvasHeight() - sprite.height - this.getCanvasHeight()/3) * Math.random();
	}

	@Override
	protected boolean outOfBounds(Sprite s) {
		// TODO Auto-generated method stub
		return (s.height + s.posY) > this.getCanvasHeight();
	}

	@Override
	protected String getInstructions() {
		// TODO Auto-generated method stub
		return "Use the left and right arrow keys to move.\nSoy sauce will slow you down.\nThe customer's chopsticks will steal your shrimp!";
	}
}
