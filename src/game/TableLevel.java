package game;

import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

public class TableLevel extends Level {
	public ArrayList<Sprite> knifeList = new ArrayList<Sprite>();
	public ArrayList<Sprite> shrimpList = new ArrayList<Sprite>();
	

	public String toString() {
		return "Table Level";
	}

	@Override
	protected void populateSceneWithSprites() {
		// TODO Auto-generated method stub
		sushi = new Sushi(0, CANVAS_HEIGHT/2, 0);
		sushi.render(myGc);
		populateSpriteArrayList("knife.png", knifeList);
		populateSpriteArrayList("shrimp.png", shrimpList);
	}

	public void moveSpritesForward(ArrayList<Sprite> sprites) {
		// TODO Auto-generated method stub
		for (int i = 0; i < sprites.size(); i++) {
			Sprite s = sprites.get(i);
			double curX = s.posX;
			s.posX = curX - spriteSpeed;
			s.render(myGc);
		}
	}

	@Override
	protected void checkListCollisions() {
		// TODO Auto-generated method stub
		if(checkSpriteCollisions(knifeList)) {
			//gameOver();
			System.out.println("game over");
		}
		if (checkSpriteCollisions(shrimpList)) {
			updateSushi();
			System.out.println("ran into shrimp");
		}
	}

	@Override
	protected void updateSushi() {
		// TODO Auto-generated method stub
		sushi.numFish++;
		System.out.println("numFish = " + sushi.numFish);
	}

	@Override
	protected void updateCanvas() {
		// TODO Auto-generated method stub
		moveSpritesForward(knifeList);
		replaceOutOfBoundsSprites(knifeList, "knife.png");
		moveSpritesForward(shrimpList);
		replaceOutOfBoundsSprites(shrimpList, "shrimp.png");
	}

	@Override
	protected double generateRandomX(Sprite sprite) {
		// TODO Auto-generated method stub
		return (CANVAS_WIDTH/3) + (CANVAS_WIDTH - sprite.width) * Math.random();
	}

	@Override
	protected double generateRandomY(Sprite sprite) {
		// TODO Auto-generated method stub
		return (CANVAS_HEIGHT - sprite.height) * Math.random();
	}

	@Override
	protected boolean outOfBounds(Sprite s) {
		// TODO Auto-generated method stub
		return (s.posX + s.width) < 0;
	}

	
}
