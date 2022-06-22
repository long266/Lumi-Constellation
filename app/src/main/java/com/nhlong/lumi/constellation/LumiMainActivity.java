package com.nhlong.lumi.constellation;

import android.content.res.Configuration;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.graphics.Color;
import android.net.Uri;
import android.provider.Settings.System;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.view.menu.SeslMenuItem;

import androidx.core.view.GravityCompat;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import androidx.preference.CheckBoxPreference;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.MultiSelectListPreference;
import androidx.preference.Preference;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.Preference.OnPreferenceClickListener;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreferenceCompat;

import lumi.constellation.drawer.DrawerLayout;
import lumi.constellation.preference.LumiColorPickerPreference;
import lumi.constellation.preference.LumiDetailedColorPickerPreference;

import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class LumiMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String LCAT = "LumiMainActivity";
    private static String MODCFG_FOLDER;
    private static String ROMCFG_FOLDER;

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

        ROMCFG_FOLDER = getResources().getString(R.string.romcfg_folder);
        MODCFG_FOLDER = getResources().getString(R.string.modcfg_folder);

    }

	@Override
	public boolean onNavigationItemSelected(MenuItem item) {
		int itemID = item.getItemId();
		switch (itemID){
				case R.id.lumi_main: 
					if(CurrentFragment != Main){
						replaceFragment(new LumiMainFragment());
						drawerLayout.setTitle("Lumi\'s Constellation", "Lumi\'s Constellation");
						CurrentFragment = Main;
					}
					break;
				case R.id.lumi_quick_settings:
						if(CurrentFragment != QuickSettings){
						replaceFragment(new LumiQuickSettingsFragment());
						drawerLayout.setTitle("Quick Settings");
						CurrentFragment = QuickSettings;
					}
					break;
				case R.id.lumi_status_bar:
						if(CurrentFragment != StatusBar){
						replaceFragment(new LumiStatusBarFragment());
						drawerLayout.setTitle("Status Bar");
						CurrentFragment = StatusBar;
					}
					break;
				case R.id.lumi_navigation_bar:
						if(CurrentFragment != NavigationBar){
						replaceFragment(new LumiNavigationBarFragment());
						drawerLayout.setTitle("Navigation Bar");
						CurrentFragment = NavigationBar;
					}
					break;
				case R.id.lumi_add_ons:
						if(CurrentFragment != AddOns){
						replaceFragment(new LumiAddOnsFragment());
						drawerLayout.setTitle("Add Ons");
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

    public static boolean setSettingInt(ContentResolver contentResolver, String setting, int value) {
        ContentResolver cr = contentResolver;
        try {
            boolean result = System.putInt(cr, setting, value);
            cr.notifyChange(Uri.parse("content://settings/system/" + setting), null);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static float getSettingFloat(ContentResolver contentResolver, String setting) {
        try {
            return System.getFloat(contentResolver, setting);
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0f;
        }
    }

    public static int getSettingInt(ContentResolver contentResolver, String setting) {
        try {
            return System.getInt(contentResolver, setting);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static boolean setSettingString(ContentResolver contentResolver, String setting, String value) {
        ContentResolver cr = contentResolver;
        try {
            boolean result = System.putString(cr, setting, value);
            cr.notifyChange(Uri.parse("content://settings/system/" + setting), null);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getSettingString(ContentResolver contentResolver, String setting) {
        try {
            return System.getString(contentResolver, setting);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static boolean setSettingFloat(ContentResolver contentResolver, String setting, float value) {
        ContentResolver cr = contentResolver;
        try {
            boolean result = System.putFloat(cr, setting, value);
            cr.notifyChange(Uri.parse("content://settings/system/" + setting), null);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean setSettingBoolean(ContentResolver contentResolver, String setting, boolean value) {
        if (setting.startsWith("romcfg") || setting.startsWith("modcfg")) {
            return setFileBoolean(setting, value);
        }
        int i;
        ContentResolver cr = contentResolver;
        if (value) {
            i = 1;
        } else {
            i = 0;
        }
        try {
            boolean result = System.putInt(cr, setting, i);
            cr.notifyChange(Uri.parse("content://settings/system/" + setting), null);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean setFileBoolean(String setting, boolean value) {
        boolean isMod = setting.startsWith("modcfg");
        boolean isReverse = isMod ? setting.startsWith("modcfgreverse_") : setting.startsWith("romcfgreverse_");
        String fileName = "";
        if (isMod) {
            if (isReverse) {
                fileName = setting.replace("modcfgreverse_", "");
            } else {
                fileName = setting.replace("modcfg_", "");
            }
        } else if (isReverse) {
            fileName = setting.replace("romcfgreverse_", "");
        } else {
            fileName = setting.replace("romcfg_", "");
        }
        Log.d(LCAT, "Set FileBoolean: value: " + value + " filname: " + fileName + " mod: " + isMod + " reverse: " + isReverse);
        new File(isMod ? MODCFG_FOLDER : ROMCFG_FOLDER).mkdirs();
        File file = new File(new StringBuilder(String.valueOf(isMod ? MODCFG_FOLDER : ROMCFG_FOLDER)).append(fileName).toString());
        boolean create = value;
        if (isReverse) {
            create = !create;
        }
        if (create) {
            try {
                Log.d(LCAT, "Set FileBoolean: Creating " + file.getPath());
                file.createNewFile();
            } catch (Exception e) {
                Log.e(LCAT, "Set FileBoolean: Creating " + file.getPath() + " ERROR: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            try {
                Log.d(LCAT, "Set FileBoolean: Deleting " + file.getPath());
                file.delete();
            } catch (Exception e2) {
                Log.e(LCAT, "Set FileBoolean: Deleting " + file.getPath() + " ERROR: " + e2.getMessage());
                e2.printStackTrace();
            }
        }
        return getFileBoolean(setting);
    }

    private static boolean getFileBoolean(String setting) {
        boolean isMod = setting.startsWith("modcfg");
        boolean isReverse = isMod ? setting.startsWith("modcfgreverse_") : setting.startsWith("romcfgreverse_");
        String fileName = "";
        if (isMod) {
            if (isReverse) {
                fileName = setting.replace("modcfgreverse_", "");
            } else {
                fileName = setting.replace("modcfg_", "");
            }
        } else if (isReverse) {
            fileName = setting.replace("romcfgreverse_", "");
        } else {
            fileName = setting.replace("romcfg_", "");
        }
        boolean exists = new File(new StringBuilder(String.valueOf(isMod ? MODCFG_FOLDER : ROMCFG_FOLDER)).append(fileName).toString()).exists();
        Log.d(LCAT, "Get FileBoolean: filname: " + fileName + " mod: " + isMod + " reverse: " + isReverse + " exists: " + exists);
        if (isReverse) {
            return !exists;
        } else {
            return exists;
        }
    }

    public static boolean getSettingBoolean(ContentResolver contentResolver, String setting) {
        if (setting.startsWith("romcfg") || setting.startsWith("modcfg")) {
            return getFileBoolean(setting);
        }
        try {
            if (System.getInt(contentResolver, setting) > 0) {
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static class LumiMainFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.long266_main_preferences, rootKey);

        }
    }

    public static class LumiQuickSettingsFragment extends PreferenceFragmentCompat implements OnPreferenceChangeListener, OnPreferenceClickListener {
        ContentResolver cr;

        class RunToolTask extends AsyncTask<Object, Void, Void> {
            RunToolTask() {
            }

            /* Access modifiers changed, original: protected|varargs */
            public Void doInBackground(Object... params) {
                SettingsTools.dispatch((Context) params[0], (String[]) params[1]);
                return null;
            }

            /* Access modifiers changed, original: protected */
            public void onPostExecute(Void result) {
                Toast.makeText(LumiQuickSettingsFragment.this.getActivity(), "Executed", 0).show();
            }
        }

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.long266_quick_settings, rootKey);
            this.cr = getActivity().getContentResolver();
            initPrefs();
        }

        @Override
        public boolean onPreferenceChange(Preference item, Object newValue) {
            Log.d(LumiMainActivity.LCAT, "onPreferenceChange: " + item.getClass().getSimpleName() + " " + item.getKey() + " > " + newValue);
            dispatchItem(item, newValue);
            return true;
        }

        public boolean onPreferenceClick(Preference item) {
            String[] data = new String[0];
            if (item.getKey().startsWith("runactivity_")) {
                String localActivity = item.getKey().substring(new String("runactivity_").length());
                Log.w(LumiMainActivity.LCAT, "Run Local Activity: " + localActivity);
                getActivity().startActivity(new Intent().setClassName(getActivity().getApplicationInfo().packageName, new StringBuilder(String.valueOf(getActivity().getApplicationInfo().packageName)).append(".").append(localActivity).toString()));
            } else if (item.getKey().startsWith("runexternal_")) {
                String externalActivity = item.getKey().substring(new String("runexternal_").length());
                Log.w(LumiMainActivity.LCAT, "Run External Activity: " + externalActivity);
                getActivity().startActivity(new Intent("android.intent.action.MAIN").setClassName(externalActivity.substring(0, externalActivity.lastIndexOf(".")), externalActivity));
            } else if (item.getKey().startsWith("activity;")) {
                data = item.getKey().substring(9).split(";");
                if (data.length != 2) {
                    Log.e(LumiMainActivity.LCAT, "Wrong Params Given: " + item.getKey().substring(9));
                    return false;
                }
                Log.w(LumiMainActivity.LCAT, "Run External Activity: " + data[0] + "/" + data[1]);
                getActivity().startActivity(new Intent("android.intent.action.MAIN").setClassName(data[0], data[1]));
            } else if (item.getKey().startsWith("tool#")) {
                if (item.getKey().substring(5).split("#").length < 1) {
                    Log.e(LumiMainActivity.LCAT, "Wrong Tool Params Given: " + item.getKey().substring(5));
                    return false;
                }
                new RunToolTask().execute(new Object[]{getActivity(), data});
            }
            return true;
        }

        private void dispatchItem(Preference item, Object value) {
            String type = item.getClass().getSimpleName();
            if (type.equals("ListPreference")) {
                dispatchList((ListPreference) item, value);
            } else if (type.equals("CheckBoxPreference")) {
                dispatchCheckbox((CheckBoxPreference) item, value);
            } else if (type.equals("MultiSelectListPreference")) {
                dispatchMultiSelectList((MultiSelectListPreference) item, (Set) value);
            } else if (type.equals("EditTextPreference")) {
                dispatchText((EditTextPreference) item, value);
            } else if (type.equals("SwitchPreferenceCompat")) {
                dispatchSwitch((SwitchPreferenceCompat) item, value);
            } else if (type.equals("LumiColorPickerPreference")) {
                dispatchLumiColorPicker((LumiColorPickerPreference) item, value);
            } else if (type.equals("LumiDetailedColorPickerPreference")) {
                dispatchLumiDetailedColorPicker((LumiDetailedColorPickerPreference) item, value);
            } else if (type.equals("SeekBarPreference")) {
                dispatchSeekBar(item, value);
            } else {
                Log.e(LumiMainActivity.LCAT, "Need to implement: " + type);
            }
        }

        private void dispatchLumiDetailedColorPicker(LumiDetailedColorPickerPreference item, Object value) {
            if (item != null) {
                String key = item.getKey();
                String[] prefs;
                String key1;
                String key2;
                String key3;
                String key4;
                String val;
                if (key.startsWith("argb;")) {
                    prefs = key.substring(5).split(";");
                    if (prefs.length != 4) {
                        Log.e(LumiMainActivity.LCAT, "color prefs wrong size! : " + prefs.length);
                        return;
                    }
                    key1 = prefs[0];
                    int val1 = Color.alpha(((Integer) value).intValue());
                    Log.d(LumiMainActivity.LCAT, "ARGBS color prefs alpha: " + key1 + " > " + val1);
                    if (key1.length() > 0) {
                        LumiMainActivity.setSettingInt(this.cr, key1, val1);
                    }
                    key2 = prefs[1];
                    int val2 = Color.red(((Integer) value).intValue());
                    Log.d(LumiMainActivity.LCAT, "ARGBS color prefs red: " + key2 + " > " + val2);
                    if (key2.length() > 0) {
                        LumiMainActivity.setSettingInt(this.cr, key2, val2);
                    }
                    key3 = prefs[2];
                    int val3 = Color.green(((Integer) value).intValue());
                    Log.d(LumiMainActivity.LCAT, "ARGBS color prefs green: " + key3 + " > " + val3);
                    if (key3.length() > 0) {
                        LumiMainActivity.setSettingInt(this.cr, key3, val3);
                    }
                    key4 = prefs[3];
                    int val4 = Color.blue(((Integer) value).intValue());
                    Log.d(LumiMainActivity.LCAT, "ARGBS color prefs blue: " + key4 + " > " + val4);
                    if (key4.length() > 0) {
                        LumiMainActivity.setSettingInt(this.cr, key4, val4);
                    }
                } else if (key.startsWith("argbf;")) {
                    prefs = key.substring(6).split(";");
                    if (prefs.length != 4) {
                        Log.e(LumiMainActivity.LCAT, "color prefs wrong size! : " + prefs.length);
                        return;
                    }
                    key1 = prefs[0];
                    float val12 = ((float) Color.alpha(((Integer) value).intValue())) / 255.0f;
                    Log.d(LumiMainActivity.LCAT, "ARGBFS color prefs alpha: " + key1 + " > " + val12);
                    if (key1.length() > 0) {
                        LumiMainActivity.setSettingFloat(this.cr, key1, val12);
                    }
                    key2 = prefs[1];
                    float val22 = ((float) Color.red(((Integer) value).intValue())) / 255.0f;
                    Log.d(LumiMainActivity.LCAT, "ARGBFS color prefs red: " + key2 + " > " + val22);
                    if (key2.length() > 0) {
                        LumiMainActivity.setSettingFloat(this.cr, key2, val22);
                    }
                    key3 = prefs[2];
                    float val32 = ((float) Color.green(((Integer) value).intValue())) / 255.0f;
                    Log.d(LumiMainActivity.LCAT, "ARGBFS color prefs green: " + key3 + " > " + val32);
                    if (key3.length() > 0) {
                        LumiMainActivity.setSettingFloat(this.cr, key3, val32);
                    }
                    key4 = prefs[3];
                    float val42 = ((float) Color.blue(((Integer) value).intValue())) / 255.0f;
                    Log.d(LumiMainActivity.LCAT, "ARGBFS color prefs blue: " + key4 + " > " + val42);
                    if (key4.length() > 0) {
                        LumiMainActivity.setSettingFloat(this.cr, key4, val42);
                    }
                } else if (key.startsWith("argb_")) {
                    key = key.substring(5);
                    val = getRGB(((Integer) value).intValue(), true);
                    Log.d(LumiMainActivity.LCAT, "ARGB color prefs: " + key + " > " + val);
                    LumiMainActivity.setSettingString(this.cr, key, val);
                } else if (key.startsWith("rgb_")) {
                    key = key.substring(4);
                    val = getRGB(((Integer) value).intValue(), false);
                    Log.d(LumiMainActivity.LCAT, "RGB color prefs: " + key + " > " + val);
                    LumiMainActivity.setSettingString(this.cr, key, val);
                } else {
                    LumiMainActivity.setSettingInt(this.cr, key, ((Integer) value).intValue());
                }
            }
        }

        private void dispatchLumiColorPicker(LumiColorPickerPreference item, Object value) {
            if (item != null) {
                String key = item.getKey();
                String[] prefs;
                String key1;
                String key2;
                String key3;
                String key4;
                String val;
                if (key.startsWith("argb;")) {
                    prefs = key.substring(5).split(";");
                    if (prefs.length != 4) {
                        Log.e(LumiMainActivity.LCAT, "color prefs wrong size! : " + prefs.length);
                        return;
                    }
                    key1 = prefs[0];
                    int val1 = Color.alpha(((Integer) value).intValue());
                    Log.d(LumiMainActivity.LCAT, "ARGBS color prefs alpha: " + key1 + " > " + val1);
                    if (key1.length() > 0) {
                        LumiMainActivity.setSettingInt(this.cr, key1, val1);
                    }
                    key2 = prefs[1];
                    int val2 = Color.red(((Integer) value).intValue());
                    Log.d(LumiMainActivity.LCAT, "ARGBS color prefs red: " + key2 + " > " + val2);
                    if (key2.length() > 0) {
                        LumiMainActivity.setSettingInt(this.cr, key2, val2);
                    }
                    key3 = prefs[2];
                    int val3 = Color.green(((Integer) value).intValue());
                    Log.d(LumiMainActivity.LCAT, "ARGBS color prefs green: " + key3 + " > " + val3);
                    if (key3.length() > 0) {
                        LumiMainActivity.setSettingInt(this.cr, key3, val3);
                    }
                    key4 = prefs[3];
                    int val4 = Color.blue(((Integer) value).intValue());
                    Log.d(LumiMainActivity.LCAT, "ARGBS color prefs blue: " + key4 + " > " + val4);
                    if (key4.length() > 0) {
                        LumiMainActivity.setSettingInt(this.cr, key4, val4);
                    }
                } else if (key.startsWith("argbf;")) {
                    prefs = key.substring(6).split(";");
                    if (prefs.length != 4) {
                        Log.e(LumiMainActivity.LCAT, "color prefs wrong size! : " + prefs.length);
                        return;
                    }
                    key1 = prefs[0];
                    float val12 = ((float) Color.alpha(((Integer) value).intValue())) / 255.0f;
                    Log.d(LumiMainActivity.LCAT, "ARGBFS color prefs alpha: " + key1 + " > " + val12);
                    if (key1.length() > 0) {
                        LumiMainActivity.setSettingFloat(this.cr, key1, val12);
                    }
                    key2 = prefs[1];
                    float val22 = ((float) Color.red(((Integer) value).intValue())) / 255.0f;
                    Log.d(LumiMainActivity.LCAT, "ARGBFS color prefs red: " + key2 + " > " + val22);
                    if (key2.length() > 0) {
                        LumiMainActivity.setSettingFloat(this.cr, key2, val22);
                    }
                    key3 = prefs[2];
                    float val32 = ((float) Color.green(((Integer) value).intValue())) / 255.0f;
                    Log.d(LumiMainActivity.LCAT, "ARGBFS color prefs green: " + key3 + " > " + val32);
                    if (key3.length() > 0) {
                        LumiMainActivity.setSettingFloat(this.cr, key3, val32);
                    }
                    key4 = prefs[3];
                    float val42 = ((float) Color.blue(((Integer) value).intValue())) / 255.0f;
                    Log.d(LumiMainActivity.LCAT, "ARGBFS color prefs blue: " + key4 + " > " + val42);
                    if (key4.length() > 0) {
                        LumiMainActivity.setSettingFloat(this.cr, key4, val42);
                    }
                } else if (key.startsWith("argb_")) {
                    key = key.substring(5);
                    val = getRGB(((Integer) value).intValue(), true);
                    Log.d(LumiMainActivity.LCAT, "ARGB color prefs: " + key + " > " + val);
                    LumiMainActivity.setSettingString(this.cr, key, val);
                } else if (key.startsWith("rgb_")) {
                    key = key.substring(4);
                    val = getRGB(((Integer) value).intValue(), false);
                    Log.d(LumiMainActivity.LCAT, "RGB color prefs: " + key + " > " + val);
                    LumiMainActivity.setSettingString(this.cr, key, val);
                } else {
                    LumiMainActivity.setSettingInt(this.cr, key, ((Integer) value).intValue());
                }
            }
        }

        private String getRGB(int color, boolean hasAlpha) {
            int red = Color.red(color);
            int green = Color.green(color);
            int blue = Color.blue(color);
            int alpha = Color.alpha(color);
            String out = "#";
            if (hasAlpha) {
                out = new StringBuilder(String.valueOf(out)).append(alpha < 17 ? "0" : "").append(Integer.toHexString(alpha)).toString();
            }
            return new StringBuilder(String.valueOf(out)).append(red < 17 ? "0" : "").append(Integer.toHexString(red)).append(green < 17 ? "0" : "").append(Integer.toHexString(green)).append(blue < 17 ? "0" : "").append(Integer.toHexString(blue)).toString();
        }

        private void dispatchSeekBar(Preference item, Object value) {
            if (item != null) {
                LumiMainActivity.setSettingInt(this.cr, item.getKey(), ((Integer) value).intValue());
            }
        }

        private void dispatchMultiSelectList(MultiSelectListPreference item, Set<String> value) {
            if (item != null) {
                String val = "";
                Iterator<String> iterator = value.iterator();
                while (iterator.hasNext()) {
                    val = new StringBuilder(String.valueOf(val)).append((String) iterator.next()).toString();
                    if (iterator.hasNext()) {
                        val = new StringBuilder(String.valueOf(val)).append(",").toString();
                    }
                }
                LumiMainActivity.setSettingString(this.cr, item.getKey(), val);
            }
        }

        private void dispatchText(EditTextPreference item, Object value) {
            if (item != null) {
                LumiMainActivity.setSettingString(this.cr, item.getKey(), (String) value);
            }
        }

        private void dispatchCheckbox(CheckBoxPreference item, Object value) {
            if (item != null) {
                LumiMainActivity.setSettingBoolean(this.cr, item.getKey(), ((Boolean) value).booleanValue());
            }
        }

        private void dispatchSwitch(SwitchPreferenceCompat item, Object value) {
            if (item != null) {
                LumiMainActivity.setSettingBoolean(this.cr, item.getKey(), ((Boolean) value).booleanValue());
            }
        }

        private void dispatchList(ListPreference item, Object value) {
            if (item != null) {
                LumiMainActivity.setSettingString(this.cr, item.getKey(), (String) value);
            }
        }

        private void initPrefs() {
            int items = getPreferenceScreen().getPreferenceCount();
            for (int i = 0; i < items; i++) {
                initItem(getPreferenceScreen().getPreference(i));
            }
        }

        private void initItem(Preference item) {
            String type = item.getClass().getSimpleName();
            if (type.equals("PreferenceCategory")) {
                initCategory((PreferenceCategory) item);
            } else if (type.equals("PreferenceScreen")) {
                initScreen((PreferenceScreen) item);
            } else if (type.equals("CheckBoxPreference")) {
                initCheckbox((CheckBoxPreference) item);
            } else if (type.equals("MultiSelectListPreference")) {
                initMultiSelectList((MultiSelectListPreference) item);
            } else if (type.equals("EditTextPreference")) {
                initText((EditTextPreference) item);
            } else if (type.equals("ListPreference")) {
                initList((ListPreference) item);
            } else if (type.equals("SwitchPreferenceCompat")) {
                initSwitch((SwitchPreferenceCompat) item);
            } else if (type.equals("LumiColorPickerPreference")) {
                initLumiColorPicker((LumiColorPickerPreference) item);
            } else if (type.equals("LumiDetailedColorPickerPreference")) {
                initLumiDetailedColorPicker((LumiDetailedColorPickerPreference) item);
            } else if (type.equals("SeekBarPreference")) {
                initSeekBar(item);
            } else {
                Log.e(LumiMainActivity.LCAT, "Need to implement: " + type);
            }
        }

        private void initLumiColorPicker(LumiColorPickerPreference item) {
            item.setOnPreferenceChangeListener(this);
            String key = item.getKey();
            if (key.startsWith("argb;")) {
                String[] prefs = key.substring(5).split(";");
                if (prefs.length != 4) {
                    Log.e(LumiMainActivity.LCAT, "color prefs wrong size! : " + prefs.length);
                    return;
                }
                setInitialColor(Color.argb(LumiMainActivity.getSettingInt(this.cr, prefs[0]), LumiMainActivity.getSettingInt(this.cr, prefs[1]), LumiMainActivity.getSettingInt(this.cr, prefs[2]), LumiMainActivity.getSettingInt(this.cr, prefs[3])));
            } else if (key.startsWith("argbf;")) {
                String[] prefs2 = key.substring(6).split(";");
                if (prefs2.length != 4) {
                    Log.e(LumiMainActivity.LCAT, "color prefs wrong size! : " + prefs2.length);
                    return;
                }
                setInitialColor(Color.argb(Integer.valueOf((int) (LumiMainActivity.getSettingFloat(this.cr, prefs2[0]) * 255.0f)).intValue(), Integer.valueOf((int) (LumiMainActivity.getSettingFloat(this.cr, prefs2[1]) * 255.0f)).intValue(), Integer.valueOf((int) (LumiMainActivity.getSettingFloat(this.cr, prefs2[2]) * 255.0f)).intValue(), Integer.valueOf((int) (LumiMainActivity.getSettingFloat(this.cr, prefs2[3]) * 255.0f)).intValue()));
            } else if (key.startsWith("argb_")) {
                String theColor = LumiMainActivity.getSettingString(this.cr, key.substring(5));
                if (theColor == null) {
                    theColor = "#FF33B5E5";
                }
                setInitialColor(Color.parseColor(theColor));
            } else if (key.startsWith("rgb_")) {
                String theColor2 = LumiMainActivity.getSettingString(this.cr, key.substring(4));
                if (theColor2 == null) {
                    theColor2 = "#33B5E5";
                }
                setInitialColor(Color.parseColor(theColor2));
            } else {
                setInitialColor(LumiMainActivity.getSettingInt(this.cr, key));
            }
        }

        private void initLumiDetailedColorPicker(LumiDetailedColorPickerPreference item) {
            item.setOnPreferenceChangeListener(this);
            String key = item.getKey();
            if (key.startsWith("argb;")) {
                String[] prefs = key.substring(5).split(";");
                if (prefs.length != 4) {
                    Log.e(LumiMainActivity.LCAT, "color prefs wrong size! : " + prefs.length);
                    return;
                }
                setInitialColor(Color.argb(LumiMainActivity.getSettingInt(this.cr, prefs[0]), LumiMainActivity.getSettingInt(this.cr, prefs[1]), LumiMainActivity.getSettingInt(this.cr, prefs[2]), LumiMainActivity.getSettingInt(this.cr, prefs[3])));
            } else if (key.startsWith("argbf;")) {
                String[] prefs2 = key.substring(6).split(";");
                if (prefs2.length != 4) {
                    Log.e(LumiMainActivity.LCAT, "color prefs wrong size! : " + prefs2.length);
                    return;
                }
                setInitialColor(Color.argb(Integer.valueOf((int) (LumiMainActivity.getSettingFloat(this.cr, prefs2[0]) * 255.0f)).intValue(), Integer.valueOf((int) (LumiMainActivity.getSettingFloat(this.cr, prefs2[1]) * 255.0f)).intValue(), Integer.valueOf((int) (LumiMainActivity.getSettingFloat(this.cr, prefs2[2]) * 255.0f)).intValue(), Integer.valueOf((int) (LumiMainActivity.getSettingFloat(this.cr, prefs2[3]) * 255.0f)).intValue()));
            } else if (key.startsWith("argb_")) {
                String theColor = LumiMainActivity.getSettingString(this.cr, key.substring(5));
                if (theColor == null) {
                    theColor = "#FF33B5E5";
                }
                setInitialColor(Color.parseColor(theColor));
            } else if (key.startsWith("rgb_")) {
                String theColor2 = LumiMainActivity.getSettingString(this.cr, key.substring(4));
                if (theColor2 == null) {
                    theColor2 = "#33B5E5";
                }
                setInitialColor(Color.parseColor(theColor2));
            } else {
                setInitialColor(LumiMainActivity.getSettingInt(this.cr, key));
            }
        }

        public void setInitialColor(int i) {
        }

        private void initSeekBar(Preference item) {
            item.setOnPreferenceChangeListener(this);
        }

        private void initMultiSelectList(MultiSelectListPreference item) {
            item.setOnPreferenceChangeListener(this);
            String initial = LumiMainActivity.getSettingString(this.cr, item.getKey());
            if (initial != null) {
                String[] ival = initial.split(",", 0);
                Set<String> fval = new HashSet();
                for (Object add : ival) {
                    fval.add((String) add);
                }
                item.setValues(fval);
            }
        }

        private void initSwitch(SwitchPreferenceCompat item) {
            if (item != null) {
                item.setOnPreferenceChangeListener(this);
                item.setChecked(LumiMainActivity.getSettingBoolean(this.cr, item.getKey()));
            }
        }

        private void initText(EditTextPreference item) {
            item.setOnPreferenceChangeListener(this);
            item.setText(LumiMainActivity.getSettingString(this.cr, item.getKey()));
        }

        private void initCheckbox(CheckBoxPreference item) {
            if (item != null) {
                item.setOnPreferenceChangeListener(this);
                item.setChecked(LumiMainActivity.getSettingBoolean(this.cr, item.getKey()));
            }
        }

        private void initList(ListPreference item) {
            item.setOnPreferenceChangeListener(this);
            item.setValue(LumiMainActivity.getSettingString(this.cr, item.getKey()));
        }

        private void initCategory(PreferenceCategory category) {
            int items = category.getPreferenceCount();
            for (int i = 0; i < items; i++) {
                initItem(category.getPreference(i));
            }
        }

        private void initScreen(PreferenceScreen category) {
            int items = category.getPreferenceCount();
            for (int i = 0; i < items; i++) {
                initItem(category.getPreference(i));
            }
        }

    }

    public static class LumiStatusBarFragment extends PreferenceFragmentCompat implements OnPreferenceChangeListener, OnPreferenceClickListener {
        ContentResolver cr;

        class RunToolTask extends AsyncTask<Object, Void, Void> {
            RunToolTask() {
            }

            /* Access modifiers changed, original: protected|varargs */
            public Void doInBackground(Object... params) {
                SettingsTools.dispatch((Context) params[0], (String[]) params[1]);
                return null;
            }

            /* Access modifiers changed, original: protected */
            public void onPostExecute(Void result) {
                Toast.makeText(LumiStatusBarFragment.this.getActivity(), "Executed", 0).show();
            }
        }

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.long266_status_bar, rootKey);
            this.cr = getActivity().getContentResolver();
            initPrefs();
        }

        @Override
        public boolean onPreferenceChange(Preference item, Object newValue) {
            Log.d(LumiMainActivity.LCAT, "onPreferenceChange: " + item.getClass().getSimpleName() + " " + item.getKey() + " > " + newValue);
            dispatchItem(item, newValue);
            return true;
        }

        public boolean onPreferenceClick(Preference item) {
            String[] data = new String[0];
            if (item.getKey().startsWith("runactivity_")) {
                String localActivity = item.getKey().substring(new String("runactivity_").length());
                Log.w(LumiMainActivity.LCAT, "Run Local Activity: " + localActivity);
                getActivity().startActivity(new Intent().setClassName(getActivity().getApplicationInfo().packageName, new StringBuilder(String.valueOf(getActivity().getApplicationInfo().packageName)).append(".").append(localActivity).toString()));
            } else if (item.getKey().startsWith("runexternal_")) {
                String externalActivity = item.getKey().substring(new String("runexternal_").length());
                Log.w(LumiMainActivity.LCAT, "Run External Activity: " + externalActivity);
                getActivity().startActivity(new Intent("android.intent.action.MAIN").setClassName(externalActivity.substring(0, externalActivity.lastIndexOf(".")), externalActivity));
            } else if (item.getKey().startsWith("activity;")) {
                data = item.getKey().substring(9).split(";");
                if (data.length != 2) {
                    Log.e(LumiMainActivity.LCAT, "Wrong Params Given: " + item.getKey().substring(9));
                    return false;
                }
                Log.w(LumiMainActivity.LCAT, "Run External Activity: " + data[0] + "/" + data[1]);
                getActivity().startActivity(new Intent("android.intent.action.MAIN").setClassName(data[0], data[1]));
            } else if (item.getKey().startsWith("tool#")) {
                if (item.getKey().substring(5).split("#").length < 1) {
                    Log.e(LumiMainActivity.LCAT, "Wrong Tool Params Given: " + item.getKey().substring(5));
                    return false;
                }
                new RunToolTask().execute(new Object[]{getActivity(), data});
            }
            return true;
        }

        private void dispatchItem(Preference item, Object value) {
            String type = item.getClass().getSimpleName();
            if (type.equals("ListPreference")) {
                dispatchList((ListPreference) item, value);
            } else if (type.equals("CheckBoxPreference")) {
                dispatchCheckbox((CheckBoxPreference) item, value);
            } else if (type.equals("MultiSelectListPreference")) {
                dispatchMultiSelectList((MultiSelectListPreference) item, (Set) value);
            } else if (type.equals("EditTextPreference")) {
                dispatchText((EditTextPreference) item, value);
            } else if (type.equals("SwitchPreferenceCompat")) {
                dispatchSwitch((SwitchPreferenceCompat) item, value);
            } else if (type.equals("LumiColorPickerPreference")) {
                dispatchLumiColorPicker((LumiColorPickerPreference) item, value);
            } else if (type.equals("LumiDetailedColorPickerPreference")) {
                dispatchLumiDetailedColorPicker((LumiDetailedColorPickerPreference) item, value);
            } else if (type.equals("SeekBarPreference")) {
                dispatchSeekBar(item, value);
            } else {
                Log.e(LumiMainActivity.LCAT, "Need to implement: " + type);
            }
        }

        private void dispatchLumiDetailedColorPicker(LumiDetailedColorPickerPreference item, Object value) {
            if (item != null) {
                String key = item.getKey();
                String[] prefs;
                String key1;
                String key2;
                String key3;
                String key4;
                String val;
                if (key.startsWith("argb;")) {
                    prefs = key.substring(5).split(";");
                    if (prefs.length != 4) {
                        Log.e(LumiMainActivity.LCAT, "color prefs wrong size! : " + prefs.length);
                        return;
                    }
                    key1 = prefs[0];
                    int val1 = Color.alpha(((Integer) value).intValue());
                    Log.d(LumiMainActivity.LCAT, "ARGBS color prefs alpha: " + key1 + " > " + val1);
                    if (key1.length() > 0) {
                        LumiMainActivity.setSettingInt(this.cr, key1, val1);
                    }
                    key2 = prefs[1];
                    int val2 = Color.red(((Integer) value).intValue());
                    Log.d(LumiMainActivity.LCAT, "ARGBS color prefs red: " + key2 + " > " + val2);
                    if (key2.length() > 0) {
                        LumiMainActivity.setSettingInt(this.cr, key2, val2);
                    }
                    key3 = prefs[2];
                    int val3 = Color.green(((Integer) value).intValue());
                    Log.d(LumiMainActivity.LCAT, "ARGBS color prefs green: " + key3 + " > " + val3);
                    if (key3.length() > 0) {
                        LumiMainActivity.setSettingInt(this.cr, key3, val3);
                    }
                    key4 = prefs[3];
                    int val4 = Color.blue(((Integer) value).intValue());
                    Log.d(LumiMainActivity.LCAT, "ARGBS color prefs blue: " + key4 + " > " + val4);
                    if (key4.length() > 0) {
                        LumiMainActivity.setSettingInt(this.cr, key4, val4);
                    }
                } else if (key.startsWith("argbf;")) {
                    prefs = key.substring(6).split(";");
                    if (prefs.length != 4) {
                        Log.e(LumiMainActivity.LCAT, "color prefs wrong size! : " + prefs.length);
                        return;
                    }
                    key1 = prefs[0];
                    float val12 = ((float) Color.alpha(((Integer) value).intValue())) / 255.0f;
                    Log.d(LumiMainActivity.LCAT, "ARGBFS color prefs alpha: " + key1 + " > " + val12);
                    if (key1.length() > 0) {
                        LumiMainActivity.setSettingFloat(this.cr, key1, val12);
                    }
                    key2 = prefs[1];
                    float val22 = ((float) Color.red(((Integer) value).intValue())) / 255.0f;
                    Log.d(LumiMainActivity.LCAT, "ARGBFS color prefs red: " + key2 + " > " + val22);
                    if (key2.length() > 0) {
                        LumiMainActivity.setSettingFloat(this.cr, key2, val22);
                    }
                    key3 = prefs[2];
                    float val32 = ((float) Color.green(((Integer) value).intValue())) / 255.0f;
                    Log.d(LumiMainActivity.LCAT, "ARGBFS color prefs green: " + key3 + " > " + val32);
                    if (key3.length() > 0) {
                        LumiMainActivity.setSettingFloat(this.cr, key3, val32);
                    }
                    key4 = prefs[3];
                    float val42 = ((float) Color.blue(((Integer) value).intValue())) / 255.0f;
                    Log.d(LumiMainActivity.LCAT, "ARGBFS color prefs blue: " + key4 + " > " + val42);
                    if (key4.length() > 0) {
                        LumiMainActivity.setSettingFloat(this.cr, key4, val42);
                    }
                } else if (key.startsWith("argb_")) {
                    key = key.substring(5);
                    val = getRGB(((Integer) value).intValue(), true);
                    Log.d(LumiMainActivity.LCAT, "ARGB color prefs: " + key + " > " + val);
                    LumiMainActivity.setSettingString(this.cr, key, val);
                } else if (key.startsWith("rgb_")) {
                    key = key.substring(4);
                    val = getRGB(((Integer) value).intValue(), false);
                    Log.d(LumiMainActivity.LCAT, "RGB color prefs: " + key + " > " + val);
                    LumiMainActivity.setSettingString(this.cr, key, val);
                } else {
                    LumiMainActivity.setSettingInt(this.cr, key, ((Integer) value).intValue());
                }
            }
        }

        private void dispatchLumiColorPicker(LumiColorPickerPreference item, Object value) {
            if (item != null) {
                String key = item.getKey();
                String[] prefs;
                String key1;
                String key2;
                String key3;
                String key4;
                String val;
                if (key.startsWith("argb;")) {
                    prefs = key.substring(5).split(";");
                    if (prefs.length != 4) {
                        Log.e(LumiMainActivity.LCAT, "color prefs wrong size! : " + prefs.length);
                        return;
                    }
                    key1 = prefs[0];
                    int val1 = Color.alpha(((Integer) value).intValue());
                    Log.d(LumiMainActivity.LCAT, "ARGBS color prefs alpha: " + key1 + " > " + val1);
                    if (key1.length() > 0) {
                        LumiMainActivity.setSettingInt(this.cr, key1, val1);
                    }
                    key2 = prefs[1];
                    int val2 = Color.red(((Integer) value).intValue());
                    Log.d(LumiMainActivity.LCAT, "ARGBS color prefs red: " + key2 + " > " + val2);
                    if (key2.length() > 0) {
                        LumiMainActivity.setSettingInt(this.cr, key2, val2);
                    }
                    key3 = prefs[2];
                    int val3 = Color.green(((Integer) value).intValue());
                    Log.d(LumiMainActivity.LCAT, "ARGBS color prefs green: " + key3 + " > " + val3);
                    if (key3.length() > 0) {
                        LumiMainActivity.setSettingInt(this.cr, key3, val3);
                    }
                    key4 = prefs[3];
                    int val4 = Color.blue(((Integer) value).intValue());
                    Log.d(LumiMainActivity.LCAT, "ARGBS color prefs blue: " + key4 + " > " + val4);
                    if (key4.length() > 0) {
                        LumiMainActivity.setSettingInt(this.cr, key4, val4);
                    }
                } else if (key.startsWith("argbf;")) {
                    prefs = key.substring(6).split(";");
                    if (prefs.length != 4) {
                        Log.e(LumiMainActivity.LCAT, "color prefs wrong size! : " + prefs.length);
                        return;
                    }
                    key1 = prefs[0];
                    float val12 = ((float) Color.alpha(((Integer) value).intValue())) / 255.0f;
                    Log.d(LumiMainActivity.LCAT, "ARGBFS color prefs alpha: " + key1 + " > " + val12);
                    if (key1.length() > 0) {
                        LumiMainActivity.setSettingFloat(this.cr, key1, val12);
                    }
                    key2 = prefs[1];
                    float val22 = ((float) Color.red(((Integer) value).intValue())) / 255.0f;
                    Log.d(LumiMainActivity.LCAT, "ARGBFS color prefs red: " + key2 + " > " + val22);
                    if (key2.length() > 0) {
                        LumiMainActivity.setSettingFloat(this.cr, key2, val22);
                    }
                    key3 = prefs[2];
                    float val32 = ((float) Color.green(((Integer) value).intValue())) / 255.0f;
                    Log.d(LumiMainActivity.LCAT, "ARGBFS color prefs green: " + key3 + " > " + val32);
                    if (key3.length() > 0) {
                        LumiMainActivity.setSettingFloat(this.cr, key3, val32);
                    }
                    key4 = prefs[3];
                    float val42 = ((float) Color.blue(((Integer) value).intValue())) / 255.0f;
                    Log.d(LumiMainActivity.LCAT, "ARGBFS color prefs blue: " + key4 + " > " + val42);
                    if (key4.length() > 0) {
                        LumiMainActivity.setSettingFloat(this.cr, key4, val42);
                    }
                } else if (key.startsWith("argb_")) {
                    key = key.substring(5);
                    val = getRGB(((Integer) value).intValue(), true);
                    Log.d(LumiMainActivity.LCAT, "ARGB color prefs: " + key + " > " + val);
                    LumiMainActivity.setSettingString(this.cr, key, val);
                } else if (key.startsWith("rgb_")) {
                    key = key.substring(4);
                    val = getRGB(((Integer) value).intValue(), false);
                    Log.d(LumiMainActivity.LCAT, "RGB color prefs: " + key + " > " + val);
                    LumiMainActivity.setSettingString(this.cr, key, val);
                } else {
                    LumiMainActivity.setSettingInt(this.cr, key, ((Integer) value).intValue());
                }
            }
        }

        private String getRGB(int color, boolean hasAlpha) {
            int red = Color.red(color);
            int green = Color.green(color);
            int blue = Color.blue(color);
            int alpha = Color.alpha(color);
            String out = "#";
            if (hasAlpha) {
                out = new StringBuilder(String.valueOf(out)).append(alpha < 17 ? "0" : "").append(Integer.toHexString(alpha)).toString();
            }
            return new StringBuilder(String.valueOf(out)).append(red < 17 ? "0" : "").append(Integer.toHexString(red)).append(green < 17 ? "0" : "").append(Integer.toHexString(green)).append(blue < 17 ? "0" : "").append(Integer.toHexString(blue)).toString();
        }

        private void dispatchSeekBar(Preference item, Object value) {
            if (item != null) {
                LumiMainActivity.setSettingInt(this.cr, item.getKey(), ((Integer) value).intValue());
            }
        }

        private void dispatchMultiSelectList(MultiSelectListPreference item, Set<String> value) {
            if (item != null) {
                String val = "";
                Iterator<String> iterator = value.iterator();
                while (iterator.hasNext()) {
                    val = new StringBuilder(String.valueOf(val)).append((String) iterator.next()).toString();
                    if (iterator.hasNext()) {
                        val = new StringBuilder(String.valueOf(val)).append(",").toString();
                    }
                }
                LumiMainActivity.setSettingString(this.cr, item.getKey(), val);
            }
        }

        private void dispatchText(EditTextPreference item, Object value) {
            if (item != null) {
                LumiMainActivity.setSettingString(this.cr, item.getKey(), (String) value);
            }
        }

        private void dispatchCheckbox(CheckBoxPreference item, Object value) {
            if (item != null) {
                LumiMainActivity.setSettingBoolean(this.cr, item.getKey(), ((Boolean) value).booleanValue());
            }
        }

        private void dispatchSwitch(SwitchPreferenceCompat item, Object value) {
            if (item != null) {
                LumiMainActivity.setSettingBoolean(this.cr, item.getKey(), ((Boolean) value).booleanValue());
            }
        }

        private void dispatchList(ListPreference item, Object value) {
            if (item != null) {
                LumiMainActivity.setSettingString(this.cr, item.getKey(), (String) value);
            }
        }

        private void initPrefs() {
            int items = getPreferenceScreen().getPreferenceCount();
            for (int i = 0; i < items; i++) {
                initItem(getPreferenceScreen().getPreference(i));
            }
        }

        private void initItem(Preference item) {
            String type = item.getClass().getSimpleName();
            if (type.equals("PreferenceCategory")) {
                initCategory((PreferenceCategory) item);
            } else if (type.equals("PreferenceScreen")) {
                initScreen((PreferenceScreen) item);
            } else if (type.equals("CheckBoxPreference")) {
                initCheckbox((CheckBoxPreference) item);
            } else if (type.equals("MultiSelectListPreference")) {
                initMultiSelectList((MultiSelectListPreference) item);
            } else if (type.equals("EditTextPreference")) {
                initText((EditTextPreference) item);
            } else if (type.equals("ListPreference")) {
                initList((ListPreference) item);
            } else if (type.equals("SwitchPreferenceCompat")) {
                initSwitch((SwitchPreferenceCompat) item);
            } else if (type.equals("LumiColorPickerPreference")) {
                initLumiColorPicker((LumiColorPickerPreference) item);
            } else if (type.equals("LumiDetailedColorPickerPreference")) {
                initLumiDetailedColorPicker((LumiDetailedColorPickerPreference) item);
            } else if (type.equals("SeekBarPreference")) {
                initSeekBar(item);
            } else {
                Log.e(LumiMainActivity.LCAT, "Need to implement: " + type);
            }
        }

        private void initLumiColorPicker(LumiColorPickerPreference item) {
            item.setOnPreferenceChangeListener(this);
            String key = item.getKey();
            if (key.startsWith("argb;")) {
                String[] prefs = key.substring(5).split(";");
                if (prefs.length != 4) {
                    Log.e(LumiMainActivity.LCAT, "color prefs wrong size! : " + prefs.length);
                    return;
                }
                setInitialColor(Color.argb(LumiMainActivity.getSettingInt(this.cr, prefs[0]), LumiMainActivity.getSettingInt(this.cr, prefs[1]), LumiMainActivity.getSettingInt(this.cr, prefs[2]), LumiMainActivity.getSettingInt(this.cr, prefs[3])));
            } else if (key.startsWith("argbf;")) {
                String[] prefs2 = key.substring(6).split(";");
                if (prefs2.length != 4) {
                    Log.e(LumiMainActivity.LCAT, "color prefs wrong size! : " + prefs2.length);
                    return;
                }
                setInitialColor(Color.argb(Integer.valueOf((int) (LumiMainActivity.getSettingFloat(this.cr, prefs2[0]) * 255.0f)).intValue(), Integer.valueOf((int) (LumiMainActivity.getSettingFloat(this.cr, prefs2[1]) * 255.0f)).intValue(), Integer.valueOf((int) (LumiMainActivity.getSettingFloat(this.cr, prefs2[2]) * 255.0f)).intValue(), Integer.valueOf((int) (LumiMainActivity.getSettingFloat(this.cr, prefs2[3]) * 255.0f)).intValue()));
            } else if (key.startsWith("argb_")) {
                String theColor = LumiMainActivity.getSettingString(this.cr, key.substring(5));
                if (theColor == null) {
                    theColor = "#FF33B5E5";
                }
                setInitialColor(Color.parseColor(theColor));
            } else if (key.startsWith("rgb_")) {
                String theColor2 = LumiMainActivity.getSettingString(this.cr, key.substring(4));
                if (theColor2 == null) {
                    theColor2 = "#33B5E5";
                }
                setInitialColor(Color.parseColor(theColor2));
            } else {
                setInitialColor(LumiMainActivity.getSettingInt(this.cr, key));
            }
        }

        private void initLumiDetailedColorPicker(LumiDetailedColorPickerPreference item) {
            item.setOnPreferenceChangeListener(this);
            String key = item.getKey();
            if (key.startsWith("argb;")) {
                String[] prefs = key.substring(5).split(";");
                if (prefs.length != 4) {
                    Log.e(LumiMainActivity.LCAT, "color prefs wrong size! : " + prefs.length);
                    return;
                }
                setInitialColor(Color.argb(LumiMainActivity.getSettingInt(this.cr, prefs[0]), LumiMainActivity.getSettingInt(this.cr, prefs[1]), LumiMainActivity.getSettingInt(this.cr, prefs[2]), LumiMainActivity.getSettingInt(this.cr, prefs[3])));
            } else if (key.startsWith("argbf;")) {
                String[] prefs2 = key.substring(6).split(";");
                if (prefs2.length != 4) {
                    Log.e(LumiMainActivity.LCAT, "color prefs wrong size! : " + prefs2.length);
                    return;
                }
                setInitialColor(Color.argb(Integer.valueOf((int) (LumiMainActivity.getSettingFloat(this.cr, prefs2[0]) * 255.0f)).intValue(), Integer.valueOf((int) (LumiMainActivity.getSettingFloat(this.cr, prefs2[1]) * 255.0f)).intValue(), Integer.valueOf((int) (LumiMainActivity.getSettingFloat(this.cr, prefs2[2]) * 255.0f)).intValue(), Integer.valueOf((int) (LumiMainActivity.getSettingFloat(this.cr, prefs2[3]) * 255.0f)).intValue()));
            } else if (key.startsWith("argb_")) {
                String theColor = LumiMainActivity.getSettingString(this.cr, key.substring(5));
                if (theColor == null) {
                    theColor = "#FF33B5E5";
                }
                setInitialColor(Color.parseColor(theColor));
            } else if (key.startsWith("rgb_")) {
                String theColor2 = LumiMainActivity.getSettingString(this.cr, key.substring(4));
                if (theColor2 == null) {
                    theColor2 = "#33B5E5";
                }
                setInitialColor(Color.parseColor(theColor2));
            } else {
                setInitialColor(LumiMainActivity.getSettingInt(this.cr, key));
            }
        }

        public void setInitialColor(int i) {
        }

        private void initSeekBar(Preference item) {
            item.setOnPreferenceChangeListener(this);
        }

        private void initMultiSelectList(MultiSelectListPreference item) {
            item.setOnPreferenceChangeListener(this);
            String initial = LumiMainActivity.getSettingString(this.cr, item.getKey());
            if (initial != null) {
                String[] ival = initial.split(",", 0);
                Set<String> fval = new HashSet();
                for (Object add : ival) {
                    fval.add((String) add);
                }
                item.setValues(fval);
            }
        }

        private void initSwitch(SwitchPreferenceCompat item) {
            if (item != null) {
                item.setOnPreferenceChangeListener(this);
                item.setChecked(LumiMainActivity.getSettingBoolean(this.cr, item.getKey()));
            }
        }

        private void initText(EditTextPreference item) {
            item.setOnPreferenceChangeListener(this);
            item.setText(LumiMainActivity.getSettingString(this.cr, item.getKey()));
        }

        private void initCheckbox(CheckBoxPreference item) {
            if (item != null) {
                item.setOnPreferenceChangeListener(this);
                item.setChecked(LumiMainActivity.getSettingBoolean(this.cr, item.getKey()));
            }
        }

        private void initList(ListPreference item) {
            item.setOnPreferenceChangeListener(this);
            item.setValue(LumiMainActivity.getSettingString(this.cr, item.getKey()));
        }

        private void initCategory(PreferenceCategory category) {
            int items = category.getPreferenceCount();
            for (int i = 0; i < items; i++) {
                initItem(category.getPreference(i));
            }
        }

        private void initScreen(PreferenceScreen category) {
            int items = category.getPreferenceCount();
            for (int i = 0; i < items; i++) {
                initItem(category.getPreference(i));
            }
        }

    }

    public static class LumiNavigationBarFragment extends PreferenceFragmentCompat implements OnPreferenceChangeListener, OnPreferenceClickListener {
        ContentResolver cr;

        class RunToolTask extends AsyncTask<Object, Void, Void> {
            RunToolTask() {
            }

            /* Access modifiers changed, original: protected|varargs */
            public Void doInBackground(Object... params) {
                SettingsTools.dispatch((Context) params[0], (String[]) params[1]);
                return null;
            }

            /* Access modifiers changed, original: protected */
            public void onPostExecute(Void result) {
                Toast.makeText(LumiNavigationBarFragment.this.getActivity(), "Executed", 0).show();
            }
        }

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.long266_nav_bar, rootKey);
            this.cr = getActivity().getContentResolver();
            initPrefs();
        }

        @Override
        public boolean onPreferenceChange(Preference item, Object newValue) {
            Log.d(LumiMainActivity.LCAT, "onPreferenceChange: " + item.getClass().getSimpleName() + " " + item.getKey() + " > " + newValue);
            dispatchItem(item, newValue);
            return true;
        }

        public boolean onPreferenceClick(Preference item) {
            String[] data = new String[0];
            if (item.getKey().startsWith("runactivity_")) {
                String localActivity = item.getKey().substring(new String("runactivity_").length());
                Log.w(LumiMainActivity.LCAT, "Run Local Activity: " + localActivity);
                getActivity().startActivity(new Intent().setClassName(getActivity().getApplicationInfo().packageName, new StringBuilder(String.valueOf(getActivity().getApplicationInfo().packageName)).append(".").append(localActivity).toString()));
            } else if (item.getKey().startsWith("runexternal_")) {
                String externalActivity = item.getKey().substring(new String("runexternal_").length());
                Log.w(LumiMainActivity.LCAT, "Run External Activity: " + externalActivity);
                getActivity().startActivity(new Intent("android.intent.action.MAIN").setClassName(externalActivity.substring(0, externalActivity.lastIndexOf(".")), externalActivity));
            } else if (item.getKey().startsWith("activity;")) {
                data = item.getKey().substring(9).split(";");
                if (data.length != 2) {
                    Log.e(LumiMainActivity.LCAT, "Wrong Params Given: " + item.getKey().substring(9));
                    return false;
                }
                Log.w(LumiMainActivity.LCAT, "Run External Activity: " + data[0] + "/" + data[1]);
                getActivity().startActivity(new Intent("android.intent.action.MAIN").setClassName(data[0], data[1]));
            } else if (item.getKey().startsWith("tool#")) {
                if (item.getKey().substring(5).split("#").length < 1) {
                    Log.e(LumiMainActivity.LCAT, "Wrong Tool Params Given: " + item.getKey().substring(5));
                    return false;
                }
                new RunToolTask().execute(new Object[]{getActivity(), data});
            }
            return true;
        }

        private void dispatchItem(Preference item, Object value) {
            String type = item.getClass().getSimpleName();
            if (type.equals("ListPreference")) {
                dispatchList((ListPreference) item, value);
            } else if (type.equals("CheckBoxPreference")) {
                dispatchCheckbox((CheckBoxPreference) item, value);
            } else if (type.equals("MultiSelectListPreference")) {
                dispatchMultiSelectList((MultiSelectListPreference) item, (Set) value);
            } else if (type.equals("EditTextPreference")) {
                dispatchText((EditTextPreference) item, value);
            } else if (type.equals("SwitchPreferenceCompat")) {
                dispatchSwitch((SwitchPreferenceCompat) item, value);
            } else if (type.equals("LumiColorPickerPreference")) {
                dispatchLumiColorPicker((LumiColorPickerPreference) item, value);
            } else if (type.equals("LumiDetailedColorPickerPreference")) {
                dispatchLumiDetailedColorPicker((LumiDetailedColorPickerPreference) item, value);
            } else if (type.equals("SeekBarPreference")) {
                dispatchSeekBar(item, value);
            } else {
                Log.e(LumiMainActivity.LCAT, "Need to implement: " + type);
            }
        }

        private void dispatchLumiDetailedColorPicker(LumiDetailedColorPickerPreference item, Object value) {
            if (item != null) {
                String key = item.getKey();
                String[] prefs;
                String key1;
                String key2;
                String key3;
                String key4;
                String val;
                if (key.startsWith("argb;")) {
                    prefs = key.substring(5).split(";");
                    if (prefs.length != 4) {
                        Log.e(LumiMainActivity.LCAT, "color prefs wrong size! : " + prefs.length);
                        return;
                    }
                    key1 = prefs[0];
                    int val1 = Color.alpha(((Integer) value).intValue());
                    Log.d(LumiMainActivity.LCAT, "ARGBS color prefs alpha: " + key1 + " > " + val1);
                    if (key1.length() > 0) {
                        LumiMainActivity.setSettingInt(this.cr, key1, val1);
                    }
                    key2 = prefs[1];
                    int val2 = Color.red(((Integer) value).intValue());
                    Log.d(LumiMainActivity.LCAT, "ARGBS color prefs red: " + key2 + " > " + val2);
                    if (key2.length() > 0) {
                        LumiMainActivity.setSettingInt(this.cr, key2, val2);
                    }
                    key3 = prefs[2];
                    int val3 = Color.green(((Integer) value).intValue());
                    Log.d(LumiMainActivity.LCAT, "ARGBS color prefs green: " + key3 + " > " + val3);
                    if (key3.length() > 0) {
                        LumiMainActivity.setSettingInt(this.cr, key3, val3);
                    }
                    key4 = prefs[3];
                    int val4 = Color.blue(((Integer) value).intValue());
                    Log.d(LumiMainActivity.LCAT, "ARGBS color prefs blue: " + key4 + " > " + val4);
                    if (key4.length() > 0) {
                        LumiMainActivity.setSettingInt(this.cr, key4, val4);
                    }
                } else if (key.startsWith("argbf;")) {
                    prefs = key.substring(6).split(";");
                    if (prefs.length != 4) {
                        Log.e(LumiMainActivity.LCAT, "color prefs wrong size! : " + prefs.length);
                        return;
                    }
                    key1 = prefs[0];
                    float val12 = ((float) Color.alpha(((Integer) value).intValue())) / 255.0f;
                    Log.d(LumiMainActivity.LCAT, "ARGBFS color prefs alpha: " + key1 + " > " + val12);
                    if (key1.length() > 0) {
                        LumiMainActivity.setSettingFloat(this.cr, key1, val12);
                    }
                    key2 = prefs[1];
                    float val22 = ((float) Color.red(((Integer) value).intValue())) / 255.0f;
                    Log.d(LumiMainActivity.LCAT, "ARGBFS color prefs red: " + key2 + " > " + val22);
                    if (key2.length() > 0) {
                        LumiMainActivity.setSettingFloat(this.cr, key2, val22);
                    }
                    key3 = prefs[2];
                    float val32 = ((float) Color.green(((Integer) value).intValue())) / 255.0f;
                    Log.d(LumiMainActivity.LCAT, "ARGBFS color prefs green: " + key3 + " > " + val32);
                    if (key3.length() > 0) {
                        LumiMainActivity.setSettingFloat(this.cr, key3, val32);
                    }
                    key4 = prefs[3];
                    float val42 = ((float) Color.blue(((Integer) value).intValue())) / 255.0f;
                    Log.d(LumiMainActivity.LCAT, "ARGBFS color prefs blue: " + key4 + " > " + val42);
                    if (key4.length() > 0) {
                        LumiMainActivity.setSettingFloat(this.cr, key4, val42);
                    }
                } else if (key.startsWith("argb_")) {
                    key = key.substring(5);
                    val = getRGB(((Integer) value).intValue(), true);
                    Log.d(LumiMainActivity.LCAT, "ARGB color prefs: " + key + " > " + val);
                    LumiMainActivity.setSettingString(this.cr, key, val);
                } else if (key.startsWith("rgb_")) {
                    key = key.substring(4);
                    val = getRGB(((Integer) value).intValue(), false);
                    Log.d(LumiMainActivity.LCAT, "RGB color prefs: " + key + " > " + val);
                    LumiMainActivity.setSettingString(this.cr, key, val);
                } else {
                    LumiMainActivity.setSettingInt(this.cr, key, ((Integer) value).intValue());
                }
            }
        }

        private void dispatchLumiColorPicker(LumiColorPickerPreference item, Object value) {
            if (item != null) {
                String key = item.getKey();
                String[] prefs;
                String key1;
                String key2;
                String key3;
                String key4;
                String val;
                if (key.startsWith("argb;")) {
                    prefs = key.substring(5).split(";");
                    if (prefs.length != 4) {
                        Log.e(LumiMainActivity.LCAT, "color prefs wrong size! : " + prefs.length);
                        return;
                    }
                    key1 = prefs[0];
                    int val1 = Color.alpha(((Integer) value).intValue());
                    Log.d(LumiMainActivity.LCAT, "ARGBS color prefs alpha: " + key1 + " > " + val1);
                    if (key1.length() > 0) {
                        LumiMainActivity.setSettingInt(this.cr, key1, val1);
                    }
                    key2 = prefs[1];
                    int val2 = Color.red(((Integer) value).intValue());
                    Log.d(LumiMainActivity.LCAT, "ARGBS color prefs red: " + key2 + " > " + val2);
                    if (key2.length() > 0) {
                        LumiMainActivity.setSettingInt(this.cr, key2, val2);
                    }
                    key3 = prefs[2];
                    int val3 = Color.green(((Integer) value).intValue());
                    Log.d(LumiMainActivity.LCAT, "ARGBS color prefs green: " + key3 + " > " + val3);
                    if (key3.length() > 0) {
                        LumiMainActivity.setSettingInt(this.cr, key3, val3);
                    }
                    key4 = prefs[3];
                    int val4 = Color.blue(((Integer) value).intValue());
                    Log.d(LumiMainActivity.LCAT, "ARGBS color prefs blue: " + key4 + " > " + val4);
                    if (key4.length() > 0) {
                        LumiMainActivity.setSettingInt(this.cr, key4, val4);
                    }
                } else if (key.startsWith("argbf;")) {
                    prefs = key.substring(6).split(";");
                    if (prefs.length != 4) {
                        Log.e(LumiMainActivity.LCAT, "color prefs wrong size! : " + prefs.length);
                        return;
                    }
                    key1 = prefs[0];
                    float val12 = ((float) Color.alpha(((Integer) value).intValue())) / 255.0f;
                    Log.d(LumiMainActivity.LCAT, "ARGBFS color prefs alpha: " + key1 + " > " + val12);
                    if (key1.length() > 0) {
                        LumiMainActivity.setSettingFloat(this.cr, key1, val12);
                    }
                    key2 = prefs[1];
                    float val22 = ((float) Color.red(((Integer) value).intValue())) / 255.0f;
                    Log.d(LumiMainActivity.LCAT, "ARGBFS color prefs red: " + key2 + " > " + val22);
                    if (key2.length() > 0) {
                        LumiMainActivity.setSettingFloat(this.cr, key2, val22);
                    }
                    key3 = prefs[2];
                    float val32 = ((float) Color.green(((Integer) value).intValue())) / 255.0f;
                    Log.d(LumiMainActivity.LCAT, "ARGBFS color prefs green: " + key3 + " > " + val32);
                    if (key3.length() > 0) {
                        LumiMainActivity.setSettingFloat(this.cr, key3, val32);
                    }
                    key4 = prefs[3];
                    float val42 = ((float) Color.blue(((Integer) value).intValue())) / 255.0f;
                    Log.d(LumiMainActivity.LCAT, "ARGBFS color prefs blue: " + key4 + " > " + val42);
                    if (key4.length() > 0) {
                        LumiMainActivity.setSettingFloat(this.cr, key4, val42);
                    }
                } else if (key.startsWith("argb_")) {
                    key = key.substring(5);
                    val = getRGB(((Integer) value).intValue(), true);
                    Log.d(LumiMainActivity.LCAT, "ARGB color prefs: " + key + " > " + val);
                    LumiMainActivity.setSettingString(this.cr, key, val);
                } else if (key.startsWith("rgb_")) {
                    key = key.substring(4);
                    val = getRGB(((Integer) value).intValue(), false);
                    Log.d(LumiMainActivity.LCAT, "RGB color prefs: " + key + " > " + val);
                    LumiMainActivity.setSettingString(this.cr, key, val);
                } else {
                    LumiMainActivity.setSettingInt(this.cr, key, ((Integer) value).intValue());
                }
            }
        }

        private String getRGB(int color, boolean hasAlpha) {
            int red = Color.red(color);
            int green = Color.green(color);
            int blue = Color.blue(color);
            int alpha = Color.alpha(color);
            String out = "#";
            if (hasAlpha) {
                out = new StringBuilder(String.valueOf(out)).append(alpha < 17 ? "0" : "").append(Integer.toHexString(alpha)).toString();
            }
            return new StringBuilder(String.valueOf(out)).append(red < 17 ? "0" : "").append(Integer.toHexString(red)).append(green < 17 ? "0" : "").append(Integer.toHexString(green)).append(blue < 17 ? "0" : "").append(Integer.toHexString(blue)).toString();
        }

        private void dispatchSeekBar(Preference item, Object value) {
            if (item != null) {
                LumiMainActivity.setSettingInt(this.cr, item.getKey(), ((Integer) value).intValue());
            }
        }

        private void dispatchMultiSelectList(MultiSelectListPreference item, Set<String> value) {
            if (item != null) {
                String val = "";
                Iterator<String> iterator = value.iterator();
                while (iterator.hasNext()) {
                    val = new StringBuilder(String.valueOf(val)).append((String) iterator.next()).toString();
                    if (iterator.hasNext()) {
                        val = new StringBuilder(String.valueOf(val)).append(",").toString();
                    }
                }
                LumiMainActivity.setSettingString(this.cr, item.getKey(), val);
            }
        }

        private void dispatchText(EditTextPreference item, Object value) {
            if (item != null) {
                LumiMainActivity.setSettingString(this.cr, item.getKey(), (String) value);
            }
        }

        private void dispatchCheckbox(CheckBoxPreference item, Object value) {
            if (item != null) {
                LumiMainActivity.setSettingBoolean(this.cr, item.getKey(), ((Boolean) value).booleanValue());
            }
        }

        private void dispatchSwitch(SwitchPreferenceCompat item, Object value) {
            if (item != null) {
                LumiMainActivity.setSettingBoolean(this.cr, item.getKey(), ((Boolean) value).booleanValue());
            }
        }

        private void dispatchList(ListPreference item, Object value) {
            if (item != null) {
                LumiMainActivity.setSettingString(this.cr, item.getKey(), (String) value);
            }
        }

        private void initPrefs() {
            int items = getPreferenceScreen().getPreferenceCount();
            for (int i = 0; i < items; i++) {
                initItem(getPreferenceScreen().getPreference(i));
            }
        }

        private void initItem(Preference item) {
            String type = item.getClass().getSimpleName();
            if (type.equals("PreferenceCategory")) {
                initCategory((PreferenceCategory) item);
            } else if (type.equals("PreferenceScreen")) {
                initScreen((PreferenceScreen) item);
            } else if (type.equals("CheckBoxPreference")) {
                initCheckbox((CheckBoxPreference) item);
            } else if (type.equals("MultiSelectListPreference")) {
                initMultiSelectList((MultiSelectListPreference) item);
            } else if (type.equals("EditTextPreference")) {
                initText((EditTextPreference) item);
            } else if (type.equals("ListPreference")) {
                initList((ListPreference) item);
            } else if (type.equals("SwitchPreferenceCompat")) {
                initSwitch((SwitchPreferenceCompat) item);
            } else if (type.equals("LumiColorPickerPreference")) {
                initLumiColorPicker((LumiColorPickerPreference) item);
            } else if (type.equals("LumiDetailedColorPickerPreference")) {
                initLumiDetailedColorPicker((LumiDetailedColorPickerPreference) item);
            } else if (type.equals("SeekBarPreference")) {
                initSeekBar(item);
            } else {
                Log.e(LumiMainActivity.LCAT, "Need to implement: " + type);
            }
        }

        private void initLumiColorPicker(LumiColorPickerPreference item) {
            item.setOnPreferenceChangeListener(this);
            String key = item.getKey();
            if (key.startsWith("argb;")) {
                String[] prefs = key.substring(5).split(";");
                if (prefs.length != 4) {
                    Log.e(LumiMainActivity.LCAT, "color prefs wrong size! : " + prefs.length);
                    return;
                }
                setInitialColor(Color.argb(LumiMainActivity.getSettingInt(this.cr, prefs[0]), LumiMainActivity.getSettingInt(this.cr, prefs[1]), LumiMainActivity.getSettingInt(this.cr, prefs[2]), LumiMainActivity.getSettingInt(this.cr, prefs[3])));
            } else if (key.startsWith("argbf;")) {
                String[] prefs2 = key.substring(6).split(";");
                if (prefs2.length != 4) {
                    Log.e(LumiMainActivity.LCAT, "color prefs wrong size! : " + prefs2.length);
                    return;
                }
                setInitialColor(Color.argb(Integer.valueOf((int) (LumiMainActivity.getSettingFloat(this.cr, prefs2[0]) * 255.0f)).intValue(), Integer.valueOf((int) (LumiMainActivity.getSettingFloat(this.cr, prefs2[1]) * 255.0f)).intValue(), Integer.valueOf((int) (LumiMainActivity.getSettingFloat(this.cr, prefs2[2]) * 255.0f)).intValue(), Integer.valueOf((int) (LumiMainActivity.getSettingFloat(this.cr, prefs2[3]) * 255.0f)).intValue()));
            } else if (key.startsWith("argb_")) {
                String theColor = LumiMainActivity.getSettingString(this.cr, key.substring(5));
                if (theColor == null) {
                    theColor = "#FF33B5E5";
                }
                setInitialColor(Color.parseColor(theColor));
            } else if (key.startsWith("rgb_")) {
                String theColor2 = LumiMainActivity.getSettingString(this.cr, key.substring(4));
                if (theColor2 == null) {
                    theColor2 = "#33B5E5";
                }
                setInitialColor(Color.parseColor(theColor2));
            } else {
                setInitialColor(LumiMainActivity.getSettingInt(this.cr, key));
            }
        }

        private void initLumiDetailedColorPicker(LumiDetailedColorPickerPreference item) {
            item.setOnPreferenceChangeListener(this);
            String key = item.getKey();
            if (key.startsWith("argb;")) {
                String[] prefs = key.substring(5).split(";");
                if (prefs.length != 4) {
                    Log.e(LumiMainActivity.LCAT, "color prefs wrong size! : " + prefs.length);
                    return;
                }
                setInitialColor(Color.argb(LumiMainActivity.getSettingInt(this.cr, prefs[0]), LumiMainActivity.getSettingInt(this.cr, prefs[1]), LumiMainActivity.getSettingInt(this.cr, prefs[2]), LumiMainActivity.getSettingInt(this.cr, prefs[3])));
            } else if (key.startsWith("argbf;")) {
                String[] prefs2 = key.substring(6).split(";");
                if (prefs2.length != 4) {
                    Log.e(LumiMainActivity.LCAT, "color prefs wrong size! : " + prefs2.length);
                    return;
                }
                setInitialColor(Color.argb(Integer.valueOf((int) (LumiMainActivity.getSettingFloat(this.cr, prefs2[0]) * 255.0f)).intValue(), Integer.valueOf((int) (LumiMainActivity.getSettingFloat(this.cr, prefs2[1]) * 255.0f)).intValue(), Integer.valueOf((int) (LumiMainActivity.getSettingFloat(this.cr, prefs2[2]) * 255.0f)).intValue(), Integer.valueOf((int) (LumiMainActivity.getSettingFloat(this.cr, prefs2[3]) * 255.0f)).intValue()));
            } else if (key.startsWith("argb_")) {
                String theColor = LumiMainActivity.getSettingString(this.cr, key.substring(5));
                if (theColor == null) {
                    theColor = "#FF33B5E5";
                }
                setInitialColor(Color.parseColor(theColor));
            } else if (key.startsWith("rgb_")) {
                String theColor2 = LumiMainActivity.getSettingString(this.cr, key.substring(4));
                if (theColor2 == null) {
                    theColor2 = "#33B5E5";
                }
                setInitialColor(Color.parseColor(theColor2));
            } else {
                setInitialColor(LumiMainActivity.getSettingInt(this.cr, key));
            }
        }

        public void setInitialColor(int i) {
        }

        private void initSeekBar(Preference item) {
            item.setOnPreferenceChangeListener(this);
        }

        private void initMultiSelectList(MultiSelectListPreference item) {
            item.setOnPreferenceChangeListener(this);
            String initial = LumiMainActivity.getSettingString(this.cr, item.getKey());
            if (initial != null) {
                String[] ival = initial.split(",", 0);
                Set<String> fval = new HashSet();
                for (Object add : ival) {
                    fval.add((String) add);
                }
                item.setValues(fval);
            }
        }

        private void initSwitch(SwitchPreferenceCompat item) {
            if (item != null) {
                item.setOnPreferenceChangeListener(this);
                item.setChecked(LumiMainActivity.getSettingBoolean(this.cr, item.getKey()));
            }
        }

        private void initText(EditTextPreference item) {
            item.setOnPreferenceChangeListener(this);
            item.setText(LumiMainActivity.getSettingString(this.cr, item.getKey()));
        }

        private void initCheckbox(CheckBoxPreference item) {
            if (item != null) {
                item.setOnPreferenceChangeListener(this);
                item.setChecked(LumiMainActivity.getSettingBoolean(this.cr, item.getKey()));
            }
        }

        private void initList(ListPreference item) {
            item.setOnPreferenceChangeListener(this);
            item.setValue(LumiMainActivity.getSettingString(this.cr, item.getKey()));
        }

        private void initCategory(PreferenceCategory category) {
            int items = category.getPreferenceCount();
            for (int i = 0; i < items; i++) {
                initItem(category.getPreference(i));
            }
        }

        private void initScreen(PreferenceScreen category) {
            int items = category.getPreferenceCount();
            for (int i = 0; i < items; i++) {
                initItem(category.getPreference(i));
            }
        }

    }

    public static class LumiAddOnsFragment extends PreferenceFragmentCompat implements OnPreferenceChangeListener, OnPreferenceClickListener {
        ContentResolver cr;

        class RunToolTask extends AsyncTask<Object, Void, Void> {
            RunToolTask() {
            }

            /* Access modifiers changed, original: protected|varargs */
            public Void doInBackground(Object... params) {
                SettingsTools.dispatch((Context) params[0], (String[]) params[1]);
                return null;
            }

            /* Access modifiers changed, original: protected */
            public void onPostExecute(Void result) {
                Toast.makeText(LumiAddOnsFragment.this.getActivity(), "Executed", 0).show();
            }
        }

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.long266_add_ons, rootKey);
            this.cr = getActivity().getContentResolver();
            initPrefs();
        }

        @Override
        public boolean onPreferenceChange(Preference item, Object newValue) {
            Log.d(LumiMainActivity.LCAT, "onPreferenceChange: " + item.getClass().getSimpleName() + " " + item.getKey() + " > " + newValue);
            dispatchItem(item, newValue);
            return true;
        }

        public boolean onPreferenceClick(Preference item) {
            String[] data = new String[0];
            if (item.getKey().startsWith("runactivity_")) {
                String localActivity = item.getKey().substring(new String("runactivity_").length());
                Log.w(LumiMainActivity.LCAT, "Run Local Activity: " + localActivity);
                getActivity().startActivity(new Intent().setClassName(getActivity().getApplicationInfo().packageName, new StringBuilder(String.valueOf(getActivity().getApplicationInfo().packageName)).append(".").append(localActivity).toString()));
            } else if (item.getKey().startsWith("runexternal_")) {
                String externalActivity = item.getKey().substring(new String("runexternal_").length());
                Log.w(LumiMainActivity.LCAT, "Run External Activity: " + externalActivity);
                getActivity().startActivity(new Intent("android.intent.action.MAIN").setClassName(externalActivity.substring(0, externalActivity.lastIndexOf(".")), externalActivity));
            } else if (item.getKey().startsWith("activity;")) {
                data = item.getKey().substring(9).split(";");
                if (data.length != 2) {
                    Log.e(LumiMainActivity.LCAT, "Wrong Params Given: " + item.getKey().substring(9));
                    return false;
                }
                Log.w(LumiMainActivity.LCAT, "Run External Activity: " + data[0] + "/" + data[1]);
                getActivity().startActivity(new Intent("android.intent.action.MAIN").setClassName(data[0], data[1]));
            } else if (item.getKey().startsWith("tool#")) {
                if (item.getKey().substring(5).split("#").length < 1) {
                    Log.e(LumiMainActivity.LCAT, "Wrong Tool Params Given: " + item.getKey().substring(5));
                    return false;
                }
                new RunToolTask().execute(new Object[]{getActivity(), data});
            }
            return true;
        }

        private void dispatchItem(Preference item, Object value) {
            String type = item.getClass().getSimpleName();
            if (type.equals("ListPreference")) {
                dispatchList((ListPreference) item, value);
            } else if (type.equals("CheckBoxPreference")) {
                dispatchCheckbox((CheckBoxPreference) item, value);
            } else if (type.equals("MultiSelectListPreference")) {
                dispatchMultiSelectList((MultiSelectListPreference) item, (Set) value);
            } else if (type.equals("EditTextPreference")) {
                dispatchText((EditTextPreference) item, value);
            } else if (type.equals("SwitchPreferenceCompat")) {
                dispatchSwitch((SwitchPreferenceCompat) item, value);
            } else if (type.equals("LumiColorPickerPreference")) {
                dispatchLumiColorPicker((LumiColorPickerPreference) item, value);
            } else if (type.equals("LumiDetailedColorPickerPreference")) {
                dispatchLumiDetailedColorPicker((LumiDetailedColorPickerPreference) item, value);
            } else if (type.equals("SeekBarPreference")) {
                dispatchSeekBar(item, value);
            } else {
                Log.e(LumiMainActivity.LCAT, "Need to implement: " + type);
            }
        }

        private void dispatchLumiDetailedColorPicker(LumiDetailedColorPickerPreference item, Object value) {
            if (item != null) {
                String key = item.getKey();
                String[] prefs;
                String key1;
                String key2;
                String key3;
                String key4;
                String val;
                if (key.startsWith("argb;")) {
                    prefs = key.substring(5).split(";");
                    if (prefs.length != 4) {
                        Log.e(LumiMainActivity.LCAT, "color prefs wrong size! : " + prefs.length);
                        return;
                    }
                    key1 = prefs[0];
                    int val1 = Color.alpha(((Integer) value).intValue());
                    Log.d(LumiMainActivity.LCAT, "ARGBS color prefs alpha: " + key1 + " > " + val1);
                    if (key1.length() > 0) {
                        LumiMainActivity.setSettingInt(this.cr, key1, val1);
                    }
                    key2 = prefs[1];
                    int val2 = Color.red(((Integer) value).intValue());
                    Log.d(LumiMainActivity.LCAT, "ARGBS color prefs red: " + key2 + " > " + val2);
                    if (key2.length() > 0) {
                        LumiMainActivity.setSettingInt(this.cr, key2, val2);
                    }
                    key3 = prefs[2];
                    int val3 = Color.green(((Integer) value).intValue());
                    Log.d(LumiMainActivity.LCAT, "ARGBS color prefs green: " + key3 + " > " + val3);
                    if (key3.length() > 0) {
                        LumiMainActivity.setSettingInt(this.cr, key3, val3);
                    }
                    key4 = prefs[3];
                    int val4 = Color.blue(((Integer) value).intValue());
                    Log.d(LumiMainActivity.LCAT, "ARGBS color prefs blue: " + key4 + " > " + val4);
                    if (key4.length() > 0) {
                        LumiMainActivity.setSettingInt(this.cr, key4, val4);
                    }
                } else if (key.startsWith("argbf;")) {
                    prefs = key.substring(6).split(";");
                    if (prefs.length != 4) {
                        Log.e(LumiMainActivity.LCAT, "color prefs wrong size! : " + prefs.length);
                        return;
                    }
                    key1 = prefs[0];
                    float val12 = ((float) Color.alpha(((Integer) value).intValue())) / 255.0f;
                    Log.d(LumiMainActivity.LCAT, "ARGBFS color prefs alpha: " + key1 + " > " + val12);
                    if (key1.length() > 0) {
                        LumiMainActivity.setSettingFloat(this.cr, key1, val12);
                    }
                    key2 = prefs[1];
                    float val22 = ((float) Color.red(((Integer) value).intValue())) / 255.0f;
                    Log.d(LumiMainActivity.LCAT, "ARGBFS color prefs red: " + key2 + " > " + val22);
                    if (key2.length() > 0) {
                        LumiMainActivity.setSettingFloat(this.cr, key2, val22);
                    }
                    key3 = prefs[2];
                    float val32 = ((float) Color.green(((Integer) value).intValue())) / 255.0f;
                    Log.d(LumiMainActivity.LCAT, "ARGBFS color prefs green: " + key3 + " > " + val32);
                    if (key3.length() > 0) {
                        LumiMainActivity.setSettingFloat(this.cr, key3, val32);
                    }
                    key4 = prefs[3];
                    float val42 = ((float) Color.blue(((Integer) value).intValue())) / 255.0f;
                    Log.d(LumiMainActivity.LCAT, "ARGBFS color prefs blue: " + key4 + " > " + val42);
                    if (key4.length() > 0) {
                        LumiMainActivity.setSettingFloat(this.cr, key4, val42);
                    }
                } else if (key.startsWith("argb_")) {
                    key = key.substring(5);
                    val = getRGB(((Integer) value).intValue(), true);
                    Log.d(LumiMainActivity.LCAT, "ARGB color prefs: " + key + " > " + val);
                    LumiMainActivity.setSettingString(this.cr, key, val);
                } else if (key.startsWith("rgb_")) {
                    key = key.substring(4);
                    val = getRGB(((Integer) value).intValue(), false);
                    Log.d(LumiMainActivity.LCAT, "RGB color prefs: " + key + " > " + val);
                    LumiMainActivity.setSettingString(this.cr, key, val);
                } else {
                    LumiMainActivity.setSettingInt(this.cr, key, ((Integer) value).intValue());
                }
            }
        }

        private void dispatchLumiColorPicker(LumiColorPickerPreference item, Object value) {
            if (item != null) {
                String key = item.getKey();
                String[] prefs;
                String key1;
                String key2;
                String key3;
                String key4;
                String val;
                if (key.startsWith("argb;")) {
                    prefs = key.substring(5).split(";");
                    if (prefs.length != 4) {
                        Log.e(LumiMainActivity.LCAT, "color prefs wrong size! : " + prefs.length);
                        return;
                    }
                    key1 = prefs[0];
                    int val1 = Color.alpha(((Integer) value).intValue());
                    Log.d(LumiMainActivity.LCAT, "ARGBS color prefs alpha: " + key1 + " > " + val1);
                    if (key1.length() > 0) {
                        LumiMainActivity.setSettingInt(this.cr, key1, val1);
                    }
                    key2 = prefs[1];
                    int val2 = Color.red(((Integer) value).intValue());
                    Log.d(LumiMainActivity.LCAT, "ARGBS color prefs red: " + key2 + " > " + val2);
                    if (key2.length() > 0) {
                        LumiMainActivity.setSettingInt(this.cr, key2, val2);
                    }
                    key3 = prefs[2];
                    int val3 = Color.green(((Integer) value).intValue());
                    Log.d(LumiMainActivity.LCAT, "ARGBS color prefs green: " + key3 + " > " + val3);
                    if (key3.length() > 0) {
                        LumiMainActivity.setSettingInt(this.cr, key3, val3);
                    }
                    key4 = prefs[3];
                    int val4 = Color.blue(((Integer) value).intValue());
                    Log.d(LumiMainActivity.LCAT, "ARGBS color prefs blue: " + key4 + " > " + val4);
                    if (key4.length() > 0) {
                        LumiMainActivity.setSettingInt(this.cr, key4, val4);
                    }
                } else if (key.startsWith("argbf;")) {
                    prefs = key.substring(6).split(";");
                    if (prefs.length != 4) {
                        Log.e(LumiMainActivity.LCAT, "color prefs wrong size! : " + prefs.length);
                        return;
                    }
                    key1 = prefs[0];
                    float val12 = ((float) Color.alpha(((Integer) value).intValue())) / 255.0f;
                    Log.d(LumiMainActivity.LCAT, "ARGBFS color prefs alpha: " + key1 + " > " + val12);
                    if (key1.length() > 0) {
                        LumiMainActivity.setSettingFloat(this.cr, key1, val12);
                    }
                    key2 = prefs[1];
                    float val22 = ((float) Color.red(((Integer) value).intValue())) / 255.0f;
                    Log.d(LumiMainActivity.LCAT, "ARGBFS color prefs red: " + key2 + " > " + val22);
                    if (key2.length() > 0) {
                        LumiMainActivity.setSettingFloat(this.cr, key2, val22);
                    }
                    key3 = prefs[2];
                    float val32 = ((float) Color.green(((Integer) value).intValue())) / 255.0f;
                    Log.d(LumiMainActivity.LCAT, "ARGBFS color prefs green: " + key3 + " > " + val32);
                    if (key3.length() > 0) {
                        LumiMainActivity.setSettingFloat(this.cr, key3, val32);
                    }
                    key4 = prefs[3];
                    float val42 = ((float) Color.blue(((Integer) value).intValue())) / 255.0f;
                    Log.d(LumiMainActivity.LCAT, "ARGBFS color prefs blue: " + key4 + " > " + val42);
                    if (key4.length() > 0) {
                        LumiMainActivity.setSettingFloat(this.cr, key4, val42);
                    }
                } else if (key.startsWith("argb_")) {
                    key = key.substring(5);
                    val = getRGB(((Integer) value).intValue(), true);
                    Log.d(LumiMainActivity.LCAT, "ARGB color prefs: " + key + " > " + val);
                    LumiMainActivity.setSettingString(this.cr, key, val);
                } else if (key.startsWith("rgb_")) {
                    key = key.substring(4);
                    val = getRGB(((Integer) value).intValue(), false);
                    Log.d(LumiMainActivity.LCAT, "RGB color prefs: " + key + " > " + val);
                    LumiMainActivity.setSettingString(this.cr, key, val);
                } else {
                    LumiMainActivity.setSettingInt(this.cr, key, ((Integer) value).intValue());
                }
            }
        }

        private String getRGB(int color, boolean hasAlpha) {
            int red = Color.red(color);
            int green = Color.green(color);
            int blue = Color.blue(color);
            int alpha = Color.alpha(color);
            String out = "#";
            if (hasAlpha) {
                out = new StringBuilder(String.valueOf(out)).append(alpha < 17 ? "0" : "").append(Integer.toHexString(alpha)).toString();
            }
            return new StringBuilder(String.valueOf(out)).append(red < 17 ? "0" : "").append(Integer.toHexString(red)).append(green < 17 ? "0" : "").append(Integer.toHexString(green)).append(blue < 17 ? "0" : "").append(Integer.toHexString(blue)).toString();
        }

        private void dispatchSeekBar(Preference item, Object value) {
            if (item != null) {
                LumiMainActivity.setSettingInt(this.cr, item.getKey(), ((Integer) value).intValue());
            }
        }

        private void dispatchMultiSelectList(MultiSelectListPreference item, Set<String> value) {
            if (item != null) {
                String val = "";
                Iterator<String> iterator = value.iterator();
                while (iterator.hasNext()) {
                    val = new StringBuilder(String.valueOf(val)).append((String) iterator.next()).toString();
                    if (iterator.hasNext()) {
                        val = new StringBuilder(String.valueOf(val)).append(",").toString();
                    }
                }
                LumiMainActivity.setSettingString(this.cr, item.getKey(), val);
            }
        }

        private void dispatchText(EditTextPreference item, Object value) {
            if (item != null) {
                LumiMainActivity.setSettingString(this.cr, item.getKey(), (String) value);
            }
        }

        private void dispatchCheckbox(CheckBoxPreference item, Object value) {
            if (item != null) {
                LumiMainActivity.setSettingBoolean(this.cr, item.getKey(), ((Boolean) value).booleanValue());
            }
        }

        private void dispatchSwitch(SwitchPreferenceCompat item, Object value) {
            if (item != null) {
                LumiMainActivity.setSettingBoolean(this.cr, item.getKey(), ((Boolean) value).booleanValue());
            }
        }

        private void dispatchList(ListPreference item, Object value) {
            if (item != null) {
                LumiMainActivity.setSettingString(this.cr, item.getKey(), (String) value);
            }
        }

        private void initPrefs() {
            int items = getPreferenceScreen().getPreferenceCount();
            for (int i = 0; i < items; i++) {
                initItem(getPreferenceScreen().getPreference(i));
            }
        }

        private void initItem(Preference item) {
            String type = item.getClass().getSimpleName();
            if (type.equals("PreferenceCategory")) {
                initCategory((PreferenceCategory) item);
            } else if (type.equals("PreferenceScreen")) {
                initScreen((PreferenceScreen) item);
            } else if (type.equals("CheckBoxPreference")) {
                initCheckbox((CheckBoxPreference) item);
            } else if (type.equals("MultiSelectListPreference")) {
                initMultiSelectList((MultiSelectListPreference) item);
            } else if (type.equals("EditTextPreference")) {
                initText((EditTextPreference) item);
            } else if (type.equals("ListPreference")) {
                initList((ListPreference) item);
            } else if (type.equals("SwitchPreferenceCompat")) {
                initSwitch((SwitchPreferenceCompat) item);
            } else if (type.equals("LumiColorPickerPreference")) {
                initLumiColorPicker((LumiColorPickerPreference) item);
            } else if (type.equals("LumiDetailedColorPickerPreference")) {
                initLumiDetailedColorPicker((LumiDetailedColorPickerPreference) item);
            } else if (type.equals("SeekBarPreference")) {
                initSeekBar(item);
            } else {
                Log.e(LumiMainActivity.LCAT, "Need to implement: " + type);
            }
        }

        private void initLumiColorPicker(LumiColorPickerPreference item) {
            item.setOnPreferenceChangeListener(this);
            String key = item.getKey();
            if (key.startsWith("argb;")) {
                String[] prefs = key.substring(5).split(";");
                if (prefs.length != 4) {
                    Log.e(LumiMainActivity.LCAT, "color prefs wrong size! : " + prefs.length);
                    return;
                }
                setInitialColor(Color.argb(LumiMainActivity.getSettingInt(this.cr, prefs[0]), LumiMainActivity.getSettingInt(this.cr, prefs[1]), LumiMainActivity.getSettingInt(this.cr, prefs[2]), LumiMainActivity.getSettingInt(this.cr, prefs[3])));
            } else if (key.startsWith("argbf;")) {
                String[] prefs2 = key.substring(6).split(";");
                if (prefs2.length != 4) {
                    Log.e(LumiMainActivity.LCAT, "color prefs wrong size! : " + prefs2.length);
                    return;
                }
                setInitialColor(Color.argb(Integer.valueOf((int) (LumiMainActivity.getSettingFloat(this.cr, prefs2[0]) * 255.0f)).intValue(), Integer.valueOf((int) (LumiMainActivity.getSettingFloat(this.cr, prefs2[1]) * 255.0f)).intValue(), Integer.valueOf((int) (LumiMainActivity.getSettingFloat(this.cr, prefs2[2]) * 255.0f)).intValue(), Integer.valueOf((int) (LumiMainActivity.getSettingFloat(this.cr, prefs2[3]) * 255.0f)).intValue()));
            } else if (key.startsWith("argb_")) {
                String theColor = LumiMainActivity.getSettingString(this.cr, key.substring(5));
                if (theColor == null) {
                    theColor = "#FF33B5E5";
                }
                setInitialColor(Color.parseColor(theColor));
            } else if (key.startsWith("rgb_")) {
                String theColor2 = LumiMainActivity.getSettingString(this.cr, key.substring(4));
                if (theColor2 == null) {
                    theColor2 = "#33B5E5";
                }
                setInitialColor(Color.parseColor(theColor2));
            } else {
                setInitialColor(LumiMainActivity.getSettingInt(this.cr, key));
            }
        }

        private void initLumiDetailedColorPicker(LumiDetailedColorPickerPreference item) {
            item.setOnPreferenceChangeListener(this);
            String key = item.getKey();
            if (key.startsWith("argb;")) {
                String[] prefs = key.substring(5).split(";");
                if (prefs.length != 4) {
                    Log.e(LumiMainActivity.LCAT, "color prefs wrong size! : " + prefs.length);
                    return;
                }
                setInitialColor(Color.argb(LumiMainActivity.getSettingInt(this.cr, prefs[0]), LumiMainActivity.getSettingInt(this.cr, prefs[1]), LumiMainActivity.getSettingInt(this.cr, prefs[2]), LumiMainActivity.getSettingInt(this.cr, prefs[3])));
            } else if (key.startsWith("argbf;")) {
                String[] prefs2 = key.substring(6).split(";");
                if (prefs2.length != 4) {
                    Log.e(LumiMainActivity.LCAT, "color prefs wrong size! : " + prefs2.length);
                    return;
                }
                setInitialColor(Color.argb(Integer.valueOf((int) (LumiMainActivity.getSettingFloat(this.cr, prefs2[0]) * 255.0f)).intValue(), Integer.valueOf((int) (LumiMainActivity.getSettingFloat(this.cr, prefs2[1]) * 255.0f)).intValue(), Integer.valueOf((int) (LumiMainActivity.getSettingFloat(this.cr, prefs2[2]) * 255.0f)).intValue(), Integer.valueOf((int) (LumiMainActivity.getSettingFloat(this.cr, prefs2[3]) * 255.0f)).intValue()));
            } else if (key.startsWith("argb_")) {
                String theColor = LumiMainActivity.getSettingString(this.cr, key.substring(5));
                if (theColor == null) {
                    theColor = "#FF33B5E5";
                }
                setInitialColor(Color.parseColor(theColor));
            } else if (key.startsWith("rgb_")) {
                String theColor2 = LumiMainActivity.getSettingString(this.cr, key.substring(4));
                if (theColor2 == null) {
                    theColor2 = "#33B5E5";
                }
                setInitialColor(Color.parseColor(theColor2));
            } else {
                setInitialColor(LumiMainActivity.getSettingInt(this.cr, key));
            }
        }

        public void setInitialColor(int i) {
        }

        private void initSeekBar(Preference item) {
            item.setOnPreferenceChangeListener(this);
        }

        private void initMultiSelectList(MultiSelectListPreference item) {
            item.setOnPreferenceChangeListener(this);
            String initial = LumiMainActivity.getSettingString(this.cr, item.getKey());
            if (initial != null) {
                String[] ival = initial.split(",", 0);
                Set<String> fval = new HashSet();
                for (Object add : ival) {
                    fval.add((String) add);
                }
                item.setValues(fval);
            }
        }

        private void initSwitch(SwitchPreferenceCompat item) {
            if (item != null) {
                item.setOnPreferenceChangeListener(this);
                item.setChecked(LumiMainActivity.getSettingBoolean(this.cr, item.getKey()));
            }
        }

        private void initText(EditTextPreference item) {
            item.setOnPreferenceChangeListener(this);
            item.setText(LumiMainActivity.getSettingString(this.cr, item.getKey()));
        }

        private void initCheckbox(CheckBoxPreference item) {
            if (item != null) {
                item.setOnPreferenceChangeListener(this);
                item.setChecked(LumiMainActivity.getSettingBoolean(this.cr, item.getKey()));
            }
        }

        private void initList(ListPreference item) {
            item.setOnPreferenceChangeListener(this);
            item.setValue(LumiMainActivity.getSettingString(this.cr, item.getKey()));
        }

        private void initCategory(PreferenceCategory category) {
            int items = category.getPreferenceCount();
            for (int i = 0; i < items; i++) {
                initItem(category.getPreference(i));
            }
        }

        private void initScreen(PreferenceScreen category) {
            int items = category.getPreferenceCount();
            for (int i = 0; i < items; i++) {
                initItem(category.getPreference(i));
            }
        }

    }

}