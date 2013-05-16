package nl.wouter.Tribe.menubar;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class Button {
	Sprite image;
	public MenuBar bar;
	
	public Button(TextureRegion buttons, MenuBar bar){
		this.bar = bar;
		if(buttons != null)
			image = new Sprite(buttons);
	}
	
	public abstract void onLeftClick();
	
	public void render(SpriteBatch batch, int xPos, int yPos){
		if(image.getOriginX() != xPos || image.getOriginY() != yPos){
			image.setPosition(xPos, yPos);
		}
		image.draw(batch);
	}
}
