package truelecter.chat.taiga.actions;

import truelecter.chat.taiga.Config;
import truelecter.chat.taiga.User;

public class SendMessageAction extends Action {
    public SendMessageAction(User u) {
        super(Config.SEND_MESSAGE_LINK);
        setArg("l", u.getName()).setArg("hash", u.getHash());
    }

    public SendMessageAction setMessage(String l) {
        setArg("msg", l);
        return this;
    }
}
