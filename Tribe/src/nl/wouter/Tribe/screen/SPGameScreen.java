package nl.wouter.Tribe.screen;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import nl.wouter.Tribe.Images;
import nl.wouter.Tribe.InputHandler;
import nl.wouter.Tribe.RTSComponent;
import nl.wouter.Tribe.map.Direction;
import nl.wouter.Tribe.map.Map;
import nl.wouter.Tribe.map.Save;
import nl.wouter.Tribe.map.entities.Entity;
import nl.wouter.Tribe.map.entities.MovingEntity;
import nl.wouter.Tribe.map.entities.players.PlayerEntity;
import nl.wouter.Tribe.map.entities.players.professions.Founder;
import nl.wouter.Tribe.map.structures.Structure;
import nl.wouter.Tribe.map.structures.nonnatural.IronSmelter;
import nl.wouter.Tribe.menubar.Button;
import nl.wouter.Tribe.rest.Sound;
import nl.wouter.Tribe.rest.Util;

public class SPGameScreen extends GameScreen {
	
	private boolean isReady = false;
	private Button levelUpButton;
	
	public SPGameScreen(RTSComponent component, InputHandler input){
		super(component, input);
		
		setMap(new Map(256, this));
		
		
		levelUpButton = new Button(Images.buttons[2][1], statusBar) {
			public void onLeftClick() {
				levelUp();
			}
		};
		
		for(int y = 10; y < getMap().getLength(); y++){
			if(!getMap().getTile(10, y).isSolid()){
				PlayerEntity p = new PlayerEntity(getMap(), this, 11, y, null);
				p.setProfession(new Founder(p));
				PlayerEntity n = new PlayerEntity(getMap(), this, 10, y, null);
				p.setProfession(new Founder(p));
				getMap().addEntity(n);
				getMap().addEntity(p);
				selectedEntities.add(p);
				getMap().addEntity(new IronSmelter(getMap(), this, 10, y + 10, Direction.WEST));
				getMap().handleEntityMutations();
				getMap().sortEntitiesForRendering();
				break;
			}
		}
	}

	public void save(){
		Save.save(getMap(), "save1"); 
	}
	
	public void save(String fileName){
		Save.save(getMap(), fileName);
	}
	
	public void load(){
		load("save1");
	}
	
	public void load(String nameFile){
		Map map = Save.load(nameFile,this);
		if(map != null)this.setMap(map);
	}
	
	public void update(){
		if(!pause){
			if(getPointer() != null){
				getPointer().update(); 
				if(bar.showPopup == false){
					setPointer(null);
				}
			}
	
			getMap().update(getWidth(), getHeight());
			
			bar.update(getWidth(), getHeight());
			
			statusBar.update(getWidth(), getHeight());
		
			if(input.space.isPressed() && selectedEntities != null && !selectedEntities.isEmpty()) targetEntity = selectedEntities.getFirst();
		
			if(targetEntity != null){
				int dx = targetEntity.getScreenX() - getWidth() / 2;
				int dy = targetEntity.getScreenY() - getHeight() / 2 ;
				
				getMap().translate(-dx / 10, -dy / 10);
			
				if(Util.abs(dx) < 10 && Util.abs(dy) < 10) targetEntity = null;
			}
		
			if(input.LMBTapped()){
				if(entityPopup != null){
					entityPopup.onLeftClick(input.getMouseX(), input.getMouseY());
				}else selectedEntities.clear();
				
				if(bar.isInBar(input.getMouseX(), input.getMouseY())){
					
				}else if(entityPopup != null && !entityPopup.isInPopup(input.getMouseX(), input.getMouseY()) ){
					selectedEntities.clear();
					selectedEntities.addAll(getMap().getEntities(getMapX(), getMapY()));
				}else if( entityPopup == null ){
					selectedEntities.addAll(getMap().getEntities(getMapX(), getMapY()));
				}
			}
			
			if(input.wasDragging() && (popup == null || !popup.isInPopup(input.mouseX, input.mouseY)) && entityPopup == null){
				int x1 = input.mouseXOnClick, y1 = input.mouseYOnClick, x2 = input.mouseX, y2 = input.mouseY;
				selectedEntities.clear();
				LinkedList<Entity> inRange = (getMap().getEntities(x1, y1 , x2, y2));
				ArrayList<Entity> structures = new ArrayList<Entity>();
				for( Entity e:inRange){
					if(e instanceof MovingEntity){
						selectedEntities.add(e);
					}else if(e instanceof Structure){
						structures.add(e);
					}
				}
				if(selectedEntities.size() == 0){
					selectedEntities.addAll(structures);
				}
			}
			
			if(entityPopup != null){
				entityPopup.update(input.getMouseX(), input.getMouseY());
				if(!selectedEntities.contains(entityPopup.getOwner())) entityPopup = null;
			}
			
			if(input.RMBTapped()){
				ArrayList<MovingEntity> entitiesToMove = new ArrayList<>();
				ArrayList<Entity> rightClicked = getMap().getEntities(getMapX(), getMapY()); //the Entity that is right clicked, if any
				
				boolean canMove = true;
				
				if(!rightClicked.isEmpty() && !selectedEntities.isEmpty()) {
					canMove = selectedEntities.getFirst().onRightClick(rightClicked.get(0), this, input);
				}
				
				if(canMove){
					for(Entity e:selectedEntities){
						if(!selectedEntities.isEmpty() && e instanceof MovingEntity){
							if(((MovingEntity)e).isMovable()){
								entitiesToMove.add((MovingEntity) e);
								entityPopup = null;
							}
						}
					}
				}
				int closestSquare;
				for(int i = 1;;i++){
					if(i*i >= entitiesToMove.size()){
						closestSquare = i;
						break;
					}
				}
				int half = closestSquare / 2;
				int entityCounter = 0;
				int xPos = getMapX() - half;
				int yPos = getMapY() - half;
				for(int y = 0; y < closestSquare; y++){
					for(int x = 0; x < closestSquare && entityCounter < entitiesToMove.size(); x++, entityCounter++){
						entitiesToMove.get(entityCounter).moveTo(new Point(xPos + x, yPos + y));
					}
				}
			}
			LinkedList<Entity> remove = new LinkedList<Entity>();
			for(Entity e: selectedEntities){
				if(!getMap().getEntities().contains(e)) remove.add(e);
			}
			
			
			selectedEntities.removeAll(remove);
			
			
			if(!isReady && isReadyForLevelUp()){
				statusBar.addButton(levelUpButton, 0);
				isReady = true;
			}
			
		}
		if (input.p.isTapped()&& popup == null){
			if(!pause)pause();
			else dePause();
		}
		if(input.escape.isTapped()){
			if(popup == null)component.setTitleScreen();
			else setPopup(null);
		}
		if(popup != null){
			popup.update(input.getMouseX(), input.getMouseY());
		}
		if(input.LMBTapped() || input.RMBTapped()) {
			new Sound("/res/Sounds/klick.mp3").play();
		}
	}
	
	public void levelUp(){
		super.levelUp();
		if(!isReadyForLevelUp() && level != 1) {
			statusBar.removeButton(levelUpButton);
			isReady = false;
		}
	}
	
	public boolean isReadyForLevelUp(){
		switch (level) {
		case 1:
			if(inventory.getGold() >= 25 && inventory.getMeat() >= 16)return true;
		case 2:
			if(inventory.getGold() >= 100 && inventory.getMeat() >=  60 && inventory.getWood() >= 25) return true;
		}
		return false;
	}
}
