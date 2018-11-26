package com.yourdomain.project50;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.yourdomain.project50.Model.Excesizes;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by apple on 11/5/18.
 */

public class Utils {

    static DecimalFormat df = new DecimalFormat("##.##");
    private static String TAG = "UtilsTAG";

    public static String CountTotalTime(int[] viewType, int[] seconds) {
        double total = 0;
        for (int i = 0; i < seconds.length; i++) {
            if (viewType[i] == Excesizes.VIEW_TYPE_UN_LIMTED_EXCERSIZE)
                continue;
            total = total + seconds[i];
        }
        String mints = String.valueOf(total / 60);
        Log.d(TAG, "total seconds for currnet excersize" + mints.substring(0, mints.lastIndexOf(".") + 2));
        return mints.substring(0, mints.lastIndexOf(".") + 2);
    }

    public static String toPersentage(int done, int totle) {
        return String.valueOf(((done * 100 / totle))) + "%";
    }

    public String CMtoFeet(double cm) {
        return df.format(Math.round((cm / 30.48) * 100D) / 100D);
    }

    public String FeettoCM(double inch) {
        return df.format(Math.round((inch * 30.48) * 100D) / 100D);
    }

    public String KGtoLBS(double kg) {
        return df.format(Math.round((kg * 2.20) * 100D) / 100D);
    }

    public String LBStoKG(double lbs) {
        return df.format(Math.round((lbs / 2.20) * 100D) / 100D);
    }

    public static void shareTextExtra(Application application,String text){
        Intent intent2 = new Intent();
        intent2.setAction(Intent.ACTION_SEND);
        intent2.setType("text/plain");
        intent2.putExtra(Intent.EXTRA_TEXT, text);
        application. startActivity(Intent.createChooser(intent2, "Share via"));
    }

    public static void startTTSScreen(Context context){
        Intent intent = new Intent();
        intent.setAction("com.android.settings.TTS_SETTINGS");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


}
