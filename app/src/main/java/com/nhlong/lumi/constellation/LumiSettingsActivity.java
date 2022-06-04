package com.nhlong.lumi.constellation;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.SeslMenuItem;
import androidx.appcompat.app.AppCompatDelegate;

import androidx.core.view.MenuCompat;

import java.util.ArrayList;
import java.util.List;

import lumi.constellation.layout.ToolbarLayout;

import android.preference.PreferenceManager;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class LumiSettingsActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener,
        Preference.SummaryProvider<androidx.preference.ListPreference> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.long266_settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.lumi_settings, new SettingsFragment())
                    .commit();
        }

		ToolbarLayout toolbarLayout = findViewById(R.id.lumiToolbarSettings);
        toolbarLayout.setNavigationButtonTooltip(getString(R.string.sesl_action_bar_up_description));
        toolbarLayout.setNavigationButtonOnClickListener(v -> onBackPressed());

        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);

    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        private Context mContext;
        private LumiSettingsActivity mActivity;

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            mContext = getContext();
            if (getActivity() instanceof LumiSettingsActivity)
                mActivity = ((LumiSettingsActivity) getActivity());
        }

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.long266_settings_preferences, rootKey);

        }
    }
	
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        String darkModeString = getString(R.string.lumi_dark_mode);
        if (key != null && sharedPreferences != null)
            if (key.equals(darkModeString)) {
                final String[] darkModeValues = getResources().getStringArray(R.array.lumi_dark_mode_values);
                // The apps theme is decided depending upon the saved preferences on app startup
                String pref = PreferenceManager.getDefaultSharedPreferences(this)
                        .getString(getString(R.string.lumi_dark_mode), getString(R.string.lumi_dark_mode_def_value));
                // Comparing to see which preference is selected and applying those theme settings
                if (pref.equals(darkModeValues[0]))
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                if (pref.equals(darkModeValues[1]))
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                if (pref.equals(darkModeValues[2]))
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
    }

    @Override
    public CharSequence provideSummary(ListPreference preference) {
        String key = preference.getKey();
        if (key != null)
            if (key.equals(getString(R.string.lumi_dark_mode)))
                return preference.getEntry();
        return null;
    }
	
    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

}