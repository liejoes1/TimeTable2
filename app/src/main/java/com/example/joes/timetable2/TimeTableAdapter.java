package com.example.joes.timetable2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Joes on 17/3/2018.
 */

public class TimeTableAdapter extends RecyclerView.Adapter<TimeTableAdapter.TimeTableHolder> {

    private Context context;
    private List<TimeTable> timetableList;


    public TimeTableAdapter(Context context, List<TimeTable> timetableList) {
        this.context = context;
        this.timetableList = timetableList;
    }

    @NonNull
    @Override
    public TimeTableHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.timetable_list, parent, false);
        return new TimeTableHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeTableHolder holder, int position) {
        TimeTable timetable = timetableList.get(position);

        holder.DateTextView.setText(timetable.getDate());
        holder.TitleTextView.setText(timetable.getModule());
        holder.RoomTextView.setText(timetable.getLocation());
        String StartTime = timetable.getStartTime().substring(11,16);
        String EndTime = timetable.getEndTime().substring(11,16);
        holder.time.setText(StartTime + " - " + EndTime);
    }

    @Override
    public int getItemCount() {
        return timetableList.size();
    }

    class TimeTableHolder extends RecyclerView.ViewHolder {

        TextView DateTextView, TitleTextView, RoomTextView, LocationTextView, time;


        public TimeTableHolder(View itemView) {
            super(itemView);

            DateTextView = itemView.findViewById(R.id.date);
            TitleTextView = itemView.findViewById(R.id.course_title);
            RoomTextView = itemView.findViewById(R.id.course_room);
            time = itemView.findViewById(R.id.time);
        }
    }

}