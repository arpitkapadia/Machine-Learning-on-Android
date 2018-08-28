package com.example.android.mcproject;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Pramodh on 11/29/2017.
 */

public class Logs extends Fragment {
    private TextView textView;
    private StringBuilder text = new StringBuilder();
    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Returning the layout file after inflating

        //Change R.layout.tab1 in you classes


        return inflater.inflate(R.layout.tab_logs, container, false);
    }


    @Override
    public void onStart() {
        super.onStart();
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data" + File.separator + getActivity().getApplicationContext().getPackageName() + File.separator + "logs.txt")  ));

            // do reading, usually loop until end of file reading
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                text.append("<font color=#cc0029>"+new StringBuffer(mLine).insert(7, "</font>").toString());
                text.append("<br>");

            }
        } catch (IOException e) {
            Toast.makeText(getActivity().getApplicationContext(),"Error reading file!", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                    TextView textview = getActivity().findViewById(R.id.logTextView);
                    if (textview == null) Log.d("TextView","TextView is null");
                    textview.setText(Html.fromHtml(text.toString()));
                    textview.setTextSize(12);
                } catch (IOException e) {
                    //log the exception
                }
            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data" + File.separator + getActivity().getApplicationContext().getPackageName() + File.separator + "logs.txt")  ));

            // do reading, usually loop until end of file reading
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                text.append("<font color=#cc0029>"+new StringBuffer(mLine).insert(7, "</font>").toString());
                text.append("<br>");

            }
        } catch (IOException e) {
            Toast.makeText(getActivity().getApplicationContext(),"Error reading file!", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                    TextView textview = getActivity().findViewById(R.id.logTextView);
                    if (textview == null) Log.d("TextView","TextView is null");
                    textview.setText(Html.fromHtml(text.toString()));
                    textview.setTextSize(12);
                } catch (IOException e) {
                    //log the exception
                }
            }
        }
    }
}
