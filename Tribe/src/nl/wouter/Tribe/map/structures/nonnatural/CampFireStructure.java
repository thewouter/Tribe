package nl.wouter.Tribe.map.structures.nonnatural;

import java.util.HashMap;

import nl.wouter.Tribe.map.Direction;
import nl.wouter.Tribe.map.Map;
import nl.wouter.Tribe.map.structures.BasicStructure;
import nl.wouter.Tribe.screen.GameScreen;

public class CampFireStructure extends BasicStructure{
	
	public static final int ID = 200;
	
	public CampFireStructure(Map map,GameScreen screen, int xPos, int yPos, Direction front){
		super(map, screen, xPos, yPos, 2, 0, ID, front);
	}
	
	public CampFireStructure(Map map, GameScreen screen, int xPos, int yPos,  int health, Direction front){
		super(map,screen, xPos,yPos,2,0,ID, front);
		this.health = health;
	}
	
	public void update(){
		
	}
	
	public int getSize(){
		return 1;
	}
	
	public int getMaxHealth(){
		return 10;
	}
	
	public String getName(){
		return "Campfire";
	}
	public int getHeadSpace(){
		return 1;
	}

	public String getExtraOne() {
		return "0";
	}
	
	public HashMap<String, Integer> getCosts(){
		return new HashMap<String, Integer>();
	}
}
