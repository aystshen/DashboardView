package com.ayst.example.dashboardview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ayst.view.DashboardView;

public class MainActivity extends AppCompatActivity {

    private DashboardView mDashboardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDashboardView = (DashboardView) findViewById(R.id.dashboardview_1);
        mDashboardView.setValue(50); // Set value
        mDashboardView.resetValue(50); // Set value and clear maximum and minimum
    }
}
