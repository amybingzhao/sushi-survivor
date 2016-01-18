package game;

import javafx.scene.image.Image;

public class Chopsticks extends Sprite {
	private static final double INIT_SPEED = 2.5;
	public double speed;
	
	public Chopsticks(double x, double y) {
		image = new Image("chopsticks.png");
		speed = INIT_SPEED;
		posX = x;
		posY = y;
		height = image.getHeight();
		width = image.getWidth();
	}
}
