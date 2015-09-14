package truelecter.chat.taiga.actions;

import truelecter.chat.taiga.Config;

public class AuthAction extends Action {
    public AuthAction() {
        super(Config.AUTH_LINK);
    }

    public AuthAction setLogin(String l) {
        setArg("l", l);
        return this;
    }

    public AuthAction setPassword(String l) {
        setArg("p", l);
        return this;
    }
}
