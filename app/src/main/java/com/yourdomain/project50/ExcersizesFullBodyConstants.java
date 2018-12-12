package com.yourdomain.project50;

import android.annotation.SuppressLint;

import com.yourdomain.project50.Model.Excesizes;

import java.util.HashMap;

/**
 * Created by apple on 11/13/18.
 */

public class ExcersizesFullBodyConstants {


    @SuppressLint("UseSparseArrays")
    private static HashMap<Integer, Excesizes> mDayToExsizeHashMap = new HashMap<>();
    private static String[] exercise_day1_titles = {"Jumping jacks day1","Jumping jacks day1 ex 2"
           };
    private static int[] exercise_day1_gif_icons = {R.drawable.simple,R.drawable.simple
           };
    private static int[] exercise_day1_times = {5,3
    };
    private static double[] exercise_day1_calories = {150,400
    };
    private static int[] getExercise_day1_view_type = {
            Excesizes.VIEW_TYPE_LIMTED_EXCERSIZE,Excesizes.VIEW_TYPE_LIMTED_EXCERSIZE
    };
    private static String[] exercise_day1_detail = {
            "A jumping jack  or star jump also called side-straddle hop in the US military,", "this is detail for ex 2"

    };
    private static int[] exercise_day1_couch_tips = {
            R.string.excersize_full_body_plan_couch_tips_day1_excerise1,R.string.excersize_full_body_plan_couch_tips_day1_excerise2

    };



    private static String[] exercise_defults_title = {
            "Jumping jacks"
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
    private static int[] exercises_defults_gif_icons = {
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
            ,R.drawable.simple
    ,R.drawable.simple};
    private static int[] exercises_defult_times = {
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
            ,40
    };
    private static double[] excersize_defult_calrious  = {
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
            ,400
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

    private static int[] excersizes_defult_couch_tips = {
            R.string.excersize_full_body_plan_couch_tip_defult,
            R.string.excersize_full_body_plan_couch_tip_defult,
            R.string.excersize_full_body_plan_couch_tip_defult,
            R.string.excersize_full_body_plan_couch_tip_defult,
            R.string.excersize_full_body_plan_couch_tip_defult,
            R.string.excersize_full_body_plan_couch_tip_defult,
            R.string.excersize_full_body_plan_couch_tip_defult,
            R.string.excersize_full_body_plan_couch_tip_defult,
            R.string.excersize_full_body_plan_couch_tip_defult,
            R.string.excersize_full_body_plan_couch_tip_defult,
            R.string.excersize_full_body_plan_couch_tip_defult,
            R.string.excersize_full_body_plan_couch_tip_defult,
            R.string.excersize_full_body_plan_couch_tip_defult,
            R.string.excersize_full_body_plan_couch_tip_defult

    };

    public static void fillUpdata() {
        Excesizes day1Excersizes = new Excesizes(exercise_day1_titles, exercise_day1_gif_icons, exercise_day1_times, exercise_day1_detail, getExercise_day1_view_type, exercise_day1_calories, exercise_day1_couch_tips);
        mDayToExsizeHashMap.put(1, day1Excersizes);

    }

    public static Excesizes getDay1ExcersizeKeys(int day) {

        fillUpdata();
        if (mDayToExsizeHashMap.containsKey(day)) {
            return mDayToExsizeHashMap.get(day);
        }
        return new Excesizes(exercise_defults_title, exercises_defults_gif_icons, exercises_defult_times, excersizes_defult_detail, getExcersize_view_type_defult,excersize_defult_calrious,excersizes_defult_couch_tips);
    }
}
