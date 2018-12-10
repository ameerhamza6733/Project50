package com.yourdomain.project50.Fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.yourdomain.project50.MY_Shared_PREF
import com.yourdomain.project50.Model.Person
import com.yourdomain.project50.Model.PersonAppearance
import com.yourdomain.project50.R
import java.util.*


class EditBMIDialogeFragment : DialogFragment() {

    private var mListener: OnBMIupdateListener? = null
    private var person: Person? = null
    private val TAG = "EditBMIDialogeFragment"

    private lateinit var editTextWaight: EditText
    private lateinit var editTextHightFT: EditText
    private lateinit var editTextHightIN: EditText
    private lateinit var tvHightUnitTYpe: TextView
    private lateinit var tvWaightUnitType: TextView
    private lateinit var editTextHightInCm: EditText
    private lateinit var btSave: TextView
    private lateinit var btCancel: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        person = MY_Shared_PREF.getPerson(activity?.application!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_edit_bmidialoge, container, false)

        editTextHightFT = rootView.findViewById(R.id.editTextHightFT)
        editTextHightIN = rootView.findViewById(R.id.editTextHightIN)
        editTextWaight = rootView.findViewById(R.id.editTextWaight)
        tvHightUnitTYpe = rootView.findViewById(R.id.tvHightUnitType)
        tvWaightUnitType = rootView.findViewById(R.id.tvLableWaightUnitType)
        editTextHightInCm = rootView.findViewById(R.id.editTextHightCm)
        btCancel = rootView.findViewById(R.id.btCancel)
        btSave = rootView.findViewById(R.id.btSave)

        when (person?.personAppearance?.SCALE_TYPE) {
            PersonAppearance.TYPE_CM_KG -> {
                tvWaightUnitType.setText("KG")
                tvHightUnitTYpe.setText("CM")
                editTextHightInCm.visibility = View.VISIBLE
                editTextHightIN.visibility = View.INVISIBLE
                editTextHightFT.visibility = View.INVISIBLE
                editTextHightInCm.setText(person?.personAppearance?.mHight.toString(), TextView.BufferType.EDITABLE)
            }
            PersonAppearance.TYPE_IN_LBS -> {
                tvHightUnitTYpe.setText("IN")
                tvWaightUnitType.setText("LB")
                editTextHightInCm.visibility = View.INVISIBLE
                editTextHightIN.visibility = View.VISIBLE
                editTextHightFT.visibility = View.VISIBLE
                var inchAndFeet = person?.personAppearance?.mHight?.toString()?.split(".")
                if (inchAndFeet?.size!! > 1) {
                    editTextHightIN.setText(inchAndFeet[1].substring(0,1)+"");
                }
                editTextHightFT.setText(inchAndFeet[0]+"")


            }

        }


        editTextWaight.setText(person?.personAppearance?.mWaight.toString(), TextView.BufferType.EDITABLE);

        editTextHightIN.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {
               if(!s.isNullOrEmpty()){
                   val number = s.toString().toInt()

                   if (number > 12){
                       editTextHightIN.setError("Please choose number between 1 to 12")
                   }
               }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.d(TAG,"before text chnaged "+s);
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d(TAG,"on text changed "+s);
            }

        })
        btSave.setOnClickListener {
            var hight = ""
            val waight = editTextWaight.text.toString()
            when (person?.personAppearance?.SCALE_TYPE) {
                PersonAppearance.TYPE_IN_LBS -> {
                    val hightInInch = editTextHightIN.text.toString().toInt()
                    if (hightInInch>12)
                    {
                        editTextHightIN.setError("Please choose number between 1 to 12")
                    }else {
                        hight = editTextHightFT.text.toString().removePrefix("F") + "." + editTextHightIN.text.toString().removePrefix("IN")
                    }
                }
                PersonAppearance.TYPE_CM_KG -> {
                    hight = editTextHightInCm.text.toString()

                }
            }
            person?.personAppearance?.mHight = hight.toDouble()
            person?.personAppearance?.mWaight = waight.toDouble()
            val personAppearance = PersonAppearance(person?.personAppearance?.SCALE_TYPE!!, hight.toDouble(), waight.toDouble(),Date())
            activity?.application?.let { MY_Shared_PREF.savePerson(it, person!!) }
            activity?.application?.let { MY_Shared_PREF.savePersonAppearanceHistory(it, personAppearance) }
            mListener?.onFragmentInteraction(person)
            try {
                dismiss()
            } catch (E: Exception) {

            }
        }
        btCancel.setOnClickListener {
            try {
                dismiss()
            } catch (E: Exception) {

            }
        }
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
        fun onFragmentInteraction(person: Person?)
    }
}// Required empty public constructor
