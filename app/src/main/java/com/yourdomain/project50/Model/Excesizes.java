package com.yourdomain.project50.Model;

import android.graphics.drawable.Drawable;

/**
 * Created by apple on 11/13/18.
 */

public class Excesizes {
    private String [] title;
    private int [] icons;
    private int [] seconds;
    private String[] detail;
    private int[] viewType;
    private double [] calories;
    private String []videosLinks;
    private int [] couchTips;

    public Excesizes(String[] title, int[] icons, int[] seconds,String[] detail,int [] viewType,double []calories,int[] couchTips,String [] videosLinks) {
        this.title = title;
        this.icons = icons;
        this.seconds = seconds;
        this.detail=detail;
        this.viewType=viewType;
        this.calories=calories;
        this.couchTips=couchTips;
        this.videosLinks=videosLinks;
    }

    public Excesizes(String[] title, int[] icons, int[] seconds,String[] detail,int [] viewType,double []calories,int[] couchTips) {
        this.title = title;
        this.icons = icons;
        this.seconds = seconds;
        this.detail=detail;
        this.viewType=viewType;
        this.calories=calories;
        this.couchTips=couchTips;
    }

    public int[] getViewType() {
        return viewType;
    }

    public String[] getTitle() {
        return title;
    }

    public int[] getIcons() {
        return icons;
    }

    public int[] getSeconds() {
        return seconds;
    }

    public String[] getDetail() {
        return detail;
    }

    public double[] getCalories() {
        return calories;
    }

    public int[] getCouchTips() {
        return couchTips;
    }

    public String[] getVideosLinks() {
        return videosLinks;
    }

    public static final int VIEW_TYPE_LIMTED_EXCERSIZE=0;
    public static final int VIEW_TYPE_UN_LIMTED_EXCERSIZE=1;
    public static final int VIEW_TYPE_REST=2;
}
