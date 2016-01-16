package game;

import java.util.ArrayList;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Sushi extends Sprite {
	private static final double INIT_SPEED = 3;
	public double speed;
	public double numFish;

	public Sushi(double x, double y) {
		image = new Image("sushi.jpg");
		speed = INIT_SPEED;
		numFish = 0;
		posX = x;
		posY = y;
		height = image.getHeight();
		width = image.getWidth();
	}

	public void handleInput(ArrayList<String> input, double canvasWidth, double canvasHeight, String level) {
		if (input.contains("LEFT") && (posX - speed >= 0)) {
			posX = posX - speed;
		}
		if (input.contains("RIGHT") && (posX + speed <= (canvasWidth - width))) {
			posX = posX + speed;
		}
		if (level.equals("Table Level")) {
			if (input.contains("UP") && (posY - speed >= 0)) {
				posY = posY - speed;
			}
			if (input.contains("DOWN") && (posY + speed <= (canvasHeight - height))) {
				posY = posY + speed;
			}
		} else if (level.equals("Customer Levl")) {
			if (input.contains("SPACE") && this.numFish > 0) {
				numFish--;
				//throw fish upwards
			}
		}
	}
}
