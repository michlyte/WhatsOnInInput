package com.gghouse.woi.whatsonininput.util;

import android.support.v4.app.NotificationCompat;

import com.gghouse.woi.whatsonininput.R;
import com.gghouse.woi.whatsonininput.WOIInputApplication;
import com.gghouse.woi.whatsonininput.common.Config;

/**
 * Created by michael on 5/4/2017.
 */

public abstract class LocalNotificationHelper {
    public static void postNotification(String title, String text) {
        NotificationCompat.Builder mNotifyBuilder =
                new NotificationCompat.Builder(WOIInputApplication.getInstance().getAppContext())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(text);

        WOIInputApplication.getInstance().getNotificationManager().notify(
                Config.NT_UPLOAD_ID,
                mNotifyBuilder.build());
    }
}
