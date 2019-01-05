package com.yourdomain.project50.ViewModle

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.yourdomain.project50.MY_Shared_PREF
import com.yourdomain.project50.Model.ExerciseDay
import com.yourdomain.project50.Model.ExcersizePlan
import java.util.*

/**
 * Created by apple on 11/13/18.
 */
class FragmentFullBodyPlanViewModle(application: Application) : AndroidViewModel(application) {
    private var mutableList: java.util.ArrayList<ExerciseDay>? = null
    private var mutableLiveData: MutableLiveData<ArrayList<ExerciseDay>?>? = null
    val restDays: IntArray = intArrayOf(5, 15, 20,25)


    fun getDays(): MutableLiveData<ArrayList<ExerciseDay>?>? {
        if (mutableLiveData == null) {
            mutableLiveData = MutableLiveData()
            mutableList = ArrayList();
            val hashMap = MY_Shared_PREF.getAllCompletedorInprogressDays(getApplication())
            for (dayNumber in 1..30) {

                if (restDays.contains(dayNumber)){
                    var excersizeDay = ExerciseDay(dayNumber, ExerciseDay.VIEW_TYPEREST)
                    mutableList?.add(excersizeDay)
                    continue
                }
                if (hashMap.containsKey(ExcersizePlan.PLAN_FULL_BODY +" "+dayNumber.toString())) {
                    mutableList?.add(hashMap.get(ExcersizePlan.PLAN_FULL_BODY +" "+dayNumber.toString())!!)

                } else {
                    var excersizeDay = ExerciseDay(dayNumber, 1)
                    mutableList?.add(excersizeDay)
                }


            }
            mutableLiveData?.value = mutableList
        }
        return mutableLiveData
    }


}