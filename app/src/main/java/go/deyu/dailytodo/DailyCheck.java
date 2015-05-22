package go.deyu.dailytodo;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Calendar;

import go.deyu.util.AppContextSingleton;

/**
 * Created by huangeyu on 15/5/21.
 */
public class DailyCheck {
    private static final String PERFERANCE_DAILY_FILE_NAME = "Daily_Perferance";
    private static final String PERFERANCE_DAILY_UPDATE_DATE_KEY = "Daily_Update_Time";

    public static boolean isChangeDay(){
        SharedPreferences prefs = getSP();
        int lastUpdateDate =  prefs.getInt(PERFERANCE_DAILY_UPDATE_DATE_KEY , 1);
        Calendar cal = Calendar.getInstance();
        int nowDate = cal.get(Calendar.DAY_OF_YEAR);
        return lastUpdateDate == nowDate;
    }
    public static void updateDayTime(){
        SharedPreferences prefs = getSP();
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_YEAR);
        prefs.edit().putInt(PERFERANCE_DAILY_UPDATE_DATE_KEY , day).commit();
    }
    private static SharedPreferences getSP(){
        Context context = AppContextSingleton.getApplicationContext();
        return context.getSharedPreferences(PERFERANCE_DAILY_FILE_NAME, Context.MODE_PRIVATE);
    }
}
