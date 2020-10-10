package messages;

public class ShootBulletMessage extends UpdateMessage {

    private static final long serialVersionUID = 1L;
    private String type;
    private double toRow;
    private double toCol;

    public ShootBulletMessage(int row, int col, double toRow, double toCol, String type) {
        super(row, col);
        this.type = type;
        this.toRow = toRow;
        this.toCol = toCol;
    }

    public String getType() { return type; }
    public double getToRow() { return toRow; }
    public double getToCol() { return toCol; }

}
