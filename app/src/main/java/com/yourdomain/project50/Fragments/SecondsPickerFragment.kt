package com.yourdomain.project50.Fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.yourdomain.project50.R
import java.lang.Exception


open class SecondsPickerFragment : DialogFragment() {

    private var mListener: OnSecondsPickerListener? = null
    private var mParamSeconds: Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParamSeconds = arguments!!.getInt(ARG_SECONDS,30)

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
      var view=  inflater.inflate(R.layout.fragment_seconds_picker, container, false)
        val tvSeconds=view.findViewById<TextView>(R.id.tvSeconds)
        val btAddSeconds=view.findViewById<TextView>(R.id.tvAddSeconds)
        val btMinSeconds=view.findViewById<TextView>(R.id.tvMinSeconds)
        val btSave=view.findViewById<TextView>(R.id.btSave)

        tvSeconds.text=mParamSeconds.toString()

        btAddSeconds.setOnClickListener {
            var seconds = tvSeconds.text.toString().toInt()
            seconds++
            tvSeconds.text=seconds.toString()
        }
        btMinSeconds.setOnClickListener {
            var seconds = tvSeconds.text.toString().toInt()
            seconds--
            tvSeconds.text=seconds.toString()
        }
        btSave.setOnClickListener { mListener?.onPicker(tvSeconds.text.toString().toInt())
        try {
            dismiss()
        }catch (E:Exception){}}
        return view;
    }



    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnSecondsPickerListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }
    companion object {

        private val ARG_SECONDS = "ARG_SECONDS"
        fun newInstance(seconds: Int): Fragment {
            val fragment = SecondsPickerFragment()
            val args = Bundle()
            args.putInt(ARG_SECONDS, seconds)

            fragment.arguments = args
            return fragment
        }
    }

   open interface OnSecondsPickerListener {
        // TODO: Update argument type and name
        fun onPicker(seconds:Int)
    }
}// Required empty public constructor
