package random.taiga.chat.truelecter.randomchat;

import android.app.Activity;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import random.taiga.chat.truelecter.randomchat.adapter.MessagesListAdapter;
import truelecter.chat.taiga.Chat;
import truelecter.chat.taiga.ChatError;
import truelecter.chat.taiga.Message;
import truelecter.chat.taiga.User;

/**
 * Created by _TrueLecter_ on 12.09.2015 for RandomCraft
 * Licensed under terms of GPLv3 http://www.gnu.org/licenses/gpl.html
 */
public class ChatActivity extends Activity {
    private EditText inputMsg;

    // Chat messages list adapter
    private MessagesListAdapter adapter;
    private List<Message> listMessages;

    private static ChatActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Button btnSend = (Button) findViewById(R.id.btnSend);
        inputMsg = (EditText) findViewById(R.id.inputMsg);
        ListView listViewMessages = (ListView) findViewById(R.id.list_view_messages);

        // Getting the person name from previous screen
        Intent i = getIntent();
        User u = User.setInstance((User) i.getSerializableExtra("user"));

        btnSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!ChatSendThread.getInstance().canSend()) {
                    showToast("Предыдущее сообщение еще обрабатывается");
                    return;
                }

                // Sending message to server
                ChatSendThread.getInstance().send(inputMsg.getText().toString());

                // Clearing the input filed once message was sent
                inputMsg.setText("");
            }
        });

        listMessages = new ArrayList<>();

        adapter = new MessagesListAdapter(this, listMessages);
        listViewMessages.setAdapter(adapter);

        Chat c = Chat.getInstance();

        ChatUpdateThread ct = new ChatUpdateThread(c, new ChatUpdateThread.NewMessageHandler() {
            @Override
            public void processMessage(Message m) {
                appendMessage(m);
            }
        });
        ct.start();

        ChatSendThread cs = new ChatSendThread(c, u);
        cs.start();

        instance = this;

    }

    /**
     * Appending message to list view
     */
    private void appendMessage(final Message m) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                try {
                    Message msg = listMessages.get(listMessages.size() - 1);
                    if (msg.getUserName().equals(m.getUserName()) && Math.abs(msg.getDate() - m.getDate()) < 600 * 1000) {
                        msg.appendMessage("\n" + m.getMessage());
                        msg.setDate(m.getDate());
                        return;
                    }
                } catch (Exception ignored) {
                }
                listMessages.add(m);

                adapter.notifyDataSetChanged();

                // Playing device's notification
                //playBeep();
            }
        });
    }

    private static void showToast(final String message) {

        instance.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(instance.getApplicationContext(), message,
                        Toast.LENGTH_LONG).show();
            }
        });

    }

    /**
     * Plays device's default notification sound
     */
    public void playBeep() {
        try {
            Uri notification = RingtoneManager
                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(),
                    notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class ChatUpdateThread extends Thread {
        private Chat c;
        private boolean running = true, stopped = false;
        private NewMessageHandler handler;

        public ChatUpdateThread(Chat c, NewMessageHandler handler) {
            this.c = c;
            this.handler = handler;
        }

        @Override
        public void run() {
            while (!stopped) {
                while (running) {
                    try {
                        List<Message> msgs = Arrays.asList(c.getNewMessages());
                        Collections.reverse(msgs);
                        for (Message m : msgs) {
                            if (handler != null) {
                                handler.processMessage(m);
                            }
                        }
                    } catch (ChatError | IOException | NullPointerException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        public interface NewMessageHandler {
            void processMessage(Message m) throws IOException;
        }
    /*
        public void pause() {
            running = false;
        }

        public void unPause() {
            running = true;
        }

        public void stopCheck() {
            stopped = true;
        }
    */
    }

    public static class ChatSendThread extends Thread {
        private static ChatSendThread instance = null;
        private Chat c;
        private User u;
        private boolean canSend = true;
        private Runnable onChangeSend;
        private String msgToSend;

        public ChatSendThread(Chat c, User u, Runnable onChageSend) {
            this.c = c;
            this.u = u;
            onChangeSend = onChageSend;
            instance = this;
        }

        public ChatSendThread(Chat c, User u) {
            this(c, u, null);
        }

        public static ChatSendThread getInstance() {
            return instance;
        }

        @Override
        public void run() {
            do {
                try {
                    if ((msgToSend == null) || msgToSend.isEmpty() || msgToSend.equals("null")) {
                        continue;
                    }
                    canSend = false;
                    onSendStateChange();
                    c.sendMessage(u, msgToSend);
                    canSend = true;
                    msgToSend = null;
                    onSendStateChange();
                } catch (ChatError e) {
                    ChatActivity.showToast(e.getMessage());
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    canSend = true;
                }
            } while (true);
        }

        public void send(String msg) {
            msgToSend = msg;
        }

        private void onSendStateChange() {
            if (onChangeSend != null) {
                onChangeSend.run();
            }
        }

        public boolean canSend() {
            return canSend;
        }
    }

}
