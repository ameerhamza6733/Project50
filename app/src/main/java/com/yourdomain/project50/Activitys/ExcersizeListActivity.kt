package com.yourdomain.project50.Activitys

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.yourdomain.project50.Model.Excesizes
import com.yourdomain.project50.R
import com.yourdomain.project50.ViewModle.GetFullBodyPlanceExcersizesByDayViewModle
import kotlinx.android.synthetic.main.each_excersize_day.*

class ExcersizeListActivity : AppCompatActivity() {

    companion object {
        val EXTRA_DAY = "extra day";
        val TAG = "ExcersizeListActivity";
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_excersize_list)
        val recyclerView = findViewById<RecyclerView>(R.id.recylerview)
        val modle = ViewModelProviders.of(this)[GetFullBodyPlanceExcersizesByDayViewModle::class.java]
        modle.getExcersizs(intent.getIntExtra(EXTRA_DAY, -2))?.observe(this, Observer {
           if (it!=null){
               recyclerView.layoutManager=LinearLayoutManager(this@ExcersizeListActivity)
               recyclerView.adapter=ExcersizeAdupter(it)
           }
        })
    }

    inner class ExcersizeAdupter(val excesizes: Excesizes) : RecyclerView.Adapter<ExcersizeAdupter.ExcersizeViewHolder>() {

        override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): ExcersizeAdupter.ExcersizeViewHolder {

            return ExcersizeViewHolder(LayoutInflater.from(p0.context)
                    .inflate(R.layout.each_excersize, p0, false));
        }

        override fun getItemCount(): Int {
            return excesizes.title.size
        }

        override fun onBindViewHolder(p0:ExcersizeAdupter.ExcersizeViewHolder, p1: Int) {

            p0.tvtitle.text=excesizes.title[p0.adapterPosition]
            p0.tvTotalDays.text=excesizes.seconds[p0.adapterPosition].toString()
            Glide.with(p0.itemView.context).asGif().load(excesizes.icons[p0.adapterPosition]).into(p0.image)
        }


        inner class ExcersizeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var tvtitle: TextView
            var image: ImageView
            var tvTotalDays: TextView

            init {
                tvtitle = itemView.findViewById(R.id.tvExcersizeTitle)
                image = itemView.findViewById(R.id.ivIcon)
                tvTotalDays = itemView.findViewById(R.id.tvTime)
                itemView.setOnClickListener {

                }
            }


        }


    }

}
