package com.yourdomain.project50.Fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.yourdomain.project50.MY_Shared_PREF
import com.yourdomain.project50.Model.Person
import com.yourdomain.project50.Model.PersonAppearance
import com.yourdomain.project50.R
import com.yourdomain.project50.Utils


class EditBMIDialogeFragment : DialogFragment() {

    private var mListener: OnBMIupdateListener? = null
    private var person:Person?=null
    private val TAG="EditBMIDialogeFragment"

    private lateinit var editTextWaight:EditText
    private lateinit var editTextHightFT: EditText
    private lateinit var editTextHightIN:EditText
    private lateinit var tvHightUnitTYpe:TextView
    private lateinit var tvWaightUnitType:TextView
    private lateinit var editTextHightInCm:EditText
    private lateinit var btSave:TextView
    private lateinit var btCancel:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        person=MY_Shared_PREF.getPerson(activity?.application!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_edit_bmidialoge, container, false)

        editTextHightFT=rootView.findViewById(R.id.editTextHightFT)
        editTextHightIN=rootView.findViewById(R.id.editTextHightIN)
        editTextWaight=rootView.findViewById(R.id.editTextWaight)
        tvHightUnitTYpe=rootView.findViewById(R.id.tvHightUnitType)
        tvWaightUnitType=rootView.findViewById(R.id.tvLableWaightUnitType)
        editTextHightInCm=rootView.findViewById(R.id.editTextHightCm)
        btCancel=rootView.findViewById(R.id.btCancel)
        btSave=rootView.findViewById(R.id.btSave)

        when(person?.personAppearance?.SCALE_TYPE){
            PersonAppearance.TYPE_CM_KG->{
                tvWaightUnitType.setText("KG")
                tvHightUnitTYpe.setText("CM")
                editTextHightInCm.visibility=View.VISIBLE
                editTextHightIN.visibility=View.INVISIBLE
                editTextHightFT.visibility=View.INVISIBLE
                editTextHightInCm.setText(person?.personAppearance?.mHight,TextView.BufferType.EDITABLE)
            }
            PersonAppearance.TYPE_IN_LBS->{
                tvHightUnitTYpe.setText("IN")
                tvWaightUnitType.setText("LB")
                editTextHightInCm.visibility=View.INVISIBLE
                editTextHightIN.visibility=View.VISIBLE
                editTextHightFT.visibility=View.VISIBLE
                var inchAndFeet =person?.personAppearance?.mHight?.split(".")
               if(inchAndFeet?.size!! >1){
                   editTextHightIN.setText(inchAndFeet[1]);
               }
                editTextHightFT.setText(inchAndFeet[0])

            }
        }
        editTextWaight.setText(person?.personAppearance?.mWaight , TextView.BufferType.EDITABLE);
        Log.d(TAG,"person  : "+person?.personAppearance?.SCALE_TYPE +" person hight : "+person?.personAppearance?.mHight)

        return rootView
    }




    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnBMIupdateListener) {
            mListener = context
        } else {
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    interface OnBMIupdateListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(person: Person)
    }
}// Required empty public constructor
