package com.yourdomain.project50.Fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.util.DisplayMetrics
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


class VideoFragment : DialogFragment() {
    private lateinit var adPlaceHolder: FrameLayout
    private lateinit var adRequest: AdRequest

    private var mNativeAdId: String? = null
    private var mTitle: String? = null
    private var mDis: String? = null
    private var mGifUrl: Int? = null
    private val TAG = "VideoFragment"

    private var mListener: OnVideoFragmentListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mTitle = arguments!!.getString(ARG_PARAM_TITLE)
            mDis = arguments!!.getString(ARG_PARAM_DES)
            mGifUrl = arguments!!.getInt(ARG_PARAM_VIDEO_URL)
            mNativeAdId = arguments!!.getString(ARG_NATIVE_AD_ID)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_video, container, false)
        adPlaceHolder = view.findViewById(R.id.adPlaceholder)
        adRequest = if (ConsentInformation.getInstance(activity).consentStatus == ConsentStatus.NON_PERSONALIZED) {
            AdRequest.Builder()
                    .addNetworkExtrasBundle(AdMobAdapter::class.java, getNonPersonalizedAdsBundle())
                    .build()
        } else {
            AdRequest.Builder()
                    .build()
        }
        val title = view.findViewById<TextView>(R.id.tvTitle)
        val description = view.findViewById<TextView>(R.id.tvDescription)
        val gifView = view.findViewById<ImageView>(R.id.videoGifView)
        title.setText(mTitle)
        description.setText(mDis)


        val displaymetrics = DisplayMetrics()
        activity!!.windowManager.defaultDisplay.getMetrics(displaymetrics)
        val height = displaymetrics.heightPixels
        val width = displaymetrics.widthPixels
        Glide.with(this).load(mGifUrl).into(gifView)

        refreshAd()
        return view
    }

    // TODO: Rename method, update argument and hook method into UI event

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnVideoFragmentListener) {
            mListener = context
        }
    }

    override fun onDetach() {
        mListener?.onDetachedVideoFragment()
        super.onDetach()
        mListener = null
    }

    interface OnVideoFragmentListener {

        fun onDetachedVideoFragment()
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM_TITLE = "ARG_PARAM_TITLE"
        private val ARG_PARAM_DES = "ARG_PARAM_DES"
        private val ARG_PARAM_VIDEO_URL = "ARG_PARAM_VIDEO_URL"
        private val ARG_NATIVE_AD_ID = "ARG_NATIVE_AD_ID"
        fun newInstance(title: String, description: String, gif: Int, nativeAdId: String): VideoFragment {
            val fragment = VideoFragment()
            val args = Bundle()
            args.putString(ARG_PARAM_TITLE, title)
            args.putString(ARG_PARAM_DES, description)
            args.putInt(ARG_PARAM_VIDEO_URL, gif)
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
