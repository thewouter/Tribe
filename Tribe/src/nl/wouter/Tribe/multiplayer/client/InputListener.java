package nl.wouter.Tribe.multiplayer.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;

import nl.wouter.Tribe.screen.MPGameScreen;

public class InputListener extends Thread{
	public BufferedReader r;
	public PrintStream p;
	private MPGameScreen owner;
	
	
	public InputListener(MPGameScreen screen, BufferedReader r, PrintStream p){
		this.r = r ;
		this.p = p;
		owner = screen;
	}
	
	public void run(){
		try{
			while(true){
				String received = r.readLine();
				if(r == null){
					System.out.println("null");
					break;
				}
				//System.out.println(received);
				owner.messageReceived(received);
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
