package nl.wouter.Tribe.screen.menus;


import java.awt.Point;

import java.io.IOException;

import javax.imageio.ImageIO;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import nl.wouter.Tribe.Images;
import nl.wouter.Tribe.RTSComponent;
import nl.wouter.Tribe.rest.Util;
import nl.wouter.Tribe.screen.Screen;

/** Een button voor op een MenuScreen */
public class MenuButton {
	private int xOffset, yPos, width, height;
	private MenuScreen screen;
	private String text;
	private boolean selected;
	private Point mouse;
	
	private boolean mouseDown = false;
	
	private Sprite buttonImage;
	private Sprite buttonImageDown;
	
	/**
	 * @param xOffset hoeveel hij van het midden verwijdert is
	 * @param width Als je -1 of lager invoert neemt hij de breedte van de
	 * standaard image
	 * @param height Als je -1 of lager invoert neemt hij de hoogte van de
	 * standaard image
	 */
	public MenuButton(String text, int xOffset, int yPos, int width, int height, MenuScreen s){
		if(buttonImage == null){
			TextureRegion[][] imgSource = Images.split(Images.load("Pictures/button.png"), 1, 2);
			buttonImage = new Sprite(imgSource[0][0]);
			buttonImageDown = new Sprite(imgSource[0][1]);
		}
		
		this.text = text;
		this.xOffset = xOffset;
		this.yPos = yPos;
		if(width > 0) this.width = width;
		else this.width = (int) buttonImage.getRegionWidth();
		if(height > 0) this.height = height;
		else this.height = (int) buttonImage.getRegionHeight();
		screen = s;
	}
	
	
	public void setMouseLocation(Point m, boolean mouseTapped){
		this.mouse = m;
		if(mouseTapped && isInRect()){
			screen.buttonPressed(this);
		}
	}
	
	private boolean isInRect(){
		
		if(mouse == null) return false;
		
		int x = mouse.x;
		int y = mouse.y;
		
		if(x > getxPos() && x < (getxPos() + width)){
			if(y > yPos && y < (yPos + height)){
				return true;
			}
		}
		
		return false;
	}
	
	public void select(){
		
	}
	
	public void render(SpriteBatch batch){
		selected = isInRect();
		if(!selected){
			buttonImage.setPosition(getxPos(), getyPos());
			buttonImage.draw(batch);
		}else{
			buttonImageDown.setPosition(getxPos(), getyPos());
			buttonImageDown.draw(batch);
		}
		Screen.font.drawLine(batch, text, getxPos() + 11, yPos + (height / 2),Util.colorFromHex(0xFF6B6B6B));
		Screen.font.drawLine(batch, text, getxPos() + 10, yPos + (height / 2) + 1,Util.colorFromHex(0xFF6B6B6B));
		
		Screen.font.drawLine(batch, text, getxPos() + 9, yPos + (height / 2), Util.colorFromHex(0xFF4C4C4C));
		Screen.font.drawLine(batch, text, getxPos() + 10, yPos + (height / 2) - 1, Util.colorFromHex(0xFF4C4C4C));
		
		Screen.font.drawLine(batch, text, getxPos() + 10, yPos + (height / 2), Util.colorFromHex(0xFF5B5B5B));
		
		if(selected){
			Util.drawRect(batch, getxPos(), getyPos(), width, height, new Color(128, 128, 255, 32));
		}
	}
	
	public int getxPos(){
		return (((screen.getWidth()) / 2) - (width / 2)) + xOffset;
	}
	
	public int getyPos(){
		return yPos;
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
}
