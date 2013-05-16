package nl.wouter.Tribe.screen.menus;

import java.awt.Graphics;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import nl.wouter.Tribe.InputHandler;
import nl.wouter.Tribe.RTSComponent;
import nl.wouter.Tribe.screen.Screen;

public class PauseScreen extends MenuScreen {
	private final Screen overlappedScreen;
	
	private MenuButton resumeGame = new MenuButton("Resume Game", 0, 20, -1, -1, this);
	
	public PauseScreen(RTSComponent component, Screen overlappedScreen, InputHandler input){
		super(component, input);
		this.overlappedScreen = overlappedScreen;
	}
	
	public void render(SpriteBatch batch, OrthographicCamera camera){
		super.render(batch, camera);
	}
	
	protected MenuButton[] getButtons(){
		MenuButton[] result = {};
		
		return result;
	}
	
	public void buttonPressed(MenuButton menuButton){
		if(menuButton == resumeGame) component.setScreen(overlappedScreen);
	}
}
