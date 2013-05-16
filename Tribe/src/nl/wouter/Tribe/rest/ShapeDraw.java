package nl.wouter.Tribe.rest;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ShapeDraw {

	public void update(){
		
	}
	
	public static void fillRoundRect(SpriteBatch batch, int x, int y, int width, int height, int rounding, Color color){
		Pixmap map = new Pixmap(width, height, Format.RGBA8888);
		map.setColor(color);
		map.fillRectangle(rounding, 0, width - 2 * rounding, height);
		map.fillRectangle(0, rounding, width, height - 2 * rounding);
		map.fillCircle(rounding, rounding, rounding);
		map.fillCircle(width - rounding, rounding, rounding);
		map.fillCircle(rounding, height - rounding, rounding);
		map.fillCircle(width - rounding, height - rounding, rounding);
		Texture t = new Texture(map);
		TextureRegion r = new TextureRegion(t);
		r.flip(false, true);
		Sprite s = new Sprite(t);
		s.setPosition(x, y);
		s.draw(batch);
		map.dispose();
		t.dispose();
	}
	
	public static void fillRect(SpriteBatch batch, int x, int y, int width, int height, Color color){
		Pixmap map = new Pixmap(width, height, Format.RGBA8888);
		map.setColor(color);
		map.fillRectangle(0, 0, width, height);
		Texture t = new Texture(map);
		TextureRegion r = new TextureRegion(t);
		r.flip(false, true);
		Sprite s = new Sprite(t);
		s.setPosition(x, y);
		s.draw(batch);
	}
	
	public static void drawRect(SpriteBatch batch, int x, int y, int width, int height, Color color){
		Pixmap map = new Pixmap(width, height, Format.RGBA8888);
		map.setColor(color);
		map.drawRectangle(0, 0, width, height);
		Texture t = new Texture(map);
		TextureRegion r = new TextureRegion(t);
		r.flip(false, true);
		Sprite s = new Sprite(r);
		s.setPosition(x, y);
		s.draw(batch);
	}

}
