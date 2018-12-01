package com.yourdomain.project50.ViewModle

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*

import com.yourdomain.project50.Model.MoreApps
import java.util.ArrayList

/**
 * Created by apple on 11/29/18.
 */
class CustomAdsViewModle : ViewModel(){
    private var mutableLiveData:MutableLiveData<ArrayList<MoreApps>>?=null
    private val moreAppList=ArrayList<MoreApps>()
    private lateinit var database: DatabaseReference


    public fun getMoreApps():LiveData<ArrayList<MoreApps>>?{
       if (mutableLiveData==null){
           mutableLiveData=MutableLiveData()
           database = FirebaseDatabase.getInstance().reference.child("ads").child("moreApps")
           database.addListenerForSingleValueEvent(object :ValueEventListener{
               override fun onDataChange(p0: DataSnapshot) {
                   p0.children.forEach { it.getValue(MoreApps::class.java)?.let { moreAppList.add(it) }

                   }
                   mutableLiveData?.value=moreAppList
               }

               override fun onCancelled(p0: DatabaseError) {

               }

           })
       }
        return mutableLiveData
    }
}