package nl.wouter.Tribe.map.projectiles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import nl.wouter.Tribe.map.Map;
import nl.wouter.Tribe.map.entities.Entity;
import nl.wouter.Tribe.rest.Sound;
import nl.wouter.Tribe.rest.Util;

public class Arrow extends Projectile{
	private final double slope;
	private static int LENGTH_ARROW = 20;

	public Arrow(Map map, Entity owner, int xStart, int yStart, double horSpeed, int distance, int direction, int startHeight, int maxDistance) {
		super(map, owner, xStart, yStart, horSpeed, distance, direction, startHeight, maxDistance);
		new Sound("/res/Sounds/bowshot.mp3").play();
		//int approxLifetimeInTicks = (int) ((TICKS_PER_SECOND * (distance + 1))/Math.sqrt(horSpeedX * horSpeedX + horSpeedY * horSpeedY));
		//System.out.println("approx. lifetime: " + approxLifetimeInTicks);
		slope = - ((double)startHeight / ((double) distance - 1));
		System.out.println("slope arrow: " + slope + "; distance is: " + distance + " and startHeight is: " + startHeight);
		//new Exception().printStackTrace(); to open call hierachy
	}

	public void render(SpriteBatch batch){
		batch.setColor(Color.BLACK);
		Util.drawLine(batch, getScreenX(), getScreenY(), getScreenX() + (int)horSpeedX / 2, (int)getScreenY() + (int)horSpeedY / 2 , Color.BLACK);
	}
	
	public void updateHeight(){
		height += (slope * horSpeed)/ TICKS_PER_SECOND;
	}

	public void onImpact(Entity e) {
		e.damage(1);
	}
	
}