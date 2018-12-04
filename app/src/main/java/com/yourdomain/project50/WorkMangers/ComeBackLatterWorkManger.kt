package com.yourdomain.project50.WorkMangers

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast

import com.yourdomain.project50.R

import androidx.work.Worker
import androidx.work.WorkerParameters
import java.util.*


/**
 * Created by apple on 12/3/18.
 */

open class ComeBackLatterWorkManger(context: Context, params: WorkerParameters) : Worker(context, params) {

    init {
        Log.d(TAG, "worker thread created")
    }

    override fun doWork(): Result {
        Log.d(TAG, "dowork")
getNotification(applicationContext)

        return Result.SUCCESS
    }

    companion object {

        val POST_NOTIFICATION_WORKER_ARG = "ComeBackLatterWorkManger"
        val TAG = "ComeBackworkTAG"
        private val ONGOING_NOTIFICATION_ID = 1133
    }
    fun getNotification(context: Context): Notification {
        val mNotificationManager1: NotificationManager?

        val notification: Notification

        mNotificationManager1 = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            createChannel(mNotificationManager1)

        val contentView = RemoteViews(applicationContext.packageName, R.layout.custom_notifaction)

        val mBuilder = NotificationCompat.Builder(applicationContext,applicationContext.getString(R.string.app_name))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setCustomBigContentView(contentView)
                .setPriority(getPriorty())
                . setWhen(Calendar.getInstance().timeInMillis);
        notification = mBuilder.build()

        mNotificationManager1.notify(ONGOING_NOTIFICATION_ID, notification)
        return notification
    }

    @TargetApi(26)
    @Synchronized
    private fun createChannel(notificationManager: NotificationManager?) {
        val name = "lockScreen"
        val importance = NotificationManager.IMPORTANCE_DEFAULT

        val mChannel = NotificationChannel(applicationContext.getString(R.string.app_name), name, importance)

        mChannel.enableLights(true)
        mChannel.lightColor = Color.BLUE
        notificationManager!!.createNotificationChannel(mChannel)
    }

    private fun getPriorty():Int{
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            NotificationManager.IMPORTANCE_HIGH
        }else{
            Notification.PRIORITY_HIGH
        }
    }
}