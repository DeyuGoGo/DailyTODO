package go.deyu.dailytodo.notification;

import android.app.Notification;

import java.util.List;

import go.deyu.dailytodo.data.NotificationMessage;

/**
 * Created by huangeyu on 15/10/30.
 */
public interface MessageNotificationController {
    public void showOverTimeNotfinishMessage();
    public Notification getForegroundNotification(int id);
    public List<NotificationMessage> getNeedAlarmMessages();

}
