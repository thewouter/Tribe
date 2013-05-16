package nl.wouter.Tribe.menubar;

import nl.wouter.Tribe.Images;
import nl.wouter.Tribe.InputHandler;
import nl.wouter.Tribe.popups.screenpopup.ScreenPopup;
import nl.wouter.Tribe.popups.screenpopup.ScreenPopupButton;
import nl.wouter.Tribe.popups.screenpopup.ScreenPopupTextField;
import nl.wouter.Tribe.popups.screenpopup.TextInput;
import nl.wouter.Tribe.screen.GameScreen;
import nl.wouter.Tribe.screen.MPGameScreen;


public class HomeBar extends MenuBar{
		public static int X_POS_FROM_RIGHT = 5, Y_POS_FROM_BOTTOM = 5, WIDTH_BUTTON = 16, HEIGHT_BUTTON = 16;
	
		public MenuBarPopup buildmenu;
		
		public HomeBar(InputHandler input, GameScreen gamescreen) {
			super(input, gamescreen, X_POS_FROM_RIGHT, Y_POS_FROM_BOTTOM, WIDTH_BUTTON, HEIGHT_BUTTON, 3);
			
			buildmenu = new MenuBarPopup((GameScreen)screen, xPosOnScreen + UITLOOP + (buttons.indexOf(this) + 1)*WIDTH_BUTTON, yPosOnScreen + UITLOOP, buttons.indexOf(this), false);
			addButton(new Button(Images.buttons[0][0], this) {
				public void onLeftClick() {
					setMenuBarPopup(buildmenu);
					showPopup = !showPopup;
				}
			});
			
			addButton(new Button(Images.buttons[1][1], this){
				public void onLeftClick() {
					MenuBarPopup popup = new MenuBarPopup((GameScreen)screen, xPosOnScreen + UITLOOP + (buttons.indexOf(this) + 1)*WIDTH_BUTTON, yPosOnScreen + UITLOOP, buttons.indexOf(this), false);
					popup.addButton(new MenuBarPopupButton(Images.buttons[2][0].getTexture()) {

						public void onLeftClick(GameScreen screen) {
							ScreenPopup options = new ScreenPopup(bar.screen.getWidth() / 2 - 50, bar.screen.getHeight() / 2 - 50, 85, 1, bar.screen, false);
							options.addPart(new ScreenPopupTextField("set music"));
							options.addPart(new ScreenPopupButton("on", options, bar.input) {
								public void onLeftClick() {
									this.owner.screen.component.amplifybackground();
								}
							});
							options.addPart(new ScreenPopupButton("off", options, bar.input) {
								public void onLeftClick() {
									this.owner.screen.component.muteBackground();
								}
							});
							options.addPart(new ScreenPopupButton("ok", options, bar.input) {
								public void onLeftClick() {
									this.owner.screen.setPopup(null);
								}
							});
							bar.screen.setPopup(options);
						}

						public String getName() {
							return "Sound";
						}
					});
					setMenuBarPopup(popup);
					showPopup = !showPopup;
				}
			});
			
			addButton(new Button(Images.buttons[0][1], this){
				public void onLeftClick() {
					MenuBarPopup popup = new MenuBarPopup(screen, xPosOnScreen + UITLOOP + (buttons.indexOf(this) + 1)*WIDTH_BUTTON, yPosOnScreen + UITLOOP, buttons.indexOf(this), false);
					popup.addButton(new MenuBarPopupButton(Images.buttons[1][0].getTexture()) {
						public void onLeftClick(GameScreen screen) {
							ScreenPopup popup = new ScreenPopup((bar.screen.getWidth() - 100)/2, (bar.screen.getHeight() - 100)/2, 100, 100, bar.screen, false);
							popup.addPart(new TextInput(popup, bar.input));
							popup.addPart(new ScreenPopupButton("load", popup, bar.input){
								public void onLeftClick() {
									bar.screen.load(owner.getTextInput(1).getOutput());
									bar.screen.setPopup(null);
								}
							});
							popup.addPart(new ScreenPopupButton("cancel", popup, bar.input) {
								public void onLeftClick() {
									bar.screen.setPopup(null);
								}
							});
							bar.screen.setPopup(popup);
							
						}

						public String getName() {
							return "Load";
						}
						
					});
					
					popup.addButton(new MenuBarPopupButton(Images.buttons[2][0].getTexture()) {
						public void onLeftClick(GameScreen screen) {
							ScreenPopup popup = new ScreenPopup((screen.getWidth() - 100)/2, (screen.getHeight() - 100)/2, 100, 100, screen, false);
							popup.addPart(new TextInput(popup, bar.input));
							popup.addPart(new ScreenPopupButton("Save", popup, bar.input){
								public void onLeftClick() {
									((GameScreen)owner.screen).save(owner.getTextInput(1).getOutput());
									((GameScreen)owner.screen).setPopup(null);
								}
							});
							popup.addPart(new ScreenPopupButton("cancel", popup, bar.input) {
								public void onLeftClick() {
									((GameScreen)owner.screen).setPopup(null);
								}
							});
							screen.setPopup(popup);
						}

						public String getName() {
							return "Save";
						}
					});
					
					setMenuBarPopup(popup);
					showPopup = !showPopup;
				}
			});
			
			addButton(new Button(Images.buttons[3][0], this) {
				public void onLeftClick() {
					this.bar.screen.component.setTitleScreen();
					if(this.bar.screen instanceof MPGameScreen){
						((MPGameScreen) this.bar.screen).quit();
					}
				}
			});
			
		}
}
