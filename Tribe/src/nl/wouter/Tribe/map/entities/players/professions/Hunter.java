package nl.wouter.Tribe.map.entities.players.professions;

import java.util.LinkedList;

import nl.wouter.Tribe.InputHandler;
import nl.wouter.Tribe.map.entities.Entity;
import nl.wouter.Tribe.map.entities.MovingEntity;
import nl.wouter.Tribe.map.entities.players.PlayerEntity;
import nl.wouter.Tribe.popups.entitypopup.EntityOptionsPopup;
import nl.wouter.Tribe.popups.entitypopup.Option;
import nl.wouter.Tribe.rest.Util;
import nl.wouter.Tribe.screen.GameScreen;
import nl.wouter.Tribe.screen.MPGameScreen;

public class Hunter extends Profession {

	private static final int TIME_TO_HUNT_ONE_DAMAGE = 120, ID = 402;
	private boolean isHunting;
	private int teller;
	private MovingEntity closestMovingEntity;

	public Hunter(PlayerEntity owner) {
		super(owner, ID);
	}
	
	public void update() {
		if(isHunting){
			teller++;
		}
		if(isHunting && !owner.isMoving() && (!(owner.map.getEntities().contains(closestMovingEntity)) || closestMovingEntity == null)){
			huntNearestAnimal();
		}
		
		if(isHunting){
			if(teller >= TIME_TO_HUNT_ONE_DAMAGE){						// hunting time !
				teller = 0;
				if(Util.getDistance(owner.xPos, owner.yPos, closestMovingEntity.xPos, closestMovingEntity.yPos) <= 1){
					closestMovingEntity.damage(1);
					owner.screen.inventory.addMeat(1);
					if(!owner.map.getEntities().contains(closestMovingEntity)){	// it's dead.
						huntNearestAnimal();
					}
				}
			}
		}
	}
	
	
	public void huntNearestAnimal(){
		isHunting = true;
		LinkedList<MovingEntity> notIncluded = new LinkedList<MovingEntity>();
		while(true){
			closestMovingEntity = (MovingEntity) owner.map.getClosestMovingEntity(owner.getxPos(), owner.getyPos(), notIncluded);
			if(!(closestMovingEntity instanceof PlayerEntity)){
				break;
			}
			notIncluded.add(closestMovingEntity);
		}
		owner.follow(closestMovingEntity, 1);
	}
	
	public boolean onRightClick(Entity entityClicked, GameScreen screen, InputHandler input){
		if(entityClicked != owner) return false;
		EntityOptionsPopup popup = new EntityOptionsPopup(owner, screen);
		if(isHunting){
			popup.addOption(new Option("stop hunting", popup) {
				public void onClick() {
					if(((GameScreen)owner.getScreen()) instanceof MPGameScreen){
						((MPGameScreen)owner.owner.screen).startHunting(owner.owner);
						return;
					}
					isHunting = false;
					((GameScreen)owner.getScreen()).setEntityPopup(null);
					closestMovingEntity = null;
				}
			});
		}else{
			popup.addOption(new Option("start hunting", popup) {
				public void onClick() {
					if(((GameScreen)owner.getScreen()) instanceof MPGameScreen){
						((MPGameScreen)owner.owner.screen).stopHunting(owner.owner);
						return;
					}
					isHunting =  true;
					((GameScreen)owner.getScreen()).setEntityPopup(null);
				}
			});
		}
		screen.setEntityPopup(popup);
		
		return true;
	}
	
	public String getName() {
		return "the Hunter";
	}
	
	public boolean getIsHunting(){
		return isHunting;
	}
	
	public void setIsHunting(boolean isHunting){
		this.isHunting = isHunting;
		if(!isHunting) closestMovingEntity = null;
	}

}
