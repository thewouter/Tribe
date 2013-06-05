package nl.wouter.Tribe.screen;

import java.awt.Point;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import nl.wouter.Tribe.InputHandler;
import nl.wouter.Tribe.RTSComponent;
import nl.wouter.Tribe.multiplayer.host.Player;
import nl.wouter.Tribe.popups.screenpopup.ScreenPopup;
import nl.wouter.Tribe.rest.RTSFont;

/**
 * Een Screen staat voor een deel van het spel, zoals het hoofdmenu, of game
 * scherm
 */
public abstract class Screen implements com.badlogic.gdx.Screen {
	public RTSComponent component;
	public static RTSFont font = new RTSFont();
	public InputHandler input;
	public ScreenPopup popup = null;
	private SpriteBatch batch;
	public OrthographicCamera camera;
	
	public Screen(RTSComponent component, InputHandler input){
		this.component = component;
		this.input = input;
		batch = new SpriteBatch();
		Gdx.gl20.glDisable(GL20.GL_BLEND);
		Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl20.glDepthMask(true);
	}
	
	public void setScreen(Screen screen){
		component.setScreen(screen);
	}
	
	public void render(float delta){
		Gdx.gl20.glClearColor(0, 0, 0, 0.5f);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.enableBlending();
		batch.begin();
		render(batch, camera);
		
		if(popup != null){
			popup.render(batch);
		}
		
		font.drawLine(batch, "" + (int) (1 / delta), 10, 10, Color.WHITE);
		batch.end();
	}
	
	public abstract void render(SpriteBatch batch, OrthographicCamera camera);
	
	public void update(){
		if(input.escape.isTapped()) {
			if(popup == null)component.setTitleScreen();
			else if(!popup.isForced()) setPopup(null);
		}
		if(popup!= null) {
			popup.update(input.getMouseX(), input.getMouseY());
			input.update();
		}
	}
	
	/** @param transparancy hoe transparant, max 1.0, min 0.0 */
	public void makeTransparant(SpriteBatch batch, float transparancy){
		Color c = batch.getColor();
		batch.setColor(c.r, c.b, c.g, transparancy);
	}
	
	public Point getMouseLocation(){
		return new Point(input.getMouseX(), input.getMouseY());
	}
	
	public int getWidth(){
		return Gdx.graphics.getWidth() / RTSComponent.SCALE;
	}
	
	public int getHeight(){
		return Gdx.graphics.getHeight() / RTSComponent.SCALE;
	}
	
	public void setPopup(ScreenPopup popup){
		this.popup = popup;
	}
	
	public void quit(){}

	public void resize(int width, int height){
		this.camera = new OrthographicCamera(getWidth(), getHeight());
		float zoom = 1/ (float)(RTSComponent.SCALE);
		camera.setToOrtho(true);
		camera.zoom = zoom;
		camera.position.x = Gdx.graphics.getWidth() / ( 2 * RTSComponent.SCALE);
		camera.position.y = Gdx.graphics.getHeight() / ( 2 * RTSComponent.SCALE);
	}

	public void show() {
		
	}

	public void hide() {
	}

	public void resume() {
	}

	public void dispose() {
		batch.dispose();
	}
	
	public void pause(){
	}
}
