package com.MONT3.partypoolapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        TextView tv = (TextView) findViewById(R.id.textView2);
        tv.setText("Welcome, " + getIntent().getStringExtra("PASSDATA"));
    }
}
