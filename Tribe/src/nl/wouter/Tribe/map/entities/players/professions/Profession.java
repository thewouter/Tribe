package nl.wouter.Tribe.map.entities.players.professions;

import nl.wouter.Tribe.InputHandler;
import nl.wouter.Tribe.map.entities.Entity;
import nl.wouter.Tribe.map.entities.players.PlayerEntity;
import nl.wouter.Tribe.rest.Util;
import nl.wouter.Tribe.screen.GameScreen;

public abstract class Profession {
	public PlayerEntity owner;
	public final int ID;

	public Profession(PlayerEntity owner, int ID) {
		this.owner = owner;
		this.ID = ID;
	}
	
	/**
	 * @param entityClicked
	 * @param screen
	 * @param input
	 * @return if it does something
	 */
	public boolean onRightClick(Entity entityClicked, GameScreen screen, InputHandler input){
		return false;
	}
	
	public static void setProfession(int ID, PlayerEntity p){
		p.setProfession(Util.getProfession(ID, p));
	}
	
	public abstract void update();

	public abstract String getName();
	
	public void walkingCalculated(){}
	
	public void setOwner(PlayerEntity owner){
		this.owner = owner;
	}
}
