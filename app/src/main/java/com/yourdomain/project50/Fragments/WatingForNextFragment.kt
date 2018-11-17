package com.yourdomain.project50.Fragments

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.bumptech.glide.Glide

import com.yourdomain.project50.R


class WatingForNextFragment : DialogFragment() {

    private val TAG="WatingForNextFragment"
    private var mParamTitle: String? = null
    private var mParamSeconds: String? = null
    private var mParamDoneExcersizes: String? = null
    private var mParamDrawble:Int=-1

    private lateinit var tvTitle: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvProgress:TextView
    private lateinit var tvDoneExcersize: TextView
    private lateinit var tvSeconds: TextView
    private lateinit var btSkip:TextView
    private lateinit var icon: ImageView
    private var mListener: OnNextExcersizeDemoFragmentListener? = null
    private var countDownTimer: CountDownTimer = object : CountDownTimer(30 * 1000, 1000) {

        override fun onTick(millisUntilFinished: Long) {
            progressBar.progress = (millisUntilFinished / 1000).toInt()
            tvProgress.text=(millisUntilFinished / 1000).toString()
        }

        override fun onFinish() {
            mListener?.onSkip()
            dismiss()

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParamTitle = arguments!!.getString(ARG_PARAM_TITLE)
            mParamSeconds = arguments!!.getString(ARG_PARAM_SECONDES)
            mParamDoneExcersizes = arguments!!.getString(ARG_PARAM_DONE_EXCERSIZE)
            mParamDrawble=arguments!!.getInt(ARG_PARAM_ICON)

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
       val view= inflater.inflate(R.layout.fragment_wating_for_next_excersize, container, false)

        tvTitle=view.findViewById(R.id.title)
        tvDoneExcersize=view.findViewById(R.id.tvDoneExcersize)
        progressBar=view.findViewById(R.id.progressBar)
        tvProgress=view.findViewById(R.id.tvProgress)
        tvSeconds=view.findViewById(R.id.seconds)
        btSkip=view.findViewById(R.id.btSkip)
        icon=view.findViewById(R.id.icon)

        progressBar.max=30
        tvProgress.text="30"
        tvTitle.text=mParamTitle
        tvDoneExcersize.text=mParamDoneExcersizes
        tvSeconds.text=mParamSeconds
        btSkip.setOnClickListener { mListener?.onSkip() ;dismiss() }
        Glide.with(this).asGif().load(mParamDrawble).into(icon)

        countDownTimer.start()
        return view;
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnNextExcersizeDemoFragmentListener) {
            mListener = context
        } else {
        }
    }

    override fun onDetach() {
        countDownTimer?.cancel()
        super.onDetach()
        mListener = null
    }

       interface OnNextExcersizeDemoFragmentListener {
        fun onSkip()
    }

    companion object {

        private val ARG_PARAM_TITLE = "ARG_PARAM_TITLE"
        private val ARG_PARAM_SECONDES = "ARG_PARAM_SECONDES"
        private val ARG_PARAM_DONE_EXCERSIZE = "ARG_PARAM_DONE_EXCERSIZE";
        private var ARG_PARAM_ICON="ARG_PARAM_ICON"

                fun newInstance(title: String, seconds: String, doneExcersizes: String,drawble:Int): WatingForNextFragment {
            val fragment = WatingForNextFragment()
            val args = Bundle()
            args.putString(ARG_PARAM_TITLE, title)
            args.putString(ARG_PARAM_SECONDES, seconds)
            args.putString(ARG_PARAM_DONE_EXCERSIZE, doneExcersizes)
                    args.putInt(ARG_PARAM_ICON,drawble)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
