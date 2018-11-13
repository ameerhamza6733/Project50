package com.yourdomain.project50.Activitys

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.yourdomain.project50.R
import com.yourdomain.project50.ViewModle.ExcersizeDayGernaterViewModle
import com.yourdomain.project50.ViewModle.GetFullBodyPlanceExcersizesByDayViewModle

class ExcersizeListActivity : AppCompatActivity() {

    companion object {
       val EXTRA_DAY="extra day";
        val TAG="ExcersizeListActivity";
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_excersize_list)
        val modle = ViewModelProviders.of(this)[GetFullBodyPlanceExcersizesByDayViewModle::class.java]
        modle.getExcersizs(intent.getIntExtra(EXTRA_DAY,-2))?.observe(this, Observer {
            Log.d(TAG,""+it?.icons?.size);
        })
    }
}
