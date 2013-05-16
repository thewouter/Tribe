package nl.wouter.Tribe.map.entities;

import java.awt.Graphics;

import nl.wouter.Tribe.map.Map;
import nl.wouter.Tribe.screen.SPGameScreen;

public abstract class ItemEntity extends Entity {
	
	public ItemEntity(SPGameScreen screen,Map map, int xPos, int yPos, int ID){
		super(map, xPos, yPos, ID,screen);
	}
	
	public void update(){
		
	}
	
	public void render(Graphics g){
	}
	
	public int getMaxHealth(){
		return 0;
	}
}
