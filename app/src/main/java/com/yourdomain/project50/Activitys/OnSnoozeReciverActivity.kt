package com.yourdomain.project50.Activitys

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.yourdomain.project50.WorkMangers.ComeBackLatterWorkManger
import java.util.concurrent.TimeUnit

class OnSnoozeReciverActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        shoozeTheExcersize()
        Toast.makeText(this,"We will you remind in 30 mints:",Toast.LENGTH_LONG).show()
        finish()
    }

    private fun shoozeTheExcersize(){

        val oneTimeSnoozeWorkRequst= OneTimeWorkRequest.Builder(ComeBackLatterWorkManger::class.java).setInitialDelay(30, TimeUnit.MINUTES).build()

        WorkManager.getInstance().beginUniqueWork("shoozeTheExcersize", ExistingWorkPolicy.REPLACE,
                oneTimeSnoozeWorkRequst).enqueue()


    }
}
