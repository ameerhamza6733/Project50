package com.yourdomain.project50.Activitys

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
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
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.InterstitialAd
import com.yourdomain.project50.MY_Shared_PREF
import com.yourdomain.project50.Model.Admob
import com.yourdomain.project50.Model.AppAdmobDataFromFirebase
import com.yourdomain.project50.Model.ExcersizeDay
import com.yourdomain.project50.Model.Excesizes
import com.yourdomain.project50.R
import com.yourdomain.project50.TTSHelperService
import com.yourdomain.project50.ViewModle.ExcersizesByDayandTypeViewModle

class ExcersizeListActivity : AppCompatActivity() {

    companion object {
        val EXTRA_DAY = "extra day";
        val EXTRA_PLAN="ExcersizeListActivity.EXTRA_PLAN";
        val EXTRA_EXCERSIZES_DONE="ExcersizeListActivity.EXTRA_EXCERSIZES_DONE"
        val TAG = "ExcersizeListActivity";
    }

   private var currentDay: ExcersizeDay? = null
    private var currentDayKey:Int=-2
    private var excersizeDone=-2
    private var currentExcesizesPlan=""
    lateinit var btStart:Button
    private var totaleExsersize=-1

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar:ProgressBar
    private lateinit var adViewContaner:RelativeLayout

    private  var mSetingsFromFirebase: AppAdmobDataFromFirebase?=null

    private var adRequest: AdRequest?=null
    private val mInterstitialAd: InterstitialAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_excersize_list)
         currentDayKey = intent.getIntExtra(EXTRA_DAY, -2)
        excersizeDone=intent.getIntExtra(EXTRA_EXCERSIZES_DONE,-2)
        Log.d(TAG,"excersize done: "+excersizeDone);
        currentExcesizesPlan=intent.getStringExtra(EXTRA_PLAN)
        if (currentDayKey != -2){
            currentDay = MY_Shared_PREF.getDayByKey(application, currentExcesizesPlan+currentDayKey.toString())

        }

         recyclerView = findViewById<RecyclerView>(R.id.recylerview)
        progressBar=findViewById(R.id.progressBar)
        adViewContaner=findViewById(R.id.ad_contaniner)
        btStart=findViewById(R.id.btStart)

        btStart.setOnClickListener {
            startService(Intent(it.context, TTSHelperService::class.java))
            val internt = Intent(it.context,ExcersizeActivity::class.java)
            if (excersizeDone==totaleExsersize-1)
                excersizeDone=-1
            internt.putExtra(ExcersizeActivity.EXTRA_EXCERSIZES_DONE,excersizeDone)
            internt.putExtra(ExcersizeActivity.EXTRA_PLAN,currentExcesizesPlan)
            internt.putExtra(ExcersizeActivity.EXTRA_DAY,currentDayKey)
            startActivity(internt)
            if (mInterstitialAd?.isLoaded==true){
                mInterstitialAd?.show()
            }
        }

        val handler = Handler()
        handler.postDelayed(object :Runnable{
            override fun run() {
            intiDataSet()
            }

        },1000)
        mSetingsFromFirebase = MY_Shared_PREF.getFirebaseAdmobAppSettings(application)


        adRequest = if (ConsentInformation.getInstance(this).consentStatus == ConsentStatus.NON_PERSONALIZED) {
            AdRequest.Builder()
                    .addNetworkExtrasBundle(AdMobAdapter::class.java,getNonPersonalizedAdsBundle() )
                    .build()
        } else {
            AdRequest.Builder()
                    .build()
        }
        loadBannerAds()
        loadInterstial()
    }



    private fun loadInterstial() {

        if (mSetingsFromFirebase?.admobAds?.interstitialAds?.id==null){
            mInterstitialAd?.adUnitId=Admob.INTERSTITIAL_AD_ID
        }else {
            mInterstitialAd?.adUnitId = mSetingsFromFirebase?.admobAds?.interstitialAds?.id
        }
        mInterstitialAd?.loadAd(adRequest)

    }
    private fun loadBannerAds(){
        val adView = AdView(this)
        adView.adSize = AdSize.BANNER
        var adId=Admob.BANNER_AD_ID
        mSetingsFromFirebase?.admobAds?.bannerAds?.id?.let {
            adId=it
        }
        adView.adUnitId =adId
        adViewContaner .addView(adView)
        adView.loadAd(adRequest)
    }
    fun getNonPersonalizedAdsBundle(): Bundle {
        val extras = Bundle()
        extras.putString("npa", "1")

        return extras
    }
    private fun intiDataSet(){
        val modle = ViewModelProviders.of(this)[ExcersizesByDayandTypeViewModle::class.java]
        modle.getExcersizs(currentDayKey,currentExcesizesPlan)?.observe(this, Observer {
            if (it != null) {
                totaleExsersize=it.icons.size
                progressBar.visibility=View.INVISIBLE
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
