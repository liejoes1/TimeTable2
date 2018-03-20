package com.example.joes.timetable2;

import android.content.Context;
import android.os.AsyncTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Joes on 20/3/2018.
 */

public class NetworkActivity {

    private static Context appContext;
    private static String ROOT_DIRECTORY_PATH;
    private static String ROOT_TEMP_PATH;

    private static final String TIMETABLE_LIST_URL = "https://webspace.apiit.edu.my/intake-timetable/TimetableIntakeList/TimetableIntakeList.xml";
    private static String TIMETABLE_INFO_BASE = "https://webspace.apiit.edu.my/intake-timetable/replyLink.php?stid=";


    public static File TimeTableListFile;

    public static void setContext(Context context) {
        appContext = context;
        CreateFolder();
    }

    public static class GetTimeTableList extends AsyncTask<Void, Void, Void> {

        File TempFile = new File(ROOT_TEMP_PATH, "TimeTableListTemporary.xml");

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL url = new URL(TIMETABLE_LIST_URL);
                DownloadFile(url, TempFile);
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
            File NewFile = new File(ROOT_DIRECTORY_PATH, "TimeTableList.xml");
            //After creating file copy the content now.
            CopyData(NewFile, TempFile);
            //When Done Pass the file to be processed
            setTimeTableListLocation(NewFile);
            try {
                DataParsing.ParseTimeTableList(new FileInputStream(NetworkActivity.TimeTableListFile));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static void setTimeTableListLocation(File file) {
        TimeTableListFile = file;
    }

    private static void CreateFolder() {
        ROOT_DIRECTORY_PATH = Utils.getRootDirPath(appContext);
        File TempDir = new File(ROOT_DIRECTORY_PATH, "/temp");
        if (!TempDir.exists()) {
            TempDir.mkdirs();
        }
        ROOT_TEMP_PATH = TempDir.toString();
    }

    private static void DownloadFile(URL url,File TempFile) {
        HttpURLConnection httpURLConnection = getNetworkData(url);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(TempFile);
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
}
