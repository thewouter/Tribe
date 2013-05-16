package nl.wouter.Tribe.map.structures.natural;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import nl.wouter.Tribe.Images;
import nl.wouter.Tribe.map.Map;
import nl.wouter.Tribe.map.entities.Entity;
import nl.wouter.Tribe.map.structures.Structure;
import nl.wouter.Tribe.map.tiles.Tile;
import nl.wouter.Tribe.screen.GameScreen;
import nl.wouter.Tribe.screen.Screen;

public abstract class MineStructure extends Structure {
	int size;
	public Sprite image;
	public int textureX, textureY;
	
	
	public MineStructure(Map map, GameScreen screen, int xPos, int yPos, int ID, int size) {
		super(map, screen,xPos, yPos, ID);
		if(size < 1) size = 1;
		if(size > 3) size = 3;
		this.size = size;
		textureX = (ID - 300) * 3;
		if(size == 1){
			textureY = 0;
		}else if(size == 2){
			textureY = 1;
		}else{
			textureY = 4;
		}
		loadImage(textureX, textureY, Images.mines.getTexture());
		health = getMaxHealth();
		
	}
	
	public MineStructure(Map map, GameScreen screen, int xPos, int yPos, int ID, int size,  int health){
		super(map, screen, xPos, yPos, ID);
		if(size < 1) size = 1;
		if(size > 3) size = 3;
		this.size = size;
		textureX = (ID - 300) * 3;
		if(size == 1){
			textureY = 0;
		}else if(size == 2){
			textureY = 1;
		}else{
			textureY = 4;
		}
		loadImage(textureX, textureY, Images.mines.getTexture());
		health = getMaxHealth();
		
		this.health = health;
	}
	
	public void render(SpriteBatch batch){
		int x = getScreenX() - (Tile.WIDTH / 2) * (getSize() - 1);
		int y = getScreenY();
		if(size == 2) y -= Tile.HEIGHT / 2;
		image.setPosition(x, y);
		image.draw(batch);
		Screen.font.drawLine(batch, "" + uniqueNumber, getScreenX(), getScreenY());
	}
	
	public void loadImage(int textureX, int textureY, Texture i){
		int x = textureX * Tile.WIDTH;
		int y = textureY * Tile.HEIGHT;
		
		int width = getSize() * Tile.WIDTH;
		int height = getSize() * Tile.HEIGHT;
		
		image = new Sprite(loadImage(x, y, width, height));
		
	}

	public int getSize() {
		return size;
	}
	
	public HashMap<String, Integer> getCosts(){
		return new HashMap<String, Integer>();
	}
	
	public void setSize(int size){
		this.size = size;
		if(size == 1){
			textureY = 0;
		}else if(size == 2){
			textureY = 1;
		}else{
			textureY = 4;
		}
		loadImage(textureX, textureY, Images.mines.getTexture());
	}
	
	public String getExtraOne(){
		return 1 + " " + size;
	}
	
	public void damage(int damage){
		super.damage(damage);
	}
	
	public int getHeadSpace(){
		return 1;
	}

	public abstract void mine(int damage, Entity miner);
}
