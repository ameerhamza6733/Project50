package com.yourdomain.project50.Model;

import android.graphics.drawable.Drawable;

/**
 * Created by apple on 11/13/18.
 */

public class Excesizes {
    private String [] title;
    private int [] icons;
    private int [] seconds;

    public Excesizes(String[] title, int[] icons, int[] seconds) {
        this.title = title;
        this.icons = icons;
        this.seconds = seconds;
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
}