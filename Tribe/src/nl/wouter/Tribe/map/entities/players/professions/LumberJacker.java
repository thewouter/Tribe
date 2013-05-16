package nl.wouter.Tribe.map.entities.players.professions;

import nl.wouter.Tribe.InputHandler;
import nl.wouter.Tribe.map.entities.Entity;
import nl.wouter.Tribe.map.entities.players.PlayerEntity;
import nl.wouter.Tribe.map.structures.natural.TreeStructure;
import nl.wouter.Tribe.popups.entitypopup.EntityOptionsPopup;
import nl.wouter.Tribe.popups.entitypopup.Option;
import nl.wouter.Tribe.rest.Util;
import nl.wouter.Tribe.screen.GameScreen;
import nl.wouter.Tribe.screen.MPGameScreen;

public class LumberJacker extends Profession {

	private static final int TIME_TO_CHOP_ONE_DAMAGE = 120, ID = 400;
	private boolean isChopping;
	private int teller;
	private TreeStructure closestTree;

	public LumberJacker(PlayerEntity owner) {
		super(owner, ID);
	}

	public void update() {
		if(isChopping){
			teller++;
			if(teller >= TIME_TO_CHOP_ONE_DAMAGE){
				teller = 0;
				if(isChopping){ 
					if(Util.getDistance(owner.xPos, owner.yPos, closestTree.xPos, closestTree.yPos)  <= 1){
						closestTree.damage(1);
						owner.screen.inventory.addWood(1);
					}
				}
			}
			if(!owner.isMoving() && (closestTree  == null || !owner.map.getEntities().contains(closestTree))){
				moveToNearestTree();
			}
		}
	}
	
	public void moveToNearestTree(){
		isChopping = true;
		closestTree = (TreeStructure) owner.map.getClosestTree(owner.getxPos(), owner.getyPos());
		owner.moveTo(closestTree);
	}

	public boolean onRightClick(Entity entityClicked, GameScreen screen, InputHandler input) {
		if(entityClicked != owner) return false;
		EntityOptionsPopup popup = new EntityOptionsPopup(owner, screen);
		if(!isChopping){
			popup.addOption(new Option("start chopping", popup) {
				public void onClick() {
					if(((GameScreen)owner.getScreen()) instanceof MPGameScreen){
						((MPGameScreen)owner.owner.screen).startChopping(owner.owner);
						return;
					}
					isChopping = true;
					((GameScreen)this.owner.getScreen()).setEntityPopup(null);
				}
			});
		}else{
			popup.addOption(new Option("stop chopping", popup) {
				public void onClick() {
					if(((GameScreen)owner.getScreen()) instanceof MPGameScreen){
						((MPGameScreen)owner.owner.screen).stopChopping(owner.owner);
						return;
					}
					isChopping = false;
					((GameScreen)this.owner.getScreen()).setEntityPopup(null);
				}
			});
		}
		screen.setEntityPopup(popup);
		return true;
	}

	public String getName() {
		return "the LumberJacker";
	}
	
	public boolean getIsChopping(){
		return isChopping;
	}
	
	public void setIsChopping(boolean isChopping){
		this.isChopping = isChopping;
	}

}
