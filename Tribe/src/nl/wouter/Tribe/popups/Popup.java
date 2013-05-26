package nl.wouter.Tribe.popups;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import nl.wouter.Tribe.Images;
import nl.wouter.Tribe.screen.Screen;

public abstract class Popup {
	private Screen screen;
	private Sprite[][] gui;
	private TextureRegion[][] pixelsRegion;
	private Sprite[][] pixels;
	
	public Popup(Screen screen){
		this.setScreen(screen);
		pixelsRegion = Images.split(Images.gui[1][1], Images.gui[1][1].getRegionWidth(), Images.gui[1][1].getRegionHeight());;
		gui = new Sprite[3][3];
		pixels = new Sprite[pixelsRegion.length][pixelsRegion[0].length];
		for(int x = 0; x < 3; x++){
			for(int y = 0; y < 3; y++){
				gui[x][y] = new Sprite(Images.gui[x][y]);
			}
		}
		for(int x = 0; x < pixelsRegion.length; x++){
			for(int y = 0; y < pixelsRegion[x].length; y++){
				pixels[x][y] = new Sprite(pixelsRegion[x][y]);
			}
		}
	}
	
	public abstract void render(SpriteBatch batch);
	public abstract void update(int mouseX, int mouseY);
	public abstract void onLeftClick(int mouseX, int mouseY);
	public abstract boolean isInPopup(int x, int y);
	
	protected void drawBox(SpriteBatch batch, int width, int height, int screenX, int screenY){
		int imageWidth = Images.gui[0][0].getRegionWidth();
		int imageHeight = Images.gui[0][0].getRegionHeight();
		//renderen achtergrond
		
		int xDone = imageWidth / 2, yDone = imageHeight / 2;
		boolean flag = true;
		for(double x = 0.5; x < width / imageWidth - 1; x++){
			xDone += imageWidth;
			for(double y = 0.5; y < height / imageHeight - 1; y++){
				if(flag) yDone += imageHeight;
				gui[1][1].setPosition((float)(screenX + x * imageWidth), (float)(screenY + y * imageHeight));
				gui[1][1].draw(batch);
			}
			flag = false;
		}
		
		for(int x = xDone, xPixel = 0; x < width - imageWidth / 2; x++ , xPixel ++){
			for(int y = imageHeight / 2, yPixel = 0; y < height - imageHeight / 2; y++, yPixel++){
				pixels[xPixel % imageWidth][yPixel % imageHeight].setPosition(x + screenX, y + screenY);
				pixels[xPixel % imageWidth][yPixel % imageHeight].draw(batch);
			}
		}
		for(int y = yDone, yPixel = 0; y < height - imageHeight / 2; y++ , yPixel ++){
			for(int x = imageWidth / 2, xPixel = 0; x < width - imageWidth / 2; x++, xPixel++){
				pixels[xPixel % imageWidth][yPixel % imageHeight].setPosition(x + screenX, y + screenY);
				pixels[xPixel % imageWidth][yPixel % imageHeight].draw(batch);
			}
		}
		//renderen rand linkerbovenhoek
		for(int x = 0; x < width / imageWidth; x++){
			for(int y = 0; y < height / imageHeight; y++){
				int imageX = x, imageY = y;
				if(x > 0) imageX = 1;
				if(y > 0) imageY = 1;
				if(imageX == 1 && imageY == 1) continue;
				gui[imageX][imageY].setPosition(screenX + x * imageWidth, screenY + y * imageHeight);
				gui[imageX][imageY].draw(batch);
			}
		}
		for(int x = 0; x < width / imageWidth; x++){
			int imageX = x, imageY = 2;
			if(x > 0) imageX = 1;
			gui[imageX][imageY].setPosition(screenX + x * imageWidth, screenY + height - imageHeight);
			gui[imageX][imageY].draw(batch);
		}
		for(int y = 0; y < height / imageHeight ; y++){
			int imageX = 2, imageY = y;
			if(y > 0) imageY = 1;
			gui[imageX][imageY].setPosition(screenX + width - imageWidth, screenY + y * imageHeight);
			gui[imageX][imageY].draw(batch);
		}
		gui[2][2].setPosition(screenX + width - imageWidth, screenY + height - imageHeight);
		gui[2][2].draw(batch);
}

	public Screen getScreen() {
		return screen;
	}

	public void setScreen(Screen screen) {
		this.screen = screen;
	}
}
