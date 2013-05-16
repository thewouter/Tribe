package nl.wouter.Tribe.map.structures.nonnatural.warrelated;

import java.util.HashMap;

import nl.wouter.Tribe.map.Direction;
import nl.wouter.Tribe.map.Map;
import nl.wouter.Tribe.screen.GameScreen;

public class WoodenGate extends Gate {
	private static int ID = 214;
	
	public WoodenGate(Map map, GameScreen screen, int xPos, int yPos, Direction front) {
		super(map, screen, xPos, yPos, 8, 0, ID, front);
	}

	public WoodenGate(Map map, GameScreen screen, int xPos, int yPos, int health, Direction front) {
		super(map, screen, xPos, yPos, 8, 0, ID, front);
		this.health = health;
	}

	public int getMaxHealth() {
		return 350;
	}

	public String getName() {
		return "Wooden gate";
	}

	public HashMap<String, Integer> getCosts() {
		HashMap<String, Integer> costs = new HashMap<String, Integer>();
		costs.put("gold", 5);
		costs.put("wood", 20);
		return costs;
	}

	public int getHeadSpace() {
		return 2;
	}
}
