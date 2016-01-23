package game;

import java.util.ArrayList;

import javafx.scene.image.Image;

public class Sushi extends Sprite {
	private final double INIT_SPEED = 3;
	private double speed;
	private double numFish;
	private static final String SUSHI_IMAGE = "sushi.png";
	private static final String TABLE_LEVEL_NAME = "Table Level";
	
	public Sushi(double x, double y, double num) {
		setImage(new Image(getClass().getClassLoader().getResourceAsStream(SUSHI_IMAGE)));
		speed = INIT_SPEED;
		numFish = num;
		setPosX(x);
		setPosY(y);
		setHeight(getImage().getHeight());
		setWidth(getImage().getWidth());
	}

	/*
	 * Handles Sushi sprite's movement in response to player key input.
	 */
	public void handleInput(ArrayList<String> input, double canvasWidth, double canvasHeight, String level) {
		if (input.contains("LEFT") && (getPosX() - speed >= 0)) {
			setPosX(getPosX() - speed);
		}
		if (input.contains("RIGHT") && (getPosX() + speed <= (canvasWidth - getWidth()))) {
			setPosX(getPosX() + speed);
		}
		if (level.equals(TABLE_LEVEL_NAME)) {
			if (input.contains("UP") && (getPosY() - speed >= 0)) {
				setPosY(getPosY() - speed);
			}
			if (input.contains("DOWN") && (getPosY() + speed <= (canvasHeight - getHeight()))) {
				setPosY(getPosY() + speed);
			}
		} 
	}
	
	/*
	 * Sets the Sushi sprite's speed.
	 */
	public void setSpeed(double s) {
		if (s >= 0) {
			this.speed = s;
		} else {
			this.speed = 0.2;
		}
	}
	
	/*
	 * Sets the Sushi sprite's number of fish.
	 */
	public void setNumFish(double n) {
		this.numFish = n;
	}
	
	/*
	 * Returns the Sushi sprite's speed.
	 */
	public double getSpeed() {
		return this.speed;
	}
	
	/*
	 * Returns the Sushi sprite's current number of fish.
	 */
	public double getNumFish() {
		return this.numFish;
	}
}
