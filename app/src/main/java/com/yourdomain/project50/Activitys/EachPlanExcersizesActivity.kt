package com.yourdomain.project50.Activitys

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
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
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.ads.consent.ConsentInformation
import com.google.ads.consent.ConsentStatus
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.reward.RewardedVideoAd
import com.yourdomain.project50.Fragments.ABSPlanDayFragment
import com.yourdomain.project50.Fragments.ButtPlanDayFragment
import com.yourdomain.project50.Fragments.FullBodyPlanDayFragment
import com.yourdomain.project50.Fragments.RateUsFragment
import com.yourdomain.project50.MY_Shared_PREF
import com.yourdomain.project50.Model.Admob
import com.yourdomain.project50.Model.AppAdmobDataFromFirebase
import com.yourdomain.project50.Model.AppSettingsFromFireBase
import com.yourdomain.project50.Model.ExcersizePlan
import com.yourdomain.project50.R
import com.yourdomain.project50.ViewModle.AppSettingsFromFirebaseViewModle
import com.yourdomain.project50.ViewModle.ExcersizePlansViewModle
import kotlinx.android.synthetic.main.activity_days_excersizes.*


class EachPlanExcersizesActivity : AppCompatActivity() {
    companion object {
        protected val TAG = "ExcersizesActivity";
        public val EXTRA_PLAN = "EXTRA_PLAN";
        private val MIN_SCALE = 0.65f
        private val MIN_ALPHA = 0.3f
        var mRewardedVideoAd: RewardedVideoAd? = null
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                startActivity(Intent(this@EachPlanExcersizesActivity, PreviewColumnChartActivity::class.java))

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                startActivity(Intent(this@EachPlanExcersizesActivity, SettingsActivity::class.java))
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }


    private lateinit var recyclerView: RecyclerView
    private lateinit var blubGifView:ImageView
    private lateinit var boxTick:ImageView
    private lateinit var mPager: com.yourdomain.project50.Adupters.MYViewPager
    private var currentExcersizePlan = ExcersizePlan.PLAN_FULL_BODY
    private var mAdmobSetingsFromFirebase: AppAdmobDataFromFirebase? = null


    private val NUM_PAGES = 3
    private var extraPlan = 0;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_days_excersizes)
        val mToolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(mToolbar)

        extraPlan = intent?.getIntExtra(EXTRA_PLAN, 0)!!
        recyclerView = findViewById(R.id.excersizeType)
        blubGifView=findViewById(R.id.blubGifView)
        boxTick=findViewById(R.id.tickBox)

        recyclerView.setHasFixedSize(true)
        mPager = findViewById(R.id.viewpager)
        intiDataSet()
        val pagerAdapter = ScreenSlidePagerAdapter(supportFragmentManager)
        //     val fadeOutTransformation = FadeOutTransformation()
        // mPager.setPageTransformer(true, fadeOutTransformation);

        mPager.adapter = pagerAdapter
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mAdmobSetingsFromFirebase = MY_Shared_PREF.getFirebaseAdmobAppSettings(application)
        if (ConsentInformation.getInstance(this).consentStatus == ConsentStatus.PERSONALIZED) {
            showNonPersonalizedAds()
        } else if (ConsentInformation.getInstance(this).consentStatus == ConsentStatus.NON_PERSONALIZED) {
            showNonPersonalizedAds()
        } else {
            showPersonalizedAds()
        }

        Glide.with(this).asGif().load(R.drawable.blubgif).into(blubGifView)
        Glide.with(this).asBitmap().load(R.drawable.ic_tick_white_24dp).into(boxTick)

        blubGifView.setOnClickListener {
            if (mRewardedVideoAd?.isLoaded==true){
                mRewardedVideoAd?.show()
            }else{
                Toast.makeText(this,"Ad not loaded",Toast.LENGTH_LONG).show()
            }
        }
        boxTick.setOnClickListener {
            val intent = Intent(this@EachPlanExcersizesActivity,MainActivity::class.java)
            startActivity(intent)
        }
    }



    private fun showPersonalizedAds() {
        var adId: String = Admob.REWADEDR_VIDEO_AD_ID
        mAdmobSetingsFromFirebase?.admobAds?.videoAds?.id?.let {
            adId = it
        }
        mRewardedVideoAd?.loadAd(adId,
                AdRequest.Builder().build())

    }

    private fun showNonPersonalizedAds() {
        var adId: String = Admob.REWADEDR_VIDEO_AD_ID
        mAdmobSetingsFromFirebase?.admobAds?.videoAds?.id?.let {
            adId = it
        }
        mRewardedVideoAd?.loadAd(adId,
                AdRequest.Builder()
                        .addNetworkExtrasBundle(AdMobAdapter::class.java, getNonPersonalizedAdsBundle())
                        .build())

    }

    fun getNonPersonalizedAdsBundle(): Bundle {
        val extras = Bundle()
        extras.putString("npa", "1")

        return extras
    }

    private fun intiDataSet() {
        val model = ExcersizePlansViewModle(application)
        var list = model.getExcersizePlans();
        if (list.size > 0) {
            list.add(0, ExcersizePlan("native ad", 0, 0, -1, ExcersizePlan.TYPE_AD))

            var excersizeAdupter = ExcersizePlansAdupter(list);
            val llm = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

            recyclerView.layoutManager = llm
            recyclerView.addOnChildAttachStateChangeListener(ChildAttachListener(llm))
            recyclerView.adapter = excersizeAdupter
            recyclerView.scrollToPosition(extraPlan + 1)
        }


    }

    override fun onBackPressed() {
        val rateUsFragment = RateUsFragment()
        rateUsFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.dialog);
        rateUsFragment.show(supportFragmentManager, "rateUsFragment")
    }



    inner class ExcersizePlansAdupter(val excersizePlans: MutableList<ExcersizePlan>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return when (viewType) {
                ExcersizePlan.TYPE_EXCERSISE -> {
                    ExcersizeViewHolder(LayoutInflater.from(p0.context)
                            .inflate(R.layout.each_excersize_plan_horizental_row, p0, false));
                }
                ExcersizePlan.TYPE_AD -> {
                    AdViewHolderViewHolder(LayoutInflater.from(p0.context)
                            .inflate(R.layout.emptyview, p0, false));
                }
                else -> {
                    ExcersizeViewHolder(LayoutInflater.from(p0.context)
                            .inflate(R.layout.emptyview, p0, false));
                }
            }


        }


        override fun getItemCount(): Int {
            return excersizePlans.size
        }

        override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
            if (ExcersizePlan.TYPE_EXCERSISE == p0.itemViewType) {
                p0 as ExcersizeViewHolder
                var daysComplted = (excersizePlans[p0.adapterPosition].totalDays - excersizePlans[p0.adapterPosition].completedDays)
                p0.tvtitle.text = excersizePlans[p0.adapterPosition].name
                Glide.with(p0.tvtitle.context).load(excersizePlans[p0.adapterPosition].image).apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE)).into(p0.image)
                p0.daysProgressBar.progress = excersizePlans[p0.adapterPosition].completedDays
                p0.tvTotalDaysLeft.text = "Days left " + daysComplted

                //  Log.d(TAG,"onBind"+p1 +" "+p0.adapterPosition);
                Log.d(TAG, "totale day completed by user " + excersizePlans[p0.adapterPosition].completedDays)
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
            var daysProgressBar: ProgressBar

            init {
                itemView.isFocusableInTouchMode = true
                tvtitle = itemView.findViewById(R.id.excersizeTitle)
                image = itemView.findViewById(R.id.image)
                tvTotalDaysLeft = itemView.findViewById(R.id.tvDaysLift)
                daysProgressBar = itemView.findViewById(R.id.progressBar)
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
                    1 -> {
                        currentExcersizePlan = ExcersizePlan.PLAN_FULL_BODY
                        mPager.setCurrentItem(0)
                    }
                    2 -> {
                        currentExcersizePlan = ExcersizePlan.PLAN_ABS
                        mPager.setCurrentItem(1)
                    }
                    3 -> {
                        currentExcersizePlan = ExcersizePlan.PLAN_BUTT
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
