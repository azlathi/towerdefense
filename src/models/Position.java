package models;

import java.util.ArrayList;
import java.util.List;

import exceptions.InvalidPlacementException;
import exceptions.TooExpensiveException;

public class Position {

	private int row;
	private int col;
	private boolean isBase;
	private boolean isBlocked;
	private boolean hasTower;
	private Tower tower;
	private List<Enemy> enemies;
	
	public Position(int row, int col) {
		this.row = row;
		this.col = col;
		this.isBase = false;
		this.isBlocked = false;
		this.hasTower = false;
		this.tower = null;
		this.enemies = new ArrayList<Enemy>();
	}
	
	public boolean setBlocked() {
		isBlocked = true;
		return true;
	}
	
	public boolean setBase() {
		if (isBase)	return false;
		isBase = true;
		return true;
	}

	public boolean setTower(Tower t, int currency)
			throws InvalidPlacementException, TooExpensiveException {
		if (hasTower || isBlocked || isBase) throw new 
		InvalidPlacementException(
				"Cannot place " + t + " on (" + row + "," + col + ").");
		if (t.getCost() > currency) throw new TooExpensiveException();
		this.tower = t;
		hasTower = true;
		return true;
	}
	
	public Tower removeTower() {
		Tower ret = tower;
		tower = null;
		hasTower = false;
		return ret;
	}

	/**
	 * @return the row
	 */
	public int getRow() {
		return row;
	}


	/**
	 * @return the col
	 */
	public int getCol() {
		return col;
	}


	/**
	 * @return the isBase
	 */
	public boolean isBase() {
		return isBase;
	}


	/**
	 * @return the isBlocked
	 */
	public boolean isBlocked() {
		return isBlocked;
	}


	/**
	 * @return the hasTower
	 */
	public boolean hasTower() {
		return hasTower;
	}


	/**
	 * @return the tower
	 */
	public Tower getTower() {
		return tower;
	}


	/**
	 * @return the enemies
	 */
	public List<Enemy> getEnemies() {
		return enemies;
	}

}
