package com.yourdomain.project50.Model

/**
 * Created by apple on 11/12/18.
 */
class ExcersizeDays(var day : Int,val  viewType:Int=-1,var totaleTime:Long=-1,var timeDone:Long=-1){
    companion object {
        val VIEW_TYPE_DAY:Int=1
        val VIEW_TYPE_AD=2;
    }
}