package nl.wouter.Tribe.popups.entitypopup;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import nl.wouter.Tribe.map.entities.Entity;
import nl.wouter.Tribe.map.entities.MovingEntity;
import nl.wouter.Tribe.rest.RTSFont;
import nl.wouter.Tribe.screen.GameScreen;
import nl.wouter.Tribe.screen.Screen;

public class EntityOptionsPopup extends EntityPopup {
	private final ArrayList<Option> options = new ArrayList<Option>();
	private int width, height = 0;
	int indexSelected = -1;
	int indexHighlighted = -1;
	int longestLine = 0;
	private boolean dimensionsSet = false;

	public EntityOptionsPopup(Entity owner, GameScreen screen, Option...options){
		super(owner, screen);
		for(int i = 0; i < options.length; i++){
			this.options.add(options[i]);
		}
	}
	
	
	public void render(SpriteBatch batch){
		if(!dimensionsSet) setDimensions();
		dimensionsSet = true;
		
		batch.setColor(Color.BLACK);
		
		drawBox(batch, width, height);
		
		for(int i = 0; i < options.size(); i++){
			if(i == indexSelected){
				options.get(i).renderInColor(batch, i, Color.BLUE);
			}else if(i == indexHighlighted){
				options.get(i).renderInColor(batch, i, Color.RED);
			}else{
				options.get(i).render(batch, i);
			}
		}
	}
	
	private void setDimensions(){
		for(int i = 0; i < options.size(); i++){
			int lineWidth = Screen.font.getLineWidth(options.get(i).getName());
			if(lineWidth > width) width = lineWidth;
		}
		
		height = RTSFont.HEIGHT * (options.size())+EMPTY_SPACE;
	}


	public void update(int mouseX, int mouseY){
		
		if(isInPopup(mouseX, mouseY)){
			indexHighlighted = (mouseY  - 16 - getScreenY())/RTSFont.HEIGHT;
		}else{
			indexHighlighted = -1;
		}
		
		
	}
	
	public void addOption(Option option){
		options.add(option);
		int lineWidth = Screen.font.getLineWidth(option.getName());
		if(lineWidth > longestLine) {
			longestLine = lineWidth;
			width = longestLine + EMPTY_SPACE;
		}
		dimensionsSet = false;
	}
	
	public boolean isInPopup(int mouseX, int mouseY){
		if(mouseX > getScreenX() && mouseX < getScreenX() + width && mouseY > getScreenY() && mouseY < getScreenY() + height){
			return true;
		}
		return false;
	}
	
	public Option getOption(int index){
		if(index < options.size() && index >= 0) return options.get(index);
		else return null;
	}
	
	public void onLeftClick(int mouseX, int mouseY) {
		//System.out.println("onLeftClick");
		if(isInPopup(mouseX, mouseY)){
			indexSelected = (mouseY - 16 - getScreenY())/RTSFont.HEIGHT;
			if(getOption(indexSelected) != null){	
				getOption(indexSelected).onClick();
			}
			if(owner instanceof MovingEntity)
				((MovingEntity) owner).setSelectedOption(indexSelected);
		}else{
			indexSelected = -1;
		}
	}
}
