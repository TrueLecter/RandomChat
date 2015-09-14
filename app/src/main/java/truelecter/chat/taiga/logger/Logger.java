package truelecter.chat.taiga.logger;

public abstract class Logger {
    public abstract void log(String s);

    public void log(Object o) {
        log(o.toString());
    }
}
