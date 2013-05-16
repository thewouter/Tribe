package nl.wouter.Tribe.map.structures.natural;

import java.awt.image.BufferedImage;
import java.util.HashMap;

import nl.wouter.Tribe.map.Direction;
import nl.wouter.Tribe.map.Map;
import nl.wouter.Tribe.map.structures.BasicStructure;
import nl.wouter.Tribe.screen.GameScreen;

public class TreeStructure extends BasicStructure{
	
	BufferedImage image;
	public final static int ID = 202;

	public TreeStructure(Map map, GameScreen player, int xPos, int yPos, Direction front) {
		super(map, player, xPos, yPos, 3, 0, ID, front);
	}
	
	
	public TreeStructure(Map map, GameScreen screen, int xPos, int yPos, int health, Direction front){
		super(map, screen, xPos, yPos, 3, 0, ID, front);
		this.health = health;
	}

	public void update() {
		
	}
	
	public int getMaxHealth() {
		return 2;
	}
	
	public String getName() {
		return "tree";
	}

	public int getHeadSpace() {
		return 2;
	}

	public int getSize() {
		return 1;
	}

	public String getExtraOne() {
		return "0";
	}
	
	public HashMap<String, Integer> getCosts(){
		HashMap<String, Integer> costs = new HashMap<String, Integer>();
		costs.put("gold", 1);
		return costs;
	}

}
