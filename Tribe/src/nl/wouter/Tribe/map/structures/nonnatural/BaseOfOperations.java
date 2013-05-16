package nl.wouter.Tribe.map.structures.nonnatural;

import java.util.HashMap;

import nl.wouter.Tribe.InputHandler;
import nl.wouter.Tribe.map.Direction;
import nl.wouter.Tribe.map.Map;
import nl.wouter.Tribe.map.entities.Entity;
import nl.wouter.Tribe.map.structures.BasicStructure;
import nl.wouter.Tribe.screen.GameScreen;

public class BaseOfOperations extends BasicStructure {
	
	public static int ID = 203;
	
	public BaseOfOperations(Map map, GameScreen screen, int xPos, int yPos, Direction front) {
		super(map,screen, xPos, yPos, 0, 4, ID, front);
		
	}
	public BaseOfOperations(Map map, GameScreen screen,int xPos, int yPos, int health, Direction front) {
		super(map, screen, xPos, yPos, 0, 4, ID, front);
		this.health = health;
		
	}
	
	public boolean onRightClick(Entity entityClicked, GameScreen screen, InputHandler input){
		if(entityClicked != this) return true;
		
		screen.inventory.showInventory();
		
		return false;
	}

	public int getHeadSpace() {
		return 2;
	}

	public int getSize() {
		return 2;
	}

	public void update() {
		
	}

	public int getMaxHealth() {
		return 300;
	}

	public String getName() {
		return "Base of operations";
	}

	public String getExtraOne() {return "0";	}
	
	public HashMap<String, Integer> getCosts(){
		HashMap<String, Integer> costs = new HashMap<String, Integer>();
		costs.put("gold", 100);
		costs.put("wood", 30);
		costs.put("stone", 50);
		return costs;
	}

}
