package truelecter.chat.taiga;

public class ChatError extends Exception {
    private static final long serialVersionUID = 931356318069014745L;

    public ChatError(String e) {
        super(e);
    }
}
