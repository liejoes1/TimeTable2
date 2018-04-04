package com.example.time.ontime.TimeTable;

import android.support.v4.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.time.ontime.R;
import com.example.time.ontime.Utils.Utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TimeTableFragment extends Fragment {
    private static Context context;
    private static String ARG_SECTION_NUMBER = "section_number";

    public List<TimeTable> dayOnly;

    public static void getContext(Context c) {
        context = c;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        dayOnly = new ArrayList<>();

        int SectionNumbers = getArguments().getInt(ARG_SECTION_NUMBER);

        switch (SectionNumbers) {
            case 0:
                dayOnly.addAll(Utils.MondayTimeTable);
                break;
            case 1:
                dayOnly.addAll(Utils.TuesdayTimeTable);
                break;
            case 2:
                dayOnly.addAll(Utils.WednesdayTimeTable);
                break;
            case 3:
                dayOnly.addAll(Utils.ThursdayTimeTable);
                break;
            case 4:
                dayOnly.addAll(Utils.FridayTimeTable);
                break;
        }
        View view = inflater.inflate(R.layout.fragment_timetable, container, false);

        try {

            if (!dayOnly.isEmpty()) {
                TextView dateTextView = view.findViewById(R.id.dateTextView);
                dateTextView.setText(new SimpleDateFormat("dd MMMM").format(new SimpleDateFormat("yyyy-MM-dd").parse(dayOnly.get(0).getDate())));
            }

        } catch (ParseException e) {
            e.printStackTrace();

        }

        RecyclerView timeTableRecyclerView = view.findViewById(R.id.rv_timetable);
        RecyclerAdapter mAdapter = new RecyclerAdapter(dayOnly, context);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);

        timeTableRecyclerView.setLayoutManager(mLayoutManager);
        timeTableRecyclerView.setHasFixedSize(true);
        timeTableRecyclerView.setAdapter(mAdapter);
        return view;
    }

    public static TimeTableFragment newInstance(int position) {

        TimeTableFragment blackFragment = new TimeTableFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, position);
        blackFragment.setArguments(args);
        return blackFragment;
    }
}
