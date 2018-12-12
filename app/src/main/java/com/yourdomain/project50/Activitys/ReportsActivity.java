package com.yourdomain.project50.Activitys;

/**
 * Created by apple on 11/29/18.
 */

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.yourdomain.project50.Fragments.ReportsFragment;
import com.yourdomain.project50.R;


public class ReportsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_column_chart);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, new ReportsFragment()).commit();
        }
    }


}