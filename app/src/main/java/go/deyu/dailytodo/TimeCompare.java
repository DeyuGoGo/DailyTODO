package go.deyu.dailytodo;

import java.util.Calendar;

/**
 * Created by huangeyu on 15/10/30.
 */
public class TimeCompare {

    /**
     * compare with now time
     * @param hour
     * @param min
     * @return if bigger than now time will return true
     */
    public static boolean isOverNowTime(int hour , int min){
        Calendar c = Calendar.getInstance();
        int now_hour = c.get(Calendar.HOUR_OF_DAY);
        int now_min = c.get(Calendar.MINUTE);
        int nowTime = now_hour*100 + now_min ;
        int comparetime  = hour * 100 + min;
        return comparetime > nowTime;
    }
}
