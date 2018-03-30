package com.example.joes.timetable2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.joes.timetable2.Settings.SettingsActivity;
import com.example.joes.timetable2.TimeTable.FragmentPager;
import com.example.joes.timetable2.TimeTable.RecyclerAdapter;
import com.example.joes.timetable2.TimeTable.TimeTableFragment;
import com.example.joes.timetable2.Utils.Utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {


    public RecyclerAdapter mAdapter;
    public static LinearLayout NoClassLinearLayout, LoadingScreenLinearLayout;
    public static RelativeLayout DisplayClassRelativeLayout;

    public static boolean INTAKE_CHANGED;

    public static String INTAKE_STATUS;


    public static void setIntakeStatus(String intakeStatus) {
        INTAKE_STATUS = intakeStatus;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        this.getSupportActionBar().show();
        //Provide Context
        TimeTableFragment.getContext(getApplicationContext());

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String value = sharedPreferences.getString("intakestatus", "");


        if (value.equals("SUCCESS")) {
            DisplayClassRelativeLayout.setVisibility(View.VISIBLE);
            NoClassLinearLayout.setVisibility(View.GONE);
        }
        else if (value.equals("FAILED")) {
            DisplayClassRelativeLayout.setVisibility(View.GONE);
            NoClassLinearLayout.setVisibility(View.VISIBLE);
        }

        Log.i("LOG", "change intake: " + INTAKE_CHANGED);
        if (INTAKE_CHANGED) {
            showSnackbar(getWindow().getDecorView().findViewById(android.R.id.content),"Intake changed successfully",Snackbar.LENGTH_LONG);
            INTAKE_CHANGED = false;
        }

        ViewPager viewPager = findViewById(R.id.viewpager);
        FragmentPager adapter = new FragmentPager(getSupportFragmentManager(), this);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(5);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(viewPager);

    }

    public void showSnackbar(View view, String message, int duration)
    {
        // Create snackbar
        final Snackbar snackbar = Snackbar.make(view, message, duration);


        // Set an action on it, and a handler
        snackbar.setAction("Refresh", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupSharedPreference();
            }
        });

        snackbar.show();
    }

    private void setupSharedPreference() {
        this.getSupportActionBar().hide();
        DisplayClassRelativeLayout.setVisibility(View.GONE);
        LoadingScreenLinearLayout.setVisibility(View.VISIBLE);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        new NetworkActivity.GetTimeTableInfo().execute(sharedPreferences.getString(getString(R.string.pref_timetable_key), getString(R.string.pref_timetable_default)));
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }



    private void init() {
        NoClassLinearLayout = (LinearLayout) findViewById(R.id.ll_no_class);
        LoadingScreenLinearLayout = (LinearLayout) findViewById(R.id.ll_loading_screen);
        DisplayClassRelativeLayout = (RelativeLayout) findViewById(R.id.rl_display_data);


    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int IDGathered = item.getItemId();
        if (IDGathered == R.id.action_settings) {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        } else if (IDGathered == R.id.action_refresh) {
            setupSharedPreference();
            Log.i("TAG", "STATUS: " + INTAKE_STATUS);
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        INTAKE_CHANGED = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister VisualizerActivity as an OnPreferenceChangedListener to avoid any memory leaks.
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }
}
