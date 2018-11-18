package com.yourdomain.project50

import android.app.Application
import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.yourdomain.project50.Model.ExcersizeDays
import com.yourdomain.project50.Model.Person
import java.util.ArrayList
import java.util.HashMap
import java.util.HashSet


/**
 * Created by apple on 11/6/18.
 */
class MY_Shared_PREF{

    companion object {

        private val SHARE_PREF_FILE=this.javaClass.`package`.name+"SHARE_PREF_FILE"
        private val SHARED_PREF_FULL_BODY_FILE=this.javaClass.`package`.name+"SHARED_PREF_FULL_BODY_FILE"

         private val  gson=Gson()

        fun savePersonAppearance( person: Person,context: Context){
        val personKey=this.javaClass.`package`.name+"personkey"

            val sharedPreferences=context.applicationContext.getSharedPreferences(SHARE_PREF_FILE,0)
            val editer = sharedPreferences.edit()
            editer.putString(personKey, gson.toJson(person)).apply()
        }

        fun getPersonAppearance(context: Context):Person{
            val sharedPreferences=context.applicationContext.getSharedPreferences(SHARE_PREF_FILE,0)
             val personKey=this.javaClass.`package`.name+"personkey"

            return gson.fromJson(sharedPreferences.getString(personKey,""),Person::class.java)
        }



        fun saveCurrentDay(application: Application,key:String,days: ExcersizeDays){
            val sharedPreferences=application.applicationContext.getSharedPreferences(SHARED_PREF_FULL_BODY_FILE,0)
            val editer = sharedPreferences.edit()
            editer.putString(key, gson.toJson(days)).apply()

        }

        fun getCurrentDay(application: Application,key:String) :ExcersizeDays?{
            val sharedPreferences=application.applicationContext.getSharedPreferences(SHARED_PREF_FULL_BODY_FILE,0)
            if (sharedPreferences.contains(key))
                return null
            return gson.fromJson(sharedPreferences.getString(key,null),ExcersizeDays::class.java)
        }

        fun getAllFullBodyPlanDays(application: Application):HashMap<String,ExcersizeDays>{
           val list = HashMap<String,ExcersizeDays>()
            val sharedPreferences=application.applicationContext.getSharedPreferences(SHARED_PREF_FULL_BODY_FILE,0)
            val keys = sharedPreferences.all
            for (entry in keys.entries) {
                list.put(entry.key,gson.fromJson(entry.value.toString(),ExcersizeDays::class.java))
                Log.d("map values", entry.key + ": " + entry.value.toString())
            }
            return list
        }

    }

}