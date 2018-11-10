package com.yourdomain.project50.ViewModle

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.yourdomain.project50.Model.Excersize
import com.yourdomain.project50.R

/**
 * Created by apple on 11/7/18.
 */
class ExcersizePlansViewModle(application: Application) : AndroidViewModel(application) {
    private var excersizeList: MutableList<Excersize>? = null


    public fun getExcersizePlans(): MutableList<Excersize> {

        excersizeList = ArrayList();

        val excersizesTypes = getApplication<Application>().resources.getStringArray(R.array.excersize_type)
        val excersizeTypeImages = getApplication<Application>().resources.getStringArray(R.array.excersize_type_icon)
        val excersizeTypeTotalDays = getApplication<Application>().resources.getStringArray(R.array.excersize_type_total_days)

        for (i in 0 until excersizesTypes.size) {
         var excersize=   Excersize(excersizesTypes[i], excersizeTypeTotalDays[i].toInt(), -1, excersizeTypeImages[i],Excersize.TYPE_EXCERSISE)
            excersizeList?.add(excersize)

                      }
        var excersize=   Excersize("native ad",0,0,"native ad id",Excersize.TYPE_AD)
        excersizeList?.add(excersize)
        return excersizeList!!;
    }
}