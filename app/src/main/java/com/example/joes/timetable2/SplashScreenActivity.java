package com.example.joes.timetable2;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
    public static Button TimeTableSubmitButton;

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

        //DownloadEverything();
        TempVal();


    }


    private void TempVal() {
        Utils.ListOfTimeTable.add(new TimeTable("21 Sept", "2018-03-19 13:45:00 +0800", "2018-03-19 15:45:00 +0800", "B - 7 -1", "CS"));
        Intent MainActivityIntent = new Intent(context, MainActivity.class);
        context.startActivity(MainActivityIntent);
    }
    private void initVar() {
        SplashScreenLinearLayout = (LinearLayout) findViewById(R.id.ll_splash_screen);
        IntakeScreenLinearLayout = (LinearLayout) findViewById(R.id.ll_intake_screen);
        LoadingScreenLinearLayout = (LinearLayout) findViewById(R.id.ll_loading_screen);

        TimeTableSubmitButton = (Button) findViewById(R.id.bt_timetable_submit);
        TimeTableListAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.actv_timetable_list);
    }

    private void DownloadEverything() {
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

        TimeTableSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HideKeyboard();
                IntakeScreenLinearLayout.setVisibility(View.GONE);
                LoadingScreenLinearLayout.setVisibility(View.VISIBLE);
                new NetworkActivity.GetTimeTableInfo().execute(TimeTableListAutoCompleteTextView.getText().toString());

            }
        });
    }

    private void HideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
