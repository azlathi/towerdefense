/**
 * @author: Arjun Lathi, Brandon Erickson, Zhaoyi Yang
 * FILE: Stage.java
 * PURPOSE: Holds attributes for each stage
 */

package constants;

import models.Enemy;


import java.util.Map;
import java.util.TreeMap;

public enum Stage {

	STAGE1(8,8,4,5,new int[]{0,1,2,2,2,2,2,2,2,2,3,4,5,6,7,7,7,6,5,4},new int[]{0,0,0,1,2,3,4,5,6,7,7,7,7,7,7,6,5,5,5,5},
			Constants.SPAWNTIMES, Constants.ENEMYWAVES, 0),
	STAGE2(8,8,7,0,new int[]{0,1,1,1,1,1,1,1,2,3,3,3,3,4,4,4,4,5,6,6,6,6,6,6,6,6,7},new int[]{7,7,6,5,4,3,2,1,1,1,2,3,4,4,5,6,7,7,7,6,5,4,3,2,1,0,0},
			Constants.SPAWNTIMES,
			Constants.ENEMYWAVES, 5),
	STAGE3(8,8,3,4,new int[]{7,7,7,6,5,4,3,2,1,1,1,1,1,2,3,4,5,5,5,4,3},new int[]{0,1,2,2,2,2,2,2,2,3,4,5,6,6,6,6,6,5,4,4,4},
			Constants.SPAWNTIMES, Constants.ENEMYWAVES, 4),
	STAGE4(8,8,0,7,new int[]{7,6,5,4,3,3,3,2,1,1,1,2,3,4,5,5,5,6,7,7,7,7,7,6,5,4,3,2,1,0,0},new int[]{0,0,0,0,0,1,2,2,2,3,4,4,4,4,4,3,2,2,2,3,4,5,6,6,6,6,6,6,6,6,7},
			Constants.SPAWNTIMES, Constants.ENEMYWAVES, 3);

	private int rows;
	private int cols;
	private int baseRow;
	private int baseCol;
	private int[] enemyPathRows;
	private int[] enemyPathCols;
	private int[] spawnTimes;
	private String[] enemies; // spawn time to enemy name
	private int blockedChance;
	
	private Stage(int rows, int cols, int baseRow, int baseCol,
			int[] enemyPathRows, int[] enemyPathCols, int[] spawnTimes, String[] enemies, int blockedChance) {

		this.rows = rows;
		this.cols = cols;
		this.baseRow = baseRow;
		this.baseCol = baseCol;
		this.enemyPathCols = enemyPathCols;
		this.enemyPathRows = enemyPathRows;
		this.spawnTimes = spawnTimes;
		this.enemies = enemies;
		this.blockedChance = blockedChance;
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
	 * @return the baseRow
	 */
	public int getBaseRow() {
		return baseRow;
	}

	/**
	 * @return the baseCol
	 */
	public int getBaseCol() {
		return baseCol;
	}

	/**
	 * @return the enemyPathRows
	 */
	public int[] getEnemyPathRows() {
		return enemyPathRows;
	}

	/**
	 * @return the enemyPathCols
	 */
	public int[] getEnemyPathCols() {
		return enemyPathCols;
	}

	public int getBlockedChance() { return blockedChance; }

	/**
	 * @return the enemies
	 */
	public Map<Integer,Enemy> getEnemies() {
		Map<Integer, Enemy> enemyMap = new TreeMap<>(); // treemap to keep order correct
		for (int i = 0; i < spawnTimes.length; i++) {
			enemyMap.put(spawnTimes[i], new Enemy(enemies[i]));
		}
		return enemyMap;
	}
	
}
