package lumi.constellation.preference;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.ArrayRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.util.SeslMisc;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.TypedArrayUtils;

import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;

import com.nhlong.lumi.constellation.R;
import lumi.constellation.preference.internal.LumiVerticalRadioViewContainer;

public class LumiVerticalRadioPreference extends Preference {
    private static final String TAG = "LumiVerticalRadioPreference";
    private static final int IMAGE = 0;
    private static final int NO_IMAGE = 1;

    private boolean mIsColorFilterEnabled = false;
    private boolean mIsDividerEnabled = false;
    private boolean mIsEnabled = true;
    private boolean mIsTouchEffectEnabled = false;
    private int mType;
    private int[] mEntriesImage;
    private CharSequence[] mEntries;
    private CharSequence[] mEntriesSubTitle;
    private CharSequence[] mEntryValues;
    private String mValue;
    private boolean mValueSet;

    private LumiVerticalRadioViewContainer mContainerLayout;
    private int mSelectedColor;
    private int mUnselectedColor;


    @SuppressLint({"RestrictedApi", "LongLogTag"})
    public LumiVerticalRadioPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LumiHorizontalRadioPreference);

        mType = a.getInt(R.styleable.LumiHorizontalRadioPreference_viewType, IMAGE);

        mEntries = TypedArrayUtils.getTextArray(a, R.styleable.LumiHorizontalRadioPreference_entries, 0);
        mEntryValues = TypedArrayUtils.getTextArray(a, R.styleable.LumiHorizontalRadioPreference_entryValues, 0);

        if (mType == IMAGE) {
            int entriesImageResId = a.getResourceId(R.styleable.LumiHorizontalRadioPreference_entriesImage, 0);
            if (entriesImageResId != 0) {
                TypedArray a2 = context.getResources().obtainTypedArray(entriesImageResId);
                mEntriesImage = new int[a2.length()];
                for (int i = 0; i < a2.length(); i++) {
                    mEntriesImage[i] = a2.getResourceId(i, 0);
                }
                a2.recycle();
            }
        } else if (mType == NO_IMAGE)
            mEntriesSubTitle = TypedArrayUtils.getTextArray(a, R.styleable.LumiHorizontalRadioPreference_entriesSubtitle, 0);

        a.recycle();

        setSelectable(false);

        TypedValue color = new TypedValue();
        context.getTheme().resolveAttribute(SeslMisc.isLightTheme(context) ? R.attr.colorPrimary : R.attr.colorSecondary, color, true);
        mSelectedColor = color.data;
        mUnselectedColor = ContextCompat.getColor(getContext(), R.color.widget_multi_button_unselected_icon_color);

        setLayoutResource(R.layout.lumi_vertical_radio_preference_layout);
    }

    @Override
    @SuppressLint("LongLogTag")
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);

        int size = mEntryValues.length;
        if (size > 2) Log.e(TAG + ".onBindViewHolder", "Max mEntryValues suggested is 3");
        mContainerLayout = holder.itemView.findViewById(R.id.radio_layout_vertical);
        mContainerLayout.setDividerEnabled(mIsDividerEnabled);

        mContainerLayout.removeAllViews();

        int i = 0;
        for (CharSequence str : mEntryValues) {
            ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.lumi_vertical_radio_preference_item, mContainerLayout);

            ViewGroup viewGroup = (ViewGroup) mContainerLayout.getChildAt(i);

            if (mType == NO_IMAGE /* noImage */) {
                ((TextView) viewGroup.findViewById(R.id.title_vertical)).setText(mEntries[i]);
                ((TextView) viewGroup.findViewById(R.id.sub_title_vertical)).setText(mEntriesSubTitle[i]);
                viewGroup.findViewById(R.id.text_frame_vertical).setVisibility(View.VISIBLE);
            } else if (mType == IMAGE /* image */) {
                ((ImageView) viewGroup.findViewById(R.id.icon_vertical)).setImageResource(mEntriesImage[i]);
                ((TextView) viewGroup.findViewById(R.id.icon_title_vertical)).setText(mEntries[i]);
                viewGroup.findViewById(R.id.image_frame_vertical).setVisibility(View.VISIBLE);
            }

            viewGroup.setVisibility(View.VISIBLE);
            if (!mIsTouchEffectEnabled) {
                viewGroup.setBackground(null);
            }

            viewGroup.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            if (!mIsTouchEffectEnabled)
                                v.setAlpha(0.6f);
                            return false;
                        case MotionEvent.ACTION_UP:
                            if (!mIsTouchEffectEnabled)
                                v.setAlpha(1.0f);
                            v.callOnClick();
                            return false;
                        case MotionEvent.ACTION_CANCEL:
                            if (!mIsTouchEffectEnabled)
                                v.setAlpha(1.0f);
                            return false;
                        default:
                            return false;
                    }
                }
            });
            viewGroup.setOnKeyListener((v, keyCode, event) -> {
                if (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_NUMPAD_ENTER || keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
                    switch (event.getAction()) {
                        case KeyEvent.ACTION_DOWN:
                            if (!mIsTouchEffectEnabled)
                                v.setAlpha(0.6f);
                            return true;
                        case KeyEvent.ACTION_UP:
                            if (!mIsTouchEffectEnabled)
                                v.setAlpha(1.0f);
                            v.playSoundEffect(SoundEffectConstants.CLICK);
                            v.callOnClick();
                    }
                }
                return false;
            });
            viewGroup.setOnClickListener(view -> {
                setValue((String) str);
                callChangeListener((String) str);
            });

            i++;
        }
        invalidate();
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getString(index);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        if (isPersistent()) {
            return superState;
        }

        final SavedState myState = new SavedState(superState);
        myState.value = getValue();
        return myState;
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        setValue(restoreValue ? getPersistedString(mValue) : (String) defaultValue);
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state == null || !state.getClass().equals(SavedState.class)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState myState = (SavedState) state;
        super.onRestoreInstanceState(myState.getSuperState());
        setValue(myState.value);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        mIsEnabled = enabled;
        invalidate();
    }

    public void setViewType(int type) {
        mType = type;
    }

    public CharSequence getEntry() {
        int index = getValueIndex();
        return index >= 0 && mEntries != null ? mEntries[index] : null;
    }

    public String getValue() {
        return mValue;
    }

    public void setValue(String value) {
        final boolean changed = !TextUtils.equals(mValue, value);
        if (changed || !mValueSet) {
            mValue = value;
            mValueSet = true;
            persistString(value);
            if (changed) {
                notifyChanged();
                invalidate();
            }
        }
    }

    private int getValueIndex() {
        return findIndexOfValue(mValue);
    }

    public void setDividerEnabled(boolean enabled) {
        mIsDividerEnabled = enabled;
    }

    public void setColorFilterEnabled(boolean enabled) {
        mIsColorFilterEnabled = enabled;
    }

    public void setTouchEffectEnabled(boolean enabled) {
        mIsTouchEffectEnabled = enabled;
    }

    public int findIndexOfValue(String value) {
        if (value != null && mEntryValues != null) {
            for (int i = mEntryValues.length - 1; i >= 0; i--) {
                if (mEntryValues[i].equals(value)) {
                    return i;
                }
            }
        }
        return -1;
    }

    @SuppressLint("WrongConstant")
    private void invalidate() {
        int i = 0;
        for (CharSequence str : mEntryValues) {
            if (mContainerLayout == null) break;

            ViewGroup viewGroup = (ViewGroup) mContainerLayout.getChildAt(i);

            RadioButton radioButton = ((RadioButton) viewGroup.findViewById(R.id.radio_button_vertical));
            TextView tv1 = null;
            TextView tv2 = null;
            ImageView imageView = null;
            if (mType == 1 /* noImage */) {
                tv1 = ((TextView) viewGroup.findViewById(R.id.title_vertical));
                tv2 = ((TextView) viewGroup.findViewById(R.id.sub_title_vertical));
                viewGroup.findViewById(R.id.text_frame_vertical).setVisibility(View.VISIBLE);
            } else if (mType == 0 /* image */) {
                imageView = ((ImageView) viewGroup.findViewById(R.id.icon_vertical));
                tv1 = ((TextView) viewGroup.findViewById(R.id.icon_title_vertical));
                viewGroup.findViewById(R.id.image_frame_vertical).setVisibility(View.VISIBLE);
            }

            boolean z = mValue.equals(str);
            radioButton.setChecked(z);
            if (!mIsDividerEnabled)
                radioButton.jumpDrawablesToCurrentState();
            tv1.setSelected(z);
            tv1.setTypeface(Typeface.create("sec-roboto-light", z ? 1 : 0));
            if (tv2 != null) {
                tv2.setSelected(z);
                tv2.setTypeface(Typeface.create("sec-roboto-light", z ? 1 : 0));
            }
            if (mIsColorFilterEnabled && imageView != null) {
                imageView.setColorFilter(z ? mSelectedColor : mUnselectedColor);
            }

            viewGroup.setEnabled(mIsEnabled);
            viewGroup.setAlpha(mIsEnabled ? 1.0f : 0.6f);

            i++;
        }
    }


    private static class SavedState extends BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
                    @Override
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    @Override
                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
        String value;

        public SavedState(Parcel source) {
            super(source);
            value = source.readString();
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeString(value);
        }
    }
}