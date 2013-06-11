package nl.wouter.Tribe.map.projectiles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import nl.wouter.Tribe.map.Map;
import nl.wouter.Tribe.map.entities.Entity;
import nl.wouter.Tribe.map.tiles.Tile;
import nl.wouter.Tribe.rest.Sound;
import nl.wouter.Tribe.rest.Util;

public class Arrow extends Projectile{
	private final double slope;
	private static int LENGTH_ARROW = 20;

	public Arrow(Map map, Entity owner, int xStart, int yStart, double horSpeed, int distance, int direction, int startHeight, int maxDistance) {
		super(map, owner, xStart, yStart, horSpeed, distance, direction, startHeight, maxDistance);
		new Sound("/res/Sounds/bowshot.mp3").play();
		slope = - ((double)startHeight / ((double) distance - 1));
	}

	public void render(SpriteBatch batch){
		batch.setColor(Color.BLACK);
		Util.drawLine(batch, getScreenX(), getScreenY() - (int)(height * Tile.HEIGHT), getScreenX() + (int)horSpeedX / 2, (int)getScreenY() + (int)horSpeedY / 2 - (int)(height * Tile.HEIGHT) , Color.BLACK);
	}
	
	public void updateHeight(){
		height += (slope * horSpeed)/ TICKS_PER_SECOND;
	}

	public void onImpact(Entity e) {
		e.damage(1);
	}
	
}