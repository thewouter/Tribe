package nl.wouter.Tribe.map.entities;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import nl.wouter.Tribe.InputHandler;
import nl.wouter.Tribe.RTSComponent;
import nl.wouter.Tribe.map.Direction;
import nl.wouter.Tribe.map.Map;
import nl.wouter.Tribe.map.structures.BasicStructure;
import nl.wouter.Tribe.map.tiles.Tile;
import nl.wouter.Tribe.multiplayer.host.MPMapHost;
import nl.wouter.Tribe.multiplayer.host.Player;
import nl.wouter.Tribe.rest.Oval;
import nl.wouter.Tribe.screen.GameScreen;
import nl.wouter.Tribe.screen.MPGameScreen;

public abstract class Entity implements Cloneable {
	public final Map map;
	private boolean removed;
	public int xPos;
	public int yPos;
	protected int health;
	public int ID;
	public GameScreen screen;
	public int uniqueNumber;
	public boolean isOwnedByPlayer = true;
	public Player owner = null;
	private Oval selectedOval;
	
	public static int ENTITY_ID_COUNTER = 1;
	
	public Entity(Map map, int xPos, int yPos, int ID, GameScreen screen){
		this.map = map;
		this.xPos = xPos;
		this.yPos = yPos;
		this.ID = ID;
		this.screen = screen;
		health = getMaxHealth();
		uniqueNumber = ENTITY_ID_COUNTER;
		Entity.ENTITY_ID_COUNTER = ENTITY_ID_COUNTER + 1;
		selectedOval = new Oval(getScreenX(), getScreenY(), Tile.getWidth() * RTSComponent.SCALE + getScreenX(), Tile.getHeight() * RTSComponent.SCALE + getScreenY());
	}
	public void setOwned(Boolean b){
		isOwnedByPlayer = b;
	}
	
	public void setID(int ID){
		this.ID = ID;
	}
	
	public abstract void update();
	public abstract void render(SpriteBatch batch);
	public abstract int getMaxHealth();
	public abstract String getName();
	public abstract HashMap<String, Integer> getCosts();
	public abstract int getHeadSpace();
	
	public void dispose(){
		
	}
	
	public void renderSelected(SpriteBatch batch){
		if(isOwnedByPlayer) batch.setColor(screen.getColor());
		else batch.setColor(Color.GREEN);
		selectedOval.setPosition(getScreenX(), getScreenY());
		selectedOval.render(batch);
		render(batch);
	}
	
	
	/**
	 * @return extra information for save and MP
	 */
	public abstract String getExtraOne();
	
	/**
	 * @param entityClicked Entity that is right-clicked
	 * @param screen
	 * @param input
	 * @return whether this Entity can move after onRightClick()
	 */
	public boolean onRightClick(Entity entityClicked, GameScreen screen, InputHandler input){
		return true;
	}
	public int getHealth(){
		return health;
	}
	
	public void damage(int damage){
		if(screen instanceof MPGameScreen){
			((MPGameScreen) screen).damageEntity(this, damage);
			return;
		}
		if(map instanceof MPMapHost){
			((MPMapHost)map).host.entityDamaged(this, damage);
		}
		
		health -= damage;
		if(health <=0){
			map.removeEntity(this);
			onDestroying();
		}
	}
	
	public void onDestroying() {}
	
	public int getxPos(){
		return xPos;
	}
	
	public int getyPos(){
		return yPos;
	}
	
	public int getScreenX(){
		return (xPos - yPos) * (-Tile.WIDTH / 2) + map.translationX;
	}
	
	public int getScreenY(){
		return (xPos + yPos) * (Tile.HEIGHT / 2) + map.translationY;
	}
	
	public boolean isSolid(int x, int y){
		return false;
	}
	
	public boolean isRemoved(){
		if(health <= 0) return true;
		return removed;
	}
	
	public void remove(){
		removed = true;
	}
	
	public Entity clone() {
        try {
            final Entity result = (Entity) super.clone();
            return result;
        } catch (final CloneNotSupportedException ex) {
            throw new AssertionError();
        }
	}

	public void damageFromHost(int damage) {
		health -= damage;
		if(health <=0) map.removeEntity(this);
	}
	
	public boolean isOwnedByPlayer(){
		return isOwnedByPlayer;
	}
	
	public String getHealthInString() {
		return health + "";
	}
	
	public boolean isWalkable(int x, int y, Entity toPass) {
		return false;
	}
	
	public void setHealth(int health){
		this.health = health;
	}
	
	public String getData(){
		int frontData = 0;
		if(this instanceof BasicStructure){
			frontData = (((BasicStructure)this).getFront() == Direction.SOUTH_EAST)?1:2;
		}
		String data = this.ID + " " + this.xPos + " " + this.yPos + " " + this.getHealth() + " " + this.getExtraOne() + " " + this.uniqueNumber + " " + frontData;
		return data;
	}
}
