package nl.wouter.Tribe.map.structures.nonnatural.warrelated;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import nl.wouter.Tribe.Images;
import nl.wouter.Tribe.map.Direction;
import nl.wouter.Tribe.map.Map;
import nl.wouter.Tribe.map.tiles.Tile;
import nl.wouter.Tribe.screen.GameScreen;

public class WoodenWall extends Wall {

	private static int ID = 213; 


	public WoodenWall(Map map, GameScreen screen, int xPos, int yPos, Direction front) {
		super(map, screen, ID, xPos, yPos, 6, 0, front);
	}
	
	public WoodenWall(Map map, GameScreen screen, int xPos, int yPos, int health, Direction front) {
		super(map, screen, ID, xPos, yPos, 6, 0, health, front);
	}


	public int getMaxHealth() {
		return 100;
	}

	public String getName() {
		return "Wooden Wall";
	}

	public HashMap<String, Integer> getCosts() {
		HashMap<String, Integer> costs = new HashMap<String, Integer>();
		costs.put("gold", 1);
		costs.put("wood", 1);
		
		return costs;
	}

	public int getHeadSpace() {
		return 1;
	}

}
