package nl.wouter.Tribe.popups.screenpopup;

import java.util.LinkedList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import nl.wouter.Tribe.Images;
import nl.wouter.Tribe.InputHandler;
import nl.wouter.Tribe.map.entities.players.PlayerEntity;
import nl.wouter.Tribe.map.entities.players.Soldier;
import nl.wouter.Tribe.map.entities.players.Weapon;
import nl.wouter.Tribe.map.structures.BasicStructure;
import nl.wouter.Tribe.map.structures.nonnatural.SchoolI;
import nl.wouter.Tribe.map.structures.nonnatural.SchoolII;
import nl.wouter.Tribe.map.structures.nonnatural.warrelated.Barracks;
import nl.wouter.Tribe.multiplayer.host.MPMapHost;
import nl.wouter.Tribe.rest.RTSFont;
import nl.wouter.Tribe.rest.Util;
import nl.wouter.Tribe.screen.MPGameScreen;
import nl.wouter.Tribe.screen.Screen;

public class BarracksPopup extends ScreenPopup {
	private Texture image;
	TextureRegion faceImage = Images.buttons[4][2];
	int xPos, yPos;
	BasicStructure owner;
	InputHandler input;

	public Button swordsMan;
	public Button bow;
	
	LinkedList<Button> buttons = new LinkedList<Button>();

	public BarracksPopup(Screen title, Texture image, Barracks owner, InputHandler input) {
		super(title.getWidth() - (image.getWidth() / 2), title.getHeight() - (image.getHeight() / 2), image.getWidth(), image.getHeight(), title, false);
		this.image = image;
		this.input = input;
		xPos = title.getWidth() / 2 - (image.getWidth() / 2);
		yPos = title.getHeight() / 2 - (image.getHeight() / 2);
		this.owner = owner;
		swordsMan = new Button(owner, xPos + 40, yPos + 20 * 0 + 10, 120, 501, Images.buttons[2][5].getTexture(), 1);
		bow = new Button(owner, xPos + 40, yPos + 50, 120, 500, Images.buttons[3][5].getTexture(), 2);
		
		setButtons();
	}
	
	private void setButtons(){
		buttons.clear();
		buttons.add(swordsMan);
		buttons.add(bow);
	}
	
	public Button getButton(int buttonID){
		for(Button b:buttons){
			if(b.getButtonID() == buttonID){
				return b;
			}
		}
		return null;
	}
	
	public void render(SpriteBatch batch) {
		batch.setColor(Color.WHITE);
		batch.draw(image, xPos, yPos); // render the box.
		
		for(Button b: buttons){
			b.render(batch);
		}
		int size = 0;
		if(owner instanceof SchoolI)size = ((SchoolI)owner).playersCollected.size();
		else if (owner instanceof SchoolII){
			size = ((SchoolII)owner).playersCollected.size();
			batch.setColor(Color.WHITE);
			Screen.font.drawBoldLine(batch, ""+((SchoolII)owner).Knowledge, xPos + width - 15, yPos + 10, Color.BLACK);
		}
		if(size > 6) size = 6;
		for(int i = 0; i < size; i++){
			batch.draw(faceImage, xPos + 5, yPos + 5 + i * 16);
		}
		
	}
	
	public void update(int mouseX, int mouseY){
		super.update(mouseX, mouseY);
		boolean LmouseDown = input.LMBTapped(), RMouseDown = input.RMBTapped();
		for(Button b: buttons){
			if(LmouseDown){
				if(b.isInButton(mouseX, mouseY)){
					b.onSelect();
				}
			}else if (RMouseDown){
				if(b.isInButton(mouseX, mouseY)){
					b.deSelect();
				}
			}
			
		}
	}
	
	public void updateBarracks() {
		for(Button b: buttons){
			b.update();
		}
	}
	
	public boolean soldierNeeded(){
		for(Button b: buttons){
			if(b.amountToBuild > 0){
				return true;
			}
		}
		return false;
	}
	
	public Soldier getSoldier(){
		for(Button b : buttons){
			if(b.amountToBuild > 0){
				Soldier soldier = new Soldier(owner.map, owner.screen, owner.xPos - 1, owner.yPos - 1, null);
				soldier.addSoldierComponent(b.getWeapon(soldier));
				soldier.owner = owner.owner;
				if(owner.map instanceof MPMapHost){
					owner.owner.update("10 2 " + owner.uniqueNumber + " " + b.buttonID);
				}
				return soldier;
			}
		}
		return null;
	}

	public void onLeftClick(int mouseX, int mouseY) {
		for(Button b: buttons){
			if(b.isInButton(mouseX, mouseY))b.onSelect();
		}
	}
	
	
	public class Button{
		int x, y, width,height;
		Texture button;
		public int amountToBuild;
		//SchoolI ownerA;
		//SchoolII ownerB;
		PlayerEntity pupil = null;
		int teller = 0;
		private final int professionID;
		boolean isActive = true;
		private final int buttonID;
		Barracks owner;
		
		public final int TICKS_TO_TEACH;
		
		private Button(Barracks owner, int x, int y,int teachTime, int professionID, Texture image, int buttonID){
			this.x = x;
			this.y = y;
			this.professionID = professionID;
			button = image;
			width = button.getWidth();
			height = button.getHeight();
			amountToBuild = 0;
			TICKS_TO_TEACH = teachTime;
			this.buttonID = buttonID;
			this.owner = owner;
		}
		
		public Weapon getWeapon(Soldier owner){
			amountToBuild -= 1;
			return Util.getWeapon(professionID, owner);
		}
		
		public int getButtonID(){
			return buttonID;
		}
		
		public void update(){
		}
		
		public void render(SpriteBatch batch){
			batch.draw(button, x, y, width, height);
			batch.setColor(new Color(128, 128, 255, 64));
			if(!isActive)Util.drawRect(batch, x, y, width, height, new Color(128, 128, 255, 64));
			
			batch.setColor(Color.WHITE);
			Screen.font.drawLine(batch, "" + amountToBuild, x + width - Screen.font.getLineWidth("" + amountToBuild), y + height - RTSFont.HEIGHT);
		}
		
		public void onSelect(){
			if(screen instanceof MPGameScreen){
				((MPGameScreen)screen).barracksPopupButtonSelected(this, owner);
			}
			if(isActive)amountToBuild++;
		}
		
		public void deSelect(){
			if(screen instanceof MPGameScreen){
				((MPGameScreen)screen).barracksPopupButtonDeselected(this, owner);
			}
			if(isActive)amountToBuild--;
		}
		
		public boolean isInButton(int x, int y){
			return(x >= this.x && y >= this.y && x < width  + this.x && y < height + this.y);
		}
	}

}
