package com.yourdomain.project50.Activitys

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatCheckBox
import android.support.v7.widget.AppCompatRadioButton
import android.widget.Button
import android.widget.TextView
import com.yourdomain.project50.R
import com.yourdomain.project50.Scale.view.DecimalScaleRulerView
import com.yourdomain.project50.Scale.view.ScaleRulerView


class ScaleActivity : AppCompatActivity() {


    lateinit var checkBoxPrivacyPolicy: AppCompatCheckBox
    lateinit var radioCentimenter: AppCompatRadioButton
    lateinit var mHeightWheelView: ScaleRulerView
    lateinit var mWeightWheelView: DecimalScaleRulerView
    lateinit var radioInch: AppCompatRadioButton
    lateinit var btNext :Button

    lateinit var tvHight: TextView
    lateinit var tvWaight: TextView
    lateinit var tvHightType: TextView
    lateinit var tvWaightType: TextView

    private var mHeight = 170f
    private val mMaxHeight = 220f
    private val mMinHeight = 100f


    private var mWeight = 60.0f
    private val mMaxWeight = 200f
    private val mMinWeight = 25f

    private var  mCM_MODE=1
    private var agreeToPrivicyPolicy=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scale)

        mHeightWheelView = findViewById(R.id.scaleWheelView_height)
        checkBoxPrivacyPolicy = findViewById(R.id.PrivacyPolicyCheckBox)
        mWeightWheelView = findViewById(R.id.scaleWheelView_weight)
        radioCentimenter = findViewById(R.id.radioCentimenter)
        tvHight = findViewById(R.id.tv_user_height_value)
        tvWaight = findViewById(R.id.tv_user_weight_value)
        tvWaightType = findViewById(R.id.tv_waight_type)
        tvHightType = findViewById(R.id.tv_hight_type)
        radioInch = findViewById(R.id.radioInch)
        btNext = findViewById(R.id.btNext)


    }


}
