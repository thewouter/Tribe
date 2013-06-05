package nl.wouter.Tribe.map.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import nl.wouter.Tribe.Animation;
import nl.wouter.Tribe.Images;
import nl.wouter.Tribe.InputHandler;
import nl.wouter.Tribe.map.Map;
import nl.wouter.Tribe.map.tiles.Tile;
import nl.wouter.Tribe.multiplayer.client.MPMapClient;
import nl.wouter.Tribe.popups.entitypopup.EntityOptionsPopup;
import nl.wouter.Tribe.popups.entitypopup.Option;
import nl.wouter.Tribe.rest.Util;
import nl.wouter.Tribe.screen.GameScreen;
import nl.wouter.Tribe.screen.SPGameScreen;
import nl.wouter.Tribe.screen.Screen;

public class SheepEntity extends MovingEntity {
	
	private Animation sheepAnimation;
	private Animation currentAnimation;
	public static final int WALK_RANGE = 6, WALK_CHANGE = 1, TICKS_PER_SHEEP = 5, APROX_LIFETIME_IN_TICKS = 60000, ID = 101;
	private Sprite stillImage;

	public SheepEntity(Map map, GameScreen screen, int xPos, int yPos) {
		super(map, screen, xPos, yPos, ID);
		sheepAnimation = new Animation(TICKS_PER_SHEEP);
		currentAnimation = sheepAnimation;
		for(int i = 0; i < Images.sheep.length-1; i++){
			sheepAnimation.addScene(new Sprite(Images.sheep[i][0]));
		}
		stillImage = new Sprite(Images.sheep[Images.sheep.length - 1][0]);
	}
	
	public SheepEntity(Map map,GameScreen screen, int xPos, int yPos, int health){
		super(map,screen ,xPos, yPos, ID);
		this.health = health;
		sheepAnimation = new Animation(TICKS_PER_SHEEP);
		currentAnimation = sheepAnimation;
		for(int i = 0; i < Images.sheep.length-1; i++){
			sheepAnimation.addScene(new Sprite(Images.sheep[i][0]));
		}
		stillImage = new Sprite(Images.sheep[Images.sheep.length - 1][0]);
	}

	protected double getTravelTime() {
		return 1000;
	}
	
	
	public int getSelectedOption() {
		return -1;
	}
	
	public void update(){
		super.update();
		sheepAnimation.update();
		if(!(map instanceof MPMapClient)){
			if(!isMoving() && Util.RANDOM.nextInt(1000) < WALK_CHANGE) moveRandomLocation(WALK_RANGE);
			if(Util.RANDOM.nextInt(APROX_LIFETIME_IN_TICKS) == 0) map.removeEntity(this);
		}
	}
	
	public boolean onRightClick(Entity entityClicked, SPGameScreen screen, InputHandler input){
		if(entityClicked == this){
			EntityOptionsPopup popup = new EntityOptionsPopup(this, screen);
			Option option1 = new Option("getclosest",popup){
				public void onClick() {
					System.out.println(map.getClosestEntity(getxPos(), getyPos()).getName());
				}
			};
			popup.addOption(option1);
			popup.addOption(new Option("get closest moving entity",popup){
				public void onClick() {
					System.out.println(map.getClosestMovingEntity(getxPos(), getyPos()).getName());
				}
			});
			
			screen.setEntityPopup(popup);
			
		}else{
			follow(entityClicked, 0);
		}
		
		return true;
	}
	
	public void setSelectedOption(int indexSelected){}
	
	public void render(SpriteBatch batch){
		batch.setColor(Color.BLACK);
		currentAnimation.getImage().setPosition(getScreenX(), getScreenY() - Tile.getHeight() / 2);
		if(isMoving()) currentAnimation.getImage().draw(batch);
		
		else{
			stillImage.setPosition(getScreenX(), getScreenY() - Tile.getHeight() / 2);
			stillImage.draw(batch);
		}
	}
	
	public int getMaxHealth(){
		return 5;
	}

	public String getName(){
		return "Sheep";
	}
	
	public String getExtraOne() {
		return "0";
	}
	
	public boolean isMovable(){
		return true;
	}

	public int getHeadSpace() {
		return 1;
	}
}
