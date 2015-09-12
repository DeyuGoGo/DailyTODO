package go.deyu.dailytodo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.sql.SQLException;

import go.deyu.dailytodo.data.NotificationMessage;
import go.deyu.dailytodo.model.MessageModelInterface;
import go.deyu.dailytodo.model.MessageModel;
import go.deyu.dailytodo.notification.Noti;
import go.deyu.util.LOG;

public class MessageReceiver extends BroadcastReceiver {

    public static final String EXTRA_INT_MESSAGE_ID = "go.deyu.extras.message.id";
    public static final String EXTRA_INT_MESSAGE_STATE = "go.deyu.extras.message.state";
    private final String TAG = getClass().getSimpleName();

    public MessageReceiver() {
    }


    public IntentFilter getIntentFilter(){
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        return intentFilter;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        MessageModelInterface model = null;
        try {
            model = new MessageModel(context);
        } catch (SQLException e) {
            LOG.d(TAG , "onReceive init MessageModel error : " + e);
        }
        if(model==null)return;

        if(intent.getAction().equals(Intent.ACTION_SCREEN_ON)){
            model.doAlarm();
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
