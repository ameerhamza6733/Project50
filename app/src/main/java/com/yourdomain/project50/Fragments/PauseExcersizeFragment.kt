package com.yourdomain.project50.Fragments

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.yourdomain.project50.R


class PauseExcersizeFragment : DialogFragment() {


    private var mParamExcesizeTilte: String? = null
    private var mParamExcersizeSeconds: String? = null
    private var mParamGif: Int? = null
    private var mNativeAdId: String? = null
    private var mListener: OnResumeListener? = null
    private lateinit var adRequest: AdRequest

    private lateinit var btContinue: ImageButton
    private lateinit var tvTitle: TextView
    private lateinit var tvSeconds: TextView
    private lateinit var gif: ImageView
    private lateinit var adPlaceHolder: FrameLayout
    private lateinit var nativeAdImagePlaceHolder:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParamExcesizeTilte = arguments!!.getString(ARG_EXCERSIXE_TITLE)
            mParamExcersizeSeconds = arguments!!.getString(ARG_EXCERSIZE_SECONDS)
            mParamGif = arguments!!.getInt(ARG_EXCERSIZE_PAUSE_GIF)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_pause_excersize, container, false)

        btContinue = view.findViewById(R.id.btResume);
        tvSeconds = view.findViewById(R.id.tvSeonds)
        tvTitle = view.findViewById(R.id.tvtitle)
        gif = view.findViewById(R.id.icon)
        adPlaceHolder = view.findViewById(R.id.adPlaceholder)
        nativeAdImagePlaceHolder=view.findViewById(R.id.native_ad_image_placeHolder)

        tvTitle.text = mParamExcesizeTilte
        tvSeconds.text = mParamExcersizeSeconds
        btContinue.setOnClickListener {
            mListener?.ResumeListener()
            dismiss()

        }
        Glide.with(activity!!).load(R.drawable.native_ad_place_holder).into(nativeAdImagePlaceHolder)
        adRequest = if (ConsentInformation.getInstance(activity).consentStatus == ConsentStatus.NON_PERSONALIZED) {
            AdRequest.Builder()
                    .addNetworkExtrasBundle(AdMobAdapter::class.java, getNonPersonalizedAdsBundle())
                    .build()
        } else {
            AdRequest.Builder()
                    .build()
        }
        refreshAd();
        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnResumeListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    interface OnResumeListener {
        fun ResumeListener()
    }

    companion object {

        private val ARG_EXCERSIXE_TITLE = "ARG_EXCERSIXE_TITLE"
        private val ARG_EXCERSIZE_SECONDS = "ARG_EXCERSIZE_SECONDS"
        private val ARG_EXCERSIZE_PAUSE_GIF = "ARG_EXCERSIZE_PAUSE_GIF"
        private val ARG_NATIVE_AD_ID = "ARG_NATIVE_AD_ID"


        fun newInstance(title: String, seconds: String, gif: Int, nativeAdId: String): PauseExcersizeFragment {
            val fragment = PauseExcersizeFragment()
            val args = Bundle()
            args.putString(ARG_EXCERSIXE_TITLE, title)
            args.putString(ARG_EXCERSIZE_SECONDS, seconds)
            args.putInt(ARG_EXCERSIZE_PAUSE_GIF, gif)
            args.putString(ARG_NATIVE_AD_ID, nativeAdId)
            fragment.arguments = args
            return fragment
        }
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


        val builder = AdLoader.Builder(activity, mNativeAdId)

        builder.forUnifiedNativeAd(UnifiedNativeAd.OnUnifiedNativeAdLoadedListener { unifiedNativeAd ->
            // OnUnifiedNativeAdLoadedListener implementation.

            val adView = layoutInflater
                    .inflate(R.layout.native_adview, null) as UnifiedNativeAdView
            populateUnifiedNativeAdView(unifiedNativeAd, adView)
            adPlaceHolder.removeAllViews()
            adPlaceHolder.addView(adView)
        })

        val videoOptions = VideoOptions.Builder()
                .build()

        val adOptions = NativeAdOptions.Builder()
                .setVideoOptions(videoOptions)
                .build()

        builder.withNativeAdOptions(adOptions)

        val adLoader = builder.withAdListener(object : AdListener() {
            override fun onAdFailedToLoad(errorCode: Int) {
                adPlaceHolder.visibility = View.GONE
                Toast.makeText(activity, "Failed to load native ad: $errorCode", Toast.LENGTH_SHORT).show()
            }


        }).build()

        adLoader.loadAd(adRequest)


    }

    fun getNonPersonalizedAdsBundle(): Bundle {
        val extras = Bundle()
        extras.putString("npa", "1")

        return extras
    }
}// Required empty public constructor
