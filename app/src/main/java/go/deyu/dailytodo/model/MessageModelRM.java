package go.deyu.dailytodo.model;

import android.content.Context;
import android.media.MediaPlayer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import go.deyu.dailytodo.DailyCheck;
import go.deyu.dailytodo.R;
import go.deyu.dailytodo.data.NotificationMessageORM;
import go.deyu.dailytodo.data.NotificationMessageRM;
import go.deyu.dailytodo.notification.Noti;
import go.deyu.dailytodo.tts.AndroidTTS;
import go.deyu.dailytodo.tts.TTStoSpeak;
import go.deyu.util.LOG;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by huangeyu on 15/5/19.
 */
public class MessageModelRM implements MessageModelInterface
{

    private Context mContext;
    private Realm realm;
    private RealmResults<NotificationMessageRM> mMessages;
    private final String TAG = getClass().getSimpleName();
    private ArrayList<OnMessageChangeListener> listeners = new ArrayList<OnMessageChangeListener>();
    public static final String PREFS_NAME = "MessagePrefs";

    public MessageModelRM(Context context){
        this.mContext = context.getApplicationContext();
        init();
    }
    private void init(){
        realm = Realm.getInstance(mContext);
        refreshMessage();
    }

    @Override
    public void refreshMessage() {
        RealmQuery<NotificationMessageRM> query = realm.where(NotificationMessageRM.class);
        mMessages = query.findAll();
    }


    public void addMessage(final String message){
        realm.executeTransaction(new Realm.Transaction() {
                                     @Override
                                     public void execute(Realm realm) {
                                         NotificationMessageRM ew = realm.createObject(NotificationMessageRM.class);
                                         ew.setMessage(message);
                                     }
                                 }
        );
        onChange();
    }

    public void changeMessageState(int id , int state){
        NotificationMessageRM m = findMessageById(id);
        realm.beginTransaction();
        if(m!=null)
            m.setState(state);
        realm.commitTransaction();
        onChange();
    }

    public void changeMessageAlarmTime(int id , int hour, int min){
        LOG.d(TAG , "changeMessageState id " + id  + " hour : " + hour + " min : " + min);
        NotificationMessageRM m = findMessageById(id);
        realm.beginTransaction();
        m.setHour(hour);
        m.setMin(min);
        realm.commitTransaction();
        onChange();
    }

    private void updateDaily(){
        LOG.d(TAG, "updateDaily");
        realm.beginTransaction();
        for(int i = 0 ; i < mMessages.size() ; i ++){
            mMessages.get(i).setState(NotificationMessageORM.STATE_NOT_FINISH);
        }
        realm.commitTransaction();
    }

    public void speakMessages(List<NotificationMessageRM> messages){
        TTStoSpeak TTS = new AndroidTTS(mContext);
        for(NotificationMessageRM m : messages)
            TTS.speak(m.getMessage());
    }
    private void speakDefaultMessage(List<NotificationMessageRM> messages){
        if(messages!=null && messages.size()>0) {
                MediaPlayer mp = MediaPlayer.create(mContext, R.raw.nottodo);
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.release();
                    }
                });
                mp.start();
        }
    }

    private void notiMessages(List<NotificationMessageRM> messages){
        for(NotificationMessageRM m : messages)
            notiMessage(m);
    }

    private void notiMessage(NotificationMessageRM m ){
        Noti.showNotification(m.getMessage(), m.getId());
    }

    private NotificationMessageRM findMessageById(int id){
        NotificationMessageRM result =null ;
        for(NotificationMessageRM m : mMessages){
            if(m.getId() == id)
                result = m;
        }
        return result;
    }

    private List<NotificationMessageRM> getNeedAlarmMessage(){
        List<NotificationMessageRM> messages = getNotFinishMessage();
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE);
        int nowTime = hour*100 + min ;
        for (NotificationMessageRM m : mMessages) {
            int messagealarmtime = m.getHour()*100 + m.getMin();
            LOG.d(TAG,"messagealarmtime : " + messagealarmtime +  " \n");
            LOG.d(TAG,"nowTime : " + nowTime +  " \n");
            if(messagealarmtime>nowTime)
                messages.remove(m);
        }
        return messages;
    }

    private List<NotificationMessageRM> getNotFinishMessage() {
        List<NotificationMessageRM> messages = new ArrayList<NotificationMessageRM>();
        for (NotificationMessageRM m : mMessages) {
            if(m.getState()== NotificationMessageRM.STATE_NOT_FINISH){
                messages.add(m);
            }
        }
        LOG.d(TAG,"mMessages size : " + mMessages.size());
        LOG.d(TAG,"getNotFinishMessage size : " + messages.size());
        return messages;
    }
    public void deleteMessage(int id){
        realm.beginTransaction();
        RealmQuery<NotificationMessageRM> query = realm.where(NotificationMessageRM.class);
        RealmResults<NotificationMessageRM> rs = query.equalTo("id", id).findAll();
        rs.removeLast();
        realm.commitTransaction();
        onChange();
    }


    public List<NotificationMessageRM> getMessages(){
        return mMessages;
    }

    public void registerListener(OnMessageChangeListener listener){
        if(listener!=null)listeners.add(listener);
    }

    public void unregisterListener(OnMessageChangeListener listener){
        if(listener!=null)listeners.remove(listener);
    }

    public void onChange(){
        for(OnMessageChangeListener l : listeners){
            if(l!=null)l.onChange();
        }
    }

    public void checkChangeDay(){
        if(DailyCheck.isChangeDay()){
            updateDaily();
            DailyCheck.updateDayTime();
        }
    }

    @Override
    public void doAlarm() {
        checkChangeDay();
        List<NotificationMessageRM> mNotfinishMessages =  getNeedAlarmMessage();
        notiMessages(mNotfinishMessages);
        speakDefaultMessage(mNotfinishMessages);
    }
}
