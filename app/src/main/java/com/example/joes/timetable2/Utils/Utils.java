package com.example.joes.timetable2.Utils;

/**
 * Created by Joes on 13/3/2018.
 */

import android.content.Context;
import android.os.Environment;
import android.support.v4.content.ContextCompat;

import com.example.joes.timetable2.TimeTable.TimeTable;

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
    private Utils() {
        // no instance

    }

    public static String getRootDirPath(Context context) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File file = ContextCompat.getExternalFilesDirs(context.getApplicationContext(),
                    null)[0];
            return file.getAbsolutePath();
        } else {
            return context.getApplicationContext().getFilesDir().getAbsolutePath();
        }
    }

    public static String getProgressDisplayLine(long currentBytes, long totalBytes) {
        return getBytesToMBString(currentBytes) + "/" + getBytesToMBString(totalBytes);
    }

    private static String getBytesToMBString(long bytes){
        return String.format(Locale.ENGLISH, "%.2fMb", bytes / (1024.00 * 1024.00));
    }

}

