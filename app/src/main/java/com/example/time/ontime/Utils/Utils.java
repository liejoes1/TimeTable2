package com.example.time.ontime.Utils;

/**
 * Created by Joes on 13/3/2018.
 */

import android.content.Context;
import android.os.Environment;
import android.support.v4.content.ContextCompat;

import com.example.time.ontime.TimeTable.TimeTable;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

public final class Utils {

    public static ArrayList<String> ListOfAllIntake = new ArrayList<>();
    public static ArrayList<TimeTable> ListOfTimeTable = new ArrayList<>();

    public static ArrayList<TimeTable> MondayTimeTable = new ArrayList<>();
    public static ArrayList<TimeTable> TuesdayTimeTable = new ArrayList<>();
    public static ArrayList<TimeTable> WednesdayTimeTable = new ArrayList<>();
    public static ArrayList<TimeTable> ThursdayTimeTable = new ArrayList<>();
    public static ArrayList<TimeTable> FridayTimeTable = new ArrayList<>();

    public static String getRootDirPath(Context context) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File file = ContextCompat.getExternalFilesDirs(context.getApplicationContext(),
                    null)[0];
            return file.getAbsolutePath();
        } else {
            return context.getApplicationContext().getFilesDir().getAbsolutePath();
        }
    }
}

