package go.deyu.dailytodo.alarm;

import go.deyu.dailytodo.data.NotificationMessage;

/**
 * Created by huangeyu on 15/10/30.
 */
public interface MessageAlarm {
    public void setDailyAlarm(NotificationMessage message);
    public void cancelDailyAlarm(NotificationMessage message);
}
