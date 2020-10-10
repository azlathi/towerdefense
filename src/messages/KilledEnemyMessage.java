package messages;

public class KilledEnemyMessage {

    private static final long serialVersionUID = 1L;
    private int bounty;
    private double row;
    private double col;

    public KilledEnemyMessage(double row, double col, int bounty) {
        this.row = row;
        this.col = col;
        this.bounty = bounty;
    }

    public int getBounty() { return bounty; }

    public double getRow() { return row; }
    public double getCol() { return col; }
}
