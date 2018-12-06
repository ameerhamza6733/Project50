package com.yourdomain.project50.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.yourdomain.project50.MY_Shared_PREF;
import com.yourdomain.project50.R;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by apple on 12/6/18.
 */

public class ReportsFragment extends Fragment {

    private DataPoint [] dataPoints;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       dataPoints= MY_Shared_PREF.Companion.getAllDataGraphCalvsDays(getActivity().getApplication());

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reports,container,false);
        GraphView graph = (GraphView) view.findViewById(R.id.graph);
        Calendar calendar = Calendar.getInstance();

        graph.getViewport().setScrollable(true); // enables horizontal scrolling
        graph.getViewport().setScrollableY(true); // enables vertical scrolling

        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(dataPoints);
        series.setSpacing(50);
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.RED);
        graph.addSeries(series);

// set date label formatter
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
        graph.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space

// set manual x bounds to have nice steps
       if (dataPoints.length>1){
           graph.getViewport().setMinX(dataPoints[0].getX());
           graph.getViewport().setMaxX(dataPoints[dataPoints.length-1].getX());
           graph.getViewport().setXAxisBoundsManual(true);
       }

// as we use dates as labels, the human rounding to nice readable numbers
// is not necessary
        graph.getGridLabelRenderer().setHumanRounding(false);
        return view;
    }
}
