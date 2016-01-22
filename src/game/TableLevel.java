package game;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class TableLevel extends Level {
	private ArrayList<Sprite> knifeList = new ArrayList<Sprite>();
	private ArrayList<Sprite> shrimpList = new ArrayList<Sprite>();
	private static final String TABLE_BACKGROUND_IMAGE = "tableBackground.png";
	private static final String KNIFE_IMAGE = "knife.png";
	private static final String SHRIMP_IMAGE = "shrimp.png";
	private ImageView myBackground;
	private static final String LEVEL_NAME = "Table Level";
	private static final long ONE_SECOND = 1000;
	
	public TableLevel (double numStartingFish) {
		this.setSushi(new Sushi(0, this.getCanvasHeight()/2, 0));
		this.getSushi().setNumFish(numStartingFish);
	}
	public String toString() {
		return LEVEL_NAME;
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
		}
		if (checkSpriteCollisions(shrimpList)) {
			updateSushiAndScore();
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
	}

	@Override
	protected void updateCanvas() {
		//addBackground("tableBackground.png");
		System.out.println("still updating canvas from table level");
		this.getBackground().setTranslateX(this.getBackground().getTranslateX() - 0.5);
		if (this.getBackground().getTranslateX() == -2048.0) {
			this.setStopLevel(true);
			Label transLabel = createLevelTransitionMessage();
			this.getRoot().getChildren().add(transLabel);
			scheduleInputTimer(this.getInput(), this);
		}
		//System.out.println(this.getBackground().getTranslateX());
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
	protected void clearObstacles() {
		knifeList.clear();
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
	
	public Label createLevelTransitionMessage() {
		Label transLabel = new Label("You made it across the table!\nNow you just have to make it past the hungry customer.\n\nPress ENTER to continue. Good luck!");
		transLabel.setWrapText(true);
		transLabel.setMinWidth(getCanvasWidth());
		transLabel.setMinHeight(getCanvasHeight());
		transLabel.setAlignment(Pos.CENTER);
		transLabel.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 20));
		transLabel.setTextFill(Color.GRAY);
		return transLabel;
	}
	
	private boolean readyForNextLevel(ArrayList<String> input) {
		return input.contains("ENTER");
	}
	
	private void scheduleInputTimer(ArrayList<String> input, Level curLevel) {
			Timer inputTimer = new Timer();
			inputTimer.schedule(new TimerTask() {
				public void run() {
					Platform.runLater(new Runnable() {
						public void run() {
							if (readyForNextLevel(input)) {
								input.remove("ENTER");
								curLevel.getMyGame().switchToCustomerLevel();
							} else {
								scheduleInputTimer(input, curLevel);
							}
						}
					});
				}
			}, ONE_SECOND, ONE_SECOND);	
		
	}
	
}
