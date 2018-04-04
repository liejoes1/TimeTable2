package com.example.time.ontime;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;


import com.example.joes.timetable2.R;
import com.example.time.ontime.Utils.DataParsing;
import com.example.time.ontime.Utils.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class SplashScreenActivity extends AppCompatActivity {

    //Layout Declaration
    public LinearLayout SplashScreenLinearLayout, IntakeScreenLinearLayout, LoadingScreenLinearLayout;
    public Context context;
    public AutoCompleteTextView TimeTableListAutoCompleteTextView;
    public Button TimeTableSubmitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //This is where the program starts
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        init();

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
            try {
                DataParsing.ParseTimeTableList(new FileInputStream(new File(NetworkActivity.ROOT_DIRECTORY_PATH, "TimeTableList.xml")));
                DataParsing.ParseTimeTable(new FileInputStream(new File(NetworkActivity.ROOT_DIRECTORY_PATH, "TimeTable.xml")));
                //When done parsing go to main activity now
                Intent MainActivityIntent = new Intent(context, MainActivity.class);
                context.startActivity(MainActivityIntent);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    private void init() {
        SplashScreenLinearLayout = findViewById(R.id.ll_splash_screen);
        IntakeScreenLinearLayout = findViewById(R.id.ll_intake_screen);
        LoadingScreenLinearLayout = findViewById(R.id.ll_loading_screen);

        TimeTableSubmitButton = findViewById(R.id.bt_timetable_submit);
        TimeTableListAutoCompleteTextView = findViewById(R.id.actv_timetable_list);
    }

    private void DownloadEverything() {
        //This method will download all the files needed
        //Only when user's first install app

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
                Boolean INTAKE_NAME_CHECK = false;
                HideKeyboard();
                for (int TotalIntake = 0; TotalIntake < Utils.ListOfAllIntake.size(); TotalIntake++) {
                    if (TimeTableListAutoCompleteTextView.getText().toString().toUpperCase().equals(Utils.ListOfAllIntake.get(TotalIntake))) {
                        INTAKE_NAME_CHECK = true;
                        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(getString(R.string.pref_timetable_key), TimeTableListAutoCompleteTextView.getText().toString()).apply();

                        IntakeScreenLinearLayout.setVisibility(View.GONE);
                        LoadingScreenLinearLayout.setVisibility(View.VISIBLE);
                        new NetworkActivity.GetTimeTableInfo().execute(TimeTableListAutoCompleteTextView.getText().toString());
                    }
                }
                if (!INTAKE_NAME_CHECK) {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Intake is invalid, Please try again.", Snackbar.LENGTH_SHORT).show();
                }

            }

        });
    }

    private void HideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
