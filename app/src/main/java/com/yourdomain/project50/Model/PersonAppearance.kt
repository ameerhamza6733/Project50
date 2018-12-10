package com.yourdomain.project50.Model

import com.google.gson.Gson
import java.util.*

/**
 * Created by apple on 11/4/18.
 */
class PersonAppearance(var SCALE_TYPE:Int ,var mHight:Double,var mWaight:Double, var date:Date) {
    companion object {
        val TYPE_CM_KG=1
        val TYPE_IN_LBS=2
    }


}