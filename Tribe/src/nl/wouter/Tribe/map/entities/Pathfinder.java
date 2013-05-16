package nl.wouter.Tribe.map.entities;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;

import nl.wouter.Tribe.map.Direction;
import nl.wouter.Tribe.map.Map;
import nl.wouter.Tribe.map.tiles.Tile;

/** tekst en uitleg op http://www.policyalmanac.org/games/aStarTutorial.htm */

public class Pathfinder extends Thread {
	private static Direction[] directions = {Direction.NORTH, Direction.NORTH_EAST, Direction.EAST, Direction.SOUTH_EAST, Direction.SOUTH, Direction.SOUTH_WEST, Direction.WEST, Direction.NORTH_WEST};
	private final Point goal;
	private final Point start;
	private final MovingEntity requester;
	private final Tile[][] currentMap;
	private final Map map;
	private final ArrayList<Entity> copyOfEntities;
	private final boolean fromEndPoint;
	
	private Node finalNode;
	
	public static void moveTo(MovingEntity requester, Point start, Point goal, Map map, ArrayList<Entity> copyOfEntities, boolean fromEndPoint){
		if(map.isSolid(goal)){
			return;
		}
		
		new Pathfinder(requester, start, goal, map.getSurface(), map, copyOfEntities, fromEndPoint).start();
	}
	
	private Pathfinder(MovingEntity requester ,Point start, Point goal, Tile[][] currentMap, Map map, ArrayList<Entity> copyOfEntities, boolean fromEndPoint){
		this.start = start;
		this.goal = goal;
		this.currentMap = currentMap;
		this.requester = requester;
		this.map = map;
		this.copyOfEntities = copyOfEntities;
		this.fromEndPoint = fromEndPoint;
	}
	
	public void run(){
		requester.setNextDirections(getPath(), fromEndPoint);
		requester.setEndPoint(goal);
	}
	
	private synchronized LinkedList<Direction> getPath(){
		LinkedList<Direction> result = new LinkedList<Direction>();
		
		ArrayList<Node> openlist = new ArrayList<Node>();
		ArrayList<Node> closedlist = new ArrayList<Node>();
		
		Node firstNode = new Node(null, start.x, start.y);
		openlist.add(firstNode);
		long startTimeNano = System.nanoTime();
		while(!openlist.isEmpty() && finalNode == null){
			getNodeLowestF(openlist).checkNeighbours(currentMap, openlist, closedlist);
			if(System.nanoTime() - startTimeNano > 1000000000){
				//System.out.println("took too long to calculate..");
				return null;
			}
		}
		
		try{
		Node node = finalNode;
		if(node == null){
			return null;
		}
		while(true){
			Direction dir = node.getDirection();
			if(dir != null)
				result.addFirst(dir);
			
			if(node.parent != null){
				node = node.parent;
			}else break;
		}
		}catch(Exception e){
			final Exception e2 = e;
			new Thread() {
				public void run(){
					e2.printStackTrace();
				}
			}.start();
			return null;
		}
		return result;
	}
	
	/** @return Node with lowest F value */
	private Node getNodeLowestF(ArrayList<Node> openlist){
		Node lowestF = openlist.get(0);
		
		for(Node n: openlist){
			if(n.getF() < lowestF.getF()) lowestF = n;
		}
		
		return lowestF;
	}
	
	private Node getEqualNode(ArrayList<Node> list, Node node){
		for(Node n: list){
			if(n.getXPos() == node.getXPos() && n.getYPos() == node.getYPos()) return n;
		}
		
		return null;
	}
	
	private class Node {
		int g, h;
		Node parent;
		//private Point position;
		private int posX, posY;
		
		public Node(Node parent, int x, int y){
			this.parent = parent;
			posX = x;
			posY = y;
			
			calculateG();
			calculateH();
		}
		
		public int getF(){
			calculateG();
			calculateH();
			
			return g + h;
		}
		
		public void calculateG(){
			int parentG = 0;
			if(parent != null){
				parentG += parent.g;
				parentG += (isDiagonal() ? 14 : 10);
			}
			g = parentG;
		}
		
		public boolean isDiagonal(){
			int dx = posX - parent.posX;
			int dy = posY - parent.posY;
			
			if(dx == 0 || dy == 0) return false;
			return true;
		}
		
		public Direction getDirection(){
			if(parent == null) return null;
			return Direction.getDirection(posX - parent.posX, posY - parent.posY);
		}
		
		public void calculateH(){
			int dx = Math.abs(getXPos() - goal.x);
			int dy = Math.abs(getYPos() - goal.y);
			
			h = 10 * (dx + dy);
		}
		
		public int getXPos(){
			return posX;
		}
		
		public int getYPos(){
			return posY;
		}
		
		public void checkNeighbours(Tile[][] surface, ArrayList<Node> openlist, ArrayList<Node> closedlist){
			openlist.remove(this);
			closedlist.add(this);
			
			if(posX == goal.x && posY == goal.y){
				finalNode = this;
				return;
			}
			
			for(Direction dir: directions){
				Point newPosition = dir.nextPoint(posX, posY);
				if(newPosition.x < 0 || newPosition.y < 0 || newPosition.x >= currentMap.length || newPosition.y >= currentMap[0].length) {
				}else{
					Entity e = map.getEntity(newPosition.x, newPosition.y, copyOfEntities);
					if(!currentMap[newPosition.x][newPosition.y].isSolid() && (e == null || e.isWalkable(newPosition.x, newPosition.y, requester))){
						
						Node newNode = new Node(this, newPosition.x, newPosition.y);
						
						Node openNode = getEqualNode(openlist, newNode);
						if(openNode != null){
							if(openNode.g > newNode.g){
								openNode.parent = this;
								openNode.calculateG();
								openNode.calculateH();
							}
						}else{
							openlist.add(new Node(this, newPosition.x, newPosition.y));
						}
					}
				}
			}
		}
	}
}
