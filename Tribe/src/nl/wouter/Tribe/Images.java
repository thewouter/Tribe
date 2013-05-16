package nl.wouter.Tribe;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Images {
	public static TextureRegion[][] terrain = split(load("Pictures/terrain3.png"), 16, 16);
	public static TextureRegion[][] gui = split(load("Pictures/gui.png"), 3, 3);
	public static TextureRegion structures = new TextureRegion(load("Pictures/structures.png"));
	public static TextureRegion font = new TextureRegion(load("Pictures/font.png"));
	public static TextureRegion[][] sheep = split(load("Pictures/animations/sheep.png"),7,1);
	public static TextureRegion[][] player = split(load("Pictures/animations/playersmall.png"),8,1);
	public static TextureRegion[][] flag = split(load("Pictures/animations/MoveFlag.png"),6,1);
	public static TextureRegion[][] buttons = split(load("Pictures/16x16buttons.png"),8,8);
	public static TextureRegion mines = new TextureRegion(load("Pictures/mines.png"));
	public static TextureRegion[][] dudes = split(load("Pictures/dudes.png"),4,4);
	public static TextureRegion school = new TextureRegion(load("Pictures/school.png"));
	public static TextureRegion[][] smallButtons = split(load("Pictures/8x8buttons.png"),10,10);

	public static TextureRegion load(String fileName){
		Texture t = new Texture(Gdx.files.internal(fileName));
		TextureRegion r = new TextureRegion(t);
		r.flip(false, true);
		return r;
	}

	public static TextureRegion[][] split(TextureRegion region, int xAmount, int yAmount){
		region.flip(false, true);
		
		TextureRegion[][] r = region.split(region.getRegionWidth() / xAmount, region.getRegionHeight() / yAmount);
		for(TextureRegion[] x: r){
			for(TextureRegion y: x){
				y.flip(false, true);
			}
		}
		TextureRegion[][] result = new TextureRegion[r[0].length][r.length];
		
		for(int x = 0; x < r.length; x++){
			for(int y = 0; y < r[0].length; y++){
				result[y][x] = r[x][y];
			}
		}
		
		return result;
	}
	
	public static TextureRegion[] split(TextureRegion image, int xAmount){
		return image.split(image.getRegionWidth() / xAmount, image.getRegionHeight())[1];
	}
}
