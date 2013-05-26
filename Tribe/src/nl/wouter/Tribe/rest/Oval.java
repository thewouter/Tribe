package nl.wouter.Tribe.rest;

import nl.wouter.Tribe.RTSComponent;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Oval {
	private int xPos;
	private int yPos;
	private int width;
	private int height;
	private MeshHelper mesh;

	public Oval(int x1, int y1, int x2, int y2) {
		this.width = x2 - x1;
		this.height = y2 - y1;
		mesh = new MeshHelper();
		float[] vertices = new float[361 * 2];
		for(int i = 0; i < 361 ; i+=1){
			vertices[2*i] = (float) Math.cos(Math.PI / 180f * i) * width / Gdx.graphics.getWidth();
			vertices[2*i+1] = (float) Math.sin(Math.PI / 180f * i) * height / Gdx.graphics.getHeight();
		}
		mesh.createMesh(vertices);
		mesh.translateInPixels(- Gdx.graphics.getWidth() / 4 + (x2 - x1) /4, - Gdx.graphics.getHeight() / 4 + (y2 - y1) / 4);
		setPosition(x1, y1);
	}
	
	public void setPosition(int x, int y){
		if(xPos == x && yPos == y) return;
		mesh.translateInPixels(-xPos, -yPos);
		mesh.translateInPixels(x, y);
		xPos = x;
		yPos = y;
	}
	
	public void render(SpriteBatch batch){
		mesh.drawMesh(batch);
	}
	
	public void dispose(){
		mesh.dispose();
	}

}
