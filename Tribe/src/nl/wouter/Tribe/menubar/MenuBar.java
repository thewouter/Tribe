package nl.wouter.Tribe.menubar;

import java.util.LinkedList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import nl.wouter.Tribe.InputHandler;
import nl.wouter.Tribe.rest.Util;
import nl.wouter.Tribe.screen.GameScreen;

public class MenuBar {
	public InputHandler input;
	public static int  UITLOOP = 5, EXTRA_WIDTH = 30,EXTRA_HEIGHT = 30;
	public int X_POS_FROM_RIGHT, Y_POS_FROM_BOTTOM, WIDTH_BUTTON, HEIGHT_BUTTON;
	public GameScreen screen;
	public LinkedList <Button> buttons = new LinkedList<Button>();
	int xPosOnScreen = 0, yPosOnScreen = 0;
	private MenuBarPopup popup;
	public boolean showPopup = false;
	public int corner, width, height;
	/**
	 * @param input
	 * @param gameScreen
	 * @param X_POS_FROM_RIGHT
	 * @param Y_POS_FROM_BOTTOM
	 * @param WIDTH_BUTTON
	 * @param HEIGHT_BUTTON
	 * @param corner	top left withe te clock 1 , 2 , 3 , 4
	 */
	
	public MenuBar(InputHandler input, GameScreen gameScreen, int X_POS_FROM_RIGHT, int Y_POS_FROM_BOTTOM, int WIDTH_BUTTON, int HEIGHT_BUTTON, int corner) {
		this.input = input;
		this.screen = gameScreen;
		this.corner = corner;
		width = UITLOOP + X_POS_FROM_RIGHT;
		height = UITLOOP + HEIGHT_BUTTON + Y_POS_FROM_BOTTOM;
		this.X_POS_FROM_RIGHT = X_POS_FROM_RIGHT;
		this.WIDTH_BUTTON = WIDTH_BUTTON;
		this.HEIGHT_BUTTON = HEIGHT_BUTTON;
		this.Y_POS_FROM_BOTTOM = Y_POS_FROM_BOTTOM;
	}

	public boolean isInBar(int x, int y){
		if(x > xPosOnScreen && x < xPosOnScreen + width && y > yPosOnScreen && y < yPosOnScreen + height) return true;
		else if(showPopup){
			if(popup.isInPopup(x, y)) return true;
		}
		return false;
	}
	
	public void addButton(Button b){
		buttons.add(b);
		width += WIDTH_BUTTON;
	}
	
	public void addButton(Button b, int index){
		buttons.add(index, b);
		width += WIDTH_BUTTON;
	}
	
	public void removeButton(Button b){
		buttons.remove(b);
		width -= WIDTH_BUTTON;
	}
	
	public void showPopup(){
		showPopup = true;
	}
	
	public void hidePopup(){
		popup = null;
		showPopup = false;
	}
	
	public void render (SpriteBatch batch , int screenWidth, int screenHeight){
		batch.setColor(Color.BLACK);
		if(corner == 3){
			Util.fillRoundRect(batch, xPosOnScreen, yPosOnScreen, buttons.size()*WIDTH_BUTTON + EXTRA_WIDTH, HEIGHT_BUTTON+EXTRA_HEIGHT, 7, Color.BLACK);
			for(int i = 0; i < buttons.size(); i++){
				buttons.get(i).render(batch, xPosOnScreen + UITLOOP + i*WIDTH_BUTTON , yPosOnScreen + UITLOOP);
			}
		}else if(corner == 1){
			Util.fillRoundRect(batch, xPosOnScreen - EXTRA_WIDTH, yPosOnScreen - EXTRA_HEIGHT, width + EXTRA_WIDTH, EXTRA_HEIGHT + height,7, Color.BLACK);
			for(int i = 0; i < buttons.size(); i++){
				if(buttons.get(i)==null) System.out.println("tetestestasdfads  " + i + "  " + buttons.size());
				buttons.get(i).render(batch, xPosOnScreen + X_POS_FROM_RIGHT + i*WIDTH_BUTTON , yPosOnScreen + Y_POS_FROM_BOTTOM);
			}
		}
		if(popup!= null && showPopup){
			popup.render(batch);
			if(popup.isInPopup(input.mouseX, input.mouseY)){
				popup.onHoverOver(batch, input.mouseX, input.mouseY);
			}
		}
	}
	
	public void update(int screenWidth, int screenHeight){
		if(corner == 3){
			yPosOnScreen = screenHeight - UITLOOP - HEIGHT_BUTTON - Y_POS_FROM_BOTTOM;
			xPosOnScreen = screenWidth - UITLOOP - X_POS_FROM_RIGHT - buttons.size()*WIDTH_BUTTON;
			
			if(popup != null) popup.update(xPosOnScreen + width - X_POS_FROM_RIGHT, yPosOnScreen + UITLOOP);
			
		}else if(corner == 1){
			xPosOnScreen = 0;
			yPosOnScreen = 0;
			
			if(popup!= null) {
				popup.update(X_POS_FROM_RIGHT, height);
			}
			
		}
		
		for(Button b: buttons){
			if(b instanceof MenubarTextField)((MenubarTextField) b).update();
		}
		
		if(input.LMBTapped() ){
			if(isOnlyInBar(input.getMouseX(), input.getMouseY())){
				int indexSelected = (input.getMouseX() - UITLOOP - xPosOnScreen) / WIDTH_BUTTON;
				if(indexSelected < buttons.size())buttons.get(indexSelected).onLeftClick();		
			}else if(isInBar(input.getMouseX(), input.getMouseY())){
				popup.onLeftClick(input.getMouseX(),input.getMouseY());
			}
		}
	}
	
	private boolean isOnlyInBar(int x, int y) {
		if(x > xPosOnScreen && x < xPosOnScreen + width && y > yPosOnScreen && y < yPosOnScreen + height)return true;
		return false;
	}

	public void setMenuBarPopup(MenuBarPopup p){
		popup = p;
	}
	
	public int getWidth(){
		return buttons.size()*WIDTH_BUTTON;
	}
	
	public int getHeight(){
		return HEIGHT_BUTTON;
	}
	
}
