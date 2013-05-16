package nl.wouter.Tribe.popups.entitypopup;

import java.awt.Graphics;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import nl.wouter.Tribe.map.entities.Entity;
import nl.wouter.Tribe.popups.Popup;
import nl.wouter.Tribe.screen.GameScreen;

public abstract class EntityPopup extends Popup{
	public static final int EMPTY_SPACE = 32;
	public final Entity owner;
	
	/**
	 * @param owner The owner of this PopUp
	 */
	public EntityPopup(Entity owner, GameScreen screen){
		super(screen);
		this.owner = owner;
	}
	
	public abstract void render(SpriteBatch batch);
	public abstract void update(int mouseX, int mouseY);
	public abstract void onLeftClick(int mouseX, int mouseY);
	public abstract boolean isInPopup(int x, int y);
	
	/**
	 * @param g
	 * @param width width in pixels
	 * @param height height in pixels
	 * @param screenX
	 * @param screenY
	 */
	
	
	protected void drawBox(SpriteBatch batch, int width, int height){
		drawBox(batch, width, height, getScreenX(), getScreenY());
	}
	
	public Entity getOwner(){
		return owner;
	}
	
	protected int getScreenX(){
		return owner.getScreenX();
	}
	
	protected int getScreenY(){
		return owner.getScreenY();
	}
}
