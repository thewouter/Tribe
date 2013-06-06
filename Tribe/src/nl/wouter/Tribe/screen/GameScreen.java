package nl.wouter.Tribe.screen;


import java.awt.Dimension;
import java.awt.Point;
import java.util.LinkedList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sun.corba.se.spi.legacy.connection.GetEndPointInfoAgainException;

import nl.wouter.Tribe.Images;
import nl.wouter.Tribe.InputHandler;
import nl.wouter.Tribe.RTSComponent;
import nl.wouter.Tribe.map.Direction;
import nl.wouter.Tribe.map.Map;
import nl.wouter.Tribe.map.MousePointer;
import nl.wouter.Tribe.map.entities.Entity;
import nl.wouter.Tribe.map.structures.nonnatural.SchoolI;
import nl.wouter.Tribe.map.structures.nonnatural.SchoolII;
import nl.wouter.Tribe.menubar.Button;
import nl.wouter.Tribe.menubar.HomeBar;
import nl.wouter.Tribe.menubar.MenuBarPopupBuildButton;
import nl.wouter.Tribe.menubar.MenuBarPopupButton;
import nl.wouter.Tribe.menubar.StatusBar;
import nl.wouter.Tribe.popups.entitypopup.EntityPopup;
import nl.wouter.Tribe.popups.screenpopup.ScreenPopup;
import nl.wouter.Tribe.popups.screenpopup.ScreenPopupButton;
import nl.wouter.Tribe.rest.Inventory;
import nl.wouter.Tribe.rest.Util;
import nl.wouter.Tribe.screen.Screen;

public abstract class GameScreen extends Screen {
	private Map map;
	public EntityPopup entityPopup = null;
	public LinkedList<Entity> selectedEntities = new LinkedList<Entity>();
	public Entity targetEntity; //the Entity the camera will go to
	public HomeBar bar;
	public StatusBar statusBar;
	private MousePointer pointer;
	public boolean pause = false;
	public int level = 0;
	private Color gameColor = Color.CYAN;
	public Inventory inventory = new Inventory(this);
	public Button levelUpButton;

	public GameScreen(RTSComponent component, InputHandler input) {
		super(component, input);

		bar = new HomeBar(input, this);
		statusBar = new StatusBar(input,this);
		
		levelUpButton = new Button(Images.buttons[2][1], statusBar) {
			public void onLeftClick() {
				levelUp();
			}
		};

		inventory.addGold(5000);
		inventory.addWood(1700);
		inventory.addStone(1500);
		inventory.addVegetables(100);
		inventory.addMeat(100);
		inventory.addIronOre(200);
		
		bar.buildmenu.addButton(MenuBarPopupBuildButton.Remove);
		
	}
	
	public void render(SpriteBatch batch, OrthographicCamera camera){
		if(getMap() != null) {
			getMap().render(batch,new Dimension(getWidth(), getHeight()), getWidth(), getHeight());
		}

		for(Entity e:selectedEntities){
			e.renderSelected(batch);
		}
		bar.render(batch, getWidth(), getHeight());
		
		statusBar.render(batch, getWidth(), getHeight());
		
		if(entityPopup != null) {
			entityPopup.render(batch);
		}
		
		batch.setColor(Color.WHITE);
		
		if(selectedEntities.size() == 1){
			font.drawBoldLine(batch, selectedEntities.getFirst().getName(), 20, getHeight() - 40, Color.BLACK);
			font.drawBoldLine(batch, "Health: " + selectedEntities.getFirst().getHealthInString(), 20, getHeight() - 30, Color.BLACK);
		}else if(!selectedEntities.isEmpty()){
			font.drawBoldLine(batch, "Multiple Select: " + selectedEntities.size(), 20, getHeight() - 30, Color.BLACK);
		}
		//font.drawBoldLine(batch, getMapX() + ":" + getMapY(), 20, 20, Color.BLACK);
		font.drawBoldLine(batch, (input.mouseX - getMap().translationX) + ":" + (input.mouseY - getMap().translationY), 20, 20, Color.BLACK);
		
		if(input.isDragging()){
			int x1 = input.mouseXOnClick, y1 = input.mouseYOnClick, x2 = input.mouseX, y2 = input.mouseY;
			int xMin = Math.min(x1, x2);
			int yMin = Math.min(y1, y2);
			int xMax = Math.max(x1, x2);
			int yMax = Math.max(y1, y2);
			Util.fillRect(batch, xMin, yMin, xMax - xMin, yMax - yMin, new Color(0f, 0f, 0f, 0.2f));
			
		}
		
		
		if(popup!= null){
			popup.render(batch);
		}
		
		if(getPointer() != null){
			getPointer().render(batch);
		}
	}

	public Entity getSelectedEntity(){
		return selectedEntities.getFirst();
	}
	
	public abstract void save();
	
	public abstract void save(String fileName);
	
	public abstract void load();
	
	public abstract void load(String nameFile);
	
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
	
	public int getMapX(int x, int y){
		return Util.getMapX(x - getMap().translationX, y - getMap().translationY);
	}
	
	public int getMapY(int x, int y){
		return Util.getMapY(x - getMap().translationX, y - getMap().translationY);
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
		super.setPopup(popup);
		if(popup == null){
			pause = false;
		}else pause = true;
	}
	
	public Color getColor(){
		return gameColor;
	}
	
	public boolean isOnlyOnMap(int x, int y){
		if(entityPopup != null)return (x >0 && x < getWidth() && y > 0 && y < getHeight() && !bar.isInBar(x, y) && !statusBar.isInBar(x, y) && entityPopup.isInPopup(x, y));
		return (x >0 && x < getWidth() && y > 0 && y < getHeight() && !bar.isInBar(x, y) && !statusBar.isInBar(x, y));
	}
	
	public boolean isSelected(Entity e){
		return selectedEntities.contains(e);
	}
	
	public void levelUp(){
		level++;
		switch(level){
		case 1:
			bar.buildmenu.addButton(MenuBarPopupBuildButton.TentI);
			bar.buildmenu.addButton(MenuBarPopupBuildButton.Barracks);
			bar.buildmenu.addButton(MenuBarPopupBuildButton.StoneDefenseTower);
			bar.buildmenu.addButton(MenuBarPopupBuildButton.WoodenWall);
			bar.buildmenu.addButton(MenuBarPopupBuildButton.WoodenGate);
			bar.buildmenu.addButton(MenuBarPopupBuildButton.WoodenTower);
			setPointer(null);
			for(Entity e:getMap().getEntities()){
				if(e instanceof SchoolI){
					SchoolI s = ((SchoolI)e);
					s.popup.lumberJacker.activate();
					s.popup.hunter.activate();
				}else if(e instanceof SchoolII){
					SchoolII s = ((SchoolII)e);
					s.popup.lumberJacker.activate();
					s.popup.hunter.activate();
				}
			}
			return;
		case 2:
			inventory.addMeat(-10);
			inventory.addWood(-50);
			inventory.addGold(-25);
			bar.buildmenu.addButton(MenuBarPopupBuildButton.StoneMine);
			bar.buildmenu.addButton(MenuBarPopupBuildButton.TentII, 2);
			bar.buildmenu.addButton(MenuBarPopupBuildButton.Tree);
			for(Entity e:getMap().getEntities()){
				if(e instanceof SchoolI){
					SchoolI s = ((SchoolI)e);
					s.popup.minerI.activate();
					s.popup.founder.activate();
				}else if(e instanceof SchoolII){
					SchoolII s = ((SchoolII)e);
					s.popup.minerI.activate();
					s.popup.founder.activate();
				}
			}
			break;
		case 3:
			inventory.addGold(- 100);
			inventory.addMeat(- 60);
			inventory.addWood(- 25);
		}
	}

	public void setMousePointer(MousePointer p){
		setPointer(p);
	}

	public Map getMap() {
		return map;
	}
	
	public void setMap(Map map) {
		this.map = map;
	}

	public MousePointer getPointer() {
		return pointer;
	}

	public void setPointer(MousePointer pointer) {
		
		
		this.pointer = pointer;
	}
	
	public void resize(int width, int height){
		super.resize(width, height);
		map.screenResized();
		map.sortEntitiesForRendering();
	}
}
