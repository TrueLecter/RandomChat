package truelecter.chat.taiga;

import android.util.Log;

import java.io.Serializable;

public class User implements Serializable {
    private static User instance;
    private String hash, name;

    public User(String name, String hash) {
        this.hash = hash;
        this.name = name;
        if (this.hash == null || this.name == null) {
            this.name = this.hash = "false";
        }
    }

    private static User getInstance() {
        if (instance == null) {
            instance = new User("false", "false");
        }
        return instance;
    }

    public static User setInstance(User u) {
        instance = u;
        return instance;
    }

    public static boolean isFromUs(Message msg) {
        boolean res = getInstance().name.equals(msg.getUserName());
        Log.i("RandomChat", "Comparing user names: \"" + getInstance().name + "\" and \"" + msg.getUserName() + "\". Result: " + res);
        return res;
    }

    public String getHash() {
        return hash;
    }

    public String getName() {
        return name;
    }

}
