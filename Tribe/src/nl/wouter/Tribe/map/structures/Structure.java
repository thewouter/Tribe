package nl.wouter.Tribe.map.structures;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import nl.wouter.Tribe.Images;
import nl.wouter.Tribe.map.Map;
import nl.wouter.Tribe.map.entities.Entity;
import nl.wouter.Tribe.map.structures.nonnatural.warrelated.Wall;
import nl.wouter.Tribe.screen.GameScreen;

public abstract class Structure extends Entity {
	
	public Structure(Map map, GameScreen screen ,int xPos, int yPos, int ID){
		super(map,xPos, yPos, ID, screen);
		//check for nearby walls to connect if needed.
		if(connectsToWall() && !(this instanceof Wall)){
			for(int x = -1; x <= getSize(); x++){
				for(int y = -1; y <= getSize(); y++){
					Entity e = map.getEntity(xPos + x, yPos + y);
					if(e instanceof Wall){
						((Wall)e).checkSides(this);
					}
				}
			}
		}
		
	}
	
	protected TextureRegion loadImage(int x, int y, int width, int height, TextureRegion region){
		TextureRegion image;
		region.flip(false, true);
		region.setRegion(x, y, width, height);
		image = new TextureRegion(region);
		region = new TextureRegion(region.getTexture());
		region.flip(false, true);
		image.flip(false, true);
		return image;
	}
	
	protected TextureRegion loadImage(int x, int y, int width, int height){
		return loadImage(x, y, width, height, Images.structures);
	}
	
	public abstract int getSize();
	
	public boolean connectsToWall(){
		return false;
	}
	
	public boolean isSolid(int x, int y){
		int dx = x - xPos;
		int dy = y - yPos;
		
		if(dx >= 0 && dy >= 0){
			if(dx < getSize() && dy < getSize()) return true;
		}
		return false;
	}
}
