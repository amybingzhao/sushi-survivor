package game;

import java.util.ArrayList;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Sushi extends Sprite {
	private final double INIT_SPEED = 3;
	private double speed;
	private double numFish;
	private static final String SUSHI_IMAGE = "sushi.png";

	public Sushi(double x, double y, double num) {
		setImage(new Image(getClass().getClassLoader().getResourceAsStream(SUSHI_IMAGE)));
		speed = INIT_SPEED;
		numFish = num;
		setPosX(x);
		setPosY(y);
		setHeight(getImage().getHeight());
		setWidth(getImage().getWidth());
	}

	public void handleInput(ArrayList<String> input, double canvasWidth, double canvasHeight, String level) {
		if (input.contains("LEFT") && (getPosX() - speed >= 0)) {
			setPosX(getPosX() - speed);
		}
		if (input.contains("RIGHT") && (getPosX() + speed <= (canvasWidth - getWidth()))) {
			setPosX(getPosX() + speed);
		}
		if (level.equals("Table Level")) {
			if (input.contains("UP") && (getPosY() - speed >= 0)) {
				setPosY(getPosY() - speed);
			}
			if (input.contains("DOWN") && (getPosY() + speed <= (canvasHeight - getHeight()))) {
				setPosY(getPosY() + speed);
			}
		} else if (level.equals("Customer Level")) {
			if (input.contains("SPACE") && this.numFish > 0) {
				this.throwFish();
			}
		}
	}
	
	private void throwFish() {
		if (numFish > 0) {
			numFish--;
			//throw it
		}
	}
	
	public void setSpeed(double s) {
		this.speed = s;
	}
	
	public void setNumFish(double n) {
		this.numFish = n;
	}
	
	public double getSpeed() {
		return this.speed;
	}
	
	public double getNumFish() {
		return this.numFish;
	}
}
