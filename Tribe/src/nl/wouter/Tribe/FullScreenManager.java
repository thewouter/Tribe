package nl.wouter.Tribe;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Window;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public class FullScreenManager {
	private RTSComponent component;
	private GraphicsDevice graphicsDevice;
	private Container container;
	
	public FullScreenManager(RTSComponent component, Container container){
		this.component = component;
		this.container = container;
		GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
		graphicsDevice = e.getDefaultScreenDevice();
	}
	
	public void setFullScreen(){
		/*JFrame frame = new JFrame();
		frame.setTitle("Tribe (fullscreen)");
		frame.setUndecorated(true);
		frame.setIgnoreRepaint(true);
		frame.setResizable(false);
		frame.setLayout(new BorderLayout(0, 0));
		frame.add(component, BorderLayout.CENTER);
		graphicsDevice.setFullScreenWindow(frame);
		
		//component.requestFocus();
		container.setVisible(false);*/
	}
	
	public void update(){/*
		Window w = graphicsDevice.getFullScreenWindow();
		
		if(w != null){
			if(w.getBufferStrategy() == null){
				w.setVisible(true);
				w.createBufferStrategy(2);
			}
			BufferStrategy s = w.getBufferStrategy();
			
			if(!s.contentsLost()){
				s.show();
			}
		}*/
	}
	
	public void restoreScreen(){/*
		Window w = graphicsDevice.getFullScreenWindow();
		
		if(w != null) w.dispose();
		graphicsDevice.setFullScreenWindow(null);
		
		if(container == null) System.out.println("null");
		container.add(component, BorderLayout.CENTER);
		component.requestFocus();
		container.setVisible(true);*/
	}
	
	public void switchFullScreen(){/*
		if(graphicsDevice.getFullScreenWindow() == null) setFullScreen();
		else restoreScreen();*/
	}
	
	public boolean isFullScreen(){
		return graphicsDevice.getFullScreenWindow() != null;
	}
}
