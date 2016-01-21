package game;

import javafx.scene.image.Image;

public class Chopsticks extends Sprite {
	private static final double INIT_SPEED = 2.5;
	private double speed;
	private static final String CHOPSTICKS_IMAGE= "chopsticks.png";
	private final double UPWARDS = -1;
	private final double DOWNWARDS = 1;
	
	public Chopsticks(double x, double y) {
		setImage(new Image(getClass().getClassLoader().getResourceAsStream(CHOPSTICKS_IMAGE)));
		speed = INIT_SPEED;
		setPosX(x);
		setPosY(y);
		setHeight(getImage().getHeight());
		setWidth(getImage().getWidth());
	}
	
	public void moveChopsticks(double sushiPosX, double sushiPosY, double sushiHeight, int canvasHeight) {
		double chopstickDirection = 1;
		if (this.getPosY() <= 0 - this.getHeight()) {
			this.setPosX(sushiPosX);
			chopstickDirection = speed * DOWNWARDS;
		}
		else if (this.getPosY() >= canvasHeight - this.getHeight() - sushiHeight + 1) {
			chopstickDirection = speed * UPWARDS;
		}
		this.setPosY(this.getPosY() + chopstickDirection);
	}
}

// unused so far...