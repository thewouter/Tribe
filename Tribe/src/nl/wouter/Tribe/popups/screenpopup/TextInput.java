package nl.wouter.Tribe.popups.screenpopup;

import java.util.LinkedList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import nl.wouter.Tribe.InputHandler;
import nl.wouter.Tribe.InputHandler.Key;
import nl.wouter.Tribe.rest.RTSFont;
import nl.wouter.Tribe.rest.Util;
import nl.wouter.Tribe.screen.Screen;

public class TextInput extends ScreenPopupPart {
	private LinkedList<String> text = new LinkedList<String>();
	private static int EMPTY_SPACE = 5;
	public boolean isActive = true;
	
	public TextInput(ScreenPopup owner,InputHandler input){
		height = RTSFont.HEIGHT + 2 * EMPTY_SPACE;
		this.owner = owner;
		this.input = input;
	}
	

	public void render(SpriteBatch batch) {
		Util.fillRect(batch, xPos, yPos, width, height, Color.BLACK);
		Util.drawRect(batch, xPos, yPos, width, height, Color.GRAY);
		
		Screen.font.drawLine(batch, getOutput()  + (isActive ? "_" : ""), xPos + EMPTY_SPACE, yPos + EMPTY_SPACE, Color.GRAY);
	}
	
	public void update(int xPos,int yPos, int width) {
		if(isActive){
			for(Key a:input.inputKeys){
				if(a.isTapped()){
					text.add(a.getChars());
				}
			}
			if(input.backspace.isTapped() && !text.isEmpty()){
				text.removeLast();
			}
		}
		
		if(input.LMBTapped()){
			if(isInBox(input.mouseX, input.mouseY)){
				isActive = true;
			}else{
				isActive = false;
			}
		}
		
		this.xPos = xPos;
		this.yPos = yPos;
		this.width = width;
	}
	
	public boolean isInBox(int x, int y){
		if(x > xPos + EMPTY_SPACE && x < xPos +EMPTY_SPACE + width && y > yPos + EMPTY_SPACE && y < yPos + EMPTY_SPACE + height) return true;
		return false;
	}
	
	public String getOutput(){
		String uitput = "";
		for(String s:text){
			uitput += s;
		}
		uitput += "";
		return uitput;
	}
	
	public void clear(){
		text.clear();
	}
	
	public void setText(String text){
		char[] tekst = text.toCharArray();
		for(char c: tekst) this.text.add(Character.toString(c));
	}
}
