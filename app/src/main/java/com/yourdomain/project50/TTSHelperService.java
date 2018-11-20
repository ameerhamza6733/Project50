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

import java.util.HashMap;
import java.util.Locale;

/**
 * Created by apple on 11/19/18.
 */

public class TTSHelperService extends Service implements TextToSpeech.OnInitListener, TextToSpeech.OnUtteranceCompletedListener {
    private static TextToSpeech myTTS;
    private static final String TAG = "SpeachService";
    public static final String ACTION_TTS="TTSHelperService.ACTION_TTS";
    //private String msg;


    @Override
    public void onCreate() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_TTS);
        LocalBroadcastManager.getInstance(this).registerReceiver(mNewTTStext,filter);
        myTTS = new TextToSpeech(this, this);
        super.onCreate();
        Log.d(TAG, "onCreate() ran");
    }


    public void sayString(String string) {
        if (myTTS != null) {

                HashMap<String, String> hashMap = new HashMap<String, String>();
            myTTS.setLanguage(new Locale("th"));
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
            if (myTTS.isLanguageAvailable(new Locale("th")) == TextToSpeech.LANG_AVAILABLE) {
                myTTS.setLanguage(new Locale("th"));
            }
            sayString("");
        } else if (initStatus == TextToSpeech.ERROR) {

        }
        Log.i(TAG, "onInit() ran");
    }

    @Override
    public void onUtteranceCompleted(String s) {
        stopSelf();
        Log.i(TAG, "onUtteranceCompleted() ran");
    }

    BroadcastReceiver mNewTTStext = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            //handle the broadcast event here
            Log.d(TAG,"onReceive: "+intent.getStringExtra("TTStext"));
            sayString(intent.getStringExtra("TTStext"));
        }
    };


}
