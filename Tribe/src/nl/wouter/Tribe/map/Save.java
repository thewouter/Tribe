package nl.wouter.Tribe.map;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import nl.wouter.Tribe.map.entities.DeerEntity;
import nl.wouter.Tribe.map.entities.Entity;
import nl.wouter.Tribe.map.entities.SheepEntity;
import nl.wouter.Tribe.map.entities.SnakeEntity;
import nl.wouter.Tribe.map.entities.players.PlayerEntity;
import nl.wouter.Tribe.map.structures.natural.GoldMine;
import nl.wouter.Tribe.map.structures.natural.TreeStructure;
import nl.wouter.Tribe.map.structures.nonnatural.BaseOfOperations;
import nl.wouter.Tribe.map.structures.nonnatural.CampFireStructure;
import nl.wouter.Tribe.map.structures.nonnatural.Tent;
import nl.wouter.Tribe.map.tiles.Tile;
import nl.wouter.Tribe.screen.GameScreen;

public class Save {
	
	private static String EXTENSION = ".rts";
	public static int CURRENT_VERSION = 3;
	
	public static void save(Map map, String nameFile){
		File theDir = new File("saves");
		if(!theDir.exists()){
			System.out.println("creating directory: saves");
			theDir.mkdir();
		}
		String fileName = "saves/" + nameFile + EXTENSION;
		File file = new File(fileName);
		
		
		try{
			FileOutputStream file_output = new FileOutputStream(file);
			DataOutputStream data_out = new DataOutputStream(file_output);
			int mapWidth = map.getWidth();
			int mapHeight = map.getLength();
			data_out.writeInt(CURRENT_VERSION);			// First the version
			
			data_out.writeInt(mapWidth);				// second  2 ints are the map width and height
			data_out.writeInt(mapHeight);

			data_out.writeInt(map.amountSheepGroups);	// the amount of sheep groups
			
			for(int x = 0; x < mapHeight; x++){			// after that the ID's of all the tiles
				for(int y = 0; y < mapWidth; y++){
					data_out.writeInt(map.getTile(x, y).getID());
				}
			}
			
			data_out.writeInt(map.getEntities().size());		//the amount of entities
			
			
			for(Entity e: map.getEntities()){		  		// then the Entities
				data_out.writeInt(e.ID);
				data_out.writeInt(e.getxPos());
				data_out.writeInt(e.getyPos());
				data_out.writeInt(e.getHealth());
				
			}
			
			file_output.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	
	public static Map load(String nameFile, GameScreen screen){
		String fileName  = "saves/" + nameFile + EXTENSION;
		File file = new File(fileName);
		Map map = null;
		int version = 0;
		
		try{
			
			FileInputStream file_input = new FileInputStream(file);
			DataInputStream data_in = new DataInputStream(file_input);
			try{
				version = data_in.readInt();
			}catch(IOException e){e.printStackTrace();}
			
			switch(version){
			case 1:
				map = loadV1(map, data_in);
				break;
			case 2:
				map = loadV2(map, data_in);
				break;
			case 3:
				map = loadV3(map, data_in, screen);
			default:
				map = loadV1(map, data_in);
				break;
				
			}
			
			data_in.close();
			
		}catch(Exception e){
			System.out.println("file not present");
		}
		return map;
	}
	
	private static Map loadV3(Map map, DataInputStream data_in, GameScreen screen) {
		try{		
			int width = data_in.readInt();
			int height = data_in.readInt();
				int amountSheepGroups = data_in.readInt();
			
			map = new Map(width, amountSheepGroups);
			
			for(int x = 0; x < height; x++){
				for( int y = 0; y < width; y++){
					int ID = data_in.readInt();
					map.changeTile(x, y, Tile.getTile(ID));
				}
			}
			
			
			int amountEntities = data_in.readInt();
			
			
			
			for(int i = 0; i < amountEntities; i++){
				int ID = data_in.readInt();
				int xPos = data_in.readInt();
				int yPos = data_in.readInt();
				int health = data_in.readInt();
				int extraOne = data_in.readInt();
				if(ID >= 300){				//mines
					switch(ID){
					case 300:
						map.addEntity(new GoldMine(map, screen, xPos, yPos, extraOne));
					}	
				}else if(ID >= 200){		//structures
					switch(ID){
					case 200:
						map.addEntity(new CampFireStructure(map,screen,  xPos, yPos, health, Direction.SOUTH_WEST));
						break;
					case 201:
						map.addEntity(new Tent(map,screen,  xPos, yPos, health, Direction.SOUTH_WEST, 2));
						break;
					case 202:
						map.addEntity(new TreeStructure(map,screen,  xPos, yPos, health, Direction.SOUTH_WEST));
						break;
					case 203:
						map.addEntity(new BaseOfOperations(map,screen,  xPos, yPos, health, Direction.SOUTH_WEST));
						break;
					}
				}else if(ID >= 100){		//movingEntities
					switch(ID){
					case 100:
						map.addEntity(new SnakeEntity(map,screen,  xPos, yPos, health));
						break;
					case 101:
						map.addEntity(new SheepEntity(map,screen,  xPos, yPos, health));
						break;
					case 102:
						map.addEntity(new PlayerEntity(map,screen,  xPos, yPos, null));
						break;
					case 103:
						map.addEntity(new DeerEntity(map,screen,  xPos, yPos, health));
						break;
					}
				}
			}
		}catch(Exception e){ //als hij bij het einde van de file is
			System.out.println("end of file");
		}
		return map;
	}


	private static Map loadV2 (Map map, DataInputStream data_in){
		try{
			
			int width = data_in.readInt();
			int height = data_in.readInt();

			int amountSheepGroups = data_in.readInt();
			
			map = new Map(width, amountSheepGroups);
			
			for(int x = 0; x < height; x++){
				for( int y = 0; y < width; y++){
					int ID = data_in.readInt();
					map.changeTile(x, y, Tile.getTile(ID));
				}
			}
			
			
			int amountEntities = data_in.readInt();
			
			
			
			for(int i = 0; i < amountEntities; i++){
				int ID = data_in.readInt();
				int xPos = data_in.readInt();
				int yPos = data_in.readInt();
				int health = data_in.readInt();
				int extraOne = data_in.readInt();
				if(ID >= 300){				//mines
					switch(ID){
					case 300:
						map.addEntity(new GoldMine(map,null,  xPos, yPos, extraOne));
					}	
				}else if(ID >= 200){		//structures
					switch(ID){
					case 200:
						map.addEntity(new CampFireStructure(map,null,  xPos, yPos, health, Direction.SOUTH_WEST));
						break;
					case 201:
						map.addEntity(new Tent(map,null,  xPos, yPos, health, Direction.SOUTH_WEST, 2));
						break;
					case 202:
						map.addEntity(new TreeStructure(map, null, xPos, yPos, health, Direction.SOUTH_WEST));
						break;
					case 203:
						map.addEntity(new BaseOfOperations(map,null,  xPos, yPos, health, Direction.SOUTH_WEST));
						break;
					}
				}else if(ID >= 100){		//movingEntities
					switch(ID){
					case 100:
						map.addEntity(new SnakeEntity(map,null,  xPos, yPos, health));
						break;
					case 101:
						map.addEntity(new SheepEntity(map, null, xPos, yPos, health));
						break;
					case 102:
						map.addEntity(new PlayerEntity(map, null, xPos, yPos,null, health));
						break;
					case 103:
						map.addEntity(new DeerEntity(map,null,  xPos, yPos, health));
						break;
					}
				}
			}
			
			
			
		}catch(Exception e){ //als hij bij het einde van de file is
			System.out.println("end of file");
		}
		return map;
	}
	
	private static Map loadV1 (Map map, DataInputStream data_in){
		try{
			int width = data_in.readInt();
			int height = data_in.readInt();

			int amountSheepGroups = data_in.readInt();
			
			map = new Map(width, amountSheepGroups);
			
			for(int x = 0; x < height; x++){
				for( int y = 0; y < width; y++){
					int ID = data_in.readInt();
					map.changeTile(x, y, Tile.getTile(ID));
				}
			}
			
			
			int amountEntities = data_in.readInt();
			
			
			
			for(int i = 0; i < amountEntities; i++){
				int ID = data_in.readInt();
				int xPos = data_in.readInt();
				int yPos = data_in.readInt();
				int health = data_in.readInt();
				if(ID >= 200){
					switch(ID){
					case 200:
						map.addEntity(new CampFireStructure(map, null, xPos, yPos, health, Direction.SOUTH_WEST));
						break;
					case 201:
						map.addEntity(new Tent(map,null,  xPos, yPos, health, Direction.SOUTH_WEST, 2));
						break;
					case 202:
						map.addEntity(new TreeStructure(map,null,  xPos, yPos, health, Direction.SOUTH_WEST));
						break;
					}
				}else if(ID >= 100){
					switch(ID){
					case 100:
						map.addEntity(new SnakeEntity(map, null, xPos, yPos, health));
						break;
					case 101:
						map.addEntity(new SheepEntity(map,null,  xPos, yPos, health));
						break;
					case 102:
						map.addEntity(new PlayerEntity(map,null,  xPos, yPos,null, health));
						break;
					case 103:
						map.addEntity(new DeerEntity(map,null,  xPos, yPos, health));
						break;
					}
				}
			}
			
			
			
		}catch(Exception e){ //als hij bij het einde van de file is
			System.out.println("end of file");
		}
		return map;
	}

}
