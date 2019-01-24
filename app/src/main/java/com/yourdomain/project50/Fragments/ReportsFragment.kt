package com.yourdomain.project50.Fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.util.Log
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
import com.jjoe64.graphview.DefaultLabelFormatter
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.ValueDependentColor
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter
import com.jjoe64.graphview.series.BarGraphSeries
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.yourdomain.project50.MY_Shared_PREF
import com.yourdomain.project50.Model.AppAdmobSettingsFromFirebase
import com.yourdomain.project50.Model.Person
import com.yourdomain.project50.Model.PersonAppearance
import com.yourdomain.project50.R
import com.yourdomain.project50.Utils

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

/**
 * Created by hamza rafiq on 12/6/18.
 */

class ReportsFragment : Fragment() {

    private val TAG = "ReportsFragmentTAG"
    internal var calendar = Calendar.getInstance()
    private lateinit var dataPoints: Array<DataPoint?>
    private var bmiImageView: ImageView? = null
    private var btEditBMI: TextView? = null
    private var tvBmi: TextView? = null
    private var currentBMI = 0.0
    private val simpleDateFormat = SimpleDateFormat("dd/MM")
    private var calGraph: GraphView? = null
    private var waightGraph: GraphView? = null
    private lateinit var tvHigest:TextView
    private lateinit var tvLightest:TextView
    private lateinit var tvCurrent:TextView
    private lateinit var waightDataPoint: Array<DataPoint?>
    private var person: Person? = null
    private lateinit var adPlaceHolder:FrameLayout
    private lateinit var adRequest:AdRequest

    private  var mSetingsFromFirebase: AppAdmobSettingsFromFirebase?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataPoints = MY_Shared_PREF.getAllDataGraphCalvsDays(activity!!.application)
        person = MY_Shared_PREF.getPerson(activity!!.application)
        waightDataPoint = MY_Shared_PREF.getPersonHistory(activity!!.application)
            mSetingsFromFirebase = MY_Shared_PREF.getFirebaseAdmobAppSettings(activity?.application!!)

        if (dataPoints!!.size == 0) {
            dataPoints = setUpDefultValue()
        }
        if (waightDataPoint!!.size == 0) {
            waightDataPoint = setUpDefultValue()
        }

        Log.d(TAG, "weight graph points: " + waightDataPoint!!.toString())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_reports, container, false)
        calGraph = view.findViewById<View>(R.id.graph) as GraphView
        waightGraph = view.findViewById(R.id.waightGraph)
        bmiImageView = view.findViewById(R.id.imageBMI)
        btEditBMI = view.findViewById(R.id.btEditBmi)
        tvBmi = view.findViewById(R.id.tvBMI)
        tvHigest=view.findViewById(R.id.tvHavest)
        tvLightest=view.findViewById(R.id.tvLightest)
        tvCurrent=view.findViewById(R.id.tvCurrentWeight)
        adPlaceHolder = view.findViewById(R.id.adPlaceholder)
        updateCalGraph()
        updateWeightGraph()
        if (mSetingsFromFirebase?.admobAds?.nativeAds13?.enable==true){
            refreshAd()
        }
        return view
    }

    @SuppressLint("SetTextI18n")
    private fun updateWeightGraph()  {

     try{
         val series = LineGraphSeries(waightDataPoint!!)
         waightGraph!!.gridLabelRenderer.labelFormatter = DateAsXAxisLabelFormatter(activity)
         if (waightDataPoint!!.size < 8) {
             waightGraph!!.gridLabelRenderer.numHorizontalLabels = waightDataPoint!!.size // only 4 because of the space
             waightGraph!!.viewport.setMinX(waightDataPoint!![0]?.x!!)
             waightGraph!!.viewport.setMaxX(waightDataPoint!![waightDataPoint!!.size - 1]?.x!!)
             waightGraph!!.viewport.isXAxisBoundsManual = true
             waightGraph!!.viewport.isScrollable = true
         } else {
             waightGraph!!.gridLabelRenderer.numHorizontalLabels = 7
             waightGraph!!.viewport.setMinX(waightDataPoint!![0]?.x!!)
             waightGraph!!.viewport.setMaxX(waightDataPoint!![7]?.x!!)
             waightGraph!!.viewport.isXAxisBoundsManual = true
             waightGraph!!.viewport.isScrollable = true
         }


         waightGraph!!.gridLabelRenderer.setHumanRounding(false)

         waightGraph!!.addSeries(series)

         waightGraph!!.title = "Weight"
         waightGraph!!.gridLabelRenderer.labelFormatter = object : DefaultLabelFormatter() {
             override fun formatLabel(value: Double, isValueX: Boolean): String {
                 return if (isValueX) {
                     // show normal x values
                     simpleDateFormat.format(Date(value.toLong()))
                 } else {
                     // show currency for y values
                     DecimalFormat("#").format(value)
                 }
             }
         }
     }catch (E:Exception){
         E.printStackTrace()
     }
        tvHigest.text = ""+ waightDataPoint[getIndexOfLargest()]!!.y
        tvLightest.text=""+waightDataPoint[getIndexOfLowest()]!!.y
        tvCurrent.text=""+waightDataPoint[waightDataPoint.size-1]!!.y

    }


    fun getIndexOfLargest(): Int {
        if (waightDataPoint == null || waightDataPoint.size == 0) return -1 // null or empty

        var largest = 0
        for (i in 1 until waightDataPoint.size) {
            if (waightDataPoint[i]!!.y > waightDataPoint[largest]!!.y) largest = i
        }
        return largest // position of the first largest found
    }

    fun getIndexOfLowest(): Int {
        if (waightDataPoint == null || waightDataPoint.size == 0) return -1 // null or empty

        var largest = 0
        for (i in 1 until waightDataPoint.size) {
            if (waightDataPoint[i]!!.y > waightDataPoint[largest]!!.y) largest = i
        }
        return largest // position of the first largest found
    }


    private fun updateCalGraph() {
       try {
           val series = BarGraphSeries(dataPoints!!)

           series.valueDependentColor = ValueDependentColor { data -> Color.rgb(data?.x?.toInt()!! * 255 / 4, Math.abs(data.y * 255 / 6).toInt(), 100) }
           btEditBMI!!.setOnClickListener {
               val editBMIDialogeFragment = EditBMIDialogeFragment()
               editBMIDialogeFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog_MinWidth)
               editBMIDialogeFragment.show(childFragmentManager, "editBMIDialogeFragment")
           }
           if (PersonAppearance.TYPE_CM_KG == person?.personAppearance?.SCALE_TYPE) {
               currentBMI = Utils.calculateBMIinKg(person!!.personAppearance.mWaight, Utils.CMtoM(person!!.personAppearance.mHight))

           } else if(PersonAppearance.TYPE_IN_LBS == person?.personAppearance?.SCALE_TYPE) {
               currentBMI = Utils.calcautleBMIinlbs(person!!.personAppearance.mWaight, Utils.FeetToInch(person!!.personAppearance.mHight))
           }else{
               Toast.makeText(activity,"Your weight and hight are not added please update it ",Toast.LENGTH_SHORT).show()
           }

           tvBmi!!.text = "BMI : " + String.format("%.1f", currentBMI)
           Glide.with(this).load(Utils.getDrawbleAccodingToBMI(currentBMI)).into(bmiImageView!!)
           series.isDrawValuesOnTop = true
           series.valuesOnTopColor = Color.RED
           series.spacing = 50


           calGraph!!.gridLabelRenderer.labelFormatter = DateAsXAxisLabelFormatter(activity)


           if (dataPoints!!.size < 8) {
               calGraph!!.gridLabelRenderer.numHorizontalLabels = dataPoints!!.size // only 4 because of the space
               calGraph!!.viewport.setMinX(dataPoints!![0]?.x!!)
               calGraph!!.viewport.setMaxX(dataPoints!![dataPoints!!.size - 1]?.x!!)
               calGraph!!.viewport.isXAxisBoundsManual = true
               calGraph!!.viewport.isScrollable = true
           } else {
               calGraph!!.gridLabelRenderer.numHorizontalLabels = 7
               calGraph!!.viewport.setMinX(dataPoints!![0]?.x!!)
               calGraph!!.viewport.setMaxX(dataPoints!![7]?.x!!)
               calGraph!!.viewport.isXAxisBoundsManual = true
               calGraph!!.viewport.isScrollable = true
           }


           calGraph!!.gridLabelRenderer.setHumanRounding(false)

           calGraph!!.addSeries(series)


           calGraph!!.gridLabelRenderer.labelFormatter = object : DefaultLabelFormatter() {
               override fun formatLabel(value: Double, isValueX: Boolean): String {
                   return if (isValueX) {
                       // show normal x values
                       simpleDateFormat.format(Date(value.toLong()))
                   } else {
                       // show currency for y values
                       DecimalFormat("#").format(value)
                   }
               }
           }
       }catch (E:Exception){

       }
    }

    private fun setUpDefultValue(): Array<DataPoint?> {
        val calendar = Calendar.getInstance()
        val d1 = calendar.time
        calendar.add(Calendar.DATE, 1)
        val d2 = calendar.time
        calendar.add(Calendar.DATE, 1)
        val d3 = calendar.time

        calendar.add(Calendar.DATE, 1)
        val d4 = calendar.time

        calendar.add(Calendar.DATE, 1)
        val d5 = calendar.time

        calendar.add(Calendar.DATE, 1)
        val d6 = calendar.time
        return arrayOf(DataPoint(d1, 0.0), DataPoint(d2, 0.0), DataPoint(d3, 0.0), DataPoint(d4, 0.0), DataPoint(d5, 0.0), DataPoint(d6, 0.0))
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
    private fun refreshAd()  {
        try{
        adRequest = if (ConsentInformation.getInstance(activity).consentStatus == ConsentStatus.NON_PERSONALIZED) {
            AdRequest.Builder()
                    .addNetworkExtrasBundle(AdMobAdapter::class.java, getNonPersonalizedAdsBundle())
                    .build()
        } else {
            AdRequest.Builder()
                    .build()
        }


            val builder = AdLoader.Builder(activity, mSetingsFromFirebase?.admobAds?.nativeAds13?.id)

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
            }).build()

            adLoader.loadAd(adRequest)
        }catch (e:Exception){
e.printStackTrace()
        }


    }

    fun getNonPersonalizedAdsBundle(): Bundle {
        val extras = Bundle()
        extras.putString("npa", "1")

        return extras
    }
}
