package com.yourdomain.project50.Activitys

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.DialogFragment
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import com.bumptech.glide.Glide
import com.google.ads.consent.ConsentInformation
import com.google.ads.consent.ConsentStatus
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.reward.RewardedVideoAd
import com.yourdomain.project50.*
import com.yourdomain.project50.Fragments.*
import com.yourdomain.project50.Model.*
import com.yourdomain.project50.Utils.CountTotalTime
import com.yourdomain.project50.ViewModle.ExcersizesByDayandTypeViewModle
import java.util.concurrent.TimeUnit


class ExcersizeActivity : AppCompatActivity(), WatingToStartExcersizeFragment.OnFragmentInteractionListener, PauseExcersizeFragment.OnResumeListener, QuitFragment.OnQuitListener, RestFragment.OnNextExcersizeDemoFragmentListener, SettingsVoiceControlFragment.OnVoicecontrolChangeListener {
    override fun onVoiceSettingUpdate(updateSettings: Settings) {
        Log.d(TAG, "new settings" + this@ExcersizeActivity.settings.workoutSettings.mute)
        this@ExcersizeActivity.settings = updateSettings;
    }

    override fun onSkip() {
        if (counter + 1 < excesizes?.icons?.size!!) {
            onNext(true);
        } else {
            sayCongragulation()
        }
    }

    override fun onQuit() {
        finish()
    }

    override fun onContinue() {
        countDown?.resume()
    }

    override fun onComeBacKLater() {
    }

    override fun ResumeListener() {
        countDown?.resume()
    }

    override fun onCountDownDonw() {
        if (counter + 1 < excesizes?.icons?.size!!) {
            onNext(true)
        } else {
            sayCongragulation()
        }

    }

    private fun sayCongragulation() {
        var person = MY_Shared_PREF.getPerson(application)
        var totleCal = person.totleKCalBurn

        for (i in excesizes!!.calories)
            totleCal = totleCal + i
        person.totleKCalBurn = totleCal

        person.totleEXERCISES = person.totleEXERCISES + excesizes?.title?.size!!
        person.totleMintsDuration = person.totleMintsDuration + totleTime.toDouble()
        MY_Shared_PREF.savePerson(application, person)

        val intent = Intent(this@ExcersizeActivity, CongragulationActivity::class.java)
        intent.putExtra(CongragulationActivity.EXTRA_DURACTION, person.totleMintsDuration)
        intent.putExtra(CongragulationActivity.EXTRA_EXCERSIZES, person.totleEXERCISES)
        intent.putExtra(CongragulationActivity.EXTRA_DAY, currentDayKey + 1)
        intent.putExtra(CongragulationActivity.EXTRA_CAL, person.totleKCalBurn)

        startActivity(intent)
        finish()

    }


    companion object {
        val EXTRA_DAY = "ExcersizeActivity.extra day";
        val EXTRA_EXCERSIZES_DONE = "ExcersizeListActivity.EXTRA_EXCERSIZES_DONE"
        val EXTRA_PLAN = "ExcersizeActivity.EXTRA_PLA"
    }

    private lateinit var mTotalProgressBar: ProgressBar
    private lateinit var mCurrentProgressBar: ProgressBar
    private lateinit var mtotalTextView: TextView
    private lateinit var mImageVIew: ImageView
    private lateinit var mTotalSeconds: TextView
    private lateinit var mtvescription: TextView
    private lateinit var mtvTitle: TextView
    private lateinit var mbtSpeaker: ImageButton
    private lateinit var mbtStop: TextView
    private lateinit var mLayout: LinearLayout
    private lateinit var mbtNext: ImageButton
    private lateinit var mbtBack: ImageButton
    private lateinit var mbtdone: ImageButton

    private var currentDay: ExcersizeDay? = null
    private var excesizes: Excesizes? = null
    private var counter = -1
    private var countDown: CustomCountDownTimer? = null
    private var mediaPlayer: MediaPlayer? = null
    private var currentDayKey: Int = -3
    private var excersizeDone = -2
    private var currentPlan = "-2"

    private var TAG = "ExcersizeActivity";

    private var totleTime = ""
    private lateinit var settings: Settings

    override fun onStart() {
        super.onStart()
        settings = MY_Shared_PREF.getAppSettings(application)

    }

    private var adRequest: AdRequest? = null
    private lateinit var adContainer: RelativeLayout
    private var mSetingsFromFirebase: AppAdmobDataFromFirebase? = null
    private var mRewardedVideoAd:RewardedVideoAd?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.each_full_screen_excersize)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val w = window
            w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
        findViews()

        currentDayKey = intent.getIntExtra(EXTRA_DAY, -2)
        excersizeDone = intent.getIntExtra(EXTRA_EXCERSIZES_DONE, -2)
        currentPlan = intent.getStringExtra(EXTRA_PLAN)
        if (currentDayKey != -2) {
            currentDay = MY_Shared_PREF.getDayByKey(application, currentPlan + currentDayKey.toString())

        }
        if (excersizeDone != -2) {
            counter = excersizeDone
        }
        val modle = ViewModelProviders.of(this)[ExcersizesByDayandTypeViewModle::class.java]
        modle.getExcersizs(currentDayKey, currentPlan)?.observe(this, Observer {
            if (it != null) {
                excesizes = it
                var string = ""
                if (it.viewType[counter + 1] == Excesizes.VIEW_TYPE_LIMTED_EXCERSIZE) {
                    string = it.seconds[counter + 1].toString() + "''"
                } else if (it.viewType[counter + 1] == Excesizes.VIEW_TYPE_UN_LIMTED_EXCERSIZE) {
                    string = "x" + it.seconds[counter + 1].toString()
                }

                if (settings.workoutSettings.voiceGuide) {
                    sendTTSBroadCast(getString(R.string.ready_to_go))
                    sendTTSBroadCast(getString(R.string.next))
                    sendTTSBroadCast(it.title[counter + 1])
                }
                totleTime = CountTotalTime(it.viewType, it.seconds)
                val fragmet = WatingToStartExcersizeFragment.newInstance(totleTime, string, it.detail[counter + 1], settings.workoutSettings.watingCoutDownTime)
                fragmet.setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                fragmet.show(supportFragmentManager, "WatingToStartExcersizeFragment")
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

        loadBannerAds()
        loadVideoAd()
    }

    private fun loadBannerAds() {
        val adView = AdView(this)
        adView.adSize = AdSize.BANNER
        var adId = Admob.BANNER_AD_ID
        mSetingsFromFirebase?.admobAds?.bannerAds?.id?.let {
            adId = it
        }
        adView.adUnitId = adId
        adContainer.addView(adView)
        adView.loadAd(adRequest)

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

    private fun findViews() {
        mTotalProgressBar = findViewById(R.id.totalProgressBar)
        mCurrentProgressBar = findViewById(R.id.thisProgressBar)
        mImageVIew = findViewById(R.id.icon)
        mTotalSeconds = findViewById(R.id.tvSeconds)
        mtvTitle = findViewById(R.id.tvTitle)
        mtvescription = findViewById(R.id.tvDescription)
        mtotalTextView = findViewById(R.id.tvRemaning);
        mbtStop = findViewById(R.id.btPassExcersize)
        mbtSpeaker = findViewById(R.id.btSpeaker)
        mbtNext = findViewById(R.id.btNext)
        mbtBack = findViewById(R.id.btBack)
        mbtdone = findViewById(R.id.btDone)
        mLayout = findViewById(R.id.type_unlimted)
        adContainer = findViewById(R.id.adContanir)


        mbtdone.setOnClickListener {

            if (counter + 1 < excesizes?.icons?.size!!) {
                onNext(false);
                updateExcersizeCountInSharePref()
            } else {
                sayCongragulation()
            }
        }
        mbtBack.setOnClickListener { onBack() }
        mbtNext.setOnClickListener {
            if (counter + 1 < excesizes?.icons?.size!!) {
                onNext(false);
                updateExcersizeCountInSharePref()
            } else {
                sayCongragulation()
            }
        }
        mbtStop.setOnClickListener {
            try {
                onPauseExcersize()
                countDown?.pause()
            } catch (E: Exception) {
                E.printStackTrace()
            }
        }
        mbtSpeaker.setOnClickListener {
            val settingsVoiceControlFragment = SettingsVoiceControlFragment()
            settingsVoiceControlFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.dialog);
            settingsVoiceControlFragment.show(supportFragmentManager, "settingsVoiceControlFragment")
        }
    }

    private fun onPauseExcersize() {
        var seconds = ""
        if (excesizes?.viewType!![counter] == Excesizes.VIEW_TYPE_LIMTED_EXCERSIZE) {
            seconds = excesizes?.seconds?.get(counter)?.toString() + "''"
        } else if (excesizes?.viewType!![counter] == Excesizes.VIEW_TYPE_UN_LIMTED_EXCERSIZE) {
            seconds = "x" + excesizes?.seconds?.get(counter)?.toString()

        }
        val pauseExcersizeFragment = PauseExcersizeFragment.newInstance(excesizes?.title!![counter], seconds, excesizes?.icons!![counter])
        pauseExcersizeFragment.setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        pauseExcersizeFragment.show(supportFragmentManager, "pauseExcersizeFragment")

    }

    private fun updateWithOutCountDownUI() {

        mTotalProgressBar.max = excesizes?.title?.size!!

        mtotalTextView.text = (counter + 1).toString() + "/" + excesizes?.icons?.size.toString()
        Glide.with(this).asGif().load(excesizes?.icons?.get(counter)).into(mImageVIew)
        if (excesizes?.viewType!![counter] == Excesizes.VIEW_TYPE_LIMTED_EXCERSIZE) {
            mTotalSeconds.text = excesizes?.seconds?.get(counter)?.toString() + "''"
        } else if (excesizes?.viewType!![counter] == Excesizes.VIEW_TYPE_UN_LIMTED_EXCERSIZE) {
            mTotalSeconds.text = "x" + excesizes?.seconds?.get(counter)?.toString()

        }
        mtvescription.text = excesizes?.detail!![counter]
        mtvTitle.text = excesizes?.title!![counter].toUpperCase()
        mTotalProgressBar.progress = counter
        mLayout.visibility = View.VISIBLE
        mCurrentProgressBar.visibility = View.INVISIBLE
        mbtStop.visibility = View.INVISIBLE


    }

    private var handle: Handler? = null
    private var runable: Runnable? = null

    private fun onNext(showWatingForNextFragment: Boolean) {
        if (showWatingForNextFragment) {
            playSound(R.raw.beep_start_exercise)
            handle = Handler()
            runable = Runnable {
                try {
                    if (settings.workoutSettings.CoachTips) {
                        Log.d(TAG, "cuch tips " + getString(excesizes?.couchTips!![counter]))
                        sendTTSBroadCast(getString(excesizes?.couchTips!![counter]!!))
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            handle?.postDelayed(runable, 3 * 1000)

            counter++
            if (excesizes?.viewType!![counter] == Excesizes.VIEW_TYPE_LIMTED_EXCERSIZE) {
                updateUIWithCountDown()
            } else if (excesizes?.viewType!![counter] == Excesizes.VIEW_TYPE_UN_LIMTED_EXCERSIZE) {
                updateWithOutCountDownUI()

            }
        } else {
            var temp = ""
            if (excesizes?.viewType!![counter + 1] == Excesizes.VIEW_TYPE_LIMTED_EXCERSIZE) {
                temp = temp + excesizes?.seconds!![counter + 1].toString() + "s"
            } else if (excesizes?.viewType!![counter + 1] == Excesizes.VIEW_TYPE_UN_LIMTED_EXCERSIZE) {
                updateWithOutCountDownUI()
                temp = temp + "x " + excesizes?.seconds!![counter + 1].toString()
            }
            Log.d(TAG, "showing data for wating fragment: " + temp)
            val watingForNextFragment = RestFragment.newInstance(excesizes?.title!![counter + 1], temp, "NEXT " + (counter + 1).toString() + "/" + (excesizes!!.icons.size).toString(), excesizes?.icons!![counter + 1], settings.workoutSettings.restTimeInSeconds)
            watingForNextFragment.setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            watingForNextFragment.show(supportFragmentManager, "watingForNextFragment")

        }
    }

    private fun onBack() {
        counter--
        if (excesizes?.viewType!![counter] == Excesizes.VIEW_TYPE_LIMTED_EXCERSIZE) {
            updateUIWithCountDown()
        } else if (excesizes?.viewType!![counter] == Excesizes.VIEW_TYPE_UN_LIMTED_EXCERSIZE) {
            updateWithOutCountDownUI()

        }
    }



    private fun updateUIWithCountDown() {
        mLayout.visibility = View.INVISIBLE
        mCurrentProgressBar.visibility = View.VISIBLE
        mbtStop.visibility = View.VISIBLE
        mTotalProgressBar.max = excesizes?.title?.size!!
        mtotalTextView.text = (counter + 1).toString() + "/" + excesizes?.icons?.size.toString()
        Glide.with(this).asGif().load(excesizes?.icons?.get(counter)).into(mImageVIew)
        if (excesizes?.viewType!![counter] == Excesizes.VIEW_TYPE_LIMTED_EXCERSIZE) {
            mTotalSeconds.text = excesizes?.seconds?.get(counter)?.toString() + "''"
        } else if (excesizes?.viewType!![counter] == Excesizes.VIEW_TYPE_UN_LIMTED_EXCERSIZE) {
            mTotalSeconds.text = "x" + excesizes?.seconds?.get(counter)?.toString()

        }
        mtvescription.text = excesizes?.detail!![counter]
        mtvTitle.text = excesizes?.title!![counter].toUpperCase()
        mTotalProgressBar.progress = counter + 1
        mCurrentProgressBar.max = excesizes?.seconds!![counter].toInt()
        var seconds = TimeUnit.SECONDS.toMillis(excesizes?.seconds!![counter]?.toLong())
        var halftime = (excesizes?.seconds!![counter] / 2)
        var half3time = (excesizes?.seconds!![counter] / 2)
        countDown = object : CustomCountDownTimer(seconds, 1000) {
            override fun onFinish() {
                playSound(R.raw.beep_end_exercise)
                if (counter + 1 < excesizes?.icons?.size!!) {
                    onNext(false);

                } else {
                    sayCongragulation()
                }
                mTotalSeconds.text = "0"
                mCurrentProgressBar.progress = 0
                updateExcersizeCountInSharePref()
            }

            override fun onTick(millisUntilFinished: Long) {
                val temseconds = (millisUntilFinished / 1000).toInt()
                mTotalSeconds.text = temseconds.toString()
                mCurrentProgressBar.progress = temseconds


                if (temseconds == halftime) {
                    runOnUiThread {
                        if (settings.workoutSettings.voiceGuide) {
                            sendTTSBroadCast(getString(R.string.half_the_time))
                        }
                    }
                }

                if (temseconds < 4) {
                    if (settings.workoutSettings.voiceGuide) {
                        sendTTSBroadCast(temseconds.toString())
                    }
                }

            }

        }
        countDown?.start()
    }

    private fun updateExcersizeCountInSharePref() {
        Log.d(TAG, "Saving currnet day in shared pref: " + Utils.toPersentage(counter + 1, excesizes?.title?.size!!))
        if (currentDayKey == -3) return

        MY_Shared_PREF.saveDayByKey(application, (currentPlan) + (currentDayKey + 1).toString(), ExcersizeDay(currentDayKey + 1, ExcersizeDay.VIEW_TYPE_DAY, excesizes?.title?.size?.toLong()!!, counter.toLong(), Utils.toPersentage(counter + 1, excesizes?.title?.size!!)))
    }

    override fun onPause() {
        Log.d(TAG, "onPause");
        countDown?.cancel()
        super.onPause()
    }

    override fun onDestroy() {

        handle?.removeCallbacks(runable)
        mediaPlayer?.stop()
        mediaPlayer?.release()
        if (mRewardedVideoAd?.isLoaded==true)
            mRewardedVideoAd?.show()
        super.onDestroy()
    }

    override fun onBackPressed() {
        countDown?.pause()
        if (settings.workoutSettings.voiceGuide) {
            sendTTSBroadCast(getString(R.string.dont_quit_keep_going))
        }
        val quitFragment = QuitFragment()
        quitFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.dialog);
        quitFragment.show(supportFragmentManager, "quitFragment")

    }

    fun sendTTSBroadCast(text: String) {

        if (!settings.workoutSettings.mute) {
            val intent = Intent(TTSHelperService.ACTION_TTS)
            intent.putExtra("TTStext", text)
            LocalBroadcastManager.getInstance(this@ExcersizeActivity.applicationContext!!).sendBroadcast(intent)
        }
    }


    private fun playSound(raw: Int) {

        mediaPlayer = MediaPlayer.create(this, raw);
        mediaPlayer?.start()
    }
}
