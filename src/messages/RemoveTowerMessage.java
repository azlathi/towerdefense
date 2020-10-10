package messages;

public class RemoveTowerMessage extends UpdateMessage {

    private static final long serialVersionUID = 1L;

    public RemoveTowerMessage(int row, int col) {
        super(row, col);
    }
}
