package com.yourdomain.project50.Model

/**
 * Created by hamza rafiq on 11/7/18.
 */
class ExcersizePlan(var name:String, var totalDays:Int, val completedDays:Int, var image:Int, var ViewType :Int){
    companion object {
        val TYPE_AD=2
        val TYPE_EXCERSISE=1;
        val PLAN_FULL_BODY="FULL BODY PLAN"
        val PLAN_ABS="ABS PLAN";
        val PLAN_BUTT="BUTT PLAN"
    }
}