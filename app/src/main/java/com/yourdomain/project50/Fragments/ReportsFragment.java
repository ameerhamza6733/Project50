package com.yourdomain.project50.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
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
import com.yourdomain.project50.MY_Shared_PREF;
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
    private double currentBMI=22.5;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM");

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataPoints = MY_Shared_PREF.Companion.getAllDataGraphCalvsDays(getActivity().getApplication());
        if (dataPoints.length == 0) {
            dataPoints = setUpDefultValue();
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reports, container, false);
        GraphView graph = (GraphView) view.findViewById(R.id.graph);
        bmiImageView=view.findViewById(R.id.imageBMI);
        btEditBMI=view.findViewById(R.id.btEditBmi);
        tvBmi=view.findViewById(R.id.tvBMI);

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
        tvBmi.setText("BMI : "+currentBMI);
        Glide.with(this).load(Utils.getDrawbleAccodingToBMI(currentBMI)).into(bmiImageView);
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.RED);
        series.setSpacing(50);


        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));


        if (dataPoints.length < 8) {
            graph.getGridLabelRenderer().setNumHorizontalLabels(dataPoints.length); // only 4 because of the space
            graph.getViewport().setMinX(dataPoints[0].getX());
            graph.getViewport().setMaxX(dataPoints[dataPoints.length - 1].getX());
            graph.getViewport().setXAxisBoundsManual(true);
            graph.getViewport().setScrollable(true);
        } else {
            graph.getGridLabelRenderer().setNumHorizontalLabels(7);
            graph.getViewport().setMinX(dataPoints[0].getX());
            graph.getViewport().setMaxX(dataPoints[7].getX());
            graph.getViewport().setXAxisBoundsManual(true);
            graph.getViewport().setScrollable(true);
        }


        graph.getGridLabelRenderer().setHumanRounding(false);

        graph.addSeries(series);


        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
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

        return view;
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
