package truelecter.chat.taiga.actions;

import truelecter.chat.taiga.Config;

/**
 * Created by _TrueLecter_ on 12.09.2015 for RandomCraft
 * Licensed under terms of GPLv3 http://www.gnu.org/licenses/gpl.html
 */
public class CheckAuthAction extends Action {

    public CheckAuthAction() {
        super(Config.CHECK_AUTH_LINK);
    }

    public CheckAuthAction setUserName(String name) {
        setArg("l", name);
        return this;
    }

    public CheckAuthAction setHash(String hash) {
        setArg("hash", hash);
        return this;
    }
}
