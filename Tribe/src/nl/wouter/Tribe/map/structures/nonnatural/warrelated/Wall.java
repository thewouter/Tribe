package nl.wouter.Tribe.map.structures.nonnatural.warrelated;

import java.awt.Image;
import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;

import nl.wouter.Tribe.map.Direction;
import nl.wouter.Tribe.map.Map;
import nl.wouter.Tribe.map.entities.Entity;
import nl.wouter.Tribe.map.structures.BasicStructure;
import nl.wouter.Tribe.map.structures.Structure;
import nl.wouter.Tribe.screen.GameScreen;

public abstract class Wall extends BasicStructure {
	protected boolean[] neightboursAreConnected = {false, false, false, false};
	protected Texture[] images = new Texture[4];

	public Wall(Map map, GameScreen screen, int ID, int xPos, int yPos, Direction front) {
		super(map, screen, xPos, yPos, 6, 0, ID, front);
		loadImages();
		checkSides();
	}
	
	public Wall(Map map, GameScreen screen, int ID, int xPos, int yPos, int health, Direction front) {
		super(map, screen, xPos, yPos, 6, 0, ID, front);
		this.health = health;
		loadImages();
		checkSides();
	}
	
	public int getSize() {
		return 1;
	}
	
	public void checkSides(Entity ... requester){
		try{
		ArrayList<Entity> entities = map.getEntities();
		for(Entity e: requester){
			entities.add(e);
		}
		for(int i = 0; i < 4; i++){
			neightboursAreConnected[i] = checkSide(i, entities);
		}
		}catch(Exception e) {e.printStackTrace();}
	}
	
	public void setConnection(int side, boolean connected){
		neightboursAreConnected[side] = connected;
	}
	
	private boolean checkSide(int side, ArrayList<Entity> entities){
		ArrayList<Entity> es;
		int x = xPos;
		int y = yPos;
		if(side % 2 == 0){	
			x += side - 1;
		}else{
			y += side - 2;
		}
		es = map.getEntities(x, y, entities);
		for(Entity e: es){
			if (e instanceof Structure && ((Structure) e).connectsToWall()){
				if(e instanceof Wall) ((Wall)e).setConnection((side + 2) % 4, true);
				return true;
			}
		}
		return false;
	}
	
	protected abstract void loadImages();
	
	public void update() {}

	public int getMaxHealth() {
		return 0;
	}

	public String getExtraOne() {
		return "0";
	}
	
	public boolean connectsToWall(){
		return true;
	}
}
