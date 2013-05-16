package nl.wouter.Tribe.map;

import java.util.Random;


public class PerlinNoise2D {
	private int seed = new Random().nextInt();
	
	private float noise(int x, int y){
		int i = x + y * 57;
		i += seed;
		
		i = (i << 13) ^ i;
		return (1.0f - ((i * (i * i * 15731 + 789221) + 1376312589) & 0x7fffffff) / 1073741824.0f);
	}
	
	
	private float interpolate(float a, float b, float x){
		float ft = (float) (x * Math.PI);
		float i = (float) ((1 - Math.cos(ft)) * .5);
		
		return a * (1 - i) + (b * i);
	}
	
	private float interpolatedNoise(float x, float y){
		int intX = (int) x;
		float fracX = x - intX;
		
		int intY = (int) y;
		float fracY = y - intY;
		
		float a = noise(intX, intY);
		float b = noise(intX + 1, intY);
		float c = noise(intX, intY + 1);
		float d = noise(intX + 1, intY + 1);
		
		float interpolateA = interpolate(a, b, fracX);
		float interpolateB = interpolate(c, d, fracX);
		
		return interpolate(interpolateA, interpolateB, fracY);
	}
	
	/**
	 * @param x x coordinaat
	 * @param y y coordinaat
	 * @param persistence - hoe "grof" de map is
	 * @param zoomlevel - hoever de map ingezoomd is
	 * @param numberOctaves - hoeveel detail er is
	 * @return een float tussen -1 en 1
	 */
	public float perlinNoise(int x, int y, float persistence, float zoomlevel, int numberOctaves){
		float result = 0;
		
		for(int i = 0; i < numberOctaves; i++){
			float frequency = (float) (1 << i);
			double amplitude = Math.pow(persistence, i);
			
			result += interpolatedNoise(x / zoomlevel * frequency, y / zoomlevel * frequency) * amplitude;
		}
		
		if(result < -1.0f) result = -1.0f;
		if(result > 1.0f) result = 1.0f;
		
		return result;
	}
	
	
	
	/*
	 * public static void main(String[] args) throws IOException{ boolean
	 * running = false;
	 * 
	 * do{ seed = new Random().nextInt();
	 * 
	 * running = false;
	 * 
	 * BufferedImage result = new BufferedImage(1280, 960,
	 * BufferedImage.TYPE_INT_RGB); //BufferedImage result = new
	 * BufferedImage(512, 512, BufferedImage.TYPE_INT_RGB);
	 * 
	 * float persistence = 0.4f; int numberOctaves = 4; float zoomlevel = 64f;
	 * 
	 * for(int x = 0; x < result.getWidth(); x++){ for(int y = 0; y <
	 * result.getHeight(); y++){ float value = perlinNoise(x, y, persistence,
	 * zoomlevel, numberOctaves);
	 * 
	 * int color = (int) (value * 128); color = color << 22;
	 * 
	 * result.setRGB(x, y, color); } }
	 * 
	 * //ImageIO.write(result, "PNG", new File("output.png"));
	 * 
	 * int option = JOptionPane.showConfirmDialog(null, null, "jisdfrawe",
	 * JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, new
	 * ImageIcon(result));
	 * 
	 * if(option == JOptionPane.OK_OPTION) running = true; }while(running); }
	 */
}
