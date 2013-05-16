package nl.wouter.Tribe.popups.entitypopup;

import java.awt.Graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import nl.wouter.Tribe.map.entities.Entity;
import nl.wouter.Tribe.rest.RTSFont;
import nl.wouter.Tribe.screen.GameScreen;
import nl.wouter.Tribe.screen.Screen;

public class EntityTextPopup extends EntityPopup {
	private final String[] text;
	private int width, height;
	private int screenX;
	private int screenY;

	public EntityTextPopup(Entity owner, GameScreen screen, String...text){
		super(owner,screen );
		this.text = text;
		
		for(String line: text){
			int lineWidth = Screen.font.getLineWidth(line);
			
			if(lineWidth > width) width = lineWidth;
		}
		
		height = RTSFont.HEIGHT * text.length + EMPTY_SPACE;
		width += EMPTY_SPACE;
	}
	
	public void render(SpriteBatch batch){
		drawBox(batch, width, height, screenX, screenY);
		
		batch.setColor(Color.BLACK);
		
		for(int i = 0; i < text.length; i++){
			//g.drawString(text[i], getscreenX() + 16, getScreenY() + 16 + i * RTSFont.HEIGHT);
			Screen.font.drawLine(batch, text[i], screenX + EMPTY_SPACE / 2, screenY + EMPTY_SPACE / 2 + i * RTSFont.HEIGHT);
		}
	}
	
	public void update(int mouseX, int mouseY){
		screenX = owner.getScreenX() + ((GameScreen)getScreen()).getMap().translationX;
		screenY = owner.getScreenY() + ((GameScreen)getScreen()).getMap().translationY;
		
	}

	public void onLeftClick(int mouseX, int mouseY) {}
	
	public boolean isInPopup(int x , int y){
		return false;
	}
}
