package com.yourdomain.project50.WorkMangers

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import java.util.concurrent.TimeUnit

/**
 * Created by hamza rafiq on 12/3/18.
 */
class RemindMeEveryDayWorkManger(context: Context, params: WorkerParameters) : ComeBackLatterWorkManger(context, params) {
    override fun doWork(): Result {
       super.doWork()
        val repativeWork = PeriodicWorkRequest.Builder(ComeBackLatterWorkManger::class.java,24, TimeUnit.HOURS)
        val workManager = WorkManager.getInstance()
        workManager.enqueue(repativeWork.build())
        return Result.SUCCESS
    }
}