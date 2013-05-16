package nl.wouter.Tribe.menubar;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import nl.wouter.Tribe.rest.RTSFont;
import nl.wouter.Tribe.screen.Screen;

public abstract class MenubarTextField extends Button{

	String text = "";

	public MenubarTextField(MenuBar bar) {
		super(null, bar);
	}
	
	public void onLeftClick() {}
	
	public void update(){
		this.text = getText();
	}
	
	public abstract String getText();
	
	public void render(SpriteBatch batch, int xPos, int yPos){
		batch.setColor(Color.WHITE);
		Screen.font.drawLine(batch, text, xPos + 1, yPos + (bar.HEIGHT_BUTTON - RTSFont.HEIGHT) / 2);
	}
	
}
