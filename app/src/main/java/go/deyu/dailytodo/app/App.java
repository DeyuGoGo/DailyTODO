package go.deyu.dailytodo.app;

import android.app.Application;

import go.deyu.dailytodo.receiver.MessageReceiver;
import go.deyu.util.AppContextSingleton;

/**
 * Created by huangeyu on 15/5/21.
 */
public class App extends Application {



    private MessageReceiver receiver ;
    @Override
    public void onCreate() {
        super.onCreate();
        AppContextSingleton.initialize(this);
        receiver = new MessageReceiver();
        registerReceiver(receiver , receiver.getIntentFilter());
    }

}
