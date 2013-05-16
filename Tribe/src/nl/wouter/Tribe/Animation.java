package nl.wouter.Tribe;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Animation {
	private final int ticksPerFrame;
	private ArrayList<Sprite> frames = new ArrayList<Sprite>();
	private int totalTime = 0, currentTime = 0;
	
	public Animation(int ticksPerFrame){
		this.ticksPerFrame = ticksPerFrame;
	}
	
	public void addScene(Sprite sheep){
		frames.add(sheep);
		totalTime += ticksPerFrame;
	}
	
	public void update(){
		currentTime++;
		
		currentTime = currentTime % totalTime;
	}
	
	public Sprite getImage(){
		return frames.get(currentTime / ticksPerFrame);
	}
	
	public Sprite getImage(int index){
		return frames.get(index);
	}
}