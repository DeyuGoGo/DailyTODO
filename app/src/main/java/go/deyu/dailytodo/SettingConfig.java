package go.deyu.dailytodo;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by huangeyu on 15/10/15.
 */
public class SettingConfig {
    private final static String PERFERANCE_SETTING = "DailyToDo_Setting_Perferance";
    private final static String KEY_SETTING_VOICE_OPEN = "voice.open";
    private final static String KEY_SETTING_BELL_OPEN = "bell.open";

    public static void setIsVoiceOpen(Context context,boolean isOpen) {
        SharedPreferences prefs = context.getSharedPreferences(PERFERANCE_SETTING , Context.MODE_PRIVATE );
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(KEY_SETTING_VOICE_OPEN , isOpen);
        editor.commit();
    }

    public static boolean getIsVoiceOpen(Context context){
        SharedPreferences prefs = context.getSharedPreferences(PERFERANCE_SETTING , Context.MODE_PRIVATE );
        return prefs.getBoolean(KEY_SETTING_VOICE_OPEN,false);
    }

    public static void setIsBelleOpen(Context context,boolean isOpen) {
        SharedPreferences prefs = context.getSharedPreferences(PERFERANCE_SETTING , Context.MODE_PRIVATE );
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(KEY_SETTING_BELL_OPEN , isOpen);
        editor.commit();
    }

    public static boolean getIsBellOpen(Context context){
        SharedPreferences prefs = context.getSharedPreferences(PERFERANCE_SETTING , Context.MODE_PRIVATE );
        return prefs.getBoolean(KEY_SETTING_BELL_OPEN , false);
    }


}
