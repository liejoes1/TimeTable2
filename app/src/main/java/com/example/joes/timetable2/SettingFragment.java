package com.example.joes.timetable2;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;


/**
 * Created by Joes on 21/3/2018.
 */


public class SettingFragment extends PreferenceFragmentCompat implements
        SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceChangeListener {


    public Context context;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_setting);

        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        context = getActivity();

        int count = preferenceScreen.getPreferenceCount();

        for (int i = 0; i < count; i++) {
            Preference preference = preferenceScreen.getPreference(i);
            if (!(preference instanceof CheckBoxPreference)) {

                String value = sharedPreferences.getString(preference.getKey(), "");
                setPreferenceSummary(preference, value);


            }
        }


        Preference preference = findPreference(getString(R.string.pref_timetable_key));
        EditTextPreference editTextPreference = (EditTextPreference) getPreferenceScreen().findPreference(getString(R.string.pref_timetable_key));
        final String value = sharedPreferences.getString(preference.getKey(), "");

        editTextPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                for (int i = 0; i < Utils.ListOfAllIntake.size(); i++) {
                    System.out.println("List Of All intake INITIAL" + newValue);
                    if (newValue.equals(Utils.ListOfAllIntake.get(i).toString())) {
                        Toast.makeText(getContext(), "Intake is OK", Toast.LENGTH_SHORT).show();
                        setPreferenceSummary(preference, newValue.toString());
                        return true;
                    }
                }
                Toast.makeText(getContext(), "Intake is BAD", Toast.LENGTH_SHORT).show();
                return false;


            }
        });
       // preference.setOnPreferenceChangeListener(this);


    }

    private void setPreferenceSummary(Preference preference, String value) {
        if (preference instanceof ListPreference) {
            // For list preferences, figure out the label of the selected value
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(value);
            if (prefIndex >= 0) {
                // Set the summary to that label
                listPreference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        } else if (preference instanceof EditTextPreference) {
            // For EditTextPreferences, set the summary to the value's simple string representation.
            preference.setSummary(value);
        }
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

        Preference preference = findPreference(s);
        if (null != preference) {
            if (!(preference instanceof CheckBoxPreference)) {

            }
        }

        EditTextPreference editTextPreference = (EditTextPreference) getPreferenceScreen().findPreference(getString(R.string.pref_timetable_key));
        final String value = sharedPreferences.getString(preference.getKey(), "");

        editTextPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                for (int i = 0; i < Utils.ListOfAllIntake.size(); i++) {
                    System.out.println("List Of All intake INITIAL" + newValue);
                    if (newValue.equals(Utils.ListOfAllIntake.get(i).toString())) {
                        Toast.makeText(getContext(), "Intake is OK", Toast.LENGTH_SHORT).show();
                        setPreferenceSummary(preference, newValue.toString());
                        return true;
                    }
                }
                Toast.makeText(getContext(), "Intake is BAD", Toast.LENGTH_SHORT).show();
                return false;


            }
        });
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        Toast error = Toast.makeText(getContext(), "Address Successfully Changed", Toast.LENGTH_SHORT);

        String sizeKey = getString(R.string.pref_timetable_key);
        if (preference.getKey().equals(sizeKey)) {
            String stringSize = (String) newValue;
            String Address = stringSize;
            Toast.makeText(getContext(), Address, Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }


}
