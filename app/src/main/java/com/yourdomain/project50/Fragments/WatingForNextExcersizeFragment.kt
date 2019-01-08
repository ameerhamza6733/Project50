package com.yourdomain.project50.Fragments

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.app.DialogFragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import com.bumptech.glide.Glide
import com.google.ads.consent.ConsentInformation
import com.google.ads.consent.ConsentStatus
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.VideoOptions
import com.google.android.gms.ads.formats.MediaView
import com.google.android.gms.ads.formats.NativeAdOptions
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.google.android.gms.ads.formats.UnifiedNativeAdView
import com.yourdomain.project50.Activitys.ExerciseActivity
import com.yourdomain.project50.CustomCountDownTimer
import com.yourdomain.project50.R


class WatingForNextExcersizeFragment : DialogFragment() {

    private val TAG = "WatingForNext"
    private var mParamTitle: String? = null
    private var mParamSeconds: String? = null
    private var mParamDoneExcersizes: String? = null
    private var mParamDrawble: Int = -1
    private var mParamRestTime: Int = 30
    private var mNativeAdId: String? = null

    private lateinit var adRequest: AdRequest
    private var mListener: OnNextExcersizeDemoFragmentListener? = null

    private lateinit var tvTitle: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvProgress: TextView
    private lateinit var tvDoneExcersize: TextView
    private lateinit var tvSeconds: TextView
    private lateinit var btSkip: TextView
    private lateinit var btIncreaseCoutDown: TextView
    private lateinit var icon: ImageView
    private lateinit var adPlaceHolder: FrameLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParamTitle = arguments!!.getString(ARG_PARAM_TITLE)
            mParamSeconds = arguments!!.getString(ARG_PARAM_SECONDES)
            mParamDoneExcersizes = arguments!!.getString(ARG_PARAM_DONE_EXCERSIZE)
            mParamDrawble = arguments!!.getInt(ARG_PARAM_ICON)
            mParamRestTime = arguments!!.getInt(ARG_PARAM_REST_TIME)
            mNativeAdId = arguments!!.getString(ARG_PARAM_NATIVE_AD_ID)
        }
        try {

            (activity as ExerciseActivity).sendTTSBroadCast(getString(R.string.take_a_rest))
        } catch (E: Exception) {

        }

    }


    private var countDownTimer: CustomCountDownTimer? = null
    private var secondRemaning=0
    private var halfTime: Int = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        getActivity()?.getWindow()?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        val view = inflater.inflate(R.layout.fragment_wating_for_next_excersize, container, false)

        tvTitle = view.findViewById(R.id.title)
        tvDoneExcersize = view.findViewById(R.id.tvDoneExcersize)
        progressBar = view.findViewById(R.id.progressBar)
        tvProgress = view.findViewById(R.id.tvProgress)
        tvSeconds = view.findViewById(R.id.seconds)
        btSkip = view.findViewById(R.id.btSkip)
        btIncreaseCoutDown = view.findViewById(R.id.btincreaseRestTime)
        icon = view.findViewById(R.id.icon)

        progressBar.max = mParamRestTime
        tvProgress.text = "" + mParamRestTime
        tvTitle.text = mParamTitle?.toUpperCase()
        tvDoneExcersize.text = mParamDoneExcersizes
        tvSeconds.text = mParamSeconds
        btSkip.setOnClickListener { mListener?.onSkip();dismiss() }
        adPlaceHolder = view.findViewById(R.id.adPlaceholder)
        btIncreaseCoutDown.setOnClickListener {

            countDownTimer?.cancel()
            progressBar.max=secondRemaning+10
            progressBar.progress=progressBar.progress-10
            val secondToCountDown = secondRemaning + 10;
            countDown(secondToCountDown.toLong())
        }
        Glide.with(this).asGif().load(mParamDrawble).into(icon)

        countDown(mParamRestTime.toLong())

        adRequest = if (ConsentInformation.getInstance(activity).consentStatus == ConsentStatus.NON_PERSONALIZED) {
            AdRequest.Builder()
                    .addNetworkExtrasBundle(AdMobAdapter::class.java, getNonPersonalizedAdsBundle())
                    .build()
        } else {
            AdRequest.Builder()
                    .build()
        }

        refreshAd();
        return view;
    }

    private fun countDown(wattingTime:Long) {
        Log.d(TAG,"count down for : "+progressBar.max)
        halfTime=((wattingTime * 1000)/2).toInt()
        countDownTimer = object : CustomCountDownTimer(wattingTime * 1000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                var s = (millisUntilFinished / 1000).toInt()
                secondRemaning = s
                progressBar.progress =s
                tvProgress.text = secondRemaning.toString()
                if (s == halfTime) {
                    sendTTSBroadCast(getString(R.string.next))
                    sendTTSBroadCast(mParamTitle!!)
                }
                if (s < 4) {
                    sendTTSBroadCast(s.toString())
                }
            }

            override fun onFinish() {
                mListener?.onSkip()
                try {
                    dismiss()
                } catch (E: Exception) {
                    E.printStackTrace()
                }

            }

        }.start()
    }

    private fun sendTTSBroadCast(string: String) {
        try {

            (activity as ExerciseActivity).sendTTSBroadCast(string)
        } catch (E: Exception) {

        }
    }

    override fun onPause() {
        Log.d(TAG, "onPause");
        countDownTimer?.pause()
        resumeCountDown=true
        super.onPause()
    }


    override fun onResume() {
        Log.d(TAG, "onResume")
       if (resumeCountDown){
           countDownTimer?.resume()
           resumeCountDown=false
       }
        super.onResume()

    }
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnNextExcersizeDemoFragmentListener) {
            mListener = context
        } else {
        }
    }



    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onResume");
    }

    override fun onDetach() {
        countDownTimer?.cancel()
        super.onDetach()
        mListener = null
    }

    interface OnNextExcersizeDemoFragmentListener {
        fun onSkip()
    }

    private fun populateUnifiedNativeAdView(nativeAd: UnifiedNativeAd, adView: UnifiedNativeAdView) {
        // Set the media view. Media content will be automatically populated in the media view once
        // adView.setNativeAd() is called.
        val mediaView = adView.findViewById<MediaView>(R.id.ad_media)
        adView.mediaView = mediaView

        // Set other ad assets.
        adView.headlineView = adView.findViewById(R.id.ad_headline)
        adView.bodyView = adView.findViewById(R.id.ad_body)
        adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
        adView.iconView = adView.findViewById(R.id.ad_app_icon)
        adView.priceView = adView.findViewById(R.id.ad_price)
        adView.starRatingView = adView.findViewById(R.id.ad_stars)
        adView.storeView = adView.findViewById(R.id.ad_store)
        adView.advertiserView = adView.findViewById(R.id.ad_advertiser)

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
    private fun refreshAd() {


        if (activity != null) {
            val builder = AdLoader.Builder(activity, mNativeAdId)

            builder.forUnifiedNativeAd(UnifiedNativeAd.OnUnifiedNativeAdLoadedListener { unifiedNativeAd ->
                // OnUnifiedNativeAdLoadedListener implementation.
                if (activity!=null){
                    val adView = layoutInflater.inflate(R.layout.native_adview, null) as UnifiedNativeAdView
                    populateUnifiedNativeAdView(unifiedNativeAd, adView)
                    adPlaceHolder.removeAllViews()
                    adPlaceHolder.addView(adView)
                }
            })

            val videoOptions = VideoOptions.Builder()
                    .build()

            val adOptions = NativeAdOptions.Builder()
                    .setVideoOptions(videoOptions)
                    .build()

            builder.withNativeAdOptions(adOptions)

            val adLoader = builder.withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(errorCode: Int) {
                    Toast.makeText(activity, "Failed to load native ad: $errorCode", Toast.LENGTH_SHORT).show()
                }

                override fun onAdOpened() {
                    Log.d(TAG,"onAdOpened")
                    super.onAdOpened()
                }

                override fun onAdClicked() {
                    Log.d(TAG,"onAdClicked")
                    super.onAdClicked()
                }
            }).build()

            adLoader.loadAd(adRequest)
        }


    }

    fun getNonPersonalizedAdsBundle(): Bundle {
        val extras = Bundle()
        extras.putString("npa", "1")

        return extras
    }

    companion object {

        private val ARG_PARAM_TITLE = "ARG_PARAM_TITLE"
        private val ARG_PARAM_SECONDES = "ARG_PARAM_SECONDES"
        private val ARG_PARAM_DONE_EXCERSIZE = "ARG_PARAM_DONE_EXCERSIZE";
        private var ARG_PARAM_ICON = "ARG_PARAM_ICON"
        private val ARG_PARAM_REST_TIME = "ARG_PARAM_REST_TIME";
        private var ARG_PARAM_NATIVE_AD_ID = "ARG_PARAM_NATIVE_AD_ID"
        private var resumeCountDown=false;
        fun newInstance(title: String, seconds: String, doneExcersizes: String, drawble: Int, restSeconds: Int, nativeAdId: String): WatingForNextExcersizeFragment {
            val fragment = WatingForNextExcersizeFragment()
            val args = Bundle()
            args.putString(ARG_PARAM_TITLE, title)
            args.putString(ARG_PARAM_SECONDES, seconds)
            args.putString(ARG_PARAM_DONE_EXCERSIZE, doneExcersizes)
            args.putInt(ARG_PARAM_REST_TIME, restSeconds)
            args.putInt(ARG_PARAM_ICON, drawble)
            args.putString(ARG_PARAM_NATIVE_AD_ID, nativeAdId)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
