package com.yourdomain.project50.ViewModle

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.yourdomain.project50.MY_Shared_PREF
import com.yourdomain.project50.Model.ExcersizeDays
import com.yourdomain.project50.Model.ExcersizePlans
import java.util.ArrayList

/**
 * Created by apple on 11/14/18.
 */
class FragmentDayButtPlanViewModle(application: Application) :AndroidViewModel(application){
    private var mutableList: java.util.ArrayList<ExcersizeDays>? = null
    private var mutableLiveData : MutableLiveData<ArrayList<ExcersizeDays>?>?=null

    fun getDays(): MutableLiveData<ArrayList<ExcersizeDays>?>? {
        if (mutableLiveData == null) {
            mutableLiveData = MutableLiveData()
            mutableList = ArrayList();
            val hashMap = MY_Shared_PREF.getAllFullBodyPlanDays(getApplication())
            for (dayNumber in 1..30) {
                if (hashMap.containsKey(ExcersizePlans.PLAN_BUTT+dayNumber.toString())) {
                    mutableList?.add(hashMap[ExcersizePlans.PLAN_BUTT+dayNumber.toString()]!!)

                } else {
                    var excersizeDays = ExcersizeDays(dayNumber, 1)
                    mutableList?.add(excersizeDays)
                }


            }
            mutableLiveData?.value = mutableList
        }
        return mutableLiveData
    }
}