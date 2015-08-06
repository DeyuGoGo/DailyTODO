package go.deyu.dailytodo.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import go.deyu.dailytodo.R;
import go.deyu.util.AppContextSingleton;

/**
 * Created by huangeyu on 15/5/21.
 */
public class Noti {

    public static void showNotification(String Message , int id){
        Context context = AppContextSingleton.getApplicationContext();
        Notification noti = new Notification.Builder(context)
                .setContentTitle("Daliy TODO")
                .setContentText(Message)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(PendingIntent.getActivity( context ,id , getLauncherIntent(context) , PendingIntent.FLAG_UPDATE_CURRENT))
                .build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(id, noti);
    }
//    TODO to cancel Intent
    private Intent getCancelIntent(){
        Intent p = new Intent();
        return p;
    }
    private static Intent getLauncherIntent(Context context){
        return context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
    }
}
