package com.example.joes.timetable2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    public RecyclerView TimeTableRecyclerView;
    public TimeTableAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        mAdapter = new TimeTableAdapter(this, Utils.ListOfTimeTable);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        TimeTableRecyclerView.setLayoutManager(mLayoutManager);
        TimeTableRecyclerView.setHasFixedSize(true);
        TimeTableRecyclerView.setAdapter(mAdapter);

    }

    private void setupSharedPreference() {
        Intent SplashScreenIntent = new Intent(this, SplashScreenActivity.class);
        startActivity(SplashScreenIntent);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        new NetworkActivity.GetTimeTableInfo().execute(sharedPreferences.getString(getString(R.string.pref_timetable_key), getString(R.string.pref_timetable_default)));
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }



    private void init() {
        TimeTableRecyclerView = (RecyclerView) findViewById(R.id.rv_timetable);
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
        if (s.equals(getString(R.string.pref_timetable_key))) {
            setupSharedPreference();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister VisualizerActivity as an OnPreferenceChangedListener to avoid any memory leaks.
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }
}
