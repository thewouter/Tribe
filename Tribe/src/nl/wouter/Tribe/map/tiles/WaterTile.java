package nl.wouter.Tribe.map.tiles;

import java.awt.Dimension;
import java.awt.Point;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class WaterTile extends Tile {
	protected WaterTile(int id, boolean solid){
		super(id, solid);
	}
	
	public void render(SpriteBatch batch, Dimension screenSize, Point translation, Point position){
		super.render(batch, screenSize, translation, position);
	}
}
