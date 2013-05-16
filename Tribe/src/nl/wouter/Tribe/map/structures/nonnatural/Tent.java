package nl.wouter.Tribe.map.structures.nonnatural;

import java.util.HashMap;
import java.util.LinkedList;

import nl.wouter.Tribe.map.Direction;
import nl.wouter.Tribe.map.Map;
import nl.wouter.Tribe.map.entities.Entity;
import nl.wouter.Tribe.map.entities.players.PlayerEntity;
import nl.wouter.Tribe.map.structures.BasicStructure;
import nl.wouter.Tribe.multiplayer.host.MPMapHost;
import nl.wouter.Tribe.screen.GameScreen;
import nl.wouter.Tribe.screen.MPGameScreen;

public class Tent extends BasicStructure{

	private int amountOfPlayers;
	public final static int ID = 201, TIME_TO_SPAWN_A_PLAYER = 120;
	private int time = 0;
	public LinkedList<PlayerEntity> players = new LinkedList<PlayerEntity>();
	public Tent(Map map, GameScreen screen, int xPos, int yPos, Direction front, int amountOfPlayers){
		super(map, screen, xPos, yPos,  0, 0,ID, front);
		this.setAmountOfPlayers(amountOfPlayers);
	}
	
	public Tent(Map map, GameScreen screen ,int xPos, int yPos, int health, Direction front, int amountOfPlayers){
		super(map,screen,xPos,yPos, 0, 0, ID, front);
		this.health = health;
		this.setAmountOfPlayers(amountOfPlayers);
	}
	
	public int getHeadSpace(){
		return 2;
	}
	
	public int getSize(){
		return 2;
	}
	
	public void update(){
		LinkedList<Entity> toBeAddedToTheMap = new LinkedList<Entity>();
		if(players.size() < amountOfPlayers)time++;
		if(time >= TIME_TO_SPAWN_A_PLAYER && players.size() < amountOfPlayers && screen != null && !(screen instanceof MPGameScreen)){
			PlayerEntity p = new PlayerEntity(map, screen, xPos + 3, yPos + 3, this);
			if(map instanceof MPMapHost){
				p.owner = owner;
			}
			toBeAddedToTheMap.add(p);
			screen.inventory.addMeat(-1);
			screen.inventory.addGold(-1);
			time = 0;
		}
		
		LinkedList<PlayerEntity> toBeRemoved = new LinkedList<PlayerEntity>();
		
		for(PlayerEntity p : players){
			if(!map.getEntities().contains(p) && !map.notOnMap.contains(p)){
				toBeRemoved.add(p);
			}
		}
		
		players.removeAll(toBeRemoved);
		map.addEntity(toBeAddedToTheMap);
		
		for(Entity e: toBeAddedToTheMap){
			if(e instanceof PlayerEntity){
				players.add((PlayerEntity) e);
			}
		}
	}
	
	public int getMaxHealth(){
		return 20;
	}
	
	public String getName(){
		return "Tent for " + amountOfPlayers + " players";
	}

	public String getExtraOne() {
		return "1 " + amountOfPlayers;
	}
	
	public HashMap<String, Integer> getCosts(){
		HashMap<String, Integer> costs = new HashMap<String, Integer>();
		costs.put("gold", 5);
		return costs;
	}

	public int getAmountOfPlayers() {
		return amountOfPlayers;
	}

	public void setAmountOfPlayers(int amountOfPlayers) {
		this.amountOfPlayers = amountOfPlayers;
	}

}
