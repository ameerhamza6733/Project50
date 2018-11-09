package com.yourdomain.project50.Activitys

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.yourdomain.project50.R


class SpalishActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spalish)

        val image= findViewById<ImageView>(R.id.spalishImageVIew)
        val tvSpalishTitle=findViewById<TextView>(R.id.tvSpalishTitle);

        tvSpalishTitle.startAnimation(AnimationUtils.loadAnimation(this, R.anim.animation_text_view));
        Glide.with(this).load(R.drawable.spalish4).into(image)
    }
}
