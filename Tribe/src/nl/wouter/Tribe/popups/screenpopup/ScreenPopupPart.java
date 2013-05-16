package nl.wouter.Tribe.popups.screenpopup;

import java.awt.Graphics;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import nl.wouter.Tribe.InputHandler;

public abstract class ScreenPopupPart{
	int xPos, yPos, width, height;
	InputHandler input;
	public ScreenPopup owner;
	
	public abstract void render(SpriteBatch batch);
	public abstract void update(int xPos,int yPos, int width);
	
}
