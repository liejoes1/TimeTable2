package com.example.joes.timetable2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.joes.timetable2.Utils.DataParsing;
import com.example.joes.timetable2.Utils.Decompress;
import com.example.joes.timetable2.Utils.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Joes on 20/3/2018.
 */

public class NetworkActivity {

    private static Context appContext;
    public static String ROOT_DIRECTORY_PATH;
    private static String ROOT_TEMP_PATH;

    private static final String TIMETABLE_LIST_URL = "https://webspace.apiit.edu.my/intake-timetable/TimetableIntakeList/TimetableIntakeList.xml";
    private static String TIMETABLE_INFO_BASE = "https://webspace.apiit.edu.my/intake-timetable/replyLink.php?stid=";

    public static boolean INITIAL_APP_LAUNCH;

    public static void setContext(Context context) {
        appContext = context;
        CreateFolder();
    }

    public static void setInitialAppLaunch(boolean initialAppLaunch) {
        INITIAL_APP_LAUNCH = initialAppLaunch;
    }

    public static class GetTimeTableList extends AsyncTask<Void, Void, Void> {

        File TempFile = new File(ROOT_TEMP_PATH, "TimeTableListTemporary.xml");

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                DownloadFile(new URL(TIMETABLE_LIST_URL), TempFile);
                //File Successfully Downloaded to the provided Folder
                //After download, we can start copy to the root to be read.
                //Nothing to be passed here
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //After creating file copy the content now.
            CopyData(new File(ROOT_DIRECTORY_PATH, "TimeTableList.xml"), TempFile);
            //When Done Pass the file to be processed
            try {
                //Parse Timetable List
                DataParsing.ParseTimeTableList(new FileInputStream(new File(ROOT_DIRECTORY_PATH, "TimeTableList.xml")));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static class GetTimeTableInfo extends AsyncTask<String, Void, String> {




        @Override
        protected String doInBackground(String... strings) {
            StringBuilder fullString = new StringBuilder();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new URL(TIMETABLE_INFO_BASE + strings[0]).openStream()));
                String tempStr;

                while ((tempStr = bufferedReader.readLine()) != null) {
                    fullString.append(tempStr);
                }
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return fullString.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(appContext);
            if (s.equals("Time table for the current week is not available for this intake code.")) {

                SharedPreferences.Editor editor = sharedPreferences.edit().putString("intakestatus", "FAILED");
                editor.apply();

                //When error, don't downlaod the timetable, cause not exist, instead go to Main Activity and show error
                Intent MainActivityIntent = new Intent(appContext, MainActivity.class);
                appContext.startActivity(MainActivityIntent);

            } else {
                SharedPreferences.Editor editor = sharedPreferences.edit().putString("intakestatus", "SUCCESS");
                editor.apply();

                new GetTimeTableData().execute(s);
            }
        }

    }

    public static class GetTimeTableData extends AsyncTask<String, Void, Void> {

        File TempFile = new File(ROOT_TEMP_PATH, "TimeTableTemporary.zip");


        @Override
        protected Void doInBackground(String... strings) {
            try {
                DownloadFile(new URL(strings[0]), TempFile);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            //Unzip the Data
            new Decompress(TempFile.toString(), ROOT_TEMP_PATH + "/TimeTableTemporary/").unzip();

            //Copy the Data to the root
            CopyData(new File(ROOT_DIRECTORY_PATH, "TimeTable.xml"), new File(ROOT_TEMP_PATH + "/TimeTableTemporary/" + "timetable.xml"));

            //Parse the data{
            try {
                DataParsing.ParseTimeTable(new FileInputStream(new File(ROOT_DIRECTORY_PATH, "TimeTable.xml")));

                Intent MainActivityIntent = new Intent(appContext, MainActivity.class);
                appContext.startActivity(MainActivityIntent);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

/***********************************************************************************************************************************************************/
/*
Limit Here, Don't change anything below,
*/

    private static void CreateFolder() {
        ROOT_DIRECTORY_PATH = Utils.getRootDirPath(appContext);
        File TempDir = new File(ROOT_DIRECTORY_PATH, "/temp");
        if (!TempDir.exists()) {
            TempDir.mkdirs();
        }
        ROOT_TEMP_PATH = TempDir.toString();
    }

    private static void DownloadFile(URL url, File TempFile) {
        try {
            HttpURLConnection httpURLConnection = getNetworkData(url);
            FileOutputStream fileOutputStream = new FileOutputStream(TempFile);
            InputStream inputStream = httpURLConnection.getInputStream();
            int lengthOfFile = httpURLConnection.getContentLength();

            byte data[] = new byte[1024];
            int count = 0;
            long total = 0;
            int progress = 0;

            while ((count = inputStream.read(data)) != -1) {
                total += count;
                int progress_temp = (int) total * 100 / lengthOfFile;
                if (progress_temp % 10 == 0 && progress != progress_temp) {
                    progress = progress_temp;
                }
                fileOutputStream.write(data, 0, count);
            }
            inputStream.close();
            fileOutputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void CopyData(File NewFile, File OldFile) {
        try {
            InputStream inputStream = new FileInputStream(OldFile);
            OutputStream outputStream = new FileOutputStream(NewFile);

            byte[] bytes = new byte[1024];
            int lengthOfFile;
            while ((lengthOfFile = inputStream.read(bytes)) > 0) {
                outputStream.write(bytes, 0, lengthOfFile);
            }
            inputStream.close();
            outputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static HttpURLConnection getNetworkData(URL url) {
        HttpURLConnection httpURLConnection = null;
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return httpURLConnection;
    }

    public static boolean isNetworkAvailable(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
