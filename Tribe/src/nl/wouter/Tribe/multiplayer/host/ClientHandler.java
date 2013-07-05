package nl.wouter.Tribe.multiplayer.host;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import nl.wouter.Tribe.map.entities.Entity;


public class ClientHandler extends Thread{
	ServerSocket serverSocket;
	boolean running = true;
	private MPHost host;
	
	public ClientHandler(MPHost host){
		this.host = host;
	}
	
	public void run(){
		ArrayList<Entity> entities= new ArrayList<Entity>();
		
		try {
			serverSocket = new ServerSocket(host.port);
			while(running){
				Socket s = serverSocket.accept();
				entities.clear();
				entities.addAll(host.getMap().getEntities());
				host.addPlayer(new Player(host.component, null, host, new BufferedReader(new InputStreamReader(s.getInputStream())), new PrintStream(s.getOutputStream()),entities));
				
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void quit() {
		running = false;
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
