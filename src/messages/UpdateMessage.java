package messages;

import java.io.Serializable;

import models.Tower;

public class UpdateMessage implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int row;
	private int col;
	
	public UpdateMessage(int row, int col) {
		this.row = row;
		this.col = col;
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
	
	

}
