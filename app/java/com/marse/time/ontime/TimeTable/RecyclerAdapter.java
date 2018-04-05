package com.marse.time.ontime.TimeTable;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.TextView;


import com.example.time.ontime.R;

import java.util.List;

/**
 * Created by Joes on 17/3/2018.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.TimeTableHolder> {

    private Context context;
    private List<TimeTable> timetableList;


    RecyclerAdapter(List<TimeTable> timetableList, Context context) {
        this.context = context;
        this.timetableList = timetableList;
    }

    @NonNull
    @Override
    public TimeTableHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TimeTableHolder(LayoutInflater.from(context).inflate(R.layout.timetable_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TimeTableHolder holder, int position) {
        TimeTable timetable = timetableList.get(position);

        holder.TitleTextView.setText(timetable.getModule());
        holder.RoomTextView.setText(timetable.getLocation());
        String StartTime = timetable.getStartTime().substring(11,16);
        String EndTime = timetable.getEndTime().substring(11,16);
        holder.time.setText(String.format("%s - %s", StartTime, EndTime));
    }

    @Override
    public int getItemCount() {
        return timetableList.size();
    }

    class TimeTableHolder extends RecyclerView.ViewHolder {

        TextView TitleTextView, RoomTextView, time;


        TimeTableHolder(View itemView) {
            super(itemView);
            TitleTextView = itemView.findViewById(R.id.course_title);
            RoomTextView = itemView.findViewById(R.id.course_room);
            time = itemView.findViewById(R.id.time);
        }
    }

}