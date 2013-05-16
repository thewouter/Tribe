package nl.wouter.Tribe.map.structures.natural;

import nl.wouter.Tribe.InputHandler;
import nl.wouter.Tribe.map.Map;
import nl.wouter.Tribe.map.entities.Entity;
import nl.wouter.Tribe.screen.GameScreen;
import nl.wouter.Tribe.screen.SPGameScreen;

public class GoldMine extends MineStructure {
	public static int ID = 300, HEALTH_SMALL = 100, HEALTH_MEDIUM = 200, HEALTH_LARGE = 300;
	
	public GoldMine  (Map map, GameScreen screen, int xPos, int yPos, int size) {
		super(map,screen, xPos, yPos, ID, size);
		map.removeEntity(map.getEntity(getxPos() - getSize(), getyPos() - getSize()));
	}
	
	public GoldMine(Map map, GameScreen screen, int xPos, int yPos, int size, int health) {
		super(map, screen, xPos, yPos ,ID , size, health);
	}

	public boolean onRightClick(Entity entityClicked, SPGameScreen screen, InputHandler input){
		return false;
	}
	
	public void update() {
		if(getHealth()  <=  100){
			size = 1;
		}else if(getHealth() <= 200){
			size = 2;
		}else{
			size = 3;
		}
	}

	public int getMaxHealth() {
		switch(size){
		case 1:
			return HEALTH_SMALL;
		case 2:
			return HEALTH_MEDIUM;
		case 3:
			return HEALTH_LARGE;
		default:
			return 0;
		}
	}

	public String getName() {
		switch(size){
		case 1:
			return "Goldmine small";
		case 2:
			return "Goldmine medium";
		case 3:
			return "Goldmine large";
		default:
			return "unknown name??";
		}
		
	}
	
	public void damage(int damage){
		super.damage(damage);
		if (health <= HEALTH_MEDIUM){
			setSize(2);
		}
		if(health <= HEALTH_SMALL){
			setSize(1);
		}
	}
	
	public void mine(int i, Entity miner){
		damage(i);
		miner.screen.inventory.addGold(1);
	}
}
