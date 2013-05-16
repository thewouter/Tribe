package nl.wouter.Tribe.map.entities.players;

public class Bow extends Weapon {
	private static int ID = 633;
	
	public Bow(Soldier owner) {
		super(owner, ID);
		MIN_HIT_RANGE = 10;
		MAX_HIT_RANGE = 12;
		LOAD_TIME = 150;
		owner.addSoldierComponent(new AlertComponent(owner, MAX_HIT_RANGE));
	}
	
	public void activate() {
		owner.map.shootArrow(owner, owner.target, true, 15, MAX_HIT_RANGE);
	}
}
