package com.yourdomain.project50.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.yourdomain.project50.MY_Shared_PREF;
import com.yourdomain.project50.R;
import com.yourdomain.project50.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by apple on 12/6/18.
 */

public class ReportsFragment extends Fragment {

    private DataPoint [] dataPoints;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM");
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       dataPoints= new DataPoint[] {
               new DataPoint(Utils.getNextDay(), 1),
               new DataPoint(Utils.getNextDay(), 5),
               new DataPoint(Utils.getNextDay(), 3),
               new DataPoint(Utils.getNextDay(), 2),
               new DataPoint(Utils.getNextDay(), 6),
               new DataPoint(Utils.getNext30Day(), 70)
       };

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reports,container,false);
        GraphView graph = (GraphView) view.findViewById(R.id.graph);
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
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(d1, 1),
                new DataPoint(d2, 5),
                new DataPoint(d3, 3),
                new DataPoint(d4,7)
        });
series.setSpacing(50);
        graph.addSeries(series);

// set date label formatter
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
        graph.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space

// set manual x bounds to have nice steps
        graph.getViewport().setMinX(d1.getTime());
        graph.getViewport().setMaxX(d3.getTime());
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setScrollable(true);

// as we use dates as labels, the human rounding to nice readable numbers
// is not necessary
        graph.getGridLabelRenderer().setHumanRounding(false);
// draw values on top

        return view;
    }
}
