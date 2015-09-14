package truelecter.chat.taiga;

import java.util.Date;

public class Message {
    private int id, userId, roomId;
    private long date;
    private String msg;

    public Message(int id, int userId, int roomId, long date, String msg) {
        this.id = id;
        this.userId = userId;
        this.roomId = roomId;
        this.date = date;
        this.msg = msg;
    }

    public String getFormattedDate() {
        return new ChatDateFormat().format(new Date(date * 1000));
    }

    public String getMessage() {
        return msg;
    }

    public void setMessage(String message) {
        this.msg = message;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getUserName() {
        UserInfo ui = UserInfo.get(userId);
        if (ui == null) {
            return null;
        }
        return ui.name;
    }

    public String getFormattedMessage() {
        return getFormattedDate() + " - " + getUserName() + " : " + getMessage();
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getRoomId() {
        return roomId;
    }

    public Message appendMessage(String apendix) {
        setMessage(msg + apendix);
        return this;
    }

    @Override
    public String toString() {
        return getFormattedMessage();
    }

}
