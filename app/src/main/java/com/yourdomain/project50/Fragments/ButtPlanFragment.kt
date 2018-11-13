package com.yourdomain.project50.Fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.yourdomain.project50.R


/**
 * A simple [Fragment] subclass.
 */
class ButtPlanFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_butt_plan, container, false)
    }

}// Required empty public constructor
