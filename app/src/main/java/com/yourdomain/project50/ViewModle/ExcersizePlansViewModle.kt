package com.yourdomain.project50.ViewModle

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.util.Log
import com.yourdomain.project50.MY_Shared_PREF
import com.yourdomain.project50.Model.ExerciseDay
import com.yourdomain.project50.Model.ExcersizePlan
import com.yourdomain.project50.R
import java.util.ArrayList

/**
 * Created by apple on 11/7/18.
 */
class ExcersizePlansViewModle(application: Application) : AndroidViewModel(application) {
    private var excersizePlanList: MutableList<ExcersizePlan>? = ArrayList()
    private val TAG="ExcersizePlansViewModle"


    public fun getExcersizePlans(): MutableList<ExcersizePlan> {
excersizePlanList?.clear()

        val excersizesTypes = arrayOf(ExcersizePlan.PLAN_FULL_BODY,ExcersizePlan.PLAN_ABS,ExcersizePlan.PLAN_BUTT)
        val excersizeTypeImages = getApplication<Application>().resources.obtainTypedArray(R.array.exercise_type_icon)
        val excersizeTypeTotalDays = getApplication<Application>().resources.getStringArray(R.array.exercise_type_total_days)
        val daysComplted = MY_Shared_PREF.getAllCompletedorInprogressDays(getApplication())

        for (i in 0 until excersizesTypes.size) {
          var fillterMapByExcersizeType= daysComplted.filterKeys { it.contains(excersizesTypes[i],true) }
          var filterMapByProgress= fillterMapByExcersizeType.filter<String, ExerciseDay> {
              Log.d(TAG," "+it.value.progress.contains("100%"))
              it.value.progress.contains("100%") }
            var excersize = ExcersizePlan(excersizesTypes[i], excersizeTypeTotalDays[i].toInt(), filterMapByProgress.size, excersizeTypeImages.getResourceId(i,-1), ExcersizePlan.TYPE_EXCERSISE)
            Log.d(TAG,""+excersize.completedDays)
            excersizePlanList?.add(excersize)

        }
        var excersize = ExcersizePlan("native ad", 0, 0, -1, ExcersizePlan.TYPE_AD)
        excersizePlanList?.add(excersize)
        return excersizePlanList!!;
    }
}