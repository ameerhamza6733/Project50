package com.yourdomain.project50.Model

/**
 * Created by apple on 11/18/18.
 */
class MoreApps(var title:String,var downloadingUrl:String,var icon:String,var viewType:Int=0,var rating:Double =0.0){
    companion object {
        val VIEW_TYPE_APP=0;
        val VIEW_TYPE_AD=1;
    }
}