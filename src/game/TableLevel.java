package game;

import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class TableLevel extends Level {
	private ArrayList<Sprite> knifeList = new ArrayList<Sprite>();
	private ArrayList<Sprite> shrimpList = new ArrayList<Sprite>();
	private static final String TABLE_BACKGROUND_IMAGE = "tableBackground.png";
	private static final String KNIFE_IMAGE = "knife.png";
	private static final String SHRIMP_IMAGE = "shrimp.png";
	
	public TableLevel (double numStartingFish) {
		this.setSushi(new Sushi(0, this.getCanvasHeight()/2, 0));
		this.getSushi().setNumFish(numStartingFish);
	}
	public String toString() {
		return "Table Level";
	}

	@Override
	protected void populateSceneWithSprites() {
		// TODO Auto-generated method stub
		addBackground(TABLE_BACKGROUND_IMAGE);
		this.getSushi().render(this.getGraphicsContext());
		populateSpriteArrayList(KNIFE_IMAGE, knifeList);
		populateSpriteArrayList(SHRIMP_IMAGE, shrimpList);
	}

	public void moveSpritesForward(ArrayList<Sprite> sprites) {
		// TODO Auto-generated method stub
		for (int i = 0; i < sprites.size(); i++) {
			Sprite s = sprites.get(i);
			double curX = s.getPosX();
			s.setPosX(curX - this.getSpriteSpeed());
			s.render(this.getGraphicsContext());
		}
	}

	@Override
	protected void checkListCollisions() {
		// TODO Auto-generated method stub
		if(checkSpriteCollisions(knifeList)) {
			knifeList.clear();
			shrimpList.clear();
			setGameOver(true);
			super.setWin(false);
			setStopLevel(true);
			System.out.println("game over");
		}
		if (checkSpriteCollisions(shrimpList)) {
			updateSushiAndScore();
			System.out.println("ran into shrimp");
		}
	}

	public void setGameOver(boolean gameOver) {
		if (gameOver) {
			super.setGameOver(true);
		}
	}
	
	@Override
	protected void updateSushiAndScore() {
		// TODO Auto-generated method stub
		this.getSushi().setNumFish(this.getSushi().getNumFish() + 1);
		getScoreLabel().setText("Score: " + Integer.toString((int) this.getSushi().getNumFish()));
		System.out.println("numFish = " + this.getSushi().getNumFish());
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
		return (this.getCanvasWidth()/3) + (this.getCanvasWidth() - sprite.getWidth()) * Math.random();
	}

	@Override
	protected double generateRandomY(Sprite sprite) {
		// TODO Auto-generated method stub
		return (this.getCanvasHeight() - sprite.getHeight()) * Math.random();
	}

	@Override
	protected boolean outOfBounds(Sprite s) {
		// TODO Auto-generated method stub
		return (s.getPosX() + s.getWidth()) < 0;
	}
	@Override
	protected String getInstructions() {
		// TODO Auto-generated method stub
		return "Use the arrow keys to move.\nCollect shrimp and dodge the knives!\nIf you get hit by a knife then it's game over.";
	}
	@Override
	protected void clearLists() {
		knifeList.clear();
		shrimpList.clear();
	}

	
}
