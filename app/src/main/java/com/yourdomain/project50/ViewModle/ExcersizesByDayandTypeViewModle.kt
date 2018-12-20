package com.yourdomain.project50.ViewModle

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.yourdomain.project50.ExcersizesABSPlanConstants
import com.yourdomain.project50.ExcersizesBUTTPlanConstants
import com.yourdomain.project50.ExcersizesFullBodyConstants
import com.yourdomain.project50.Model.ExcersizePlan
import com.yourdomain.project50.Model.Excesizes

/**
 * Created by apple on 11/13/18.
 */

class ExcersizesByDayandTypeViewModle : ViewModel() {


    private var mutableLiveData: MutableLiveData<Excesizes>? = null

    fun getExcersizs(day: Int,plan :String): LiveData<Excesizes>? {
        if (mutableLiveData == null) {
            mutableLiveData = MutableLiveData()
            when (plan) {
                ExcersizePlan.PLAN_FULL_BODY -> {
                    val excesizes = ExcersizesFullBodyConstants.getDay1ExcersizeKeys(day + 1)
                    mutableLiveData?.value = excesizes
                }
                ExcersizePlan.PLAN_ABS -> {
                    val excesizes = ExcersizesABSPlanConstants.getExcersizeByDay(day + 1)
                    mutableLiveData?.value = excesizes
                }
                else -> {
                    val excesizes = ExcersizesBUTTPlanConstants.getDay1ExcersizeKeys(day + 1)
                    mutableLiveData?.value = excesizes
                }
            }

        }
        return mutableLiveData
    }


}
