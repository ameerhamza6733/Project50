package com.yourdomain.project50.Activitys

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.DialogFragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.ads.consent.ConsentInformation
import com.google.ads.consent.ConsentStatus
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.reward.RewardedVideoAd
import com.yourdomain.project50.Fragments.RateUsFragment
import com.yourdomain.project50.MY_Shared_PREF
import com.yourdomain.project50.Model.Admob
import com.yourdomain.project50.Model.AppAdmobDataFromFirebase
import com.yourdomain.project50.Model.MoreApps
import com.yourdomain.project50.R
import com.yourdomain.project50.Utils
import com.yourdomain.project50.ViewModle.CustomAdsViewModle
import com.yourdomain.project50.ViewModle.MoreAppViewModle
import java.text.DecimalFormat
import java.util.*

class CongragulationActivity : AppCompatActivity() {

    private lateinit var tvDayComleted: TextView
    private lateinit var tvCalriesBurn: TextView
    private lateinit var tvTotleExcersize: TextView
    private lateinit var ivCongragulation: ImageView
    private lateinit var tvDuration: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var btClose: ImageButton
    private lateinit var btShare: ImageButton

    private var mSetingsFromFirebase: AppAdmobDataFromFirebase? = null
    private var mRewardedVideoAd: RewardedVideoAd?=null
    private var adRequest: AdRequest? = null

    internal var df = DecimalFormat("##.##")
    companion object {
        val EXTRA_EXCERSIZES = "CongragulationActivity.extraday";
        val EXTRA_DURACTION = "CongragulationActivity.EXTRA_DURACTION"
        val EXTRA_CAL = "CongragulationActivity.extra_cal"
        val EXTRA_DAY="CongragulationActivity.EXTRA_DAY"
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
        tvCalriesBurn.text =df.format( intent?.getDoubleExtra(EXTRA_CAL,0.0))
        tvTotleExcersize.text = df.format(intent?.getDoubleExtra(EXTRA_EXCERSIZES, 0.0))
        tvDuration.text = df.format(intent?.getDoubleExtra(EXTRA_DURACTION,0.0))
        tvDayComleted.text=intent?.getIntExtra(EXTRA_DAY,0).toString()
        Glide.with(this)
                .load(R.drawable.congragulation_cup)
                .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(ivCongragulation)


        val viewModle = ViewModelProviders.of(this).get(CustomAdsViewModle::class.java)
        viewModle.getMoreApps()?.observe(this, Observer {
           it?.let {
               progressBar.visibility = View.INVISIBLE
               val llm = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
               recyclerView.layoutManager = llm
               recyclerView.adapter = MoreAppsAdupter(it)
           }
        })

        mSetingsFromFirebase = MY_Shared_PREF.getFirebaseAppSettings(application)
        adRequest = if (ConsentInformation.getInstance(this).consentStatus == ConsentStatus.NON_PERSONALIZED) {
            AdRequest.Builder()
                    .addNetworkExtrasBundle(AdMobAdapter::class.java, getNonPersonalizedAdsBundle())
                    .build()
        } else {
            AdRequest.Builder()
                    .build()
        }
        loadVideoAd()

    }
    private fun loadVideoAd(){
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        var adID= Admob.REWADEDR_VIDEO_AD_ID
        mSetingsFromFirebase?.admobAds?.videoAds?.id?.let {
            adID = it
        }
        mRewardedVideoAd?.loadAd(adID,adRequest)

    }
    fun getNonPersonalizedAdsBundle(): Bundle {
        val extras = Bundle()
        extras.putString("npa", "1")

        return extras
    }
    private fun findVIews() {
        tvCalriesBurn = findViewById(R.id.tvTotaleCalresBurn)
        tvDayComleted = findViewById(R.id.tvDaysCompelted)
        tvTotleExcersize = findViewById(R.id.tvTotleExcersizesDone)
        tvDuration = findViewById(R.id.tvDuraction)
        ivCongragulation = findViewById(R.id.icon_congragulation)
        recyclerView = findViewById(R.id.recylerview)
        progressBar = findViewById(R.id.progressBar)
        btClose = findViewById(R.id.btClose)
        btShare = findViewById(R.id.btShare)

        if(intent?.getIntExtra(EXTRA_DAY,0)==1 || intent?.getIntExtra(EXTRA_DAY,0)==30 ){
            val rateUsFragment = RateUsFragment()
            rateUsFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.dialog);
            rateUsFragment.show(supportFragmentManager, "rateUsFragment")
        }

        btClose.setOnClickListener { finish() }
        btShare.setOnClickListener { Utils.shareTextExtra(application,"I have just completed "+tvDayComleted.text+" of (app name). Join me "+application.packageName) }

    }

    override fun onBackPressed() {
        if (mRewardedVideoAd?.isLoaded==true)
        mRewardedVideoAd?.show()
        finish()
    }

    inner class MoreAppsAdupter(val moreApps: ArrayList<MoreApps>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return when (viewType) {
                MoreApps.VIEW_TYPE_APP -> {
                    MoreAppViewHolder(LayoutInflater.from(p0.context)
                            .inflate(R.layout.each_more_app, p0, false));
                }
                MoreApps.VIEW_TYPE_AD -> {
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
                p0.tvtitle.text = moreApps.get(p0.adapterPosition).title
                p0.rating.text=moreApps.get(p0.adapterPosition).rating.toString()
                Glide.with(p0.context).load(moreApps.get(p0.adapterPosition).icon).into(p0.icon)
            } else if (MoreApps.VIEW_TYPE_AD == p0.itemViewType) {
                p0 as AdViewHolderViewHolder

            }

        }

        override fun getItemViewType(position: Int): Int {
            return moreApps[position].viewType
        }

        inner class MoreAppViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var tvtitle: TextView
            var icon: ImageView
            var rating:TextView
            var context: Context

            init {
                itemView.isFocusableInTouchMode = true
                tvtitle = itemView.findViewById(R.id.appTitle)
                icon = itemView.findViewById(R.id.appIcon)
                context = itemView.context
                rating=itemView.findViewById(R.id.apprating)


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
