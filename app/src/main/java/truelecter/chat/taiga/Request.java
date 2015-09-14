package truelecter.chat.taiga;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Request {
    public static String convertArrayToString(String[] args) throws UnsupportedEncodingException {
        String res = "";
        if ((args == null) || (args.length == 0))
            return res;
        res = args[0];
        for (int i = 1; i < args.length; i++) {
            if ((args[i] == null) || args[i].isEmpty()) {
                continue;
            }
            String[] str = args[i].split("=");
            res += "&" + str[0] + "=" + URLEncoder.encode(str[1], "UTF-8");
        }
        return res;
    }

    public static String executePost(String targetURL, String... urlParameters) throws Exception {
        return executePost(targetURL, convertArrayToString(urlParameters));
    }

    public static String executePost(String targetURL, String urlParameters) throws Exception {
        HttpURLConnection connection = null;
        try {
            // Create connection
            URL url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            //connection.setRequestProperty("Content-Length", Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches(false);
            connection.setDoOutput(true);

            // System.out.println(connection.getURL() + "?" + urlParameters);

            // Send request
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.close();

            // Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            if (null != connection) {
                connection.disconnect();
            }
            return response.toString();
        } catch (Exception e) {
            if (connection != null) {
                connection.disconnect();
            }
            e.printStackTrace();
            throw e;
        }
    }
}
