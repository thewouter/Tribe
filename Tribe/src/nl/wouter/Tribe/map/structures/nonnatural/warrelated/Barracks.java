package nl.wouter.Tribe.map.structures.nonnatural.warrelated;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import nl.wouter.Tribe.Images;
import nl.wouter.Tribe.InputHandler;
import nl.wouter.Tribe.map.Direction;
import nl.wouter.Tribe.map.Map;
import nl.wouter.Tribe.map.entities.Entity;
import nl.wouter.Tribe.map.entities.MovingEntity;
import nl.wouter.Tribe.map.entities.players.Bow;
import nl.wouter.Tribe.map.entities.players.PlayerEntity;
import nl.wouter.Tribe.map.entities.players.Soldier;
import nl.wouter.Tribe.map.structures.BasicStructure;
import nl.wouter.Tribe.popups.screenpopup.BarracksPopup;
import nl.wouter.Tribe.screen.GameScreen;
import nl.wouter.Tribe.screen.MPGameScreen;
import nl.wouter.Tribe.screen.Screen;

public class Barracks extends BasicStructure {
	private Texture image = Images.school.getTexture();
	public static int ID = 211, MAX_SOLDIER = 5;
	public boolean isSelected = false;
	public ArrayList<Soldier> soldiers = new ArrayList<Soldier>();
	private ArrayList<PlayerEntity> players = new ArrayList<>();
	private BarracksPopup popup;
	private int time = 0;
	
	/**
	 * barracks can teach and house soldiers.
	 */
	
	public Barracks(Map map, GameScreen screen, int xPos, int yPos, Direction front) {
		super(map, screen, xPos, yPos, 5, 9, ID, front);
		popup = new BarracksPopup(screen, image, this, screen.input);
	}

	public Barracks(Map map, GameScreen screen, int xPos, int yPos, int health, Direction front) {
		super(map, screen, xPos, yPos, 5, 9, ID, front);
		popup = new BarracksPopup(screen, image, this, screen.input);
		this.health = health;
	}
	
	public BarracksPopup getpopup(){
		return popup;
	}

	public int getHeadSpace() {
		return 2;
	}

	public int getSize() {
		return 3;
	}
	
	public void render(SpriteBatch batch){
		super.render(batch);
		Screen.font.drawLine(batch, popup.bow.amountToBuild + "", getScreenX(), getScreenY());
	}
	
	public boolean onRightClick(Entity entityClicked, GameScreen screen, InputHandler input){
		screen.setPopup(popup);
		return false;
	}

	public void update() {
		if(screen instanceof MPGameScreen) return;
		LinkedList<Soldier> toRemove = new LinkedList<Soldier>();
		for(int x = -1; x <=1; x++){
			for(int y = -1; y <= 1; y++){
				Entity e = map.getEntity(x + xPos, y + yPos);
				if(e != null && e instanceof PlayerEntity&& ((MovingEntity)e).entityGoal == this && players.size() + soldiers.size() < MAX_SOLDIER){
					players.add((PlayerEntity) e);
				}
			}
		}
		for(Soldier s: soldiers){
			if(!map.containsEntity(s)){
				toRemove.add(s);
			}
		}
		
		soldiers.removeAll(toRemove);
		
		if(popup.soldierNeeded()){
			time ++;
			if(time > 180){
				time = 0;
				Soldier s = popup.getSoldier();
				map.addEntity(s);
				soldiers.add(s);
				PlayerEntity p = players.get(0);
				map.removeEntity(p);
				players.remove(p);
			}
		}
		popup.updateBarracks();
	}

	public int getMaxHealth() {
		return 400;
	}

	public String getName() {
		return "Barracks";
	}

	public HashMap<String, Integer> getCosts() {
		HashMap<String, Integer> costs = new HashMap<String, Integer>();
		costs.put("gold", 5);
		costs.put("wood", 20);
		return costs;
	}

	public String getExtraOne() {
		return "0";
	}
	
	public PlayerEntity getPupil(){
		return soldiers.get(0);
	}

}
