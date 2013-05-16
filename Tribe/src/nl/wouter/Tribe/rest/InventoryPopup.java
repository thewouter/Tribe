package nl.wouter.Tribe.rest;

import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import nl.wouter.Tribe.Images;
import nl.wouter.Tribe.popups.screenpopup.ScreenPopup;
import nl.wouter.Tribe.screen.Screen;

public class InventoryPopup extends ScreenPopup {
	private static int BLANK_SPACE = 10, SIZE_WIDTH = 5, SIZE_HEIGHT = 5, TILE_WIDTH = 16, TILE_HEIGHT = 16, SPACE_BETWEEN_TILES = 4;
	private static int WIDTH = 2 * BLANK_SPACE + (SIZE_WIDTH - 1) * SPACE_BETWEEN_TILES + SIZE_WIDTH * TILE_WIDTH, 
			HEIGHT =  2 * BLANK_SPACE + (SIZE_HEIGHT - 1) * SPACE_BETWEEN_TILES + SIZE_HEIGHT * TILE_HEIGHT;
	public Button gold = new Button(0,0,this, Images.buttons[6][1]);
	public Button stone = new Button(0,1,this, Images.buttons[5][0]);
	public Button wood = new Button(2,1,this, Images.buttons[6][0]);
	public Button vegetables = new Button(0,2,this, Images.buttons[5][1]);
	public Button meat = new Button(1,2,this, Images.buttons[7][0]);
	public Button ironOre = new Button(1, 1, this, Images.buttons[7][1]);
	public Button iron = new Button(1, 0, this, Images.buttons[5][2]);
	public ArrayList<Button> buttons = new ArrayList<>();
	private Inventory inventory;
	
	public InventoryPopup(Screen title, Inventory inventory) {
		super((title.getWidth() - WIDTH) / 2, (title.getHeight() - HEIGHT)/2, WIDTH, HEIGHT, title, false);
		this.inventory = inventory;
		loadButtons();
	}
	
	public void loadButtons(){
		buttons.clear();
		buttons.add(gold);
		gold.updateAmount(inventory.getGold());
		buttons.add(stone);
		stone.updateAmount(inventory.getStone());
		buttons.add(meat);
		meat.updateAmount(inventory.getMeat());
		buttons.add(wood);
		wood.updateAmount(inventory.getWood());
		buttons.add(vegetables);
		vegetables.updateAmount(inventory.getVegetables());
		buttons.add(ironOre);
		ironOre.updateAmount(inventory.getIronOre());
		buttons.add(iron);
		iron.updateAmount(inventory.getIron());
	}
	
	public void render(SpriteBatch batch){
		drawBox(batch, width, height, xPos, yPos);
		for(Button b:buttons){
			b.render(batch);
		}
		
	}
	
	public class Button{
		private int x, y;
		private InventoryPopup popup;
		private TextureRegion image;
		private int amount = 0;
		public Button(int x, int y, InventoryPopup popup, TextureRegion buttons){
			this.x = x;
			this.y = y;
			this.popup = popup;
			this.image = buttons;
		}
		
		public void render(SpriteBatch batch){
			int xPos = popup.xPos + InventoryPopup.BLANK_SPACE + x * (InventoryPopup.TILE_WIDTH + InventoryPopup.SPACE_BETWEEN_TILES);
			int yPos = popup.yPos + InventoryPopup.BLANK_SPACE + y * (InventoryPopup.TILE_HEIGHT + InventoryPopup.SPACE_BETWEEN_TILES);
			Sprite s = new Sprite(image);
			s.setPosition(xPos, yPos);
			s.draw(batch);
			Screen.font.drawLine(batch, amount + "", xPos, yPos);
		}
		
		public void updateAmount(int amount){
			this.amount = amount;
		}
	}
}
