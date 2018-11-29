package com.yourdomain.project50.Activitys

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.yourdomain.project50.Model.ExcersizePlan
import com.yourdomain.project50.R
import com.yourdomain.project50.ViewModle.ExcersizePlansViewModle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import com.google.android.gms.ads.NativeExpressAdView


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private var currentExcersizePlan = -1
    private val mNativeExpressAdView: NativeExpressAdView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        recyclerView = findViewById(R.id.recylerview)
        progressBar = findViewById(R.id.progressBar)
        val handler = Handler()

        handler.postDelayed({
            intiDataSet()
        }, 1000)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.main, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        when (item.itemId) {
//            R.id.action_settings -> return true
//            else -> return super.onOptionsItemSelected(item)
//        }
//    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun intiDataSet() {
        val model = ExcersizePlansViewModle(application)
        var list = model.getExcersizePlans();
        if (list.size > 0) {
            progressBar.visibility = View.INVISIBLE
            var excersizeAdupter = ExcersizeAdupter(list);
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = excersizeAdupter
        }

    }

    private inner class ExcersizeAdupter(val excersizePlans: MutableList<ExcersizePlan>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return when (viewType) {
                ExcersizePlan.TYPE_EXCERSISE -> {
                    ExcersizeViewHolder(LayoutInflater.from(p0.context)
                            .inflate(R.layout.each_excersize_plan, p0, false));
                }
                ExcersizePlan.TYPE_AD -> {
                    AdViewHolderViewHolder(LayoutInflater.from(p0.context)
                            .inflate(R.layout.native_adview, p0, false));
                }
                else -> {
                    ExcersizeViewHolder(LayoutInflater.from(p0.context)
                            .inflate(R.layout.each_excersize_plan, p0, false));
                }
            }


        }

        override fun getItemCount(): Int {
            return excersizePlans.size
        }

        val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL);

        override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {

            if (ExcersizePlan.TYPE_EXCERSISE == p0.itemViewType) {
                p0 as ExcersizeViewHolder
              var daysComplted=  (excersizePlans[p0.adapterPosition].totalDays - excersizePlans[p0.adapterPosition].completedDays)
                p0.daysProgressBar.progress=excersizePlans[p0.adapterPosition].completedDays
                p0.tvTotalDaysLeft.text="Days left "+daysComplted
                p0.tvtitle.text = excersizePlans[p0.adapterPosition].name
                Glide.with(p0.itemView.context).load(R.drawable.body_plan_background_screm).into(p0.imScrem)
                Glide.with(p0.tvtitle.context).load(excersizePlans[p0.adapterPosition].image).apply(requestOptions).into(p0.image)

            } else if (ExcersizePlan.TYPE_AD == p0.itemViewType) {
                p0 as AdViewHolderViewHolder

            }

        }

        override fun getItemViewType(position: Int): Int {
            return excersizePlans[position].ViewType
        }

        inner class ExcersizeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var tvtitle: TextView
            var image: ImageView
            var tvTotalDaysLeft: TextView
            var imScrem: ImageView
            var daysProgressBar:ProgressBar

            init {
                itemView.setOnClickListener {
                   val intent= Intent(itemView.context, EachPlanExcersizesActivity::class.java)
                    intent.putExtra(EachPlanExcersizesActivity.EXTRA_PLAN,adapterPosition)
                    itemView.context.startActivity(intent)
                    finish()
                }
                tvtitle = itemView.findViewById(R.id.excersizeTitle)
                image = itemView.findViewById(R.id.image)
                tvTotalDaysLeft = itemView.findViewById(R.id.tvDaysLift)
                imScrem = itemView.findViewById(R.id.imageViewScream);
                daysProgressBar=itemView.findViewById(R.id.progressBar)
            }
        }


        inner class AdViewHolderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var nativeAd: TextView
            var context:Context

            init {
                context=itemView.context
                nativeAd = itemView.findViewById(R.id.native_Adview)
            }
        }
    }
}



