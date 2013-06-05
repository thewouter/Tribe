package nl.wouter.Tribe.map.entities.players;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import nl.wouter.Tribe.Animation;
import nl.wouter.Tribe.Images;
import nl.wouter.Tribe.InputHandler;
import nl.wouter.Tribe.map.Direction;
import nl.wouter.Tribe.map.Map;
import nl.wouter.Tribe.map.entities.Entity;
import nl.wouter.Tribe.map.entities.ItemEntity;
import nl.wouter.Tribe.map.entities.MovingEntity;
import nl.wouter.Tribe.map.entities.players.professions.LumberJacker;
import nl.wouter.Tribe.map.entities.players.professions.Profession;
import nl.wouter.Tribe.map.structures.BasicStructure;
import nl.wouter.Tribe.map.structures.Structure;
import nl.wouter.Tribe.map.structures.nonnatural.Tent;
import nl.wouter.Tribe.map.tiles.Tile;
import nl.wouter.Tribe.multiplayer.host.MPMapHost;
import nl.wouter.Tribe.popups.entitypopup.EntityOptionsPopup;
import nl.wouter.Tribe.popups.entitypopup.Option;
import nl.wouter.Tribe.rest.Sound;
import nl.wouter.Tribe.rest.Util;
import nl.wouter.Tribe.screen.GameScreen;
import nl.wouter.Tribe.screen.MPGameScreen;

public class PlayerEntity extends MovingEntity {
	public String name;
	private ArrayList<ItemEntity> inventory = new ArrayList<ItemEntity>();
	private int lastSelectedOption = -1;
	InputHandler input;
	protected static int ID = 102;
	private Animation animation;
	private Animation backwardAnimation;
	public final Structure ownerTent;
	private Profession profession;
	
	public PlayerEntity(Map map, GameScreen screen, int xPos, int yPos, Structure tent){
		super(map,screen, xPos, yPos, ID);
		name = Util.NAME_GEN.getRandomName();
		loadAnimation(Images.player);
		ownerTent = tent;
	}
	
	public PlayerEntity(Map map, GameScreen screen, int xPos, int yPos, int ID, Structure tent){
		super(map,screen, xPos, yPos, ID);
		name = Util.NAME_GEN.getRandomName();
		loadAnimation(Images.player);
		ownerTent = tent;
		
	}
	
	public void update(){
		super.update();
		animation.update();
		backwardAnimation.update();
		if(profession != null){
			profession.update();
		}
	}
	
	public PlayerEntity(Map map, GameScreen screen, int xPos, int yPos, Structure tent, int health){
		super(map,screen,xPos,yPos, ID);
		this.health = health;
		name = Util.NAME_GEN.getRandomName();
		ownerTent = tent;
	}
	
	public PlayerEntity(Map map, GameScreen screen, int xPos, int yPos, int ID, Structure tent, int health){
		super(map,screen,xPos,yPos, ID);
		this.health = health;
		name = Util.NAME_GEN.getRandomName();
		ownerTent = tent;
	}
	
	private void loadAnimation(TextureRegion[][] player){
		animation = new Animation(3); 
		for(int i = 0; i < player.length; i++){
			animation.addScene(new Sprite(player[i][0]));
		}
	
		backwardAnimation = new Animation(3);
		for(int i = player.length - 1; i >= 0; i--){
			backwardAnimation.addScene(new Sprite(player[i][0]));
		}
		
	}
	
	public void setNextDirections(LinkedList<Direction> toSet, boolean fromEndPoint){
		super.setNextDirections(toSet, fromEndPoint);
		if(profession != null){
			profession.walkingCalculated();
		}
	}
	
	public void render(SpriteBatch batch){
		if(isMoving() && nextDirections.getFirst().getyOffset() - nextDirections.getFirst().getxOffset() <= 0)batch.draw(animation.getImage(), getScreenX() + (Tile.WIDTH - animation.getImage().getRegionWidth()) / 2, getScreenY() - (animation.getImage().getRegionHeight() - Tile.HEIGHT / 2));
		else if(isMoving())batch.draw(backwardAnimation.getImage(), getScreenX() + (Tile.WIDTH - animation.getImage().getRegionWidth()) / 2, getScreenY() - (animation.getImage().getRegionHeight() - Tile.HEIGHT / 2));
		else batch.draw(animation.getImage(0), getScreenX() + (Tile.WIDTH - animation.getImage().getRegionWidth()) / 2, getScreenY() - (animation.getImage().getRegionHeight() - Tile.HEIGHT / 2));
	}
	
	protected void onStopMoving(){
		Entity e = map.getEntity(xPos, yPos);
		
		if(e instanceof ItemEntity){
			ItemEntity item = (ItemEntity) e;
			inventory.add(item);
			
			map.removeEntity(e);
		}
	}
	
	public int getHeadSpace(){
		return 2;
	}
	
	public boolean onRightClick(Entity entityClicked, GameScreen screen, InputHandler input){
		if(profession != null && (entityClicked == this) && profession.onRightClick(entityClicked, screen, input)) return true;
		if(entityClicked == this){
			EntityOptionsPopup popup = new EntityOptionsPopup(this, screen);
			      
			Option option2 = new Option("lumber", popup){
				public void onClick(){
					((PlayerEntity)owner.owner).setProfession(new LumberJacker((PlayerEntity) owner.owner));
				}
			};
			Option option1 = new Option("add tent",popup) {
				public void onClick() {
					map.addEntity(new Tent(map, ((GameScreen)this.owner.getScreen()), xPos, yPos-2, Direction.SOUTH_WEST, 2));
				}
			};
			Option dig = new Option("dig", popup){
				public void onClick() {
					int ID = map.getTile(xPos, yPos - 1).getID();
					if(ID == 0 || ID == 1) map.changeTile(xPos, yPos - 1, Tile.sand1);
					else if(ID == 17) map.changeTile(xPos, yPos - 1, Tile.water1);
				}
			};
			Option raise = new Option("raise",popup){
				public void onClick() {
					int ID = map.getTile(xPos, yPos - 1).getID();
					if(ID == 2) map.changeTile(xPos, yPos - 1, Tile.sand1);
					else if(ID == 17) map.changeTile(xPos, yPos - 1, Tile.grass1);
				}
			};
			popup.addOption(new Option("shoot", popup){
				public void onClick() {
					new Sound("/res/Sounds/shot.mp3").play();
				}
			});
			
			popup.addOption(new Option("damage", popup) {
				public void onClick() {
					this.owner.owner.damage(1);
				}
			});
			popup.addOption(option1);
			popup.addOption(option2);
			popup.addOption(dig);
			popup.addOption(raise);
			
			screen.setEntityPopup(popup);
		}else if(entityClicked instanceof BasicStructure){
			moveTo(entityClicked);
		}else if(entityClicked instanceof MovingEntity){
			follow(entityClicked, 5);
		}
		return false;
	}
	
	protected double getTravelTime(){
		return 500;
	}
	
	public int getMaxHealth(){
		return 10;
	}
	
	public String getName(){
		if(profession == null) return name;
		return name + " " + profession.getName();
	}
	
	public void setSelectedOption(int index){
		lastSelectedOption = index;
	}
	
	public int getSelectedOption(){
		return lastSelectedOption;
	}

	public String getExtraOne() {
		return 1 + " " + Util.getProfessionID(profession);
	}

	public boolean isMovable() {
		return true;
	}
	
	public void setProfession(Profession p){
		if(screen instanceof MPGameScreen){ // it's the client.
			((MPGameScreen)screen).setProfession(this, p);
			return;
		}else if(map instanceof MPMapHost && map.containsEntity(this)){ // it's the host
			((MPMapHost)map).addProfession(this, p);
		}
		profession = p;
	}
	
	public void setProfessionFromHost(Profession p){
		profession = p;
	}
	
	public Profession getProfession(){
		return profession;
	}
}
