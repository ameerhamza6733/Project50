package com.yourdomain.project50.ViewModle

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.yourdomain.project50.Model.ExcersizePlans
import com.yourdomain.project50.R

/**
 * Created by apple on 11/7/18.
 */
class ExcersizePlansViewModle(application: Application) : AndroidViewModel(application) {
    private var excersizePlansList: MutableList<ExcersizePlans>? = null


    public fun getExcersizePlans(): MutableList<ExcersizePlans> {

        excersizePlansList = ArrayList();

        val excersizesTypes = getApplication<Application>().resources.getStringArray(R.array.excersize_type)
        val excersizeTypeImages = getApplication<Application>().resources.getStringArray(R.array.excersize_type_icon)
        val excersizeTypeTotalDays = getApplication<Application>().resources.getStringArray(R.array.excersize_type_total_days)

        for (i in 0 until excersizesTypes.size) {
         var excersize=   ExcersizePlans(excersizesTypes[i], excersizeTypeTotalDays[i].toInt(), -1, excersizeTypeImages[i],ExcersizePlans.TYPE_EXCERSISE)
            excersizePlansList?.add(excersize)

                      }
        var excersize=   ExcersizePlans("native ad",0,0,"native ad id",ExcersizePlans.TYPE_AD)
        excersizePlansList?.add(excersize)
        return excersizePlansList!!;
    }
}