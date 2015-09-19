package truelecter.chat.taiga;

public class Config {
    public static int STARTING_MESSAGES_COUNT = 150;
    public static int CHECK_INTERVAL = 2000;
    public static int SEND_TIMEOUT = 2000;
    public static String HOST = "http://172.16.32.7:81/client/";
    public static String CHECK_AUTH_LINK = HOST + "checkAuth.php";
    public static String GET_MESSAGES_LINK = HOST + "getMessages.php";
    public static String AUTH_LINK = HOST + "auth.php";
    public static String SEND_MESSAGE_LINK = HOST + "sendMessage.php";

    public static String testLogin = "bidlo";
    public static String testPassword = "lal";

    public static boolean LOGIN_DEBUG = true;
}
