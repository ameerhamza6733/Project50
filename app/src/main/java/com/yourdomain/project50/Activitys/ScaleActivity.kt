package com.yourdomain.project50.Activitys

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatCheckBox
import android.support.v7.widget.AppCompatRadioButton
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import com.yourdomain.project50.MY_Shared_PREF
import com.yourdomain.project50.Model.Person
import com.yourdomain.project50.Model.PersonAppearance
import com.yourdomain.project50.R
import com.yourdomain.project50.Scale.view.ScaleRulerView
import com.yourdomain.project50.Utils
import java.lang.annotation.RetentionPolicy


class ScaleActivity : AppCompatActivity(), View.OnClickListener {
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.radioCentimenter -> {
                mCM_MODE=1
                tvWaightType.text = "Kg"
                tvHightType.text = "CM"

                var utils = Utils()
                tvHight.text=utils.FeettoCM(mHeight.toDouble())
                tvWaight.text=utils.LBStoKG(mWeight.toDouble())

                mHeight=tvHight.text.toString().toFloat()
                mWeight=tvWaight.text.toString().toFloat()
            }
            R.id.radioInch -> {
                mCM_MODE=0
                tvWaightType.text = "LBS"
                tvHightType.text = "FT"

                var utils = Utils()
                tvHight.text=utils.CMtoFeet(mHeight.toDouble())
                tvWaight.text=utils.KGtoLBS(mWeight.toDouble())

                mHeight=tvHight.text.toString().toFloat()
                mWeight=tvWaight.text.toString().toFloat()

            }
            R.id.PrivacyPolicyCheckBox->{
                agreeToPrivicyPolicy=checkBoxPrivacyPolicy.isChecked
            }
            R.id.btNext->{
                if (agreeToPrivicyPolicy){

                    val personAppearance =PersonAppearance(mHeight.toString(),mWeight.toString(),mCM_MODE)
                    val person=Person(personAppearance)
                    MY_Shared_PREF.savePersonAppearance(person,this@ScaleActivity)
                    startActivity(Intent(this@ScaleActivity,MainActivity::class.java))

                }else{
                    Toast.makeText(this@ScaleActivity,"Agree to privacy policy ",Toast.LENGTH_LONG).show()
                }
            }

        }
    }
    lateinit var checkBoxPrivacyPolicy: AppCompatCheckBox
    lateinit var radioCentimenter: AppCompatRadioButton
    lateinit var scaleWheelViewHeight: ScaleRulerView
    lateinit var scaleWheelWaight: ScaleRulerView
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

        scaleWheelViewHeight = findViewById(R.id.scaleWheelView_height)
        checkBoxPrivacyPolicy=findViewById(R.id.PrivacyPolicyCheckBox)
        scaleWheelWaight = findViewById(R.id.scaleWheelView_weight)
        radioCentimenter = findViewById(R.id.radioCentimenter)
        tvHight = findViewById(R.id.tv_user_height_value)
        tvWaight = findViewById(R.id.tv_user_weight_value)
        tvWaightType = findViewById(R.id.tv_waight_type)
        tvHightType = findViewById(R.id.tv_hight_type)
        radioInch = findViewById(R.id.radioInch)
        btNext=findViewById(R.id.btNext)


        tvHight.text = mHeight.toString()

        scaleWheelViewHeight.initViewParam(mHeight, mMaxHeight, mMinHeight)
        scaleWheelViewHeight.setValueChangeListener({
            mHeight = it
            tvHight.text=mHeight.toString()



        })

        scaleWheelWaight.initViewParam(mWeight, mMaxWeight, mMinWeight)
        scaleWheelWaight.setValueChangeListener({
            mWeight = it
            tvWaight.text = it.toString()
        })

        checkBoxPrivacyPolicy.setOnClickListener(this)
        radioCentimenter.setOnClickListener(this)
        radioInch.setOnClickListener(this)
        btNext.setOnClickListener(this)


    }


}
