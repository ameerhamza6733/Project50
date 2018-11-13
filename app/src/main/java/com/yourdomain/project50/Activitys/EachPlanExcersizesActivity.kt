package com.yourdomain.project50.Activitys

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.*
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.yourdomain.project50.Fragments.BlankFragment
import com.yourdomain.project50.Model.ExcersizeDays
import com.yourdomain.project50.Model.ExcersizePlans
import com.yourdomain.project50.R
import com.yourdomain.project50.ViewModle.ExcersizeDayGernaterViewModle
import com.yourdomain.project50.ViewModle.ExcersizePlansViewModle
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.view.Menu
import com.yourdomain.project50.Adupters.MainPagerAdapter
import kotlinx.android.synthetic.main.app_bar_main.*
import java.util.*


class EachPlanExcersizesActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var mPager: ViewPager
    private  val NUM_PAGES = 5


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_days_excersizes)
        val mToolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(mToolbar)
        recyclerView = findViewById(R.id.excersizeType)
        mPager = findViewById(R.id.viewpager)
        intiDataSet()
        val pagerAdapter = ScreenSlidePagerAdapter(supportFragmentManager)
        mPager.adapter = pagerAdapter



        //  navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    private fun intiDataSet() {
        val model = ExcersizePlansViewModle(application)
        var list = model.getExcersizePlans();
        if (list.size > 0) {
            var excersizeAdupter = ExcersizePlansAdupter(list);
            recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            recyclerView.adapter = excersizeAdupter
        }




    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }
    inner class ExcersizePlansAdupter(val excersizePlans: MutableList<ExcersizePlans>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return when (viewType) {
                ExcersizePlans.TYPE_EXCERSISE -> {
                    ExcersizeViewHolder(LayoutInflater.from(p0.context)
                            .inflate(R.layout.each_excersize_plan_horizental_row, p0, false));
                }
                ExcersizePlans.TYPE_AD -> {
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

        override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {

            if (ExcersizePlans.TYPE_EXCERSISE == p0.itemViewType) {
                p0 as ExcersizeViewHolder
                p0.tvtitle.text = excersizePlans[p0.adapterPosition].name
                Glide.with(p0.tvtitle.context).load(excersizePlans[p0.adapterPosition].image).into(p0.image)

            } else if (ExcersizePlans.TYPE_AD == p0.itemViewType) {
                p0 as AdViewHolderViewHolder
            }

        }

        override fun getItemViewType(position: Int): Int {
            return excersizePlans[position].ViewType
        }

        inner class ExcersizeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var tvtitle: TextView
            var image: ImageView
            var tvTotalDays: TextView

            init {
                tvtitle = itemView.findViewById(R.id.excersizeTitle)
                image = itemView.findViewById(R.id.image)
                tvTotalDays = itemView.findViewById(R.id.tvTotalDays)
                itemView.setOnClickListener {
                    if (adapterPosition==0){

                    }
                }
            }


        }


        inner class AdViewHolderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var nativeAd: TextView

            init {

                nativeAd = itemView.findViewById(R.id.nativeAd)
            }
        }
    }

    private inner class ScreenSlidePagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
        override fun getCount(): Int = NUM_PAGES

        override fun getItem(position: Int): Fragment = BlankFragment()
    }


}
