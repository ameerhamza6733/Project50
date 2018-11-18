package com.yourdomain.project50.ViewModle

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.yourdomain.project50.Model.MoreApps
import java.util.ArrayList

/**
 * Created by hamza rafiq  on 11/18/18.
 */

class MoreAppViewModle :ViewModel(){
    private var mutableLiveData:MutableLiveData<ArrayList<MoreApps>>?=null
    private var arrayList:ArrayList<MoreApps>?=null;

    public fun getApps():LiveData<ArrayList<MoreApps>>{
        if (mutableLiveData==null){
            mutableLiveData= MutableLiveData();
            arrayList= ArrayList();

            arrayList?.add(MoreApps("Kick the Buddy","https://play.google.com/store/apps/details?id=com.playgendary.kickthebuddy","https://apkmody.co/wp-content/uploads/2018/07/cover-kick-the-buddy.jpg"))
            mutableLiveData?.value=arrayList
        }
        return mutableLiveData!!
    }
}
