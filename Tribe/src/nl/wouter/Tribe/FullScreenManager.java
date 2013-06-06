package nl.wouter.Tribe;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;

public class FullScreenManager {
	private RTSComponent component;
	private boolean fullscreen = false;
	private int lastWidth, lastHeight;
	
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
		fullscreen = true;
		lastWidth = Gdx.graphics.getWidth();
		lastHeight = Gdx.graphics.getHeight();
		Gdx.graphics.setDisplayMode(largest.width, largest.height, true);
	}
	
	public void update(){
	}
	
	public void restoreScreen(){
		Gdx.graphics.setDisplayMode(lastWidth, lastHeight, false);
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
