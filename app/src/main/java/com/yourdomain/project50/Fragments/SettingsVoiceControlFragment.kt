package com.yourdomain.project50.Fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Switch
import com.yourdomain.project50.MY_Shared_PREF
import com.yourdomain.project50.Model.Settings
import com.yourdomain.project50.R


class SettingsVoiceControlFragment : DialogFragment() {

    private var mListener: OnVoicecontrolChangeListener? = null
    private lateinit var btMute: Switch
    private lateinit var btCoutchTips: Switch
    private lateinit var btVoiceGuide: Switch
    private var settings: Settings? = null
    private val TAG = "Setting_Voice_Contro";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settings = MY_Shared_PREF.getAppSettings(activity?.application!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_setting__voice__control_, container, false)
        btMute = view.findViewById(R.id.btMute)
        btCoutchTips = view.findViewById(R.id.btCoutchTips)
        btVoiceGuide = view.findViewById(R.id.btVoiceGiude)

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

            MY_Shared_PREF.saveAppSettings(activity?.application!!, settings!!)
            mListener?.onVoiceSettingUpdate(settings!!)
        })
        btVoiceGuide.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, isChecked ->
            Log.d(TAG, "btVoiceGuide: " + isChecked);
            settings?.workoutSettings?.voiceGuide = isChecked

            MY_Shared_PREF.saveAppSettings(activity?.application!!, settings!!)
            mListener?.onVoiceSettingUpdate(settings!!)
        })
        btCoutchTips.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, isChecked ->
            Log.d(TAG, "btCoutchTips: " + isChecked);
            settings?.workoutSettings?.CoachTips = isChecked

            MY_Shared_PREF.saveAppSettings(activity?.application!!, settings!!)
            mListener?.onVoiceSettingUpdate(settings!!)
        })
        updateUI()
        return view
    }

    private fun updateUI() {
        settings?.let {
            btMute.isChecked = it.workoutSettings.mute
            btVoiceGuide.isChecked = it.workoutSettings.voiceGuide
            btCoutchTips.isChecked = it.workoutSettings.CoachTips

        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnVoicecontrolChangeListener) {
            mListener = context
        } else {
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    interface OnVoicecontrolChangeListener {
        // TODO: Update argument type and name
        fun onVoiceSettingUpdate(updateSettings: Settings)
    }
}// Required empty public constructor
