package com.yourdomain.project50.Activitys

import android.content.Context
import android.content.Intent
import android.graphics.Point
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
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
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
import com.google.android.gms.ads.reward.RewardItem
import com.google.android.gms.ads.reward.RewardedVideoAd
import com.google.android.gms.ads.reward.RewardedVideoAdListener
import com.yourdomain.project50.CenterZoomLayoutManager
import com.yourdomain.project50.Fragments.EachDayFragment
import com.yourdomain.project50.Fragments.RateUsFragment
import com.yourdomain.project50.MY_Shared_PREF
import com.yourdomain.project50.Model.Admob
import com.yourdomain.project50.Model.AppAdmobSettingsFromFirebase
import com.yourdomain.project50.Model.ExcersizePlan
import com.yourdomain.project50.R
import com.yourdomain.project50.ViewModle.ExcersizePlansViewModle
import kotlinx.android.synthetic.main.activity_days_excersizes.*


class EachPlanActivity : AppCompatActivity(), RewardedVideoAdListener {
    override fun onRewardedVideoAdClosed() {

    }

    override fun onRewardedVideoAdLeftApplication() {
    }

    override fun onRewardedVideoAdLoaded() {
    }

    override fun onRewardedVideoAdOpened() {
    }

    override fun onRewardedVideoCompleted() {
    }

    override fun onRewarded(p0: RewardItem?) {
    }

    override fun onRewardedVideoStarted() {
    }

    override fun onRewardedVideoAdFailedToLoad(p0: Int) {
        Log.d(TAG,"onRewardedVideoAdFailedToLoad: "+p0)
        when (p0) {
            3 -> {
                requestToLoadVideoAd()
            }
        }
    }


    companion object {
        protected val TAG = "ExcersizesActivity";
        public val EXTRA_PLAN = "EXTRA_PLAN";
        var mRewardedVideoAd: RewardedVideoAd? = null

    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                startActivity(Intent(this@EachPlanActivity, ReportsActivity::class.java))

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                startActivity(Intent(this@EachPlanActivity, SettingsActivity::class.java))
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }


    private lateinit var recyclerView: RecyclerView
    private var excersizePlansListAdupter: ExcersizePlansAdupter? = null
    private lateinit var blubGifView: ImageView
    private lateinit var boxTick: ImageView
    private lateinit var mPager: com.yourdomain.project50.Adupters.MYViewPager
    private var currentVisibalExcersizePlan = ExcersizePlan.PLAN_FULL_BODY
    private var mAdmobSetingsFromFirebase: AppAdmobSettingsFromFirebase? = null
    private lateinit var excersizePlanList: MutableList<ExcersizePlan>

    //TODO:3)AddNewPlan defile  number of plans you have
    private val NUM_PAGES = 5
    private var extraPlan = 0;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_days_excersizes)
        val mToolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(mToolbar)
        Log.d(TAG, "onCreate call");
        extraPlan = intent?.getIntExtra(EXTRA_PLAN, 0)!!
        intent?.getBooleanExtra("EXIT", false)?.let {
            if (it) {

            }
        }
        recyclerView = findViewById(R.id.excersizeType)
        blubGifView = findViewById(R.id.blubGifView)
        boxTick = findViewById(R.id.tickBox)


        mPager = findViewById(R.id.viewpager)
        intiDataSet()
        val pagerAdapter = ScreenSlidePagerAdapter(supportFragmentManager)


        mPager.adapter = pagerAdapter
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mAdmobSetingsFromFirebase = MY_Shared_PREF.getFirebaseAdmobAppSettings(application)
        requestToLoadVideoAd()
        mRewardedVideoAd?.rewardedVideoAdListener = this
        Glide.with(this).asGif().load(R.drawable.blubgif).into(blubGifView)
        Glide.with(this).asBitmap().load(R.drawable.ic_tick_white_24dp).into(boxTick)

        blubGifView.setOnClickListener {
            if (mRewardedVideoAd?.isLoaded == true) {
                mRewardedVideoAd?.show()
            } else {
                Toast.makeText(this, "Ad not loaded", Toast.LENGTH_LONG).show()
            }
        }
        boxTick.setOnClickListener {
            val intent = Intent(this@EachPlanActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun requestToLoadVideoAd() {
        if (ConsentInformation.getInstance(this).consentStatus == ConsentStatus.PERSONALIZED) {
            showNonPersonalizedAds()
        } else if (ConsentInformation.getInstance(this).consentStatus == ConsentStatus.NON_PERSONALIZED) {
            showNonPersonalizedAds()
        } else {
            showPersonalizedAds()
        }
    }

    private fun showPersonalizedAds() {
        var adId: String = Admob.REWADEDR_VIDEO_AD_ID
        mAdmobSetingsFromFirebase?.admobAds?.videoAds4?.id?.let {
            adId = it
        }
        mRewardedVideoAd?.loadAd(adId,
                AdRequest.Builder().build())

    }

    private fun showNonPersonalizedAds() {
        var adId: String = Admob.REWADEDR_VIDEO_AD_ID
        mAdmobSetingsFromFirebase?.admobAds?.videoAds4?.id?.let {
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
        Log.d(TAG, "inti data set for day left progressbar")
        val model = ExcersizePlansViewModle(application)
        excersizePlanList = model.getExcersizePlans();
        if (excersizePlanList.size > 0) {
            excersizePlanList.add(0, ExcersizePlan("native ad", 0, 0, -1, ExcersizePlan.TYPE_AD))
            excersizePlanList.add( ExcersizePlan("native ad", 0, 0, -1, ExcersizePlan.TYPE_AD))

            if (excersizePlansListAdupter == null) {
                excersizePlansListAdupter = ExcersizePlansAdupter(excersizePlanList);
                val llm = CenterZoomLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                recyclerView.layoutManager = llm
                recyclerView.addOnChildAttachStateChangeListener(ChildAttachListener(llm))
                recyclerView.adapter = excersizePlansListAdupter
                recyclerView.scrollToPosition(extraPlan + 1)
            } else {
                excersizePlansListAdupter?.notifyDataSetChanged()
            }

        }


    }

    override fun onBackPressed() {
        val rateUsFragment = RateUsFragment()
        rateUsFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog_MinWidth_Transparent);
        rateUsFragment.show(supportFragmentManager, "rateUsFragment")
    }

    public fun getScreenWidth(): Int {

        val wm = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager;
        val display = wm.getDefaultDisplay();
        val size = Point();
        display.getSize(size);

        return size.x;
    }

    inner class ExcersizePlansAdupter(var excersizePlans: MutableList<ExcersizePlan>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return when (viewType) {
                ExcersizePlan.TYPE_EXCERSISE -> {
                    val itemView = LayoutInflater.from(p0.context).inflate(R.layout.each_excersize_plan_horizental_row, p0, false)

                    ExcersizeViewHolder(itemView);
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
            Log.d(TAG, "onBindViewHolder ")
            if (ExcersizePlan.TYPE_EXCERSISE == p0.itemViewType) {
                p0 as ExcersizeViewHolder
                var daysComplted = excersizePlans[p0.adapterPosition].totalDays - excersizePlans[p0.adapterPosition].completedDays
                p0.tvtitle.text = excersizePlans[p0.adapterPosition].name
                Glide.with(p0.tvtitle.context).load(excersizePlans[p0.adapterPosition].image).apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE)).into(p0.image)
                p0.daysProgressBar.progress = excersizePlans[p0.adapterPosition].completedDays
                p0.tvTotalDaysLeft.text = "Days left " + daysComplted
                Log.d("ExcersizePlansAdupter", "onBindViewHolder: " + daysComplted)
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
            var root: View


            init {

                root = itemView
                tvtitle = itemView.findViewById(R.id.excersizeTitle)
                image = itemView.findViewById(R.id.image)
                tvTotalDaysLeft = itemView.findViewById(R.id.tvDaysLift)
                daysProgressBar = itemView.findViewById(R.id.progressBar)


                itemView.onFocusChangeListener = object : View.OnFocusChangeListener {
                    override fun onFocusChange(v: View?, hasFocus: Boolean) {

                        if (hasFocus) {
                            // run scale animation and make it bigger
                            Log.d(TAG, "has focus")

                            itemView.animate().scaleY(1.2f);

                        } else {
                            Log.d(TAG, "has no focus")
                            // run scale animation and make it smaller

                            itemView.animate().scaleY(1f);

                        }
                    }
                };

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
                        currentVisibalExcersizePlan = ExcersizePlan.PLAN_FULL_BODY
                        mPager.setCurrentItem(llm.findFirstCompletelyVisibleItemPosition()-1)
                    }
                    2 -> {
                        currentVisibalExcersizePlan = ExcersizePlan.PLAN_ABS
                        mPager.setCurrentItem(llm.findFirstCompletelyVisibleItemPosition()-1)
                    }
                    3 -> {
                        currentVisibalExcersizePlan = ExcersizePlan.PLAN_BUTT
                        mPager.setCurrentItem(llm.findFirstCompletelyVisibleItemPosition()-1)
                    }
                    //TODO:4)AddNewPlan add new plan to UI
                    4->{
                        currentVisibalExcersizePlan = ExcersizePlan.PLAN_BUTT_New
                        mPager.setCurrentItem(llm.findFirstCompletelyVisibleItemPosition()-1)
                    }
                    5->{
                        currentVisibalExcersizePlan = ExcersizePlan.TEST_NEW_PLAN
                        mPager.setCurrentItem(llm.findFirstCompletelyVisibleItemPosition()-1)
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
                    return EachDayFragment.newInstance(ExcersizePlan.PLAN_FULL_BODY)
                }
                1 -> {
                    return EachDayFragment.newInstance(ExcersizePlan.PLAN_ABS)
                }
                2 -> {
                    return EachDayFragment.newInstance(ExcersizePlan.PLAN_BUTT)
                }
                3->{
                    return EachDayFragment.newInstance(ExcersizePlan.PLAN_BUTT_New)
                }
                4->{
                    return EachDayFragment.newInstance(ExcersizePlan.TEST_NEW_PLAN)
                }

            }
            return null
        }
    }

    inner class FadeOutTransformation : ViewPager.PageTransformer {
        override fun transformPage(page: View, position: Float) {

            mPager.translationX = -position * page.width


        }
    }


}
