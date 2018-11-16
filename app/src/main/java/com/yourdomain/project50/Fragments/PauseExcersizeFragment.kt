package com.yourdomain.project50.Fragments

import android.app.Dialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

import com.yourdomain.project50.R

class PauseExcersizeFragment : DialogFragment() {

    private var mParamExcesizeTilte: String? = null
    private var mParamExcersizeSeconds: String? = null
    private var mParamGif:Int?=null
    private var mListener: OnResumeListener? = null

    private lateinit var btContinue:ImageButton
    private lateinit var tvTitle:TextView
    private lateinit var tvSeconds:TextView
    private lateinit var gif:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParamExcesizeTilte = arguments!!.getString(ARG_EXCERSIXE_TITLE)
            mParamExcersizeSeconds = arguments!!.getString(ARG_EXCERSIZE_SECONDS)
            mParamGif=arguments!!.getInt(ARG_EXCERSIZE_PAUSE_GIF)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       val view= inflater.inflate(R.layout.fragment_pause_excersize, container, false)

        btContinue=view.findViewById(R.id.btResume);
        tvSeconds=view.findViewById(R.id.tvSeonds)
        tvTitle=view.findViewById(R.id.tvtitle)
        gif=view.findViewById(R.id.icon)

        tvTitle.text=mParamExcesizeTilte
        tvSeconds.text=mParamExcersizeSeconds
        Glide.with(this).asGif().load(mParamGif).into(gif)
        btContinue.setOnClickListener {
            mListener?.ResumeListener()
            dismiss()

        }
        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnResumeListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    interface OnResumeListener {
        fun ResumeListener()
    }

    companion object {
       
        private val ARG_EXCERSIXE_TITLE = "ARG_EXCERSIXE_TITLE"
        private val ARG_EXCERSIZE_SECONDS = "ARG_EXCERSIZE_SECONDS"
        private val ARG_EXCERSIZE_PAUSE_GIF="ARG_EXCERSIZE_PAUSE_GIF"

     
        fun newInstance(title: String, seconds: String,gif :Int): PauseExcersizeFragment {
            val fragment = PauseExcersizeFragment()
            val args = Bundle()
            args.putString(ARG_EXCERSIXE_TITLE, title)
            args.putString(ARG_EXCERSIZE_SECONDS, seconds)
            args.putInt(ARG_EXCERSIZE_PAUSE_GIF,gif)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
