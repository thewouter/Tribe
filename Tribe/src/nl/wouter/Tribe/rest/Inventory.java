package nl.wouter.Tribe.rest;

import nl.wouter.Tribe.multiplayer.host.MPHost;
import nl.wouter.Tribe.screen.GameScreen;


/**
 * this is a inventory for the GameScreen || MPHost object
 * @author wouter
 */
public class Inventory {
	private GameScreen owner;
	private InventoryPopup popup;
	private int gold = 0;
	private int meat = 0;
	private int wood = 0;
	private int stone = 0;
	private int vegetables = 0;
	private int ironOre = 0;
	private int iron = 0;
	
	public Inventory(GameScreen owner){
		this.owner = owner;
	}
	
	public Inventory(MPHost mpHost) {
		
	}

	public void showInventory() {
		popup = new InventoryPopup(owner, this);
		owner.setPopup(popup);
	}

	public int getGold() {
		return gold;
	}

	public void addGold(int gold) {
		this.gold += gold;
		
	}

	public int getMeat() {
		return meat;
	}

	public void addMeat(int meat) {
		this.meat += meat;
	}

	public int getWood() {
		return wood;
	}

	public void addWood(int wood) {
		this.wood += wood;
	}

	public int getStone() {
		return stone;
	}

	public void addStone(int stone) {
		this.stone += stone;
		//new Exception(this.stone + "").printStackTrace();
	}

	public int getVegetables() {
		return vegetables;
	}

	public void addVegetables(int vegetables) {
		this.vegetables += vegetables;
	}

	public int getIronOre() {
		return ironOre;
	}

	public void addIronOre(int ironOre) {
		this.ironOre += ironOre;
	}

	public int getIron() {
		return iron;
	}

	public void addIron(int iron) {
		this.iron += iron;
	}

}
