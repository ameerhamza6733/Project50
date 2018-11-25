package com.yourdomain.project50.Activitys

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.CompoundButton
import android.widget.Switch
import android.widget.TextView
import com.yourdomain.project50.Fragments.SecondsPickerFragment
import com.yourdomain.project50.Fragments.WatingCountDownSecondsPicker
import com.yourdomain.project50.Fragments.WatingToStartExcersizeFragment
import com.yourdomain.project50.MY_Shared_PREF
import com.yourdomain.project50.Model.Settings
import com.yourdomain.project50.R


class SettingsActivity : AppCompatActivity(),SecondsPickerFragment.OnSecondsPickerListener,WatingCountDownSecondsPicker.OnWattingCountDownSecondPickerListener {
    override fun onWattingSecondPicked(seconds: Int) {
        settings?.workoutSettings?.watingCoutDownTime=seconds
        btCountDownPicker.text=seconds.toString()+" s"
        MY_Shared_PREF.saveAppSettings(application,settings!!)
    }

    override fun onPicker(seconds: Int) {
        settings?.workoutSettings?.restTimeInSeconds=seconds
        btRestPicker.text=seconds.toString()+" s"
        MY_Shared_PREF.saveAppSettings(application,settings!!)
    }

    private lateinit var btMute: Switch
    private lateinit var btCoutchTips: Switch
    private lateinit var btVoiceGuide: Switch
    private lateinit var btCountDownPicker: TextView
    private lateinit var btRestPicker: TextView

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

    }

    private fun onClicks(){
        btMute.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, isChecked ->
            Log.d(TAG,"btMute: "+isChecked);
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
            Log.d(TAG,"btVoiceGuide: "+isChecked);
            settings?.workoutSettings?.voiceGuide = isChecked

            MY_Shared_PREF.saveAppSettings(application, settings!!)
        })
        btCoutchTips.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, isChecked ->
            Log.d(TAG,"btCoutchTips: "+isChecked);
            settings?.workoutSettings?.CoachTips = isChecked

            MY_Shared_PREF.saveAppSettings(application, settings!!)
        })

        btCountDownPicker.setOnClickListener {
            val  countDownSecondsPicker=WatingCountDownSecondsPicker.newInstance(settings?.workoutSettings?.watingCoutDownTime!!) as  WatingCountDownSecondsPicker
            countDownSecondsPicker.setStyle(DialogFragment.STYLE_NORMAL, R.style.dialog);
            countDownSecondsPicker.show(supportFragmentManager, "countDownSecondsPicker")
        }

        btRestPicker.setOnClickListener {
            val  secondsPickerFragment=SecondsPickerFragment.newInstance(settings?.workoutSettings?.restTimeInSeconds!!) as SecondsPickerFragment
            secondsPickerFragment.setStyle(DialogFragment.STYLE_NORMAL,R.style.dialog);
            secondsPickerFragment.show(supportFragmentManager, "SecondsPickerFragment")

        }
    }

    private fun updateUI(){
       settings?.let {
           btMute.isChecked= it.workoutSettings.mute
           btVoiceGuide.isChecked= it.workoutSettings.voiceGuide
           btCoutchTips.isChecked=it.workoutSettings.CoachTips
           btRestPicker.text=it.workoutSettings.restTimeInSeconds.toString()+"s"
           btCountDownPicker.text=it.workoutSettings.watingCoutDownTime.toString()+"s"
       }
    }

}
