package game;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Sprite {
	private Image image;
	private double posX;
	private double posY;
	private double height;
	private double width;
	
	/*
	 * Sets the image for the sprite.
	 */
	public void setImage(String filename) {
		image = new Image(getClass().getClassLoader().getResourceAsStream(filename));
		setHeight(image.getHeight());
		setWidth(image.getWidth());
	}
	
	/*
	 * Sets the (x,y) position of the sprite.
	 */
	public void setPosition(double x, double y) {
		setPosX(x);
		setPosY(y);
	}
	
	/*
	 * Draws the sprite on the canvas.
	 */
	public void render(GraphicsContext gc) {
		gc.drawImage(getImage(), getPosX(), getPosY());
	}
	
	/*
	 * Returns the boundary of the sprite image.
	 */
	public Rectangle2D getBoundary() {
		return new Rectangle2D(getPosX(), getPosY(), getWidth(), getHeight());
	}
	
	/*
	 * Returns true if the sprite image intersects another sprite's boundary.
	 */
	public boolean intersects(Rectangle2D r) {
		return r.intersects(this.getBoundary());
	}

	/*
	 * Returns the y-position of the sprite.
	 */
	public double getPosY() {
		return posY;
	}

	/*
	 * Sets the y-position of the sprite.
	 */
	public void setPosY(double posY) {
		this.posY = posY;
	}

	/*
	 * Returns the x-position of the sprite.
	 */
	public double getPosX() {
		return posX;
	}
	
	/*
	 * Sets the x-position of the sprite.
	 */
	public void setPosX(double posX) {
		this.posX = posX;
	}

	/*
	 * Returns the sprite's image.
	 */
	public Image getImage() {
		return image;
	}

	/*
	 * Sets the sprite's image.
	 */
	public void setImage(Image image) {
		this.image = image;
	}

	/*
	 * Returns the sprite's height.
	 */
	public double getHeight() {
		return height;
	}

	/*
	 * Sets the sprite's height.
	 */
	public void setHeight(double height) {
		this.height = height;
	}

	/*
	 * Returns the sprite's width.
	 */
	public double getWidth() {
		return width;
	}
	
	/*
	 * Gets the sprite's height.
	 */
	public void setWidth(double width) {
		this.width = width;
	}
	
}
