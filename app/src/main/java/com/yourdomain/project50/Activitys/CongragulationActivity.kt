package com.yourdomain.project50.Activitys

import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.yourdomain.project50.R
import kotlinx.android.synthetic.main.each_excersize_day.*

class CongragulationActivity : AppCompatActivity() {

    private lateinit var tvDayComleted:TextView
    private lateinit var tvCalriesBurn:TextView
    private lateinit var tvTotleExcersize:TextView
    private lateinit var ivCongragulation:ImageView
    private lateinit var tvDuration:TextView
    private lateinit var btClose:ImageButton
    private lateinit var btShare:ImageButton

    companion object {
        val EXTRA_DAY="CongragulationActivity.extraday";
        val EXTRA_DURACTION="CongragulationActivity.EXTRA_DURACTION"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_congragulation)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val w = window
            w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
        findVIews()

        tvDayComleted.text=intent?.getIntExtra(EXTRA_DAY,0).toString()
        tvDuration.text=intent?.getStringExtra(EXTRA_DURACTION)
        Glide.with(this).load(R.drawable.congragulation_cup).into(ivCongragulation)
        btClose.setOnClickListener { finish() }
        btShare.setOnClickListener { Snackbar.make(btShare,"Share action pressed",Snackbar.LENGTH_SHORT).show() }
    }

    private fun findVIews() {
        tvCalriesBurn=findViewById(R.id.tvTotaleCalresBurn)
        tvDayComleted=findViewById(R.id.tvDaysCompelted)
        tvTotleExcersize=findViewById(R.id.tvTotleExcersizesDone)
        tvDuration=findViewById(R.id.tvDuraction)
        ivCongragulation=findViewById(R.id.icon_congragulation)
        btClose=findViewById(R.id.btClose)
        btShare=findViewById(R.id.btShare)

    }
}
