package nl.wouter.Tribe.map.structures.nonnatural.warrelated;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import nl.wouter.Tribe.Images;
import nl.wouter.Tribe.map.Direction;
import nl.wouter.Tribe.map.Map;
import nl.wouter.Tribe.map.entities.Entity;
import nl.wouter.Tribe.map.structures.BasicStructure;
import nl.wouter.Tribe.map.structures.Structure;
import nl.wouter.Tribe.map.tiles.Tile;
import nl.wouter.Tribe.screen.GameScreen;

public abstract class Wall extends BasicStructure {
	protected boolean[] neightboursAreConnected = {false, false, false, false};
	protected Sprite[] images = new Sprite[4];
	private int textureX, textureY;

	public Wall(Map map, GameScreen screen, int ID, int xPos, int yPos, int textureX, int textureY, Direction front) {
		super(map, screen, xPos, yPos, textureX, textureY, ID, front);
		this.textureX =textureX;
		this.textureY =textureY;
		loadImages();
		checkSides();
	}
	
	public Wall(Map map, GameScreen screen, int ID, int xPos, int yPos, int textureX, int textureY, int health, Direction front) {
		super(map, screen, xPos, yPos, textureX, textureY, ID, front);
		this.health = health;
		this.textureX =textureX;
		this.textureY =textureY;
		loadImages();
		checkSides();
	}
	
	public int getSize() {
		return 1;
	}
	
	public void checkSides(Entity ... requester){
		try{
		ArrayList<Entity> entities = map.getEntities();
		for(Entity e: requester){
			entities.add(e);
		}
		for(int i = 0; i < 4; i++){
			neightboursAreConnected[i] = checkSide(i, entities);
		}
		}catch(Exception e) {e.printStackTrace();}
	}
	
	public void setConnection(int side, boolean connected){
		neightboursAreConnected[side] = connected;
	}
	
	public void render(SpriteBatch batch){
		for(int i = 0; i < images.length; i++){
			if(neightboursAreConnected[i]){
				images[i].setPosition(getScreenX() - (Tile.WIDTH / 2) * (getSize() - 1), getScreenY() - (getHeadSpace() * Tile.HEIGHT) + (Tile.HEIGHT / 2) * (getSize() - 1));
				images[i].draw(batch);
			}
		}
		
		boolean flag = true;
		for(boolean b:neightboursAreConnected){
			if(b) flag = false;
		}
		
		if(flag){
			for(Sprite image:images){
				image.setPosition(getScreenX() - (Tile.WIDTH / 2) * (getSize() - 1), getScreenY() - (getHeadSpace() * Tile.HEIGHT) + (Tile.HEIGHT / 2) * (getSize() - 1));
				image.draw(batch);
			}
		}
	}
	
	private boolean checkSide(int side, ArrayList<Entity> entities){
		ArrayList<Entity> es;
		int x = xPos;
		int y = yPos;
		if(side % 2 == 0){	
			x += side - 1;
		}else{
			y += side - 2;
		}
		es = map.getEntities(x, y, entities);
		for(Entity e: es){
			if (e instanceof Structure && ((Structure) e).connectsToWall()){
				if(e instanceof Wall) ((Wall)e).setConnection((side + 2) % 4, true);
				return true;
			}
		}
		return false;
	}
	
	public void update() {}

	protected void loadImages() {
		Images.structures.flip(false, true);
		for (int i = 0, k = 0; i < 2; i++){
			for(int j = 0; j < 2; j++, k++){
				int x = (textureX + j)* Tile.WIDTH;
				int y = (textureY + i * 2) * Tile.HEIGHT;
				int width = getSize() * Tile.WIDTH;
				int height = (getSize() + getHeadSpace()) * Tile.HEIGHT;
				Images.structures.setRegion(x, y, width, height);
				images[k] = new Sprite(Images.structures);
				images[k].flip(false, true);
			}
				
		}
		Images.structures.setTexture(Images.structures.getTexture());
		Images.structures.flip(false, true);
	}
	public String getExtraOne() {
		return "0";
	}
	
	public boolean connectsToWall(){
		return true;
	}
}
