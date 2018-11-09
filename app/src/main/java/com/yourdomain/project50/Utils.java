package com.yourdomain.project50;

import java.text.DecimalFormat;

/**
 * Created by apple on 11/5/18.
 */

public class Utils {
     DecimalFormat  df = new DecimalFormat("#.##");
    public  String CMtoFeet(double cm){
        return df.format(Math.round((cm/30.48) * 100D) / 100D);
    }

    public  String FeettoCM(double inch){
        return df.format(Math.round((inch*30.48) * 100D) / 100D);
    }

    public String KGtoLBS(double kg){
        return df.format(Math.round((kg*2.20 )* 100D) / 100D);
    }

    public String LBStoKG(double lbs){
        return df.format(Math.round((lbs/2.20 )* 100D) / 100D );
    }
}
