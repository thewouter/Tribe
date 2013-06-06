package nl.wouter.Tribe.rest;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import nl.wouter.Tribe.map.Direction;
import nl.wouter.Tribe.map.Map;
import nl.wouter.Tribe.map.entities.DeerEntity;
import nl.wouter.Tribe.map.entities.Entity;
import nl.wouter.Tribe.map.entities.SheepEntity;
import nl.wouter.Tribe.map.entities.SnakeEntity;
import nl.wouter.Tribe.map.entities.players.AlertComponent;
import nl.wouter.Tribe.map.entities.players.Bow;
import nl.wouter.Tribe.map.entities.players.PlayerEntity;
import nl.wouter.Tribe.map.entities.players.Shield;
import nl.wouter.Tribe.map.entities.players.Soldier;
import nl.wouter.Tribe.map.entities.players.SoldierComponent;
import nl.wouter.Tribe.map.entities.players.Sword;
import nl.wouter.Tribe.map.entities.players.Weapon;
import nl.wouter.Tribe.map.entities.players.professions.Farmer;
import nl.wouter.Tribe.map.entities.players.professions.Founder;
import nl.wouter.Tribe.map.entities.players.professions.Hunter;
import nl.wouter.Tribe.map.entities.players.professions.LumberJacker;
import nl.wouter.Tribe.map.entities.players.professions.Miner;
import nl.wouter.Tribe.map.entities.players.professions.Profession;
import nl.wouter.Tribe.map.structures.BasicStructure;
import nl.wouter.Tribe.map.structures.natural.GoldMine;
import nl.wouter.Tribe.map.structures.natural.IronMine;
import nl.wouter.Tribe.map.structures.natural.StoneMine;
import nl.wouter.Tribe.map.structures.natural.TreeStructure;
import nl.wouter.Tribe.map.structures.nonnatural.BaseOfOperations;
import nl.wouter.Tribe.map.structures.nonnatural.CampFireStructure;
import nl.wouter.Tribe.map.structures.nonnatural.Farm;
import nl.wouter.Tribe.map.structures.nonnatural.IronSmelter;
import nl.wouter.Tribe.map.structures.nonnatural.SchoolI;
import nl.wouter.Tribe.map.structures.nonnatural.Tent;
import nl.wouter.Tribe.map.structures.nonnatural.warrelated.Barracks;
import nl.wouter.Tribe.map.structures.nonnatural.warrelated.StoneDefenseTower;
import nl.wouter.Tribe.map.structures.nonnatural.warrelated.WoodenDefenseTower;
import nl.wouter.Tribe.map.structures.nonnatural.warrelated.WoodenGate;
import nl.wouter.Tribe.map.structures.nonnatural.warrelated.WoodenWall;
import nl.wouter.Tribe.map.tiles.Tile;
import nl.wouter.Tribe.screen.GameScreen;

/**
 * Class with handy static methods en constants
 */

public class Util {
	public static final Random RANDOM = new Random(2222);
	public static final NameGenerator NAME_GEN = new NameGenerator();
	
	public static int getDistance(Entity a, Entity b){
		int x1 = a.getxPos();
		int y1 = a.getyPos();
		int x2 = b.getxPos();
		int y2 = b.getyPos();
		return getDistance(x1, y1, x2, y2);
	}
	
	public static void drawLine(SpriteBatch batch, int x1, int y1, int x2, int y2, Color color) {
		int x = Math.abs(x2 - x1), y =  Math.abs(y2 - y1);
		int xMin = Math.min(x2 , x1), yMin =  Math.min(y2 , y1);
		Pixmap map = new Pixmap(x, y, Format.RGBA8888);
		map.setColor(color);
		map.drawLine(x1 - xMin, y1 - yMin, x2 - xMin , y2 - yMin);
		Texture t = new Texture(map);
		TextureRegion r = new TextureRegion(t);
		Sprite s = new Sprite(r);
		s.flip(false, true);
		s.setPosition(xMin, yMin);
		s.draw(batch);
		batch.flush();
		map.dispose();
		t.dispose();
	}
	
	public static void fillRoundRect(SpriteBatch batch, int x, int y, int width, int height, int rounding, Color color){
		Pixmap map = new Pixmap(width, height, Format.RGBA8888);
		map.setColor(color);
		map.fillRectangle(rounding, 0, width - 2 * rounding, height);
		map.fillRectangle(0, rounding, width, height - 2 * rounding);
		map.fillCircle(rounding, rounding, rounding);
		map.fillCircle(width - rounding, rounding, rounding);
		map.fillCircle(rounding, height - rounding, rounding);
		map.fillCircle(width - rounding, height - rounding, rounding);
		Texture t = new Texture(map);
		TextureRegion r = new TextureRegion(t);
		r.flip(false, true);
		Sprite s = new Sprite(t);
		s.setPosition(x, y);
		s.draw(batch);
		batch.flush();
		map.dispose();
		t.dispose();
	}
	
	public static void fillRect(SpriteBatch batch, int x, int y, int width, int height, Color color){
		Pixmap map = new Pixmap(width, height, Format.RGBA8888);
		map.setColor(color);
		map.fillRectangle(0, 0, width, height);
		Texture t = new Texture(map);
		TextureRegion r = new TextureRegion(t);
		r.flip(false, true);
		Sprite s = new Sprite(t);
		s.setPosition(x, y);
		s.draw(batch);
		batch.flush();
		map.dispose();
		t.dispose();
	}
	
	public static void drawRect(SpriteBatch batch, int x, int y, int width, int height, Color color){
		Pixmap map = new Pixmap(width, height, Format.RGBA8888);
		map.setColor(color);
		map.drawRectangle(0, 0, width, height);
		Texture t = new Texture(map);
		TextureRegion r = new TextureRegion(t);
		r.flip(false, true);
		Sprite s = new Sprite(r);
		s.setPosition(x, y);
		s.draw(batch);
		batch.flush();
		map.dispose();
		t.dispose();
	}
	
	public static int getDirectionInDegrees(Entity a, Entity b, boolean fromTop){
		int screenXA = a.getScreenX() + Tile.getWidth() / 2;
		int screenYA = a.getScreenY() + Tile.getHeight() / 2;
		int screenXB = b.getScreenX() + Tile.getWidth() / 2;
		int screenYB = b.getScreenY() + Tile.getHeight() / 2;
		
		int x = - screenXA + screenXB;
		int y = screenYA - screenYB;

		if(x == 0 && y < 0){
			return 180;
		}
		int reminder = 0;
		if(x >= 0 && y <= 0){
			reminder = 90;
			int temp = y;
			y = x;
			x = -temp;
		}else if(x < 0 && y <= 0){
			reminder = 180;
			int temp = x;
			x = abs(y);
			y = abs(temp);
		}else if(x < 0 && y >= 0){
			reminder = 270;
			int temp = y;
			y = -x;
			x = -temp;
		}
		
		x = abs(x);
		y = abs(y);
		
		return (int) (reminder + Math.toDegrees(Math.atan2(y , x)));
		
	}
	
	public static Color colorFromHex(long hex){
            float a = (hex & 0xFF000000L) >> 24;
            float r = (hex & 0xFF0000L) >> 16;
            float g = (hex & 0xFF00L) >> 8;
            float b = (hex & 0xFFL);
                            
            return new Color(r/255f, g/255f, b/255f, a/255f);
    }
	
	public static int getScreenX(int mapX, int mapY){
		return (mapX - mapY) * (-Tile.WIDTH / 2);
	}
	
	public static int getScreenY(int mapX, int mapY){
		return (mapX + mapY) * (Tile.HEIGHT / 2);
	}
	
	public static int getMapX(int screenX, int screenY){
		return (int) Math.floor((screenY / 16.0) - ((screenX - Tile.WIDTH / 2) / 32.0));
	}
	
	public static int getMapY(int screenX, int screenY){
		return (int) Math.floor((screenY / 16.0) + ((screenX - Tile.WIDTH / 2) / 32.0));
	}
	
	public static int abs(int x) {
		return (x > 0) ? x : -x;
	}
	
	public static int getDistance(int x1, int y1, int x2, int y2){
		return (int) Math.sqrt(Math.pow((x1-x2), 2) + Math.pow((y1-y2), 2));
	}
	
	public static ArrayList<String> splitString(String string){
		char[] inChars = string.toCharArray();		
		ArrayList<String> result = new ArrayList<String>();
		int lastSpace = -1;
		for(int i = 0; i < inChars.length; i++){
			if((int)inChars[i]==32){
				String s = "";
				for(int ii = lastSpace + 1; ii < i; ii++){
					s = s + inChars[ii];
				}
				s.trim();
				lastSpace = i;
				if(s == "") continue;
				result.add(s);
			}
		}
		String s = "";
		for(int i = lastSpace + 1; i < inChars.length; i++){
			s = s + inChars[i];
		}
		s.trim();
		
		result.add(s);
		return result;
	}
	
	public static int parseInt(final String s){
	    if (s == null )
	        throw new NumberFormatException("Null string");
	    else if (s == ""){
	    	System.out.println("string is '' (empty) ");
	    	new Exception().printStackTrace();
	    	return 0;
	    }
	    
	    int num  = 0;
	    int sign = -1;
	    final int len  = s.length();
	    final char ch  = s.charAt(0);
	    if ( ch == '-' ){
	        if ( len == 1 )
	            throw new NumberFormatException( "Missing digits:  " + s );
	        sign = 1;
	    }else{
	        final int d = ch - '0';
	        if ( d < 0 || d > 9 )
	            throw new NumberFormatException( "Malformed:  " + s );
	        num = -d;
	    }
	    
	    final int max = (sign == -1) ? -Integer.MAX_VALUE : Integer.MIN_VALUE;
	    final int multmax = max / 10;
	    int i = 1;
	    while ( i < len ){
	        int d = s.charAt(i++) - '0';
	        if ( d < 0 || d > 9 )
	            throw new NumberFormatException( "Malformed:  " + s );
	        if ( num < multmax )
	            throw new NumberFormatException( "Over/underflow:  " + s );
	        num *= 10;
	        if ( num < (max+d) )
	            throw new NumberFormatException( "Over/underflow:  " + s );
	        num -= d;
	    }
	    
	    return sign * num;
	}
	
	public static Entity getEntity(Map map, String entity, GameScreen screen){
		Entity e = getEntity(entity, map, screen);
		return e;
	}
	
	public static Entity getEntity(Map map, GameScreen screen, int ID, int xPos, int yPos, int[] extraInfoOne, int front){
		int extraInfo = 0;
		if(extraInfoOne.length == 2){
			extraInfo = extraInfoOne[1];
		}
		Entity e = null;
		switch(ID){
		case 300:
			e = new GoldMine(map, screen, xPos, yPos, extraInfo);
			break;
		case 301:
			e = new IronMine(map, screen, xPos, yPos, extraInfo);
			break;
		case 200:
			e = new CampFireStructure(map, screen,  xPos, yPos, Direction.SOUTH_WEST);
			break;
		case 201:
			e = new Tent(map, screen,  xPos, yPos, Direction.SOUTH_WEST, extraInfo);
			break;
		case 202:
			e = new TreeStructure(map, screen,  xPos, yPos, Direction.SOUTH_WEST);
			break;
		case 203:
			e = new BaseOfOperations(map, screen,  xPos, yPos, Direction.SOUTH_WEST);
			break;
		case 206:
			e = new StoneMine(map, screen,  xPos, yPos, Direction.SOUTH_WEST);
			break;
		case 209:
			e = new Farm(map, screen,  xPos, yPos, Direction.SOUTH_WEST);
			break;
		case 210:
			e = new SchoolI(map, screen,  xPos, yPos, Direction.SOUTH_WEST);
			break;
		case 211:
			e = new Barracks(map, screen,  xPos, yPos, Direction.SOUTH_WEST);
			break;
		case 212:
			e = new StoneDefenseTower(map, screen,  xPos, yPos, Direction.SOUTH_WEST);
			if(extraInfoOne.length > 2){
				int[] extraInfoSoldier = new int[extraInfoOne.length - 5];
				for(int i = 5; i < extraInfoOne.length; i++){
					extraInfoSoldier[i - 5] = extraInfoOne[i];
				}
				Entity en = getEntity(map, screen, extraInfoOne[1], extraInfoOne[2], extraInfoOne[3], extraInfoOne[4], extraInfoSoldier, extraInfoOne[5], extraInfoOne[6]);
				System.out.println(en);
				((StoneDefenseTower)e).setGuard(en);
			}
			break;
		case 213:
			e = new WoodenWall(map, screen,  xPos, yPos, Direction.SOUTH_WEST);
			break;
		case 214:
			e = new WoodenGate(map, screen,  xPos, yPos, Direction.SOUTH_WEST);
			break;
		case 215:
			e = new IronSmelter(map, screen,  xPos, yPos, Direction.SOUTH_WEST);
			break;
		case 216:
			e = new WoodenDefenseTower(map, screen, xPos, yPos, Direction.SOUTH_WEST);
			if(extraInfoOne.length > 2){
				int[] extraInfoSoldier = new int[extraInfoOne.length - 5];
				for(int i = 5; i < extraInfoOne.length; i++){
					extraInfoSoldier[i - 5] = extraInfoOne[i];
				}
				Entity en = getEntity(map, screen, extraInfoOne[1], extraInfoOne[2], extraInfoOne[3], extraInfoOne[4], extraInfoSoldier, extraInfoOne[5], extraInfoOne[6]);
				System.out.println(en);
				((StoneDefenseTower)e).setGuard(en);
			}
			break;
		case 100:
			e = new SnakeEntity(map, screen,  xPos, yPos);
			break;
		case 101:
			e = new SheepEntity(map, screen,  xPos, yPos);
			break;
		case 102:
			e = new PlayerEntity(map, screen,  xPos, yPos, null);
			((PlayerEntity)e).setProfessionFromHost(getProfession(extraInfo, (PlayerEntity)e));
			break;
		case 103:
			e = new DeerEntity(map, screen,  xPos, yPos);
			break;
		case 107:
			e = new Soldier(map, screen, xPos, yPos, null);
			for(SoldierComponent c: getComponents(extraInfoOne, (Soldier) e)){
				((Soldier)e).addSoldierComponent(c);
			}
			break;
		default:
			System.out.println("Entity not Found: " + ID);
		}
		if(e instanceof BasicStructure && front == 1){
			((BasicStructure)e).setFront(Direction.SOUTH_EAST);
		}
		return e;
	}
	
	public static Entity getEntity(Map map, GameScreen screen, int ID, int xPos, int yPos, int health, int[] extraInfoOne, int front){
		int extraInfo = 0;
		if(extraInfoOne.length == 2){
			extraInfo = extraInfoOne[1];
		}
		Entity e = null;
		switch(ID){
		case 300:
			e = new GoldMine(map, screen, xPos, yPos, extraInfo, health);
			break;
		case 301:
			e = new IronMine(map, screen, xPos, yPos, extraInfo, health);
			break;
		case 200:
			e = new CampFireStructure(map, screen,  xPos, yPos, health, Direction.SOUTH_WEST);
			break;
		case 201:
			e = new Tent(map, screen,  xPos, yPos, health, Direction.SOUTH_WEST, extraInfo);
			break;
		case 202:
			e = new TreeStructure(map, screen,  xPos, yPos, health, Direction.SOUTH_WEST);
			break;
		case 203:
			e = new BaseOfOperations(map, screen,  xPos, yPos, health, Direction.SOUTH_WEST);
			break;
		case 206:
			e = new StoneMine(map, screen,  xPos, yPos, health, Direction.SOUTH_WEST);
			break;
		case 209:
			e = new Farm(map, screen,  xPos, yPos, health, Direction.SOUTH_WEST);
			break;
		case 210:
			e = new SchoolI(map, screen,  xPos, yPos, health, Direction.SOUTH_WEST);
			break;
		case 211:
			e = new Barracks(map, screen,  xPos, yPos, health, Direction.SOUTH_WEST);
			break;
		case 212:
			e = new StoneDefenseTower(map, screen,  xPos, yPos, health, Direction.SOUTH_WEST);
			if(extraInfoOne.length > 2){
				int[] extraInfoSoldier = new int[extraInfoOne.length - 5];
				for(int i = 5; i < extraInfoOne.length; i++){
					extraInfoSoldier[i - 5] = extraInfoOne[i];
				}
				Entity en = getEntity(map, screen, extraInfoOne[1], extraInfoOne[2], extraInfoOne[3], extraInfoOne[4], extraInfoSoldier, extraInfoOne[5], extraInfoOne[6]);
				System.out.println(en);
				((StoneDefenseTower)e).setGuard(en);
			}
			break;
		case 213:
			e = new WoodenWall(map, screen,  xPos, yPos, health, Direction.SOUTH_WEST);
			break;
		case 214:
			e = new WoodenGate(map, screen,  xPos, yPos, health, Direction.SOUTH_WEST);
			break;
		case 215:
			e = new IronSmelter(map, screen,  xPos, yPos, health, Direction.SOUTH_WEST);
			break;
		case 216:
			e = new WoodenDefenseTower(map, screen, xPos, yPos, health, Direction.SOUTH_WEST);
			if(extraInfoOne.length > 2){
				int[] extraInfoSoldier = new int[extraInfoOne.length - 5];
				for(int i = 5; i < extraInfoOne.length; i++){
					extraInfoSoldier[i - 5] = extraInfoOne[i];
				}
				Entity en = getEntity(map, screen, extraInfoOne[1], extraInfoOne[2], extraInfoOne[3], extraInfoOne[4], extraInfoSoldier, extraInfoOne[5], extraInfoOne[6]);
				System.out.println(en);
				((StoneDefenseTower)e).setGuard(en);
			}
			break;
		case 100:
			e = new SnakeEntity(map, screen,  xPos, yPos, health);
			break;
		case 101:
			e = new SheepEntity(map, screen,  xPos, yPos, health);
			break;
		case 102:
			e = new PlayerEntity(map, screen,  xPos, yPos, null);
			((PlayerEntity)e).setProfessionFromHost(getProfession(extraInfo, (PlayerEntity)e));
			e.setHealth(health);
			break;
		case 103:
			e = new DeerEntity(map, screen,  xPos, yPos, health);
			break;
		case 107:
			e = new Soldier(map, screen, xPos, yPos, null);
			for(SoldierComponent c: getComponents(extraInfoOne, (Soldier) e)){
				((Soldier)e).addSoldierComponent(c);
			}
			break;
		default:
			System.out.println("Entity not Found: " + ID);
		}
		if(e instanceof BasicStructure && front == 1){
			((BasicStructure)e).setFront(Direction.SOUTH_EAST);
		}
		return e;
	}
	
	public static ArrayList<SoldierComponent> getComponents(int[] data, Soldier owner){
		ArrayList<SoldierComponent> components = new ArrayList<SoldierComponent>();
		for(int i = 1; i < data.length; i++){
			components.add(getSoldierComponent(data[i], owner));
		}
		return components;
	}
	
	public static SoldierComponent getSoldierComponent(int ID, Soldier owner){
		System.out.println(ID);
		SoldierComponent c = null;
		switch(ID){
		case 600:
			c = new AlertComponent(owner, ID % 100);
			break;
		case 631:
			c = new Shield(owner, 1, 100);
			break;
		case 632:
			c = new Sword(owner, 1);
			break;
		case 633:
			c = new Bow(owner);
			break;
		}
		return c;
	}

	public static Profession getProfession(int ID, PlayerEntity player){
		//System.out.println(ID);
		switch(ID){
		case(400):
			return new LumberJacker(player);
		case(401):
			return new Miner(player, 1);
		case(402):
			return new Hunter(player);
		case(403):
			return new Founder(player);
		case(404):
			return new Farmer(player);
		case(405):
			return new Miner(player, 2);
		case(406):
			return new Miner(player, 3);
		default:
			return null;
		}
	}
	
	public static Entity getEntity(Map map, GameScreen screen, int ID, int xPos, int yPos, int health, int[] extraInfoOne, int uniqeNumber, int front){
		Entity e = getEntity(map, screen, ID, xPos, yPos, health, extraInfoOne, front);
		e.uniqueNumber = uniqeNumber;
		return e;
	}
	
	public static Entity getEntity(String entity, Map map, GameScreen screen){
		//System.out.println(entity);
		ArrayList<String> entityInstrings = splitString(entity);
		int ID = parseInt(entityInstrings.get(0));
		int xPos = parseInt(entityInstrings.get(1));
		int yPos = parseInt(entityInstrings.get(2));
		int health = parseInt(entityInstrings.get(3));
		int number = parseInt(entityInstrings.get(4));
		int[] extraInfoOne = new int[number + 1];
		extraInfoOne[0] = number;
		int n = 5;
		for(int i = 1; i < number + 1; i++){
			extraInfoOne[i] = parseInt(entityInstrings.get(n));
			n++;
		}
		int uniqueNumber = parseInt(entityInstrings.get(n++));
		int front = parseInt(entityInstrings.get(n++));
		
		
		return getEntity(map, screen, ID, xPos, yPos, health, extraInfoOne, uniqueNumber, front);
	}
	
	public static int getProfessionID(Profession prof){
		System.out.println(prof);
		if (prof instanceof LumberJacker){
			return 400;
		}
		if(prof instanceof Miner){
			if(((Miner) prof).level == 1){
				return 401;
			}
			if(((Miner) prof).level == 2){
				return 405;
			}
		}
		if(prof instanceof Hunter){
			return 402; 
		}
		if(prof instanceof Founder){
			return 403;
		}
		if(prof instanceof Farmer){
			return 404;
		}
		return 0;
	}
	
	public static Weapon getWeapon(int ID, Soldier owner){
		switch(ID){
		case 501:
			return new Sword(owner, 1);
		case 500:
			return new Bow(owner);
		default:
			return null;
		}
	}
}	
	