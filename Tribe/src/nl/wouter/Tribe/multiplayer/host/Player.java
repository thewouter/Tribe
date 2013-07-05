package nl.wouter.Tribe.multiplayer.host;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import nl.wouter.Tribe.InputHandler;
import nl.wouter.Tribe.RTSComponent;
import nl.wouter.Tribe.map.entities.Entity;
import nl.wouter.Tribe.rest.Inventory;
import nl.wouter.Tribe.screen.GameScreen;

public class Player {
	public InputListener input;
	public MPHost host;
	public final int ID;
	private boolean isLoaded = false;
	private String loginName = "Player";	
	private static int PLAYERID = 0;
	
	private static int getNextPlayerID(){
		PLAYERID +=1;
		return PLAYERID;
	}
	
	public Player(RTSComponent component, InputHandler input, MPHost host, BufferedReader r, PrintStream p, List<Entity> entities) {
		ID = getNextPlayerID();
		
		this.input = new InputListener(this, r, p);
		try {
			setLoginName(this.input.r.readLine().split(" ",2)[1]);
		} catch (IOException e) {e.printStackTrace();}
		
		this.host = host;
		
		this.input.send(host.getMap().getData());
				
		this.input.read(); 		// wait for confirmation from the client that the map has been decoded.
		
		this.input.start();
		isLoaded = true;
	}

	public void save() {}

	public void save(String fileName) {}

	public void load() {}

	public void load(String nameFile) {}

	public void update() {}
	
	public void update(String update){
		if(isLoaded()) input.send(update);
	}
	
	public void InputReceived(String message){
		host.messageReceived(message, this);
	}
	
	public void sendTextMessage(String message){
		 input.send(message);
	}
	
	public void quit() {
		host.removePlayer(this);
		input.quit();
	}
	
	public boolean isLoaded(){
		return isLoaded;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	
}
