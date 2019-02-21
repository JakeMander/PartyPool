package com.MONT3.partypoolapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TestActivity extends AppCompatActivity {
private Button buttonJoin;
private Button buttonCreate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        TextView tv = (TextView) findViewById(R.id.textView2);
        tv.setText("Welcome, " + getIntent().getStringExtra("PASSDATA"));
        buttonJoin = (Button) findViewById(R.id.button2);
        buttonCreate = (Button) findViewById(R.id.button3);

        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
    }
    public void openDialog()
    {
        CreateDialog dialog1 = new CreateDialog();
        dialog1.show(getSupportFragmentManager(),"create dialog");
    }
}
