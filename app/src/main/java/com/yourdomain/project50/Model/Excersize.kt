package com.yourdomain.project50.Model

/**
 * Created by hamza rafiq on 11/7/18.
 */
class Excersize(var name:String="", var totalDays:Int=-1,var completedDays:Int=-1, var image:String="",var ViewType :Int=-1){
    companion object {
        val TYPE_AD=2
        val TYPE_EXCERSISE=1;
    }
}