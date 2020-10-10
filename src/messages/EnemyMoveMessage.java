package messages;

import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

import models.Enemy;

public class EnemyMoveMessage{

	private static final long serialVersionUID = 1L;
	private Map<Integer, Enemy> list;
	private boolean reachBase;
	
	public EnemyMoveMessage(Map<Integer, Enemy> list) {
		this.list = list;
	}

	/**
	 * @return the tower
	 */
	public Map<Integer, Enemy> getList() {
		return list;
	}
}
