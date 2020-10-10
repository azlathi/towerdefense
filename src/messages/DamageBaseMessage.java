package messages;

public class DamageBaseMessage extends UpdateMessage {

    private static final long serialVersionUID = 1L;
    private int damage;

    public DamageBaseMessage(int damage, int row, int col) {
        super(row, col);
        this.damage = damage;
    }

    public int getDamage() { return damage; }
}
