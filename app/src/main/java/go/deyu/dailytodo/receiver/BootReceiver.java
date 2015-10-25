package go.deyu.dailytodo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import go.deyu.dailytodo.AlarmMessageService;
import go.deyu.util.LOG;

public class BootReceiver extends BroadcastReceiver {

    private final String TAG = getClass().getSimpleName();
    public BootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        LOG.d(TAG , "Deyu onReceive " + intent.getAction());
        context.startService(new Intent(context, AlarmMessageService.class));
    }
}
