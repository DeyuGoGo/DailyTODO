package go.deyu.dailytodo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import go.deyu.dailytodo.AlarmMessageService;
import go.deyu.util.LOG;

public class MessageReceiver extends BroadcastReceiver {

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
        LOG.d(TAG,"onReceive : " + intent.getAction());
        if(intent.getAction().equals(Intent.ACTION_SCREEN_ON)){
            Intent i = new Intent(context , AlarmMessageService.class);
            i.setAction(AlarmMessageService.ACTION_START_ALARM);
            context.startService(i);
        }
    }

}
