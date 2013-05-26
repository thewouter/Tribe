package nl.wouter.Tribe.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import nl.wouter.Tribe.InputHandler;
import nl.wouter.Tribe.map.entities.Entity;
import nl.wouter.Tribe.map.structures.BasicStructure;
import nl.wouter.Tribe.map.tiles.Tile;
import nl.wouter.Tribe.multiplayer.host.MPMapHost;
import nl.wouter.Tribe.rest.Sound;
import nl.wouter.Tribe.rest.Util;
import nl.wouter.Tribe.screen.GameScreen;

public abstract class MousePointer {
	public Map map;
	public InputHandler input;
	public GameScreen screen;
	public int x = 0, y = 0;
	private Entity entity;
	private Direction face = Direction.SOUTH_WEST;
	private Sprite image = null;
	
	public MousePointer(Map map, InputHandler input, GameScreen screen){
		this.map = map;
		this.input = input;
		this.screen = screen;
		entity = toBuild(face);
		if(entity instanceof BasicStructure){
			image = ((BasicStructure)entity).getImage();
		}
	}
	
	public void update(){
		x = input.getMouseX();
		y = input.getMouseY();
		if(input.LMBTapped() && screen.isOnlyOnMap(x, y)){
			Entity e = toBuild(face);
			if(e == null) {
				afterBuild();
				return;
			}
			HashMap<String, Integer> costs;
			costs= e.getCosts();
			Set s = costs.entrySet();
			Iterator it = s.iterator();
			ArrayList<String> keysDone = new ArrayList<String>();
			while(it.hasNext()){
				Entry en = (Entry)it.next();
				String material = (String)en.getKey();
				int cost = (int) en.getValue();
				if(!handleCosts(material, cost)){ // not enough materials detected!
					System.out.println(material);
					for(String key :keysDone){
						handleCosts(key, -cost);
						System.out.println(key);
					}
					new Sound("src/res/Sounds/airhorn.mp3").play();
					return;
				}else{
					keysDone.add(material);
				}
			}
			map.addEntity(e);
			
			afterBuild();
		}
		
		if(input.comma.isTapped()){
			face = Direction.SOUTH_EAST;
		}
		if(input.dot.isTapped()){
			face = Direction.SOUTH_WEST;
		}
	}
	
	private boolean handleCosts(String material, int amount){
		if(map instanceof MPMapHost){
			return true;
		}
		switch(material){
		case "gold":
			if(screen.inventory.getGold() >= amount){
				screen.inventory.addGold(- amount);
				return true;
			}
			return false;
		case "wood":
			if(screen.inventory.getWood() >= amount){
				screen.inventory.addWood(- amount);
				return true;
			}
			return false;
		case "meat":
			if(screen.inventory.getMeat() >= amount){
				screen.inventory.addMeat(- amount);
				return true;
			}
			
			return false;
		case "vegatables":
			if(screen.inventory.getVegetables() >= amount){
				screen.inventory.addVegetables(- amount);
				return true;
			}
			return false;
		case "stone":
			if(screen.inventory.getStone() >= amount){
				screen.inventory.addStone(- amount);
				return true;
			}
			return false;
		default:
			System.out.println("unknown material: " + material);
			return false;
			
			
		}
	}
	
	public abstract Entity toBuild(Direction face);
	
	public void render(SpriteBatch batch){
		if(entity instanceof BasicStructure){
			int x = this.x - (Tile.WIDTH / 2) * (((BasicStructure)entity).getSize() - 1) - Tile.getWidth() / 2;
			int y = this.y - ((entity.getHeadSpace() )* Tile.HEIGHT) + (Tile.HEIGHT / 2) * (((BasicStructure)entity).getSize() - 1) - Tile.getHeight() / 2;
			int width = (int) image.getWidth();
			int height = (int) image.getHeight();
			
			if(face == Direction.SOUTH_WEST){
				image.setPosition(x, y);
				image.draw(batch);
			}
			else{
				image.flip(true, false);
				image.setPosition(x, y);
				image.draw(batch);
				image.flip(true, false);
			}
		}else{
			batch.setColor(Color.RED);
			Util.fillRect(batch, x, y, 5, 5,Color.RED);
		}
	}
	
	public void afterBuild(){} // can be overwritten in some cases
}
