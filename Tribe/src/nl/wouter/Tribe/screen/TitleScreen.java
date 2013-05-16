package nl.wouter.Tribe.screen;

import java.util.Random;

import nl.wouter.Tribe.Images;
import nl.wouter.Tribe.InputHandler;
import nl.wouter.Tribe.RTSComponent;
import nl.wouter.Tribe.map.tiles.Tile;
import nl.wouter.Tribe.screen.menus.MainMenu;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TitleScreen extends Screen{
	private int totalTime;
	private TextureRegion grass;
	private TextureRegion grass2;
	private Sprite logo;
	private float logoTransparancy = 0.2f; //hoe transparant het logo is, max. 1.0
	private boolean[][] tiles = new boolean[64][64];
	
	public TitleScreen(RTSComponent game, InputHandler input){
		super(game, input);
		grass2 = Images.terrain[0][0];
		grass = Images.terrain[1][0];
		TextureRegion r = new TextureRegion(Images.load("Pictures/logo2.png"));
		logo = new Sprite(r);
		Random random = new Random();
		
		for(int x = 0; x < tiles.length; x++){
			for(int y = 0; y < tiles[x].length; y++){
				boolean b = random.nextBoolean();
				tiles[x][y] = b;
			}
		}
	}
	
	public void render(SpriteBatch batch, OrthographicCamera camera){
		for(int x = 0; x < tiles.length; x++){
			for(int y = 0; y < tiles[x].length; y++){
				if(tiles[x][y]){
					batch.draw(grass, x * Tile.WIDTH, y * Tile.HEIGHT);
					batch.draw(grass, x * Tile.WIDTH - Tile.WIDTH / 2, y * Tile.HEIGHT - Tile.HEIGHT / 2);
				}else{
					batch.draw(grass2, x * 32, y * 16);
					batch.draw(grass2, x * 32 - 16, y * 16 - 8);
				}
			}
			
		}
		//super.makeTransparant(g, logoTransparancy);
		logo.draw(batch);
	}
	
	public void update(){
		totalTime++;
		if(totalTime > 200 && !(component.getScreen() instanceof MainMenu)) setScreen(new MainMenu(component, this, input));
		
		boolean switchScreen = false;
		
		if(logoTransparancy < 1.0f){
			logoTransparancy += (float) RTSComponent.MS_PER_TICK / 2000;
		}else{
			logoTransparancy = 1.0f;
			switchScreen = true;
		}
		if(switchScreen){
			if(!(component.getScreen() instanceof MainMenu)) setScreen(new MainMenu(component, this, input));
		}
	}
	
	public void resize(int width, int height){
		super.resize(width, height);
		logo.setPosition(getWidth() / 2 - logo.getWidth() / 2, 30);
	}
}
