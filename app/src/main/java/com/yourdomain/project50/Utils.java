package com.yourdomain.project50;

import android.util.Log;

import com.yourdomain.project50.Model.Excesizes;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

/**
 * Created by apple on 11/5/18.
 */

public class Utils {

    private static String TAG="UtilsTAG";
   static DecimalFormat df = new DecimalFormat("#.##");

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

    public static String CountTotalTime(int [] viewType,int[] seconds) {
        int total = 0;
        for (int i=0;i<seconds.length ;i++) {
            if (viewType[i]== Excesizes.VIEW_TYPE_UN_LIMTED_EXCERSIZE)
                continue;
            total = total + seconds[i];
        }
        Log.d(TAG,"total seconds for currnet excersize"+TimeUnit.SECONDS.toMinutes(total));
        return  df.format(Math.round((total/60D)));
    }
public static String toPersentage(int done , int totle){
    return String.valueOf(((done * 100 / totle)))+"%";
}
}
