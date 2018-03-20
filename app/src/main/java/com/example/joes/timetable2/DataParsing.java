package com.example.joes.timetable2;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;

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
}
