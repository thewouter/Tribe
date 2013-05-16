package nl.wouter.Tribe.popups.entitypopup;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import nl.wouter.Tribe.rest.RTSFont;
import nl.wouter.Tribe.screen.Screen;

public abstract class Option {
	public String name;
	public EntityOptionsPopup owner;
	
	
	public Option(String name, EntityOptionsPopup owner){
		this.name = name;
		this.owner = owner;
	}
	
	public String getName(){
		return name;
	}
	
	public void render(SpriteBatch batch, int index){
		Screen.font.drawLine(batch, name, owner.screenX + 16, owner.screenY + 16 + index * RTSFont.HEIGHT);
	}
	
	public void renderInColor(SpriteBatch batch, int index, Color c){
		Screen.font.drawLineAndShadow(batch, name, owner.screenX + 16, owner.screenY + 16 + index * RTSFont.HEIGHT, c);
	}
	
	public EntityOptionsPopup getPopup(){
		return owner;
	}
	
	public abstract void onClick();
}
