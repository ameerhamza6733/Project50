package com.yourdomain.project50.Activitys

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.OnChildAttachStateChangeListener
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.yourdomain.project50.Fragments.ABSPlanDayFragment
import com.yourdomain.project50.Fragments.ButtPlanDayFragment
import com.yourdomain.project50.Fragments.FullBodyPlanDayFragment
import com.yourdomain.project50.Fragments.RateUsFragment
import com.yourdomain.project50.Model.ExcersizePlans
import com.yourdomain.project50.R
import com.yourdomain.project50.ViewModle.ExcersizePlansViewModle
import kotlinx.android.synthetic.main.activity_days_excersizes.*


class EachPlanExcersizesActivity : AppCompatActivity() {
    companion object {

        private val MIN_SCALE = 0.65f
        private val MIN_ALPHA = 0.3f
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                startActivity(Intent(this@EachPlanExcersizesActivity,SettingsActivity::class.java))
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }
    protected val TAG = "ExcersizesActivity";
    private lateinit var recyclerView: RecyclerView
    private lateinit var mPager: com.yourdomain.project50.Adupters.MYViewPager
    private var currentExcersizePlan=ExcersizePlans.PLAN_FULL_BODY
    private val NUM_PAGES = 3


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_days_excersizes)
        val mToolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(mToolbar)
        recyclerView = findViewById(R.id.excersizeType)
        recyclerView.setHasFixedSize(true)
        mPager = findViewById(R.id.viewpager)
        intiDataSet()
        val pagerAdapter = ScreenSlidePagerAdapter(supportFragmentManager)
        //     val fadeOutTransformation = FadeOutTransformation()
         // mPager.setPageTransformer(true, fadeOutTransformation);

        mPager.adapter = pagerAdapter
          navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    private fun intiDataSet() {
        val model = ExcersizePlansViewModle(application)
        var list = model.getExcersizePlans();
        if (list.size > 0) {
            list.add(0, ExcersizePlans("native ad", 0, 0, "native ad id", ExcersizePlans.TYPE_AD))
            var excersizeAdupter = ExcersizePlansAdupter(list);
            val llm = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

            recyclerView.layoutManager = llm
            recyclerView.addOnChildAttachStateChangeListener(ChildAttachListener(llm))
            recyclerView.adapter = excersizeAdupter
        }


    }

    override fun onBackPressed() {
        val rateUsFragment = RateUsFragment()
        rateUsFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.dialog);
        rateUsFragment.show(supportFragmentManager, "rateUsFragment")
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
                            .inflate(R.layout.emptyview, p0, false));
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
                //  Log.d(TAG,"onBind"+p1 +" "+p0.adapterPosition);
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
                itemView.isFocusableInTouchMode = true
                tvtitle = itemView.findViewById(R.id.excersizeTitle)
                image = itemView.findViewById(R.id.image)
                tvTotalDays = itemView.findViewById(R.id.tvTotalDays)
                itemView.setOnFocusChangeListener { v, hasFocus ->
                    //  Log.d(TAG, "focus: $hasFocus  $adapterPosition")
                }


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

    private inner class ChildAttachListener(internal var llm: LinearLayoutManager) : OnChildAttachStateChangeListener {

        override fun onChildViewAttachedToWindow(view: View) {

            val handler = Handler()
            handler.post(Runnable {
                Log.d(TAG, "onChildViewAttachedToWindow" + llm.findFirstCompletelyVisibleItemPosition())
                when (llm.findFirstCompletelyVisibleItemPosition()) {
                    0 -> {
                        currentExcersizePlan=ExcersizePlans.PLAN_FULL_BODY
                        mPager.setCurrentItem(0)
                    }
                    2 -> {
                        currentExcersizePlan=ExcersizePlans.PLAN_ABS
                        mPager.setCurrentItem(1)
                    }
                    3 -> {
                        currentExcersizePlan=ExcersizePlans.PLAN_BUTT
                        mPager.setCurrentItem(2)
                    }
                }

            })
        }

        override fun onChildViewDetachedFromWindow(view: View) {
            val handler = Handler()
            handler.post(Runnable {
                Log.d(TAG, "onChildViewDetachedFromWindow" + llm.findLastCompletelyVisibleItemPosition())

            })
        }
    }

    private inner class ScreenSlidePagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
        override fun getCount(): Int = NUM_PAGES

        override fun getItem(position: Int): Fragment? {
            when (position) {
                0 -> {
                    return FullBodyPlanDayFragment()
                }
                1 -> {
                    return ABSPlanDayFragment()
                }
                2 -> {
                    return ButtPlanDayFragment()
                }
            }
            return null
        }
    }

    inner class FadeOutTransformation : ViewPager.PageTransformer {
        override fun transformPage(page: View, position: Float) {

            mPager.translationX = -position * page.width

            mPager.alpha = 1 - Math.abs(position)


        }
    }



}
