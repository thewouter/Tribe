package nl.wouter.Tribe.map;

import java.awt.Point;

import nl.wouter.Tribe.map.tiles.Tile;

public enum Direction {
	NORTH(-1, -1), SOUTH(1, 1), WEST(1, -1), EAST(-1, 1), NORTH_EAST(-1, 0), SOUTH_WEST(1, 0),
	NORTH_WEST(0, -1), SOUTH_EAST(0, 1);
	
	private final byte xOffset, yOffset;
	
	private Direction(int xOffset, int yOffset){
		this.xOffset = (byte) xOffset;
		this.yOffset = (byte) yOffset;
	}
	
	public Tile getTile(Map map, Point position){
		return map.getTile(nextPoint(position).x, nextPoint(position).y);
	}
	
	public Point nextPoint(Point point){
		return new Point(point.x + xOffset, point.y + yOffset);
	}
	
	public Point nextPoint(int x, int y){
		return new Point(x + xOffset, y + yOffset);
	}
	
	public int nextX(int x){
		return x + xOffset;
	}
	
	public int nextY(int y){
		return y + yOffset;
	}
	
	public byte getxOffset(){
		return xOffset;
	}
	
	public byte getyOffset(){
		return yOffset;
	}
	
	public boolean isDiagonal(){
		return !(xOffset == 0 || yOffset == 0);
	}
	
	public Point getPointOnScreen(){
		int x = (xOffset - yOffset) * -16;
		int y = (xOffset + yOffset) * 8;
		
		return new Point(x, y);
	}
	
	public static Direction getDirection(int xOffset, int yOffset){
		for(Direction dir: Direction.values()){
			if(dir.xOffset == xOffset && dir.yOffset == yOffset) return dir;
		}
		
		throw new IllegalArgumentException("Point too far away!");
	}
	
	public static Direction getDirection(Point origin, Point newPoint){
		int dx = newPoint.x - origin.x;
		int dy = newPoint.y - origin.y;
		
		if(Math.abs(dx) > 1 || Math.abs(dy) > 1) throw new IllegalArgumentException("Points to far away from each other!");
		
		for(Direction dir: Direction.values()){
			if(dir.xOffset == dx && dir.yOffset == dy) return dir;
		}
		
		throw new IllegalArgumentException("Point too far away!");
	}
}
