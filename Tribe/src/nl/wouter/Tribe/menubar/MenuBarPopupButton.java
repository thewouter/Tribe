package nl.wouter.Tribe.menubar;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import nl.wouter.Tribe.screen.GameScreen;
import nl.wouter.Tribe.screen.Screen;

public abstract class MenuBarPopupButton {
	Texture image;
	
	public MenuBarPopupButton(Texture i){
		image = i;
	}
	
	public abstract void onLeftClick(GameScreen screen);
	
	public abstract String getName();
	
	public void render(SpriteBatch batch, int xPos, int yPos){
		batch.draw(image,xPos,yPos);
	}
	
	public void renderHoverOver(SpriteBatch batch, int xPos, int yPos){
		Screen.font.drawLine(batch, getName(), xPos, yPos - image.getHeight(), Color.BLACK);
	}
}
