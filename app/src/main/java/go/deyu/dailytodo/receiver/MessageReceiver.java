package go.deyu.dailytodo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.sql.SQLException;
import java.util.List;

import go.deyu.dailytodo.data.NotificationMessage;
import go.deyu.dailytodo.model.MessageModel;
import go.deyu.dailytodo.notification.Noti;
import go.deyu.util.LOG;

public class MessageReceiver extends BroadcastReceiver {

    public static final String ACTION_FINISH_MESSAGE = "go.deyu.finish.message";
    public static final String EXTRA_INT_MESSAGE_ID = "go.deyu.extras.message.id";
    public static final String EXTRA_INT_MESSAGE_STATE = "go.deyu.extras.message.state";
    private final String TAG = getClass().getSimpleName();

    public MessageReceiver() {
    }


    public IntentFilter getIntentFilter(){
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(ACTION_FINISH_MESSAGE);
        return intentFilter;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        MessageModel model = null;
        try {
            model = new MessageModel(context);
        } catch (SQLException e) {
            LOG.d(TAG , "onReceive init MessageModel error : " + e);
        }
        if(model==null)return;

        if(intent.getAction().equals(Intent.ACTION_SCREEN_ON)){
            model.checkChangeDay();
            List<NotificationMessage> mNotfinishMessages =  model.getNotFinishMessage();
            model.notiMessages(mNotfinishMessages);
            model.speakDefaultMessage(mNotfinishMessages);
        }

        if(intent.getAction().equals(ACTION_FINISH_MESSAGE)){
            int id = intent.getIntExtra(EXTRA_INT_MESSAGE_ID , -1);
            int state = intent.getIntExtra(EXTRA_INT_MESSAGE_STATE , -1);
            LOG.d(TAG , "ACTION_FINISH_MESSAGE id : " + id + " state : " + state);
            if(id == -1 || state == -1){
                LOG.e(TAG , "no exist id or state!!!!!!");
                return;
            }
            model.changeMessageState(id ,state);
        }
    }

    private void notifyMessage(MessageModel model) {
        for (NotificationMessage m : model.getMessages()) {
            if(m.getState()==NotificationMessage.STATE_NOT_FINISH){
                Noti.showNotification(m.getMessage(), m.getId());
            }
        }
    }

}
