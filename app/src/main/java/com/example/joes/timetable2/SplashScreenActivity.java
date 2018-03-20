package com.example.joes.timetable2;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class SplashScreenActivity extends AppCompatActivity {

    //Layout Declaration
    public static LinearLayout SplashScreenLinearLayout, IntakeScreenLinearLayout, LoadingScreenLinearLayout;
    public static Context context;
    public static AutoCompleteTextView TimeTableListAutoCompleteTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        initVar();

        //Provide Context
        NetworkActivity.setContext(this);
        context = this;
        // If Data is not exist - Initial Startup Only
        SplashScreenLinearLayout.setVisibility(View.VISIBLE);

        DownloadEverything();

    }

    private void initVar() {
        SplashScreenLinearLayout = (LinearLayout) findViewById(R.id.ll_splash_screen);
        IntakeScreenLinearLayout = (LinearLayout) findViewById(R.id.ll_intake_screen);
        LoadingScreenLinearLayout = (LinearLayout) findViewById(R.id.ll_loading_screen);

        TimeTableListAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.actv_timetable_list);
    }

    private static void DownloadEverything() {
        //This method will download all the files needed
        //It only used for Initial Welcome Screen
        //Or user force refresh ir

        //Set Download Mode


        //Download Timetable List
        new NetworkActivity.GetTimeTableList().execute();
        //Result: File is Downloaded ready to be processed

        //Now the data is inside the Object inside Util
        TimeTableListAutoCompleteTextView.setAdapter(DataParsing.getIntakeList(context));
        SplashScreenLinearLayout.setVisibility(View.GONE);
        IntakeScreenLinearLayout.setVisibility(View.VISIBLE);



    }

}
