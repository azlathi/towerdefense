package models;

import java.util.LinkedList;

import constants.EnemyType;
import javafx.animation.PathTransition;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

public class Enemy {
	// ALL row and col present Position in Pixel.
	private int health;
	private int damage;
	private int speed;
	private String image;
	private EnemyType type;
	private double startRow;
	private double startCol;
	private double endRow;
	private double endCol;
	private double fromRow;
	private double fromCol;
	private double toRow;
	private double toCol;
	private LinkedList<Double> rowPath;
	private LinkedList<Double> colPath;
	int id;
	private boolean isSpawn;
	private boolean isFrozen;

	public Enemy(String t) {
		this.type = EnemyType.valueOf(t);
		this.health = type.getHealth();
		this.damage = type.getDamage();
		this.speed = type.getSpeed();
		this.image = type.getImage();
		this.id = 0;
		this.fromRow = 998;
		this.fromCol = 998;
		this.toRow = 999;
		this.toCol = 999;
		this.rowPath = new LinkedList<Double>();
		this.colPath = new LinkedList<Double>();
		this.isSpawn = false;
		this.isFrozen = false;
	}

	public void initialposition(LinkedList<Position> enemyPath) {
		this.startRow = enemyPath.getFirst().getRow() * 75 + 75;
		this.startCol = enemyPath.getFirst().getCol() * 75 + 37.5;
		this.endRow = enemyPath.getLast().getRow() * 75 + 75;
		this.endCol = enemyPath.getLast().getCol() * 75 + 37.5;
		this.fromRow = startRow;
		this.fromCol = startCol;
		this.toRow = fromRow;
		this.toCol = fromCol;
		for (Position position : enemyPath) {
			rowPath.add(position.getRow() * 75 + 75.0);
			colPath.add(position.getCol() * 75 + 37.5);
		}
	}

	/**
	 * @return the damage
	 */
	public int getDamage() {
		return damage;
	}

	public void removeHP(int amount) {
		health -= amount;
	}

	/**
	 * @return the speed
	 */
	public double getSpeed() {
		return speed;
	}

	/**
	 * @return the health
	 */
	public double getHealth() {
		return health;
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
		return image;
	}

	public boolean unFreeze() {

		return true;
	}

	public void move() {
		// If arrived the end of this "part of path"
		// update a new End Position
		double tempRow = toRow;
		double tempCol = toCol;
		for (int i = 0; i < (this.speed * 1.0 / 25); i++) {
			if (toRow != endRow || toCol != endCol) {
				moveOnce();
			}
		}
		fromRow = tempRow;
		fromCol = tempCol;
	}

	public void moveOnce() {
		fromRow = toRow;
		fromCol = toCol;
		if (fromRow == rowPath.getFirst() && fromCol == colPath.getFirst()) {
			rowPath.remove(0);
			colPath.remove(0);
		}
		// move up/down/left/right
		if (fromRow == rowPath.getFirst() && fromCol < colPath.getFirst()) {
			toRow = fromRow;
			toCol = fromCol + 1;
		} else if (fromRow == rowPath.getFirst()
				&& fromCol > colPath.getFirst()) {
			toRow = fromRow;
			toCol = fromCol - 1;
		} else if (fromCol == colPath.getFirst()
				&& fromRow < rowPath.getFirst()) {
			toRow = fromRow + 1;
			toCol = fromCol;
		} else if (fromCol == colPath.getFirst()
				&& fromRow > rowPath.getFirst()) {
			toRow = fromRow - 1;
			toCol = fromCol;
		}
	}

	public double getFromRow() {
		return this.fromRow;
	}

	public double getFromCol() {
		return this.fromCol;
	}

	public double getToRow() {
		return this.toRow;
	}

	public double getToCol() {
		return this.toCol;
	}

	public void setID(int i) {
		this.id = i;
	}

	public int getID() {
		return this.id;
	}

	public double getEndRow() {
		return this.endRow;
	}

	public double getEndCol() {
		return this.endCol;
	}

	public boolean setSpawn(boolean spawn) {
		this.isSpawn = spawn;
		return true;
	}

	public boolean setFrozen(boolean frozen) {
		this.isFrozen = frozen;
		return true;
	}

	public boolean isFrozen() {
		return isFrozen;
	}

	public boolean isSpawn() {
		return isSpawn;
	}

}
