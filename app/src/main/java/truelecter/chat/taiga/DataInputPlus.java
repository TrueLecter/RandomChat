package truelecter.chat.taiga;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Based on class: ua.com.glybovets.arrays.DataInput
 */

public final class DataInputPlus {
    public static Long getLong(String prompt) {
        String s = getString(prompt);
        while (!isInt(s)) {
            System.out.println("Input isn't long!");
            s = getString(prompt);
        }
        return Long.valueOf(s);
    }

    public static char getChar(String prompt) {
        String s = getString(prompt);
        return s != null ? s.charAt(0) : 0;
    }

    public static Integer getInt(String prompt) {
        String s = getString(prompt);
        while (!isInt(s)) {
            System.out.println("Input isn't integer!");
            s = getString(prompt);
        }
        return Integer.valueOf(s);

    }

    public static String getString(String prompt) {
        System.out.print(prompt);
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);
        String s = null;
        try {
            s = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }

    public static double getDouble(String prompt) {
        String s = getString(prompt);
        while (!isDouble(s)) {
            System.out.println("Input isn't double!");
            s = getString(prompt);
        }
        return Double.valueOf(s);
    }

    public static boolean isDouble(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    public static boolean isInt(String str) {
        return str.matches("-?\\d+");
    }
}