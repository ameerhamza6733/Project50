package com.yourdomain.project50.Activitys

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import com.bumptech.glide.Glide
import com.yourdomain.project50.Model.MoreApps
import com.yourdomain.project50.R
import com.yourdomain.project50.ViewModle.MoreAppViewModle
import kotlinx.android.synthetic.main.each_excersize_day.*
import java.util.ArrayList

class CongragulationActivity : AppCompatActivity() {

    private lateinit var tvDayComleted:TextView
    private lateinit var tvCalriesBurn:TextView
    private lateinit var tvTotleExcersize:TextView
    private lateinit var ivCongragulation:ImageView
    private lateinit var tvDuration:TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
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


        val viewModle= ViewModelProviders.of(this).get(MoreAppViewModle::class.java)
        viewModle.getApps().observe(this, Observer {
            if (it!=null){
                progressBar.visibility=View.INVISIBLE
                val llm = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                recyclerView.layoutManager = llm
                recyclerView.adapter=MoreAppsAdupter(it)
            }
        })
    }

    private fun findVIews() {
        tvCalriesBurn=findViewById(R.id.tvTotaleCalresBurn)
        tvDayComleted=findViewById(R.id.tvDaysCompelted)
        tvTotleExcersize=findViewById(R.id.tvTotleExcersizesDone)
        tvDuration=findViewById(R.id.tvDuraction)
        ivCongragulation=findViewById(R.id.icon_congragulation)
        recyclerView=findViewById(R.id.recylerview)
        progressBar=findViewById(R.id.progressBar)
        btClose=findViewById(R.id.btClose)
        btShare=findViewById(R.id.btShare)

    }
    inner class MoreAppsAdupter(val moreApps:  ArrayList<MoreApps>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return when (viewType) {
                MoreApps.VIEW_TYPE_APP -> {
                    MoreAppViewHolder(LayoutInflater.from(p0.context)
                            .inflate(R.layout.each_more_app, p0, false));
                }
                MoreApps.VIEW_TYPE_AD  -> {
                    AdViewHolderViewHolder(LayoutInflater.from(p0.context)
                            .inflate(R.layout.emptyview, p0, false));
                }
                else -> {
                    MoreAppViewHolder(LayoutInflater.from(p0.context)
                            .inflate(R.layout.each_more_app, p0, false));
                }
            }


        }

        override fun getItemCount(): Int {
            return moreApps.size
        }

        override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
            if (MoreApps.VIEW_TYPE_APP == p0.itemViewType) {
                p0 as MoreAppViewHolder
                   p0.tvtitle.text=moreApps.get(p0.adapterPosition).appName
                Glide.with(p0.context).load(moreApps.get(p0.adapterPosition).appIcon).into(p0.icon)
            } else if (MoreApps.VIEW_TYPE_AD == p0.itemViewType) {
                p0 as AdViewHolderViewHolder

            }

        }

        override fun getItemViewType(position: Int): Int {
            return moreApps[position].viewType
        }

        inner class MoreAppViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var tvtitle: TextView
            var icon:ImageView
            var context:Context

            init {
                itemView.isFocusableInTouchMode = true
                tvtitle = itemView.findViewById(R.id.appTitle)
               icon=itemView.findViewById(R.id.appIcon)
                context=itemView.context


            }


        }


        inner class AdViewHolderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            // var nativeAd: TextView

            init {

                // nativeAd = itemView.findViewById(R.id.nativeAd)
                // nativeAd.visibility=View.GONE
            }
        }
    }

}
