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
		double x = (CANVAS_WIDTH - sprite.width) * Math.random();
		//double x = CANVAS_WIDTH;
		double y = (CANVAS_HEIGHT - sprite.height) * Math.random();
		sprite.setPosition(x, y);
		return sprite;
	}

	@Override
	protected void moveSpritesForward(ArrayList<Sprite> sprites) {
		// TODO Auto-generated method stub
		
	}

}
