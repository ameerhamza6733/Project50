package com.yourdomain.project50;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import com.jjoe64.graphview.series.DataPoint;
import com.yourdomain.project50.Model.Excesizes;
import com.yourdomain.project50.Model.PersonAppearance;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by apple on 11/5/18.
 */

public class Utils {


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
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + pack));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            application.startActivity(intent);
        } catch (android.content.ActivityNotFoundException anfe) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + pack));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            application.startActivity(intent);
        }
    }

    public static void shareTextIntent(Context application, String text) {
        String shareBody = text;
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        application.startActivity(Intent.createChooser(sharingIntent, "Share using"));
    }

    public static void feedbackEmaileIntent(Context context) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        //TODO:replace your email
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"ameerhamza6733@gmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "subject");
        intent.putExtra(Intent.EXTRA_TEXT, "mail body");
        context.startActivity(Intent.createChooser(intent, ""));
    }

    public static void openBrowser(Context application, String url) {
        if (!url.startsWith("https://") && !url.startsWith("http://")) {
            url = "http://" + url;
        }
        Intent openUrlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        openUrlIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        application.startActivity(openUrlIntent);
    }

    public static String CMtoFeet(double cm) {
        return String.valueOf(cm / 30.48);
    }

    public static double KGtoLBS(double waightInKg){
        return waightInKg*2.205;
    }


    public static boolean isNetworkAvailable(Application application) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    static Calendar cal = Calendar.getInstance();
    static Date date = new Date();

    public static Date getNextDay() {

        cal.setTime(date);
        cal.add(Calendar.DATE, 1); //minus number would decrement the days
        return cal.getTime();
    }

    public static Date getNext30Day() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, 29); //minus number would decrement the days
        return cal.getTime();
    }

    public static int getDrawbleAccodingToBMI(double currentBMI){
        if (currentBMI<18.5){
            return R.drawable.bmi1;
        }else if (currentBMI>=18.5 && currentBMI<24.5){
            return R.drawable.bmi2;
        }else  if(currentBMI>=24.5 && currentBMI<30.5){
            return  R.drawable.bmi3;
        }else if(currentBMI>=30.5 && currentBMI<40.5 ){
            return R.drawable.bmi4;
        }else {
         return    R.drawable.bmi5;
        }
    }

    public static double CMtoM(double hightInCm){
       Double hightInMeter = hightInCm/100;
       Log.d(TAG,"converting cm to meter : hight in cm : "+hightInCm+ "hight in mater: "+hightInMeter);
        return  hightInMeter;
    }

    public static double FeetToInch(double feet){
        return  feet*12;
    }
    public static  double calculateBMIinKg(double waightInKg,double hightInMeter) {
        return waightInKg/(hightInMeter*2);

    }

    public static double calcautleBMIinlbs(double wagitInPound, double hightInInch){
        return (wagitInPound /(hightInInch*hightInInch))*703;
    }
}
