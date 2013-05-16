package nl.wouter.Tribe.map.structures.nonnatural.warrelated;

import java.util.HashMap;

import nl.wouter.Tribe.map.Direction;
import nl.wouter.Tribe.map.Map;
import nl.wouter.Tribe.screen.GameScreen;

public class StoneDefenseTower extends DefenseTower {


	public StoneDefenseTower(Map map, GameScreen screen, int xPos, int yPos, Direction front) {
		super(map, screen, xPos, yPos, 4, 0, ID, front);
	}
	
	public StoneDefenseTower(Map map, GameScreen screen, int xPos, int yPos, int health, Direction front) {
		super(map, screen, xPos, yPos, 4, 0, ID, front);
		this.health = health;
	}

	public int getHeadSpace() {
		return 2;
	}

	public int getMaxHealth() {
		return 300;
	}

	public String getName() {
		return "Primary defense Tower";
	}

	public HashMap<String, Integer> getCosts() {
		HashMap<String, Integer> costs = new HashMap<String, Integer>();
		costs.put("gold", 20);
		costs.put("wood", 5);
		costs.put("stone", 25);
		return costs;
	}
}
