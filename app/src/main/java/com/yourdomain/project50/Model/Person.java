package com.yourdomain.project50.Model;

import com.google.gson.Gson;

import java.text.DecimalFormat;

/**
 * Created by apple on 11/20/18.
 */

public class Person {
    private double totleEXERCISES;
    private double totleKCalBurn;
    private double totleMintsDuration;

    public Person() {
        totleEXERCISES=0;
        totleKCalBurn=0;
        totleMintsDuration=0;
    }

    public double getTotleEXERCISES() {
        return totleEXERCISES;
    }

    public void setTotleEXERCISES(double totleEXERCISES) {
        this.totleEXERCISES = totleEXERCISES;
    }

    public double getTotleKCalBurn() {
        return totleKCalBurn;
    }

    public void setTotleKCalBurn(double totleKCalBurn) {
        this.totleKCalBurn = totleKCalBurn;
    }

    public double getTotleMintsDuration() {
        return totleMintsDuration;
    }

    public void setTotleMintsDuration(double totleMintsDuration) {
        this.totleMintsDuration = totleMintsDuration;
    }

}
