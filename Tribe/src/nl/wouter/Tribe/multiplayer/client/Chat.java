package nl.wouter.Tribe.multiplayer.client;

import java.util.ArrayList;
import java.util.LinkedList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import nl.wouter.Tribe.InputHandler.Key;
import nl.wouter.Tribe.rest.RTSFont;
import nl.wouter.Tribe.screen.GameScreen;
import nl.wouter.Tribe.screen.MPGameScreen;
import nl.wouter.Tribe.screen.Screen;

public class Chat {
	public static int MAX_LINE_WIDTH = 100;
	public static int X_POS = 0, Y_POS_FROM_BOTTOM = 100;
	private ArrayList<Message> messages = new ArrayList<Message>();
	private GameScreen screen;
	private ArrayList<Message> messagesToRemove = new ArrayList<Message>();
	private LinkedList<String> text = new LinkedList<String>();
	private boolean isSelected = false;
	
	public Chat(GameScreen screen){
		this.screen = screen;
	}
	
	public synchronized void render(SpriteBatch batch){
		int size = messages.size() + 1;
		int yPos = screen.getHeight() - Y_POS_FROM_BOTTOM - size * RTSFont.HEIGHT;
		int xPos = X_POS;
		int height = size * RTSFont.HEIGHT;
		batch.setColor(new Color(0, 0, 0, 35));
		Pixmap map = new Pixmap(MAX_LINE_WIDTH, height, Format.RGBA8888);
		map.setColor(new Color(0, 0, 0, 35));
		map.fill();
		Texture t = new Texture(map);
		Sprite s = new Sprite(t);
		s.setPosition(xPos, yPos);
		s.draw(batch);
		
		for(Message m:messages){
			String message = m.getMessage();
			Screen.font.drawLine(batch, message, xPos, yPos + RTSFont.HEIGHT * messages.indexOf(m), Color.BLACK);
		}
		
		Screen.font.drawLine(batch, getText() + (isSelected ? "_" : ""), xPos, yPos + height - RTSFont.HEIGHT, Color.BLACK);
	}
	
	public synchronized void update(){
		for(Message m:messages){
			m.update();
		}
		if(screen.input.LMBTapped()){
			if(isInRect(screen.input.mouseX, screen.input.mouseY)){
				isSelected = true;
				screen.pause = true;
			}else{
				isSelected = false;
				screen.pause = false;
			}
		}
		if(isSelected){
			for(Key a:screen.input.inputKeys){
				if(a.isTapped()){
					text.add(a.getChars());
				}
			}
			if(screen.input.backspace.isTapped() && !text.isEmpty()){
				 text.removeLast();
			}
			if(screen.input.enter.isTapped()){
				if(screen instanceof MPGameScreen) {
					((MPGameScreen) screen).sendMessage(getText());
					text.clear();
				}
			}
		}
		
		messages.removeAll(messagesToRemove);
		messagesToRemove.clear();
	}
	
	private boolean isInRect(int x, int y){
		if(x > X_POS && x < MAX_LINE_WIDTH && y < screen.getHeight() - Y_POS_FROM_BOTTOM && y > screen.getHeight() - Y_POS_FROM_BOTTOM - RTSFont.HEIGHT){
			return true;
		}
		return false;
	}
	
	private String getText(){
		String textToRender = "";
		for(String s:text){
			textToRender = textToRender + s;
		}
		return textToRender;
	}
	
	public synchronized void removeMessage(Message message){
		messagesToRemove.add(message);
	}
	
	public synchronized void messageReceived(String message){
		messages.add(new Message(message.split(" ", 3)[2], message.split(" ", 3)[1], 600, this));
	}
}
