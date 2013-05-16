package nl.wouter.Tribe.map.entities.players;

import java.awt.Graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import nl.wouter.Tribe.Images;
import nl.wouter.Tribe.map.entities.Entity;

public class Shield extends SoldierComponent {
	private int protection = 0;
	private int health = 0;
	public static int ID = 601;
	private Texture image;
	/**
	 * @param owner
	 * @param protection Number between 0 - 100, 0 is no protection , 100 is full
	 */
	public Shield(Soldier owner, int protection, int health) {
		super(owner, ID);
		this.protection = protection;
		this.health = health;
		image = Images.smallButtons[0][0].getTexture();
		
	}

	public void render(SpriteBatch batch) {
		batch.draw(image,owner.getScreenX(), owner.getScreenY());
	}
	
	public void renderSelected(SpriteBatch batch){
		render(batch);
	}

	public void update() {
		
	}
	
	public void damage(int damage){
		health -= damage;
		if(health <= 0){
			owner.removeSoldierComponent(this);
		}
	}

	public void activate() {}

	public int getProtection() {
		return protection;
	}

	public void activate(Entity target) {
	}
}
