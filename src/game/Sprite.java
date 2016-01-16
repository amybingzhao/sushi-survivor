package game;

import java.util.ArrayList;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Sprite {
	public Image image;
	public double posX;
	public double posY;
	public double height;
	public double width;
	
	public void setImage(String filename) {
		image = new Image(filename);
		height = image.getHeight();
		width = image.getWidth();
	}
	
	public void setPosition(double x, double y) {
		posX = x;
		posY = y;
	}
	
	public void render(GraphicsContext gc) {
		gc.drawImage(image, posX, posY);
	}
	
	public Rectangle2D getBoundary() {
		return new Rectangle2D(posX, posY, width, height);
	}
	
	public boolean intersects(Rectangle2D r) {
		return r.intersects(this.getBoundary());
	}
	
}
