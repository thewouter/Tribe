package nl.wouter.Tribe.screen.menus;

import java.awt.Graphics;

import nl.wouter.Tribe.InputHandler;
import nl.wouter.Tribe.RTSComponent;
import nl.wouter.Tribe.screen.Screen;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class MenuScreen extends Screen {
	protected int selectedButton = 0;
	
	public MenuScreen(RTSComponent component, InputHandler input){
		super(component, input);
	}
	
	public void render(SpriteBatch batch, OrthographicCamera camera){
		for(MenuButton b: getButtons()){
			b.render(batch);
		}
	}
	
	public void update(){
		super.update();
		if(popup != null)return;
		for(MenuButton b: getButtons()){
			b.setMouseLocation(super.getMouseLocation(), super.input.LMBTapped());
		}
	}
	
	protected abstract MenuButton[] getButtons();
	
	public abstract void buttonPressed(MenuButton menuButton);
	
}
