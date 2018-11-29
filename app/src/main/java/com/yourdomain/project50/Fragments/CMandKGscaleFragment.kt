package com.yourdomain.project50.Fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.AppCompatCheckBox
import android.support.v7.widget.AppCompatRadioButton
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.yourdomain.project50.Activitys.MainActivity
import com.yourdomain.project50.R
import com.yourdomain.project50.Scale.view.DecimalScaleRulerView
import com.yourdomain.project50.Scale.view.ScaleRulerView


class CMandKGscaleFragment : Fragment(), View.OnClickListener {


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.radioCentimenter -> {

            }
            R.id.radioInch -> {
                mListener?.onINCandLBSRadioClick()
            }
            R.id.PrivacyPolicyCheckBox -> {
                agreeToPrivicyPolicy = checkBoxPrivacyPolicy.isChecked
            }
            R.id.btNext -> {
                if (agreeToPrivicyPolicy) {
                    val intent = Intent(activity,MainActivity::class.java)
                    activity?.startActivity(intent)
                    mListener?.onCMandKGNext()
                } else {
                    Toast.makeText(activity, "Agree to privacy policy ", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private var mParam1: String? = null
    private var mParam2: String? = null

    private var mListener: OnINCandLBSRadioListener? = null


    private lateinit var checkBoxPrivacyPolicy: AppCompatCheckBox
    private lateinit var radioCentimenter: AppCompatRadioButton
    private lateinit var mHeightWheelView: ScaleRulerView
    private lateinit var mWeightWheelView: ScaleRulerView
    private lateinit var radioInch: AppCompatRadioButton
    private lateinit var btNext: Button

    private lateinit var tvHight: TextView
    private lateinit var tvWaight: TextView
    private lateinit var tvHightType: TextView
    private lateinit var tvWaightType: TextView

    private var mHeight = 170f
    private val mMaxHeight = 220f
    private val mMinHeight = 100f


    private var mWeight = 60.0f
    private val mMaxWeight = 200f
    private val mMinWeight = 25f

    private var mCM_MODE = 1
    private var agreeToPrivicyPolicy = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments!!.getString(ARG_PARAM1)
            mParam2 = arguments!!.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val rootView = inflater.inflate(R.layout.fragment_cmand_kgscale, container, false)

        mHeightWheelView = rootView.findViewById(R.id.scaleWheelView_height)
        checkBoxPrivacyPolicy = rootView.findViewById(R.id.PrivacyPolicyCheckBox)
        mWeightWheelView = rootView.findViewById(R.id.scaleWheelView_weight)
        radioCentimenter = rootView.findViewById(R.id.radioCentimenter)
        tvHight = rootView.findViewById(R.id.tv_user_height_value)
        tvWaight = rootView.findViewById(R.id.tv_user_weight_value)
        tvWaightType = rootView.findViewById(R.id.tv_waight_type)
        tvHightType = rootView.findViewById(R.id.tv_hight_type)
        radioInch = rootView.findViewById(R.id.radioInch)
        btNext = rootView.findViewById(R.id.btNext)

        radioCentimenter.isChecked=true
        radioCentimenter.isEnabled=false

        btNext.setOnClickListener(this)
        radioInch.setOnClickListener(this)
        radioCentimenter.setOnClickListener(this)
        checkBoxPrivacyPolicy.setOnClickListener(this)

        tvHight.text = mHeight.toString()
        mHeightWheelView.initViewParam(mHeight, mMaxHeight, mMinHeight)
        mHeightWheelView.setValueChangeListener({
            mHeight = it
            tvHight.text=mHeight.toString()
        })
        mWeightWheelView.initViewParam(mWeight, mMaxWeight, mMinWeight)
        mWeightWheelView.setValueChangeListener({
            mWeight = it
            tvWaight.text = it.toString()
        })

        return rootView;
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnINCandLBSRadioListener) {
            mListener = context
        } else {
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }


    interface OnINCandLBSRadioListener {

        fun onINCandLBSRadioClick()
        fun onCMandKGNext();

    }

    companion object {

        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"


        fun newInstance(param1: String, param2: String): CMandKGscaleFragment {
            val fragment = CMandKGscaleFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
