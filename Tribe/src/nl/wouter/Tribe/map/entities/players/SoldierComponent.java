package nl.wouter.Tribe.map.entities.players;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import nl.wouter.Tribe.map.entities.Entity;

public abstract class SoldierComponent {
	
	public Soldier owner;
	public int ID;
	
	public SoldierComponent(Soldier owner, int ID) {
		this.owner = owner;
		this.ID = ID;
	}
	
	public abstract void render(SpriteBatch batch);
	public abstract void update();
	public abstract void activate();
	public abstract void renderSelected(SpriteBatch batch);
	public abstract void activate(Entity target);
}
