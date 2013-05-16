package nl.wouter.Tribe.map.entities.players;

import java.awt.Graphics;
import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import nl.wouter.Tribe.InputHandler;
import nl.wouter.Tribe.map.Map;
import nl.wouter.Tribe.map.entities.Entity;
import nl.wouter.Tribe.map.entities.MovingEntity;
import nl.wouter.Tribe.map.structures.Structure;
import nl.wouter.Tribe.screen.GameScreen;

public class Soldier extends PlayerEntity {
	ArrayList<SoldierComponent> comp = new ArrayList<SoldierComponent>();
	Entity target = null;
	Weapon weapon = null;
	public int shieldProtection;
	ArrayList<SoldierComponent> toRemove = new ArrayList<SoldierComponent>();
	private double progressToNextDamage = 0.0;
	private static int ID = 107;

	public Soldier(Map map, GameScreen screen, int xPos, int yPos, Structure tent) {
		super(map, screen, xPos, yPos, ID, tent);
	}

	public Soldier(Map map, GameScreen screen, int xPos, int yPos, Structure tent, int health) {
		super(map, screen, xPos, yPos, ID, tent, health);
	}
	
	public void addSoldierComponent(SoldierComponent comp){
		SoldierComponent remove = null;
		if(comp instanceof Weapon){
			weapon = (Weapon) comp;
			for(SoldierComponent c : this.comp){
				if(c instanceof Weapon){
					remove = c;
				}
			}
		}
		if(comp instanceof Shield){
			for(SoldierComponent c : this.comp){
				if(c instanceof Shield){
					remove = c;
				}
			}
			shieldProtection = ((Shield)comp).getProtection();
		}
		this.comp.remove(remove);
		this.comp.add(comp);
	}
	
	public void removeSoldierComponent(SoldierComponent comp){
		toRemove.add(comp);
		if(comp instanceof Shield){
			shieldProtection = 0;
		}
	}
	
	public void render(SpriteBatch batch){
		super.render(batch);
		for(SoldierComponent c:comp){
			c.render(batch);
		}
	}
	
	public void renderSelected(SpriteBatch batch){
		for(SoldierComponent c:comp){
			c.render(batch);
		}
		super.renderSelected(batch);
	}
	
	public synchronized void update(){
		super.update();
		for(SoldierComponent c:comp){c.update();}
		if(!map.getEntities().contains(target)) target = null;
		comp.removeAll(toRemove);
	}
	
	public void activate(Entity target){
		this.target = target;
		for(SoldierComponent c: comp){
			c.activate(target);
		}
		follow(target, 0);
	}
	
	public String getName(){
		return name + " the Soldier";
	}
	
	public boolean onRightClick(Entity entityClicked, GameScreen screen, InputHandler input){
		if(entityClicked == null) activate(null);
		if(entityClicked != this && entityClicked instanceof MovingEntity){ // attack it!!!
			activate(entityClicked);
			return false;
		}else if(entityClicked != this){
			moveTo(entityClicked);
			return false;
		}
		return false;
	}
	
	public String getHealthInString(){
		if(shieldProtection != 0) return health + ",  " + shieldProtection + " protection";
		return health + ""; 
	}
	
	public void damage(int damage){
		double damages = (double) damage;
		progressToNextDamage += damages * ((100.0 - shieldProtection * 1.0) / 100.0) * 1.0;
		if(progressToNextDamage >= 1){
			super.damage((int) Math.floor(progressToNextDamage));
			progressToNextDamage -= (int) Math.floor(progressToNextDamage);
		}
	}

	public synchronized ArrayList<SoldierComponent> getComponents() {
		return comp;
	}

	public Weapon getWeapon() {
		return weapon;
	}

	public String getExtraOne(){
		String result = "" + comp.size();
		for(SoldierComponent c: comp){
			result = result + " " + c.ID;
		}
		return result;
	}
}
