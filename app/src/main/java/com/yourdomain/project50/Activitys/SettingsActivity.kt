package com.yourdomain.project50.Activitys

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.FragmentTransaction
import android.widget.Switch
import android.widget.TextView
import com.yourdomain.project50.Fragments.SettingsFragment
import com.yourdomain.project50.R
import kotlinx.android.synthetic.main.activity_settings.*
import org.w3c.dom.Text

class SettingsActivity : AppCompatActivity() {

    private lateinit var btMute:Switch
    private lateinit var btCoutchTips:Switch
    private lateinit var btVoiceGuide:Switch
    private lateinit var btCountDownPicker:TextView
    private lateinit var btRestPicker:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        findViews()

    }

    private fun findViews() {
        btMute=findViewById(R.id.btMute)
        btCoutchTips=findViewById(R.id.btCoutchTips)
        btVoiceGuide=findViewById(R.id.btVoiceGiude)
        btCountDownPicker=findViewById(R.id.btCountDownPicker)
        btRestPicker=findViewById(R.id.btRestPicker)
    }
}
