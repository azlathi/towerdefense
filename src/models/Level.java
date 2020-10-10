package models;

import java.util.*;

import constants.Constants;
import constants.EnemyType;
import constants.Stage;
import constants.TowerType;
import exceptions.InvalidPlacementException;
import exceptions.TooExpensiveException;
import messages.*;

public class Level extends Observable implements Runnable {

	private int rows;
	private int cols;
	private Position[][] board;
	private Position base;
	private LinkedList<Position> enemyPath;
	private Stage stage;
	private int currency;
	private Map<Integer, Enemy> enemies;
	private LinkedList<Integer> enemiesToRemove;
	private boolean paused;
	private int tick;
	private int tickRate;
	private int health;
	private int totalEnemies;
	private LinkedList<Tower> towers;

	public Level(String s) {
		startLevel(s);
	}
	public void restartLevel(String s) {
		startLevel(s);
		setChanged();
		notifyObservers(new RestartMessage());
	}

	private boolean startLevel(String s) {
		stage = Stage.valueOf(s);
		rows = stage.getRows();
		cols = stage.getCols();
		board = new Position[rows][cols];
		enemies = new TreeMap<>(stage.getEnemies());
		totalEnemies = enemies.size();
		enemiesToRemove = new LinkedList<>();
		towers = new LinkedList<>();
		paused = true;
		enemyPath = new LinkedList<>();
		currency = 1500;
		tick = 0;
		tickRate = 70;
		health = 100;
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				board[row][col] = new Position(row, col);
			}
		}
		base = board[stage.getBaseRow()][stage.getBaseCol()];
		base.setBase();
		// add all path in enemyPath.
		for (int i = 0; i < stage.getEnemyPathRows().length-1; i++) {
			enemyPath.add(board[stage.getEnemyPathRows()[i]][stage.getEnemyPathCols()[i]]);
		}
		return true;
	}

	/**
	 *
	 * @param row
	 * @param col
	 * @param t
	 * @return
	 * @throws InvalidPlacementException
	 */
	public boolean setPos(int row, int col, String t)
			throws InvalidPlacementException,
			TooExpensiveException {
		boolean pitfallValid = false;
		for (Position p : enemyPath) {
			if (p.getRow() == row && p.getCol() == col) {
				if (t.equals(Constants.TOWER5)) {
					pitfallValid = true;
				} else {
					throw new InvalidPlacementException();
				}
			}
		}
		if (!pitfallValid && t.equals(Constants.TOWER5)) throw new InvalidPlacementException();
		Tower newTower = new Tower(t, row, col);
		if (board[row][col].setTower(newTower, currency)) {
			towers.add(newTower);
			currency -= TowerType.valueOf(t).getCost();
			setChanged();
			notifyObservers(new AddTowerMessage(board[row][col].getTower(), row,
					col, currency));
			return true;
		}
		return false;
	}

	public boolean setTickRate(int rate) {
		tickRate = rate;
		return true;
	}

	public boolean removeTower(int row, int col) {
		Tower t = board[row][col].removeTower();
		if (t != null) {
			int i = towers.indexOf(t);
			currency += (t.getCost() * .5);
			towers.remove(t);
			setChanged();
			notifyObservers(new SellTowerMessage(row, col, t.getType()));
		}
		return true;
	}

	@Override
	public void run() {
		paused = false;
		while (!paused) {
			// for pos in board, call shoot
			// spawn enemy if on spawn tick
			spawnEnemy();
			shootTower();
			try {
				Thread.sleep(tickRate);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			tick++;
		}
	}

	public void spawnEnemy() {
		Iterator<Integer> iter = enemies.keySet().iterator();
		boolean removeEnemy = false;
		int removeKey = 0;
		while (iter.hasNext()) {
			int key = iter.next();
			Enemy enemy = enemies.get(key);
			enemy.setID(key);
			if (key <= tick || enemy.isSpawn()) {
				if (!enemy.isFrozen()) {
					enemy.move();
					enemy.setSpawn(true); //isSpawn = true
					if (enemy.getFromCol() == enemy.getEndCol() && enemy.getFromRow() == enemy.getEndRow()) {
						int damage = enemy.getDamage();
						removeEnemy = true;
						removeKey = key;
						health -= damage;
						if (health <= 0) {
							health = 0;
							setChanged();
							notifyObservers(); //END THE GAME / LOST THE GAME
						}
						setChanged();
						notifyObservers(new DamageBaseMessage(damage, base.getRow(), base.getCol()));
					}
				}
			}
		}
		if (removeEnemy) enemies.remove(removeKey);
		setChanged();
		notifyObservers(new EnemyMoveMessage(enemies));
	}

	public void shootTower() {
		for (Tower temp : towers) {
			double enemyCol = -1;
			double enemyRow = -1;
			if (tick % temp.getFiringRate() == 0) {
				boolean removeEnemy = false;
				int removeKey = 0;
				for (Integer key : enemies.keySet()) {
					Enemy e = enemies.get(key);
					if (inRange(temp.getRow(), temp.getCol(), e.getFromRow(), e.getFromCol(), temp.getFiringRange()) && e.isSpawn()) {
						if (!(e.getType().equals("TANK") && temp.getType().equals("PITFALL"))) e.removeHP(temp.getDamage());
						if (e.getHealth() <= 0) {
							removeEnemy = true;
							removeKey = key;
						}
						if (temp.getType().equals(Constants.TOWER5)) {
							board[temp.getIntRow()][temp.getIntCol()].removeTower();
							towers.remove(temp);
							setChanged();
							notifyObservers(new RemoveTowerMessage(temp.getIntRow(), temp.getIntCol()));
						} else if (temp.getType().equals(Constants.TOWER3)) {
							e.setFrozen(true);
							Thread unFreeze = new Thread(() -> {
								try {
									Thread.sleep(1000);
								} catch (InterruptedException ex) {
									ex.printStackTrace();
								}
								e.setFrozen(false);
							});
							unFreeze.start();
						}
						enemyCol = e.getFromCol();
						enemyRow = e.getFromRow();
						break;
					}
				}
				if (removeEnemy) {//gives a delay to wait for bullet to reach enemy
					if (!enemiesToRemove.contains(removeKey)) {
						enemiesToRemove.add(removeKey);
						Thread waitForBullet = new Thread(() -> {
							int key = enemiesToRemove.getLast();
							int sleepAmt = 0;
							if (temp.getType().equals(Constants.TOWER1) || temp.getType().equals(Constants.TOWER2)) {
								sleepAmt = 100;
							} else if (temp.getType().equals(Constants.TOWER3)) {
								sleepAmt = 250;
							} else if (temp.getType().equals(Constants.TOWER4)) {
								sleepAmt = 150;
							}
							try {
								Thread.sleep(sleepAmt);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							if (enemies.get(key) != null) {
								currency += EnemyType.valueOf(enemies.get(key).getType()).getBounty();
								setChanged();
								notifyObservers(new KilledEnemyMessage(enemies.get(key).getFromRow(), enemies.get(key).getFromCol(), EnemyType.valueOf(enemies.get(key).getType()).getBounty()));
								enemies.remove(key);
							}
						});
						waitForBullet.start();
					}
				}
			}
			setChanged();
			notifyObservers(new ShootBulletMessage(temp.getIntRow(), temp.getIntCol(), enemyRow, enemyCol, temp.getType()));
		}

	}

	private boolean inRange(double x1, double y1, double x2, double y2,
							double range) {
		return (Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1)) < (range + 30));
	}

	public void setEnemyStartPoint(double enemyBaseRow, double enemyBaseCol) {
		for (Enemy enemy : enemies.values()) {
			enemy.initialposition(enemyPath);
		}
	}
	public void setPause(boolean paused) {
		this.paused = paused;
	}

	/**
	 * @return the rows
	 */
	public int getRows() {
		return rows;
	}

	/**
	 * @return the cols
	 */
	public int getCols() {
		return cols;
	}

	/**
	 * @return the base
	 */
	public Position getBase() {
		return base;
	}

	/**
	 * @return the enemyPath
	 */
	public List<Position> getEnemyPath() {
		return enemyPath;
	}

	public int getHealth() { return health; }

	public String getStage() {
		return stage.name();
	}

	public int currentCurrency() {
		return currency;
	}

	public int getEnemySize() { return totalEnemies; }

	/**
	 *
	 * @param row
	 * @param col
	 * @return
	 */
	public Position getPos(int row, int col) {
		return board[row][col];
	}

}
