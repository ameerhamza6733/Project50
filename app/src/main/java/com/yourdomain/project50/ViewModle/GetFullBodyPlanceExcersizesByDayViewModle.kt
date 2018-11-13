package com.yourdomain.project50.ViewModle

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.yourdomain.project50.ExcersizesConstants
import com.yourdomain.project50.Model.Excesizes

/**
 * Created by apple on 11/13/18.
 */

class GetFullBodyPlanceExcersizesByDayViewModle : ViewModel() {


    private var mutableLiveData: MutableLiveData<Excesizes>? = null

    fun getExcersizs(day: Int): LiveData<Excesizes>? {
        if (mutableLiveData == null) {
            mutableLiveData = MutableLiveData()
            val ids = ExcersizesConstants.getDay1ExcersizeKeys(day + 1)
            mutableLiveData?.value = ids
        }
        return mutableLiveData
    }


}
