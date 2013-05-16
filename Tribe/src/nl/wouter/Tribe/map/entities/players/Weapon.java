package nl.wouter.Tribe.map.entities.players;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import nl.wouter.Tribe.map.entities.Entity;
import nl.wouter.Tribe.rest.Util;

public abstract class Weapon extends SoldierComponent {
	boolean isFighting = false;
	public int MIN_HIT_RANGE = 0, MAX_HIT_RANGE = 1, LOAD_TIME = 0; //standard.. r
	int ticksCounter = 0;

	public Weapon(Soldier owner, int ID) {
		super(owner, ID);
		for(SoldierComponent c: owner.getComponents()){
			if(c instanceof Weapon){
				owner.comp.remove(c);
			}
		}
	}

	public void render(SpriteBatch batch) {}
	public void renderSelected(SpriteBatch batch){}

	public void update(){
		if(isFighting){
			ticksCounter++;
			if(ticksCounter >= LOAD_TIME && owner.target != null && Util.getDistance(owner.target, owner) <= MIN_HIT_RANGE){
				owner.standStill();
				activate();
				ticksCounter = 0;
			}else if(owner.target == null){
				isFighting = false;
			}else if(Util.getDistance(owner.target, owner) > MIN_HIT_RANGE){
				owner.follow(owner.target, 1);
			}
		}
	}

	public abstract void activate();
	
	public void activate(Entity target){
		isFighting = true;
		System.out.println("activate");
	}
	
	public void setTarget(Entity t){
		isFighting = true;
	}
}
