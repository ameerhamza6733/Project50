package com.yourdomain.project50.Activitys

import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.CompoundButton
import android.widget.Switch
import android.widget.TextView
import android.widget.TimePicker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.yourdomain.project50.Fragments.RateUsFragment
import com.yourdomain.project50.Fragments.SecondsPickerFragment
import com.yourdomain.project50.Fragments.TTSLauguagePicker
import com.yourdomain.project50.Fragments.WatingCountDownSecondsPicker
import com.yourdomain.project50.MY_Shared_PREF
import com.yourdomain.project50.Model.Settings
import com.yourdomain.project50.R
import com.yourdomain.project50.TTSHelperService
import com.yourdomain.project50.Utils
import com.yourdomain.project50.WorkMangers.ComeBackLatterWorkManger
import com.yourdomain.project50.WorkMangers.RemindMeEveryDayWorkManger
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class SettingsActivity : AppCompatActivity(), SecondsPickerFragment.OnSecondsPickerListener, WatingCountDownSecondsPicker.OnWattingCountDownSecondPickerListener {


    override fun onWattingSecondPicked(seconds: Int) {
        settings?.workoutSettings?.watingCoutDownTime = seconds
        btCountDownPicker.text = seconds.toString() + " s"
        MY_Shared_PREF.saveAppSettings(application, settings!!)
    }

    override fun onPicker(seconds: Int) {
        settings?.workoutSettings?.restTimeInSeconds = seconds
        btRestPicker.text = seconds.toString() + " s"
        MY_Shared_PREF.saveAppSettings(application, settings!!)
    }

    private lateinit var btMute: Switch
    private lateinit var btCoutchTips: Switch
    private lateinit var btVoiceGuide: Switch
    private lateinit var btCountDownPicker: TextView
    private lateinit var btRestPicker: TextView
    private lateinit var btSetectTTSEngine: TextView
    private lateinit var btDownloadTTSEngine: TextView
    private lateinit var btTTSDeviceSetting: TextView
    private lateinit var btTestVoice: TextView
    private lateinit var btTTSLanguage: TextView
    private lateinit var btSHare: TextView
    private lateinit var btFeedback: TextView
    private lateinit var btPrivacyPolicy: TextView
    private lateinit var btRateUs: TextView
    private lateinit var btRemindMeEveryDay: TextView

    private val TAG = "SettingsActivity";
    private var settings: Settings? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        settings = MY_Shared_PREF.getAppSettings(application)

        findViews()
        onClicks()
        updateUI()

        Log.d(TAG, "" + settings?.workoutSettings?.CoachTips)
    }

    private fun findViews() {
        btMute = findViewById(R.id.btMute)
        btCoutchTips = findViewById(R.id.btCoutchTips)
        btVoiceGuide = findViewById(R.id.btVoiceGiude)
        btCountDownPicker = findViewById(R.id.btCountDownPicker)
        btRestPicker = findViewById(R.id.btRestPicker)
        btSetectTTSEngine = findViewById(R.id.btSetectTTS)
        btDownloadTTSEngine = findViewById(R.id.btDownloadTTSEngine)
        btTTSDeviceSetting = findViewById(R.id.btDeviceTTSSettings)
        btTestVoice = findViewById(R.id.btTestVoice)
        btTTSLanguage = findViewById(R.id.btVoicelanguage)
        btSHare = findViewById(R.id.btShare)
        btFeedback = findViewById(R.id.btFeedback)
        btPrivacyPolicy = findViewById(R.id.btPrivacyPolicy)
        btRemindMeEveryDay = findViewById(R.id.btRemindMe)
        btRateUs = findViewById(R.id.btRateUs)

    }

    private fun onClicks() {
        btMute.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, isChecked ->
            Log.d(TAG, "btMute: " + isChecked);
            if (isChecked) {
                btVoiceGuide.isClickable = false;
                btCoutchTips.isClickable = false;
                btVoiceGuide.isChecked = false
                btCoutchTips.isChecked = false
            } else {
                btVoiceGuide.isClickable = true;
                btCoutchTips.isClickable = true;
                btVoiceGuide.isChecked = true
                btCoutchTips.isChecked = true
            }
            settings?.workoutSettings?.CoachTips = btCoutchTips.isChecked
            settings?.workoutSettings?.mute = btMute.isChecked
            settings?.workoutSettings?.voiceGuide = btVoiceGuide.isChecked

            MY_Shared_PREF.saveAppSettings(application, settings!!)
        })
        btVoiceGuide.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, isChecked ->
            Log.d(TAG, "btVoiceGuide: " + isChecked);
            settings?.workoutSettings?.voiceGuide = isChecked

            MY_Shared_PREF.saveAppSettings(application, settings!!)
        })
        btCoutchTips.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, isChecked ->
            Log.d(TAG, "btCoutchTips: " + isChecked);
            settings?.workoutSettings?.CoachTips = isChecked

            MY_Shared_PREF.saveAppSettings(application, settings!!)
        })

        btCountDownPicker.setOnClickListener {
            val countDownSecondsPicker = WatingCountDownSecondsPicker.newInstance(settings?.workoutSettings?.watingCoutDownTime!!) as WatingCountDownSecondsPicker
            countDownSecondsPicker.setStyle(DialogFragment.STYLE_NORMAL, R.style.dialog);
            countDownSecondsPicker.show(supportFragmentManager, "countDownSecondsPicker")
        }

        btRestPicker.setOnClickListener {
            val secondsPickerFragment = SecondsPickerFragment.newInstance(settings?.workoutSettings?.restTimeInSeconds!!) as SecondsPickerFragment
            secondsPickerFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.dialog);
            secondsPickerFragment.show(supportFragmentManager, "SecondsPickerFragment")

        }

        btSetectTTSEngine.setOnClickListener { Utils.startTTSScreen(applicationContext) }

        btDownloadTTSEngine.setOnClickListener {
            val goToMarket = Intent(Intent.ACTION_VIEW).setData(Uri.parse("market://search?q=text to speech"))
            startActivity(goToMarket)
        }

        btTTSDeviceSetting.setOnClickListener {
            Utils.startTTSScreen(applicationContext)
        }

        btTestVoice.setOnClickListener { sendTTSBroadCast(getString(R.string.did_you_hear_test_voice)) }
        btTTSLanguage.setOnClickListener {
            val ttsLauguagePicker = TTSLauguagePicker()
            ttsLauguagePicker.setStyle(DialogFragment.STYLE_NORMAL, R.style.dialog);
            ttsLauguagePicker.show(supportFragmentManager, "ttsLauguagePicker")
        }

        btPrivacyPolicy.setOnClickListener { Utils.openBrowser(application, getString(R.string.privacy_policy)) }

        btFeedback.setOnClickListener { Utils.feedbackEmaileIntent(this@SettingsActivity) }

        btRateUs.setOnClickListener {
            val rateUsFragment = RateUsFragment()
            rateUsFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.dialog);
            rateUsFragment.show(supportFragmentManager, "rateUsFragment")
        }

        btSHare.setOnClickListener { Utils.shareTextIntent(this@SettingsActivity, "I have just completed Day" + "N" + "of " + getString(R.string.app_name) + ". Join me: https://play.google.com/store/apps/details?id=" + application.packageName) }

        btRemindMeEveryDay.setOnClickListener {
            showHourPicker()
        }
    }

    fun showHourPicker() {
        val myCalender = Calendar.getInstance()
        val hour = myCalender.get(Calendar.HOUR_OF_DAY)
        val minute = myCalender.get(Calendar.MINUTE)


        val myTimeListener = object : TimePickerDialog.OnTimeSetListener {
            override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
                if (view.isShown) {
                    myCalender.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    myCalender.set(Calendar.MINUTE, minute)
                    val sdf = SimpleDateFormat("HH:mm")
                    val str = sdf.format(Date())
                    Log.d(TAG, "current hour : " + str.split(":")[0] + " current mint: " + str.split(":")[1] + " user pick hour " + hourOfDay + " user pick mints: " + minute)

                    var notifactioAfterHours = Math.abs((hourOfDay - str.split(":")[0].toInt()))
                    var notifactionAfterMints = Math.abs((minute - str.split(":")[1].toInt()))

                    Log.d(TAG, "notifactioAfterHours:  " + notifactioAfterHours + " notifactionAfterMints: " + notifactionAfterMints)

                    val intiDaly = ((notifactioAfterHours * 60) + notifactionAfterMints).toLong()
                    sacduleThePaddingNotifaction(intiDaly)
                }
            }
        }
        val timePickerDialog = TimePickerDialog(this, R.style.TheamTimePicker, myTimeListener, hour, minute, false)
        timePickerDialog.setTitle("When you want reminder")
        timePickerDialog.show()
    }

    private fun sacduleThePaddingNotifaction(intiDelay: Long) {
        Log.d(TAG, "shauding notifaction for 30 mints")
        val postNotationWithDelay = OneTimeWorkRequest
                .Builder(RemindMeEveryDayWorkManger::class.java)
                .setInitialDelay(intiDelay, TimeUnit.MINUTES).build()


        val workManager = WorkManager.getInstance()
        workManager.beginUniqueWork(
                "RemindMeEveryDayWorkMangerTAG",
                ExistingWorkPolicy.REPLACE,
                postNotationWithDelay
        ).enqueue()

    }

    private fun updateUI() {
        settings?.let {
            btMute.isChecked = it.workoutSettings.mute
            btVoiceGuide.isChecked = it.workoutSettings.voiceGuide
            btCoutchTips.isChecked = it.workoutSettings.CoachTips
            btRestPicker.text = it.workoutSettings.restTimeInSeconds.toString() + "s"
            btCountDownPicker.text = it.workoutSettings.watingCoutDownTime.toString() + "s"
        }
    }

    private fun sendTTSBroadCast(text: String) {


        val intent = Intent(TTSHelperService.ACTION_TTS)
        intent.putExtra("TTStext", text)
        LocalBroadcastManager.getInstance(this@SettingsActivity.applicationContext!!).sendBroadcast(intent)

    }
//
//    private fun initSupportedLanguagesLegacy():HashMap<String,String> {
//       var hashMap=HashMap<String,String>()
//        hashMap.put("Italian-ITA","")
//        hashMap.put("English-AUS","")
//        hashMap.put("English-USA","")
//        hashMap.put("Turkish-TUR","")
//        hashMap.put("Russian-RUS","")
//        hashMap.put("German-DEU","")
//        hashMap.put("Spanish-ESP","")
//        hashMap.put("Bengali-BGD","")
//        hashMap.put("Norwegian Bokmal-NOR","")
//        hashMap.put("Spanish-MEX","")
//        hashMap.put("Portuguese-BRA","")
//        hashMap.put("English-GBR","")
//        hashMap.put("Indonesian-IDN","")
//        hashMap.put("Korean","")
//        hashMap.put("English-IND","")
//        hashMap.put("French-FRA","")
//        hashMap.put("Polish-POL","")
//        hashMap.put("Cantonese-HKG","")
//        hashMap.put("Chinese-TWN","")
//        hashMap.put("Hungarian-HUN","")
//        hashMap.put("Finish-FIN","")
//        hashMap.put("Japanese-JPN","")
//        hashMap.put("Chinese-CHN","")
//        hashMap.put("Spanish-USA","")
//        hashMap.put("Thai-THA","")
//        hashMap.put("Danish-DNK","")
//        hashMap.put("Dutch-NLD","")
//        hashMap.put("Hindi-IND","")
//        return hashMap
//    }

}
