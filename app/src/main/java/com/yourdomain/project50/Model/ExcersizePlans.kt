package com.yourdomain.project50.Model

/**
 * Created by hamza rafiq on 11/7/18.
 */
class ExcersizePlans(var name:String, var totalDays:Int, var completedDays:Int, var image:String, var ViewType :Int){
    companion object {
        val TYPE_AD=2
        val TYPE_EXCERSISE=1;
    }
}