package nl.wouter.Tribe;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Tribe";
		cfg.useGL20 = true;
		cfg.width = 600;
		cfg.height = 600;
		cfg.forceExit = true;
		
		new LwjglApplication(new RTSComponent(), cfg);
	}
}
