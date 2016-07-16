package go.deyu.dailytodo.app;

import android.app.Application;
import android.content.Intent;

import go.deyu.dailytodo.AlarmMessageService;
import go.deyu.dailytodo.data.NotificationMessageRM;
import go.deyu.dailytodo.model.MessageRMMigration;
import go.deyu.util.AppContextSingleton;
import go.deyu.util.LOG;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by huangeyu on 15/5/21.
 */
public class App extends Application {

    private final String TAG =getClass().getSimpleName();

//    in app create init some util tool and register MessageReceiver
    @Override
    public void onCreate() {
        super.onCreate();
        LOG.LOGTAG = getString(getApplicationInfo().labelRes);
        LOG.d(TAG , "onCreate");
        LOG.DEBUG = false;
        AppContextSingleton.initialize(this);
        initRealm();
        Intent i = new Intent(this , AlarmMessageService.class);
        i.setAction(AlarmMessageService.ACTION_START_ALARM);
        startService(i);
    }

    private void initRealm(){
        RealmConfiguration config = new RealmConfiguration.Builder(this)
                .name("message.realm")
                .schemaVersion(1)
                .build();
        Realm.setDefaultConfiguration(config);
    }

}
