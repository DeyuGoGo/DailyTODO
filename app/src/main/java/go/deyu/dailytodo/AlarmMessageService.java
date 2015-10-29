package go.deyu.dailytodo;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.IBinder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import go.deyu.dailytodo.data.NotificationMessage;
import go.deyu.dailytodo.data.NotificationMessageRM;
import go.deyu.dailytodo.model.MessageModelInterface;
import go.deyu.dailytodo.model.MessageModelRM;
import go.deyu.dailytodo.notification.Noti;
import go.deyu.dailytodo.receiver.MessageReceiver;
import go.deyu.util.LOG;

public class AlarmMessageService extends Service {

    public static final String ACTION_START_ALARM = "go.deyu.start.alarm";

    private final int NOTIFICATION_FOREGROUND_ID = 0x87;

    private MessageReceiver receiver ;

    private final String TAG = getClass().getSimpleName();

    private MessageModelInterface<NotificationMessage> model ;


    public AlarmMessageService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LOG.d(TAG, "onCreate");
        receiver = new MessageReceiver();
        registerReceiver(receiver, receiver.getIntentFilter());
        model = new MessageModelRM(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(intent.getAction() == null) return START_STICKY;

        if(intent.getAction().equals(ACTION_START_ALARM)){
            model.checkChangeDay();
            doAlarm(model.getMessages());
            startForegroundNotification();
        }
        return START_STICKY;
    }




    /**
     * Called by the system to notify a Service that it is no longer used and is being removed.  The
     * service should clean up any resources it holds (threads, registered
     * receivers, etc) at this point.  Upon return, there will be no more calls
     * in to this Service object and it is effectively dead.  Do not call this method directly.
     */
    @Override
    public void onDestroy() {
        LOG.d(TAG, "onDestroy");
        super.onDestroy();
        if(receiver!=null)unregisterReceiver(receiver);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    public void doAlarm(List<NotificationMessage> messages) {
        List<NotificationMessage> mNotfinishMessages =  getNeedAlarmMessage(messages);
        notiMessages(mNotfinishMessages);
        if(SettingConfig.getIsVoiceOpen(this))speakDefaultMessage(mNotfinishMessages);
    }

    private void startForegroundNotification(){
        String title = getResources().getString(R.string.app_name);
        Bitmap BIcon = BitmapFactory.decodeResource(getResources(), R.drawable.dailytodoicon);
        int count = getNotFinishMessage(model.getMessages()).size();
        Notification notification = new Notification.Builder(this)
                .setContentTitle(title)
                .setContentText(String.format(getString(R.string.todaynotfinish) , count))
                .setLargeIcon(BIcon)
                .setSmallIcon(R.drawable.wallclock)
                .setContentIntent(PendingIntent.getActivity(this, NOTIFICATION_FOREGROUND_ID, Noti.getLauncherIntent(this), PendingIntent.FLAG_UPDATE_CURRENT))
                .build();
        startForeground(NOTIFICATION_FOREGROUND_ID, notification);
    }

    private void notiMessages(List<NotificationMessage> messages){
        for(NotificationMessage m : messages)
            notiMessage(m);
    }

    private void notiMessage(NotificationMessage m ){
        Noti.showNotification(m.getMessage(), m.getId());
    }


    private List<NotificationMessage> getNeedAlarmMessage(List<NotificationMessage> mMessages){
        List<NotificationMessage> messages = getNotFinishMessage(mMessages);
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE);
        int nowTime = hour*100 + min ;
        for (NotificationMessage m : mMessages) {
            int messagealarmtime = m.getHour()*100 + m.getMin();
            LOG.d(TAG,"messagealarmtime : " + messagealarmtime +  " \n");
            LOG.d(TAG,"nowTime : " + nowTime +  " \n");
            if(messagealarmtime>nowTime)
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

    private void speakDefaultMessage(List<NotificationMessage> messages){
        if(messages!=null && messages.size()>0) {
            MediaPlayer mp = MediaPlayer.create(this, R.raw.nottodo);
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                }
            });
            mp.start();
        }
    }
}
