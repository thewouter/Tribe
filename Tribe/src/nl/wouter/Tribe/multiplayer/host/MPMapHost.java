package nl.wouter.Tribe.multiplayer.host;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.LinkedList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import nl.wouter.Tribe.map.Map;
import nl.wouter.Tribe.map.entities.Entity;
import nl.wouter.Tribe.map.entities.MovingEntity;
import nl.wouter.Tribe.map.entities.SheepEntity;
import nl.wouter.Tribe.map.entities.players.PlayerEntity;
import nl.wouter.Tribe.map.entities.players.professions.Profession;
import nl.wouter.Tribe.map.projectiles.Projectile;
import nl.wouter.Tribe.rest.Util;

public class MPMapHost extends Map implements Cloneable{
	public MPHost host;

	public MPMapHost(int mapSize, MPHost host) {
		super(mapSize, null);
		this.host = host;
	}
	
	public synchronized void update(int screenWidth, int screenHeight){
		int x = 0, y = 0;
		if(host.input.up.isPressed()) y += 5;
		if(host.input.down.isPressed()) y -= 5;
		if(host.input.left.isPressed()) x += 5;
		if(host.input.right.isPressed()) x -= 5;
		translate(x, y);
		for(Entity e: getEntities()){
			if(e.xPos + e.yPos + 2 > - ((translationY) / 8) && e.xPos + e.yPos - 1 < - ((translationY - screenHeight - 128)/ 8) && e.xPos - e.yPos - 3 < ((translationX) / 16) && e.xPos - e.yPos + 1 > ((translationX - screenWidth) / 16)) {
				e.update();
			}else if(e instanceof MovingEntity){
				e.update();
			}
		}
			
		double fraction = ((double)amountSheepGroups/((double)SheepEntity.APROX_LIFETIME_IN_TICKS));
		if(Util.RANDOM.nextDouble() <= fraction){
			addSheepGroup(null);
		}
		for(Entity e: toBeRemoved){
			host.entityRemoved(getEntities().indexOf(e));
		}
		for(Projectile p: getProjectiles()){
			p.update();
		}
		handleEntityMutations(true);
	}
	
	public void render(SpriteBatch batch){
		render(batch, new Dimension(1000, 1000), 1000, 1000);
	}
	
	public synchronized void addEntity(Entity e){
		super.addEntity(e);
		if(host != null) host.entityAdded(e, e.owner);
	}
	
	public synchronized void addEntity(LinkedList<Entity> entities){
		for(Entity e:entities){
			addEntity(e);
		}
	}
	
	public void removeEntity(Entity e){
		super.removeEntity(e);
		if(host != null) host.entityRemoved(e.uniqueNumber);
	}
	
	public void addProfession(PlayerEntity p, Profession prof){
		host.addProfession(p, prof);
	}
	
	public void shootArrow(Entity start, Entity end, boolean fromTop, int horSpeed, int distance){
		super.shootArrow(start, end, fromTop, horSpeed, distance);
		host.arrowShot(start, end, fromTop, horSpeed, distance);
	}
}
