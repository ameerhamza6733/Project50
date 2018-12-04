package com.yourdomain.project50.WorkMangers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import com.yourdomain.project50.R;

import androidx.work.Worker;
import androidx.work.WorkerParameters;


/**
 * Created by apple on 12/3/18.
 */

public class ComeBackLatterWorkManger extends Worker {

    public static final String POST_NOTIFICATION_WORKER_ARG="ComeBackLatterWorkManger";
    public static final String TAG="ComeBackworkTAG";

    public ComeBackLatterWorkManger(@NonNull Context context, @NonNull WorkerParameters params) {super(context, params);
    Log.d(TAG,"worker thread created");
    }

    @NonNull
    @Override
    public Worker.Result doWork() {
        Log.d(TAG,"dowork");
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "CHANNEL_ID")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getApplicationContext().getString(R.string.app_name))
                .setContentText(getApplicationContext().getString(R.string.comeback_notification_title))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        notificationManager.notify(11, mBuilder.build());
        createNotificationChannel();

        return Result.SUCCESS;
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getApplicationContext().getString(R.string.comeback_notification_title);
            String description = getApplicationContext().getString(R.string.comeback_notification_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("CHANNEL_ID", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getApplicationContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}