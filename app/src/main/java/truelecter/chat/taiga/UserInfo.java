package truelecter.chat.taiga;


import java.util.HashMap;

public class UserInfo {
    private static HashMap<Integer, UserInfo> cache = new HashMap<>();

    public String name, avatar;

    public UserInfo(String name, String avatar) {
        this.name = name;
        this.avatar = avatar;
    }

    public static UserInfo put(int id, UserInfo info) {
        cache.put(id, info);
        return info;
    }

    public static boolean contains(int id) {
        return cache.containsKey(id);
    }

    public static UserInfo get(int id) {
        if (!contains(id)) {
            return new UserInfo("false", "false");
        }
        return cache.get(id);
    }
}
