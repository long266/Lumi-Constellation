package com.nhlong.lumi.constellation;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings.System;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class NavBarActivity extends AppCompatActivity {
    private static final String LCAT = "QuickSettingsActivity";
    private static String MODCFG_FOLDER;
    private static String ROMCFG_FOLDER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.long266_main_navbar_activity);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_navbar, new SettingsFragment())
                .commit();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_navbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ROMCFG_FOLDER = getResources().getString(R.string.romcfg_folder);
        MODCFG_FOLDER = getResources().getString(R.string.modcfg_folder);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
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

    public static class SettingsFragment extends PreferenceFragmentCompat implements OnPreferenceChangeListener, OnPreferenceClickListener {
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
                Toast.makeText(SettingsFragment.this.getActivity(), "Executed", 0).show();
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
            Log.d(NavBarActivity.LCAT, "onPreferenceChange: " + item.getClass().getSimpleName() + " " + item.getKey() + " > " + newValue);
            dispatchItem(item, newValue);
            return true;
        }

        public boolean onPreferenceClick(Preference item) {
            String[] data = new String[0];
            if (item.getKey().startsWith("runactivity_")) {
                String localActivity = item.getKey().substring(new String("runactivity_").length());
                Log.w(NavBarActivity.LCAT, "Run Local Activity: " + localActivity);
                getActivity().startActivity(new Intent().setClassName(getActivity().getApplicationInfo().packageName, new StringBuilder(String.valueOf(getActivity().getApplicationInfo().packageName)).append(".").append(localActivity).toString()));
            } else if (item.getKey().startsWith("runexternal_")) {
                String externalActivity = item.getKey().substring(new String("runexternal_").length());
                Log.w(NavBarActivity.LCAT, "Run External Activity: " + externalActivity);
                getActivity().startActivity(new Intent("android.intent.action.MAIN").setClassName(externalActivity.substring(0, externalActivity.lastIndexOf(".")), externalActivity));
            } else if (item.getKey().startsWith("activity;")) {
                data = item.getKey().substring(9).split(";");
                if (data.length != 2) {
                    Log.e(NavBarActivity.LCAT, "Wrong Params Given: " + item.getKey().substring(9));
                    return false;
                }
                Log.w(NavBarActivity.LCAT, "Run External Activity: " + data[0] + "/" + data[1]);
                getActivity().startActivity(new Intent("android.intent.action.MAIN").setClassName(data[0], data[1]));
            } else if (item.getKey().startsWith("tool#")) {
                if (item.getKey().substring(5).split("#").length < 1) {
                    Log.e(NavBarActivity.LCAT, "Wrong Tool Params Given: " + item.getKey().substring(5));
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
            } else if (type.equals("SeekBarPreference")) {
                dispatchSeekBar(item, value);
            } else {
                Log.e(NavBarActivity.LCAT, "Need to implement: " + type);
            }
        }

        private void dispatchSeekBar(Preference item, Object value) {
            if (item != null) {
                NavBarActivity.setSettingInt(this.cr, item.getKey(), ((Integer) value).intValue());
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
                NavBarActivity.setSettingString(this.cr, item.getKey(), val);
            }
        }

        private void dispatchText(EditTextPreference item, Object value) {
            if (item != null) {
                NavBarActivity.setSettingString(this.cr, item.getKey(), (String) value);
            }
        }

        private void dispatchCheckbox(CheckBoxPreference item, Object value) {
            if (item != null) {
                NavBarActivity.setSettingBoolean(this.cr, item.getKey(), ((Boolean) value).booleanValue());
            }
        }

        private void dispatchSwitch(SwitchPreferenceCompat item, Object value) {
            if (item != null) {
                NavBarActivity.setSettingBoolean(this.cr, item.getKey(), ((Boolean) value).booleanValue());
            }
        }

        private void dispatchList(ListPreference item, Object value) {
            if (item != null) {
                NavBarActivity.setSettingString(this.cr, item.getKey(), (String) value);
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
            } else if (type.equals("SeekBarPreference")) {
                initSeekBar(item);
            } else {
                Log.e(NavBarActivity.LCAT, "Need to implement: " + type);
            }
        }

        private void initSeekBar(Preference item) {
            item.setOnPreferenceChangeListener(this);
        }

        private void initMultiSelectList(MultiSelectListPreference item) {
            item.setOnPreferenceChangeListener(this);
            String initial = NavBarActivity.getSettingString(this.cr, item.getKey());
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
                item.setChecked(NavBarActivity.getSettingBoolean(this.cr, item.getKey()));
            }
        }

        private void initText(EditTextPreference item) {
            item.setOnPreferenceChangeListener(this);
            item.setText(NavBarActivity.getSettingString(this.cr, item.getKey()));
        }

        private void initCheckbox(CheckBoxPreference item) {
            if (item != null) {
                item.setOnPreferenceChangeListener(this);
                item.setChecked(NavBarActivity.getSettingBoolean(this.cr, item.getKey()));
            }
        }

        private void initList(ListPreference item) {
            item.setOnPreferenceChangeListener(this);
            item.setValue(NavBarActivity.getSettingString(this.cr, item.getKey()));
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
