package com.yourdomain.project50;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;

import com.yourdomain.project50.Model.Excesizes;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by apple on 11/13/18.
 */

public class ExcersizesConstants {


    @SuppressLint("UseSparseArrays")
    private static HashMap<Integer,Excesizes> mDayToExsizeHashMap =  new HashMap<>();

    public  static void  fillUpdata(){
        Excesizes day1Excersizes = new Excesizes(excersizes_day1_title,excersizes_day1_gif_icons,excersize_day1_times);
        mDayToExsizeHashMap.put(1,day1Excersizes);

    }

    public static Excesizes getDay1ExcersizeKeys(int day) {
        fillUpdata();
        if (mDayToExsizeHashMap.containsKey(day)){
            return mDayToExsizeHashMap.get(day);
        }
        return  new Excesizes(excersizes_defults_title,excersizes_defults_gif_icons,excersize_defult_times);
    }




    private static String [] excersizes_day1_title ={"Jumping jacks day1"
            ,"high stepping"
            ,"standing bicycle crunches"
            ,"cross arm crunches"};
    private static int[] excersizes_day1_gif_icons={R.drawable.simple
            ,R.drawable.simple
            ,R.drawable.simple
            ,R.drawable.simple};

    private static int [] excersize_day1_times={15,
            40,
            60,
            40,
    };


    private static String [] excersizes_defults_title ={"Jumping jacks"
            ,"high stepping"
            ,"standing bicycle crunches"
            ,"cross arm crunches"};
    private static int[] excersizes_defults_gif_icons={R.drawable.simple
            ,R.drawable.simple
            ,R.drawable.simple
            ,R.drawable.simple};

    private static int [] excersize_defult_times={10,
        20,
        30,
        40,
    };
}
