package com.yourdomain.project50.ViewModle

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.google.firebase.database.*
import com.yourdomain.project50.Model.Admob
import com.yourdomain.project50.Model.AppAdmobSettingsFromFirebase


/**
 * Created by hamza rafiq on 11/26/18.
 */
class GetAdmobDataFromFireBaseViewModle : ViewModel() {
    private var mutableLiveSettings: MutableLiveData<AppAdmobSettingsFromFirebase>? = null
    var database = FirebaseDatabase.getInstance()
    private val TAG="GetDataFromFireBase";

    public fun getAppSettingFromFireBase(): LiveData<AppAdmobSettingsFromFirebase>? {
        if (mutableLiveSettings == null) {
            mutableLiveSettings = MutableLiveData();

            val myRef: DatabaseReference = database.getReference("ads").child("admob")
            myRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
mutableLiveSettings?.value=null
                }

                override fun onDataChange(p0: DataSnapshot) {
                   val appAdmobDataFromFirebase=AppAdmobSettingsFromFirebase(p0.getValue(Admob::class.java))
                    mutableLiveSettings?.value=appAdmobDataFromFirebase
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
        return mutableLiveSettings
    }
}