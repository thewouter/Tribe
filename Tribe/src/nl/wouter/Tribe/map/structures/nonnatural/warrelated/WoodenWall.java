package nl.wouter.Tribe.map.structures.nonnatural.warrelated;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import nl.wouter.Tribe.Images;
import nl.wouter.Tribe.map.Direction;
import nl.wouter.Tribe.map.Map;
import nl.wouter.Tribe.map.tiles.Tile;
import nl.wouter.Tribe.screen.GameScreen;

public class WoodenWall extends Wall {

	private static int ID = 213; 


	public WoodenWall(Map map, GameScreen screen, int xPos, int yPos, Direction front) {
		super(map, screen, ID, xPos, yPos, front);
	}
	
	public WoodenWall(Map map, GameScreen screen, int xPos, int yPos, int health, Direction front) {
		super(map, screen, ID, xPos, yPos, health, front);
	}

	protected void loadImages() {
		for (int i = 0, k = 0; i < 2; i++){
			for(int j = 0; j < 2; j++, k++){
				int x = (6 + j)* Tile.WIDTH;
				int y = (i) * Tile.HEIGHT * 2;
				int width = getSize() * Tile.WIDTH;
				int height = (getSize() + getHeadSpace()) * Tile.HEIGHT;
				try{
					images[k] = Images.split(Images.split(Images.structures, x, y)[1][1],width, height)[0][0].getTexture();;
				}catch(NullPointerException e){
					e.printStackTrace();
				}
			}
		}
	}
	
	public void render(SpriteBatch batch){
		for(int i = 0; i < images.length; i++){
			if(neightboursAreConnected[i]) batch.draw(images[i], getScreenX() - (Tile.WIDTH / 2) * (getSize() - 1), getScreenY() - (getHeadSpace() * Tile.HEIGHT) + (Tile.HEIGHT / 2) * (getSize() - 1));
		}
		
		boolean flag = true;
		for(boolean b:neightboursAreConnected){
			if(b) flag = false;
		}
		
		if(flag){
			for(Texture image:images){
				batch.draw(image, getScreenX() - (Tile.WIDTH / 2) * (getSize() - 1), getScreenY() - (getHeadSpace() * Tile.HEIGHT) + (Tile.HEIGHT / 2) * (getSize() - 1));
			}
		}
	}

	public int getMaxHealth() {
		return 200;
	}

	public String getName() {
		return "Wooden Wall: " + this.toString();
	}

	public HashMap<String, Integer> getCosts() {
		HashMap<String, Integer> costs = new HashMap<String, Integer>();
		costs.put("gold", 1);
		costs.put("wood", 1);
		
		return costs;
	}

	public int getHeadSpace() {
		return 1;
	}

}
