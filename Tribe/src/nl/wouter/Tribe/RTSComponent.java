package nl.wouter.Tribe;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

import nl.wouter.Tribe.FullScreenManager;
import nl.wouter.Tribe.InputHandler;
import nl.wouter.Tribe.multiplayer.host.MPHost;
import nl.wouter.Tribe.rest.Sound;
import nl.wouter.Tribe.screen.SPGameScreen;
import nl.wouter.Tribe.screen.TitleScreen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class RTSComponent extends Game {

	public static final double MS_PER_TICK = 1000.0 / 60.0;
	private SpriteBatch batch;
	private Texture texture;
	private Sprite sprite;
	private SPGameScreen gameScreen;
	private TitleScreen titleScreen;
	boolean running = true;
	public static final int SCALE = 2;
	private FullScreenManager fullScreenManager;
	private InputHandler input;
	long fps;
	private BufferedImage screenImage;
	private Sound backgroundSound;
	public boolean backgroundSoundOn = false;
	private String loginName = "Player";
	private float updateTimer = 0f;
	
	public void create() {
		input = InputHandler.getInputHandler();
		Gdx.input.setInputProcessor(input);
		loadBackgroundMusic();
		setTitleScreen();
		
	}

	public void dispose() {
		super.dispose();
		//batch.dispose();
		//texture.dispose();
	}

	public void render() {
		super.render();
		updateTimer += Gdx.graphics.getDeltaTime();
		while(updateTimer > MS_PER_TICK / 1000){
			updateTimer -= MS_PER_TICK / 1000;
			update();
		}
	}

	private void update() {
		((nl.wouter.Tribe.screen.Screen)getScreen()).update();
		input.update();
	}

	public void resize(int width, int height) {
		super.resize(width, height);
	}

	public void pause() {
		super.pause();
	}

	public void resume() {
		super.resume();
	}
	
	public void stop(){
		running = false;
		//Options.window_width = container.getSize().width;
		//Options.window_height = container.getSize().height;
		//Options.startFullScreen = fullScreenManager.isFullScreen();
		//Options.SOUND_ON = backgroundSoundOn;
		
		//Options.writeOptions();
	}
	
	
	public void setScreen(Screen s){
		if(s instanceof SPGameScreen && backgroundSoundOn){
			backgroundSound.play();
		}else{
			backgroundSound.stop();
		}
		if(getScreen() instanceof MPHost){
			((MPHost) getScreen()).quit();
		}
		super.setScreen(s);
	}
	
	public void setGameScreen(boolean newScreen){
		System.out.println(newScreen);
		if(gameScreen == null || newScreen == true) {
			gameScreen = null;
			gameScreen = new SPGameScreen(this, input);
		}
		setScreen(gameScreen);
		
		if(backgroundSoundOn) backgroundSound.loop();
	}
	
	public void setTitleScreen(){
		if(titleScreen == null) {
			titleScreen = new TitleScreen(this, input);
		}
		setScreen(titleScreen);
		backgroundSound.stop();
	}
	
	public void setHostedGame(int port){
		setScreen(new MPHost(this, input, port));
	}
	
	public void muteBackground(){
		backgroundSound.stop();
		backgroundSoundOn = false;
	}
	
	public void amplifybackground(){
		if(!backgroundSoundOn){
			backgroundSoundOn = true;
			backgroundSound.play();
		}
	}
	
	private void loadBackgroundMusic(){
		final ArrayList<String> fileNames = new ArrayList<String>();
		
		File theDir = new File("music");
		if(!theDir.exists()){
			System.out.println("creating directory: music");
			theDir.mkdir();
		}
		Path startDir = Paths.get("music");
		String pattern = "*.mp3";
		FileSystem fs = FileSystems.getDefault();
		
		final PathMatcher matcher = fs.getPathMatcher("glob:" + pattern);
		
		FileVisitor<Path> matcherVisitor = new SimpleFileVisitor<Path>() {
		    public FileVisitResult visitFile(Path file, BasicFileAttributes attribs) {
		        Path name = file.getFileName();
		        if (matcher.matches(name)) {
		            fileNames.add(file.toString());
		        }
		        return FileVisitResult.CONTINUE;
		    }
		};
		try {
			Files.walkFileTree(startDir, matcherVisitor);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		backgroundSound = new Sound(fileNames);
		
	}
	
	public boolean isMember(String name){
		try {
			URL source = new URL("http://www.wouter.byethost15.com/users.nothin"); //super secret database of registered users!
			InputStreamReader input = new InputStreamReader(source.openStream());
			BufferedReader in = new BufferedReader(input);
			String inputLine;
			while((inputLine = in.readLine())!= null){
				if(name.equals(inputLine)) return true;
				//System.out.println(inputLine);
			}
		} catch (FileNotFoundException e) {
			return true;
		} catch (MalformedURLException e) {
			return true;
		} catch (IOException e) {
			return true;
		}
		return false;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
}
