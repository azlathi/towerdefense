package messages;

import models.Tower;

public class AddTowerMessage extends UpdateMessage{
	
	private static final long serialVersionUID = 1L;
	private Tower tower;

	public AddTowerMessage(Tower tower, int row, int col,int currency) {
		super(row, col);
		this.tower = tower;
	}
	
	/**
	 * @return the tower
	 */
	public Tower getTower() {
		return tower;
	}
}
