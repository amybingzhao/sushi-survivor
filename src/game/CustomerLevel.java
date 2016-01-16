package game;

import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

public class CustomerLevel extends Level {
	public ArrayList<Sprite> soySauceList = new ArrayList();
	public Sprite chopsticks = new Sprite();
	private double chopstickDirection;
	private static final double UPWARDS = -1;
	private static final double DOWNWARDS = 1;
	
	// TODO: create constructor with sushi or numFish or something
	
	public String toString() {
		return "Customer Level";
	}

	@Override
	protected void populateSceneWithSprites() {
		// TODO Auto-generated method stub
		sushi = new Sushi(CANVAS_WIDTH/2, CANVAS_HEIGHT, 0);//NEED TO RETAIN NUM FISH SOMEHOW
		sushi.posY = CANVAS_HEIGHT - sushi.height;
		sushi.render(myGc);
		initChopsticks();
		populateSpriteArrayList("soysauce.png", soySauceList);

	}
	
	private void initChopsticks() {
		chopsticks.setImage("chopsticks.png");
		chopsticks.posX = CANVAS_WIDTH/2;
		chopsticks.posY = 0 - chopsticks.height;
		chopsticks.render(myGc);
	}
	
	public void moveSpritesForward(ArrayList<Sprite> sprites) {
		// TODO Auto-generated method stub
		for (int i = 0; i < sprites.size(); i++) {
			Sprite s = sprites.get(i);
			double curY = s.posY;
			s.posY = curY + spriteSpeed;
			s.render(myGc);
		}
	}
	
	private void moveChopsticks() {
		if (chopsticks.posY <= 0 - chopsticks.height) {
			chopsticks.posX = sushi.posX;
			chopstickDirection = spriteSpeed * DOWNWARDS;
		}
		else if (chopsticks.posY >= CANVAS_HEIGHT - chopsticks.height) {
			chopstickDirection = spriteSpeed * UPWARDS;
		}
		chopsticks.posY = chopsticks.posY + chopstickDirection;
		chopsticks.render(myGc);
	}

	@Override
	protected void checkListCollisions() {
		// TODO Auto-generated method stub
		if (checkSpriteCollisions(soySauceList)) {
			sushi.speed = sushi.speed - 0.3;
			System.out.println("ran into soy sauce");
		}
		if (sushi.intersects(chopsticks.getBoundary())) {
			updateSushi();
		}
		
	}

	@Override
	protected void updateSushi() {
		// TODO Auto-generated method stub
		sushi.numFish = sushi.numFish - 2;
		System.out.println("numFish = " + sushi.numFish);	
	}

	@Override
	protected void updateCanvas() {
		// TODO Auto-generated method stub
		moveSpritesForward(soySauceList);
		replaceOutOfBoundsSprites(soySauceList, "soysauce.png");
		moveChopsticks();
	}

	@Override
	protected double generateRandomX(Sprite sprite) {
		// TODO Auto-generated method stub
		return (CANVAS_WIDTH - sprite.width) * Math.random();
	}

	@Override
	protected double generateRandomY(Sprite sprite) {
		// TODO Auto-generated method stub
		return (CANVAS_HEIGHT - sprite.height - CANVAS_HEIGHT/3) * Math.random();
	}

	@Override
	protected boolean outOfBounds(Sprite s) {
		// TODO Auto-generated method stub
		return (s.height + s.posY) > CANVAS_HEIGHT;
	}
}
