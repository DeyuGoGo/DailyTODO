package go.deyu.dailytodo.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

import go.deyu.dailytodo.AlarmMessageService;
import go.deyu.dailytodo.TimeCompare;
import go.deyu.dailytodo.data.NotificationMessage;
import go.deyu.dailytodo.data.NotificationMessageRM;
import go.deyu.util.LOG;

/**
 * Created by huangeyu on 15/10/30.
 */
public class MessageAlarmGo implements MessageAlarm{

    private Context mContext ;

    private final String TAG = getClass().getSimpleName();

    public MessageAlarmGo(Context context){
        mContext = context;
    }
    @Override
    public void setDailyAlarm(NotificationMessage message) {

        boolean alarmUp = (PendingIntent.getService(mContext, message.getId() ,
                new Intent(mContext,AlarmMessageService.class),
                PendingIntent.FLAG_NO_CREATE) != null);
        if(alarmUp)return;

        Calendar cal = Calendar.getInstance();

        LOG.d(TAG,"TimeCompare.isOverNowTime : "+TimeCompare.isOverNowTime(message.getHour(),message.getMin()));
        if(message.getState() == NotificationMessageRM.STATE_FINISH || (!TimeCompare.isOverNowTime(message.getHour(),message.getMin()))){
            cal.add(Calendar.DATE , 1);
        }

        cal.set(Calendar.HOUR_OF_DAY , message.getHour());
        cal.set(Calendar.MINUTE, message.getMin());
        cal.set(Calendar.SECOND , 0);
        PendingIntent pi = getMessageIntent(message);
        AlarmManager am = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);

    }

    @Override
    public void cancelDailyAlarm(NotificationMessage message) {
        AlarmManager am = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        am.cancel(getMessageIntent(message));
    }

    private PendingIntent getMessageIntent(NotificationMessage message){
        Intent intent = new Intent(mContext , AlarmMessageService.class);
        intent.setAction(AlarmMessageService.ACTION_ALARM_BELL);
        intent.putExtra(MessageAlarm.EXTRA_ALARM_MESSAGE_KEY , message.getMessage());
        return PendingIntent.getService(mContext, message.getId(), intent, PendingIntent.FLAG_ONE_SHOT);
    }
}
