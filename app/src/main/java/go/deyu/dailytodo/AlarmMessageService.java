package go.deyu.dailytodo;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.List;

import go.deyu.dailytodo.alarm.MessageAlarm;
import go.deyu.dailytodo.alarm.MessageAlarmGo;
import go.deyu.dailytodo.data.NotificationMessage;
import go.deyu.dailytodo.model.MessageModelInterface;
import go.deyu.dailytodo.model.MessageModelRM;
import go.deyu.dailytodo.notification.MessageNotificationController;
import go.deyu.dailytodo.notification.MessageNotificationControllerGo;
import go.deyu.dailytodo.receiver.MessageReceiver;
import go.deyu.util.LOG;

public class AlarmMessageService extends Service {

    public static final String ACTION_START_ALARM = "go.deyu.start.alarm";

    public static final String ACTION_ALARM_BELL = "go.deyu.alarm_bell";

    public static final String ACTION_SETTING_CHANGE = "go.deyu.setting.changing";

    private MessageReceiver receiver ;

    private final String TAG = getClass().getSimpleName();

    private final int NOTIFICATION_FOREGROUND_ID = 0x87;

    private MessageNotificationController mMessageNotificationController;

    private MessageModelInterface<NotificationMessage> model ;

    private MessageAlarm mMessageAlarm;

    private MediaPlayer mMediaPlayer = null;


    public AlarmMessageService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LOG.d(TAG, "onCreate");
        receiver = new MessageReceiver();
        registerReceiver(receiver, receiver.getIntentFilter());
        model = new MessageModelRM(this);
        mMessageAlarm = new MessageAlarmGo(this);
        mMessageNotificationController = new MessageNotificationControllerGo(this,model);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(intent == null ) return START_STICKY;

        if(intent.getAction() == null) return START_STICKY;

        if(intent.getAction().equals(ACTION_START_ALARM)){
            model.checkChangeDay();
            mMessageNotificationController.showOverTimeNotfinishMessage();
            startForeground(NOTIFICATION_FOREGROUND_ID, mMessageNotificationController.getForegroundNotification(NOTIFICATION_FOREGROUND_ID));
            if(SettingConfig.getIsVoiceOpen(this))speakDefaultMessage(mMessageNotificationController.getNeedAlarmMessages());
            setupAlarm();
        }

        if(intent.getAction().equals(ACTION_ALARM_BELL)){
            LOG.d(TAG, "鬧鐘響鈴");
            if(mMediaPlayer == null){
                mMediaPlayer = MediaPlayer.create(this, R.raw.alarmmusic);
                mMediaPlayer.setLooping(true);
                mMediaPlayer.start();
            }
            showAlarmView(intent.getStringExtra(MessageAlarm.EXTRA_ALARM_MESSAGE_KEY));
        }

        if(intent.getAction().equals(ACTION_SETTING_CHANGE)){
            setupAlarm();
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

    public static Intent getSettingChangeIntent(Context context){
        Intent i = new Intent(context, AlarmMessageService.class);
        i.setAction(AlarmMessageService.ACTION_SETTING_CHANGE);
        return i;
    }

    private void setupAlarm(){
        for (NotificationMessage m : model.getMessages()) {
            if (SettingConfig.getIsBellOpen(this))
                mMessageAlarm.setDailyAlarm(m);
            else
                mMessageAlarm.cancelDailyAlarm(m);
        }
    }
    private void showAlarmView(String message){
        View v = LayoutInflater.from(this).inflate(R.layout.window_bell_cancel_view , null);
        TextView tv = (TextView)v.findViewById(R.id.tv_title);
        tv.setText(message);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mMediaPlayer!=null) {
                    mMediaPlayer.stop();
                    mMediaPlayer.release();
                    mMediaPlayer = null;
                }
                WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
                wm.removeView(v);
            }
        });

        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
            params.width = WindowManager.LayoutParams.WRAP_CONTENT;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
            params.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            params.gravity = Gravity.CENTER;
            wm.addView(v, params);
    }
}
