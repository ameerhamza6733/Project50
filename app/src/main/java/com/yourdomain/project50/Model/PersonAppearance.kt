package com.yourdomain.project50.Model

import com.google.gson.Gson

/**
 * Created by apple on 11/4/18.
 */
class PersonAppearance(var SCALE_TYPE:Int ,var mHight:String,var mWaight:String) {
    companion object {
        val TYPE_CM_KG=1
        val TYPE_IN_LBS=2
    }


}