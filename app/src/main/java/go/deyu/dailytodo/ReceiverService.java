package go.deyu.dailytodo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import go.deyu.dailytodo.receiver.MessageReceiver;

public class ReceiverService extends Service {

    private MessageReceiver receiver ;


    public ReceiverService() {
    }

    /**
     * Called by the system when the service is first created.  Do not call this method directly.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        receiver = new MessageReceiver();
        registerReceiver(receiver, receiver.getIntentFilter());
    }

    /**
     * Called by the system to notify a Service that it is no longer used and is being removed.  The
     * service should clean up any resources it holds (threads, registered
     * receivers, etc) at this point.  Upon return, there will be no more calls
     * in to this Service object and it is effectively dead.  Do not call this method directly.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(receiver!=null)unregisterReceiver(receiver);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
