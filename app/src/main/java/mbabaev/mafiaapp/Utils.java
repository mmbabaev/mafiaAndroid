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
}
