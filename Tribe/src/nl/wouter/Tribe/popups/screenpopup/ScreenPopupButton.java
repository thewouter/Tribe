package nl.wouter.Tribe.popups.screenpopup;

import java.io.IOException;

import javax.imageio.ImageIO;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import nl.wouter.Tribe.Images;
import nl.wouter.Tribe.InputHandler;
import nl.wouter.Tribe.rest.Util;
import nl.wouter.Tribe.screen.Screen;

public abstract class ScreenPopupButton extends ScreenPopupPart {
	private Sprite button;
	private String text;
	Boolean isInRect = false;
	
	public ScreenPopupButton(String text, ScreenPopup owner, InputHandler input){
		button = new Sprite(Images.load("Pictures/buttonsmall.png"));
		this.input = input;
		this.text = text;
		this.owner = owner;
		height = (int) button.getHeight();
	}
	
	public void render(SpriteBatch batch) {
		button.draw(batch);
		
		Screen.font.drawLine(batch, text, xPos + 11, yPos + (height / 2),Util.colorFromHex(0xFF6B6B6B));
		Screen.font.drawLine(batch, text, xPos + 10, yPos + (height / 2) + 1,Util.colorFromHex(0xFF6B6B6B));
		
		Screen.font.drawLine(batch, text, xPos + 9, yPos + (height / 2), Util.colorFromHex(0xFF4C4C4C));
		Screen.font.drawLine(batch, text, xPos + 10, yPos + (height / 2) - 1, Util.colorFromHex(0xFF4C4C4C));
		
		Screen.font.drawLine(batch, text, xPos + 10, yPos + (height / 2), Util.colorFromHex(0xFF5B5B5B));
		
		
		if(isInRect){
			Util.fillRect(batch, xPos, yPos, width, height, Util.colorFromHex(0x208080FF));
		}
	}
	
	public abstract void onLeftClick();
	
	public void update(int xPos, int yPos, int width) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.width = (int) button.getWidth();
		if(input.LMBTapped() && isInRect(input.mouseX, input.mouseY)){
			onLeftClick();
		}
		isInRect = isInRect(input.mouseX, input.mouseY);
		button.setPosition(xPos, yPos);
		
	}
	
	private boolean isInRect(int x, int y){
		if(x > xPos && x < xPos + width && y > yPos && y < yPos + height) return true;
		return false;
	}
	
}
