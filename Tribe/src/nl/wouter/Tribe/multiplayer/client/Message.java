package nl.wouter.Tribe.multiplayer.client;

public class Message {
	private final String message;
	private int lifetimeLeft;
	private Chat chat;
	private String playerName;
	
	public Message(String message, String playerName, int lifetimeInTicks, Chat chat) {
		this.message = message;
		lifetimeLeft = lifetimeInTicks;
		this.chat = chat;
		this.playerName = playerName;
	}
	
	public void update(){
		lifetimeLeft --;
		if(lifetimeLeft <= 0){
			chat.removeMessage(this);
		}
	}
	
	public String getMessage(){
		return playerName + ":  " + message;
	}

}
