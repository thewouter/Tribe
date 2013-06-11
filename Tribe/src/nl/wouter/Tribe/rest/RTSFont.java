package nl.wouter.Tribe.rest;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class RTSFont {
	public static final int HEIGHT = 12;
	public static final String FONT_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;,{}\"´`'<>";
	FreeTypeFontGenerator generator;
	BitmapFont font ;
	private String fileName = "alterebro-pixel-font.ttf";
	
	public RTSFont(){
		generator= new FreeTypeFontGenerator(Gdx.files.internal("data/" + fileName));
		font = generator.generateFont(HEIGHT + 4, FONT_CHARACTERS, true);
		generator.dispose();
	}
	
	public void drawLine(SpriteBatch batch, String text, int xPos, int yPos){
		font.drawMultiLine(batch, text, xPos, yPos);
	}
	
	public void drawLine(SpriteBatch batch, String text, int xPos, int yPos, Color color){
		font.setColor(color);
		drawLine(batch, text, xPos, yPos);
	}
	
	public void drawLineAndShadow(SpriteBatch batch, String text, int xPos, int yPos, Color shadowColor){
		Color oldColor = batch.getColor();
		drawLine(batch, text, xPos + 1, yPos + 1, shadowColor);
		drawLine(batch, text, xPos, yPos, oldColor);
	}
	
	public void drawBoldLine(SpriteBatch batch, String text, int xPos, int yPos, Color backgroundColor){
		Color oldColor = batch.getColor();
		
		font.setColor(backgroundColor);
		drawLine(batch, text, xPos + 1, yPos);
		drawLine(batch, text, xPos, yPos + 1);
		drawLine(batch, text, xPos - 1, yPos);
		drawLine(batch, text, xPos, yPos - 1);
		
		font.setColor(oldColor);
		drawLine(batch, text, xPos, yPos);
	}
	
	public int getLineWidth(String text){
		int width = 0;
		
		for(int i = 0; i < text.length(); i++){
			width += 5;
		}
		
		return width;
	}
}
