package com.yourdomain.project50.DataBaseConstants;

import android.annotation.SuppressLint;

import com.yourdomain.project50.Model.Excesizes;
import com.yourdomain.project50.R;

import java.util.HashMap;

/**
 * Created by hamza rafiq on 11/13/18.
 */

public class ExcersizesABSPlanConstants {


    @SuppressLint("UseSparseArrays")
    private static HashMap<Integer, Excesizes> mDayToExsizeHashMap = new HashMap<>();
    private static String[] excersizes_day1_title = {"Jumping jacks day1"
            , "high stepping"
            , "standing bicycle crunches"
            , "cross arm crunches"};
    private static int[] excersizes_day1_gif_icons = {R.drawable.simple
            , R.drawable.simple
            , R.drawable.simple
            , R.drawable.simple};
    private static int[] excersize_day1_times = {15,
            10,
            60,
            10
    };

    private static double[] excersize_day1_calories = {150,
            400,
            600,
            470
    };

    private static int[] getExcersize_day1_view_typet = {
            Excesizes.VIEW_TYPE_LIMTED_EXCERSIZE,
            Excesizes.VIEW_TYPE_LIMTED_EXCERSIZE,
            Excesizes.VIEW_TYPE_UN_LIMTED_EXCERSIZE,
            Excesizes.VIEW_TYPE_LIMTED_EXCERSIZE
    };
    private static String[] excersizes_day1_detail = {
            "A jumping jack  or star jump also called side-straddle hop in the US military,",
            "moving with a high step a high-stepping horse.",
            "Stand tall with your feet shoulder-width and your toes pointing forward. Place your fingertips behind the neck with your elbows pointing out to your sides in line with your shoulders",
            ": Crossed Arm Crunch. Crossed arm crunch is a beginner friendly bodyweight crunch exercise that works your"
    };
    private static String[] excersizes_day1_couch_tips = {
            "this is couch tips day 1 exercise 1",
            "this is couch tips day 1 exercise 2",
            "this is couch tips day 1 exercise 3",
            "this is couch tips day 1 exercise 4"

    };


    private static String[] excersizes_defults_title = {
            "ABS Jumping jacks"
            , "High stepping"
            , "Standing bicycle crunches"
            , "Cross arm crunches"
            , "Super man"
            , "Reverse crunches"
            , "Leg raises"
            , "Skipping without rope"
            , "Standing bicycle crunches"
            , "Cross arm crunches"
            , "Super man "
            , "Plank"
            , "Standing side bend"
            , "Child's pose"};
    private static int[] excersizes_defults_gif_icons = {
            R.drawable.simple
            , R.drawable.simple
            , R.drawable.simple
            , R.drawable.simple
            , R.drawable.simple
            , R.drawable.simple
            , R.drawable.simple
            , R.drawable.simple
            , R.drawable.simple
            , R.drawable.simple
            , R.drawable.simple
            , R.drawable.simple
            , R.drawable.simple
            , R.drawable.simple};
    private static int[] excersize_defult_times = {
            10,
            20,
            30,
            40,
            12,
            12,
            10,
            40,
            10,
            10,
            40,
            40,
            40
            , 40
    };

    private static double[] excersize_defult_calrious = {
            100,
            290,
            300,
            408,
            128,
            128,
            106,
            404,
            106,
            100,
            403,
            406,
            408
            , 400
    };
    private static String[] excersizes_defult_couch_tips = {
            "",
            "",
            "",
            "",
           "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""

    };
    private static int[] getExcersize_view_type_defult = {
            Excesizes.VIEW_TYPE_LIMTED_EXCERSIZE,
            Excesizes.VIEW_TYPE_LIMTED_EXCERSIZE,
            Excesizes.VIEW_TYPE_UN_LIMTED_EXCERSIZE,
            Excesizes.VIEW_TYPE_LIMTED_EXCERSIZE,
            Excesizes.VIEW_TYPE_UN_LIMTED_EXCERSIZE,
            Excesizes.VIEW_TYPE_UN_LIMTED_EXCERSIZE,
            Excesizes.VIEW_TYPE_UN_LIMTED_EXCERSIZE,
            Excesizes.VIEW_TYPE_UN_LIMTED_EXCERSIZE,
            Excesizes.VIEW_TYPE_UN_LIMTED_EXCERSIZE,
            Excesizes.VIEW_TYPE_UN_LIMTED_EXCERSIZE,
            Excesizes.VIEW_TYPE_UN_LIMTED_EXCERSIZE,
            Excesizes.VIEW_TYPE_LIMTED_EXCERSIZE,
            Excesizes.VIEW_TYPE_LIMTED_EXCERSIZE,
            Excesizes.VIEW_TYPE_LIMTED_EXCERSIZE
    };
    private static String[] excersizes_defult_detail = {
            "A jumping jack  or star jump also called side-straddle hop in the US military,"
            , "moving with a high step a high-stepping horse."
            , "Stand tall with your feet shoulder-width and your toes pointing forward. Place your fingertips behind the neck with your elbows pointing out to your sides in line with your shoulders"
            , ": Crossed Arm Crunch. Crossed arm crunch is a beginner friendly bodyweight crunch exercise that works your"
            , "supter man description "
            , "Reverse crunches"
            , "Leg raises description"
            , "Skipping without rope description"
            , "Standing bicycle crunches description"
            , "Cross arm crunches description"
            , "Super man description"
            , "Plank description"
            , "Standing bend description"
            , "Child's pose description"
    };

    private static String[] excersize_defult_video_links = {
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
            , ""
    };

    public static void fillUpdata() {
        Excesizes day1Excersizes = new Excesizes(excersizes_day1_title, excersizes_day1_gif_icons, excersize_day1_times, excersizes_day1_detail, getExcersize_day1_view_typet, excersize_day1_calories, excersizes_day1_couch_tips);
        mDayToExsizeHashMap.put(1, day1Excersizes);

    }

    public static Excesizes getExcersizeByDay(int day) {

        fillUpdata();
        if (mDayToExsizeHashMap.containsKey(day)) {
            return mDayToExsizeHashMap.get(day);
        }
        return new Excesizes(excersizes_defults_title, excersizes_defults_gif_icons, excersize_defult_times, excersizes_defult_detail, getExcersize_view_type_defult, excersize_defult_calrious, excersizes_defult_couch_tips);
    }
}
