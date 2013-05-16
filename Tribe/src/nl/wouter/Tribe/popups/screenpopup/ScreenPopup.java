package nl.wouter.Tribe.popups.screenpopup;

import java.awt.Graphics;
import java.util.LinkedList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import nl.wouter.Tribe.popups.Popup;
import nl.wouter.Tribe.screen.Screen;

public class ScreenPopup extends Popup{
	protected int xPos,yPos;
	protected int width;
	protected int height;
	private static int EMPTY_SPACE = 10;
	LinkedList<ScreenPopupPart> parts = new LinkedList<ScreenPopupPart>();
	public Screen screen;
	private boolean forced;
	
	public ScreenPopup(int xPos, int yPos, int width, int height, Screen title, boolean forced){
		super(title);
		this.xPos = xPos;
		this.yPos = yPos;
		this.width = width;
		this.height = height;
		this.screen = title;
		this.forced = forced;
	}
	
	public boolean isForced(){
		return forced;
	}

	public void render(SpriteBatch batch) {
		drawBox(batch, width, height, xPos, yPos);
		for(ScreenPopupPart p:parts){
			p.render(batch);
		}
	}

	public void update(int mouseX, int mouseY) {
		if(isInPopup(mouseX, mouseY));
		int totalHeight = 0;
		for(int i = 0; i < parts.size(); i++){
			parts.get(i).update((xPos * 2 + width)/2- (width - 2 * EMPTY_SPACE)/2, yPos + EMPTY_SPACE + i * EMPTY_SPACE + totalHeight, width - 2 * EMPTY_SPACE);
			totalHeight += parts.get(i).height;
		}
		if(this.height < (parts.size() + 1) * EMPTY_SPACE + totalHeight){
			this.height = (parts.size() + 1) * EMPTY_SPACE + totalHeight;
		}
		
	}

	public void onLeftClick(int mouseX, int mouseY) {
		
	}

	public boolean isInPopup(int x, int y) {
		if(x > xPos && x < xPos + width && y > yPos && y < yPos + height){
			return true;
		}
		return false;
	}
	
	public ScreenPopupPart getPart(int index){
		return parts.get(index);
	}
	
	public void addPart(ScreenPopupPart p){
		parts.add(p);
	}
	
	public TextInput getTextInput(int index){
		int counter = 0;
		for(ScreenPopupPart p: parts){
			if(p instanceof TextInput){
				counter++;
				if(index == counter)return (TextInput) p;
			}
		}
		return null;
	}
}
