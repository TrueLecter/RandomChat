package truelecter.chat.taiga;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import truelecter.chat.taiga.actions.Action;
import truelecter.chat.taiga.actions.AuthAction;
import truelecter.chat.taiga.actions.CheckAuthAction;
import truelecter.chat.taiga.actions.GetMessagesAction;
import truelecter.chat.taiga.actions.SendMessageAction;
import truelecter.chat.taiga.logger.ConsoleLogger;
import truelecter.chat.taiga.logger.Logger;

public class Chat {
    protected static Chat instance;
    private long lastCheck = 0;
    private long interval = 0;
    private long lastSend = 0;
    private Logger logger;
    //private User u;

    private Chat() {
        this(new ConsoleLogger());
        instance = this;
    }

    public Chat(Logger l) {
        logger = l;
    }

    public static Chat getInstance() {
        if (instance == null) {
            instance = new Chat();
        }
        return instance;
    }

    /**
     * Receives session hash
     *
     * @param l - user login
     * @param p - user password
     * @return {@link truelecter.chat.taiga.User User} object, if login
     * successful, <b>null</b> if login failed
     * @throws ChatError
     * @throws JSONException
     */
    public User login(String l, String p) throws ChatError, JSONException {
        Action a = new AuthAction().setLogin(l).setPassword(p);
        String res;
        try {
            res = a.execute();
        } catch (Exception e) {
            logger.log("Login failed!");
            logger.log(e.getMessage());
            e.printStackTrace();
            return null;
        }
        JSONObject jo = new JSONObject(res);
        if (jo.has("error"))
            throw new ChatError("Login failed! Code: " + jo.get("error") + " \n" + jo.getString("errorMsg"));
        return new User(l, jo.getString("hash"));
    }

    // Not to repeat myself
    private Message[] getMessages(String res) throws ChatError, JSONException {
        if (Math.abs(interval - System.currentTimeMillis()) < Config.CHECK_INTERVAL)
            // logger.log("Skipped, timeout: " + Math.abs(interval -
            // System.currentTimeMillis()));
            return new Message[0];
        JSONObject jo = new JSONObject(res);
        if (jo.has("error"))
            throw new ChatError("Getting messages failed! Code: " + jo.get("error") + " \n" + jo.getString("errorMsg"));
        interval = System.currentTimeMillis();
        if (!(jo.get("msg") instanceof JSONArray))
            return new Message[0];
        JSONArray ja = jo.getJSONArray("msg");
        Message[] r = new Message[ja.length()];
        for (int i = 0; i < ja.length(); i++) {
            JSONObject j = ja.getJSONObject(i);
            int uid = j.getInt("uid");
            if (!UserInfo.contains(j.getInt("uid")))
                UserInfo.put(uid, new UserInfo(j.getString("uname"), j.getString("avatar")));
            r[i] = new Message(j.getInt("id"), uid, j.getInt("rid"), j.getLong("date"), j.getString("msg"));
        }
        lastCheck = System.currentTimeMillis();
        return r;
    }

    /**
     * Get last <b>count</b> messages.
     *
     * @param count - number of messages to get
     * @return - array of {@link truelecter.chat.taiga.Message Message} array,
     * with messages in descending order.
     * @throws ChatError
     */
    public Message[] getMessagesByCount(int count) throws ChatError, JSONException {
        Action a = new GetMessagesAction().setLimit(count);
        String res;
        try {
            res = a.execute();
        } catch (Exception e) {
            logger.log("Getting messages failed!");
            logger.log(e.getMessage());
            return null;
        }
        return getMessages(res);
    }

    /**
     * Get messages starting from <b>date</b>
     *
     * @param date - date to start from
     * @return - array of {@link truelecter.chat.taiga.Message Message} array,
     * with messages in descending order.
     * @throws ChatError
     */
    public Message[] getMessagesByDate(long date) throws ChatError, JSONException {
        Action a = new GetMessagesAction().setDate(date / 1000);
        String res;
        try {
            res = a.execute();
        } catch (Exception e) {
            logger.log("Getting messages failed!");
            logger.log(e.getMessage());
            return null;
        }
        return getMessages(res);
    }

    /**
     * Get messages starting from last update
     *
     * @return - array of {@link truelecter.chat.taiga.Message Message} array,
     * with messages in descending order.
     * @throws ChatError
     */

    public Message[] getNewMessages() throws ChatError, JSONException {
        if (lastCheck < 1)
            return getMessagesByCount(Config.STARTING_MESSAGES_COUNT);
        return getMessagesByDate(lastCheck);
    }

    /**
     * Sends message.
     *
     * @param u   - messenger.
     * @param msg - message.
     * @return were message sent or not.
     * @throws ChatError
     */
    public boolean sendMessage(User u, String msg) throws ChatError, JSONException {
        long t;
        if ((t = Math.abs(lastSend - System.currentTimeMillis())) < Config.SEND_TIMEOUT)
            throw new ChatError("You are typing to fast. Please, wait " + (t / 1000.0) + " sec. more");
        lastSend = System.currentTimeMillis();
        Action a = new SendMessageAction(u).setMessage(msg);
        String res;
        try {
            res = a.execute();
        } catch (Exception e) {
            logger.log("Message wasn't sent!");
            logger.log(e.getMessage());
            return false;
        }
        JSONObject jo = new JSONObject(res);
        if (jo.has("error") && (jo.getInt("error") != 0))
            throw new ChatError("Message wasn't sent! Code: " + jo.get("error") + " \n" + jo.getString("errorMsg"));
        return true;
    }

    /**
     * Check if auth hash is valid for this user
     *
     * @param name - user name
     * @param hash - hash to check
     * @return {@link truelecter.chat.taiga.User User} object instance if success, <b>null</b> if not
     */
    public User checkAuth(String name, String hash) throws JSONException {
        Action a = new CheckAuthAction().setHash(hash).setUserName(name);
        String res;
        try {
            res = a.execute();
        } catch (Exception e) {
            logger.log("Can't check user auth!");
            logger.log(e.getMessage());
            return null;
        }
        JSONObject jo = new JSONObject(res);
        if (jo.getInt("error") != 0)
            return null;
        return new User(name, hash);
    }

}
