package lumi.constellation.preference;

import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.AttributeSet;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;

import com.nhlong.lumi.constellation.R;
import androidx.picker.app.SeslColorPickerDialog;

import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import androidx.preference.internal.PreferenceImageView;

public class LumiColorPickerPreference extends Preference implements Preference.OnPreferenceClickListener,
        SeslColorPickerDialog.OnColorSetListener {

    PreferenceViewHolder mViewHolder;
    Dialog mDialog;
    PreferenceImageView mPreview;
    private int mValue = Color.BLACK;
    private ArrayList<Integer> mUsedColors = new ArrayList();

    private long mLastClickTime;
    private boolean mAlphaSliderEnabled = false;

    public LumiColorPickerPreference(Context context) {
        this(context, null);
    }

    public LumiColorPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public LumiColorPickerPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public static int convertToColorInt(String argb) throws IllegalArgumentException {
        if (!argb.startsWith("#")) {
            argb = "#" + argb;
        }

        return Color.parseColor(argb);
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        int colorInt;
        String mHexDefaultValue = a.getString(index);
        if (mHexDefaultValue != null && mHexDefaultValue.startsWith("#")) {
            colorInt = convertToColorInt(mHexDefaultValue);
            return colorInt;
        } else {
            return a.getColor(index, Color.BLACK);
        }
    }

    @Override
    protected void onSetInitialValue(Object defaultValue) {
        onColorSet(defaultValue == null ? getPersistedInt(mValue) : (Integer) defaultValue);
    }

    private void init(Context context, AttributeSet attrs) {
        setWidgetLayoutResource(R.layout.sesl_color_picker_preference_widget);

        setOnPreferenceClickListener(this);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LumiCommonColorPickerPreference);
        mAlphaSliderEnabled = a.getBoolean(R.styleable.LumiCommonColorPickerPreference_showAlphaSlider, false);
        a.recycle();
    }

    @Override
    public void onBindViewHolder(@NonNull PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        mViewHolder = holder;
        mPreview = (PreferenceImageView) holder.findViewById(R.id.imageview_widget);
        setPreviewColor();
    }

    private void setPreviewColor() {
        if (mPreview == null) {
            return;
        }

        GradientDrawable drawable = (GradientDrawable) getContext().getDrawable(R.drawable.sesl_color_picker_preference_preview).mutate();
        drawable.setColor(mValue);

        mPreview.setBackground(drawable);
    }

    @Override
    public void onColorSet(int color) {
        if (isPersistent()) {
            persistInt(color);
        }
        mValue = color;

        callChangeListener(color);
        addRecentColor(color);
        setPreviewColor();
    }

    @Override
    public boolean onPreferenceClick(@NonNull Preference preference) {
        long uptimeMillis = SystemClock.uptimeMillis();
        if (uptimeMillis - mLastClickTime > 600L) {
            showDialog(null);
        }
        mLastClickTime = uptimeMillis;
        return false;
    }

    private void showDialog(Bundle state) {
        SeslColorPickerDialog dialog = new SeslColorPickerDialog(getContext(), this, mValue, getRecentColors());
        dialog.setNewColor(mValue);
        dialog.setTransparencyControlEnabled(mAlphaSliderEnabled);
        if (state != null)
            dialog.onRestoreInstanceState(state);
        dialog.show();

        mDialog = dialog;
    }

    public void setAlphaSliderEnabled(boolean enable) {
        mAlphaSliderEnabled = enable;
    }

    private void addRecentColor(int color) {
        for (int i = 0; i < mUsedColors.size(); i++) {
            if (mUsedColors.get(i) == color)
                mUsedColors.remove(i);
        }

        if (mUsedColors.size() > 5) {
            mUsedColors.remove(0);
        }

        mUsedColors.add(color);
    }

    private int[] getRecentColors() {
        int[] usedColors = new int[mUsedColors.size()];
        ArrayList<Integer> reverseUsedColor = new ArrayList<>(mUsedColors);

        Collections.reverse(reverseUsedColor);

        for (int i = 0; i < reverseUsedColor.size(); i++) {
            usedColors[i] = reverseUsedColor.get(i);
        }
        return usedColors;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        if (mDialog == null || !mDialog.isShowing()) {
            return superState;
        }

        final SavedState myState = new SavedState(superState);
        myState.dialogBundle = mDialog.onSaveInstanceState();
        return myState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state == null || !(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState myState = (SavedState) state;
        super.onRestoreInstanceState(myState.getSuperState());
        showDialog(myState.dialogBundle);
    }

    public String toString(ArrayList<Integer> arrList) {
        int n = arrList.size();
        if (n == 0)
            return "0";

        String str = "";
        for (int i = arrList.size() - 1; i > 0; i--) {
            int nums = arrList.get(i);
            str += nums;
        }

        return str;
    }


    private static class SavedState extends BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
        Bundle dialogBundle;

        public SavedState(Parcel source) {
            super(source);
            dialogBundle = source.readBundle();
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeBundle(dialogBundle);
        }
    }
}