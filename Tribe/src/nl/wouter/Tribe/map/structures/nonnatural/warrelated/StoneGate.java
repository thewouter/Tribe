package nl.wouter.Tribe.map.structures.nonnatural.warrelated;

import java.util.HashMap;

import nl.wouter.Tribe.map.Direction;
import nl.wouter.Tribe.map.Map;
import nl.wouter.Tribe.screen.GameScreen;

public class StoneGate extends Gate {
	private static int ID = 218;
	public StoneGate(Map map, GameScreen screen, int xPos, int yPos, Direction front) {
		super(map, screen, xPos, yPos, 13, 0, ID, front);
	}
	
	public StoneGate(Map map, GameScreen screen, int xPos, int yPos, int health, Direction front) {
		super(map, screen, xPos, yPos, 13, 0, ID, front);
		this.health = health;
	}

	public int getMaxHealth() {
		return 400;
	}

	public String getName() {
		return "Stone gate";
	}

	public HashMap<String, Integer> getCosts() {
		HashMap<String, Integer> costs = new HashMap<String, Integer>();
		costs.put("gold", 20);
		costs.put("wood", 5);
		costs.put("stone", 50);
		return costs;
	}

	public int getHeadSpace() {
		return 2;
	}

}
