package go.deyu.dailytodo.model;

import android.content.Context;
import android.media.MediaPlayer;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.UpdateBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import go.deyu.dailytodo.DailyCheck;
import go.deyu.dailytodo.R;
import go.deyu.dailytodo.data.NotificationMessageORM;
import go.deyu.dailytodo.dbh.DatabaseHelper;
import go.deyu.dailytodo.notification.Noti;
import go.deyu.dailytodo.tts.AndroidTTS;
import go.deyu.dailytodo.tts.TTStoSpeak;
import go.deyu.util.LOG;

/**
 * Created by huangeyu on 15/5/19.
 */
public class MessageModel implements MessageModelInterface
{

    private DatabaseHelper dbh ;
    private Dao<NotificationMessageORM, Integer> messageDao ;
    private Context mContext;
    private ArrayList<NotificationMessageORM> mMessages ;
    private final String TAG = getClass().getSimpleName();
    private ArrayList<OnMessageChangeListener> listeners ;
    public static final String PREFS_NAME = "MessagePrefs";

    public MessageModel(Context context)throws SQLException{
        this.mContext = context;
        init();
    }
    private void init() throws SQLException{
        this.dbh = new DatabaseHelper(mContext);
        this.listeners = new ArrayList<OnMessageChangeListener>();
        this.messageDao = dbh.getNotificationMessageDao();
        this.mMessages = new ArrayList<NotificationMessageORM>();
        List<NotificationMessageORM> DBMessages =  getDBMessages();
        if(DBMessages!=null) mMessages.addAll(DBMessages);
    }

    public void refreshMessage() {
        List<NotificationMessageORM> DBMessages = null;
        try {
            DBMessages = getDBMessages();
        } catch (SQLException e) {
            LOG.d(TAG,"Exception : " + e);
        }
        mMessages.clear();
        if(DBMessages!=null) mMessages.addAll(DBMessages);
    }


    public void addMessage(String message){
        try{
            messageDao.create(new NotificationMessageORM(message));
        } catch (SQLException e){
            LOG.d(TAG , "addMessage Exception : " + e);
        }
        onChange();
    }

    public void changeMessageState(int id , int state){
        try{
            LOG.d(TAG, "changeMessageState id " + id + " state : " + state);
            NotificationMessageORM n = messageDao.queryForId(id);
            n.setState(state);
            messageDao.update(n);
        } catch (SQLException e){
            LOG.d(TAG , "changeMessageState Exception : " + e);
        }
    }

    public void changeMessageAlarmTime(int id , int hour, int min){
        LOG.d(TAG , "changeMessageState id " + id  + " hour : " + hour + " min : " + min);
        try{
            NotificationMessageORM n = messageDao.queryForId(id);
            n.setHour(hour);
            n.setMin(min);
            messageDao.update(n);
        } catch (SQLException e){
        LOG.d(TAG, "changeMessageState Exception : " + e);
        }
        onChange();
    }

    private void updateDaily(){
        try {
            LOG.d(TAG, "updateDaily");
            UpdateBuilder<NotificationMessageORM, Integer> builder = messageDao.updateBuilder();
            builder.updateColumnValue("state", new Integer(NotificationMessageORM.STATE_NOT_FINISH));
            builder.update();
        } catch (SQLException e) {
            LOG.e(TAG , "updateDaily fail : " + e);
            e.printStackTrace();
        }
    }

    public void speakMessages(List<NotificationMessageORM> messages){
        TTStoSpeak TTS = new AndroidTTS(mContext);
        for(NotificationMessageORM m : messages)
            TTS.speak(m.getMessage());
    }
    private void speakDefaultMessage(List<NotificationMessageORM> messages){
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

    private void notiMessages(List<NotificationMessageORM> messages){
        for(NotificationMessageORM m : messages)
            notiMessage(m);
    }

    private void notiMessage(NotificationMessageORM m ){
        Noti.showNotification(m.getMessage(), m.getId());
    }

    private List<NotificationMessageORM> getNeedAlarmMessage(){
        List<NotificationMessageORM> messages = getNotFinishMessage();
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE);
        int nowTime = hour*100 + min ;
        for (NotificationMessageORM m : mMessages) {
            int messagealarmtime = m.getHour()*100 + m.getMin();
            LOG.d(TAG,"messagealarmtime : " + messagealarmtime +  " \n");
            LOG.d(TAG,"nowTime : " + nowTime +  " \n");
            if(messagealarmtime>nowTime)
                messages.remove(m);
        }
        return messages;
    }

    private List<NotificationMessageORM> getNotFinishMessage() {
        List<NotificationMessageORM> messages = new ArrayList<NotificationMessageORM>();
        for (NotificationMessageORM m : mMessages) {
            if(m.getState()== NotificationMessageORM.STATE_NOT_FINISH){
                messages.add(m);
            }
        }
        return messages;
    }
    public void deleteMessage(int id){
        try{
            messageDao.deleteById(id);
        } catch (SQLException e){
            LOG.d(TAG , "addMessage Exception : " + e);
        }
        onChange();
    }

    public List<NotificationMessageORM> getMessages(){
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

    public List<NotificationMessageORM> getDBMessages() throws SQLException{
        return messageDao.queryForAll();
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
        List<NotificationMessageORM> mNotfinishMessages =  getNeedAlarmMessage();
        notiMessages(mNotfinishMessages);
        speakDefaultMessage(mNotfinishMessages);
    }


}
