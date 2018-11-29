package com.yourdomain.project50.ViewModle

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.google.firebase.database.*
import com.yourdomain.project50.Model.Admob
import com.yourdomain.project50.Model.AdmobAds
import com.yourdomain.project50.Model.AppAdmobDataFromFirebase


/**
 * Created by hamza rafiq on 11/26/18.
 */
class GetDataFromFireBaseViewModle : ViewModel() {
    private var mutableLiveData: MutableLiveData<AppAdmobDataFromFirebase>? = null
    var database = FirebaseDatabase.getInstance()
    private val TAG="GetDataFromFireBase";

    public fun getAppSettingFromFireBase(): LiveData<AppAdmobDataFromFirebase>? {
        if (mutableLiveData == null) {
            mutableLiveData = MutableLiveData();

            val myRef: DatabaseReference = database.getReference("ads").child("admob")
            myRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    Log.d(TAG,""+p0.getValue(Admob::class.java)?.interstitialAds?.id)
                   val appAdmobDataFromFirebase=AppAdmobDataFromFirebase(p0.getValue(Admob::class.java))
                    mutableLiveData?.value=appAdmobDataFromFirebase
//                    p0.children.forEach {
//                        Log.d(TAG,"each key "+it.key)
//                       when(it.key){
//                           "appId"->{
//
//                           }
//                           "bannerAds"->{
//
//                           }
//                           "interstitialAds"->{
//
//
//                           }
//                           "publisherId"->{
//
//                           }
//                       }
//                    }

                }

            })

        }
        return mutableLiveData
    }
}