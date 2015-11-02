package go.deyu.dailytodo.model;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.UpdateBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import go.deyu.dailytodo.DailyCheck;
import go.deyu.dailytodo.data.NotificationMessageORM;
import go.deyu.dailytodo.dbh.DatabaseHelper;
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
            LOG.d(TAG, "addMessage Exception : " + e);
        }
        onChange();
    }

    @Override
    public NotificationMessageORM findMessageById(int messageid) {
        try{
            NotificationMessageORM n = messageDao.queryForId(messageid);
            return n;
        } catch (SQLException e){
            LOG.d(TAG, "changeMessageState Exception : " + e);
        }
        return null;
    }

    public void changeMessageState(int id , int state){
        try{
            LOG.d(TAG, "changeMessageState id " + id + " state : " + state);
            NotificationMessageORM n = messageDao.queryForId(id);
            n.setState(state);
            messageDao.update(n);
        } catch (SQLException e){
            LOG.d(TAG, "changeMessageState Exception : " + e);
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

    @Override
    public void changeMessage(int id, String message) {
        try{
            NotificationMessageORM n = messageDao.queryForId(id);
            n.setMessage(message);
            messageDao.update(n);
        } catch (SQLException e){
            LOG.d(TAG, "changeMessage Exception : " + e);
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

    @Override
    public void checkChangeDay(){
        if(DailyCheck.isChangeDay()){
            updateDaily();
            DailyCheck.updateDayTime();
        }
    }

    @Override
    public void close() {

    }
}
