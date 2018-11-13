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
                var list = getExcersizes()
                var excersizeDays= ExcersizeDays(day,list,1)
                mutableList?.add(excersizeDays)
            }
            mutableLiveData?.value=mutableList
        }
        return mutableLiveData
    }

    private fun getExcersizes(): ArrayList<Excersize> {
        var excersize = Excersize("Jumping jacks", 30, "nn", Excersize.VIEW_TYPE_EXCERSIZE)
        val list = ArrayList<Excersize>()
        for (i in 1..20) {
            list.add(excersize)
        }

        return list;
    }
}