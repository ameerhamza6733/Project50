package com.yourdomain.project50.Activitys


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.ads.consent.*
import com.google.android.gms.ads.MobileAds
import com.yourdomain.project50.MY_Shared_PREF
import com.yourdomain.project50.Model.AppAdmobSettingsFromFirebase
import com.yourdomain.project50.R
import com.yourdomain.project50.TTSHelperService
import com.yourdomain.project50.Utils
import com.yourdomain.project50.ViewModle.GetAdmobDataFromFireBaseViewModle
import java.net.MalformedURLException
import java.net.URL
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.crashlytics.android.Crashlytics
import com.yourdomain.project50.WorkMangers.RemindMeAfter48Hour
import java.util.concurrent.TimeUnit


class SplashActivity : AppCompatActivity() {
    val TAG = "SpalishActivityTAG"
    private var form: ConsentForm? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spalish)
        Crashlytics.log("SplashActivity on Create")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val w = window
            w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
        val image = findViewById<ImageView>(R.id.spalishImageVIew)
        val tvSpalishTitle = findViewById<TextView>(R.id.tvSpalishTitle);

        tvSpalishTitle.startAnimation(AnimationUtils.loadAnimation(this, R.anim.animation_text_view));
        Glide.with(this).load(R.drawable.spalish4).into(image)
        MobileAds.initialize(this, getString(R.string.admob_app_id));
      scheduleThe48HourNotifications(48)
       if (Utils.isNetworkAvailable(application)){
           ViewModelProviders.of(this).get(GetAdmobDataFromFireBaseViewModle::class.java).getAppSettingFromFireBase()?.observe(this, Observer {
               if (it != null) {
                   MY_Shared_PREF.saveFireBaseAppAdmobSetting(application, it)
                   checkForConsent(it)
               }
           })
       }else{
           val handler = Handler()
           handler.postDelayed(Runnable {
               showPersonalizedAds()
           }, 3*1000)

       }


    }

    private fun scheduleThe48HourNotifications(intiDelay: Long) {

        val postNotationWithDelay = OneTimeWorkRequest
                .Builder(RemindMeAfter48Hour::class.java)
                .setInitialDelay(intiDelay, TimeUnit.HOURS).build()


        val workManager = WorkManager.getInstance()
        workManager.beginUniqueWork(
                "RemindMeAfter48HourTAG",
                ExistingWorkPolicy.REPLACE,
                postNotationWithDelay
        ).enqueue()

    }
    private fun checkForConsent(appAdmobSettingsFromFirebase: AppAdmobSettingsFromFirebase) {
        val consentInformation = ConsentInformation.getInstance(this@SplashActivity)
        val publisherIds = arrayOf(appAdmobSettingsFromFirebase?.admobAds?.publisherId)
        consentInformation.requestConsentInfoUpdate(publisherIds, object : ConsentInfoUpdateListener {
            override fun onConsentInfoUpdated(consentStatus: ConsentStatus) {
                // User's consent status successfully updated.
                when (consentStatus) {
                    ConsentStatus.PERSONALIZED -> {
                        Log.d(TAG, "Showing Personalized ads")
                        showPersonalizedAds()
                    }
                    ConsentStatus.NON_PERSONALIZED -> {
                        Log.d(TAG, "Showing Non-Personalized ads")
                        showNonPersonalizedAds()
                    }
                    ConsentStatus.UNKNOWN -> {
                        Log.d(TAG, "UNKNOWN")
                        if (ConsentInformation.getInstance(baseContext).isRequestLocationInEeaOrUnknown) {
                            Log.d(TAG, "requestConsent")
                            requestConsent()
                        } else {
                            Log.d(TAG, "showPersonalizedAds")
                            showPersonalizedAds()
                        }
                    }
                    else -> {
                    }
                }
            }

            override fun onFailedToUpdateConsentInfo(errorDescription: String) {
                // User's consent status failed to update.
            }
        })
    }

    private fun showNonPersonalizedAds() {
        if (MY_Shared_PREF.isPersonAppearanceSaved(application)){
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))

        }else{
            startActivity(Intent(this@SplashActivity, ScaleActivity::class.java))

        }
        startService(Intent(this@SplashActivity, TTSHelperService::class.java))


        finish()
    }

    private fun showPersonalizedAds() {
        startService(Intent(this@SplashActivity, TTSHelperService::class.java))
        if (MY_Shared_PREF.isPersonAppearanceSaved(application)){
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))

        }else{
            startActivity(Intent(this@SplashActivity, ScaleActivity::class.java))

        }
        finish()
    }

    private fun requestConsent() {
        var privacyUrl: URL? = null
        try {
            // TODO: Replace with your app's privacy policy URL.
            /*
            watch this video how to create privacy policy in mint
            https://www.youtube.com/watch?v=lSWSxyzwV-g&t=140s
            */
            privacyUrl = URL(getString(R.string.privacy_policy))
        } catch (e: MalformedURLException) {
            e.printStackTrace()
            // Handle error.
        }

        form = ConsentForm.Builder(this@SplashActivity, privacyUrl)
                .withListener(object : ConsentFormListener() {
                    override fun onConsentFormLoaded() {
                        // Consent form loaded successfully.
                        Log.d(TAG, "Requesting Consent: onConsentFormLoaded")
                        showForm()
                    }

                    override fun onConsentFormOpened() {
                        // Consent form was displayed.
                        Log.d(TAG, "Requesting Consent: onConsentFormOpened")
                    }

                    override fun onConsentFormClosed(
                            consentStatus: ConsentStatus?, userPrefersAdFree: Boolean?) {
                        Log.d(TAG, "Requesting Consent: onConsentFormClosed")
                        if (userPrefersAdFree!!) {
                            // Buy or Subscribe
                            Log.d(TAG, "Requesting Consent: User prefers AdFree")
                        } else {
                            Log.d(TAG, "Requesting Consent: Requesting consent again")
                            when (consentStatus) {
                                ConsentStatus.PERSONALIZED -> showPersonalizedAds()
                                ConsentStatus.NON_PERSONALIZED -> showNonPersonalizedAds()
                                ConsentStatus.UNKNOWN -> showNonPersonalizedAds()
                            }

                        }
                        // Consent form was closed.
                    }

                    override fun onConsentFormError(errorDescription: String?) {
                        Log.d(TAG, "Requesting Consent: onConsentFormError. Error - " + errorDescription!!)
                        // Consent form error.
                    }
                })
                .withPersonalizedAdsOption()
                .withNonPersonalizedAdsOption()
                .withAdFreeOption()
                .build()
        form?.load()
    }

    /*
 want test your app watch this video https://youtu.be/_JOapnq8hrs?t=654
 */


    private fun showForm() {
        if (form == null) {
            Log.d(TAG, "Consent form is null")
        }
        if (form != null) {
            Log.d(TAG, "Showing consent form")
            form?.show()
        } else {
            Log.d(TAG, "Not Showing consent form")
        }
    }
}
