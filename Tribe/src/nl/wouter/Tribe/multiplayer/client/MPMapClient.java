package nl.wouter.Tribe.multiplayer.client;

import java.awt.Dimension;
import java.awt.Point;
import java.util.LinkedList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import nl.wouter.Tribe.Images;
import nl.wouter.Tribe.map.Map;
import nl.wouter.Tribe.map.entities.Entity;
import nl.wouter.Tribe.rest.Util;
import nl.wouter.Tribe.screen.MPGameScreen;
import nl.wouter.Tribe.screen.Screen;

public class MPMapClient extends Map{
	private boolean hasLoaded = false;
	private Texture grass;
	private Texture grass2;
	private boolean[][] tiles = new boolean[32][32];
	private MapLoader loader;
	private MPGameScreen screen;
	
	
	public MPMapClient(String map, InputListener input, MPGameScreen screen) {
		super(Integer.parseInt(Util.splitString(map).get(0)), Util.parseInt(Util.splitString(map).get(1)), screen);
		this.screen = screen;
		
		loader = new MapLoader(map, this, input, screen);
		loader.run();
		grass2 = Images.terrain[0][0].getTexture();
		grass = Images.terrain[1][0].getTexture();
		
		for(int x = 0; x < 20; x++){
			for(int y = 0; y < 20; y++){
				boolean b = Util.RANDOM.nextBoolean();
				tiles[x][y] = b;
			}
		}
		
		setHasLoaded(true);
	}
	
	public void update(int screenWidth,int screenHeight){
		if(!isHasLoaded()){
			return;
		}
		super.update(screenWidth, screenHeight);
		
	}
	
	public void render(SpriteBatch batch, Dimension screenSize, int screenWidth, int screenHeight){
		if(!isHasLoaded()){
			for(int x = 0; x < tiles.length; x++){
				for(int y = 0; y < tiles[x].length; y++){
					if(tiles[x][y]){
						batch.draw(grass, x * 32, y * 16);
						batch.draw(grass, x * 32 - 16, y * 16 - 8);
					}else{
						batch.draw(grass2, x * 32, y * 16);
						batch.draw(grass2, x * 32 - 16, y * 16 - 8);
					}
				}
				
			}
			Screen.font.drawLine(batch, loader.checkProgress() + " %", 10, 10);
			return;
		}
		
		super.render(batch, screenSize, screenWidth, screenHeight);
	}
	
	public void addEntityFromHost(Entity u){
		super.addEntity(u);
	}
	
	public void addEntityFromHost(LinkedList<Entity> u){
		super.addEntity(u);
	}
	
	public void removeEntityFromHost(Entity u){
		super.removeEntity(u);
	}
	
	public void addEntity(Entity u){
		screen.addEntity(u, u.xPos, u.yPos);
	}
	
	public void addEntity(LinkedList<Entity> entities){
		for(Entity e:entities){
			addEntity(e);
		}
	}
	
	public void removeEntity(Entity u){
		screen.removeEntity(u);
	}

	public boolean isHasLoaded() {
		return hasLoaded;
	}

	public void setHasLoaded(boolean hasLoaded) {
		this.hasLoaded = hasLoaded;
	}
}
