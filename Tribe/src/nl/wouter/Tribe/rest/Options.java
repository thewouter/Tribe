package nl.wouter.Tribe.rest;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Options {
	public static int window_width = 800, window_height = 600;
	public static boolean startFullScreen = false, SOUND_ON;
	public static String name = "Name";
	
	public static String fileName = "options.dat";
	
	/** maakt options.dat met alle variabelen */
	public static void writeOptions(){
		File file = new File(fileName);
		
		try{
			FileOutputStream file_output = new FileOutputStream(file);
			DataOutputStream data_out = new DataOutputStream(file_output);
			
			data_out.writeInt(window_width);
			data_out.writeInt(window_height);
			data_out.writeBoolean(startFullScreen);
			data_out.writeBoolean(SOUND_ON);
			
			file_output.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/** herlaad options.dat en vervangt variabelen */
	
	public static void loadOptions(){
		File file = new File(fileName);
		
		try{
			
			//hiermee kun je date op files schrijven
			FileInputStream file_input = new FileInputStream(file);
			DataInputStream data_in = new DataInputStream(file_input);
			
			try{
				window_width = data_in.readInt();
				window_height = data_in.readInt();
				startFullScreen = data_in.readBoolean();
				SOUND_ON = data_in.readBoolean();
				
			}catch(EOFException e){ //als hij bij het einde van de file is
				System.out.println("end of file");
			}
			
			data_in.close();
		}catch(Exception e){
			System.out.println("Error! : " + e);
		}
	}
}
