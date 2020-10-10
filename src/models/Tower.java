package models;

import constants.TowerType;

public class Tower {

	private double firingRange;
	private double firingRate;
	private int damage;
	private int cost;
	private TowerType type;
	private int intRow;
	private int intCol;
	private double row;
	private double col;
	
	public Tower(String t, int row, int col) {
		this.type = TowerType.valueOf(t);
		this.firingRange = type.getFiringRange();
		this.firingRate = type.getFiringRate();
		this.damage = type.getDamage();
		this.cost = type.getCost();
		this.row = row * 75 + 37.5;
		this.col = col * 75 + 37.5;
		this.intRow = row;
		this.intCol = col;
	}

	/**
	 * @return the damage
	 */
	public int getDamage() {
		return damage;
	}

	public double getRow() { return row; }

	public double getCol() { return col; }

	public int getIntRow() { return intRow; }

	public int getIntCol() { return intCol; }

	/**
	 * @return the firingRange
	 */
	public double getFiringRange() {
		return firingRange;
	}

	/**
	 * @return the firingRate
	 */
	public double getFiringRate() {
		return firingRate;
	}

	/**
	 * @return the cost
	 */
	public int getCost() {
		return cost;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type.name();
	}
	
	
	public String toString() {
		return type.toString();
	}
	
	public String getImage() {
		return type.getImage();
	}
	

	
}
