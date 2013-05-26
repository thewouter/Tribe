package nl.wouter.Tribe.map.entities.players.professions;

import nl.wouter.Tribe.InputHandler;
import nl.wouter.Tribe.map.Direction;
import nl.wouter.Tribe.map.MousePointer;
import nl.wouter.Tribe.map.entities.Entity;
import nl.wouter.Tribe.map.entities.players.PlayerEntity;
import nl.wouter.Tribe.map.structures.nonnatural.BaseOfOperations;
import nl.wouter.Tribe.popups.entitypopup.EntityOptionsPopup;
import nl.wouter.Tribe.popups.entitypopup.Option;
import nl.wouter.Tribe.rest.Sound;
import nl.wouter.Tribe.screen.GameScreen;

public class Founder extends Profession {
	public static int ID = 403;

	public Founder(PlayerEntity owner) {
		super(owner, ID);
	}

	public void update() {
		
	}
	
	public boolean onRightClick(Entity entityClicked, GameScreen screen, InputHandler input){
		EntityOptionsPopup popup = new EntityOptionsPopup(owner, screen);
		popup.addOption(new Option("Start a new settlement", popup) {
			public void onClick() {
				((GameScreen)owner.getScreen()).bar.setMenuBarPopup(((GameScreen)owner.getScreen()).bar.buildmenu);
				((GameScreen)owner.getScreen()).bar.showPopup();
				((GameScreen)owner.getScreen()).setMousePointer(new MousePointer(owner.owner.map, owner.owner.screen.input, ((GameScreen)owner.getScreen())) {
					public Entity toBuild(Direction face) {
						return new BaseOfOperations(map, screen, screen.getMapX(), screen.getMapY(), face);
					}
					public void afterBuild(){
						map.removeEntity(owner.owner);
						new Sound("/res/Sounds/Buildbaseofoperations.mp3").play();
						if(screen.level < 1) screen.levelUp();
						owner.owner.screen.setPointer(null);
					}
				});
				((GameScreen)owner.getScreen()).entityPopup = null;
			}
		});
		screen.setEntityPopup(popup);
		return true;
	}

	public String getName() {
		return "the Founder";
	}

}
