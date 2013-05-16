package nl.wouter.Tribe.menubar;

import java.awt.image.BufferedImage;

import com.badlogic.gdx.graphics.Texture;

import nl.wouter.Tribe.Images;
import nl.wouter.Tribe.map.Direction;
import nl.wouter.Tribe.map.entities.Entity;
import nl.wouter.Tribe.rest.MousePointer;
import nl.wouter.Tribe.rest.Util;
import nl.wouter.Tribe.screen.GameScreen;

public abstract class MenuBarPopupBuildButton extends MenuBarPopupButton{
	private int ID;
	private int[] extraInfoOne;
	
	public static MenuBarPopupBuildButton TentI = new MenuBarPopupBuildButton(Images.buttons[5][6].getTexture(), 201, new int[]{1, 2}) {
		public String getName() {
			return "Tent I";
		}
	};	
	public static MenuBarPopupBuildButton Barracks = new MenuBarPopupBuildButton(Images.buttons[3][5].getTexture(), 211, new int[]{0}) {
		public String getName() {
			return "Barracks";
		}
	};	
	public static MenuBarPopupBuildButton StoneDefenseTower = new MenuBarPopupBuildButton(Images.buttons[6][5].getTexture(), 212, new int[]{0}) {
		public String getName() {
			return "Stone defense tower";
		}
	};
	public static MenuBarPopupBuildButton WoodenWall = new MenuBarPopupBuildButton(Images.buttons[4][5].getTexture(), 213, new int[]{0}) {
		public String getName() {
			return "Wooden Wall";
		}
	};
	public static MenuBarPopupBuildButton WoodenGate = new MenuBarPopupBuildButton(Images.buttons[5][5].getTexture(), 214, new int[]{0}) {
		public String getName() {
			return "Wooden Gate";
		}
	};
	public static MenuBarPopupBuildButton StoneMine = new MenuBarPopupBuildButton(Images.buttons[4][6].getTexture(), 206, new int[]{0}) {
		public String getName() {
			return "Stonemine";
		}
	};
	public static MenuBarPopupBuildButton TentII = new MenuBarPopupBuildButton(Images.buttons[2][7].getTexture(), 201, new int[]{1, 4}) {
		public String getName() {
			return "TentII";
		}
	};
	public static MenuBarPopupBuildButton Tree = new MenuBarPopupBuildButton(Images.buttons[6][7].getTexture(), 202, new int[]{0}) {
		public String getName() {
			return "Tree";
		}
	};
	public static MenuBarPopupBuildButton Remove = new MenuBarPopupBuildButton(Images.buttons[0][7].getTexture(), 0, new int[]{0}) {
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
	public MenuBarPopupBuildButton(Texture i, int ID, int[] extraInfoOne) {
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
				return Util.getEntity(screen.getMap(), screen, ID, Util.getMapX(input.mouseX - screen.getMap().translationX, input.mouseY - screen.getMap().translationY), Util.getMapY(input.mouseX - screen.getMap().translationX	, input.mouseY - screen.getMap().translationY), extraInfoOne, direction);
			}
		});
	}

	
	

}
