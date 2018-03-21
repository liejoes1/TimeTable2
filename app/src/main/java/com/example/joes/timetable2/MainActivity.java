package com.example.joes.timetable2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

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

    private void init() {
        TimeTableRecyclerView = (RecyclerView) findViewById(R.id.rv_timetable);
    }

}
