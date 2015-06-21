package go.deyu.dailytodo.app;

import android.app.Application;

import go.deyu.dailytodo.receiver.MessageReceiver;
import go.deyu.util.AppContextSingleton;
import go.deyu.util.LOG;

/**
 * Created by huangeyu on 15/5/21.
 */
public class App extends Application {

    private MessageReceiver receiver ;
    private final String TAG =getClass().getSimpleName();

//    in app create init some util tool and register MessageReceiver
    @Override
    public void onCreate() {
        super.onCreate();
        LOG.LOGTAG = getString(getApplicationInfo().labelRes);
        LOG.d(TAG , "onCreate");
        AppContextSingleton.initialize(this);
        receiver = new MessageReceiver();
        registerReceiver(receiver , receiver.getIntentFilter());
    }


}
