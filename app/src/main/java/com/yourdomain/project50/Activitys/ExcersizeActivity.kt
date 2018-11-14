package com.yourdomain.project50.Activitys

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AppCompatActivity
import com.yourdomain.project50.Fragments.WatingFragment
import com.yourdomain.project50.MY_Shared_PREF
import com.yourdomain.project50.Model.ExcersizeDays
import com.yourdomain.project50.Model.Excesizes
import com.yourdomain.project50.R
import com.yourdomain.project50.Utils.CountTotalTime
import com.yourdomain.project50.ViewModle.GetFullBodyPlanceExcersizesByDayViewModle

class ExcersizeActivity : AppCompatActivity() {

    companion object {
        val EXTRA_DAY = "ExcersizeActivity.extra day";
    }

    private var currentDay: ExcersizeDays? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.each_full_screen_excersize)
        val currentDayKey = intent.getIntExtra(EXTRA_DAY, -2)
        if (currentDayKey != -2) {
            currentDay = MY_Shared_PREF.getCurrentDay(application, currentDayKey.toString())

        }
        val modle = ViewModelProviders.of(this)[GetFullBodyPlanceExcersizesByDayViewModle::class.java]
        modle.getExcersizs(currentDayKey)?.observe(this, Observer {
            if (it != null) {
                var string=""
                if (it.viewType[0]==Excesizes.VIEW_TYPE_LIMTED_EXCERSIZE){
                    string =it.seconds[0].toString()+"''"
                }else if (it.viewType[0] == Excesizes.VIEW_TYPE_UN_LIMTED_EXCERSIZE){
                    string="x"+it.seconds[0].toString()
                }
                val fragmet = WatingFragment.newInstance(CountTotalTime(it.seconds).toInt(), string, it.detail[0])
               fragmet.setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                fragmet.show(supportFragmentManager, null)
            }
        })


    }
}
