package com.example.joes.timetable2.TimeTable;

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

import com.example.joes.timetable2.MainActivity;
import com.example.joes.timetable2.R;
import com.example.joes.timetable2.Utils.Utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TimeTableFragment extends Fragment {
    private static Context context;
    private static int CURRENT_POSITION;
    private static final String ARG_SECTION_NUMBER = "section_number";

    public static RecyclerView TimeTableRecyclerView;
    public RecyclerAdapter mAdapter;
    public static TextView DateTextView;

    public List<TimeTable> dayOnly;

    public static void getContext(Context c) {
        context = c;
    }
    public static void getPosition(int position) {
        CURRENT_POSITION = position;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        dayOnly = new ArrayList<>();
        Bundle args = getArguments();
        int SectionNumbers = args.getInt(ARG_SECTION_NUMBER);
        Log.i("TAG","Section Number: " + SectionNumbers);

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
        Log.i("Danke", "Title: " + dayOnly.get(0).getDate());
        View view = inflater.inflate(R.layout.fragment_timetable, null);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM");
        Date date = null;
        try {
             date = df.parse(dayOnly.get(0).getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateTextView = view.findViewById(R.id.dateTextView);
            DateTextView.setText(sdf.format(date));

        TimeTableRecyclerView = view.findViewById(R.id.rv_timetable);
        mAdapter = new RecyclerAdapter(dayOnly, context);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        TimeTableRecyclerView.setLayoutManager(mLayoutManager);
        TimeTableRecyclerView.setHasFixedSize(true);
        TimeTableRecyclerView.setAdapter(mAdapter);
        return view;
    }

    public TimeTableFragment() {

    }

    public static TimeTableFragment newInstance(int position) {


        TimeTableFragment blackFragment = new TimeTableFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, position);
        blackFragment.setArguments(args);
        return blackFragment;
    }
}
