package com.yourdomain.project50.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.yourdomain.project50.MY_Shared_PREF;
import com.yourdomain.project50.Model.Person;
import com.yourdomain.project50.Model.PersonAppearance;
import com.yourdomain.project50.R;
import com.yourdomain.project50.Utils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by hamza rafiq on 12/6/18.
 */

public class ReportsFragment extends Fragment {

    private final String TAG = "ReportsFragmentTAG";
    Calendar calendar = Calendar.getInstance();
    private DataPoint[] dataPoints;
    private ImageView bmiImageView;
    private TextView btEditBMI ;
    private TextView tvBmi;
    private double currentBMI=0;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM");
    private GraphView calGraph;
    private GraphView waightGraph;
    private DataPoint[] waightDataPoint;
    private Person person;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataPoints = MY_Shared_PREF.Companion.getAllDataGraphCalvsDays(getActivity().getApplication());
      person=MY_Shared_PREF.Companion.getPerson(getActivity().getApplication());
        waightDataPoint=MY_Shared_PREF.Companion.getPersonHistory(getActivity().getApplication());
        if (dataPoints.length == 0) {
            dataPoints = setUpDefultValue();
        }
        if (waightDataPoint.length==0){
            waightDataPoint=setUpDefultValue();
        }

        Log.d(TAG,"weight graph points: "+waightDataPoint.toString());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reports, container, false);
        calGraph = (GraphView) view.findViewById(R.id.graph);
        waightGraph=view.findViewById(R.id.waightGraph);
        bmiImageView=view.findViewById(R.id.imageBMI);
        btEditBMI=view.findViewById(R.id.btEditBmi);
        tvBmi=view.findViewById(R.id.tvBMI);
        updateCalGraph();
        updateWeightGraph();
        return view;
    }
    private void updateWeightGraph(){

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(waightDataPoint);
        waightGraph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));


        if (waightDataPoint.length < 8) {
            waightGraph.getGridLabelRenderer().setNumHorizontalLabels(waightDataPoint.length); // only 4 because of the space
            waightGraph.getViewport().setMinX(waightDataPoint[0].getX());
            waightGraph.getViewport().setMaxX(waightDataPoint[waightDataPoint.length - 1].getX());
            waightGraph.getViewport().setXAxisBoundsManual(true);
            waightGraph.getViewport().setScrollable(true);
        } else {
            waightGraph.getGridLabelRenderer().setNumHorizontalLabels(7);
            waightGraph.getViewport().setMinX(waightDataPoint[0].getX());
            waightGraph.getViewport().setMaxX(waightDataPoint[7].getX());
            waightGraph.getViewport().setXAxisBoundsManual(true);
            waightGraph.getViewport().setScrollable(true);
        }


        waightGraph.getGridLabelRenderer().setHumanRounding(false);

        waightGraph.addSeries(series);

        waightGraph.setTitle("Weight");
        waightGraph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    // show normal x values
                    return simpleDateFormat.format(new Date((long) value));
                } else {
                    // show currency for y values
                    return new DecimalFormat("#").format(value);
                }
            }
        });
    }
private void updateCalGraph(){
    BarGraphSeries<DataPoint> series = new BarGraphSeries<>(dataPoints);

    series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
        @Override
        public int get(DataPoint data) {
            return Color.rgb((int) data.getX() * 255 / 4, (int) Math.abs(data.getY() * 255 / 6), 100);
        }
    });
    btEditBMI.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            EditBMIDialogeFragment editBMIDialogeFragment = new EditBMIDialogeFragment();
            editBMIDialogeFragment.setStyle(DialogFragment.STYLE_NORMAL,R.style.dialog);
            editBMIDialogeFragment.show(getChildFragmentManager(),"editBMIDialogeFragment");
        }
    });
    if (PersonAppearance.Companion.getTYPE_CM_KG()==person.getPersonAppearance().getSCALE_TYPE()){
        currentBMI=Utils.calculateBMIinKg(person.getPersonAppearance().getMWaight(),Utils.CMtoM(person.getPersonAppearance().getMHight()));

    }else {
        currentBMI=Utils.calcautleBMIinlbs(person.getPersonAppearance().getMWaight(),Utils.FeetToInch(person.getPersonAppearance().getMHight()));
    }

    tvBmi.setText("BMI : "+ String.format("%.1f",currentBMI));
    Glide.with(this).load(Utils.getDrawbleAccodingToBMI(currentBMI)).into(bmiImageView);
    series.setDrawValuesOnTop(true);
    series.setValuesOnTopColor(Color.RED);
    series.setSpacing(50);


    calGraph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));


    if (dataPoints.length < 8) {
        calGraph.getGridLabelRenderer().setNumHorizontalLabels(dataPoints.length); // only 4 because of the space
        calGraph.getViewport().setMinX(dataPoints[0].getX());
        calGraph.getViewport().setMaxX(dataPoints[dataPoints.length - 1].getX());
        calGraph.getViewport().setXAxisBoundsManual(true);
        calGraph.getViewport().setScrollable(true);
    } else {
        calGraph.getGridLabelRenderer().setNumHorizontalLabels(7);
        calGraph.getViewport().setMinX(dataPoints[0].getX());
        calGraph.getViewport().setMaxX(dataPoints[7].getX());
        calGraph.getViewport().setXAxisBoundsManual(true);
        calGraph.getViewport().setScrollable(true);
    }


    calGraph.getGridLabelRenderer().setHumanRounding(false);

    calGraph.addSeries(series);


    calGraph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
        @Override
        public String formatLabel(double value, boolean isValueX) {
            if (isValueX) {
                // show normal x values
                return simpleDateFormat.format(new Date((long) value));
            } else {
                // show currency for y values
                return new DecimalFormat("#").format(value);
            }
        }
    });
}
    private DataPoint[] setUpDefultValue() {
        Calendar calendar = Calendar.getInstance();
        Date d1 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d2 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d3 = calendar.getTime();

        calendar.add(Calendar.DATE, 1);
        Date d4 = calendar.getTime();

        calendar.add(Calendar.DATE, 1);
        Date d5 = calendar.getTime();

        calendar.add(Calendar.DATE, 1);
        Date d6 = calendar.getTime();
        DataPoint[] dataPoints = new DataPoint[]{
                new DataPoint(d1, 0),
                new DataPoint(d2, 0),
                new DataPoint(d3, 0),
                new DataPoint(d4, 0),
                new DataPoint(d5, 0),
                new DataPoint(d6, 0)
        };
        return dataPoints;
    }
}
