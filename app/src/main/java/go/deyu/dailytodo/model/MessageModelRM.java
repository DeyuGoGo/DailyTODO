package go.deyu.dailytodo.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import go.deyu.dailytodo.DailyCheck;
import go.deyu.dailytodo.data.NotificationMessageORM;
import go.deyu.dailytodo.data.NotificationMessageRM;
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
        realm = Realm.getDefaultInstance();
        refreshMessage();
    }

    @Override
    public void refreshMessage() {
        RealmQuery<NotificationMessageRM> query = realm.where(NotificationMessageRM.class);
        mMessages = query.findAll();
    }


    @Override
    public void addMessage(final String message){
        realm.executeTransaction(new Realm.Transaction() {
                                     @Override
                                     public void execute(Realm realm) {
                                         NotificationMessageRM ew = realm.createObject(NotificationMessageRM.class);
                                         ew.setMessage(message);
                                         ew.setState(NotificationMessageRM.STATE_NOT_FINISH);
                                         ew.setId(getIncrementID());
                                     }
                                 }
        );
        onChange();
    }

    @Override
    public void changeMessageState(int id , int state){
        NotificationMessageRM m = findMessageById(id);
        realm.beginTransaction();
        if(m!=null)
            m.setState(state);
        realm.commitTransaction();
        onChange();
    }


    @Override
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

    @Override
    public NotificationMessageRM findMessageById(int id){
        NotificationMessageRM result =null ;
        for(NotificationMessageRM m : mMessages){
            if(m.getId() == id)
                result = m;
        }
        return result;
    }

    @Override
    public void deleteMessage(int id){
        RealmQuery<NotificationMessageRM> query = realm.where(NotificationMessageRM.class);
        NotificationMessageRM rs = query.equalTo("id", id).findFirst();
        realm.beginTransaction();
        rs.removeFromRealm();
        realm.commitTransaction();
        onChange();
    }

    @Override
    public void changeMessage(int id, String message) {
        RealmQuery<NotificationMessageRM> query = realm.where(NotificationMessageRM.class);
        NotificationMessageRM rs = query.equalTo("id", id).findFirst();
        realm.beginTransaction();
        rs.setMessage(message);
        realm.commitTransaction();
        onChange();
    }


    @Override
    public List<NotificationMessageRM> getMessages(){
        return mMessages;
    }

    @Override
    public void registerListener(OnMessageChangeListener listener){
        if(listener!=null)listeners.add(listener);
    }

    @Override
    public void unregisterListener(OnMessageChangeListener listener){
        if(listener!=null)listeners.remove(listener);
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
        if(realm!=null)realm.close();
    }

    //    拿目前列表中最大的ID+1去當作新資料的ID。
    private int getIncrementID(){
        int i = 0 ;
        if(mMessages!=null && mMessages.size()>0){
            for(NotificationMessageRM data: mMessages)
                if(data.getId()>i)i=data.getId();
        }
        return ++i;
    }


    public void onChange(){
        for(OnMessageChangeListener l : listeners){
            if(l!=null)l.onChange();
        }
    }
}
