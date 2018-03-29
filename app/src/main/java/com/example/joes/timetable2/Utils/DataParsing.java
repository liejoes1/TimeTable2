package com.example.joes.timetable2.Utils;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.example.joes.timetable2.TimeTable.TimeTable;
import com.example.joes.timetable2.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
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
            XmlToJson xmlToJson = new XmlToJson.Builder(xmlFileName, null).build();
            JSONObject RootObject = new JSONObject(xmlToJson.toString());
            JSONObject WeekObject = RootObject.getJSONObject("week");
            JSONArray DayArray = WeekObject.getJSONArray("day");
            Log.i("LOG", "Parse Timetable: " + DayArray);

            for (int DayIndex = 0; DayIndex < DayArray.length(); DayIndex++) {
                JSONObject DailyObject = DayArray.getJSONObject(DayIndex);
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
            }
            Utils.MondayTimeTable.clear();
            Utils.TuesdayTimeTable.clear();
            Utils.WednesdayTimeTable.clear();
            Utils.ThursdayTimeTable.clear();
            Utils.FridayTimeTable.clear();

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String tempDate = dateFormat.parse(Utils.ListOfTimeTable.get(0).getDate()).toString().substring(0, 4);
            Log.i("CUCK", "WELCOME" + tempDate);
            for (int i = 0; i < Utils.ListOfTimeTable.size(); i++) {

                if (dateFormat.parse(Utils.ListOfTimeTable.get(i).getDate()).toString().substring(0, 3).equals("Mon")){

                    Utils.MondayTimeTable.add ((Utils.ListOfTimeTable.get(i)));

                }
                else if (dateFormat.parse(Utils.ListOfTimeTable.get(i).getDate()).toString().substring(0, 3).equals("Tue")){

                    Utils.TuesdayTimeTable.add ((Utils.ListOfTimeTable.get(i)));

                }
                else if (dateFormat.parse(Utils.ListOfTimeTable.get(i).getDate()).toString().substring(0, 3).equals("Wed")){

                    Utils.WednesdayTimeTable.add ((Utils.ListOfTimeTable.get(i)));

                }
                else if (dateFormat.parse(Utils.ListOfTimeTable.get(i).getDate()).toString().substring(0, 3).equals("Thu")){

                    Utils.ThursdayTimeTable.add ((Utils.ListOfTimeTable.get(i)));

                }
                else if (dateFormat.parse(Utils.ListOfTimeTable.get(i).getDate()).toString().substring(0, 3).equals("Fri")){

                    Utils.FridayTimeTable.add ((Utils.ListOfTimeTable.get(i)));

                }
            }

            //Check Friday




        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}