package com.yourdomain.project50.Fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import com.yourdomain.project50.Model.PersonAppearance
import com.yourdomain.project50.R
import com.yourdomain.project50.Scale.view.ScaleRulerView
import com.yourdomain.project50.Utils
import java.util.*


open class CMandKGscaleFragment : Fragment(), View.OnClickListener {


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.radioInch -> {
                mListener?.onINCandLBSRadioClick()
            }
            R.id.PrivacyPolicyCheckBox -> {
                agreeToPrivicyPolicy = checkBoxPrivacyPolicy.isChecked
            }
            R.id.btNext -> {
                if (agreeToPrivicyPolicy) {

                    val personAppearance=PersonAppearance(mScaleType,tvHight.text.toString().toDouble(),tvWaight.text.toString().toDouble(), Date())
                    mListener?.onNext(personAppearance)
                } else {
                    Toast.makeText(activity, "Agree to privacy policy ", Toast.LENGTH_LONG).show()
                }
            }
            R.id.tvAgreeToPrivacyPolicy->{
                var url=getString(R.string.privacy_policy_url)
                if (!url.startsWith("https://") && !url.startsWith("http://")) {
                    url = "http://$url"
                }
                val openUrlIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(openUrlIntent)
            }
        }
    }

    protected var mParam1: String? = null
    protected var mParam2: String? = null

    protected var mListener: OnINCandLBSRadioListener? = null


    protected lateinit var checkBoxPrivacyPolicy: AppCompatCheckBox
    protected lateinit var radioCentimenter: AppCompatRadioButton
    protected lateinit var mHeightWheelView: ScaleRulerView
    protected lateinit var mWeightWheelView: ScaleRulerView
    protected lateinit var radioInch: AppCompatRadioButton
    protected lateinit var btNext: Button
    protected lateinit var tvAgreToPolicyPrivacy:TextView

    protected lateinit var tvHight: TextView
    protected lateinit var tvWaight: TextView
    protected lateinit var tvHightType: TextView
    protected lateinit var tvWaightType: TextView

    protected var mHeight = 170f
    protected val mMaxHeight = 220f
    protected val mMinHeight = 100f


    protected var mWeight = 60.0f
    protected val mMaxWeight = 200f
    protected val mMinWeight = 25f

    protected var mScaleType = -1
    protected var agreeToPrivicyPolicy = true

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
        tvAgreToPolicyPrivacy=rootView.findViewById(R.id.tvAgreeToPrivacyPolicy)
        tvHight = rootView.findViewById(R.id.tv_user_height_value)
        tvWaight = rootView.findViewById(R.id.tv_user_weight_value)
        tvWaightType = rootView.findViewById(R.id.tv_waight_type)
        tvHightType = rootView.findViewById(R.id.tv_hight_type)
        radioInch = rootView.findViewById(R.id.radioInch)
        btNext = rootView.findViewById(R.id.btNext)

        radioCentimenter.isChecked=true
        checkBoxPrivacyPolicy.isChecked=true


        btNext.setOnClickListener(this)
        radioInch.setOnClickListener(this)
        checkBoxPrivacyPolicy.setOnClickListener(this)
        tvAgreToPolicyPrivacy.setOnClickListener(this)

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
mScaleType=PersonAppearance.TYPE_CM_KG
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
        fun onCMandKGadioClick()
        fun onNext( personAppearance: PersonAppearance);
        fun onINCandLBSRadioClick()


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
