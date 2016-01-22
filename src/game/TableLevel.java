package game;

import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class TableLevel extends Level {
	private ArrayList<Sprite> knifeList = new ArrayList<Sprite>();
	private ArrayList<Sprite> shrimpList = new ArrayList<Sprite>();
	private static final String TABLE_BACKGROUND_IMAGE = "tableBackground.png";
	private static final String KNIFE_IMAGE = "knife.png";
	private static final String SHRIMP_IMAGE = "shrimp.png";
	private ImageView myBackground;
	
	public TableLevel (double numStartingFish) {
		this.setSushi(new Sushi(0, this.getCanvasHeight()/2, 0));
		this.getSushi().setNumFish(numStartingFish);
	}
	public String toString() {
		return "Table Level";
	}

	@Override
	protected void populateSceneWithSprites() {
		this.getSushi().render(this.getGraphicsContext());
		populateSpriteArrayList(KNIFE_IMAGE, knifeList);
		populateSpriteArrayList(SHRIMP_IMAGE, shrimpList);
	}

	public void moveSpritesForward(ArrayList<Sprite> sprites) {
		for (int i = 0; i < sprites.size(); i++) {
			Sprite s = sprites.get(i);
			double curX = s.getPosX();
			s.setPosX(curX - this.getSpriteSpeed());
			s.render(this.getGraphicsContext());
		}
	}

	@Override
	protected void checkListCollisions() {
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
		this.getSushi().setNumFish(this.getSushi().getNumFish() + 1);
		getScoreLabel().setText("Score: " + Integer.toString((int) this.getSushi().getNumFish()));
		System.out.println("numFish = " + this.getSushi().getNumFish());
	}

	@Override
	protected void updateCanvas() {
		//addBackground("tableBackground.png");
		this.getBackground().setTranslateX(this.getBackground().getTranslateX() - 0.5);
		System.out.println(this.getBackground().getTranslateX());
		moveSpritesForward(knifeList);
		moveSpritesForward(shrimpList);
	}
	
	@Override
	protected double generateRandomX(Sprite sprite) {
		return (this.getCanvasWidth()/3) + (this.getCanvasWidth() - sprite.getWidth()) * Math.random();
	}

	@Override
	protected double generateRandomY(Sprite sprite) {
		return (this.getCanvasHeight() - sprite.getHeight()) * Math.random();
	}

	@Override
	protected boolean outOfBounds(Sprite s) {
		return (s.getPosX() + s.getWidth()) < 0;
	}
	@Override
	protected String getInstructions() {
		return "Use the arrow keys to move.\nCollect shrimp and dodge the knives!\nIf you get hit by a knife then it's game over.";
	}
	@Override
	protected void clearLists() {
		knifeList.clear();
		shrimpList.clear();
	}
	@Override
	protected void replaceSprites() {
		// TODO Auto-generated method stub
		replaceOutOfBoundsSprites(knifeList, KNIFE_IMAGE);
		replaceOutOfBoundsSprites(shrimpList, SHRIMP_IMAGE);
		addSpritesToGetNumSpritesPerType(knifeList, KNIFE_IMAGE);
		addSpritesToGetNumSpritesPerType(shrimpList, SHRIMP_IMAGE);
	}

	public String getBackgroundImageName() {
		return TABLE_BACKGROUND_IMAGE;
	}
	
}
