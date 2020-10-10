/**
 * @author: Arjun Lathi, Zhaoyi Yang, Brandon Erickson
 * FILE: EnemyType.java
 * PURPOSE: Holds attributes for each enemy type
 */

package constants;

public enum EnemyType {

	//(health, damage, speed, bounty, image)
	KNIGHT(500,10,50,75, Constants.KNIGHT), //average
	ASSASSIN(100,25,250, 100,Constants.ASSASSIN), //fast, high damage, low hp
	BRUTE(750,25,50,150,Constants.BRUTE), //step above knight
	GIANT(2000, 50,20,250,Constants.GIANT), //tanky slow, medium high damage
	CONQUISTADOR(750,50,100,300, Constants.CONQUISTADOR), //high speed, high damage, medium health
	TANK(15000,999,5,1000,Constants.TANKENEMY);

	private int health; // initial hp
	private int damage; // damage on base hit
	private int speed;  // the ticks it takes to move to the next position aka lower is faster
	private int bounty; // the money to get on kill
	private String image;// the image that represents the enemy

    
	private EnemyType(int health, int damage, int speed, int bounty, String image) {
		this.health = health;
		this.damage = damage;
		this.bounty = bounty;
		this.speed = speed;
		this.image = image;
	}


	/**
	 * @return the health
	 */
	public int getHealth() {
		return health;
	}

	/**
	 * @return the damage
	 */
	public int getDamage() {
		return damage;
	}

	/**
	 * @return the speed
	 */
	public int getSpeed() {
		return speed;
	}

	/**
	 * @return the image
	 */
	public String getImage() {
		return image;
	}

	/**
	 * @return the bounty
	 */
	public int getBounty() {
		return bounty;
	}
	
}
