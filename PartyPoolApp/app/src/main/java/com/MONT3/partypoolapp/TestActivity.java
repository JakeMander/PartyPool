package com.MONT3.partypoolapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

public class TestActivity extends AppCompatActivity{
private Button buttonJoin;
private Button buttonCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        TextView tv = findViewById(R.id.textView2);
        tv.setText("Welcome, " + getIntent().getStringExtra("PASSDATA"));
        buttonJoin = findViewById(R.id.button2);
        buttonCreate = findViewById(R.id.button3);

        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreateDialog();
            }
        });

        buttonJoin.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View v){
                openJoinDialog();
            }
        });
    }
    public void openCreateDialog()
    {
        CreateDialog dialog1 = new CreateDialog();
        dialog1.show(getSupportFragmentManager(),"create dialog");
    }

    public void openJoinDialog()
    {
        JoinDialog dialog1 = new JoinDialog();
        dialog1.show(getSupportFragmentManager(),"join dialog");
    }

}
