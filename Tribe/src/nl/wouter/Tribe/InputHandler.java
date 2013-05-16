package nl.wouter.Tribe;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.LinkedList;

import nl.wouter.Tribe.RTSComponent;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

public class InputHandler implements InputProcessor {
	private ArrayList<Key> keylist = new ArrayList<Key>();
	public LinkedList<Key> inputKeys = new LinkedList<Key>();

	public Key left = new Key("", Input.Keys.LEFT, Input.Keys.A);
	public Key fullScreen = new Key("", Input.Keys.F11);
	public Key right = new Key("", Input.Keys.RIGHT, Input.Keys.D);
	public Key up = new Key("", Input.Keys.UP, Input.Keys.W);
	public Key down = new Key("", Input.Keys.DOWN, Input.Keys.S);
	public Key use = new Key("", Input.Keys.ENTER);
	public Key enter = new Key("", Input.Keys.ENTER);
	public Key space = new Key(" ", Input.Keys.SPACE);
	public Key escape = new Key("", Input.Keys.ESCAPE);
	public Key increase = new Key("", Input.Keys.PAGE_UP, Input.Keys.E);
	public Key decrease = new Key("", Input.Keys.PAGE_DOWN, Input.Keys.Q);
	public Key debug = new Key("", Input.Keys.F3);
	public Key a = new Key ("a", Input.Keys.A);
	public Key b = new Key ("b", Input.Keys.B);
	public Key c = new Key ("c", Input.Keys.C);
	public Key d = new Key ("d", Input.Keys.D);
	public Key e = new Key ("e", Input.Keys.E);
	public Key f = new Key ("f", Input.Keys.F);
	public Key g = new Key ("g", Input.Keys.G);
	public Key h = new Key ("h", Input.Keys.H);
	public Key i = new Key ("i", Input.Keys.I);
	public Key j = new Key ("j", Input.Keys.J);
	public Key k = new Key ("k", Input.Keys.K);
	public Key l = new Key ("l", Input.Keys.L);
	public Key m = new Key ("m", Input.Keys.M);
	public Key n = new Key ("n", Input.Keys.N);
	public Key o = new Key ("o", Input.Keys.O);
	public Key p = new Key ("p", Input.Keys.P);
	public Key q = new Key ("q", Input.Keys.Q);
	public Key r = new Key ("r", Input.Keys.R);
	public Key s = new Key ("s", Input.Keys.S);
	public Key t = new Key ("t", Input.Keys.T);
	public Key u = new Key ("u", Input.Keys.U);
	public Key v = new Key ("v", Input.Keys.V);
	public Key w = new Key ("w", Input.Keys.W);
	public Key x = new Key ("x", Input.Keys.X);
	public Key y = new Key ("y", Input.Keys.Y);
	public Key z = new Key ("z", Input.Keys.Z);
	public Key n1 = new Key ("1", Input.Keys.NUM_1);
	public Key n2 = new Key ("2", Input.Keys.NUM_2);
	public Key n3 = new Key ("3", Input.Keys.NUM_3);
	public Key n4 = new Key ("4", Input.Keys.NUM_4);
	public Key n5 = new Key ("5", Input.Keys.NUM_5);
	public Key n6 = new Key ("6", Input.Keys.NUM_6);
	public Key n7 = new Key ("7", Input.Keys.NUM_7);
	public Key n8 = new Key ("8", Input.Keys.NUM_8);
	public Key n9 = new Key ("9", Input.Keys.NUM_9);
	public Key n0 = new Key ("0", Input.Keys.NUM_0);
	public Key backspace = new Key ("backspace", Input.Keys.BACKSPACE);
	public Key dot = new Key (".", Input.Keys.PERIOD, 65);
	//public Key lessThan = new Key ("<", Input.Keys.LEFT_BRACKET);
	//public Key moreThan = new Key (">", Input.Keys.GREATER);
	public Key comma = new Key (",", Input.Keys.COMMA);
	
	private boolean LMBDown = false, LMBWasDown = false;
	private boolean RMBDown = false, RMBWasDown = false;
	private boolean isDragging = false, wasDragging = false;
	public int mouseX, mouseY, mouseWheelChange, mouseXOnClick, mouseYOnClick;
	private char charTyped;
	
	private InputHandler(){
		//Only InputHandler can initialize an InputHandler object
		inputKeys.add(a);
		inputKeys.add(b);
		inputKeys.add(c);
		inputKeys.add(d);
		inputKeys.add(e);
		inputKeys.add(f);
		inputKeys.add(g);
		inputKeys.add(h);
		inputKeys.add(i);
		inputKeys.add(j);
		inputKeys.add(k);
		inputKeys.add(l);
		inputKeys.add(m);
		inputKeys.add(n);
		inputKeys.add(o);
		inputKeys.add(p);
		inputKeys.add(q);
		inputKeys.add(r);
		inputKeys.add(t);
		inputKeys.add(s);
		inputKeys.add(u);
		inputKeys.add(v);
		inputKeys.add(w);
		inputKeys.add(x);
		inputKeys.add(y);
		inputKeys.add(z);
		inputKeys.add(space);
		inputKeys.add(n0);
		inputKeys.add(n1);
		inputKeys.add(n2);
		inputKeys.add(n3);
		inputKeys.add(n4);
		inputKeys.add(n5);
		inputKeys.add(n6);
		inputKeys.add(n7);
		inputKeys.add(n8);
		inputKeys.add(n9);
		inputKeys.add(dot);
	}
	
	public void update(){
		LMBWasDown = LMBDown;
		RMBWasDown = RMBDown;
		mouseWheelChange = 0;
		wasDragging = isDragging;
		
		for(Key key: keylist){
			key.update();
		}
		
		charTyped = KeyEvent.CHAR_UNDEFINED;
	}
	
	public boolean typedChar(){
		return charTyped != KeyEvent.CHAR_UNDEFINED;
	}
	
	public char getCharTyped(){
		return charTyped;
	}
	
	public boolean LMBDown(){
		return LMBDown;
	}
	
	public boolean LMBTapped(){
		return !LMBDown && LMBWasDown;
	}
	
	public boolean RMBDown(){
		return RMBDown;
	}
	
	public boolean RMBTapped(){
		return !RMBDown && RMBWasDown;
	}
	
	public boolean isDragging(){
		return isDragging;
	}
	
	public boolean wasDragging(){
		return wasDragging;
	}
	
	public int getMouseX(){		//coordinats in mapcoordinats!
		return mouseX;
	}
	
	public int getMouseY(){		//coordinats in mapcoordinats!
		return mouseY;
	}
	
	public int getMouseWheelChange(){
		return mouseWheelChange;
	}

	public boolean keyDown(int keycode) {
		for(Key key: keylist){
			key.press(keycode);
		}
		
		return true;
	}

	public boolean keyUp(int keycode) {
		for(Key key:keylist){
			key.release(keycode);
		}
		return true;
	}

	public boolean keyTyped(char character) {
		return false;
	}

	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if(button == Input.Buttons.LEFT)LMBDown = true;
		else RMBDown = true;
		return true;
	}

	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if(button == Input.Buttons.LEFT) LMBDown = false;
		else RMBDown = false;
		isDragging = false;
		return true;
	}

	public boolean touchDragged(int screenX, int screenY, int pointer) {
		mouseX = screenX / RTSComponent.SCALE;
		mouseY = screenY / RTSComponent.SCALE;
		if(!isDragging()){
			mouseYOnClick = mouseY;
			mouseXOnClick = mouseX;
		}
		if(LMBDown()) isDragging = true;
		return true;
	}

	public boolean mouseMoved(int screenX, int screenY) {
		mouseX = screenX / RTSComponent.SCALE;
		mouseY = screenY / RTSComponent.SCALE;
		return true;
	}

	public boolean scrolled(int amount) {
		mouseWheelChange += amount;
		return true;
	}
	
	public static InputHandler getInputHandler(){
		InputHandler input = new InputHandler();
		return input;
	}
	
	public class Key {
		private final int[] keys;
		private boolean pressed = false, wasPressed = false;
		private int timePressed = 0;
		private String chars;
		
		public Key(String chars,int...keys){
			this.chars = chars;
			this.keys = keys;
			keylist.add(this);
		}
		
		public void update(){
			wasPressed = pressed;
			
			if(pressed) timePressed++;
			else timePressed = 0;
		}
		
		public void press(int key){
			if(has(key)) pressed = true;
		}
		
		public void release(int key){
			if(has(key)) pressed = false;
		}
		
		/**
		 * @return for how long the key has been pressed, in ticks
		 */
		public double getTimePressed(){
			return timePressed;
		}
		
		public boolean has(int key){
			for(int i = 0; i < keys.length; i++){
				if(keys[i] == key) return true;
			}
			return false;
		}
		
		public boolean isTapped(){
			return pressed && !wasPressed;
		}
		
		public boolean isPressed(){
			return pressed;
		}
		
		public String getChars(){
			return chars;
		}
	}
}
