package com.nhlong.lumi.constellation;

import android.content.Context;
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

import lumi.constellation.drawer.DrawerLayout;

import androidx.fragment.app.Fragment;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.android.material.navigation.NavigationView;

public class LumiMainActivity extends AppCompatActivity {

    private Context context;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.long266_main_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.lumi_main, new SettingsFragment())
                    .commit();
        }

		context = this;

        drawerLayout = findViewById(R.id.drawerLayout);
        drawerLayout.setDrawerButtonTooltip(getString(R.string.lumi_action_settings));
        drawerLayout.setDrawerButtonOnClickListener(v -> startActivity(new Intent().setClass(context, LumiSettingsActivity.class)));

		initDrawer();

    }
	
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerLayout.setDrawerOpen(false, false);
    }

    private void initDrawer() {
        drawerLayout.setDrawerButtonIcon(getDrawable(R.drawable.sesl_ic_ab_settings));
        drawerLayout.setDrawerButtonTooltip("Settings");
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.long266_main_preferences, rootKey);

        }
    }

}