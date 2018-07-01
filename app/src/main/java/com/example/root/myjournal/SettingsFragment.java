package com.example.root.myjournal;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

import java.text.DateFormat;
import java.util.Date;

// SettingsFragment class
public class SettingsFragment extends PreferenceFragment
    implements SharedPreferences.OnSharedPreferenceChangeListener
{
    private final static int VERSION_M = 23;

    // On create
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);

        SharedPreferences preferences =
            PreferenceManager.getDefaultSharedPreferences(getActivity());

        // Get folder summary
        EditTextPreference folder =
            (EditTextPreference) findPreference(MainActivity.PREF_FOLDER);

        // Set folder in text view
        folder.setSummary(preferences.getString(MainActivity.PREF_FOLDER,
                                                MainActivity.DIARY));
        // Get index preference
        DatePickerPreference entry =
            (DatePickerPreference) findPreference(MainActivity.PREF_INDEX_PAGE);

        // Get value
        long value = preferences.getLong(MainActivity.PREF_INDEX_PAGE,
                                         DatePickerPreference.DEFAULT_VALUE);
        Date date = new Date(value);

        // Set summary
        DateFormat format = DateFormat.getDateInstance();
        String s = format.format(date);
        entry.setSummary(s);

        // Get about summary
        Preference about = findPreference(MainActivity.PREF_ABOUT);
        String sum = (String) about.getSummary();

        // Set version in text view
        s = String.format(sum, BuildConfig.VERSION_NAME);
        about.setSummary(s);
    }

    // on Resume
    @Override
    public void onResume()
    {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
            .registerOnSharedPreferenceChangeListener(this);
    }

    // on Pause
    @Override
    public void onPause()
    {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
            .unregisterOnSharedPreferenceChangeListener(this);
    }

    // On preference tree click
    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
                                         Preference preference)
    {
        boolean result =
            super.onPreferenceTreeClick(preferenceScreen, preference);

        // Set home as up
        if (preference instanceof PreferenceScreen)
        {
            Dialog dialog = ((PreferenceScreen)preference).getDialog();
            ActionBar actionBar = dialog.getActionBar();
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

        return result;
    }

    // On shared preference changed
    @Override
    public void onSharedPreferenceChanged(SharedPreferences preferences,
                                          String key)
    {
        if (key.equals(MainActivity.PREF_FOLDER))
        {
            // Get folder summary
            EditTextPreference folder =
                (EditTextPreference) findPreference(key);

            // Set folder in text view
            folder.setSummary(preferences.getString(key, MainActivity.DIARY));
        }

        if (key.equals(MainActivity.PREF_INDEX_PAGE))
        {
            // Get index preference
            DatePickerPreference entry =
                (DatePickerPreference) findPreference(key);

            // Get value
            long value =
                preferences.getLong(key, DatePickerPreference.DEFAULT_VALUE);
            Date date = new Date(value);

            // Set summary
            DateFormat format = DateFormat.getDateInstance();
            String s = format.format(date);
            entry.setSummary(s);
        }
        if (key.equals(MainActivity.PREF_DARK_THEME))
        {
            if (Build.VERSION.SDK_INT != VERSION_M)
                getActivity().recreate();
        }
    }
}
