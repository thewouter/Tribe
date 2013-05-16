package nl.wouter.Tribe.rest;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import javazoom.jl.player.advanced.AdvancedPlayer;


public class MP3 {
    private final ArrayList<String> fileNames = new ArrayList<String>();
    private AdvancedPlayer player; 
    private boolean isLooping = false;
    private boolean isPlaying = false;

    // constructor that takes the name of an MP3 file
    public MP3(String fileName) {
    	if(fileName.startsWith("src")){
    		fileName = fileName.split("c", 2)[1];
    		System.out.println(fileName);
    	}
        fileNames.add(fileName);
    }
    
    public MP3(ArrayList<String> fileNames){
    	for(String s:fileNames){
    		if(s.startsWith("src")){
    			s = s.split("c", 2)[1];
    		}
    	}
    	this.fileNames.addAll(fileNames);
    }

    private void close() {
    	if(player != null)player.close();
    }
    
    // play the MP3 file to the sound card
    public void play() {
    	isPlaying = true;
    	new Thread(){
    		public void run(){
    			for(String s:fileNames){
    				if(!isPlaying) return;
    				try{
	    				InputStream in = getClass().getResourceAsStream(s);
	    				BufferedInputStream bis = new BufferedInputStream(in);
	    				player = new AdvancedPlayer(bis);
						player.play();
					} catch (Exception e) {
						e.printStackTrace();
					}
    				
    			}
    		}
    	}.start();
    }
    
    // loop the MP3 file to the sound card
    public void loop(){
    	isPlaying = true;
    	new Thread(){
    		public void run(){
    			for(String s:fileNames){
    				if(!isPlaying) return;
    				try{
	    				InputStream in = new FileInputStream(s);
	    				BufferedInputStream bis = new BufferedInputStream(in);
	    				player = new AdvancedPlayer(bis);
						player.play();
					} catch (Exception e) {
						e.printStackTrace();
					}
    				
    			}
    			if(isLooping) loop();
    		}
    	}.start();
    }
    
    public void stop(){
    	close();
    	isLooping = false;
    	isPlaying = false;
    }
    
    public int getLenght(){
    	float durationInSeconds = 0;
    	for(String s: fileNames){
	    	File file = new File(s);
		    AudioInputStream audioInputStream = null;
			try {
				audioInputStream = AudioSystem.getAudioInputStream(file);
			} catch (Exception e) {e.printStackTrace();}
			
		    AudioFormat format = audioInputStream.getFormat();
		    long audioFileLength = file.length();
		    int frameSize = format.getFrameSize();
		    float frameRate = format.getFrameRate();
	    	durationInSeconds += (audioFileLength / (frameSize * frameRate));
    	}
	    return (int) durationInSeconds;
    }
}
