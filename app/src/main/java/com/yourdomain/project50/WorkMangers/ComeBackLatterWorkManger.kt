package com.yourdomain.project50.WorkMangers

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.util.Log
import android.widget.RemoteViews
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.yourdomain.project50.Activitys.ExcersizeActivity
import com.yourdomain.project50.Activitys.ExcersizeListActivity
import com.yourdomain.project50.MY_Shared_PREF
import com.yourdomain.project50.R
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
        private val RESUME_EXCERSIZE="RESUME_EXCERSIZE"
    }

    fun getNotification(context: Context): Notification {
        val mNotificationManager1: NotificationManager?

        val notification: Notification

        mNotificationManager1 = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            createChannel(mNotificationManager1)

        val contentView = RemoteViews(applicationContext.packageName, R.layout.custom_notifaction)

        contentView.setOnClickPendingIntent(R.id.btNotfactionStart,getStartPaddingIntent())

        val mBuilder = NotificationCompat.Builder(applicationContext, applicationContext.getString(R.string.app_name))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setCustomBigContentView(contentView)
                .setPriority(getPriorty())
                .setWhen(Calendar.getInstance().timeInMillis);
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

    private fun getPriorty(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            NotificationManager.IMPORTANCE_HIGH
        } else {
            Notification.PRIORITY_HIGH
        }
    }

    private fun getStartPaddingIntent(): PendingIntent {
        val comeBackLatter=MY_Shared_PREF.getComeBackLatterExcersize(applicationContext)

        val resutmentButtonIntent = Intent(applicationContext,ExcersizeListActivity::class.java)
        resutmentButtonIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        resutmentButtonIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        resutmentButtonIntent.action=ExcersizeListActivity.ACTION_START_EXCERSIZE

        resutmentButtonIntent.putExtra(ExcersizeListActivity.EXTRA_EXCERSIZES_DONE,comeBackLatter?.complatedExcersize)
        resutmentButtonIntent.putExtra(ExcersizeListActivity.EXTRA_DAY,comeBackLatter?.excersizeDayKey)
        resutmentButtonIntent.putExtra(ExcersizeListActivity.EXTRA_PLAN,comeBackLatter?.excersizePlan)

        val  contentIntent = PendingIntent.getActivity(applicationContext, 0, resutmentButtonIntent, 0)
        return contentIntent
    }
}