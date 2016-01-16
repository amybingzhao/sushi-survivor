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
		populateSpriteArrayList("knife.png", knifeList);
		populateSpriteArrayList("shrimp.png", shrimpList);
	}
	
	private void populateSpriteArrayList(String filename, ArrayList<Sprite> array) {
		for (int i = 0; i < NUM_SPRITES_PER_TYPE; i++) {
			Sprite s = generateSprite(filename);
			array.add(s);
			s.render(myGc);
		}
	}

	private Sprite generateSprite(String filename) {
		// TODO Auto-generated method stub
		Sprite sprite = new Sprite();
		sprite.setImage(filename);
		double x = (CANVAS_WIDTH/3) + (CANVAS_WIDTH - sprite.width) * Math.random();
		double y = (CANVAS_HEIGHT - sprite.height) * Math.random();
		sprite.setPosition(x, y);
		return sprite;
	}

	private void moveSpritesForward(ArrayList<Sprite> sprites) {
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
		};
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

	public void replaceOutOfBoundsSprites(ArrayList<Sprite> sprites, String filename) {
		for (int i = 0; i < sprites.size(); i++) {
			Sprite s = sprites.get(i);
			if ((s.posX + s.width) < 0) {
				sprites.remove(i);
				addMoreSprites(sprites, filename, 1);
			}
		}
		// replace the ones that've been collided with:
		// TODO: make into own method
		int diff = NUM_SPRITES_PER_TYPE - sprites.size();
		addMoreSprites(sprites, filename, diff);
	}
	
	public void addMoreSprites(ArrayList<Sprite> sprites, String filename, int num) {
		for (int i = 0; i < num; i++) {
			Sprite s = generateSprite(filename);
			s.posX = CANVAS_WIDTH;
			sprites.add(s);
			s.render(myGc);
		}
	}
}
