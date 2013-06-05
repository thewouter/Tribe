package nl.wouter.Tribe;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;

public class FullScreenManager {
	private RTSComponent component;
	private boolean fullscreen = false;
	
	public FullScreenManager(RTSComponent component){
		this.component = component;
	}
	
	public void setFullScreen(){
		int maxWidth = 0;
		DisplayMode largest = null;
		for(DisplayMode d: component.getModes()){
			if(d.width > maxWidth && d.bitsPerPixel == 32){
				maxWidth = d.width;
				largest = d;
			}
		}
		fullscreen = Gdx.graphics.setDisplayMode(largest);
	}
	
	public void update(){
	}
	
	public void restoreScreen(){
		Gdx.graphics.setDisplayMode(600, 600, false);
		fullscreen = false;
	}
	
	public void switchFullScreen(){
		if(!isFullScreen()) setFullScreen();
		else restoreScreen();
	}
	
	public boolean isFullScreen(){
		return fullscreen;
	}
}
