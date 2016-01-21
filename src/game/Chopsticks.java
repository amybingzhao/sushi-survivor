package game;

import javafx.scene.image.Image;

public class Chopsticks extends Sprite {
	private static final double INIT_SPEED = 2.5;
	public double speed;
	private static final String CHOPSTICKS_IMAGE= "chopsticks.png";
	
	public Chopsticks(double x, double y) {
		image = new Image(getClass().getClassLoader().getResourceAsStream(CHOPSTICKS_IMAGE));
		speed = INIT_SPEED;
		posX = x;
		posY = y;
		height = image.getHeight();
		width = image.getWidth();
	}
}

// unused so far...