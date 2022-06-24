package ua.tsopin.test;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Date;

import ua.tsopin.test.utils.Utils;

public class SettingsHandler {

    private static final String SHARED_PREFERENCES_FILE = "main_settings";
    private static final String SHARED_PREFERENCES_LAST_LOGIN = "last_login";
    private static final String SHARED_PREFERENCES_LAST_SYNC = "last_sync";
    private static final String SHARED_PREFERENCES_SAVE_LOGIN = "save_login";

    private static SettingsHandler instance = null;

    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public String getLastLogin() {
        return sharedPreferences.getString(SHARED_PREFERENCES_LAST_LOGIN, "");
    }

    public void setLastLogin(String lastLogin) {
        editor.putString(SHARED_PREFERENCES_LAST_LOGIN, lastLogin);
        editor.apply();
    }

    public boolean isSavelogin() {
        return sharedPreferences.getBoolean(SHARED_PREFERENCES_SAVE_LOGIN, true);
    }

    public void setSavelogin(boolean savelogin) {
        editor.putBoolean(SHARED_PREFERENCES_SAVE_LOGIN, savelogin);
        editor.apply();
    }

    public String getLastSync() {
        return sharedPreferences.getString(SHARED_PREFERENCES_LAST_SYNC, Utils.formateDate(new Date(0)));
    }

    public void setLastSync(String lastSync) {
        editor.putString(SHARED_PREFERENCES_LAST_SYNC, lastSync);
        editor.apply();
    }

    public static synchronized SettingsHandler getInstance(Context context) {
        if (instance == null) {
            instance = new SettingsHandler(context);
        }
        return instance;
    }

    private SettingsHandler(Context context) {
        this.context = context.getApplicationContext();
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

}
