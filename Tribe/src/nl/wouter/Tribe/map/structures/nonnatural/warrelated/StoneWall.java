package nl.wouter.Tribe.map.structures.nonnatural.warrelated;

import java.util.HashMap;

import nl.wouter.Tribe.map.Direction;
import nl.wouter.Tribe.map.Map;
import nl.wouter.Tribe.screen.GameScreen;

public class StoneWall extends Wall {
	private static int ID = 217;
	public StoneWall(Map map, GameScreen screen, int xPos, int yPos,Direction front) {
		super(map, screen, ID, xPos, yPos, 11, 0, front);
	}

	public StoneWall(Map map, GameScreen screen, int xPos, int yPos,
			int health, Direction front) {
		super(map, screen, ID, xPos, yPos, 11, 0, health, front);
	}

	public int getMaxHealth() {
		return 300;
	}

	public String getName() {
		return "Stone wall";
	}

	public HashMap<String, Integer> getCosts() {
		HashMap<String, Integer> costs = new HashMap<String, Integer>();
		costs.put("gold", 1);
		costs.put("stone", 2);
		return costs;
	}

	public int getHeadSpace() {
		return 1;
	}

}
