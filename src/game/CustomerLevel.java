package game;

import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

public class CustomerLevel extends Level {
	public ArrayList<Sprite> soySauceList;
	public Sprite chopstick;
	
	public String toString() {
		return "Customer Level";
	}

	@Override
	protected void populateSceneWithSprites() {
		// TODO Auto-generated method stub
		
	}
	
	private void moveSpritesForward(ArrayList<Sprite> sprites) {
		// TODO Auto-generated method stub
		for (int i = 0; i < sprites.size(); i++) {
			Sprite s = sprites.get(i);
			double curY = s.posY;
			s.posY = curY + spriteSpeed;
			s.render(myGc);
		}
	}

	@Override
	protected void checkListCollisions() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void updateSushi() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void updateCanvas() {
		// TODO Auto-generated method stub
		
	}

}
