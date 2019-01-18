package com.yourdomain.project50.ViewModle

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.yourdomain.project50.DataBaseConstants.*
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
                ExcersizePlan.PLAN_BUTT -> {
                    val excesizes = ExcersizesBUTTPlanConstants.getDay1ExcersizeKeys(day + 1)
                    mutableLiveData?.value = excesizes
                }
                //TODO:5)AddNewPlan please fill up data for newly created plan from database
                //TODO:6)AddNewPlan To create database please copy past ExcersizesBUTTPlanConstants file with different name e.g. ExcersizesBUTTPlan2Constants
                ExcersizePlan.PLAN_BUTT_New->{
                    val excesizes = ExcersizesBUTTPlan2Constants.getDay1ExcersizeKeys(day + 1)
                    mutableLiveData?.value = excesizes
                }
                ExcersizePlan.TEST_NEW_PLAN->{
                    val excesizes = ExcersizesNEW_PLAN_TESTConstants.getDay1ExcersizeKeys(day + 1)
                    mutableLiveData?.value = excesizes
                }

            }

        }
        return mutableLiveData
    }


}
