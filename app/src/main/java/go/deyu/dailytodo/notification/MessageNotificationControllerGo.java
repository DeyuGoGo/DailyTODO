package go.deyu.dailytodo.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;
import java.util.List;

import go.deyu.dailytodo.R;
import go.deyu.dailytodo.TimeCompare;
import go.deyu.dailytodo.data.NotificationMessage;
import go.deyu.dailytodo.data.NotificationMessageRM;
import go.deyu.dailytodo.model.MessageModelInterface;
import go.deyu.util.LOG;

/**
 * Created by huangeyu on 15/10/30.
 */
public class MessageNotificationControllerGo implements MessageNotificationController{


    private final String TAG = getClass().getSimpleName();

    private Context mContext;

    private MessageModelInterface<NotificationMessage> mModel ;

    private List<NotificationMessage> mMessages;

    public MessageNotificationControllerGo(Context context ,MessageModelInterface<NotificationMessage> model ){
        mContext = context;
        mModel = model;
        mMessages = mModel.getMessages();

    }
    public void doAlarm() {
    }

    private void notiMessages(List<NotificationMessage> messages){
        for(NotificationMessage m : messages)
            notiMessage(m);
    }

    private void notiMessage(NotificationMessage m ){
        Noti.showNotification(m.getMessage(), m.getId());
    }


    private List<NotificationMessage> getOverTimeMessage(List<NotificationMessage> messages){
        for (NotificationMessage m : messages) {
//            如果提醒時間還沒到就把它移除掉
            if(TimeCompare.isOverNowTime(m.getHour(),m.getMin()))
                messages.remove(m);
        }
        return messages;
    }
    private List<NotificationMessage> getNotFinishMessage(List<NotificationMessage> mMessages) {
        List<NotificationMessage> messages = new ArrayList<NotificationMessage>();
        for (NotificationMessage m : mMessages) {
            if(m.getState()== NotificationMessageRM.STATE_NOT_FINISH){
                messages.add(m);
            }
        }
        LOG.d(TAG,"mMessages size : " + mMessages.size());
        LOG.d(TAG,"getNotFinishMessage size : " + messages.size());
        return messages;
    }

    @Override
    public void showOverTimeNotfinishMessage() {
        notiMessages(getNeedAlarmMessages());
    }

    @Override
    public Notification getForegroundNotification(int id ) {
        String title = mContext.getResources().getString(R.string.app_name);
        Bitmap BIcon = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.dailytodoicon);
        int count = getNotFinishMessage(mModel.getMessages()).size();
        Notification notification = new Notification.Builder(mContext)
                .setContentTitle(title)
                .setContentText(String.format(mContext.getString(R.string.todaynotfinish) , count))
                .setLargeIcon(BIcon)
                .setSmallIcon(R.drawable.wallclock)
                .setContentIntent(PendingIntent.getActivity(mContext, id, Noti.getLauncherIntent(mContext), PendingIntent.FLAG_UPDATE_CURRENT))
                .build();
        return notification;
    }

    @Override
    public List<NotificationMessage> getNeedAlarmMessages() {
        List<NotificationMessage> mNeedAlarmMessages  = getNotFinishMessage(mMessages);
        return getOverTimeMessage(mNeedAlarmMessages);
    }
}
