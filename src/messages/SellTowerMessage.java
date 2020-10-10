package messages;

public class SellTowerMessage extends UpdateMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String type;

	public SellTowerMessage(int row, int col, String type) {
		super(row, col);
		this.type = type;
	}

	public String getType() { return type; }

}
