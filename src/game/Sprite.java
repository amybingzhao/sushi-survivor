package game;

import java.util.ArrayList;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Sprite {
	private Image image;
	private double posX;
	private double posY;
	private double height;
	private double width;
	
	public void setImage(String filename) {
		image = new Image(getClass().getClassLoader().getResourceAsStream(filename));
		setHeight(image.getHeight());
		setWidth(image.getWidth());
	}
	
	public void setPosition(double x, double y) {
		setPosX(x);
		setPosY(y);
	}
	
	public void render(GraphicsContext gc) {
		gc.drawImage(getImage(), getPosX(), getPosY());
	}
	
	public Rectangle2D getBoundary() {
		return new Rectangle2D(getPosX(), getPosY(), getWidth(), getHeight());
	}
	
	public boolean intersects(Rectangle2D r) {
		return r.intersects(this.getBoundary());
	}

	public double getPosY() {
		return posY;
	}

	public void setPosY(double posY) {
		this.posY = posY;
	}

	public double getPosX() {
		return posX;
	}

	public void setPosX(double posX) {
		this.posX = posX;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}
	
}
