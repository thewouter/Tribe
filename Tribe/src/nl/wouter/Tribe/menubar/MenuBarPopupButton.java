package nl.wouter.Tribe.menubar;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import nl.wouter.Tribe.screen.GameScreen;
import nl.wouter.Tribe.screen.Screen;

public abstract class MenuBarPopupButton {
	Sprite image;
	
	public MenuBarPopupButton(Sprite i){
		image = i;
	}
	
	public abstract void onLeftClick(GameScreen screen);
	
	public abstract String getName();
	
	public void render(SpriteBatch batch, int xPos, int yPos){
		image.setPosition(xPos, yPos);
		image.draw(batch);
	}
	
	public void renderHoverOver(SpriteBatch batch, int xPos, int yPos){
		Screen.font.drawLine(batch, getName(), xPos, (int)( yPos - image.getHeight()), Color.BLACK);
	}
}
