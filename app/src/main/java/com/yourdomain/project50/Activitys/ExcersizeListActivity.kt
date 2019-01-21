package com.yourdomain.project50.Activitys

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.google.ads.consent.ConsentInformation
import com.google.ads.consent.ConsentStatus
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.*
import com.yourdomain.project50.MY_Shared_PREF
import com.yourdomain.project50.Model.Admob
import com.yourdomain.project50.Model.AppAdmobSettingsFromFirebase
import com.yourdomain.project50.Model.Excesizes
import com.yourdomain.project50.Model.ExerciseDay
import com.yourdomain.project50.R
import com.yourdomain.project50.TTSHelperService
import com.yourdomain.project50.ViewModle.ExcersizesByDayandTypeViewModle

class ExcersizeListActivity : AppCompatActivity() {

    companion object {
        val EXTRA_DAY = "extra day";
        val EXTRA_PLAN = "ExcersizeListActivity.EXTRA_PLAN";
        val EXTRA_EXCERSIZES_DONE = "ExcersizeListActivity.EXTRA_EXCERSIZES_DONE"
        val ACTION_START_EXCERSIZE = "ACTION_START_EXCERSIZE"
        val TAG = "ExcersizeListActivity";
    }

    private var currentDay: ExerciseDay? = null
    private var currentDayKey: Int = -2
    private var excersizeDone = -2
    private var currentExcesizesPlan = ""
    lateinit var btStart: Button
    private var totaleExsersize = -1

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var adViewContaner: RelativeLayout

    private var mSetingsFromFirebase: AppAdmobSettingsFromFirebase? = null

    private var adRequest: AdRequest? = null
    private var mInterstitialAd: InterstitialAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_excersize_list)
        currentDayKey = intent.getIntExtra(EXTRA_DAY, -2)
        excersizeDone = intent.getIntExtra(EXTRA_EXCERSIZES_DONE, -2)
        Log.d(TAG, "excersize done: " + excersizeDone);
        currentExcesizesPlan = intent.getStringExtra(EXTRA_PLAN)
        if (currentDayKey != -2) {
            currentDay = MY_Shared_PREF.getDayByKey(application, currentExcesizesPlan + currentDayKey.toString())

        }

        recyclerView = findViewById<RecyclerView>(R.id.recylerview)
        progressBar = findViewById(R.id.progressBar)
        adViewContaner = findViewById(R.id.ad_contaniner)
        btStart = findViewById(R.id.btStart)

        btStart.setOnClickListener {
            openExcersizeActivty()
        }

        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                intiDataSet()
            }

        }, 500)
        mSetingsFromFirebase = MY_Shared_PREF.getFirebaseAdmobAppSettings(application)


        adRequest = if (ConsentInformation.getInstance(this).consentStatus == ConsentStatus.NON_PERSONALIZED) {
            AdRequest.Builder()
                    .addNetworkExtrasBundle(AdMobAdapter::class.java, getNonPersonalizedAdsBundle())
                    .build()
        } else {
            AdRequest.Builder()
                    .build()
        }
        loadBannerAds()
        loadInterstial()
        mInterstitialAd?.adListener = object : AdListener() {

            override fun onAdFailedToLoad(p0: Int) {
                super.onAdFailedToLoad(p0)
                when(p0){
                    3->{
                        loadInterstial()
                    }
                }
            }
        }
//        intent?.action?.let {
//            if (it.equals(ACTION_START_EXCERSIZE)) {
//                openExcersizeActivty()
//            }
//        }
    }

    private fun openExcersizeActivty() {
        startService(Intent(this, TTSHelperService::class.java))
        val internt = Intent(this, ExerciseActivity::class.java)
        if (excersizeDone == totaleExsersize - 1)
            excersizeDone = -1
        internt.putExtra(ExerciseActivity.EXTRA_EXCERSIZES_DONE, excersizeDone)
        internt.putExtra(ExerciseActivity.EXTRA_PLAN, currentExcesizesPlan)
        internt.putExtra(ExerciseActivity.EXTRA_DAY, currentDayKey)
        startActivity(internt)
        if (mInterstitialAd?.isLoaded == true) {
            mInterstitialAd?.show()
        }
    }

    private fun loadInterstial() {
        mInterstitialAd = InterstitialAd(this)
        if (mSetingsFromFirebase?.admobAds?.interstitialAds5?.id == null) {
            mInterstitialAd?.adUnitId = Admob.INTERSTITIAL_AD_ID
        } else {
            mInterstitialAd?.adUnitId = mSetingsFromFirebase?.admobAds?.interstitialAds5?.id
        }
        mInterstitialAd?.loadAd(adRequest)


    }

    private fun loadBannerAds() {
        val adView = AdView(this)
        adView.adSize = AdSize.BANNER
        var adId = Admob.BANNER_AD_ID
        mSetingsFromFirebase?.admobAds?.bannerAds5?.id?.let {
            adId = it
        }
        adView.adUnitId = adId
        adViewContaner.addView(adView)
        adView.loadAd(adRequest)
    }

    fun getNonPersonalizedAdsBundle(): Bundle {
        val extras = Bundle()
        extras.putString("npa", "1")

        return extras
    }

    private fun intiDataSet() {
        val modle = ViewModelProviders.of(this)[ExcersizesByDayandTypeViewModle::class.java]
        modle.getExcersizs(currentDayKey, currentExcesizesPlan)?.observe(this, Observer {
            if (it != null) {
                totaleExsersize = it.icons.size
                progressBar.visibility = View.INVISIBLE
                recyclerView.layoutManager = LinearLayoutManager(this@ExcersizeListActivity)
                recyclerView.adapter = ExcersizeAdupter(it)
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

        override fun onBindViewHolder(p0: ExcersizeAdupter.ExcersizeViewHolder, p1: Int) {

            p0.tvtitle.text = excesizes.title[p0.adapterPosition]
            p0.tvTotalDays.text = excesizes.seconds[p0.adapterPosition].toString()
            Glide.with(p0.itemView.context).asGif().load(excesizes.icons[p0.adapterPosition]).into(p0.image)
            if (excersizeDone == p0.adapterPosition) {
                p0.rootItemView.setBackgroundColor(Color.parseColor("#242c70"))
            }
        }


        inner class ExcersizeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var tvtitle: TextView
            var image: ImageView
            var tvTotalDays: TextView
            var rootItemView: View

            init {
                tvtitle = itemView.findViewById(R.id.tvExcersizeTitle)
                image = itemView.findViewById(R.id.ivIcon)
                tvTotalDays = itemView.findViewById(R.id.tvTime)
                rootItemView = itemView
                itemView.setOnClickListener {

                }
            }


        }


    }

}
