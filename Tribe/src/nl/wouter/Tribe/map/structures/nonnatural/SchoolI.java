package nl.wouter.Tribe.map.structures.nonnatural;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.graphics.Texture;

import nl.wouter.Tribe.Images;
import nl.wouter.Tribe.InputHandler;
import nl.wouter.Tribe.map.Direction;
import nl.wouter.Tribe.map.Map;
import nl.wouter.Tribe.map.entities.Entity;
import nl.wouter.Tribe.map.entities.players.PlayerEntity;
import nl.wouter.Tribe.map.structures.BasicStructure;
import nl.wouter.Tribe.multiplayer.host.MPMapHost;
import nl.wouter.Tribe.popups.screenpopup.SchoolPopup;
import nl.wouter.Tribe.screen.GameScreen;

public class SchoolI extends BasicStructure {
	
	private Texture image = Images.school.getTexture();
	public static int ID = 210;
	public boolean isSelected = false;
	public ArrayList<PlayerEntity> playersCollected = new ArrayList<PlayerEntity>();
	public SchoolPopup popup;
	
	
	public SchoolI(Map map, GameScreen screen, int xPos, int yPos, Direction front) {
		super(map, screen, xPos, yPos, 0, 12, ID, front);
		if(map instanceof MPMapHost){
			return;
		}
		int level = screen.level;
		popup = new SchoolPopup(screen, image, this, screen.input);
		if(level <= 0) return;
		popup.hunter.activate();
		popup.lumberJacker.activate();
		if(level <= 1) return;
		popup.minerI.activate();
		popup.founder.activate();
	}

	public SchoolI(Map map, GameScreen screen, int xPos, int yPos, int health, Direction front) {
		super(map, screen, xPos, yPos, 0, 12, ID, front);
		if(map instanceof MPMapHost){
			return;
		}
		int level = screen.level;
		popup = new SchoolPopup(screen, image, this, screen.input);
		this.health = health;
		if(level <= 0) return;
		popup.hunter.activate();
		popup.lumberJacker.activate();
		if(level <= 1) return;
		popup.minerI.activate();
		popup.founder.activate();
		
	}

	public int getHeadSpace() {
		return 2;
	}
	
	public boolean onRightClick(Entity entityClicked, GameScreen screen, InputHandler input){
		screen.setPopup(popup);
		return false;
	}

	public int getSize() {
		return 2;
	}

	public void update() {
		for(int x = -1; x < getSize() + 1; x++){
			for(int y = -1; y < getSize() + 1; y++){
				Entity e = map.getEntity(xPos + x, yPos + y);
				if(e != null && e instanceof PlayerEntity && ((PlayerEntity)e).getProfession() == null){	//it's a player, no profession.
					playersCollected.add((PlayerEntity) e);
					map.removeEntityFromMap(e);
				}
			}
		}
		if(!(map instanceof MPMapHost)) popup.updateSchool();
	}

	public int getMaxHealth() {
		return 567;
	}

	public String getName() {
		return "Elementary school";
	}

	public HashMap<String, Integer> getCosts() {
		HashMap<String, Integer> costs = new HashMap<String, Integer>();
		costs.put("gold",10);
		costs.put("wood", 10);
		return costs;
	}

	public String getExtraOne() {
		return "0";
	}
	
	public void releasePupil(PlayerEntity pupil){
		pupil.entityGoal = null;
		map.setEntityBackOnMap(pupil);
	}
	
	public PlayerEntity getPupil(){
		return playersCollected.get(0);
	}
}
