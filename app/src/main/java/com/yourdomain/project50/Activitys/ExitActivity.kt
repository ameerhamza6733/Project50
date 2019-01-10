package com.yourdomain.project50.Activitys

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat.startActivity
import android.content.Intent
import com.yourdomain.project50.TTSHelperService


class ExitActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val stopIntent = Intent(this, TTSHelperService::class.java)
        stopIntent.action=TTSHelperService.ACTION_STOP
        startService(stopIntent)
        finish()
    }


}
