package com.yourdomain.project50;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.yourdomain.project50.Model.Excesizes;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by apple on 11/5/18.
 */

public class Utils {


    static Calendar cal = Calendar.getInstance();
    static Date date = new Date();
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

    public static void shareTextExtra(Context application, String text) {
        Intent intent2 = new Intent();
        intent2.setAction(Intent.ACTION_SEND);
        intent2.setType("text/plain");
        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent2.putExtra(Intent.EXTRA_SUBJECT,application.getString(R.string.app_name));
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
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT,application.getString(R.string.app_name));
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        application.startActivity(Intent.createChooser(sharingIntent, "Share using"));
    }

    public static void feedbackEmaileIntent(Context context) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        //TODO:replace your email
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"ameerhamza6733@gmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name));
        intent.putExtra(Intent.EXTRA_TEXT, "");
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

    public static double CMtoFeet(double cm) {
        return cm / 30.48;
    }

    public static double KGtoLBS(double waightInKg) {
        return waightInKg * 2.205;
    }

    public static boolean isNetworkAvailable(Application application) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

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

    public static int getDrawbleAccodingToBMI(double currentBMI) {
        if (currentBMI < 18.5) {
            return R.drawable.bmi1;
        } else if (currentBMI >= 18.5 && currentBMI < 24.5) {
            return R.drawable.bmi2;
        } else if (currentBMI >= 24.5 && currentBMI < 30.5) {
            return R.drawable.bmi3;
        } else if (currentBMI >= 30.5 && currentBMI < 40.5) {
            return R.drawable.bmi4;
        } else {
            return R.drawable.bmi5;
        }
    }

    public static double CMtoM(double hightInCm) {
        Double hightInMeter = hightInCm / 100;
        Log.d(TAG, "converting cm to meter : hight in cm : " + hightInCm + "hight in mater: " + hightInMeter);
        return hightInMeter;
    }

    public static double FeetToInch(double feet) {
        return feet * 12;
    }

    public static double calculateBMIinKg(double waightInKg, double hightInMeter) {
        return waightInKg / (hightInMeter * 2);

    }

    public static double calcautleBMIinlbs(double wagitInPound, double hightInInch) {
        return (wagitInPound / (hightInInch * hightInInch)) * 703;
    }

    public static void RestartTheApp(Application c) {
        try {
            //check if the context is given
            if (c != null) {
                //fetch the packagemanager so we can get the default launch activity
                // (you can replace this intent with any other activity if you want
                PackageManager pm = c.getPackageManager();
                //check if we got the PackageManager
                if (pm != null) {
                    //create the intent with the default start activity for your application
                    Intent mStartActivity = pm.getLaunchIntentForPackage(
                            c.getPackageName()
                    );
                    if (mStartActivity != null) {
                        mStartActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //create a pending intent so the application is restarted after System.exit(0) was called.
                        // We use an AlarmManager to call this intent in 100ms
                        int mPendingIntentId = 223344;
                        PendingIntent mPendingIntent = PendingIntent
                                .getActivity(c, mPendingIntentId, mStartActivity,
                                        PendingIntent.FLAG_CANCEL_CURRENT);
                        AlarmManager mgr = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
                        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
                        //kill the application
                        System.exit(0);
                    } else {
                        Crashlytics.log("Was not able to restart application, mStartActivity null");
                        Log.e(TAG, "Was not able to restart application, mStartActivity null");
                    }
                } else {
                    Crashlytics.log("Was not able to restart application, PM null");
                    Log.e(TAG, "Was not able to restart application, PM null");
                }
            } else {
                Crashlytics.log("Was not able to restart application, Context null");
                Log.e(TAG, "Was not able to restart application, Context null");
            }
        } catch (Exception ex) {
            Crashlytics.log("Was not able to restart application ");
            Crashlytics.logException(ex);
            Log.e(TAG, "Was not able to restart application");
        }
    }

    public static String youtubeUrlToVideoId(String videoUrl) {




        return videoUrl.replace("https://www.youtube.com/watch?v=", "")+"\"";
    }
}
