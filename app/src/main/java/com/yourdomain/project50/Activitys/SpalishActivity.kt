package com.yourdomain.project50.Activitys

import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.yourdomain.project50.R


class SpalishActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spalish)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val w = window
            w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
        val image= findViewById<ImageView>(R.id.spalishImageVIew)
        val tvSpalishTitle=findViewById<TextView>(R.id.tvSpalishTitle);

        tvSpalishTitle.startAnimation(AnimationUtils.loadAnimation(this, R.anim.animation_text_view));
        Glide.with(this).load(R.drawable.spalish4).into(image)

        val handler = Handler()
        handler.postDelayed(object :Runnable{
            override fun run() {
                startActivity(Intent(this@SpalishActivity,ScaleActivity::class.java))
            }

        },3000)
    }
}
