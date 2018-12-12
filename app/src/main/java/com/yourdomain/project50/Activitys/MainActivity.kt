package com.yourdomain.project50.Activitys

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.DialogFragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.ads.consent.ConsentInformation
import com.google.ads.consent.ConsentStatus
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.*
import com.google.android.gms.ads.formats.MediaView
import com.google.android.gms.ads.formats.NativeAdOptions
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.google.android.gms.ads.formats.UnifiedNativeAdView
import com.yourdomain.project50.Fragments.MoreAppsDialogeFragment
import com.yourdomain.project50.MY_Shared_PREF
import com.yourdomain.project50.Model.Admob
import com.yourdomain.project50.Model.AppAdmobSettingsFromFirebase
import com.yourdomain.project50.Model.AppSettingsFromFireBase
import com.yourdomain.project50.Model.ExcersizePlan
import com.yourdomain.project50.R
import com.yourdomain.project50.ViewModle.AppSettingsFromFirebaseViewModle
import com.yourdomain.project50.ViewModle.ExcersizePlansViewModle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private var currentExcersizePlan = -1
    private val mNativeExpressAdView: NativeExpressAdView? = null
    private var mSettingFireBase: AppSettingsFromFireBase? = null
    private var mAdmobSettingsFromFireBase:AppAdmobSettingsFromFirebase?=null
    private lateinit var adRequst: AdRequest
    private var nativeAdId:String?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        recyclerView = findViewById(R.id.recylerview)
        progressBar = findViewById(R.id.progressBar)
        val handler = Handler()

        handler.postDelayed({
            intiDataSet()
        }, 500 )
        mAdmobSettingsFromFireBase=MY_Shared_PREF.getFirebaseAdmobAppSettings(this.application)
       nativeAdId=Admob.NATIVE_AD_ID
        mAdmobSettingsFromFireBase?.admobAds?.nativeAds3?.id?.let {
            nativeAdId=it
        }
        adRequst = if (ConsentInformation.getInstance(this).consentStatus == ConsentStatus.NON_PERSONALIZED) {
            AdRequest.Builder()
                    .addNetworkExtrasBundle(AdMobAdapter::class.java, getNonPersonalizedAdsBundle())
                    .build()
        } else {
            AdRequest.Builder()
                    .build()
        }
        getAppSetingFromFirBase()

    }

    private fun getAppSetingFromFirBase() {
        val appSettingsFromFirebaseViewModle = ViewModelProviders.of(this).get(AppSettingsFromFirebaseViewModle::class.java)
        appSettingsFromFirebaseViewModle.getAppSettingFromFirabs()?.observe(this, Observer {
            if (it != null) {
                mSettingFireBase = it
                MY_Shared_PREF.saveAppsSettingFromFireBase(application, it)
            }
        })
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
                    val unifiedNativeLayoutView = LayoutInflater.from(
                            p0.getContext()).inflate(R.layout.ad_unified,
                            p0, false)
                    UnifiedNativeAdViewHolder(unifiedNativeLayoutView)
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
                var daysComplted = (excersizePlans[p0.adapterPosition].totalDays - excersizePlans[p0.adapterPosition].completedDays)
                p0.daysProgressBar.progress = excersizePlans[p0.adapterPosition].completedDays
                p0.tvTotalDaysLeft.text = "Days left " + daysComplted
                p0.tvtitle.text = excersizePlans[p0.adapterPosition].name
                Glide.with(p0.itemView.context).load(R.drawable.body_plan_background_screm).into(p0.imScrem)
                Glide.with(p0.tvtitle.context).load(excersizePlans[p0.adapterPosition].image).apply(requestOptions).into(p0.image)

            } else if (ExcersizePlan.TYPE_AD == p0.itemViewType) {
                p0 as UnifiedNativeAdViewHolder
                p0.adView.visibility=View.GONE
                refreshAd(p0.adView)
            }

        }

        override fun getItemViewType(position: Int): Int {
            return excersizePlans[position].ViewType
        }

        inner class ExcersizeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var tvtitle: TextView
            var image: ImageView
            var imScrem: ImageView
            var tvTotalDaysLeft: TextView
            var daysProgressBar: ProgressBar

            init {
                itemView.setOnClickListener {
                    if (mSettingFireBase?.moreAppsDialog == true) {
                        val moreAppsDialogeFragment = MoreAppsDialogeFragment.newInstance(adapterPosition, mSettingFireBase?.moreAppsDevIdorAppId)
                        moreAppsDialogeFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.dialog);
                        moreAppsDialogeFragment.show(supportFragmentManager, "moreAppsDialogeFragment")
                    } else {
                        val intent = Intent(itemView.context, EachPlanExcersizesActivity::class.java)
                        intent.putExtra(EachPlanExcersizesActivity.EXTRA_PLAN, adapterPosition)
                        itemView.context.startActivity(intent)
                        finish()
                    }
                }
                tvtitle = itemView.findViewById(R.id.excersizeTitle)
                image = itemView.findViewById(R.id.image)
                imScrem = itemView.findViewById(R.id.imageViewScream);
                tvTotalDaysLeft = itemView.findViewById(R.id.tvDaysLift)
                daysProgressBar = itemView.findViewById(R.id.progressBar)
            }
        }


        inner class UnifiedNativeAdViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {

            val adView: UnifiedNativeAdView

            init {
                adView = view.findViewById<View>(R.id.ad_view) as UnifiedNativeAdView

                // The MediaView will display a video asset if one is present in the ad, and the
                // first image asset otherwise.
                adView.mediaView = adView.findViewById<View>(R.id.ad_media) as MediaView

                // Register the view used for each individual asset.
                adView.headlineView = adView.findViewById(R.id.ad_headline)
                adView.bodyView = adView.findViewById(R.id.ad_body)
                adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
                adView.iconView = adView.findViewById(R.id.ad_icon)
                adView.priceView = adView.findViewById(R.id.ad_price)
                adView.starRatingView = adView.findViewById(R.id.ad_stars)
                adView.storeView = adView.findViewById(R.id.ad_store)
                adView.advertiserView = adView.findViewById(R.id.ad_advertiser)
            }
        }
    }

    private fun populateUnifiedNativeAdView(nativeAd: UnifiedNativeAd, adView: UnifiedNativeAdView) {
        // Set the media view. Media content will be automatically populated in the media view once
        // adView.setNativeAd() is called.


        // The headline is guaranteed to be in every UnifiedNativeAd.
        (adView.headlineView as TextView).text = nativeAd.headline

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.body == null) {
            adView.bodyView.visibility = View.INVISIBLE
        } else {
            adView.bodyView.visibility = View.VISIBLE
            (adView.bodyView as TextView).text = nativeAd.body
        }

        if (nativeAd.callToAction == null) {
            adView.callToActionView.visibility = View.INVISIBLE
        } else {
            adView.callToActionView.visibility = View.VISIBLE
            (adView.callToActionView as Button).setText(nativeAd.callToAction)
        }

        if (nativeAd.icon == null) {
            adView.iconView.visibility = View.GONE
        } else {
            (adView.iconView as ImageView).setImageDrawable(
                    nativeAd.icon.drawable)
            adView.iconView.visibility = View.VISIBLE
        }

        if (nativeAd.price == null) {
            adView.priceView.visibility = View.INVISIBLE
        } else {
            adView.priceView.visibility = View.VISIBLE
            (adView.priceView as TextView).text = nativeAd.price
        }

        if (nativeAd.store == null) {
            adView.storeView.visibility = View.INVISIBLE
        } else {
            adView.storeView.visibility = View.VISIBLE
            (adView.storeView as TextView).text = nativeAd.store
        }

        if (nativeAd.starRating == null) {
            adView.starRatingView.visibility = View.INVISIBLE
        } else {
            (adView.starRatingView as RatingBar).rating = nativeAd.starRating!!.toFloat()
            adView.starRatingView.visibility = View.VISIBLE
        }

        if (nativeAd.advertiser == null) {
            adView.advertiserView.visibility = View.INVISIBLE
        } else {
            (adView.advertiserView as TextView).text = nativeAd.advertiser
            adView.advertiserView.visibility = View.VISIBLE
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad. The SDK will populate the adView's MediaView
        // with the media content from this native ad.
        adView.setNativeAd(nativeAd)

        // Get the video controller for the ad. One will always be provided, even if the ad doesn't
        // have a video asset.
        val vc = nativeAd.videoController


    }

    /**
     * Creates a request for a new native ad based on the boolean parameters and calls the
     * corresponding "populate" method when one is successfully returned.
     *
     */
    private fun refreshAd(adView: UnifiedNativeAdView) {


        val builder = AdLoader.Builder(this@MainActivity, nativeAdId)
        var native: UnifiedNativeAd? = null
        builder.forUnifiedNativeAd(UnifiedNativeAd.OnUnifiedNativeAdLoadedListener { unifiedNativeAd ->
            // OnUnifiedNativeAdLoadedListener implementation.
            native = unifiedNativeAd

        })

        val videoOptions = VideoOptions.Builder()
                .build()

        val adOptions = NativeAdOptions.Builder()
                .setVideoOptions(videoOptions)
                .build()

        builder.withNativeAdOptions(adOptions)

        val adLoader = builder.withAdListener(object : AdListener() {
            override fun onAdFailedToLoad(errorCode: Int) {
                adView.visibility=View.GONE
                Toast.makeText(this@MainActivity, "Failed to load native ad: $errorCode", Toast.LENGTH_SHORT).show()
            }

            override fun onAdLoaded() {
                super.onAdLoaded()
                native?.let {
                    adView.visibility=View.VISIBLE
                    populateUnifiedNativeAdView(it, adView)

                }
            }
        }).build()

        adLoader.loadAd(adRequst)


    }

    fun getNonPersonalizedAdsBundle(): Bundle {
        val extras = Bundle()
        extras.putString("npa", "1")

        return extras
    }
}



