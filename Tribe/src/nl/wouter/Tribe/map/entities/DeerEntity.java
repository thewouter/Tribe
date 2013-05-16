package nl.wouter.Tribe.map.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import nl.wouter.Tribe.InputHandler;
import nl.wouter.Tribe.map.Map;
import nl.wouter.Tribe.rest.Util;
import nl.wouter.Tribe.screen.GameScreen;
import nl.wouter.Tribe.screen.Screen;

public class DeerEntity extends MovingEntity {
	public static final int WALK_RANGE = 5, WALK_CHANGE = 5, ID = 103;
	
	public DeerEntity(Map map, GameScreen screen, int xPos, int yPos){
		super(map, screen, xPos, yPos, ID);
	}
	
	public DeerEntity(Map map,GameScreen screen, int xPos, int yPos, int health){
		super(map, screen, xPos, yPos, ID);
		this.health = health;
	}
	
	public void update(){
		super.update();
		if(!isMoving() && Util.RANDOM.nextInt(1000) < WALK_CHANGE) moveRandomLocation(WALK_RANGE);
	}
	
	public void onRightClick(GameScreen screen, InputHandler input){
		//you can make a popup os here.
		
	}
	
	public void render(SpriteBatch batch){
		batch.setColor(Color.BLACK);
		Screen.font.drawLine(batch, getName(), getScreenX(), getScreenY());
	}
	
	public int getMaxHealth(){
		return 4;
	}
	
	public String getName(){
		return "Deer";
	}

	protected double getTravelTime(){
		return 200;
	}

	
	public int getSelectedOption() {return -1;}

	public void setSelectedOption(int indexSelected) {}
	
	public String getExtraOne() {
		return "0";
	}

	public boolean isMovable() {
		return false;
	}

	public int getHeadSpace() {
		return 1;
	}
}
