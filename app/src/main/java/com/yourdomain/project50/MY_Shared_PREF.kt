package com.yourdomain.project50

import android.app.Application
import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.jjoe64.graphview.series.DataPoint
import com.yourdomain.project50.Model.*
import java.util.*


/**
 * Created by apple on 11/6/18.
 */
class MY_Shared_PREF {

    companion object {

        private val TAG = "MY_Shared_PREF";

        private val SHARE_PREF_FILE = this.javaClass.`package`.name + "SHARE_PREF_FILE"
        private val SHARED_PREF_ALL_DAYS_FILE = this.javaClass.`package`.name + "SHARED_PREF_ALL_DAYS_FILE"
        private val SHARE_PREF_SEETINGS = "SHARE_PREF_SEETINGS";

        private val SHARE_PREFF_PERSON = "share pref person"
        private val SHARE_PREFF_PERSON_KEY = "SHARE_PREFF_PERSON_KEY"

        private val SHARED_PREF_APP_SETTINGS = "SHARED_PREF_APP_SETTINGS";
        private val SHARED_PREF_APP_SETTINGS_KEY = "SHARED_PREF_APP_SETTINGS_KEY";

        private val SHARE_PREF_ADMOB_SETTINGS_FROM_FIREBAW_FILE = "SHARE_PREF_ADMOB_SETTINGS_FROM_FIREBAW_FILE"
        private val SHARE_PREF_ADMOB_SETTINGS_FROM_FIREBAW_KEY = "SHARE_PREF_ADMOB_SETTINGS_FROM_FIREBAW_KEY"

        private val SHARE_PREF_GRAPHS_FILE = "SHARE_PREF_GRAPHS_FILE"

        private val SHARE_PREF_SETTING_FROM_FIREBASE_FILE = "SHARE_PREF_SETTING_FROM_FIREBASE_FILE"
        private val SHARE_PREF_SETTTINGS_FROM_FIREBASE_KEY = "SHARE_PREF_SETTTINGS_FROM_FIREBASE_KEY"


        private val SHARE_PREF_COME_BACK_LATTER_FILE = "SHARE_PREF_COME_BACK_LATTER_FILE"
        private val SHARE_PREF_COME_BACK_LATTER_KEY = "SHARE_PREF_COME_BACK_LATTER_KEY"

        private val gson = Gson()

        fun savePersonAppearance(person: Person, context: Context) {
            val personKey = this.javaClass.`package`.name + "personkey"

            val sharedPreferences = context.applicationContext.getSharedPreferences(SHARE_PREF_FILE, 0)
            val editer = sharedPreferences.edit()
            editer.putString(personKey, gson.toJson(person)).apply()
        }

        fun getPersonAppearance(context: Context): Person {
            val sharedPreferences = context.applicationContext.getSharedPreferences(SHARE_PREF_FILE, 0)
            val personKey = "MY_SHAREED_PREF" + "personkey"

            return gson.fromJson(sharedPreferences.getString(personKey, ""), Person::class.java)
        }


        fun saveDayByKey(application: Application, key: String, day: ExerciseDay) {
            val sharedPreferences = application.applicationContext.getSharedPreferences(SHARED_PREF_ALL_DAYS_FILE, 0)
            val editer = sharedPreferences.edit()
            editer.putString(key, gson.toJson(day)).apply()

        }

        fun getDayByKey(application: Application, key: String): ExerciseDay? {
            val sharedPreferences = application.applicationContext.getSharedPreferences(SHARED_PREF_ALL_DAYS_FILE, 0)
            if (!sharedPreferences.contains(key))
                return null
            return gson.fromJson(sharedPreferences.getString(key, null), ExerciseDay::class.java)
        }

        fun getAllCompletedorInprogressDays(application: Application): HashMap<String, ExerciseDay> {
            val list = HashMap<String, ExerciseDay>()
            val sharedPreferences = application.applicationContext.getSharedPreferences(SHARED_PREF_ALL_DAYS_FILE, 0)
            val keys = sharedPreferences.all
            for (entry in keys.entries) {
                list.put(entry.key, gson.fromJson(entry.value.toString(), ExerciseDay::class.java))
                Log.d("key ", entry.key + " value: " + entry.value.toString())
            }
            return list
        }

        fun saveAppSettings(application: Application, key: String, value: String) {
            val sharePref = application.applicationContext.getSharedPreferences(SHARE_PREF_SEETINGS, 0)
            val edit = sharePref.edit()
            edit.putString(key, value).apply()
        }

        fun savePerson(application: Application, person: Person) {
            val sharePref = application.applicationContext.getSharedPreferences(SHARE_PREFF_PERSON, 0)
            val edit = sharePref.edit()
            edit.putString(SHARE_PREFF_PERSON_KEY, gson.toJson(person)).apply()
        }

        fun isPersonAppearanceSaved(application: Application): Boolean {
            val sharePref = application.applicationContext.getSharedPreferences(SHARE_PREFF_PERSON, 0)
            return sharePref.contains(SHARE_PREFF_PERSON_KEY)
        }

        fun getPerson(application: Application): Person {
            val sharePref = application.applicationContext.getSharedPreferences(SHARE_PREFF_PERSON, 0)
            return if (sharePref.contains(SHARE_PREFF_PERSON_KEY)) {
                gson.fromJson(sharePref.getString(SHARE_PREFF_PERSON_KEY, null), Person::class.java)
            } else {
                Person();
            }
        }

        fun saveAppSettings(application: Application, settings: Settings) {
            val sharePref = application.applicationContext.getSharedPreferences(SHARE_PREF_SEETINGS, 0)
            val editer = sharePref.edit()
            editer.putString(SHARED_PREF_APP_SETTINGS_KEY, gson.toJson(settings)).apply()
        }

        fun getAppSettings(application: Application): Settings {
            val sharePref = application.applicationContext.getSharedPreferences(SHARE_PREF_SEETINGS, 0)
            if (!sharePref.contains(SHARED_PREF_APP_SETTINGS_KEY))
                return Settings(TTSSettings(), WorkoutSettings());
            return gson.fromJson(sharePref.getString(SHARED_PREF_APP_SETTINGS_KEY, ""), Settings::class.java)
        }

        fun saveFireBaseAppAdmobSetting(application: Application, appSettingFromFirebase: AppAdmobSettingsFromFirebase) {
            val share_Pref = application.getSharedPreferences(SHARE_PREF_ADMOB_SETTINGS_FROM_FIREBAW_FILE, 0)
            val editer = share_Pref.edit()
            editer.putString(SHARE_PREF_ADMOB_SETTINGS_FROM_FIREBAW_KEY, gson.toJson(appSettingFromFirebase)).apply()
        }

        fun getFirebaseAdmobAppSettings(application: Application): AppAdmobSettingsFromFirebase {
            val share_pref = application.getSharedPreferences(SHARE_PREF_ADMOB_SETTINGS_FROM_FIREBAW_FILE, 0)
            if (share_pref.contains(SHARE_PREF_ADMOB_SETTINGS_FROM_FIREBAW_KEY)) {
                return gson.fromJson(share_pref.getString(SHARE_PREF_ADMOB_SETTINGS_FROM_FIREBAW_KEY, ""), AppAdmobSettingsFromFirebase::class.java)
            } else {

                return AppAdmobSettingsFromFirebase()
            }
        }

        fun saveGraphCalvsDays(application: Application, dataPoint: DataPoint, key: String) {
            Log.d(TAG, "save data points for graphs")

            val share_pref = application.getSharedPreferences(SHARE_PREF_GRAPHS_FILE, 0)
            val editer = share_pref.edit()
            var updateDataPoint = dataPoint;
            if (share_pref.contains(key)) {
                val priveiousCalFromThisDay = gson.fromJson<DataPoint>(share_pref.getString(key, null), DataPoint::class.java)
                updateDataPoint = DataPoint(dataPoint.x, priveiousCalFromThisDay.y + dataPoint.y)
            }
            editer.putString(key, gson.toJson(updateDataPoint))
            editer.apply()

        }

        fun getAllDataGraphCalvsDays(application: Application): Array<DataPoint?> {

            val sharef_Pref = application.getSharedPreferences(SHARE_PREF_GRAPHS_FILE, 0)
            val allEntries = sharef_Pref.getAll()
            val dataPointArray = arrayOfNulls<DataPoint>(allEntries.size)
            var counter = 0
            for (entry in allEntries.entries) {
                // i in [1, 10), 10 is excluded
                dataPointArray.set(counter, gson.fromJson(entry.value.toString(), DataPoint::class.java))
                Log.d(TAG, "key : " + entry.key + " value of x :  " + dataPointArray[counter]?.x?.toLong()?.let { Date(it) })
                counter++
            }

            return dataPointArray.reversedArray()
        }

        fun saveAppsSettingFromFireBase(application: Application, appSettingFromFirebase: AppSettingsFromFireBase) {
            val sharePref = application.getSharedPreferences(SHARE_PREF_SETTING_FROM_FIREBASE_FILE, 0)
            val edit = sharePref.edit()
            edit.putString(SHARE_PREF_SETTTINGS_FROM_FIREBASE_KEY, gson.toJson(appSettingFromFirebase)).apply()
        }

        fun saveComeBackLatterExcersize(application: Application, comeBackLatter: ComeBackLatter) {
            val share_pref = application.getSharedPreferences(SHARE_PREF_COME_BACK_LATTER_FILE, 0)
            val edit = share_pref.edit()
            edit.putString(SHARE_PREF_COME_BACK_LATTER_KEY, gson.toJson(comeBackLatter))
            edit.apply()
        }

        fun getComeBackLatterExcersize(contex: Context): ComeBackLatter? {
            val share_pref = contex.getSharedPreferences(SHARE_PREF_COME_BACK_LATTER_FILE, 0)
            return gson.fromJson(share_pref.getString(SHARE_PREF_COME_BACK_LATTER_KEY, null), ComeBackLatter::class.java)
        }


    }

}