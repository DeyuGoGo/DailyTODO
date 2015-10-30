package go.deyu.dailytodo.alarm;

import go.deyu.dailytodo.data.NotificationMessage;

/**
 * Created by huangeyu on 15/10/30.
 */
public interface MessageAlarm {
    public final String EXTRA_ALARM_MESSAGE_KEY = "key_alarm_message";
    public void setDailyAlarm(NotificationMessage message);
    public void cancelDailyAlarm(NotificationMessage message);
}
