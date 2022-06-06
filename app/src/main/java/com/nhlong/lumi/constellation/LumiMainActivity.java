package com.nhlong.lumi.constellation;

import android.content.Intent;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import androidx.appcompat.view.menu.SeslMenuItem;

import androidx.core.view.GravityCompat;

import lumi.constellation.drawer.DrawerLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.android.material.navigation.NavigationView;

public class LumiMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Context context;
    private DrawerLayout drawerLayout;

	private static final int Main = 0;
	private static final int QuickSettings = 1;
	private static final int StatusBar = 2;
	private static final int NavigationBar = 3;
	private static final int AddOns = 4;
	private int CurrentFragment = Main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.long266_main_activity);

        context = this;

        replaceFragment(new LumiMainFragment());

        drawerLayout = findViewById(R.id.lumiDrawerLayout);
        drawerLayout.setDrawerButtonTooltip(getString(R.string.lumi_action_settings));
        drawerLayout.setDrawerButtonOnClickListener(v -> startActivity(new Intent().setClass(context, LumiSettingsActivity.class)));

        NavigationView navigationView = findViewById(R.id.lumiNavigationView);
        navigationView.setNavigationItemSelectedListener(this);

        initDrawer();

    }

	@Override
	public boolean onNavigationItemSelected(MenuItem item) {
		int itemID = item.getItemId();
		switch (itemID){
				case R.id.lumi_main: 
					if(CurrentFragment != Main){
						replaceFragment(new LumiMainFragment());
						CurrentFragment = Main;
					}
					break;
				case R.id.lumi_quick_settings:
						if(CurrentFragment != QuickSettings){
						replaceFragment(new LumiQuickSettingsFragment());
						CurrentFragment = QuickSettings;
					}
					break;
				case R.id.lumi_status_bar:
						if(CurrentFragment != StatusBar){
						replaceFragment(new LumiStatusBarFragment());
						CurrentFragment = StatusBar;
					}
					break;
				case R.id.lumi_navigation_bar:
						if(CurrentFragment != NavigationBar){
						replaceFragment(new LumiNavigationBarFragment());
						CurrentFragment = NavigationBar;
					}
					break;
				case R.id.lumi_add_ons:
						if(CurrentFragment != AddOns){
						replaceFragment(new LumiAddOnsFragment());
						CurrentFragment = AddOns;
					}
					break;
		
		}
		drawerLayout.mDrawer.closeDrawer(drawerLayout.mDrawerContent);
	    return true;
	
    }

	@Override
	public void onBackPressed() {
		if (drawerLayout.mDrawer.isDrawerOpen(drawerLayout.mDrawerContent)){
			drawerLayout.mDrawer.closeDrawer(drawerLayout.mDrawerContent, true);
		}else{
			super.onBackPressed();
		}
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

	private void replaceFragment(Fragment fragment){
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.lumi_main,fragment);
		transaction.commit();
	}

    public static class LumiMainFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.long266_main_preferences, rootKey);

        }
    }

    public static class LumiQuickSettingsFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.long266_quick_settings, rootKey);

        }
    }

    public static class LumiStatusBarFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.long266_status_bar, rootKey);

        }
    }

    public static class LumiNavigationBarFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.long266_nav_bar, rootKey);

        }
    }

    public static class LumiAddOnsFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.long266_add_ons, rootKey);

        }
    }

}