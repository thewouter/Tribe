package nl.wouter.Tribe.menubar;

import java.awt.Graphics;
import java.util.LinkedList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import nl.wouter.Tribe.popups.Popup;
import nl.wouter.Tribe.screen.GameScreen;

public class MenuBarPopup extends Popup {
	LinkedList<MenuBarPopupButton> buttons = new LinkedList<MenuBarPopupButton>();
	private int xPos, yPos, width, height;
	private boolean allignFromTop;
	/**
	 * @param xPos x position of the button created in this popup
	 * @param yPos y position of the button created in this popup
	 */
	
	public static int WIDTH_BUTTON = 16, HEIGHT_BUTTON = 16, EMPTY_SPACE = 20, BUTTONS_PER_ROW = 5;
	
	public MenuBarPopup(GameScreen screen, int xPos, int yPos, int index, boolean allignFromTop){
		super(screen);
		this.xPos = xPos;
		this.yPos = yPos;
		this.allignFromTop = allignFromTop;
	}
	
	public void render(SpriteBatch batch){
		int screenX = 0;
		int screenY = 0;
		if(!allignFromTop){
			screenX = xPos - width;
			screenY = yPos - height;
		}else{
			screenX = xPos;
			screenY = yPos;
		}
		
		drawBox(batch, width, height, screenX, screenY);
		
		for(int i = 0; i < buttons.size(); i++){
			int x = i % 5;
			int y  = i/5;
			buttons.get(i).render(batch, screenX + EMPTY_SPACE/2 + x * WIDTH_BUTTON, screenY + EMPTY_SPACE/2 + y * HEIGHT_BUTTON);
		}
		
		
	}
	
	public void addButton(MenuBarPopupButton b){
		if(b != null) {buttons.add(b);return;}
		System.out.println("button == null !!");
	}
	
	public void addButton(MenuBarPopupButton b, int index){
		if(b != null) {buttons.add(index, b);return;}
		System.out.println("button == null !!");
	}
	
	public void removeButton(MenuBarPopupButton b){
		buttons.remove(b);
	}
	
	public boolean isInPopup(int x, int y){
		if(!allignFromTop)if(x > xPos - width && x < xPos && y > yPos - height && y < yPos)return true;if(!allignFromTop)if(x > xPos - width && x < xPos && y > yPos - height && y < yPos)return true;
		if(allignFromTop)if(x > xPos && x < xPos + width && y > yPos && y < yPos + height)return true;
		return false;
	}
	
	public void onLeftClick(int mouseX, int mouseY){
		int x,y;
		if(!allignFromTop){
			x = (mouseX - xPos + width - EMPTY_SPACE/2) / WIDTH_BUTTON + 1;
			y = (mouseY - yPos + height -EMPTY_SPACE/2) / HEIGHT_BUTTON + 1;
		}else{
			x = (mouseX - xPos - EMPTY_SPACE/2) / WIDTH_BUTTON + 1;
			y = (mouseY - yPos - EMPTY_SPACE/2) / HEIGHT_BUTTON + 1;
		}
		try{
			getButton(x, y).onLeftClick((GameScreen)getScreen());
		}catch(Exception e){
			//no button at that place!
		}
	}
	
	public void update(int xPos, int yPos){
		this.xPos = xPos;
		this.yPos = yPos;
		height =((int) Math.floor(buttons.size()/BUTTONS_PER_ROW)+1) * HEIGHT_BUTTON + EMPTY_SPACE;
		width = BUTTONS_PER_ROW * WIDTH_BUTTON +EMPTY_SPACE;
	}
	
	public void onHoverOver(SpriteBatch batch, int mouseX, int mouseY){
		int x, y;
		int screenX = 0;
		int screenY = 0;
		if(!allignFromTop){
			x = (mouseX - xPos + width - EMPTY_SPACE / 2) / WIDTH_BUTTON + 1;
			y = (mouseY - yPos + height - EMPTY_SPACE / 2) / HEIGHT_BUTTON + 1;
			screenX = xPos - width;
			screenY = yPos - height;
		}else{
			x = (mouseX - xPos - EMPTY_SPACE / 2) / WIDTH_BUTTON + 1;
			y = (mouseY - yPos - EMPTY_SPACE / 2) / HEIGHT_BUTTON + 1;
			screenX = xPos;
			screenY = yPos;
		}
		try{
			getButton(x, y).renderHoverOver(batch, screenX + EMPTY_SPACE/2 + x * WIDTH_BUTTON, screenY + EMPTY_SPACE/2 + y * HEIGHT_BUTTON);
		}catch(Exception e){}; // no button at that place
	}
	
	public MenuBarPopupButton getButton(int x, int y){
		if(5*(y-1)+x <= buttons.size()) return buttons.get(5*(y-1)+x-1);
		return null;
	}
	
	public void update(int translationX, int translationY, int mouseX, int mouseY) {}
}
