package truelecter.chat.taiga.logger;

public class ConsoleLogger extends Logger {

    @Override
    public void log(String s) {
        System.out.println(s);
    }

}
