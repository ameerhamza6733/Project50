package com.yourdomain.project50.Fragments

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import com.yourdomain.project50.MY_Shared_PREF
import com.yourdomain.project50.Model.Settings
import com.yourdomain.project50.R
import com.yourdomain.project50.TTSHelperService
import java.util.*

/**
 * Created by apple on 11/25/18.
 */

class TTSLauguagePicker : DialogFragment() {
    private var localeList: ArrayList<Locale>? = null
    private var adupterPostion=0
    private var settings: Settings? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        localeList = TTSHelperService.initSupportedLanguagesLegacy()
        settings=MY_Shared_PREF.getAppSettings(activity?.application!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_tts_lauguage_picker, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recylerview)
        localeList?.let {
            recyclerView.layoutManager = LinearLayoutManager(activity)
            recyclerView.setHasFixedSize(true)
            recyclerView.adapter = TTSLauguageAdupter()

        }
        return view
    }

    inner class TTSLauguageAdupter : RecyclerView.Adapter<TTSLauguageAdupter.MyViewHolder>() {
        override fun onBindViewHolder(p0: MyViewHolder, p1: Int) {
            p0.radioButton.text = localeList?.get(p0.adapterPosition)?.displayLanguage + " - " + localeList?.get(p0.adapterPosition)?.displayCountry
            p0.radioButton.setOnClickListener {
               adupterPostion=p0.adapterPosition
                val ttsSettings = settings?.ttsSettings
                ttsSettings?.locale = localeList?.get(adupterPostion)!!
                settings?.ttsSettings = ttsSettings!!
                MY_Shared_PREF.saveAppSettings(activity?.application!!, settings!!)
                TTSHelperService.locale = localeList?.get(adupterPostion)
                try {
                    dismiss()
                }catch (e:Exception){}
            }
        }

        override fun getItemCount(): Int {
            return localeList?.size!!
        }


        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): TTSLauguageAdupter.MyViewHolder {
            return MyViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.each_tts, p0, false))
        }

        inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val radioButton: RadioButton

            init {
                radioButton = itemView.findViewById(R.id.radio_button_tts_lanuage)
            }

        }

    }

}
