package com.example.joes.timetable2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.joes.timetable2.TimeTable.TimeTable;
import com.example.joes.timetable2.Utils.DataParsing;
import com.example.joes.timetable2.Utils.Utils;

import java.io.File;
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

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPreferences.getString(getString(R.string.pref_timetable_key), getString(R.string.pref_timetable_default)).equals("null")) {
            //Means that it is not yet defined, first startup
            DownloadEverything();
        } else {
            //If already defined earlier, just parse the data, dont downlaod to save time


            try {
                DataParsing.ParseTimeTableList(new FileInputStream(new File(NetworkActivity.ROOT_DIRECTORY_PATH, "TimeTableList.xml")));
                DataParsing.ParseTimeTable(new FileInputStream(new File(NetworkActivity.ROOT_DIRECTORY_PATH, "TimeTable.xml")));

                SplashScreenLinearLayout.setVisibility(View.VISIBLE);
                new CountDownTimer(1000, 3000) {

                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {

                        Intent MainActivityIntent = new Intent(context, MainActivity.class);
                        context.startActivity(MainActivityIntent);
                    }

                }.start();


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


        }



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

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        TimeTableSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HideKeyboard();
                for (int TotalIntake = 0; TotalIntake < Utils.ListOfAllIntake.size(); TotalIntake++) {
                    if (TimeTableListAutoCompleteTextView.getText().toString().toUpperCase().equals(Utils.ListOfAllIntake.get(TotalIntake))) {

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(getString(R.string.pref_timetable_key), TimeTableListAutoCompleteTextView.getText().toString());
                        editor.apply();
                        IntakeScreenLinearLayout.setVisibility(View.GONE);
                        LoadingScreenLinearLayout.setVisibility(View.VISIBLE);
                        new NetworkActivity.GetTimeTableInfo().execute(TimeTableListAutoCompleteTextView.getText().toString());
                    }
                }
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),"Intake is invalid, Please try again.",Snackbar.LENGTH_SHORT).show();
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
