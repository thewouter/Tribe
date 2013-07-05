package nl.wouter.Tribe.multiplayer.host;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import nl.wouter.Tribe.InputHandler;
import nl.wouter.Tribe.RTSComponent;
import nl.wouter.Tribe.map.Direction;
import nl.wouter.Tribe.map.entities.Entity;
import nl.wouter.Tribe.map.entities.MovingEntity;
import nl.wouter.Tribe.map.entities.players.PlayerEntity;
import nl.wouter.Tribe.map.entities.players.professions.Founder;
import nl.wouter.Tribe.map.entities.players.professions.Hunter;
import nl.wouter.Tribe.map.entities.players.professions.LumberJacker;
import nl.wouter.Tribe.map.entities.players.professions.Miner;
import nl.wouter.Tribe.map.entities.players.professions.Profession;
import nl.wouter.Tribe.map.structures.BasicStructure;
import nl.wouter.Tribe.map.structures.nonnatural.warrelated.Barracks;
import nl.wouter.Tribe.rest.Inventory;
import nl.wouter.Tribe.rest.Util;
import nl.wouter.Tribe.screen.GameScreen;
import nl.wouter.Tribe.screen.Screen;


public class MPHost extends GameScreen{
	
	public int port;
	
	//public MPMapHost map;
	
	public Inventory inventory =new Inventory(this);
	
	public LinkedList<Player> players = new LinkedList<Player>();
	private ClientHandler clientHandler;
	
	private LinkedList<Player> toRemove = new LinkedList<Player>();
	private LinkedList<Player> toAdd = new LinkedList<Player>();
	
	
		
	public MPHost(RTSComponent component, InputHandler input, int port) {
		super(component , input);
		this.port = port;
		
		setMap(new MPMapHost(256, this));
		
		clientHandler = new ClientHandler(this);
		clientHandler.start();
	}
	
	public void addPlayer(Player p){
		toAdd.add(p);
	}
	
	public void removePlayer(Player p){
		toRemove.add(p);
	}
	
	public void update(){
		for(Player p: players){
			for(String s: p.input.getMessages()){
				messageReceived(s, p);
			}
		}
		
		getMap().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		super.update();
		
		players.removeAll(toRemove);
		players.addAll(toAdd);
		toRemove.clear();
		for(Player p: toAdd){
			PlayerEntity player = new PlayerEntity(getMap(), this, 11, 11, null);
			player.owner = p;
			player.setProfession(new Founder(player));
			getMap().addEntity(player);
		}
		toAdd.clear();
	}
	
	public void entityMoved(Entity e, int newX, int newY){
		String toSend = "2 1 " + e.uniqueNumber + " " + newX + " " + newY; 
		for(Player p: players){
			p.update(toSend);
		}
	}
	
	public void entityMoved(Entity entity, Entity goal){
		String toSend = "2 2 " + entity.uniqueNumber + " " + goal.uniqueNumber;
		for(Player p:players){
			p.update(toSend);
		}
	}
	
	public void entityFollowed(Entity entity, Entity goal, int distance){
		String toSend = "2 3 " + entity.uniqueNumber + " " + goal.uniqueNumber + " " + distance;
		for(Player p:players){ 
			p.update(toSend);
		}
	}
	
	public void entityRemoved(int uniqueNumber){
		for(Player p:players){
			p.update(5 + " " + uniqueNumber);
		}
	}
	
	public void render(SpriteBatch batch, OrthographicCamera camera) {
		//map.render(batch, new Dimension(getWidth(), getHeight()), getWidth(), getHeight());
	}
	
	public void messageReceived(String message, Player owner) {
		//System.out.println(message);
		switch(Util.parseInt(Util.splitString(message).get(0))){
		case 2:
			moveEntity(message);
			break;
		case 1:
			String messageToSend = "1 " + owner.getLoginName() + " " + message.split(" ", 2)[1];
			for(Player p: players){
				p.sendTextMessage(messageToSend);
			}
			break;
		case 3:
			addEntity(message, owner);
			break;
		case 5:
			removeEntity(message);
			break;
		case 6:
			damageEntity(message);
			break;
		case 7:
			professionSet(message);
			break;
		case 8:
			professionMethod(message);
			break;
		case 9:
			shootArrow(message);
			break;
		case 10:
			pressBarrackPopupButton(message);
		}
	}
	
	private void pressBarrackPopupButton(String message){
		int uniqueNumberBarrack = Util.parseInt(Util.splitString(message).get(2));
		int select = Util.parseInt(Util.splitString(message).get(1));
		int buttonID = Util.parseInt(Util.splitString(message).get(3));
		Entity e = getMap().getEntity(uniqueNumberBarrack);
		if(e instanceof Barracks){
			if(select == 1){
				((Barracks)e).getpopup().getButton(buttonID).onSelect();
			}else if(select == 2){
				((Barracks)e).getpopup().getButton(buttonID).deSelect();
			}
		}
	}
	
	public void arrowShot(Entity start, Entity end, boolean fromTop, int horSpeed, int distance){
		String update = 9 + " " + start.uniqueNumber + " " + ((fromTop)? 1 : 0) + " " + end.uniqueNumber + " " + horSpeed + " " + distance;
		for(Player p: players){
			p.update(update);
		}
	}
	
	private void shootArrow(String message){
		int uniqueNumberStart = Util.parseInt(Util.splitString(message).get(1));
		int uniqueNumberEnd = Util.parseInt(Util.splitString(message).get(3));
		boolean fromTop = Util.parseInt(Util.splitString(message).get(2)) == 1 ? true : false ;
		int horSpeed = Util.parseInt(Util.splitString(message).get(4));
		int maxDistance = Util.parseInt(Util.splitString(message).get(5));
		Entity start = getMap().getEntity(uniqueNumberStart);
		Entity end = getMap().getEntity(uniqueNumberEnd);
		getMap().shootArrow(start, end, fromTop, horSpeed, maxDistance);
		
	}
	
	private void professionMethod(String message){
		int uniqueNumber = Util.parseInt(Util.splitString(message).get(1));
		int method = Util.parseInt(Util.splitString(message).get(2));
		Entity e = getMap().getEntity(uniqueNumber);
		if(e instanceof PlayerEntity){
			PlayerEntity player = (PlayerEntity)e;
			Profession profession = player.getProfession();
			if(profession == null){
				System.out.println("profession = null!");
				return;
			}
			switch(method){
			case 1:
				if(profession instanceof Hunter){
					((Hunter)profession).setIsHunting(true);
				}else{
					System.out.println("profession isn't Hunter");
				}
				break;
			case 2:
				if(profession instanceof Hunter){
					((Hunter)profession).setIsHunting(false);
				}else{
					System.out.println("profession isn't Hunter");
				}
				break;
			case 3:
				if(profession instanceof Miner){
					((Miner)profession).setIsMining(true);
				}else{
					System.out.println("profession isn't Miner");
				}
				break;
			case 4:
				if(profession instanceof Miner){
					((Miner)profession).setIsMining(false);
				}else{
					System.out.println("profession isn't Miner");
				}
				break;
			case 5:
				if(profession instanceof LumberJacker){
					((LumberJacker)profession).setIsChopping(true);
				}else{
					System.out.println("profession isn't LumerJacker");
				}
				break;
			case 6:
				if(profession instanceof LumberJacker){
					((LumberJacker)profession).setIsChopping(false);
				}else{
					System.out.println("profession isn't LumberJacker");
				}
				break;
			}
		}
	}
	
	private void addEntity(String message, Player owner) {
		int ID = Util.parseInt(Util.splitString(message).get(1));
		int xPos = Util.parseInt(Util.splitString(message).get(2));
		int yPos = Util.parseInt(Util.splitString(message).get(3));
		int health = Util.parseInt(Util.splitString(message).get(4));
		int number = Util.parseInt(Util.splitString(message).get(5));
		int[] extraInfoOne = new int[number];
		int n = 6;
		for(int i = 0; i < number; i++){
			extraInfoOne[i] = Util.parseInt(Util.splitString(message).get(n));
			n++;
		}
		int isOwnedByPlayer = Util.parseInt(Util.splitString(message).get(n++));
		int front = Util.parseInt(Util.splitString(message).get(n));
		Entity e = Util.getEntity(getMap(), this, ID, xPos, yPos, health, extraInfoOne, front);
		if(isOwnedByPlayer == 1) e.owner = owner;
		e.screen = this;
		e.setHealth(health);
		getMap().addEntity(e);
		
	}
	
	private void damageEntity(String message){
		getMap().getEntity(Util.parseInt(Util.splitString(message).get(1))).damage(Util.parseInt(Util.splitString(message).get(2)));
	}
	
	public void entityAdded(Entity e, Player owner){
		int ID = e.ID;
		int xPos = e.xPos;
		int yPos = e.yPos;
		int uniqueNumber = e.uniqueNumber;
		int health = e.getHealth();
		int frontData = 0;
		if(e instanceof BasicStructure){
			frontData = (((BasicStructure)e).getFront() == Direction.SOUTH_EAST)? 1 : 2;
		}
		String extraInfoOne = e.getExtraOne();
		String update = "4 0 " + ID + " " + uniqueNumber + " " + xPos + " " + yPos + " " + health + " " + extraInfoOne + " " + frontData;
		for(Player p: players){
			if(e.owner == p){
				p.update("4 1 " + ID + " " + uniqueNumber + " " + xPos + " " + yPos + " " + health + " " + extraInfoOne + " " + frontData);
				continue;
			}
			p.update(update);
		}
	}
	
	public void entityDamaged(Entity e, int damage){
		int uniqueNumber = e.uniqueNumber;
		String update = 6 + " " + uniqueNumber + " " + damage;
		for(Player p: players){
			p.update(update);
		}
	}
	
	private void moveEntity(String message){
		Entity e = getMap().getEntity(Util.parseInt(Util.splitString(message).get(2)));
		int oneOrTwo = Util.parseInt(Util.splitString(message).get(1));
		if(oneOrTwo == 1){
			if(e instanceof MovingEntity){
				((MovingEntity)e).moveTo(new Point(Util.parseInt(Util.splitString(message).get(3)),Util.parseInt(Util.splitString(message).get(4))));
			}
		}else if(oneOrTwo == 2){
			if(e instanceof MovingEntity){
				((MovingEntity)e).moveTo(getMap().getEntity(Util.parseInt(Util.splitString(message).get(3))));
			}
		}else if(oneOrTwo == 3){ // haha
			if(e instanceof MovingEntity){
				((MovingEntity)e).follow(getMap().getEntity(Util.parseInt(Util.splitString(message).get(3))), Util.parseInt(Util.splitString(message).get(4)));
			}
		}
	}
	
	private void removeEntity(String message){
		getMap().removeEntity(getMap().getEntity(Util.parseInt(Util.splitString(message).get(1))));
	}

	public void addProfession(PlayerEntity p, Profession prof) {
		String update = 7 + " " + p.uniqueNumber + " " + Util.getProfessionID(prof);
		for(Player pl: players){
			pl.update(update);
		}
	}
	
	private void professionSet(String message){
		int uniqueNumber = Util.parseInt(Util.splitString(message).get(1));
		int ProfessionID = Util.parseInt(Util.splitString(message).get(2));
		Profession.setProfession(ProfessionID, (PlayerEntity)getMap().getEntity(uniqueNumber));
	}
	
	public void quit(){
		for(Player p: players){
			p.quit();
		}
		clientHandler.quit();
	}

	public void save() {
	}

	public void save(String fileName) {
	}

	public void load() {
	}

	public void load(String nameFile) {
	}
}

