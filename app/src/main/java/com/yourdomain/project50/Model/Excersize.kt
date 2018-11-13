package com.yourdomain.project50.Model

/**
 * Created by apple on 11/12/18.
 */
class Excersize(val title:String,val totalTimeInSeconds:Int,val excersizeGif :String,val viewType:Int){
    companion object {
        val VIEW_TYPE_EXCERSIZE:Int=1
        val VIEW_TYPE_AD=2;
    }
}