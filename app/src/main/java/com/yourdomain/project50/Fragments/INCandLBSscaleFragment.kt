package com.yourdomain.project50.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yourdomain.project50.Model.PersonAppearance
import com.yourdomain.project50.Utils

/**
 * Created by apple on 12/1/18.
 */

class INCandLBSscaleFragment : CMandKGscaleFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)


        tvWaightType.text = "LBS"
        tvHightType.text = "IN"
        radioInch.isChecked = true
        mHeightWheelView.initViewParam(mHeight, mMaxHeight, mMinHeight)
        mHeightWheelView.setValueChangeListener({

            mHeight = Utils.CMtoFeet(it.toDouble()).toFloat()
            tvHight.text = mHeight.toString()
        })
        mWeightWheelView.initViewParam(mWeight, mMaxWeight, mMinWeight)
        mWeightWheelView.setValueChangeListener({
            mWeight = Utils.KGtoLBS(it.toDouble()).toFloat()
            tvWaight.text = mWeight.toString()
        })
        radioCentimenter.setOnClickListener {
            mListener?.onCMandKGadioClick()
        }
        tvHight.text= String.format("%.1f",Utils.CMtoFeet(mHeight.toDouble()).toDouble())
        tvWaight.text=Utils.KGtoLBS(mWeight.toDouble()).toString()
        mScaleType= PersonAppearance.TYPE_IN_LBS
        return view
    }


}
