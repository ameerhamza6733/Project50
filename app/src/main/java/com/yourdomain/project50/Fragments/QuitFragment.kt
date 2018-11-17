package com.yourdomain.project50.Fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

import com.yourdomain.project50.R
import org.w3c.dom.Text


class QuitFragment : DialogFragment() {

    private var mListener: OnQuitListener? = null

    private lateinit var backGoudidImage:ImageView
    private lateinit var btQuit:Button
    private lateinit var btClose:TextView
    private lateinit var btContinue:TextView
    private lateinit var btComeBackLater:TextView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
       val view= inflater.inflate(R.layout.fragment_quit, container, false)
        backGoudidImage=view.findViewById(R.id.backGroundImage)
        btQuit=view.findViewById(R.id.btQuit)
        btClose=view.findViewById(R.id.btClose)
        btContinue=view.findViewById(R.id.btContinue)
        btComeBackLater=view.findViewById(R.id.btComeBackLater)

        Glide.with(this).asBitmap().load(R.drawable.spalish2).apply(RequestOptions().override(400,450)).into(backGoudidImage)
        btContinue.setOnClickListener {dismiss()
            mListener?.onContinue() }
        btClose.setOnClickListener {
            dismiss()
            mListener?.onContinue() }
        btComeBackLater.setOnClickListener { mListener?.onComeBacKLater() }
        btQuit.setOnClickListener { mListener?.onQuit() }
        return view
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnQuitListener) {
            mListener = context
        } else {
        }
    }

    override fun onDetach() {

        super.onDetach()
        mListener = null
    }

       interface OnQuitListener {
        fun onQuit()
         fun onContinue();
           fun onComeBacKLater()
    }
}// Required empty public constructor
