package com.yourdomain.project50;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.yourdomain.project50.Model.Excesizes;

import java.text.DecimalFormat;

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

    public static void shareTextExtra(Application application, String text) {
        Intent intent2 = new Intent();
        intent2.setAction(Intent.ACTION_SEND);
        intent2.setType("text/plain");
        intent2.putExtra(Intent.EXTRA_TEXT, text);
        application.startActivity(Intent.createChooser(intent2, "Share via"));
    }

    public static void startTTSScreen(Context context) {
        Intent intent = new Intent();
        intent.setAction("com.android.settings.TTS_SETTINGS");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void openGooglePlay(Application application, String pack) {
        try {
            Intent intent =new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + pack));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            application.startActivity(intent);
        } catch (android.content.ActivityNotFoundException anfe) {
        Intent intent =   new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + pack));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            application.startActivity(intent);
        }
    }

    public static void shareTextIntent(Context application,String text){
        String shareBody =text;
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
       application. startActivity(Intent.createChooser(sharingIntent, "Share using"));
    }

    public static void feedbackEmaileIntent(Context context){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        //TODO:replace your email
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "ameerhamza6733@gmail.com" });
        intent.putExtra(Intent.EXTRA_SUBJECT, "subject");
        intent.putExtra(Intent.EXTRA_TEXT, "mail body");
        context.startActivity(Intent.createChooser(intent, ""));
    }

    public static void openBrowser(Context application,String url){
        if (!url.startsWith("https://") && !url.startsWith("http://")){
            url = "http://" + url;
        }
        Intent openUrlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        application.startActivity(openUrlIntent);
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

}
