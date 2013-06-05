package nl.wouter.Tribe.map.structures;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;

import nl.wouter.Tribe.Images;
import nl.wouter.Tribe.map.Direction;
import nl.wouter.Tribe.map.Map;
import nl.wouter.Tribe.map.tiles.Tile;
import nl.wouter.Tribe.screen.GameScreen;
import nl.wouter.Tribe.screen.Screen;

public abstract class BasicStructure extends Structure {
	private Sprite image;
	private Sprite flippedImage;
	private Direction front;
	
	public BasicStructure(Map map, GameScreen screen, int xPos, int yPos, int textureX, int textureY, int ID, Direction front){
		super(map, screen, xPos, yPos, ID);
		this.setFront(front);
		loadImage(textureX, textureY);
	}
	
	public void render(SpriteBatch batch){
		if(getFront() != Direction.SOUTH_EAST){
			getImage().setPosition(getScreenX() - (Tile.WIDTH / 2) * (getSize() - 1), getScreenY() - (getHeadSpace() * Tile.HEIGHT) + (Tile.HEIGHT / 2) * (getSize() - 1));
			getImage().draw(batch);
		}
		else{
			flippedImage.setPosition(getScreenX() - (Tile.WIDTH / 2) * (getSize() - 1), getScreenY() - (getHeadSpace() * Tile.HEIGHT) + (Tile.HEIGHT / 2) * (getSize() - 1));
			flippedImage.draw(batch);
		}
		//Screen.font.drawLine(batch, uniqueNumber + "", getScreenX() + 10, getScreenY());
	}
	
	public void render(SpriteBatch batch, int x, int y){
		batch.draw(getImage(), x - (Tile.WIDTH / 2) * (getSize() - 1), y - (getHeadSpace() * Tile.HEIGHT) + (Tile.HEIGHT / 2) * (getSize() - 1));
		
	}
	
	protected void loadImage(int textureX, int textureY){
		int x = textureX * Tile.WIDTH;
		int y = textureY * Tile.HEIGHT;
		int width = getSize() * Tile.WIDTH;
		int height = (getSize() + getHeadSpace()) * Tile.HEIGHT;
		setImage(new Sprite(loadImage(x, y, width, height)));
		TextureRegion r = new TextureRegion(getImage());
		r.flip(true, false);
		flippedImage = new Sprite(r);
	}
	
	public Sprite getImage(){
		return image;
	}

	public Direction getFront() {
		return front;
	}

	public void setFront(Direction front) {
		this.front = front;
	}

	public void setImage(Sprite image) {
		this.image = image;
	}
}
