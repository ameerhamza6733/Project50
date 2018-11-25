package com.yourdomain.project50.Fragments

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.yourdomain.project50.R
import android.support.v4.content.LocalBroadcastManager
import android.content.Intent
import com.yourdomain.project50.TTSHelperService


class WatingToStartExcersizeFragment : DialogFragment() {


    private var mParamALlExcersizeTotalTime: String? = null
    private var mParamThisExcersizeTotalTime: String = ""
    private var mParamDiscription: String? = null
    private var mListener: OnFragmentInteractionListener? = null
    private var watingTime:Int=30

    private lateinit var mtvDiscription: TextView
    private lateinit var mThisExsersizeTotalTime: TextView
    private lateinit var mExsersizseTotalTime: TextView
    private lateinit var mSkipButton: TextView
    private lateinit var mbtBack:TextView
    private var countDownTimer: CountDownTimer? =null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParamALlExcersizeTotalTime = arguments!!.getString(mParamALlExcersizeTotalTime_KEY)
            mParamThisExcersizeTotalTime = arguments!!.getString(mParamThisExcersizeTotalTime_KEY)
            mParamDiscription = arguments!!.getString(mParamDiscription_KEY)
            watingTime=arguments!!.getInt(mParamWatingTime,30)


        }
    }
    private fun sendTTSBroadCast(text: String) {

        val intent = Intent(TTSHelperService.ACTION_TTS)
        intent.putExtra("TTStext", text)

        LocalBroadcastManager.getInstance(activity?.applicationContext!!).sendBroadcast(intent)

    }


    private lateinit var mPrograssBar: ProgressBar


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_wating, container, false)
        mExsersizseTotalTime = view.findViewById(R.id.tvTotalTime)
        mThisExsersizeTotalTime = view.findViewById(R.id.tvThisTotalTimeForNextExcersize)
        mSkipButton = view.findViewById(R.id.btSkip)
        mbtBack=view.findViewById(R.id.btBack)
        mtvDiscription = view.findViewById(R.id.tvDiscription)
        mPrograssBar = view.findViewById<ProgressBar>(R.id.progressBar)

        updateUI()
      countDownTimer=  object : CountDownTimer(watingTime.toLong() * 1000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                var sconds_= (millisUntilFinished / 1000).toInt()
                mPrograssBar.progress = sconds_
                if (sconds_<4){
                    sendTTSBroadCast(sconds_.toString())
                }
            }

            override fun onFinish() {
                mListener?.onCountDownDonw()
                try {
                    dismiss()
                }catch (e:Exception){
                    e.printStackTrace()
                }

            }
        }
        countDownTimer?.start()
        mSkipButton.setOnClickListener { countDownTimer?.onFinish() }
        mbtBack.setOnClickListener { activity?.finish() }

        return view
    }



    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
//            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    fun updateUI() {
        mtvDiscription.text = mParamDiscription
        mExsersizseTotalTime.text = mParamALlExcersizeTotalTime.toString()
        mThisExsersizeTotalTime.text = mParamThisExcersizeTotalTime.toString()
    }

    override fun onDetach() {
        countDownTimer?.cancel()
        super.onDetach()
        mListener = null
    }

    interface OnFragmentInteractionListener {
        fun onCountDownDonw()
    }

    companion object {
        private val mParamALlExcersizeTotalTime_KEY = "mParamALlExcersizeTotalTime"
        private val mParamThisExcersizeTotalTime_KEY = "mParamThisExcersizeTotalTime"
        private val mParamDiscription_KEY = "mParamDiscription_KEY";
        private val mParamWatingTime="mParamWatingTime"


        fun newInstance(totaleTime: String, totaleTimeForThis: String, discription: String,watingTime:Int): WatingToStartExcersizeFragment {
            val fragment = WatingToStartExcersizeFragment()
            val args = Bundle()
            args.putString(mParamALlExcersizeTotalTime_KEY, totaleTime)
            args.putString(mParamThisExcersizeTotalTime_KEY, totaleTimeForThis)
            args.putString(mParamDiscription_KEY, discription)
            args.putInt(mParamWatingTime,watingTime)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
