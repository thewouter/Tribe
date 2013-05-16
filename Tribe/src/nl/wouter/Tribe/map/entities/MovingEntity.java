package nl.wouter.Tribe.map.entities;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import nl.wouter.Tribe.Animation;
import nl.wouter.Tribe.Images;
import nl.wouter.Tribe.RTSComponent;
import nl.wouter.Tribe.map.Direction;
import nl.wouter.Tribe.map.Map;
import nl.wouter.Tribe.map.structures.BasicStructure;
import nl.wouter.Tribe.map.tiles.Tile;
import nl.wouter.Tribe.multiplayer.client.MPMapClient;
import nl.wouter.Tribe.multiplayer.host.MPMapHost;
import nl.wouter.Tribe.rest.Util;
import nl.wouter.Tribe.screen.GameScreen;
import nl.wouter.Tribe.screen.MPGameScreen;

public abstract class MovingEntity extends Entity {
	protected double timeTraveled; //hoelang hij onderweg is
	private Entity goal = null;
	public Entity entityGoal = null;
	private Point entPoint = new Point(0,0), targetPoint = new Point (0,0);
	Direction nextDirection = null;
	Animation flag = new Animation(10);
	
	public LinkedList<Direction> nextDirections = new LinkedList<Direction>();
	private LinkedList<Direction> nextNextDirections = null;
	private LinkedList<Direction> addNextDirections = null;
	private int followDistance = 0;
	
	public MovingEntity(Map map, GameScreen screen, int xPos, int yPos,  int ID){
		super(map, xPos, yPos, ID, screen);
		for(int i = 0; i < Images.flag.length - 1; i++){
			flag.addScene(new Sprite(Images.flag[i][0]));
		}
	}
	
	public void renderSelected(SpriteBatch batch){
		super.renderSelected(batch);
		if(isMoving()){
			flag.getImage().setPosition((entPoint.x - entPoint.y) * (-Tile.WIDTH / 2) + map.translationX, (entPoint.x + entPoint.y) * (Tile.HEIGHT / 2) - (1 * Tile.HEIGHT) + map.translationY);
			flag.getImage().draw(batch);
		}
	}
	
	public void update(){
		if(isMoving()) flag.update();
		if(addNextDirections != null){
			nextDirections.addAll(addNextDirections);
			addNextDirections = null;
		}else if(nextNextDirections != null){
			nextDirections = nextNextDirections;
			nextNextDirections = null;
			if(nextDirection != null){
				nextDirections.add(0, nextDirection);
			}
		}else if(nextDirections == null){
			nextDirections = new LinkedList<Direction>();
		}
		if(goal != null){
			if(goal instanceof MovingEntity){ // following
				if(Util.abs(goal.xPos - targetPoint.x) > 0.1|| Util.abs(goal.yPos - targetPoint.y) > 0.1){
					targetPoint = new Point(goal.xPos,goal.yPos);
					int[] result = getClosestMovePoint(goal, xPos, yPos, followDistance);
					ArrayList<Entity> e = new ArrayList<Entity>();
					e.addAll(map.getEntities());
					Pathfinder.moveTo(this,getNextMovePoint(), new Point(result[0], result[1]), map, e, false);
				}
			}else if(goal instanceof BasicStructure){ // moving
				entityGoal = goal;
				int[] result = getClosestMovePoint(goal, xPos, yPos, followDistance);
				targetPoint = new Point(goal.xPos,goal.yPos);ArrayList<Entity> e = new ArrayList<Entity>();
				e.addAll(map.getEntities());
				Pathfinder.moveTo(this,getNextMovePoint(), new Point(result[0], result[1]), map, e, false);
				goal = null;
			}
		}
		
		
		if(!nextDirections.isEmpty()) {
			if(nextDirection == null) nextDirection = nextDirections.get(0);
			timeTraveled += RTSComponent.MS_PER_TICK / (getTravelTime() * (nextDirection.isDiagonal() ? Math.sqrt(2) : 1.0));
		}
		
		while(timeTraveled > 1){
			timeTraveled -= 1;
			
			if(nextDirections.size() == 1){
				timeTraveled = 0;
				onStopMoving();
			}
			xPos += nextDirection.getxOffset();
			yPos += nextDirection.getyOffset();
			
			if(!nextDirections.isEmpty()) nextDirections.remove(0);
			nextDirection = null;
		}
	}
	
	public int[] getClosestMovePoint(Entity goal, int xPos, int yPos, int distance){
		int size = 1;
		int closestX = 0, closestY = 0;
		int closestDistance = map.getLength();
		if(goal instanceof BasicStructure) size = ((BasicStructure) goal).getSize();
		for(int x = goal.xPos - 1 - distance; x < goal.xPos + 1 + distance + size; x++){
			for(int y = goal.yPos - 1 - distance; y < goal.yPos + 1 + distance + size; y++){
				Entity e = map.getEntity(x,y);
				if(e == null && !map.isSolid(new Point(x,y)) && Util.getDistance(goal.xPos, goal.yPos, x, y) >= distance && Util.getDistance(goal.xPos, goal.yPos, x, y) < distance + 1){
					int ddistance = Util.getDistance(xPos, yPos, x, y);
					if(ddistance <= closestDistance){
						closestX = x;
						closestY = y;
						closestDistance = ddistance;
					}
				}
			}
		}
		int[] result = {closestX,closestY};
		return result;
	}
	
	public void setNextDirections(LinkedList<Direction> toSet, boolean fromEndPoint){
		if(!fromEndPoint) {
			nextNextDirections = toSet;
		}else {
			addNextDirections = toSet;
		}
	}
	
	protected void onStopMoving(){
	}
	
	public void follow(Entity e , int distance){
		if(screen instanceof MPGameScreen){
			((MPGameScreen)screen).followEntity(this, e, distance);
			 return;
		}else if(map instanceof MPMapHost){
			((MPMapHost)map).host.entityFollowed(this, e, distance);
		}
		goal = e;
		entityGoal = e;
		this.followDistance = distance;
	}
	
	public void followFromHost(Entity e, int distance){
		goal = e;
		entityGoal = e;
		this.followDistance = distance;
	}
	
	public void buildMenu(){}
	
	public Direction getNextDirection(){
		if(!nextDirections.isEmpty()) return nextDirections.get(0);
		else return null;
	}
	
	public HashMap<String, Integer> getCosts(){
		return new HashMap<String, Integer>();
	}
	
	public void standStill(){
		nextDirections = new LinkedList<Direction>();
		goal = null;
		entityGoal = null;
		onStopMoving();
	}

	public void moveTo(Point goal){
		if(map instanceof MPMapClient){
			if(screen == null || !isOwnedByPlayer) return;
			((MPGameScreen)screen).moveEntity(this, goal.x, goal.y);
			return;
		}else if(map instanceof MPMapHost){
			((MPMapHost)map).host.entityMoved(this, goal.x, goal.y);
		}
		this.goal = null;
		ArrayList<Entity> e = new ArrayList<Entity>();
		e.addAll(map.getEntities());
		Pathfinder.moveTo(this,getNextMovePoint(), goal, map, e, false);
		followDistance = 0;
	}
	
	public Point getNextMovePoint(){
		if(nextDirection == null){
			return new Point(xPos, yPos);
		}
		//System.out.println(nextDirection);
		return new Point(xPos + nextDirection.getxOffset(), yPos + nextDirection.getyOffset());
	}
	
	public void moveTo(Entity goal){
		if(screen instanceof MPGameScreen){
			if(isOwnedByPlayer()){
				((MPGameScreen)screen).moveEntity(this, goal);
			}
			return;
		}else if(map instanceof MPMapHost){
			((MPMapHost)map).host.entityMoved(this, goal);
		}
		this.goal = goal;
		followDistance = 1;
	}

	public void moveToFromHost(Point goal){
		this.goal = null;
		ArrayList<Entity> e = new ArrayList<Entity>();
		e.addAll(map.getEntities());
		Pathfinder.moveTo(this,getNextMovePoint(), goal, map, e, false);
		followDistance = 0;
	}
	
	public void moveToFromHost(Entity goal){
		this.goal = goal;
		followDistance = 0;
	}
	
	public boolean isMoving(){
		if(nextDirections == null)return false;
		return !nextDirections.isEmpty();
	}
	
	public int getScreenX(){
		int x = super.getScreenX();
		
		Direction direction = null;
		if(nextDirections != null && !nextDirections.isEmpty()) direction = nextDirection;
		
		if(direction != null) x += Math.round(direction.getPointOnScreen().x * timeTraveled);
		
		return x;
	}
	
	public int getScreenY(){
		int y = super.getScreenY();
		
		Direction direction = null;
		if(nextDirections != null && !nextDirections.isEmpty()) direction = nextDirection;
		
		if(direction != null) y += Math.round(direction.getPointOnScreen().y * timeTraveled);
		
		return y;
	}
	
	public void moveRandomLocation(int WALK_RANGE){
		int x, y;
		do{
			x = xPos + Util.RANDOM.nextInt(WALK_RANGE * 2) - WALK_RANGE;
			y = yPos + Util.RANDOM.nextInt(WALK_RANGE * 2) - WALK_RANGE;
		}while(map.isSolid(x, y) && x != xPos && y != yPos);
	
		moveTo(new Point(x, y));
		
	}
	
	public Entity getGoal(){
		return goal;
	}
	
	public void setEndPoint(Point p){
		entPoint = p;    		//fuck spelling.
	}
	
	public Point getEndPoint(){
		return entPoint;
	}
	
	public abstract int getHeadSpace();
	
	/** @return tijd die het duurt om over 1 tile te bewegen */
	protected abstract double getTravelTime();

	public abstract int getSelectedOption() ;

	public abstract void setSelectedOption(int indexSelected) ;
	
	public abstract boolean isMovable();
}
