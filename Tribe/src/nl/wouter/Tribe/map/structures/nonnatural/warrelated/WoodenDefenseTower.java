package nl.wouter.Tribe.map.structures.nonnatural.warrelated;

import java.util.HashMap;

import nl.wouter.Tribe.map.Direction;
import nl.wouter.Tribe.map.Map;
import nl.wouter.Tribe.screen.GameScreen;

public class WoodenDefenseTower extends DefenseTower {
	public static int ID = 216;

	public WoodenDefenseTower(Map map, GameScreen screen, int xPos, int yPos, Direction front) {
		super(map, screen, xPos, yPos, 8, 5, ID, front);
	}
	
	public WoodenDefenseTower(Map map, GameScreen screen, int xPos, int yPos, int health, Direction front) {
		super(map, screen, xPos, yPos, 8, 5, ID, front);
		this.health = health;
	}

	public int getHeadSpace() {
		return 2;
	}
	
	public int getMaxHealth() {
		return 100;
	}

	public String getName() {
		return "Wooden defensetower";
	}

	public HashMap<String, Integer> getCosts() {
		HashMap<String, Integer> costs = new HashMap<>();
		costs.put("wood", 25);
		costs.put("gold", 15);
		return costs;
	}

}
