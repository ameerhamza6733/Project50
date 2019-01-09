package com.yourdomain.project50.Fragments

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.yourdomain.project50.R
import com.yourdomain.project50.Utils
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.request.RequestOptions



/**
 * Created by apple on 11/26/18.
 */

class RateUsFragment : DialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.rate_us, container, false)
        val rateImage = view.findViewById<ImageView>(R.id.imageRate)
        val btRateUs = view.findViewById<TextView>(R.id.btRateUs)
        val btRateClose = view.findViewById<TextView>(R.id.btRateClose)
        var requestOptions = RequestOptions()

        Glide.with(this).asBitmap().load(R.drawable.rate_us).into(rateImage)
        btRateUs.setOnClickListener {
            Utils.openGooglePlay(activity?.application, activity?.packageName)
        }
        btRateClose.setOnClickListener {
           if (activity?.javaClass?.simpleName?.contains("EachPlanActivity")==true){
               activity?.finish()
           }else {
               try {
                   dismiss()
               } catch (E: Exception) {

               }
           }
        }
        return view
    }
}
