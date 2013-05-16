package nl.wouter.Tribe.map.structures.nonnatural;

import java.util.HashMap;

import nl.wouter.Tribe.map.Direction;
import nl.wouter.Tribe.map.Map;
import nl.wouter.Tribe.map.structures.BasicStructure;
import nl.wouter.Tribe.screen.GameScreen;

public class Farm extends BasicStructure{
	public static int ID = 209;
	public Farm(Map map, GameScreen screen, int xPos, int yPos, Direction front) {
		super(map, screen, xPos, yPos,2, 8, ID, front);
	}

	public Farm(Map map, GameScreen screen, int xPos, int yPos, int health, Direction front) {
		super(map, screen, xPos, yPos,2, 8, ID, front);
		this.health = health;
	}

	public int getHeadSpace() {
		return 2;
	}

	public int getSize() {
		return 3;
	}

	public void update() {
	}

	public int getMaxHealth() {
		return 6;
	}

	public String getName() {
		return "Farm";
	}

	public HashMap<String, Integer> getCosts() {
		HashMap<String, Integer> costs = new HashMap<String, Integer>();
		costs.put("gold", 5);
		costs.put("wood", 15);
		costs.put("stone", 3);
		return costs;
	}

	public String getExtraOne() {
		return "0";
	}

}
