package nl.wouter.Tribe.screen;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import nl.wouter.Tribe.InputHandler;
import nl.wouter.Tribe.RTSComponent;
import nl.wouter.Tribe.map.Direction;
import nl.wouter.Tribe.map.Map;
import nl.wouter.Tribe.map.Save;
import nl.wouter.Tribe.map.entities.Entity;
import nl.wouter.Tribe.map.entities.MovingEntity;
import nl.wouter.Tribe.map.entities.players.PlayerEntity;
import nl.wouter.Tribe.map.entities.players.professions.Profession;
import nl.wouter.Tribe.map.structures.BasicStructure;
import nl.wouter.Tribe.map.structures.Structure;
import nl.wouter.Tribe.map.structures.nonnatural.warrelated.Barracks;
import nl.wouter.Tribe.multiplayer.client.Chat;
import nl.wouter.Tribe.multiplayer.client.InputListener;
import nl.wouter.Tribe.multiplayer.client.MPMapClient;
import nl.wouter.Tribe.popups.entitypopup.EntityPopup;
import nl.wouter.Tribe.popups.screenpopup.BarracksPopup.Button;
import nl.wouter.Tribe.popups.screenpopup.ScreenPopup;
import nl.wouter.Tribe.popups.screenpopup.ScreenPopupButton;
import nl.wouter.Tribe.rest.Sound;
import nl.wouter.Tribe.rest.Util;

	public class MPGameScreen extends GameScreen {
	private MPMapClient map;
	public int port;
	public String IP;
	private InputListener listener;
	private Socket socket;
	private boolean isLoaded = false;
	private ArrayList<String> messagesToHandle = new ArrayList<String>();
	private Chat chat = new Chat(this);


	public MPGameScreen(RTSComponent component, InputHandler input, int port, String IP){
		super(component, input);
		this.IP = IP;
		this.port = port;
		try {
			socket = new Socket(this.IP, this.port);
			listener = new InputListener(this,  new BufferedReader(new InputStreamReader(socket.getInputStream())), new PrintStream(socket.getOutputStream()));
			listener.send("0 " + component.getLoginName());
		} catch (IOException e) {
			System.out.println(port);
			System.out.println(IP);
			System.out.println(e);
			this.component.setTitleScreen();
		}
		map = new MPMapClient(listener.read(), listener, this);
		
		listener.start();
	}
	
	public void setIsLoaded(boolean isLoaded){
		this.isLoaded = isLoaded;
	}
	
	public void save(){
		Save.save(getMap(), "save1");
	}
	
	public void save(String fileName){
		Save.save(getMap(), fileName);
	}
	
	public boolean isLoaded(){
		return isLoaded;
	}
	
	public void load(){
		load("save1");
	}

	public void load(String nameFile){}

	public synchronized void update(){
		if(!pause){
			
			if(getPointer() != null){
				getPointer().update(); 
				if(bar.showPopup == false){
					setPointer(null);
				}
			}
		}
		getMap().update( getWidth(), getHeight());
		bar.update(getWidth(), getHeight());
		
		statusBar.update(getWidth(), getHeight());
	
		if(input.space.isPressed() && selectedEntities != null && !selectedEntities.isEmpty()) targetEntity = selectedEntities.getFirst();
	
		if(targetEntity != null){
			int dx = targetEntity.getScreenX() + (getMap().translationX - getWidth() / 2);
			int dy = targetEntity.getScreenY() + (getMap().translationY - getHeight() / 2);
			
			getMap().translationX -= dx / 10;
			getMap().translationY -= dy / 10;
		
			if(Util.abs(dx) < 10 && Util.abs(dy) < 10) targetEntity = null;
		}
	
		if(input.LMBTapped()){
			if(entityPopup != null){
				entityPopup.onLeftClick(input.getMouseX(), input.getMouseY());
			}else selectedEntities.clear();
			
			if(bar.isInBar(input.getMouseX(), input.getMouseY())){
				
			}else if(entityPopup != null && !entityPopup.isInPopup(input.getMouseX(), input.getMouseY()) ){
				selectedEntities.clear();
				selectedEntities.addAll(getMap().getEntities(getMapX(), getMapY()));
			}else if( entityPopup == null ){
				selectedEntities.addAll(getMap().getEntities(getMapX(), getMapY()));
			}
		}
		
		if(entityPopup != null){
			entityPopup.update(input.getMouseX(), input.getMouseY());
			if(!selectedEntities.contains(entityPopup.getOwner())) entityPopup = null;
		}
		if(input.wasDragging() && (popup == null || !popup.isInPopup(input.mouseX, input.mouseY))){
			int x1 = input.mouseXOnClick, y1 = input.mouseYOnClick, x2 = input.mouseX, y2 = input.mouseY;
			selectedEntities.clear();
			LinkedList<Entity> inRange = (getMap().getEntities(x1, y1 , x2, y2));
			ArrayList<Entity> structures = new ArrayList<Entity>();
			for( Entity e:inRange){
				if(e instanceof MovingEntity){
					selectedEntities.add(e);
				}else if(e instanceof Structure){
					structures.add(e);
				}
			}
			if(selectedEntities.size() == 0){
				selectedEntities.addAll(structures);
			}
		}
		
		if(input.RMBTapped()){
			
			Entity rightClicked = getMap().getEntity(getMapX(), getMapY()); //the Entity that is right clicked, if any
			boolean canMove = true;
			
			if(rightClicked != null && !selectedEntities.isEmpty()) {
				canMove = selectedEntities.getFirst().onRightClick(rightClicked, this, input);
			}
			
			if(canMove){
				if(!selectedEntities.isEmpty() && selectedEntities.getFirst() instanceof MovingEntity){
					if(((MovingEntity)selectedEntities.getFirst()).isMovable()){
						((MovingEntity) selectedEntities.getFirst()).moveTo(new Point(getMapX(), getMapY()));
						entityPopup = null;
					}
				}
			}
		}
		LinkedList<Entity> remove = new LinkedList<Entity>();
		for(Entity e: selectedEntities){
			if(!getMap().getEntities().contains(e)) remove.add(e);
		}
		
		selectedEntities.removeAll(remove);
		
		chat.update();
		
		if (input.p.isTapped()&& popup == null){
			if(!pause)pause();
			else dePause();
		}
		if(input.escape.isTapped()){
			if(popup == null)component.setTitleScreen();
			else setPopup(null);
		}
		if(popup != null){
			popup.update(input.getMouseX(), input.getMouseY());
		}
		if(input.LMBTapped() || input.RMBTapped()) {
			new Sound("/res/Sounds/klick.mp3").play();
		}
		if(isLoaded()){
			for(String message: messagesToHandle){
				messageReceived(message);
			}
			messagesToHandle.clear();
		}
		
	}
	
	public synchronized void render(SpriteBatch batch){
		getMap().render(batch, new Dimension(getWidth(), getHeight()), getWidth(), getHeight());
		super.render(batch, camera);
		chat.render(batch);
	}
	
	public Map getMap(){
		return map;
	}
	
	public void setEntityPopup(EntityPopup popup){
		this.entityPopup = popup;
	}

	public void removePopup(){
		entityPopup = null;
	}

	public int getMapX(){
		return Util.getMapX(input.getMouseX() - getMap().translationX, input.getMouseY() - getMap().translationY);
	}

	public int getMapY(){
		return Util.getMapY(input.getMouseX() - getMap().translationX, input.getMouseY() - getMap().translationY);
	}

	public void deselectEntity(){
		selectedEntities.clear();
	}

	public void pause(){
		pause = true;
		popup = new ScreenPopup((getWidth()-84)/2, (getHeight() - 20)/2, 84, 20, this, false);
		popup.addPart(new ScreenPopupButton("play", popup, input) {
			public void onLeftClick() {
				dePause();
			}
		});
	}

	public void dePause(){
		popup = null;
		pause = false;
	}

	public void setPopup(ScreenPopup popup){
		this.popup = popup;
		if(popup == null){
			pause = false;
		}else pause = true;
	}

	public boolean isOnlyOnMap(int x, int y){
		return (x >0 && x < getWidth() && y > 0 && y < getHeight() && !bar.isInBar(x, y) && !statusBar.isInBar(x, y));
	}
	
	public void sendMessage(String message){
		listener.update(1 + " " + message);
	}
	
	public synchronized void messageReceived(String message){
		if(!isLoaded() ){
			messagesToHandle.add(message);
			return;
		}
		int messageID = Util.parseInt(Util.splitString(message).get(0));
		switch(messageID){
		case 1:
			chat.messageReceived(message);
			break;
		case 2:
			moveEntity(message);
			break;
		case 4:
			entityAdded(message);
			break;
		case 5:
			entityRemoved(message);
			break;
		case 6:
			EntityDamaged(message);
			break;
		case 7:
			professionAdded(message);
			break;
		case 9:
			shootArrow(message);
			break;
		case 10:
			pressBarracksPopup(message);
			break;
		default:
			new Exception("invalid message: " + messageID).printStackTrace();
		}
	}
	
	private void pressBarracksPopup(String message){
		int select = Util.parseInt(Util.splitString(message).get(1));
		int uniqueNumber = Util.parseInt(Util.splitString(message).get(2));
		int buttonID = Util.parseInt(Util.splitString(message).get(3));
		Entity e = getMap().getEntity(uniqueNumber);
		if(e instanceof Barracks){
			if(select == 1){
				((Barracks)e).getpopup().getButton(buttonID).amountToBuild++;
			}else if(select == 2){
				((Barracks)e).getpopup().getButton(buttonID).amountToBuild--;
			}
		}
	}
	
	private void shootArrow(String message){
		Entity start = getMap().getEntity(Util.parseInt(Util.splitString(message).get(1)));
		boolean fromTop = (Util.parseInt(Util.splitString(message).get(2)) == 1)? true : false;
		Entity end = getMap().getEntity(Util.parseInt(Util.splitString(message).get(3)));
		int horSpeed = Util.parseInt(Util.splitString(message).get(4));
		int maxDistance = Util.parseInt(Util.splitString(message).get(5));
		
		getMap().shootArrowFromHost(start, end, fromTop, horSpeed, maxDistance);
		
	}
	
	private void professionAdded(String update){
		int uniqueNumber = Util.parseInt(Util.splitString(update).get(1));
		int professionID = Util.parseInt(Util.splitString(update).get(2));
		if(professionID != 0){
			((PlayerEntity)getMap().getEntity(uniqueNumber)).setProfessionFromHost(Util.getProfession(professionID, (PlayerEntity) getMap().getEntity(uniqueNumber)));
		}else{
			System.out.println("professionID not valid: " + update);
		}
	}
	
	private void entityAdded(String entity){
		//System.out.println(entity);
		int ID = Util.parseInt(Util.splitString(entity).get(2));
		int uniqueNumber = Util.parseInt(Util.splitString(entity).get(3));
		int xPos = Util.parseInt(Util.splitString(entity).get(4));
		int yPos = Util.parseInt(Util.splitString(entity).get(5));
		int health = Util.parseInt(Util.splitString(entity).get(6));
		int number = Util.parseInt(Util.splitString(entity).get(7));
		int[] extraInfoOne = new int[number + 1];
		extraInfoOne[0] = number;
		int n = 8;
		for(int i = 0; i < number; i++){
			extraInfoOne[i+1] = Util.parseInt(Util.splitString(entity).get(n));
			//System.out.println(extraInfoOne[i]);
			n++;
		}
		int front = Util.parseInt(Util.splitString(entity).get(n++));
		Entity e = Util.getEntity(getMap(), this, ID, xPos, yPos,health, extraInfoOne, uniqueNumber, front);
		if(Util.parseInt(Util.splitString(entity).get(1)) == 0) e.setOwned(false);
		e.screen = this;
		((MPMapClient)getMap()).addEntityFromHost(e);
	
	}
	
	private void moveEntity(String entity){
		int oneOrTwo = Util.parseInt(Util.splitString(entity).get(1));
		int uniqueNumber = Util.parseInt(Util.splitString(entity).get(2));
		Entity e = getMap().getEntity(uniqueNumber);
		if(e instanceof MovingEntity){
			switch(oneOrTwo){
			case 1:
				((MovingEntity)e).moveToFromHost(new Point(Util.parseInt(Util.splitString(entity).get(3)),Util.parseInt(Util.splitString(entity).get(4))));
				break;
			case 2:
				((MovingEntity)e).moveToFromHost(getMap().getEntity(Util.parseInt(Util.splitString(entity).get(3))));
				break;
			case 3:
				((MovingEntity)e).followFromHost(getMap().getEntity(Util.parseInt(Util.splitString(entity).get(3))), Util.parseInt(Util.splitString(entity).get(3)));
				break;
			}
		}
	}
	
	public void quit(){
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void moveEntity(MovingEntity movingEntity, int x, int y) {
		String update = 2 + " " + 1 + " "+ movingEntity.uniqueNumber + " " + x + " " + y; 
		listener.update(update);
	}
	
	public void moveEntity(MovingEntity entity, Entity goal){
		String update = 2 + " " + 2 + " "+ entity.uniqueNumber + " " + goal.uniqueNumber; 
		listener.update(update);
	}
	
	public void followEntity(MovingEntity entity, Entity goal, int distance){
		String update = 2 + " " + 3 + " " + entity.uniqueNumber + " " + goal.uniqueNumber + " " + distance;
		listener.update(update);
	}
	
	public void addEntity(Entity e, int x, int y){
		int frontData = 0;
		if(e instanceof BasicStructure){
			frontData = (((BasicStructure)e).getFront() == Direction.SOUTH_EAST)? 1 : 2;
		}
		String update = 3 + " " + e.ID + " " + x + " " + y + " " + e.getHealth() + " " + e.getExtraOne() + " " + ((e.isOwnedByPlayer())? 1: 0) + " " + frontData ;
		listener.update(update);
	}
	
	public void damageEntity(Entity e, int damage){
		listener.update(6 + " " + e.uniqueNumber + " " + damage);
	}
	
	private void EntityDamaged(String message){
		Entity e = getMap().getEntity(Util.parseInt(Util.splitString(message).get(1)));
		if(e != null) {	
			e.damageFromHost(Util.parseInt(Util.splitString(message).get(2)));
		}else{
			System.out.println(Util.parseInt(Util.splitString(message).get(1)));
		}
	}
	
	public void removeEntity(Entity e){
		listener.update(5 + " " + e.uniqueNumber);
	}
	
	private void entityRemoved(String message){
		((MPMapClient)getMap()).removeEntityFromHost(getMap().getEntity(Util.parseInt(Util.splitString(message).get(1))));
	}
	
	public void setProfession(PlayerEntity p, Profession prof){
		listener.update( 7 + " " + p.uniqueNumber + " " + Util.getProfessionID(prof));
	}

	public void startChopping(Entity owner) {
		listener.update(8 + " " + owner.uniqueNumber + " " + 5);
	}

	public void stopChopping(Entity owner) {
		listener.update(8 + " " + owner.uniqueNumber + " " + 6);
	}

	public void startMining(Entity owner) {
		listener.update(8 + " " + owner.uniqueNumber + " " + 3);
	}

	public void stopMining(Entity owner) {
		listener.update(8 + " " + owner.uniqueNumber + " " + 4);
	}

	public void startHunting(Entity owner) {
		listener.update(8 + " " + owner.uniqueNumber + " " + 1);
	}

	public void stopHunting(Entity owner) {
		listener.update(8 + " " + owner.uniqueNumber + " " + 2);
	}
	
	public void shootArrow(Entity start, Entity end, boolean fromTop, int horSpeed, int maxDistance){
		listener.update(9 + " " + start.uniqueNumber + " " + ( fromTop ? "1" : " 0" ) + " " + end.uniqueNumber + " " + horSpeed + " " + maxDistance);
	}
	
	public void barracksPopupButtonSelected(Button b, Barracks barrack){
		listener.update(10 + " 1 " + barrack.uniqueNumber + " " + b.getButtonID());
	}
	
	public void barracksPopupButtonDeselected(Button b, Barracks barrack){
		listener.update(10 + " 2 " + barrack.uniqueNumber + " " + b.getButtonID());
	}
}

