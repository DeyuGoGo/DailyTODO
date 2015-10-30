package go.deyu.dailytodo.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import go.deyu.dailytodo.R;
import go.deyu.util.AppContextSingleton;

/**
 * Created by huangeyu on 15/5/21.
 */

public class Noti {

    public static void showNotification(String Message , int id){
        Context context = AppContextSingleton.getApplicationContext();
        String title = context.getResources().getString(R.string.app_name);
        Bitmap BIcon = BitmapFactory.decodeResource(context.getResources(),R.drawable.dailytodoicon);
        Notification noti = new Notification.Builder(context)
                .setContentTitle(title)
                .setContentText(Message)
                .setLargeIcon(BIcon)
                .setSmallIcon(R.drawable.wallclock)
                .setContentIntent(PendingIntent.getActivity(context, id, getLauncherIntent(context), PendingIntent.FLAG_UPDATE_CURRENT))
                .build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(id, noti);
    }

    public static Intent getLauncherIntent(Context context){
        return context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
    }
}
