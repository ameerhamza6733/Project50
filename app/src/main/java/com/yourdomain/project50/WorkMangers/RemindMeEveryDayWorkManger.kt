package com.yourdomain.project50.WorkMangers

import android.content.Context
import androidx.work.*
import com.yourdomain.project50.Model.ComeBackLatter
import java.util.concurrent.TimeUnit

/**
 * Created by hamza rafiq on 12/3/18.
 */
class RemindMeEveryDayWorkManger(context: Context, params: WorkerParameters) : ComeBackLatterWorkManger(context, params) {
    override fun doWork(): Result {
       super.doWork()
        val postNotationWithDelay = OneTimeWorkRequest
                .Builder(RemindMeEveryDayWorkManger::class.java)
                .setInitialDelay(24, TimeUnit.HOURS).build()

        val workManager = WorkManager.getInstance()
        workManager.beginUniqueWork(
                "RemindMeEveryDayWorkMangerTAG",
                ExistingWorkPolicy.REPLACE,
                postNotationWithDelay
        ).enqueue()
        return Result.SUCCESS
    }

    override fun comeBackLatter(): ComeBackLatter? {
        return null
    }
}