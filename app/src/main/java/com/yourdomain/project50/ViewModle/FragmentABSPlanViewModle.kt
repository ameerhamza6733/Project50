package com.yourdomain.project50.ViewModle

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.yourdomain.project50.MY_Shared_PREF
import com.yourdomain.project50.Model.ExerciseDay
import com.yourdomain.project50.Model.ExcersizePlan
import java.util.ArrayList

/**
 * Created by apple on 11/14/18.
 */
class FragmentABSPlanViewModle(application: Application) :AndroidViewModel(application){
    private var mutableList: java.util.ArrayList<ExerciseDay>? = null
    private var mutableLiveData : MutableLiveData<ArrayList<ExerciseDay>?>?=null

    fun getDays(): MutableLiveData<ArrayList<ExerciseDay>?>? {
        if (mutableLiveData == null) {
            mutableLiveData = MutableLiveData()
            mutableList = ArrayList();
            val hashMap = MY_Shared_PREF.getAllCompletedorInprogressDays(getApplication())
            for (dayNumber in 1..30) {
                if (hashMap.containsKey(ExcersizePlan.PLAN_ABS+" "+dayNumber.toString())) {
                    mutableList?.add(hashMap.get(ExcersizePlan.PLAN_ABS+" "+dayNumber.toString())!!)

                } else {
                    var excersizeDays = ExerciseDay(dayNumber, 1)
                    mutableList?.add(excersizeDays)
                }


            }
            mutableLiveData?.value = mutableList
        }
        return mutableLiveData
        return mutableLiveData
    }
}