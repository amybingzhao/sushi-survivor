package game;

import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class TableLevel extends Level {
	public ArrayList<Sprite> knifeList = new ArrayList<Sprite>();
	public ArrayList<Sprite> shrimpList = new ArrayList<Sprite>();
	private static final String TABLE_BACKGROUND_IMAGE = "tableBackground.png";
	private static final String KNIFE_IMAGE = "knife.png";
	private static final String SHRIMP_IMAGE = "shrimp.png";
	
	public TableLevel (double numStartingFish) {
		sushi = new Sushi(0, CANVAS_HEIGHT/2, 0);
		sushi.numFish = numStartingFish;
	}
	public String toString() {
		return "Table Level";
	}

	@Override
	protected void populateSceneWithSprites() {
		// TODO Auto-generated method stub
		addBackground(TABLE_BACKGROUND_IMAGE);
		sushi.render(myGc);
		populateSpriteArrayList(KNIFE_IMAGE, knifeList);
		populateSpriteArrayList(SHRIMP_IMAGE, shrimpList);
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
			knifeList.clear();
			shrimpList.clear();
			gameOver = true;
			win = false;
			stopLevel = true;
			System.out.println("game over");
		}
		if (checkSpriteCollisions(shrimpList)) {
			updateSushiAndScore();
			System.out.println("ran into shrimp");
		}
	}

	@Override
	protected void updateSushiAndScore() {
		// TODO Auto-generated method stub
		sushi.numFish++;
		scoreLabel.setText("Score: " + Integer.toString((int) sushi.numFish));
		System.out.println("numFish = " + sushi.numFish);
	}

	@Override
	protected void updateCanvas() {
		// TODO Auto-generated method stub
		addBackground("tableBackground.png");
		moveSpritesForward(knifeList);
		replaceOutOfBoundsSprites(knifeList, KNIFE_IMAGE);
		moveSpritesForward(shrimpList);
		replaceOutOfBoundsSprites(shrimpList, SHRIMP_IMAGE);
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
	@Override
	protected String getInstructions() {
		// TODO Auto-generated method stub
		return "Use the arrow keys to move.\nCollect shrimp and dodge the knives!\nIf you get hit by a knife then it's game over.";
	}

	
}
