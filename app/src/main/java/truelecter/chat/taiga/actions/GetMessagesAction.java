package truelecter.chat.taiga.actions;

import truelecter.chat.taiga.Config;

public class GetMessagesAction extends Action {
    public GetMessagesAction() {
        super(Config.GET_MESSAGES_LINK);
    }

    public GetMessagesAction setDate(long l) {
        setArg("date", String.valueOf(l));
        return this;
    }

    public GetMessagesAction setLimit(int l) {
        setArg("limit", String.valueOf(l));
        return this;
    }
}
