package nl.wouter.Tribe.menubar;

import java.util.LinkedList;

import nl.wouter.Tribe.Images;
import nl.wouter.Tribe.InputHandler;
import nl.wouter.Tribe.screen.GameScreen;

public class StatusBar extends nl.wouter.Tribe.menubar.MenuBar{
	public InputHandler input;
	public static int X_POS_FROM_RIGHT = 5, Y_POS_FROM_BOTTOM = 5, WIDTH_BUTTON = 16, HEIGHT_BUTTON = Images.buttons[0][0].getRegionHeight(), UITLOOP = 5, EXTRA_WIDTH = 30,EXTRA_HEIGHT = 30;
	public GameScreen screen;
	LinkedList <Button> buttons = new LinkedList<Button>();
	int xPosOnScreen = 0, yPosOnScreen = 0;
	public boolean showPopup = false;
	
	
	
	public StatusBar(InputHandler input, GameScreen screen) {
		super(input, screen, X_POS_FROM_RIGHT, Y_POS_FROM_BOTTOM, WIDTH_BUTTON, HEIGHT_BUTTON, 1);
		this.input = input;
		this.screen = screen;
		
		
		addButton(new MenubarTextField(this) {
			public String getText() {
				return "LvL";
			}
		});
		addButton(new MenubarTextField(this) {
			public String getText() {
				return new String(" " + bar.screen.level);
			}
		});
		
		addButton(new MenubarTextField(this) {
			public String getText() {
				return "gold :";
			}
		});
		
		addButton(new MenubarTextField(this) {public String getText() {return "";}}); // 4 empty characters
		
		addButton(new MenubarTextField(this) {
			public String getText() {
				return new String("" + StatusBar.this.screen.inventory.getGold());
			}
		});
		
		addButton(new MenubarTextField(this) {public String getText() {return "";}}); // 4 empty characters
		
		
	}
}
