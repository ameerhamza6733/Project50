package com.yourdomain.project50.ViewModle

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.util.Log
import com.yourdomain.project50.MY_Shared_PREF
import com.yourdomain.project50.Model.ExcersizeDay
import com.yourdomain.project50.Model.ExcersizePlan
import com.yourdomain.project50.R

/**
 * Created by apple on 11/7/18.
 */
class ExcersizePlansViewModle(application: Application) : AndroidViewModel(application) {
    private var excersizePlanList: MutableList<ExcersizePlan>? = null
    private val TAG="ExcersizePlansViewModle"


    public fun getExcersizePlans(): MutableList<ExcersizePlan> {

        excersizePlanList = ArrayList();

        val excersizesTypes = getApplication<Application>().resources.getStringArray(R.array.excersize_type)
        val excersizeTypeImages = getApplication<Application>().resources.getStringArray(R.array.excersize_type_icon)
        val excersizeTypeTotalDays = getApplication<Application>().resources.getStringArray(R.array.excersize_type_total_days)
        val daysComplted = MY_Shared_PREF.getAllCompletedorInprogressDays(getApplication())

        for (i in 0 until excersizesTypes.size) {
          var fillterMapByExcersizeType= daysComplted.filterKeys { it.contains(excersizesTypes[i],true) }
          var filterMapByProgress= fillterMapByExcersizeType.filter<String, ExcersizeDay> { it.value.progress.contains("100%") }
            var excersize = ExcersizePlan(excersizesTypes[i], excersizeTypeTotalDays[i].toInt(), filterMapByProgress.size, excersizeTypeImages[i], ExcersizePlan.TYPE_EXCERSISE)
            excersizePlanList?.add(excersize)

        }
        var excersize = ExcersizePlan("native ad", 0, 0, "native ad id", ExcersizePlan.TYPE_AD)
        excersizePlanList?.add(excersize)
        return excersizePlanList!!;
    }
}