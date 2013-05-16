package nl.wouter.Tribe.map.structures.nonnatural.warrelated;

import nl.wouter.Tribe.map.Direction;
import nl.wouter.Tribe.map.Map;
import nl.wouter.Tribe.map.entities.Entity;
import nl.wouter.Tribe.map.structures.BasicStructure;
import nl.wouter.Tribe.screen.GameScreen;

public abstract class Gate extends BasicStructure {

	public Gate(Map map, GameScreen screen, int xPos, int yPos, int textureX, int textureY, int ID, Direction front) {
		super(map, screen, xPos, yPos, textureX, textureY, ID, front);
	}
	
	public int getSize() {
		return 3;
	}

	public void update() {
		
	}
	
	public boolean isWalkable(int x, int y, Entity toPass){
		x -= xPos;
		y -= yPos;
		
		if(!toPass.isOwnedByPlayer || x > getSize() || y > getSize() || x < 0 || y < 0) return false;
		if(getFront() == Direction.SOUTH_EAST)return y == 1;
		return x == 1;
	}

	public String getExtraOne() {
		return "0";
	}
	
	public boolean connectsToWall(){
		return true;
	}
}
