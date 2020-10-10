package controller;

import java.util.List;
import java.util.Observer;

import exceptions.InvalidPlacementException;
import exceptions.TooExpensiveException;
import models.Level;
import models.Position;

public class TowerDefenseController {

	private boolean initialized;
	private Level currentLevel;
	private int level;
	private String[] stages = new String[]{"STAGE1","STAGE2","STAGE3","STAGE4"};
	private Observer obs;

	public TowerDefenseController(Observer obs) {
		this.obs = obs;
	}

	public boolean initialize(String stage, Observer obs) {
		if (initialized)
			return false;
		currentLevel = new Level(stage);
		currentLevel.addObserver(obs);
		initialized = true;
		return true;
	}

	public int getEnemySize() {
		return currentLevel.getEnemySize();
	}

	public boolean setPaused(boolean paused) {
		currentLevel.setPause(paused);
		return paused;
	}

	public boolean placeTower(int row, int col, String t)
			throws InvalidPlacementException {
		boolean retVal = false;
		try {
			retVal = currentLevel.setPos(row, col, t);
		} catch (TooExpensiveException tee) {
			tee.printStackTrace();
		}
		return retVal;
	}

	/**
	 *
	 * @param row
	 * @param col
	 * @return
	 */
	public String getTower(int row, int col) {
		String retVal = "";
		if (!currentLevel.getPos(row, col).hasTower())
			return retVal;
		retVal = currentLevel.getPos(row, col).getTower().getType();
		return retVal;
	}

	public Position getPosition(int row, int col) {
		return currentLevel.getPos(row, col);
	}

	public int getCurrency() {
		return currentLevel.currentCurrency();
	}

	public int getHealth() { return currentLevel.getHealth(); }

	public List<Position> getEnemyPath() {
		return currentLevel.getEnemyPath();
	}

	public Position getBase() {
		return currentLevel.getBase();
	}

	public Observer getObserver() {
		return obs;
	}

	public void sellTower(int row, int col) {
		currentLevel.removeTower(row, col);
	}

	public void setTickRate(int rate) {
		currentLevel.setTickRate(rate);
	}

	public void setEnemyStartPoint(double enemyBaseRow, double enemyBaseCol) {
		currentLevel.setEnemyStartPoint(enemyBaseRow, enemyBaseCol);
	}

	public void runGame() {
		(new Thread(currentLevel)).start();
	}

	public String getStage() {
		return currentLevel.getStage();
	}

	public void startNextLevel() {
		level++;
		currentLevel.restartLevel(stages[level]);
	}

	public void restartLevel() {
		currentLevel.restartLevel(currentLevel.getStage());
	}

	public void restartLevel(String stage) {
		currentLevel.restartLevel(stage);
	}

}
