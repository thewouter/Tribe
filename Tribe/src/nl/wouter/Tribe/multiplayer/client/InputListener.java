package nl.wouter.Tribe.multiplayer.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

import nl.wouter.Tribe.screen.MPGameScreen;

public class InputListener extends Thread{
	public BufferedReader r;
	public PrintStream p;
	private ArrayList<String> messageBuffer;
	
	
	public InputListener(MPGameScreen screen, BufferedReader r, PrintStream p){
		this.r = r ;
		this.p = p;
		messageBuffer = new ArrayList<>();
	}


	public synchronized ArrayList<String> getMessages(){
		ArrayList<String> messages = new ArrayList<>();
		for(String s:messageBuffer){
			messages.add(s);
		}
		messageBuffer.clear();
		return messages;
	}
	
	private synchronized void addMessage(String message){
		messageBuffer.add(message);
	}
	
	public void run(){
		try{
			while(true){
				String received = r.readLine();
				if(r == null || received == null){
					break;
				}
				addMessage(received);
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		
		try {
			r.close();
			p.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String read(){
		try {
			return r.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public void send(String message){
		p.println(message);
	}
	
	public void update(String update){
		p.println(update);
	}
}
