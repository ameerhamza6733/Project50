package com.yourdomain.project50

import android.content.Context
import com.google.gson.Gson
import com.yourdomain.project50.Model.Person


/**
 * Created by apple on 11/6/18.
 */
class MY_Shared_PREF{

    companion object {

        private val SHARE_PREF_FILE=this.javaClass.`package`.name+"SHARE_PREF_FILE"
        private val personKey=this.javaClass.`package`.name+"personkey"

         private val  gson=Gson()

        fun savePersonAppearance( person: Person,context: Context){

            val sharedPreferences=context.applicationContext.getSharedPreferences(SHARE_PREF_FILE,0)
            val editer = sharedPreferences.edit()
            editer.putString(personKey, gson.toJson(person)).apply()
        }

        fun getPersonAppearance(context: Context):Person{
            val sharedPreferences=context.applicationContext.getSharedPreferences(SHARE_PREF_FILE,0)

            return gson.fromJson(sharedPreferences.getString(personKey,""),Person::class.java)
        }
    }

}