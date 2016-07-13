package mbabaev.mafiaapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class Utils {
    public static void showError(Context context) {
        Toast.makeText(context, "Произошла ошибка!", Toast.LENGTH_SHORT).show();
    }

    public static SharedPreferences prefs(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static String getJoinId(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return Integer.toString(prefs.getInt("join_id", 0));
    }

    public static String getUserId(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return Integer.toString(prefs.getInt("user_id", 0));
    }

    public static String getRole(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString("role", "");
    }
}
