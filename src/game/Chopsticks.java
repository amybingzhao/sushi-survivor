package game;

import javafx.scene.image.Image;

public class Chopsticks extends Sprite {
	private static final double INIT_SPEED = 2.5;
	public double speed;
	private static final String CHOPSTICKS_IMAGE= "chopsticks.png";
	
	public Chopsticks(double x, double y) {
		setImage(new Image(getClass().getClassLoader().getResourceAsStream(CHOPSTICKS_IMAGE)));
		speed = INIT_SPEED;
		setPosX(x);
		setPosY(y);
		setHeight(getImage().getHeight());
		setWidth(getImage().getWidth());
	}
}

// unused so far...