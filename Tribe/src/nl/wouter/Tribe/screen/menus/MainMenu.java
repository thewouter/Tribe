package nl.wouter.Tribe.screen.menus;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import nl.wouter.Tribe.InputHandler;
import nl.wouter.Tribe.RTSComponent;
import nl.wouter.Tribe.popups.screenpopup.ScreenPopup;
import nl.wouter.Tribe.popups.screenpopup.ScreenPopupButton;
import nl.wouter.Tribe.popups.screenpopup.TextInput;
import nl.wouter.Tribe.screen.MPGameScreen;
import nl.wouter.Tribe.screen.Screen;
import nl.wouter.Tribe.screen.TitleScreen;

public class MainMenu extends MenuScreen {
	private Screen title;
	private float buttonTransparancy;
	
	private MenuButton startGame = new MenuButton("Start Game", 0, 100, -1, -1, this);
	private MenuButton newSPGame = new MenuButton("New Game", 0, 130, -1, -1, this);
	private MenuButton newMPGame = new MenuButton("Join MP Game", 0, 160, -1, -1, this);
	private MenuButton hostMPGame = new MenuButton("Host MP Game", 0, 190, -1, -1, this);
	private MenuButton exitGame = new MenuButton("Exit Game", 0, 220, -1, -1, this);
	
	public MainMenu(RTSComponent component, TitleScreen title, InputHandler input){
		super(component, input);
		this.title = title;
		
		ScreenPopup popup = new ScreenPopup((getWidth()-100)/2, (getHeight() - 100)/2, 100, 100, this, true);
		TextInput userNameInput = new TextInput(popup,input);
		userNameInput.setText("name");
		popup.addPart(userNameInput);
		popup.addPart(new ScreenPopupButton("OK", popup, input) {
			
			public void onLeftClick() {
				String username = owner.getTextInput(1).getOutput();
				try{
				if(!owner.screen.component.isMember(username)) {
					owner.screen.component.stop();
				}else {
					setPopup(null);
					owner.screen.component.setLoginName(username);
				}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
		if(title.component.getLoginName().equals("Player"))setPopup(popup);
	}
	
	public void render(SpriteBatch batch, OrthographicCamera camera){
		title.render(batch, camera);
		super.makeTransparant(batch, buttonTransparancy);
		super.render(batch, camera);
		
		
	}
	
	protected MenuButton[] getButtons(){
		MenuButton[] result = {startGame, newSPGame, newMPGame, exitGame, hostMPGame};
		return result;
	}
	
	public void update(){
		super.update();
		if(buttonTransparancy < 1.0f){
			buttonTransparancy += (float) RTSComponent.MS_PER_TICK / 1000;
		}
		if(buttonTransparancy > 1.0f){
			buttonTransparancy = 1.0f;
		}
	}
	
	public void resize(int width, int height){
		super.resize(width, height);
		title.resize(width, height);
	}
	
	public void buttonPressed(MenuButton menuButton){
		if(menuButton.equals(startGame))super.component.setGameScreen(false);
		else if(menuButton.equals(exitGame)) component.stop();
		else if(menuButton.equals(newSPGame)) component.setGameScreen(true);
		else if(menuButton.equals(newMPGame)){
			ScreenPopup popup = new ScreenPopup((getWidth() / 2 )- 50, (getHeight() / 2) - 50, 100, 100, title, false);
			TextInput IP = new TextInput(popup, input);
			IP.setText("localhost");
			popup.addPart(IP);
			TextInput port = new TextInput(popup, input);
			port.setText("1995");
			popup.addPart(port);
			popup.addPart(new ScreenPopupButton("OK", popup, input) {
				public void onLeftClick() {
					String ip = owner.getTextInput(1).getOutput();
					String port = owner.getTextInput(2).getOutput();
					try{ 
						owner.screen.component.setScreen(new MPGameScreen(owner.screen.component, input, Integer.parseInt(port), ip));
					}catch(Exception e){
						owner.getTextInput(2).clear();
						owner.getTextInput(2).setText("invalid");
						System.out.println(e);
					}
				}
			});
			setPopup(popup);
		}
		else if(menuButton.equals(hostMPGame)) component.setHostedGame(1995);
	}
}
