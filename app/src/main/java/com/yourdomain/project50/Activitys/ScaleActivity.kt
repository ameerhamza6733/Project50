package com.yourdomain.project50.Activitys


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.ads.consent.ConsentInformation
import com.google.ads.consent.ConsentStatus
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.yourdomain.project50.Fragments.CMandKGscaleFragment
import com.yourdomain.project50.Fragments.INCandLBSscaleFragment
import com.yourdomain.project50.MY_Shared_PREF
import com.yourdomain.project50.Model.Admob
import com.yourdomain.project50.Model.AppAdmobSettingsFromFirebase
import com.yourdomain.project50.Model.Person
import com.yourdomain.project50.Model.PersonAppearance
import com.yourdomain.project50.R


class ScaleActivity : AppCompatActivity(), CMandKGscaleFragment.OnINCandLBSRadioListener {


    override fun onCMandKGadioClick() {
        showCMandKGscaleFragment()
    }

    override fun onNext(personAppearance: PersonAppearance) {
        Log.d(TAG,"OnNext: ")
        Log.d(TAG, personAppearance.toString())
        val person = Person()
        person.personAppearance = personAppearance
        MY_Shared_PREF.savePerson(application, person)
        MY_Shared_PREF.savePersonAppearanceHistory(application, personAppearance)
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
        if (mInterstitialAd?.isLoaded == true) {
            mInterstitialAd?.show()

        }else{
            Log.d(TAG,"add not loaded")
        }
        finish()
    }

    override fun onINCandLBSRadioClick() {
        showINCandLBSscaleFragment()
    }

    private val TAG = "ScaleActivity"

    private var mInterstitialAd: InterstitialAd? = null
    private var mSetingsFromFirebase: AppAdmobSettingsFromFirebase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scale)

        mSetingsFromFirebase = MY_Shared_PREF.getFirebaseAdmobAppSettings(application)
        if (mSetingsFromFirebase?.admobAds?.interstitialAds2?.enable == true)
        {
            mInterstitialAd = InterstitialAd(this);
            requsteToLoadIntestialAd()
        }

        mInterstitialAd?.adListener = object : AdListener() {

            override fun onAdFailedToLoad(p0: Int) {
                super.onAdFailedToLoad(p0)
                when (p0) {
                    3 -> {
                        Log.d(TAG,"add not loaded")
                        requsteToLoadIntestialAd()
                    }
                }
            }

            override fun onAdLoaded() {
                super.onAdLoaded()
                Log.d(TAG,"ad loaded")
            }
        }
        showCMandKGscaleFragment()

    }

    private fun requsteToLoadIntestialAd() {
        if (ConsentInformation.getInstance(this).consentStatus == ConsentStatus.PERSONALIZED) {
            showNonPersonalizedAds()
        } else if (ConsentInformation.getInstance(this).consentStatus == ConsentStatus.NON_PERSONALIZED) {
            showNonPersonalizedAds()
        } else {
            showPersonalizedAds()
        }
    }

    private fun showCMandKGscaleFragment() {
        val cMandKGscaleFragment = CMandKGscaleFragment.newInstance("", "")
        val fm = supportFragmentManager
        fm.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.container, cMandKGscaleFragment)
                .commitAllowingStateLoss()
    }

    private fun showINCandLBSscaleFragment() {
        val inCandLBSscaleFragment = INCandLBSscaleFragment()
        val fm = supportFragmentManager
        fm.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.container, inCandLBSscaleFragment)
                .commitAllowingStateLoss()
    }

    private fun showPersonalizedAds() {


        if (mSetingsFromFirebase?.admobAds?.interstitialAds2?.id == null) {
            mInterstitialAd?.adUnitId = Admob.INTERSTITIAL_AD_ID
        } else {
            mInterstitialAd?.adUnitId = mSetingsFromFirebase?.admobAds?.interstitialAds2?.id
        }
        mInterstitialAd?.loadAd(AdRequest.Builder().build());

    }

    private fun showNonPersonalizedAds() {


        if (mSetingsFromFirebase?.admobAds?.interstitialAds2?.id == null) {
            mInterstitialAd?.adUnitId = Admob.INTERSTITIAL_AD_ID
        } else {
            mInterstitialAd?.adUnitId = mSetingsFromFirebase?.admobAds?.interstitialAds2?.id
        }
        val adRequest = AdRequest.Builder()
                .addNetworkExtrasBundle(AdMobAdapter::class.java, getNonPersonalizedAdsBundle())

                .build()
        mInterstitialAd?.loadAd(adRequest)

    }

    fun getNonPersonalizedAdsBundle(): Bundle {
        val extras = Bundle()
        extras.putString("npa", "1")

        return extras
    }


}
