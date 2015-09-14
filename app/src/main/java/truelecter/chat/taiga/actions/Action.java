package truelecter.chat.taiga.actions;

import java.util.HashMap;
import java.util.Map;

import truelecter.chat.taiga.Request;

public abstract class Action {
    private String link;
    private HashMap<String, String> args = new HashMap<>();

    public Action(String link) {
        this.link = link;
    }

    private static String[] getParamArray(Map<String, String> map) {
        String[] res = new String[map.size()];
        int i = 0;
        for (Map.Entry<String, String> e : map.entrySet()) {
            res[i++] = e.getKey() + "=" + e.getValue();
        }
        return res;
    }

    public Action setArg(String key, String value) {
        args.put(key, value);
        return this;
    }

    public String execute() throws Exception {
        return Request.executePost(link, getParamArray(args));
    }

}
