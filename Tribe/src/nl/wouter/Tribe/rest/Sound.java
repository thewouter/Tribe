package nl.wouter.Tribe.rest;

import java.util.ArrayList;


public class Sound {
       private MP3 mp3;
       
        public Sound(String...file){
        	ArrayList<String> files = new ArrayList<String>();
        	for(String s:file){
        		files.add(s);
        	}
        	mp3 = new MP3(files);
        }
       
        public Sound(ArrayList<String> files) {
        	mp3 = new MP3(files);
		}

		public void play(){
        	mp3.play();
        	
        }
        
        public void isPlaying(){
        	
        }
        
        public void loop(){
            mp3.loop();
        }
        
        public void stop(){
        	mp3.stop();
        }
}