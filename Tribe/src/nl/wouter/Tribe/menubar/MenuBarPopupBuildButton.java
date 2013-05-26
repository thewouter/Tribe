package nl.wouter.Tribe.menubar;

import java.awt.image.BufferedImage;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import nl.wouter.Tribe.Images;
import nl.wouter.Tribe.map.Direction;
import nl.wouter.Tribe.map.MousePointer;
import nl.wouter.Tribe.map.entities.Entity;
import nl.wouter.Tribe.rest.Util;
import nl.wouter.Tribe.screen.GameScreen;

public abstract class MenuBarPopupBuildButton extends MenuBarPopupButton{
	private int ID;
	private int[] extraInfoOne;
	
	public static MenuBarPopupBuildButton TentI = new MenuBarPopupBuildButton(new Sprite(Images.buttons[5][6]), 201, new int[]{1, 2}) {
		public String getName() {
			return "Tent I";
		}
	};	
	public static MenuBarPopupBuildButton Barracks = new MenuBarPopupBuildButton(new Sprite(Images.buttons[3][5]), 211, new int[]{0}) {
		public String getName() {
			return "Barracks";
		}
	};	
	public static MenuBarPopupBuildButton StoneDefenseTower = new MenuBarPopupBuildButton(new Sprite(Images.buttons[6][5]), 212, new int[]{0}) {
		public String getName() {
			return "Stone defense tower";
		}
	};
	public static MenuBarPopupBuildButton WoodenWall = new MenuBarPopupBuildButton(new Sprite(Images.buttons[4][5]), 213, new int[]{0}) {
		public String getName() {
			return "Wooden Wall";
		}
	};
	public static MenuBarPopupBuildButton WoodenGate = new MenuBarPopupBuildButton(new Sprite(Images.buttons[5][5]), 214, new int[]{0}) {
		public String getName() {
			return "Wooden Gate";
		}
	};
	public static MenuBarPopupBuildButton StoneMine = new MenuBarPopupBuildButton(new Sprite(Images.buttons[4][6]), 206, new int[]{0}) {
		public String getName() {
			return "Stonemine";
		}
	};
	public static MenuBarPopupBuildButton TentII = new MenuBarPopupBuildButton(new Sprite(Images.buttons[2][7]), 201, new int[]{1, 4}) {
		public String getName() {
			return "TentII";
		}
	};
	public static MenuBarPopupBuildButton Tree = new MenuBarPopupBuildButton(new Sprite(Images.buttons[6][7]), 202, new int[]{0}) {
		public String getName() {
			return "Tree";
		}
	};
	public static MenuBarPopupBuildButton Remove = new MenuBarPopupBuildButton(new Sprite(Images.buttons[0][7]), 0, new int[]{0}) {
		public void onLeftClick(GameScreen screen){
			screen.setPointer(new MousePointer(screen.getMap(), screen.input, screen) {
				public Entity toBuild(Direction face) {
					return null;
				}
				public void afterBuild(){
					for(Entity e:screen.selectedEntities){
						map.removeEntity(e);
					}
				}
			});
		}
		public String getName() {
			return "remove";
		}
	};
	public MenuBarPopupBuildButton(Sprite i, int ID, int[] extraInfoOne) {
		super(i);
		this.extraInfoOne = extraInfoOne;
		this.ID = ID;
	}

	public void onLeftClick(GameScreen screen){
		screen.setPointer(new MousePointer(screen.getMap(), screen.input, screen) {
			public Entity toBuild(Direction face) {
				int direction = 1;
				if(face != Direction.SOUTH_EAST){
					direction = 2;
				}
				Entity e = null;
				try{
					e =  Util.getEntity(screen.getMap(), screen, ID, Util.getMapX(input.mouseX - screen.getMap().translationX, input.mouseY - screen.getMap().translationY), Util.getMapY(input.mouseX - screen.getMap().translationX	, input.mouseY - screen.getMap().translationY), extraInfoOne, direction);
				}catch(Exception ex){
					ex.printStackTrace();
				}
				return e;
			}
		});
	}

	
	

}
