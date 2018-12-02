package com.yourdomain.project50.ViewModle

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.yourdomain.project50.Model.AppSettingsFromFireBase

/**
 * Created by hamza rafiq on 12/1/18.
 */
class AppSettingsFromFirebaseViewModle:ViewModel(){
   private var database = FirebaseDatabase.getInstance()

    private var mutableLiveData:MutableLiveData<AppSettingsFromFireBase>?=null
    public fun getAppSettingFromFirabs():LiveData<AppSettingsFromFireBase>?{
        if (mutableLiveData==null){
            mutableLiveData= MutableLiveData()

            database.getReference("AppSettingsFromFireBase").addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                    mutableLiveData?.value=null
                }

                override fun onDataChange(p0: DataSnapshot) {
                    mutableLiveData?.value=p0.getValue(AppSettingsFromFireBase::class.java)

                }

            })
        }
        return mutableLiveData
    }
}