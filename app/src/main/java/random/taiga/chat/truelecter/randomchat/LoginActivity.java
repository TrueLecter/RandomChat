package random.taiga.chat.truelecter.randomchat;

/**
 * Created by _TrueLecter_ on 12.09.2015 for RandomCraft
 * Licensed under terms of GPLv3 http://www.gnu.org/licenses/gpl.html
 */

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;

import truelecter.chat.taiga.Chat;
import truelecter.chat.taiga.ChatError;
import truelecter.chat.taiga.Config;
import truelecter.chat.taiga.Ini;
import truelecter.chat.taiga.User;

public class LoginActivity extends Activity {

    private static LoginActivity instance;
    private EditText txtName, txtPass;

    public static void showToast(final String text) {
        instance.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(instance.getApplicationContext(), text, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        //Enable synchronous running
        //StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        //StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_name);

        Button btnJoin = (Button) findViewById(R.id.btnJoin);
        txtName = (EditText) findViewById(R.id.name);
        txtPass = (EditText) findViewById(R.id.pass);

        // Hiding the action bar
        ActionBar a = getActionBar();
        if (a != null) {
            a.hide();
        }

        try {
            User u = getUserFromFile();
            if (u != null) {
                Intent intent = new Intent(LoginActivity.this,
                        ChatActivity.class);
                intent.putExtra("user", u);
                startActivity(intent);
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

        btnJoin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (getFieldError(txtName, txtPass)) {
                    case 1:
                        showToast("Введите логин");
                        return;
                    case 2:
                        showToast("Введите пароль");
                        return;
                    case 3:
                        showToast("Введите логин и пароль");
                        return;
                }

                String name = txtName.getText().toString().trim();
                String pass = txtPass.getText().toString().trim();
                login(name, pass);
            }
        });
    }

    private void spinImage() {
        instance.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Animation rot = AnimationUtils.loadAnimation(instance.getApplicationContext(), R.anim.rotate_logo);
                ImageView iv = (ImageView) findViewById(R.id.imgLogo);
                iv.startAnimation(rot);
            }
        });
    }

    private void stopImage() {
        instance.runOnUiThread(new Runnable() {
                                   @Override
                                   public void run() {
                                       ImageView iv = (ImageView) findViewById(R.id.imgLogo);
                                       iv.clearAnimation();
                                   }
                               }
        );
    }

    private void login(String l, String p) {
        Toast.makeText(getApplicationContext(), "Логинимся...", Toast.LENGTH_LONG).show();
        spinImage();
        new AsyncTask<String, Void, User>() {

            @Override
            protected User doInBackground(String... params) {
                Chat c = Chat.getInstance();
                User u;
                try {
                    if (Config.LOGIN_DEBUG)
                        u = c.login(Config.testLogin, Config.testPasword);
                    else
                        u = c.login(params[0], params[1]);
                    if (u == null) {
                        throw new NullPointerException("Отсутствует соединение с сервером");
                    }
                } catch (NullPointerException | ChatError chatError) {
                    showToast("Ошибка входа: \n" + chatError.getMessage());
                    stopImage();
                    return null;
                } catch (JSONException e) {
                    showToast("Неправильный ответ сервера: \n" + e.getMessage());
                    stopImage();
                    return null;
                }
                stopImage();
                saveUser(u);
                return u;
            }

            @Override
            protected void onPostExecute(User u) {
                if (u == null) {
                    return;
                }
                Intent intent = new Intent(LoginActivity.this,
                        ChatActivity.class);
                intent.putExtra("user", u);
                startActivity(intent);
            }
        }.execute(l, p);
    }

    private void saveUser(User u) {
        String name = u.getName(), hash = u.getHash();
        File myDir = getFilesDir();
        File f;
        try {
            myDir.mkdirs();
            f = new File(myDir, "auth");
            Ini i = new Ini();
            i.put("User", "login", name);
            i.put("User", "hash", hash);
            i.write(f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private User getUserFromFile() throws JSONException, IOException {
        File myDir = getFilesDir();
        File f = null;
        try {
            f = new File(myDir, "auth");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Ini i = new Ini(f);
        return Chat.getInstance().checkAuth(i.getString("User", "login", ""), i.getString("User", "hash", ""));
    }

    private int getFieldError(EditText name, EditText pass) {
        int i = 0;
        if (name.getText().toString().trim().length() == 0) {
            i++;
        }
        if (pass.getText().toString().trim().length() == 0) {
            i += 2;
        }
        return i;
    }


}
