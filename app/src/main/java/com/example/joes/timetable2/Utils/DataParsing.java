package com.example.joes.timetable2.Utils;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.example.joes.timetable2.TimeTable.TimeTable;
import com.example.joes.timetable2.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileInputStream;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;

/**
 * Created by Joes on 20/3/2018.
 */

public class DataParsing {

    public static void ParseTimeTableList(FileInputStream xmlFileName) {
        try {
            //Before storing clear it first
            Utils.ListOfAllIntake.clear();

            XmlToJson xmlToJson = new XmlToJson.Builder(xmlFileName, null).build();
            JSONObject RootObject = new JSONObject(xmlToJson.toString());
            JSONObject WeekOfObject = RootObject.getJSONObject("weekof");
            JSONArray IntakeArray = WeekOfObject.getJSONArray("intake");

            for (int CurrentIntakeIndex = 0; CurrentIntakeIndex < IntakeArray.length(); CurrentIntakeIndex++) {
                JSONObject EachIntake = IntakeArray.getJSONObject(CurrentIntakeIndex);
                Utils.ListOfAllIntake.add(CurrentIntakeIndex, EachIntake.getString("name"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayAdapter<String> getIntakeList(Context context) {
        return new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, Utils.ListOfAllIntake);
    }

    public static void ParseTimeTable(FileInputStream xmlFileName) {
        try {
            Utils.ListOfTimeTable.clear();

            XmlToJson xmlToJson = new XmlToJson.Builder(xmlFileName, null).build();
            JSONObject RootObject = new JSONObject(xmlToJson.toString());
            JSONObject WeekObject = RootObject.getJSONObject("week");
            JSONArray DayArray = WeekObject.getJSONArray("day");

            for (int DayIndex = 0; DayIndex < DayArray.length(); DayIndex++) {
                JSONObject DailyObject = DayArray.getJSONObject(DayIndex);

                if (DailyObject.toString().substring(0, 10).equals("{\"class\":[")) {
                    //More than one class for that day
                    JSONArray ClassArray = DailyObject.getJSONArray("class");
                    for (int DailyArrayIndex = 0; DailyArrayIndex < ClassArray.length(); DailyArrayIndex++) {
                        JSONObject SubjectObject = ClassArray.getJSONObject(DailyArrayIndex);
                        String Module = SubjectObject.getString("subject");
                        String Classroom = SubjectObject.getString("location");
                        String StartTime = SubjectObject.getString("start");
                        String EndTime = SubjectObject.getString("end");
                        String Date = StartTime.substring(0, 10);

                        Utils.ListOfTimeTable.add(new TimeTable(Date, StartTime, EndTime, Classroom, Module));
                    }
                } else if (DailyObject.toString().substring(0, 10).equals("{\"class\":{")) {
                    //Only One Class for that day
                    JSONObject ClassObject = DailyObject.getJSONObject("class");

                    String Module = ClassObject.getString("subject");
                    String Classroom = ClassObject.getString("location");
                    String StartTime = ClassObject.getString("start");
                    String EndTime = ClassObject.getString("end");
                    String Date = StartTime.substring(0, 10);

                    Utils.ListOfTimeTable.add(new TimeTable(Date, StartTime, EndTime, Classroom, Module));
                } else {
                    //No Class for that day
                    //Do Nothing, dont add.
                }
            }
            Utils.MondayTimeTable.clear();
            Utils.TuesdayTimeTable.clear();
            Utils.WednesdayTimeTable.clear();
            Utils.ThursdayTimeTable.clear();
            Utils.FridayTimeTable.clear();

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            for (int i = 0; i < Utils.ListOfTimeTable.size(); i++) {

                if (dateFormat.parse(Utils.ListOfTimeTable.get(i).getDate()).toString().substring(0, 3).equals("Mon")) {

                    Utils.MondayTimeTable.add((Utils.ListOfTimeTable.get(i )));

                } else if (dateFormat.parse(Utils.ListOfTimeTable.get(i).getDate()).toString().substring(0, 3).equals("Tue")) {

                    Utils.TuesdayTimeTable.add((Utils.ListOfTimeTable.get(i)));

                } else if (dateFormat.parse(Utils.ListOfTimeTable.get(i).getDate()).toString().substring(0, 3).equals("Wed")) {

                    Utils.WednesdayTimeTable.add((Utils.ListOfTimeTable.get(i)));

                } else if (dateFormat.parse(Utils.ListOfTimeTable.get(i).getDate()).toString().substring(0, 3).equals("Thu")) {

                    Utils.ThursdayTimeTable.add((Utils.ListOfTimeTable.get(i)));

                } else if (dateFormat.parse(Utils.ListOfTimeTable.get(i).getDate()).toString().substring(0, 3).equals("Fri")) {

                    Utils.FridayTimeTable.add((Utils.ListOfTimeTable.get(i)));

                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
