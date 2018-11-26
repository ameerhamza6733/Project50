package com.yourdomain.project50;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.yourdomain.project50.Model.Settings;
import com.yourdomain.project50.Model.TTSSettings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by apple on 11/19/18.
 */

public class TTSHelperService extends Service implements TextToSpeech.OnInitListener {
    public static final String ACTION_TTS = "TTSHelperService.ACTION_TTS";
    public static final String ACTION_NEW_LANGUAGES="ACTION_NEW_LANGUAGES";
    private static final String TAG = "SpeachService";
    public static TextToSpeech myTTS;
    public static Locale locale;
    BroadcastReceiver mNewTTStext = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            //handle the broadcast event here
            if (intent.getAction().equals(ACTION_TTS)){
                Log.d(TAG, "onReceive: " + intent.getStringExtra("TTStext"));
                sayString(intent.getStringExtra("TTStext"));
            }

        }
    };
    //private String msg;
    private Settings settings;

    public static ArrayList<Locale> initSupportedLanguagesLegacy() {
        if (myTTS == null) return null;
        Locale[] locales = Locale.getAvailableLocales();
        ArrayList<Locale> localeList = new ArrayList<Locale>();
        for (Locale locale : locales) {
            int res = TTSHelperService.myTTS.isLanguageAvailable(locale);
            if (res == TextToSpeech.LANG_COUNTRY_AVAILABLE) {
                localeList.add(locale);
                Log.d(TAG, "TTS Language" + locale.getDisplayName());
            }
        }
        return localeList;
    }



    @Override
    public void onCreate() {
        settings = MY_Shared_PREF.Companion.getAppSettings(getApplication());
        locale=settings.getTtsSettings().getLocale();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_TTS);
        filter.addAction(ACTION_NEW_LANGUAGES);
        LocalBroadcastManager.getInstance(this).registerReceiver(mNewTTStext, filter);
        myTTS = new TextToSpeech(this, this);
        super.onCreate();
        Log.d(TAG, "onCreate() ran");
    }

    public void sayString(String string) {
        if (myTTS != null) {

            HashMap<String, String> hashMap = new HashMap<String, String>();
            myTTS.setLanguage(locale);
            myTTS.speak(string, TextToSpeech.QUEUE_ADD, hashMap);

        }

        Log.i(TAG, "sayString() ran");
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        if (myTTS != null) {
            myTTS.stop();
            myTTS.shutdown();
        }
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mNewTTStext);
        super.onDestroy();
        Log.i(TAG, "onDestroy() ran");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onInit(int initStatus) {
        if (initStatus == TextToSpeech.SUCCESS) {
            if (myTTS.isLanguageAvailable(locale) == TextToSpeech.LANG_AVAILABLE) {
                myTTS.setLanguage(locale);
            }
            sayString("");
        } else if (initStatus == TextToSpeech.ERROR) {

        }
        Log.i(TAG, "onInit() ran");
    }

}
