package com.yourdomain.project50.ViewModle

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.yourdomain.project50.Model.Excersize
import com.yourdomain.project50.Model.ExcersizeDays
import java.util.*

/**
 * Created by apple on 11/13/18.
 */
class ExcersizeDayGernaterViewModle : ViewModel() {
    private var mutableList: java.util.ArrayList<ExcersizeDays>? = null
    private var mutableLiveData : MutableLiveData<ArrayList<ExcersizeDays>?>?=null

    fun getDays(): MutableLiveData<ArrayList<ExcersizeDays>?>? {
        if (mutableLiveData == null) {
            mutableLiveData=MutableLiveData()
            mutableList = ArrayList();

            for (day in 1..30) {

                var excersizeDays= ExcersizeDays(day,1)
                mutableList?.add(excersizeDays)
            }
            mutableLiveData?.value=mutableList
        }
        return mutableLiveData
    }


}