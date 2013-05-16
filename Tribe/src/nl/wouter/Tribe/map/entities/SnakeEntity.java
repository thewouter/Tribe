package nl.wouter.Tribe.map.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import nl.wouter.Tribe.InputHandler;
import nl.wouter.Tribe.map.Map;
import nl.wouter.Tribe.popups.entitypopup.EntityOptionsPopup;
import nl.wouter.Tribe.popups.entitypopup.Option;
import nl.wouter.Tribe.rest.Util;
import nl.wouter.Tribe.screen.GameScreen;
import nl.wouter.Tribe.screen.SPGameScreen;
import nl.wouter.Tribe.screen.Screen;

public class SnakeEntity extends MovingEntity {
	
	private boolean scared = false;
	private int ticksScared = 11;
	public static final int ID = 100;
	
	
	public static final int WALK_RANGE = 3, WALK_CHANGE_NORMAL = 3, MAX_TICKS_SCARED = 1000, WALK_CHANGE_SCARED = 20;

	public SnakeEntity(Map map, GameScreen player, int xPos, int yPos) {
		super(map,player, xPos, yPos, ID);
	}
	
	public SnakeEntity(Map map, GameScreen screen, int xPos, int yPos, int health){
		super(map, screen, xPos, yPos, ID);
		this.health = health;
	}

	protected double getTravelTime() {
		return 100;
	}

	public int getSelectedOption() {
		return -1;
	}
	
	public void update(){
		if(ticksScared >= MAX_TICKS_SCARED)scared = false;
		if(scared) {
			ticksScared++;
		}
		
		
		super.update();
		if(scared)if(!isMoving() && Util.RANDOM.nextInt(1000) < WALK_CHANGE_SCARED) moveRandomLocation(WALK_RANGE);
		else if(!isMoving() && Util.RANDOM.nextInt(1000) < WALK_CHANGE_NORMAL) moveRandomLocation(WALK_RANGE);
		
	}
	
	public void onRightClick(SPGameScreen screen, InputHandler input){
		EntityOptionsPopup popup = new EntityOptionsPopup(this, screen);
		Option option1 = new Option("boe!",popup){
			public void onClick() {
				scare();
			}
		};
		popup.addOption(option1);
		screen.setEntityPopup(popup);
	}
	
	public void scare(){
		scared  = true;
		ticksScared = 0;
	}
	
	public void setSelectedOption(int indexSelected) {}

	public void render(SpriteBatch batch) {
		batch.setColor(Color.YELLOW);
		Screen.font.drawLine(batch, getName(), getScreenX(), getScreenY());
	}

	public int getMaxHealth() {
		return 5;
	}

	public String getName() {
		return "Snake";
	}

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
