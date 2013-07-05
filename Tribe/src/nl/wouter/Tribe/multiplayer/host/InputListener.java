package nl.wouter.Tribe.multiplayer.host;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;



public class InputListener extends Thread{
	
	public Player p;
	public BufferedReader r;
	private PrintStream out;
	private boolean running = true;
	private ArrayList<String> messageBuffer;
	
	public InputListener(Player p, BufferedReader r, PrintStream out){
		this.p = p;
		this.r = r;
		this.out = out;
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
				if(!running){
					break;
				}
				if(r == null || received == null){
					p.quit();
					break;
				}
				addMessage(received);
			}
		}catch(IOException e){
			System.out.println(e);
			System.out.println("connection lost!");
			p.quit();
		}
		
		try {
			r.close();
			out.close();
			p.quit();
		} catch (final IOException e) {
			new Thread(){
				public void run(){
					e.printStackTrace();
				}
			}.run();
			p.quit();
		}
	}
	
	public void send(String message){
		out.println(message);
	}
	
	public String read(){
		try {
			return r.readLine();
		} catch (IOException e) {
			
		}
		return null;
	}

	public void quit() {
		running = false;
	}
}
