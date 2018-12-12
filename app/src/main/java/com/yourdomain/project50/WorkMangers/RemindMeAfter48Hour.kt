package com.yourdomain.project50.WorkMangers

import android.app.Application
import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

/**
 * Created by apple on 12/12/18.
 */
class RemindMeAfter48Hour(context: Context, params: WorkerParameters):   ComeBackLatterWorkManger(context, params) {
    override fun doWork(): ListenableWorker.Result {
        super.doWork()
        return ListenableWorker.Result.SUCCESS
    }
}